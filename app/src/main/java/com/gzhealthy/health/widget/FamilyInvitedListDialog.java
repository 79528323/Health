package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FamilyMemberInviteListAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.FamilyMemberInvite;
import com.gzhealthy.health.model.FamilyQRCode;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class FamilyInvitedListDialog extends Dialog implements View.OnClickListener, OnRefreshListener
        , BaseQuickAdapter.RequestLoadMoreListener,FamilyMemberInviteListAdapter.OnItemStatusSelectListener {
    Context context;

    RecyclerView mRecyclerView;

    ImageView close;

    LifeSubscription mLifeSubscription;
    ResponseState mResponseState;
    RxManager rxManager;

    FamilyMemberInviteListAdapter adapter;

    SmartRefreshLayout smartRefreshLayout;

    int mPage=1;

    OnInviteOperationCallBack onInviteOperationCallBack;

    public FamilyInvitedListDialog(Context context, LifeSubscription lifeSubscription, ResponseState responseState) {
        super(context);
        this.context = context;
        this.rxManager =rxManager;
        this.mLifeSubscription = lifeSubscription;
        this.mResponseState = responseState;
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_family_invited_list,null);
        mRecyclerView = view.findViewById(R.id.mRecyclerView);
        mRecyclerView.addItemDecoration(new LineDividerItemDecoration(this.context));
        close = view.findViewById(R.id.close);
        close.setOnClickListener(this);
        smartRefreshLayout = view.findViewById(R.id.refreshLayout);
        smartRefreshLayout.setOnRefreshListener(this);

        setContentView(view);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.CENTER);
        getWindow().getAttributes().width = ScreenUtils.getScreenWidth()/10 * 8;
        getWindow().getAttributes().height = ScreenUtils.getScreenHeight() / 8 * 4;

        adapter = new FamilyMemberInviteListAdapter(context,this.mLifeSubscription,this.mResponseState);
        adapter.setOnLoadMoreListener(this,mRecyclerView);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemStatusSelectListener(this);
    }

    @Override
    public void show() {
        super.show();
        adapter.getData().clear();
        smartRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
//                this.onSelectInviteListener.onInvitefriend();
                dismiss();
                break;

//            case R.id.new_invite:
//                this.onSelectInviteListener.onNewInvite();
//                dismiss();
//                break;
        }
    }



    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        getList(1);
    }

    @Override
    public void onLoadMoreRequested() {
        getList(mPage);
    }

    @Override
    public void refuseOraccept(int ItemId,String member ,int agree) {
        if (adapter!=null){
            List<FamilyMemberInvite.DataBean> list = adapter.getData();
            Iterator<FamilyMemberInvite.DataBean> iterator = list.iterator();
            while (iterator.hasNext()){
                FamilyMemberInvite.DataBean bean = iterator.next();
                if (bean.id == ItemId){
                    bean.ifAgree = agree;
                    adapter.notifyDataSetChanged();
                    if (onInviteOperationCallBack!=null)
                        onInviteOperationCallBack.onOperation(bean.ifAgree==1?true:false);
                    break;
                }
            }
        }
    }





    public void getList(int page){
        Map<String,String> param = new HashMap<>();
        param.put("page",String.valueOf(page));
        param.put("size","10");
        HttpUtils.invoke(mLifeSubscription, mResponseState, InsuranceApiFactory.getmHomeApi().getMemberInviteList(param),
                new CallBack<FamilyMemberInvite>() {

                    @Override
                    public void onResponse(FamilyMemberInvite data) {
                        if (data.code == 1){
                            if (smartRefreshLayout.isRefreshing()){
                                if (data.data.isEmpty()){
                                    adapter.setEmptyView(R.layout.layout_family_invite_nodata,mRecyclerView);
                                }else {
                                    adapter.setNewData(data.data);
//                                    smartRefreshLayout.finishRefresh();
                                    mPage = 2;
                                    if (data.data.size() < 10){
                                        adapter.loadMoreEnd(true);
                                    }
                                }
                                smartRefreshLayout.finishRefresh();
                            }else{
                                if (data.data.isEmpty()){
                                    adapter.loadMoreEnd(true);
                                }else {
                                    mPage++;
                                    adapter.addData(data.data);
                                    adapter.loadMoreComplete();
                                }
                            }
                        }else {
                            ToastUtil.showToast(data.msg);
                            smartRefreshLayout.finishRefresh();
                            adapter.loadMoreEnd(true);
                        }
                    }
                });
    }


    public interface OnInviteOperationCallBack{
        void onOperation(boolean isAccept);
    }

    public void setOnInviteOperationCallBack(OnInviteOperationCallBack onInviteOperationCallBack) {
        this.onInviteOperationCallBack = onInviteOperationCallBack;
    }
}
