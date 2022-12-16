package com.gzhealthy.health.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.MyFamilyPersonDetailActivity;
import com.gzhealthy.health.activity.WebViewActivity;
import com.gzhealthy.health.activity.report.HealthyReportQuestionActivity;
import com.gzhealthy.health.adapter.FamilyMemberAdapter;
import com.gzhealthy.health.adapter.FamilyMemberDetailAdapter;
import com.gzhealthy.health.adapter.HealthNewReportAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.model.HealthyNewReport;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Utils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.decoration.HeartRateDividerItemDecoration;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;

public class FamilyPersonTabFragment extends BaseFra{
    @BindView(R.id.detail)
    TextView detail;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    View emptyView;
    FamilyMember.DataBeanX.DataBean bean;
    FamilyMemberDetailAdapter adapter;

    public static FamilyPersonTabFragment newInstance(FamilyMember.DataBeanX.DataBean bean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("FamilyMember", bean);
        FamilyPersonTabFragment fragment = new FamilyPersonTabFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this,loadingPageView);
        detail.setOnClickListener(this::widgetClick);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LineDividerItemDecoration(getActivity(),0));
        emptyView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_member_nodata,null);

        adapter = new FamilyMemberDetailAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            bean = (FamilyMember.DataBeanX.DataBean) bundle.getSerializable("FamilyMember");
            if (bean!=null) {
                getData(bean.id);
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_family_person_tab;
    }

    @Override
    protected void widgetClick(View view) {
        switch (view.getId()){
            case R.id.detail:
                MyFamilyPersonDetailActivity.instance(getActivity(), (Integer) view.getTag());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        Log.e("111","setMenuVisibility  "+menuVisible);
        if (menuVisible){
            if (bean!=null)
                getData(bean.id);
        }
        super.setMenuVisibility(menuVisible);
    }


    public void getData(int id){
        Map<String,String> param = new HashMap<>();
        param.put("id",String.valueOf(id));
        param.put("dateTime", DateUtil.getNowTimeStr());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getMemberDetail(param),
                new CallBack<FamilyMemberDetail>() {

//                    @Override
//                    public void onStart() {
//                        showWaitDialog();
//                        super.onStart();
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        super.onCompleted();
//                        hideWaitDialog();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        super.onError(e);
//                        hideWaitDialog();
//                    }

                    @Override
                    public void onResponse(FamilyMemberDetail data) {
                        if (data.code == 1){
                            adapter.replaceData(data.data.healthData);
                            if (data.data.healthData.isEmpty()){
                                adapter.setEmptyView(R.layout.layout_member_nodata, mRecyclerView);
                                adapter.getEmptyView().findViewById(R.id.refresh)
                                        .setOnClickListener(v -> {
                                    getData(id);
                                });
                            }

                            title.setText(bean.memberNickName);
                            detail.setTag(bean.id);
                        }else {
                            ToastUtil.showToast(data.msg);
                            if (data.code == 500){
                                //获取不到此人信息后后退回列表并将Postion设为第一个
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
                            }
                        }
                    }
                });
    }
}






