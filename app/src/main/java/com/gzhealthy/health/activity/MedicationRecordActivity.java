package com.gzhealthy.health.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.MedicationRecordAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.MedicationRecordModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MedicationRecordActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {
    private int mPage = 1;

    private MedicationRecordAdapter mMedicationRecordAdapter = new MedicationRecordAdapter();

    @BindView(R.id.rvMedicationRecord)
    RecyclerView rvMedicationRecord;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_medication_record;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("用药记录");
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
        mMedicationRecordAdapter.addHeaderView(header);
        mMedicationRecordAdapter.setOnLoadMoreListener(this, rvMedicationRecord);
        mMedicationRecordAdapter.setOnItemClickListener(this);
        mMedicationRecordAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            MedicationRecordModel.DataBean bean = mMedicationRecordAdapter.getItem(position);
            Intent intent = new Intent(this,MedicationAddActivity.class);
            intent.putExtra(IntentParam.ID, Integer.parseInt(bean.getId()));
            startActivity(intent);
        });

        rvMedicationRecord.setAdapter(mMedicationRecordAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                InsuranceApiFactory.getmHomeApi().getMedicationRecord(param),
                new CallBack<MedicationRecordModel>() {

                    @Override
                    public void onResponse(MedicationRecordModel data) {
                        if (data.getCode() == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                mMedicationRecordAdapter.setNewData(data.getData());
                            } else if (mMedicationRecordAdapter.isLoading()) {
                                mPage++;
                                mMedicationRecordAdapter.loadMoreComplete();
                                mMedicationRecordAdapter.addData(data.getData());
                            } else {
                                mPage = 1;
                                mMedicationRecordAdapter.setNewData(data.getData());
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (mMedicationRecordAdapter.isLoading()) {
                                mMedicationRecordAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (data.getData().isEmpty()) {
                            mMedicationRecordAdapter.loadMoreEnd();
                        }
                        if (mMedicationRecordAdapter.getData().isEmpty()) {
                            mMedicationRecordAdapter.setEmptyView(R.layout.activity_medication_record_not_data, rvMedicationRecord);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        MedicationRecordModel.DataBean bean = mMedicationRecordAdapter.getItem(position);
        Intent intent = new Intent(this,MedicationDetailsActivity.class);
        intent.putExtra(IntentParam.ID, bean.getId());
        startActivity(intent);
    }

    @OnClick(R.id.btAdd)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btAdd://新增用药记录
                startActivity(new Intent(this, MedicationAddActivity.class));
                break;
        }

    }
}
