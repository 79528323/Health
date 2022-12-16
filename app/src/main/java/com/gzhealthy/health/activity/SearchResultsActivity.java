package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.NewsTabAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.edittext.ClearableEditText;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 搜索结果
 */
public class SearchResultsActivity extends BaseAct {

    private boolean refreshType;
    private int page;
    private int oldListSize;
    private int newListSize;
    private int addListSize;

    private String keyword = "";

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.et_search)
    ClearableEditText et_search;

    private NewsTabAdapter mNewsTabAdapter;
    private List<NewsListModel.DataBean> mList = new ArrayList<>();

    public static void instance(Context context) {
        Intent intent = new Intent(context, SearchResultsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_results;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
//        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("搜索结果");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(SearchResultsActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(true);
        page = 1;
        showKeyboard();
        et_search.addTextChangedListener(tWatcher);
        et_search.setOnEditorActionListener(onEditorActionListener);
        et_search.setListener(() -> {
            mList.clear();
            mNewsTabAdapter.notifyDataSetChanged();
        });
        initData1();
    }

    @OnClick({R.id.tv_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                goBack();
//                keyword = et_search.getText().toString().trim();
//                if (keyword.isEmpty()) {
//                    ToastUtil.showToast("请输入关键词");
//                    return;
//                }
//                keywordSearch();
                break;
//            case R.id.iv_back:
//                finish();
//                break;
            default:
                break;
        }
    }

    TextWatcher tWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            keyword = et_search.getText().toString().trim();
            if (keyword.isEmpty()) {
                clearAdapter();
                return;
            }
            keywordSearch();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            keyword = et_search.getText().toString().trim();
            if (!keyword.isEmpty()) {
                keywordSearch();
            }
            return true;
        }
    };

    private void showKeyboard() {
        et_search.setFocusable(true);
        et_search.setFocusableInTouchMode(true);
        et_search.requestFocus();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)
                        et_search.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(et_search, 0);
            }
        }, 998);
    }

    private void initData1() {
        // 开启自动加载功能（非必须）
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setEnableLoadMore(true);
                        keywordSearch();
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
        //触发自动刷新
//        refreshLayout.autoRefresh();
    }

    private void keywordSearch() {
        refreshType = true;
        page = 1;
        getData(page);
    }

    private void getData(int page) {

        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("categoryId", "");
        hashMap.put("keyWord", keyword);
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
                            }else {
                                clearAdapter();
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
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mNewsTabAdapter = new NewsTabAdapter(this, mList);
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
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mNewsTabAdapter = new NewsTabAdapter(this, mList);
            mRecyclerView.setAdapter(mNewsTabAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mNewsTabAdapter.setItemClikListener(new NewsTabAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                if (!TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""))) {
//                    String title = mList.get(position).getTitle();
//                    String id = mList.get(position).getId() + "";
//                    String token = SPCache.getString("token", "");
////                    String url = Constants.NEWS_DETAIL + id + "&token=" + token;
//                    String url = Constants.NEWS_DETAIL + id;

//                    Html5Activity.newIntent(SearchResultsActivity.this, "" + title, url, true);
                    ArticleHtmlActivity.newIntent(SearchResultsActivity.this,mList.get(position));
                } else {
                    ToastUtils.showShort("请先登录哦~~");
                }
            }

            @Override
            public void onItemLongClik(View view, int position) {

            }
        });
        page++;
    }


    public void clearAdapter(){
        if (refreshType) {
            mList.clear();
        }
        setDataToUI();
    }
}
