package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.DiseaseRecordAdapter;
import com.gzhealthy.health.adapter.MedicationRecordAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.DiseaseRecord;
import com.gzhealthy.health.model.MedicationRecordModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class DiseaseRecordActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {
    private int mPage = 1;

    private DiseaseRecordAdapter mDiseaseRecordAdapter = new DiseaseRecordAdapter();

    @BindView(R.id.rvDiseaseRecord)
    RecyclerView rvDiseaseRecord;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_disease_record;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("病历记录");
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
        mDiseaseRecordAdapter.addHeaderView(header);
        mDiseaseRecordAdapter.setOnLoadMoreListener(this, rvDiseaseRecord);
        mDiseaseRecordAdapter.setOnItemClickListener(this);
        mDiseaseRecordAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            DiseaseRecord.DataBean bean = mDiseaseRecordAdapter.getItem(position);
            Intent intent = new Intent(this,DiseaseAddActivity.class);
            intent.putExtra(IntentParam.ID, bean.id);
            startActivity(intent);
        });

        rvDiseaseRecord.setAdapter(mDiseaseRecordAdapter);
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
                InsuranceApiFactory.getmHomeApi().getDiseaseRecordList(param),
                new CallBack<DiseaseRecord>() {

                    @Override
                    public void onResponse(DiseaseRecord data) {
                        if (data.code == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                mDiseaseRecordAdapter.setNewData(data.data);
                            } else if (mDiseaseRecordAdapter.isLoading()) {
                                mPage++;
                                mDiseaseRecordAdapter.loadMoreComplete();
                                mDiseaseRecordAdapter.addData(data.data);
                            } else {
                                mPage = 1;
                                mDiseaseRecordAdapter.setNewData(data.data);
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (mDiseaseRecordAdapter.isLoading()) {
                                mDiseaseRecordAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.msg);
                        }
                        if (data.data.isEmpty()) {
                            mDiseaseRecordAdapter.loadMoreEnd();
                        }
                        if (mDiseaseRecordAdapter.getData().isEmpty()) {
                            mDiseaseRecordAdapter.setEmptyView(R.layout.activity_disease_record_not_data, rvDiseaseRecord);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DiseaseRecord.DataBean bean = mDiseaseRecordAdapter.getItem(position);
        Intent intent = new Intent(this,DiseaseDetailsActivity.class);
        intent.putExtra(IntentParam.ID, bean.id);
        startActivity(intent);
    }

    @OnClick(R.id.btAdd)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btAdd://新增病例记录
                startActivity(new Intent(this, DiseaseAddActivity.class));
                break;
        }
    }


    public static void instance(Context context){
        Intent intent = new Intent(context,DiseaseRecordActivity.class);
        context.startActivity(intent);
    }
}
