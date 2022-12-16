package com.gzhealthy.health.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.activity.report.HealthyReportQuestionActivity;
import com.gzhealthy.health.adapter.HealthNewReportAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.model.HealthyNewReport;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Utils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.decoration.HeartRateDividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.Map;

import rx.Subscription;

public class HealthReportTabFragment extends Fragment implements LifeSubscription, ResponseState {

    public static final String BUNDLE_TITLE = "title";
    //    private RxManager rxManager = new RxManager();
    View mContentView;
    //
//    private String titleTab;
    RecyclerView mRecyclerView;
    RefreshLayout refreshLayout;
    LinearLayout bottom_linear;
    TextView btn_que;
    int type = 1;
    int page;
    HealthNewReportAdapter adapter;

    public static HealthReportTabFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        HealthReportTabFragment fragment = new HealthReportTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 禁用系统字体大小改变应用文字大小
        DispUtil.disabledDisplayDpiChange(this.getResources());
        mContentView = inflater.inflate(R.layout.fragment_health_report_tab, container, false);
        initView();
        initData();
        getList();
        return mContentView;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", type);
        }
        mRecyclerView = mContentView.findViewById(R.id.mRecyclerView);
        refreshLayout = mContentView.findViewById(R.id.refreshLayout);
        bottom_linear = mContentView.findViewById(R.id.bottom_linear);
        btn_que = mContentView.findViewById(R.id.btn_que);
        page = 1;
    }

    private void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new HeartRateDividerItemDecoration(getActivity(), 20));
        adapter = new HealthNewReportAdapter(getActivity());
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            HealthyNewReport.DataBean bean = (HealthyNewReport.DataBean) adapter.getItem(position);
            WebViewActivity.startLoadLink(getContext(), bean.reportUrl, "健康报告");
        });
        mRecyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                page += 1;
                getList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                page = 1;
                getList();
            }
        });
        // 触发自动刷新
//        refreshLayout.autoRefresh();

        btn_que.setOnClickListener(v -> {
            Utils.gotoActy(getActivity(), HealthyReportQuestionActivity.class);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        bottom_linear.setVisibility(type > 2 ? View.GONE : View.VISIBLE);
    }


    public void getList() {
        Map<String, String> param = new HashMap<>();
        param.put("type", String.valueOf(type));
        param.put("page", String.valueOf(page));
        param.put("size", String.valueOf(10));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getHealthReport(param),
                new CallBack<HealthyNewReport>() {

                    @Override
                    public void onResponse(HealthyNewReport report) {
                        if (report.code == 1) {
                            if (page == 1) {
                                adapter.setNewData(report.data);
                                if (report.data.isEmpty()) {
                                    View layout = LayoutInflater.from(getActivity()).inflate(R.layout.activity_healthy_report_not_data, null);
                                    TextView tips = layout.findViewById(R.id.tips);
                                    TextView desc = layout.findViewById(R.id.desc);
                                    switch (type) {
                                        case 1:
                                            tips.setText("暂无健康日报");
                                            desc.setVisibility(View.GONE);
                                            break;
                                        case 2:
                                            tips.setText("暂无健康月报");
                                            desc.setVisibility(View.VISIBLE);
                                            desc.setText("每月初自动为您推送哦！");
                                            break;
                                        case 3:
                                            tips.setText("暂无健康季报");
                                            desc.setVisibility(View.VISIBLE);
                                            desc.setText("每季初自动为您推送哦！");
                                            break;
                                        case 4:
                                            tips.setText("暂无健康年报");
                                            desc.setVisibility(View.VISIBLE);
                                            desc.setText("每年初自动为您推送哦！");
                                            break;
                                    }
                                    adapter.setEmptyView(layout);
                                }
                            } else {
                                adapter.addData(report.data);
                            }
                        }

                        resetRefreshLayout(report.data.isEmpty());
                    }
                });
    }


    public void resetRefreshLayout(boolean isEnable) {
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(!isEnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
//        rxManager.clear();
        super.onDestroy();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
            Log.e("111", "titleTab == " + menuVisible);
        }
    }


    @Override
    public void bindSubscription(Subscription subscription) {

    }

    @Override
    public void setState(int state) {

    }

}






