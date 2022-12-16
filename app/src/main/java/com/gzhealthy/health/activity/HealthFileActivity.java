package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.HealthFileAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.HealthRecordModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HealthFileActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {

    private int mPage = 1;

    private HealthFileAdapter mHealthFileAdapter = new HealthFileAdapter();

    @BindView(R.id.rvHealthFile)
    RecyclerView rvHealthFile;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getView() {
        return super.getView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_file;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("无创检测报告");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        refreshLayout.setOnRefreshListener(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(5));
        View header = new View(this);
        header.setLayoutParams(params);
        mHealthFileAdapter.addHeaderView(header);
        mHealthFileAdapter.setOnLoadMoreListener(this, rvHealthFile);
        mHealthFileAdapter.setOnItemClickListener(this);

        rvHealthFile.setAdapter(mHealthFileAdapter);

        getData("1");
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        getData("1");
    }

    @Override
    public void onLoadMoreRequested() {
        getData(String.valueOf(++mPage));
    }


    private void getData(String page) {
        Map<String, String> param = new HashMap<>();
        param.put("page", page);
        param.put("size", "10");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getHealthRecord(param),
                new CallBack<HealthRecordModel>() {

                    @Override
                    public void onResponse(HealthRecordModel data) {
                        if (data.getCode() == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                mHealthFileAdapter.setNewData(data.getData());
                            } else if (mHealthFileAdapter.isLoading()) {
                                mPage++;
                                mHealthFileAdapter.loadMoreComplete();
                                mHealthFileAdapter.addData(data.getData());
                            } else {
                                mPage = 1;
                                mHealthFileAdapter.setNewData(data.getData());
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (mHealthFileAdapter.isLoading()) {
                                mHealthFileAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (data.getData().isEmpty()) mHealthFileAdapter.loadMoreEnd();
                        if (mHealthFileAdapter.getData().isEmpty()) {
                            mHealthFileAdapter.setEmptyView(R.layout.activity_health_file_not_data, rvHealthFile);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        HealthRecordModel.DataBean bean = mHealthFileAdapter.getItem(position);
        WebViewActivity.startLoadLink(this,bean.getReportUrl(),"");
    }

}

