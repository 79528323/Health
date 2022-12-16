package com.gzhealthy.health.activity.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.HealthFileActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.HealthyEcgReport;
import com.gzhealthy.health.tool.HttpUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康报告
 */
public class HealthyTianReportActivity extends BaseAct {
    @BindView(R.id.ecg_report_frame)
    FrameLayout ecg_report_frame;
    @BindView(R.id.hrv_report_frame)
    FrameLayout hrv_report_frame;
    @BindView(R.id.health_report_frame)
    FrameLayout health_report_frame;
    @BindView(R.id.check_report_frame)
    FrameLayout check_report_frame;

    @BindView(R.id.back)
    LinearLayout back;
    String statisticUrl;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_tian_report;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        setBarLeftIcon(R.mipmap.login_back);
//        setBarBackgroundColor(R.color.app_btn_parimary);
//        setTitle("体安报告");
//        getToolBar().setBackgroundColor(ContextCompat.getColor(this,R.color.white));
//        getCenterTextView().setTextColor(ContextCompat.getColor(this,R.color.text_color_1));
//        getCenterTextView().setTextSize(18);
//        hideOrShowToolbar(false);

        hideOrShowToolbar(true);
        hideOrShowAppbar(true);
        mImmersionBar.transparentStatusBar().statusBarDarkFont(false).init();
        back.setOnClickListener(v -> {
            goBack();
        });
        getData();
        ecg_report_frame.setOnClickListener(this);
        hrv_report_frame.setOnClickListener(this);
        health_report_frame.setOnClickListener(this);
        check_report_frame.setOnClickListener(this);
//        initView();
//        getList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-体安报告");
        getData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-体安报告");
    }

    //    public void initView(){
//        page = 1;
//        history_reclyer.setLayoutManager(new LinearLayoutManager(this));
//        history_reclyer.addItemDecoration(new HeartRateDividerItemDecoration(this,20));
//        adapter = new HealthyReportAdapter(this, id -> {
//
//            getReport(id);
//        });
//        history_reclyer.setAdapter(adapter);
//
//        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
//            @Override
//            public void onLoadMore(RefreshLayout refreshLayout) {
//                page+=1;
//                getList();
//            }
//
//            @Override
//            public void onRefresh(RefreshLayout refreshLayout) {
//                page=1;
//                getList();
//            }
//        });
//    }
//
//    public void getList(){
//        Map<String,String> param = new HashMap<>();
//        param.put("page",String.valueOf(page));
//        param.put("size",String.valueOf(pageSize));
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportList(param,SPCache.getString(SPCache.KEY_TOKEN,"")),
//                new CallBack<HealthyReportHistory>() {
//
//                    @Override
//                    public void onResponse(HealthyReportHistory history) {
//                        if (history.code == 1){
//                            if (page == 1) {
//                                adapter.refreshData(history.data.list);
//                                history_reclyer.setVisibility(history.data.list.isEmpty()?View.GONE:View.VISIBLE);
//                            }else {
//                                adapter.appendData(history.data.list);
//                            }
//                        }
//
//                        resetSmartlayout();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        resetSmartlayout();
//                        super.onError(e);
//                    }
//                });
//    }
//
//
    public static void instance(Context context) {
        context.startActivity(new Intent(context, HealthyTianReportActivity.class));
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.health_report_frame:
                HealthyReportHistoryActivity.instance(this);
                break;

            case R.id.hrv_report_frame:
                HealthyHRVReportActivity.instance(this);
                break;

            case R.id.ecg_report_frame:
                if (!TextUtils.isEmpty(statisticUrl)) {
                    WebViewActivity.startLoadLink(this, statisticUrl, "心电报告");
                }
                break;
            case R.id.check_report_frame:
                startActivity(new Intent(this, HealthFileActivity.class));
                break;
        }
    }


    public void getData() {
        Map<String, String> param = new HashMap<>();
//        param.put(SPCache.KEY_TOKEN,SPCache.getString(SPCache.KEY_TOKEN,""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getHealthEcgReport(param),
                new CallBack<HealthyEcgReport>() {

                    @Override
                    public void onResponse(HealthyEcgReport data) {
                        if (data.code == 1)
                            HealthyTianReportActivity.this.statisticUrl = data.data.statisticUrl;
                    }
                });
    }

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
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
////                                setupReportView(data.data);
//                                HealthyReportResultActivity.inStance(HealthyTianReportActivity.this,data);
//                            }
//                        });
//                    }
//                });
//    }
//
//
//    public void resetSmartlayout(){
//        refreshLayout.finishLoadMore();
//        refreshLayout.finishRefresh();
//    }
}
