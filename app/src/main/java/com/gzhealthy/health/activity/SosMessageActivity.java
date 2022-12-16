package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.SosMessageAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.SosListModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 紧急消息
 */
public class SosMessageActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private SosMessageAdapter sosMessageAdapter = new SosMessageAdapter();

    @BindView(R.id.rvSos)
    RecyclerView rvSos;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mPage = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sos_message;
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, SosMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("紧急消息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        refreshLayout.setOnRefreshListener(this);
        sosMessageAdapter.setOnLoadMoreListener(this, rvSos);
        sosMessageAdapter.setOnItemChildClickListener(this);

        rvSos.setAdapter(sosMessageAdapter);

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

    public void getData(String page) {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString("token", ""));
        param.put("pageNum", page);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getSosList(param),
                new CallBack<SosListModel>() {

                    @Override
                    public void onResponse(SosListModel data) {
                        if (data.getCode() == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                sosMessageAdapter.setNewData(data.getData());
                            } else if (sosMessageAdapter.isLoading()) {
                                mPage++;
                                sosMessageAdapter.loadMoreComplete();
                                sosMessageAdapter.addData(data.getData());
                            } else {
                                mPage = 1;
                                sosMessageAdapter.setNewData(data.getData());
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (sosMessageAdapter.isLoading()) {
                                sosMessageAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (data.getData().isEmpty()) sosMessageAdapter.loadMoreEnd();
                        if (sosMessageAdapter.getData().isEmpty()) {
                            sosMessageAdapter.setEmptyView(R.layout.activity_sos_message_not_data, rvSos);
                        }
                    }
                });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.llLinearLayout) {
            SosListModel.DataModel dataModel = sosMessageAdapter.getItem(position);
            oneKeyRead(new int[]{dataModel.getId()}, dataModel, position);
        }
    }

    public void oneKeyRead(int[] idsArry, SosListModel.DataModel dataModel, int position) {
        Map<String, Object> param = new HashMap<>();
        param.put("ids", idsArry);
        param.put("version", BuildConfig.VERSION_NAME);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));

        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().setRead(body),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        Intent intent = new Intent(SosMessageActivity.this, SosMessageDetailsActivity.class);
                        intent.putExtra(IntentParam.DATA, dataModel);
                        startActivity(intent);

                        dataModel.setIfRead(1);
                        sosMessageAdapter.notifyItemChanged(position);
                        RxBus.getInstance().postEmpty(RxEvent.REFRESH_MESSAGE_BDAGE);
                    }
                });
    }
}
