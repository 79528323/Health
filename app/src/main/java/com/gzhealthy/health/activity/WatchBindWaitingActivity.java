package com.gzhealthy.health.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.BindType;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.push.MyJPushMessageReceiver;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

import static com.gzhealthy.health.push.MyJPushMessageReceiver.CONTENT_TYPE_REFUSE_BIND;
import static com.gzhealthy.health.push.MyJPushMessageReceiver.CONTENT_TYPE_UNTIE_BIND_CONDITION;
import static com.gzhealthy.health.push.MyJPushMessageReceiver.CONTENT_TYPE_WATCHBIND;


public class WatchBindWaitingActivity extends BaseAct {
    /**
     * 首次绑定
     */
    private static final int BIND_SUCCESS = 0x10001;
    /**
     * 手表绑定拒绝
     */
    private static final int BIND_REFUSE = 0x10002;
    /**
     * 首次后的绑定
     */
    private static final int BIND_CONDITION = 0x10003;
    /**
     * 解绑
     */
    private static final int BIND_UNBIND = 0x10004;


    @BindView(R.id.imei)
    TextView tv_imei;

    @BindView(R.id.tips)
    TextView tv_tips;

    @BindView(R.id.progress)
    ImageView iv_progress;

    String imei;

    Handler handler;

//    boolean isLoop = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_watch_bind_waiting;
    }


    public static void instance(Context context,String IMEI) {
        Intent intent = new Intent(context, WatchBindWaitingActivity.class);
        intent.putExtra("IMEI",IMEI);
        context.startActivity(intent);
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("设备确认绑定");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(WatchBindWaitingActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        handler = new Handler(Looper.getMainLooper());
        imei = getIntent().getStringExtra(IntentParam.IMEI);
        indeterminateProgress(iv_progress);
        onSubscribe();
        tv_imei.setText(imei);
        watchBindApply(imei);
    }


    public void onSubscribe(){
        rxManager.onRxEvent(RxEvent.WATCH_BIND_AND_BIND_PERSON_INFO)
                .subscribe((Action1<Object>) o -> {
                    handler.removeCallbacks(runnable);
                    showUnBindDailog(getString(R.string.bindinfo_toast),BIND_SUCCESS);
                    RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
                });

        rxManager.onRxEvent(RxEvent.WATCH_BIND_REFUSE)
                .subscribe(o ->{
                    handler.removeCallbacks(runnable);
                    showUnBindDailog(getString(R.string.watch_refuse_toast),BIND_REFUSE);
                });

        rxManager.onRxEvent(RxEvent.WATCH_BIND_UNTIE_BIND_CONDITION)
                .subscribe(o ->{
                    ToastUtil.showToast(getString(R.string.watch_bind_ok_toast));
                    RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
                    finish();
                });
    }



    @Override
    public void setState(int state) {
        if (state == Constants.ResponseStatus.STATE_EMPTY){
        }
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        super.setState(state);
    }


    public void watchBindApply(String IMEI){
        Map<String,String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei",IMEI);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().watchBindApply(params),
                new CallBack<BaseModel>() {

            @Override
            public void onResponse(BaseModel data) {
                tv_tips.setText(data.msg);
                if (data.code != 1){
                    iv_progress.setVisibility(View.GONE);
                }else {
                    handler.post(runnable);
                }
            }
        });
    }


    /**
     * 获取绑定状态
     * @param IMEI
     */
    public void getBindType(String IMEI){
        Map<String,String> params = new HashMap<>();
        params.put("token", SPCache.getString("token", ""));
        params.put("imei",IMEI);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getWatchBindType(params),
                new CallBack<BindType>() {
            @Override
            public void onResponse(BindType data) {
//                tv_tips.setText(data.msg);
                if (data.code == 1){
                    if (data.data.content_type.equals(CONTENT_TYPE_WATCHBIND)){
                        //手表绑定
                        RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_AND_BIND_PERSON_INFO);
                    }else if (data.data.content_type.equals(CONTENT_TYPE_REFUSE_BIND)){
                        //手表端拒绝绑定
                        RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_REFUSE);
                    }else if (data.data.content_type.equals(CONTENT_TYPE_UNTIE_BIND_CONDITION)){
                        //绑定手表而无须再进入个人信息绑定页面
                        RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_UNTIE_BIND_CONDITION);
                    }
                }else {
                    iv_progress.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                }
            }
        });
    }


    public void indeterminateProgress(ImageView imageView){
        // 第二个参数"rotation"表明要执行旋转
        // 0f -> 360f，从旋转360度，也可以是负值，负值即为逆时针旋转，正值是顺时针旋转。
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);

        // 动画的持续时间，执行多久？
        anim.setDuration(1200);

        // 回调监听
        anim.addUpdateListener(animation -> {
            float value = (Float) animation.getAnimatedValue();
        });

        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(99999999);
        // 正式开始启动执行动画
        anim.start();
    }




    private void showUnBindDailog(String s,int type){
        View  view = LayoutInflater.from(this).inflate(R.layout.dialog_unbind_watch,null);
        TextView title = view.findViewById(R.id.title);
        title.setText(s);

        TextView btn_cancel = view.findViewById(R.id.cancel);
        TextView btn_confirm = view.findViewById(R.id.confirm);

        if (type == BIND_REFUSE){
            btn_cancel.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("重新扫描");
        }else if (type == BIND_SUCCESS){
            btn_cancel.setVisibility(View.VISIBLE);
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("去填写");
        }else {
            btn_cancel.setVisibility(View.GONE);
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("确定");
        }

        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutView(view)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                            if (type == BIND_SUCCESS){
                                BindInfoActivity.instance(WatchBindWaitingActivity.this,"");
                                RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
                                finish();
                            }else if (type == BIND_REFUSE){
                                OperationBindScanActivity.startActivity(WatchBindWaitingActivity.this);
                                goBack();
//                                iv_progress.setVisibility(View.VISIBLE);
//                                handler.postDelayed(runnable,2 * 1000);
//                                watchBindApply(imei);
                            }else {
                                RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
                                finish();
                            }
                        });
                    }
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }


//    private class TimeHandler extends Handler{
//        WeakReference<AppCompatActivity> reference;
//
//        public TimeHandler(AppCompatActivity activity) {
//            reference = new WeakReference<AppCompatActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            if (reference.get()!=null){
//                getBindType(imei);
//            }
//        }
//    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getBindType(imei);
            handler.postDelayed(this,2 * 1000);
        }
    };

}