package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.MyCardPackageAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.CardPackageModel;
import com.gzhealthy.health.model.WechatApplet;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MyCardPackageActivity extends BaseAct implements OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {
    private int mPage = 1;

    private MyCardPackageAdapter mMyCardPackageAdapter = new MyCardPackageAdapter();

    @BindView(R.id.rvMyCard)
    RecyclerView rvMyCard;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_card_package;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("我的卡包");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        refreshLayout.setOnRefreshListener(this);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ConvertUtils.dp2px(6));
        View header = new View(this);
        header.setLayoutParams(params);
        mMyCardPackageAdapter.addHeaderView(header);
        mMyCardPackageAdapter.setOnLoadMoreListener(this, rvMyCard);
        mMyCardPackageAdapter.setOnItemClickListener(this);

        rvMyCard.setAdapter(mMyCardPackageAdapter);

        getData("1");
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-我的卡包");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-我的卡包");
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
        param.put("type", "1");
        param.put("page", page);
        param.put("size", "10");
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getCardPackage(param),
                new CallBack<CardPackageModel>() {

                    @Override
                    public void onResponse(CardPackageModel data) {
                        if (data.getCode() == 1) {
                            if (refreshLayout.isRefreshing()) {
                                mPage = 1;
                                refreshLayout.finishRefresh(0, true);
                                mMyCardPackageAdapter.setNewData(data.getData());
                            } else if (mMyCardPackageAdapter.isLoading()) {
                                mPage++;
                                mMyCardPackageAdapter.loadMoreComplete();
                                mMyCardPackageAdapter.addData(data.getData());
                            } else {
                                mPage = 1;
                                mMyCardPackageAdapter.setNewData(data.getData());
                            }
                        } else {
                            if (refreshLayout.isRefreshing()) {
                                refreshLayout.finishRefresh(0, false);
                            } else if (mMyCardPackageAdapter.isLoading()) {
                                mMyCardPackageAdapter.loadMoreFail();
                            }
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (data.getData().isEmpty()) {
                            mMyCardPackageAdapter.loadMoreEnd();
                        }
                        if (mMyCardPackageAdapter.getData().isEmpty()) {
                            mMyCardPackageAdapter.setEmptyView(R.layout.activity_my_card_package_no_data, rvMyCard);
                        }
                    }
                });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        applet();
    }


    public void applet() {
        Map<String, String> params = new HashMap<>();
        params.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().intoApplet(params), new CallBack<WechatApplet>() {
                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onResponse(WechatApplet data) {
                        hideWaitDialog();
                        if (data.code == 1) {
                            WechatPlatform.weChatMiniProgram(MyCardPackageActivity.this, data.data.appletId, "");
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }
                });
    }
}
