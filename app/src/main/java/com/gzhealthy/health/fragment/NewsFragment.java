package com.gzhealthy.health.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.ArticleHtmlActivity;
import com.gzhealthy.health.activity.SearchResultsActivity;
import com.gzhealthy.health.adapter.MultipleTypesAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.Admodel;
import com.gzhealthy.health.model.NewsCategoryModel;
import com.gzhealthy.health.model.RollNewsModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.NoticeView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewsFragment extends BaseFra {

    private LinearLayout ll_search;
    private Banner mBanner;
//    private NoticeView notice_view;

    private List<String> mTitle;
    private List<Fragment> mFragment;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private RefreshLayout refreshLayout;

    @Override
    protected void init(Bundle savedInstanceState) {

        ll_search = (LinearLayout) $(R.id.ll_search);
        ll_search.setOnClickListener(this);
        mBanner = (Banner) $(R.id.mBanner);
//        notice_view = (NoticeView) $(R.id.notice_view);
        mTabLayout = (TabLayout) $(R.id.mTabLayout);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabRippleColor(getActivity().getResources().getColorStateList(R.color.transparent));

        mViewPager = (ViewPager) $(R.id.mViewPager);

        refreshLayout = (RefreshLayout) $(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getBannerData();
                refreshLayout.finishRefresh();
                RxBus.getInstance().postEmpty(RxEvent.ON_NEW_TAB_REFRESH);
            }
        });
        getBannerData();
//        getNoticeViewData();
        getTabData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_news_layout;
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                SearchResultsActivity.instance(getActivity());
                break;
            default:
                break;
        }

    }

    private void getBannerData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("location", "1");
        hashMap.put("terminal", "2");
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getBannerList(hashMap), new CallBack<Admodel>() {
                    @Override
                    public void onResponse(Admodel data) {
                        if (data.getCode() == 1) {
                            List<Admodel.DataBean.AdVoListBean> list = data.getData().getAdVoList();
                            if (!list.isEmpty()) {
                                mBanner.setIndicator(new RectangleIndicator(getActivity()));
                                mBanner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(12));
                                mBanner.setIndicatorSpace((int) BannerUtils.dp2px(4));
                                mBanner.setIndicatorRadius(8);
                                mBanner.setAdapter(new MultipleTypesAdapter(getActivity(), data.getData().getAdVoList()))
                                        .addBannerLifecycleObserver(getActivity()) // ???????????????????????????
                                        .setOnBannerListener((data1, position) -> {
                                            L.i("????????? Position???" + position);
//                                            L.i("?????????" + data.getData().getAdVoList().get(position).getSkipType());
//                                            String skipType = data.getData().getAdVoList().get(position).getSkipType();
//                                            String name = data.getData().getAdVoList().get(position).getAdName();
//                                            String id = data.getData().getAdVoList().get(position).getAdId() + "";
                                            Admodel.DataBean.AdVoListBean bean = data.getData().getAdVoList().get(position);
                                            bannerSkipType(bean);
                                        });
                            }
                        }
                    }
                });
    }

    /**
     * ?????????????????????
     *
     */
    private void bannerSkipType(Admodel.DataBean.AdVoListBean bean) {
        switch (bean.getSkipType()) {
            case Constants.BannerJumpType.NO_JUMP:
                break;

//            case Constants.BannerJumpType.APP:
////                Html5Activity.newIntent(getActivity(), "" + name, id, false);
//                break;
            case Constants.BannerJumpType.INNER:
                if (isUserLogin()) {

//                    Html5Activity.newIntent(getActivity()
//                            ,bean.getAdName()
//                            ,bean.getSkipPosition()
//                            ,false);
                } else {
                    ToastUtil.showToast("???????????????~~");
                }
                break;
            case Constants.BannerJumpType.OUT:
//                Html5Activity.newIntent(getActivity()
//                        ,bean.getAdName()
//                        ,bean.getSkipPosition()
//                        ,false);
                break;
            default:
                break;
        }
    }


//    /**
//     * ????????????
//     */
//    private void getNoticeViewData() {
//        HashMap<String, String> hashMap = new HashMap<>();
//        HttpUtils.invoke(this, this,
//                InsuranceApiFactory.getmHomeApi().getRollNews(hashMap), new CallBack<RollNewsModel>() {
//                    @Override
//                    public void onResponse(RollNewsModel data) {
//                        if (data.getCode() == 1) {
//                            if (null != data.getData() && data.getData().size() > 0) {
//                                notice_view.setNoticeList1(data.getData());
//                                notice_view.setOnItemClickListener(new NoticeView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(TextView view, int position) {
//                                        if (isUserLogin()) {
////                                            String title = data.getData().get(position).getTitle();
////                                            String url = data.getData().get(position).getUrl();
////                                            Html5Activity.newIntent(getActivity(), "" + title, url, false);
//                                            ArticleHtmlActivity.newIntent(getActivity(),data.getData().get(position).getId());
//                                        } else {
//                                            ToastUtils.showShort("???????????????~~");
//                                        }
//                                    }
//                                });
//                                notice_view.start();
//                            }
//                        }
//                    }
//                });
//    }

    private void getTabData() {
        HashMap<String, String> hashMap = new HashMap<>();
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getCategory(hashMap), new CallBack<NewsCategoryModel>() {
                    @Override
                    public void onResponse(NewsCategoryModel data) {
                        if (data.getCode() == 1) {
                            if (null != data.getData() && data.getData().size() > 0) {
                                addTab(data.getData());
                            }
                        }
                    }
                });
    }

    /**
     * ????????????
     */
    private void addTab(List<NewsCategoryModel.DataBean> dataList) {

        mTitle = new ArrayList<>();
        mFragment = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            NewsCategoryModel.DataBean bean = dataList.get(i);
//            L.i("?????????" + bean.getTitle());
            mTitle.add(bean.getTitle() + "");
            mFragment.add(NewsTabFragment.newInstance(bean.getId() + ""));
        }
        //?????????
        mViewPager.setOffscreenPageLimit(1);
        //???????????????
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            //?????????item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //??????item?????????
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //????????????
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                changeTabTextView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                changeTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //??????
        mTabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * ?????????????????????
     *
     * @param tab
     * @param isBold
     */
    public void changeTabTextView(TabLayout.Tab tab, boolean isBold) {
        View view = tab.getCustomView();
        if (null == view) {
            tab.setCustomView(R.layout.layout_tab1);
        }
        TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
        if (isBold) {
            textView.setTextAppearance(getActivity(), R.style.TabLayoutBoldTextSize);
        } else {
            textView.setTextAppearance(getActivity(), R.style.TabLayoutNormalTextSize);
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);


        }
    }

    // FragmentPagerAdapter+ViewPager???????????????
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // ?????????
            getBannerData();
//            getNoticeViewData();
            getTabData();
        }
    }

    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }


}






