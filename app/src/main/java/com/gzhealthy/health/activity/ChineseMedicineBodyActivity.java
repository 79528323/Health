package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.ChineseMedicineBodyAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.ChineseMedicineBodyModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class ChineseMedicineBodyActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    private int mPage = 1;

    private ChineseMedicineBodyAdapter mChineseMedicineBodyAdapter = new ChineseMedicineBodyAdapter();

    @BindView(R.id.rvChineseMedicineBody)
    RecyclerView rvChineseMedicineBody;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_chinese_medicine_body;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("中医体质");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        refreshLayout.setOnRefreshListener(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(5));
        View header = new View(this);
        header.setLayoutParams(params);
        mChineseMedicineBodyAdapter.addHeaderView(header);
        mChineseMedicineBodyAdapter.setOnLoadMoreListener(this, rvChineseMedicineBody);

        rvChineseMedicineBody.setAdapter(mChineseMedicineBodyAdapter);

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
                InsuranceApiFactory.getmHomeApi().getChineseMedicineBody(param),
                new CallBack<ChineseMedicineBodyModel>() {

                    @Override
                    public void onResponse(ChineseMedicineBodyModel data) {
                        if (data.getCode() == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                mChineseMedicineBodyAdapter.setNewData(data.getData());
                            } else if (mChineseMedicineBodyAdapter.isLoading()) {
                                mPage++;
                                mChineseMedicineBodyAdapter.loadMoreComplete();
                                mChineseMedicineBodyAdapter.addData(data.getData());
                            } else {
                                mPage = 1;
                                mChineseMedicineBodyAdapter.setNewData(data.getData());
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (mChineseMedicineBodyAdapter.isLoading()) {
                                mChineseMedicineBodyAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (data.getData().isEmpty()) mChineseMedicineBodyAdapter.loadMoreEnd();
                    }
                });
    }
}
