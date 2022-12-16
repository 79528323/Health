package com.gzhealthy.health.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
//import com.gzhealthy.health.activity.ECGHtmlActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.adapter.HeartEKGAdapter;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.contract.HealthyDataContract;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.presenter.HealthyDataPresenter;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.decoration.HeartRateDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 心电
 */
public class HeartECGFragment extends BaseFra implements HealthyDataContract.EKGView  {
    private HealthyDataPresenter presenter;
//    @BindView(R.id.healthChoiceView)
//    HealthDateChoiceView healthDateChoiceView;
    private HeartEKGAdapter adapter;
    private RecyclerView recyclerView;
    private CalendarPopWindow popWindow;
    private SmartRefreshLayout layout;

    @BindView(R.id.check_state_lay)
    LinearLayout check_state_lay;

    @BindView(R.id.check_state)
    TextView check_state;

    public HeartECGFragment() {
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        adapter = new HeartEKGAdapter(getActivity(),mHightOffset);
        recyclerView = (RecyclerView) $(R.id.line_reclyer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HeartRateDividerItemDecoration(getActivity(),20));
        recyclerView.setAdapter(adapter);

//        View line = $(R.id.viewline);
//        healthDateChoiceView=(HealthDateChoiceView) $(R.id.healthChoiceView);

        Map<String,String> param = new HashMap<>();
        param.put("dateTime" , DateUtil.getStringDate());
        param.put("token", SPCache.getString("token",""));
        param.put("type","3");
        presenter = new HealthyDataPresenter(this,this,this);


        layout = (SmartRefreshLayout) $(R.id.refreshLayout);
        layout.setOnRefreshListener(refreshLayout -> {
            presenter.getEKGInfo(param);
        });
        layout.autoRefresh();

        rxManager.onRxEvent(
                RxEvent.ON_SELECT_HEALTHY_DATE)
                .subscribe(o -> {
                    String day = (String) o;
//                    healthDateChoiceView.setDate(day);
                    param.put("dateTime" , day);
                    layout.autoRefresh();
//                    presenter.getHealthyInfo(param);
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_heart_ekg_layout;
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()) {
//            case R.id.check_state:
//
//                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
//        if (!menuVisible){
//            popWindow.dismiss();
//        }
    }

    // FragmentPagerAdapter+ViewPager的使用场景
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // 可见时
//            MobclickAgent.onEvent(getActivity(), Constants.UMeng.HOME_PAGE);
        }
    }

    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-心电详情");
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-心电详情");
    }

    @Override
    public void getSuccess(List<EKGModel.DataBean> list,String statisticUrl) {
        check_state_lay.setVisibility(TextUtils.isEmpty(statisticUrl)?View.GONE:View.VISIBLE);
        adapter.refreshData(list);
        layout.finishRefresh();
        if (!list.isEmpty() || adapter.getItemCount() > 0){
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            recyclerView.setVisibility(View.GONE);
        }
        setCheckStateListener(statisticUrl);
    }

    @Override
    public void getFail(String msg,String statisticUrl) {
        check_state_lay.setVisibility(TextUtils.isEmpty(statisticUrl)?View.GONE:View.VISIBLE);
        adapter.refreshData(null);
        recyclerView.setVisibility(adapter.getItemCount() > 0?View.VISIBLE:View.GONE);
        layout.finishRefresh();
        setCheckStateListener(statisticUrl);
    }


    private void setCheckStateListener(String url){
        if (check_state!=null){
            check_state.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(url)){
                    WebViewActivity.startLoadLink(getActivity(),url,"");
//                    startActivity(
//                            new Intent(getActivity(), ECGHtmlActivity.class).putExtra("url",url));
                }
            });
        }
    }
}






