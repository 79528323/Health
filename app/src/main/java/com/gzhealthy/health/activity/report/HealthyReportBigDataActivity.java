package com.gzhealthy.health.activity.report;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
//import com.gzhealthy.health.activity.ECGHtmlActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.HealthyStatistics;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.DispUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告大数据
 */
public class HealthyReportBigDataActivity extends BaseAct {
    @BindView(R.id.high_blood)
    TextView tv_high_blood;

    @BindView(R.id.low_blood)
    TextView tv_low_blood;

    @BindView(R.id.high_rate)
    TextView tv_high_rate;

    @BindView(R.id.low_rate)
    TextView tv_low_rate;

    @BindView(R.id.days)
    TextView tv_days;

    @BindView(R.id.avg_rate)
    TextView tv_avg_rate;

    @BindView(R.id.avg_pressure)
    TextView tv_avg_pressure;

    @BindView(R.id.wear_day)
    TextView tv_wear_day;

    @BindView(R.id.amount)
    TextView tv_amount;

    @BindView(R.id.ecg)
    TextView tv_ecg;

    @BindView(R.id.normal)
    TextView tv_normal;

    @BindView(R.id.exception)
    TextView tv_exception;

    @BindView(R.id.ecg_linear)
    LinearLayout ecg_linear;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report_big_data;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("健康大数据");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-健康数据");
        getData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-健康数据");
    }

    public void initView(){
        getData();
    }


    public void getData(){
        Map<String,String> param = new HashMap<>();
        param.put(SPCache.KEY_TOKEN,SPCache.getString(SPCache.KEY_TOKEN,""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().healthDataStatistic(param),
                new CallBack<HealthyStatistics>() {

                    @Override
                    public void onResponse(HealthyStatistics data) {
                        convertDays(data.data);
                        bloods(data.data);
                        rates(data.data);
                        ecgs(data.data);
//                        if (!TextUtils.isEmpty(data.data.statisticUrl)){
//                            ecg_linear.setOnClickListener(v -> {
//                                startActivity(
//                                        new Intent(HealthyReportBigDataActivity.this, ECGHtmlActivity.class).putExtra("url",data.data.statisticUrl));
//                            });
//                        }
                    }
                });
    }


    public void convertDays(HealthyStatistics.DataBean data){
        String day = getString(R.string.big_data_title,data.serviceDays);
        SpannableString spannableString = new SpannableString(day);
        spannableString.setSpan(new AbsoluteSizeSpan(DispUtil.sp2px(this,24)),day.lastIndexOf("有")+1,day.lastIndexOf("天"), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        tv_days.setText(spannableString);

        tv_amount.setText(String.valueOf(data.amount));
        tv_wear_day.setText(String.valueOf(data.wearDays));
    }


    public void bloods(HealthyStatistics.DataBean data){
        tv_avg_pressure.setText(data.avgHigh+"/"+data.avgLow);
        tv_high_blood.setText(getString(R.string.big_data_blood,data.highestHigh,data.highestLow));
        tv_low_blood.setText(getString(R.string.big_data_blood,data.lowestHigh,data.lowestLow));
    }

    public void rates(HealthyStatistics.DataBean data){
        tv_avg_rate.setText(String.valueOf(data.avgRate));
        tv_high_rate.setText(getString(R.string.big_data_rate,data.highestRate));
        tv_low_rate.setText(getString(R.string.big_data_rate,data.lowestRate));
    }

    public void ecgs(HealthyStatistics.DataBean data){
        tv_ecg.setText(String.valueOf(data.ecgTimes));
        tv_normal.setText(String.valueOf(data.ecgNormalNum)+"次");
        tv_exception.setText(String.valueOf(data.ecgAbnormalNum)+"次");
    }


    public static void inStance(Context context){
        context.startActivity(new Intent(context, HealthyReportBigDataActivity.class));
    }
}
