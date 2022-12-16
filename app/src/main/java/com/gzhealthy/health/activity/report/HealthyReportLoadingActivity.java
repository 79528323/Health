package com.gzhealthy.health.activity.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告
 */
public class HealthyReportLoadingActivity extends BaseAct {
    @BindView(R.id.desc)
    TextView tv_desc;

    Timer timer;

    int count = 0;//计数

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 0){
                tv_desc.setText((String)msg.obj);
            }else {
                HealthyReportResultActivity.inStance(HealthyReportLoadingActivity.this, (HealthyReport) msg.obj);
                finish();
            }
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report_loading;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(1f).statusBarDarkFont(true).init();
        setBarLeftIcon(R.mipmap.login_back);
        setBarBackgroundColor(R.color.white);
        setTitle("健康报告");
//        getToolBar().setBackgroundColor(ContextCompat.getColor(this,R.color.text_color_1));
        getCenterTextView().setTextColor(ContextCompat.getColor(this,R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        setDescAnimation();
        isGetReport();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (timer !=null){
            timer.purge();
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    //    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_data:
//                HealthyReportBigDataActivity.inStance(this);
//                break;
//
//            case R.id.linear_question:
//            case R.id.btn_question:
//                HealthyReportQuestionActivity.inStance(this);
//                break;
//
//            case R.id.report_history:
//                HealthyReportHistoryActivity.inStance(this);
//                break;
//
//            case R.id.btn_file:
//                HealthFileActivity.instance(this);
//                break;
//
//            case R.id.report_get:
//                getReport(0);
//                break;
//
//        }
//        super.onClick(view);
//    }


    /**
     * 获取报告
     */
    public void getReport(){
        Map<String,String> param = new HashMap<>();
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
                new CallBack<HealthyReport>() {
                    @Override
                    public void onResponse(HealthyReport data) {
                        if (data.code == 1){
                            handler.sendMessageDelayed(handler.obtainMessage(1,data),700);
                        }else {
                            ToastUtil.showToast(data.msg);
                            finish();
                        }
                    }
                });
    }

    public void isGetReport(){
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isGetReport(
                SPCache.getString(SPCache.KEY_TOKEN,"")),
                new CallBack<BaseModel<Boolean>>() {

                    @Override
                    public void onResponse(BaseModel<Boolean> model) {
                        if (model.code != 1){
                            ToastUtil.showToast(model.msg);
                            goBack();
                        }else {
                            if (!model.data){
                                ToastUtil.showToast(model.msg);
                            }else {
                                getReport();
                            }
                        }
                    }
                });
    }


    /**
     * 设置点点倒数效果
     */
    public void setDescAnimation(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (count >= 3){
                    count = 0;
                }else {
                    count += 1;
                }

                String desc = "";
                for (int i=0; i  < count; i++){
                    desc += ".";
                }
                handler.sendMessage(handler.obtainMessage(0,desc));
            }
        };
        timer.schedule(task,300,300);
    }


    public static void inStance(Context context){
        context.startActivity(new Intent(context, HealthyReportLoadingActivity.class));
    }

}
