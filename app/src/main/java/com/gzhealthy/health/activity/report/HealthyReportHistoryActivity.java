package com.gzhealthy.health.activity.report;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.HealthyReportAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.fragment.HealthReportTabFragment;
import com.gzhealthy.health.fragment.NewsTabFragment;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.HealthyReportHistory;
import com.gzhealthy.health.model.NewsCategoryModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.widget.decoration.HeartRateDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 健康报告
 */
public class HealthyReportHistoryActivity extends BaseAct {
//    @BindView(R.id.refreshLayout)
//    SmartRefreshLayout refreshLayout;

//    @BindView(R.id.history_reclyer)
//    RecyclerView history_reclyer;

//    @BindView(R.id.no_data)
//    TextView no_data;
    @BindView(R.id.mTabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;

    HealthyReportAdapter adapter;

    int page;

    final int pageSize = 10;
    private List<Fragment> mFragment;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_report_history;
    }

    @SuppressLint("ResourceType")
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setBarLeftIcon(R.mipmap.login_back);
        setBarBackgroundColor(R.color.app_btn_parimary);
        setTitle("健康报告");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this,R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        initView();
//        getList();
    }


    public void initView(){
        addTab();
        page = 1;
        mTabLayout.setTabRippleColor(getResources().getColorStateList(R.color.transparent));
    }


    private void addTab() {

        List<String> mTitle = new ArrayList<>();
        mTitle.add("日报");
        mTitle.add("月报");
        mTitle.add("季报");
        mTitle.add("年报");

        mFragment = new ArrayList<>();
        for (int i = 0; i < mTitle.size(); i++) {
//            NewsCategoryModel.DataBean bean = dataList.get(i);
//            L.i("标题：" + bean.getTitle());
//            mTitle.add(bean.getTitle() + "");
            TabLayout.Tab tab = mTabLayout.newTab();
            View view = LayoutInflater.from(this).inflate(R.layout.layout_report_tab1,null);
            TextView textView =  view.findViewById(R.id.tab_name);
            textView.setBackgroundResource(i==0?R.drawable.btn_unbind_watch_confirm:R.drawable.bg_rect_radius);
            textView.setTextColor(ContextCompat.getColor(HealthyReportHistoryActivity.this,i==0?R.color.white:R.color.global_333333));
            textView.setText(mTitle.get(i));
            tab.setCustomView(view);
            mTabLayout.addTab(tab);
            mFragment.add(HealthReportTabFragment.newInstance(i+1));
        }

        //预加载
        mViewPager.setOffscreenPageLimit(1);
        //设置适配器
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });


        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.layout_report_tab1);
                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_name);
                textView.setBackgroundResource(R.drawable.btn_unbind_watch_confirm);
                textView.setTextColor(getColor(R.color.white));
                textView.setText(mTitle.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.layout_report_tab1);
                }
                TextView textView = tab.getCustomView().findViewById(R.id.tab_name);
                textView.setBackgroundResource(0);
                textView.setTextColor(getColor(R.color.global_333333));
                textView.setText(mTitle.get(tab.getPosition()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //绑定
        mTabLayout.setupWithViewPager(mViewPager);

    }


    public void getList(){
        Map<String,String> param = new HashMap<>();
        param.put("page",String.valueOf(page));
        param.put("size",String.valueOf(pageSize));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportList(param,SPCache.getString(SPCache.KEY_TOKEN,"")),
                new CallBack<HealthyReportHistory>() {

                    @Override
                    public void onResponse(HealthyReportHistory history) {
                        if (history.code == 1){
                            if (page == 1) {
                                adapter.refreshData(history.data.list);
//                                history_reclyer.setVisibility(history.data.list.isEmpty()?View.GONE:View.VISIBLE);
                            }else {
                                adapter.appendData(history.data.list);
                            }
                        }

//                        resetSmartlayout();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        resetSmartlayout();
                        super.onError(e);
                    }
                });
    }


    public static void instance(Context context){
        context.startActivity(new Intent(context, HealthyReportHistoryActivity.class));
    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.btn_data:
//                HealthyReportBigDataActivity.inStance(this);
//                break;
//
//            case R.id.btn_question:
//                HealthyReportQuestionActivity.inStance(this);
//                break;
//        }
//    }


    public void getReport(int id){
        Map<String,String> param = new HashMap<>();
        if (id > 0){
            param.put("id",String.valueOf(id));//报告id，不传则返回最新的报告
        }
//        param.put("type","0");//类型，0-自动生成，1-主动生成，默认为：0
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
                new CallBack<HealthyReport>() {
                    @Override
                    public void onResponse(HealthyReport data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                setupReportView(data.data);
                                HealthyReportResultActivity.inStance(HealthyReportHistoryActivity.this,data);
                            }
                        });
                    }
                });
    }

}
