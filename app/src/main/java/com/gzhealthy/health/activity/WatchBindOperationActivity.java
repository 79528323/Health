package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
//import com.google.zxing.integration.android.IntentIntegrator;
//import com.google.zxing.integration.android.IntentResult;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BindWatchAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.callback.OnUnBindWatchCallBack;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.WatchDeviceModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.widget.decoration.WatchItemDecoration;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import rx.functions.Action1;

public class WatchBindOperationActivity extends BaseAct {
    private SwipeMenuRecyclerView swipeMenuRecyclerView;
    private BindWatchAdapter adapter;
//    private IntentIntegrator intentIntegrator;
//    private WatchBindCofirmCallback watchBindCofirmCallback;
    @BindView(R.id.device_add)
    RelativeLayout addDevice;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_watch_bind_oper;
    }


    public static void instance(Context context) {
        Intent intent = new Intent(context, WatchBindOperationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("我的设备");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(WatchBindOperationActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

//        intentIntegrator = new IntentIntegrator(this);
//        intentIntegrator.setCaptureActivity(QRCaptureActivity.class);
//        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);// 扫码的类型,可选：一维码，二维码，一/二维码
//        intentIntegrator.setPrompt("");// 设置提示语
//        intentIntegrator.setCameraId(0);// 选择摄像头,可使用前置或者后置
//        intentIntegrator.setBeepEnabled(true);// 是否开启声音,扫完码之后会"哔"的一声
//        intentIntegrator.setBarcodeImageEnabled(false);// 扫完码之后生成二维码的图片

        swipeMenuRecyclerView = $(R.id.swipeRecler);
        addDevice.setOnClickListener(this);

//        addDevice = $(R.id.device_add);

        swipeMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeMenuRecyclerView.setEnableTouchAlways(true);
        swipeMenuRecyclerView.addItemDecoration(new WatchItemDecoration(this));
        adapter = new BindWatchAdapter(this, new OnUnBindWatchCallBack() {
            @Override
            public void unBind() {
//                showUnBindDailog();
            }
        });
        swipeMenuRecyclerView.setAdapter(adapter);

//        watchBindCofirmCallback = new WatchBindCofirmCallback();
//        registerReceiver(watchBindCofirmCallback,new IntentFilter("com.gzhealthy.health.message"));

        rxManager.onRxEvent(RxEvent.WATCH_BIND_GET_WATCH)
                .subscribe((Action1<Object>) o -> getWatch());

        RxBus.getInstance().postEmpty(RxEvent.WATCH_BIND_GET_WATCH);
    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (JPushInterface.isPushStopped(this))
//            JPushInterface.resumePush(this);
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
//        if (result!=null){
//            String IMEI = result.getContents();
//            if (!TextUtils.isEmpty(IMEI)){
//                Logger.e("111","IMEI="+IMEI);
//                if (IMEI.startsWith("http")){
//                    IMEI = IMEI.substring(IMEI.indexOf("=")+1);
//                }
//
//                WatchBindWaitingActivity.instance(WatchBindOperationActivity.this,IMEI);
//            }
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-我的设备");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-我的设备");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.device_add:
                OperationBindScanActivity.startActivity(this);
                break;
        }
    }

    @Override
    public void setState(int state) {
        if (state == Constants.ResponseStatus.STATE_EMPTY){
        }
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        super.setState(state);
    }



    public void getWatch(){
        Map<String,String> params = new HashMap<>();
        params.put("token",SPCache.getString("token",""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getBindList(params), new CallBack<WatchDeviceModel>() {
            @Override
            public void onResponse(WatchDeviceModel data) {
                adapter.refreshData(data.data);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}