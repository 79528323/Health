package com.gzhealthy.health.activity.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.HealthBodyInfoActivity;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告
 */
public class HealthyReportActivity extends BaseAct {
    @BindView(R.id.btn_file)
    LinearLayout btn_file;

    @BindView(R.id.linear_question)
    LinearLayout linear_question;

    @BindView(R.id.btn_data)
    LinearLayout btn_data;

    @BindView(R.id.btn_question)
    TextView btn_question;

    @BindView(R.id.report_history)
    TextView tv_report_history;

//    @BindView(R.id.report_get)
//    TextView tv_report_get;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mImmersionBar.statusBarColor(R.color.app_btn_parimary).statusBarDarkFont(false).init();
        setBarLeftIcon(R.mipmap.icon_white_back);
        setBarBackgroundColor(R.color.app_btn_parimary);
        setTitle("健康报告");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this,R.color.app_btn_parimary));
        getCenterTextView().setTextColor(ContextCompat.getColor(this,R.color.white));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

//        tv_report_get.setEnabled(false);
//        isGetReport();
        setOnClickListener(R.id.btn_data);
        setOnClickListener(R.id.btn_question);
        setOnClickListener(R.id.report_history);
        setOnClickListener(R.id.btn_file);
//        setOnClickListener(R.id.report_get);
        setOnClickListener(R.id.linear_question);

//        rxManager.onRxEvent(RxEvent.ON_REFRESH_REPORT_BUTTON_STATUS)
//                .subscribe((Action1<Object>) o -> {
//                                      isGetReport();
//                });
    }


    @Override
    protected void onResume() {
        RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_REPORT_BUTTON_STATUS);
        super.onResume();
    }

    public static void inStance(Context context){
        context.startActivity(new Intent(context,HealthyReportActivity.class));
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_data:
                HealthyReportBigDataActivity.inStance(this);
                break;

            case R.id.linear_question:
            case R.id.btn_question:
                HealthyReportQuestionActivity.inStance(this);
                break;

            case R.id.report_history:
                HealthyReportHistoryActivity.instance(this);
                break;

            case R.id.btn_file:
                HealthBodyInfoActivity.instance(this);
                break;

//            case R.id.report_get:
////                getReport(0);
//                HealthyReportLoadingActivity.inStance(this);
//                break;

        }
        super.onClick(view);
    }

//    /**
//     * 获取报告
//     * @param id
//     */
//    public void getReport(int id){
//        Map<String,String> param = new HashMap<>();
//        if (id > 0){
//            param.put("id",String.valueOf(id));//报告id，不传则返回最新的报告
//        }
////        param.put("type","0");//类型，0-自动生成，1-主动生成，默认为：0
//        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
//                new CallBack<HealthyReport>() {
//                    @Override
//                    public void onResponse(HealthyReport data) {
//                        if (data.code == 1){
//                            HealthyReportResultActivity.inStance(HealthyReportActivity.this,data);
//                        }else {
//                            ToastUtil.showToast(data.msg);
//                        }
//                    }
//                });
//    }

//    public void isGetReport(){
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isGetReport(SPCache.getString(SPCache.KEY_TOKEN,"")),
//                new CallBack<BaseModel<Boolean>>() {
//
//                    @Override
//                    public void onResponse(BaseModel<Boolean> model) {
//                        if (model.code == 1){
//                            tv_report_get.setEnabled(model.data);
//                            btn_question.setVisibility(model.data?View.GONE:View.VISIBLE);
//                        }
//                    }
//                });
//    }
}
