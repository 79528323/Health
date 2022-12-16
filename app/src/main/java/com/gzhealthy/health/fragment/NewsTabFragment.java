package com.gzhealthy.health.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.ArticleHtmlActivity;
import com.gzhealthy.health.adapter.NewsTabAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

public class NewsTabFragment extends Fragment implements LifeSubscription, ResponseState {

    public static final String BUNDLE_TITLE = "title";
    private RxManager rxManager = new RxManager();
    private View mContentView;

    private String titleTab;
    private RecyclerView mRecyclerView;
    private RefreshLayout refreshLayout;
    private NewsTabAdapter mNewsTabAdapter;
    private List<NewsListModel.DataBean> mList = new ArrayList<>();

    private boolean refreshType;
    private int page;
    private int oldListSize;
    private int newListSize;
    private int addListSize;

    public static NewsTabFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE, title);
        NewsTabFragment fragment = new NewsTabFragment();
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
        mContentView = inflater.inflate(R.layout.fragment_news_tab, container, false);
        initView();
        initData();
        return mContentView;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            titleTab = bundle.getCharSequence(BUNDLE_TITLE, "") + "";
        }
        mRecyclerView = mContentView.findViewById(R.id.mRecyclerView);
        refreshLayout = mContentView.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);

        refreshType = true;
        page = 1;
        getData(page);
    }

    private void initData() {
        rxManager.onRxEvent(RxEvent.ON_NEW_TAB_REFRESH)
                .subscribe(o -> {
                    refreshType = true;
                    refreshLayout.setEnableLoadMore(true);
                    page = 1;
                    getData(page);
                    refreshLayout.finishRefresh();
                    refreshLayout.resetNoMoreData();
                });

        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = true;
                        refreshLayout.setEnableLoadMore(true);
                        page = 1;
                        getData(page);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 2000);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshType = false;
                        if (page > 50) {
                            ToastUtil.showToast("暂无更多的数据啦");
                            // 将不会再次触发加载更多事件
                            refreshLayout.finishLoadMoreWithNoMoreData();
                            return;
                        }
                        getData(page);
                        refreshLayout.setEnableLoadMore(false);

                        refreshLayout.finishLoadMore();
                    }
                }, 2000);
            }
        });
        // 触发自动刷新
//        refreshLayout.autoRefresh();
    }

    private void getData(int page) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("categoryId", titleTab);
        hashMap.put("limit", "10");
        hashMap.put("page", page + "");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getNewsList(hashMap), new CallBack<NewsListModel>() {
                    @Override
                    public void onResponse(NewsListModel data) {
                        if (data.getCode() == 1) {
                            if (null != data.getData() && data.getData().size() > 0) {
                                if (refreshType) {
                                    mList.clear();
                                    oldListSize = 0;
                                } else {
                                    oldListSize = mList.size();
                                }
                                mList.addAll(data.getData());
                                setDataToUI();
                            }
                        }
                    }
                });
    }

    private void setDataToUI() {

        newListSize = mList.size();
        addListSize = newListSize - oldListSize;

//        L.i("页数：" + page);
//        L.i("数据集mList：" + mList.size());
//        L.i("新数据集newListSize：" + newListSize);
//        L.i("增加数据集addListSize：" + addListSize);

        if (refreshType) {
            try {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
                mNewsTabAdapter = new NewsTabAdapter(getActivity(), mList);
                mRecyclerView.setAdapter(mNewsTabAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mNewsTabAdapter.notifyItemRangeInserted(mList.size() - addListSize, mList.size());
            mNewsTabAdapter.notifyItemRangeChanged(mList.size() - addListSize, mList.size());
            refreshLayout.setEnableLoadMore(true);
        }

        try {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            mNewsTabAdapter = new NewsTabAdapter(getActivity(), mList);
            mRecyclerView.setAdapter(mNewsTabAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNewsTabAdapter.setItemClikListener(new NewsTabAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
//                if (SPCache.getBoolean("islogin", false)) {
                    ArticleHtmlActivity.newIntent(getActivity(),mList.get(position));
//
//
////                    String title = mList.get(position).getTitle();
////                    String id = mList.get(position).getId() + "";
////                    String token = SPCache.getString("token", "");
//////                    String url = Constants.NEWS_DETAIL + id + "&token=" + token;
////                    String url = Constants.NEWS_DETAIL + id;
////                    Html5Activity.newIntent(getActivity(),
////                            "" + title, url, true,true,mList.get(position).getContent());
//                } else {
//                    ToastUtils.showShort("请先登录哦~~");
//                }
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });
        page++;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        rxManager.clear();
        super.onDestroy();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
            Log.e("111","titleTab == "+menuVisible);
        }
    }


    @Override
    public void bindSubscription(Subscription subscription) {

    }

    @Override
    public void setState(int state) {

    }

}






