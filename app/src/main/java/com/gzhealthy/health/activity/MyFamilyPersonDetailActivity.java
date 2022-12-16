package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FamilyMemberDetailAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CalendarFamilyDailog;
import com.gzhealthy.health.widget.CalendarPopWindow;
import com.gzhealthy.health.widget.CircleImageView;
import com.gzhealthy.health.widget.FamilyDeleteDialog;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 家庭成员 邀请
 */
public class MyFamilyPersonDetailActivity extends BaseAct implements FamilyDeleteDialog.OnDeleteMemberCallBack{

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.name)
    TextView name;

    @BindView(R.id.uid)
    TextView uid;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.edit)
    ImageView edit;

    @BindView(R.id.user_icon)
    CircleImageView user_icon;

    @BindView(R.id.see_my_author)
    LinearLayout see_my_author;

    @BindView(R.id.delete)
    LinearLayout delete;

    FamilyMemberDetailAdapter adapter;

    FamilyDeleteDialog deleteDialog;

//    CalendarPopWindow popWindow;

    CalendarFamilyDailog calendarFamilyDailog;

    View emptyView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_family_person_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("成员详情");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        initView();
    }


    public void initView(){
        int id = getIntent().getIntExtra(IntentParam.ID,0);
        date.setText(DateUtil.getNowTimeStr());

        edit.setOnClickListener(this);
        see_my_author.setOnClickListener(this);
        delete.setOnClickListener(this);
        date.setOnClickListener(this);

        emptyView = LayoutInflater.from(this).inflate(R.layout.layout_member_nodata,null);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LineDividerItemDecoration(this,0));
        adapter = new FamilyMemberDetailAdapter(this);
        mRecyclerView.setAdapter(adapter);

        deleteDialog = new FamilyDeleteDialog(this,this,this);
        deleteDialog.setMemberId(id);
        deleteDialog.setOnDeleteMemberCallBack(this);

//        popWindow = new CalendarPopWindow(this, rxManager, day -> {
//            popWindow.dismiss();
//            date.setText(day);
//            getData(id,true);
//        });

        calendarFamilyDailog = new CalendarFamilyDailog(this, rxManager, day -> {
            calendarFamilyDailog.dismiss();
            date.setText(day);
            getData(id,true);
        });

        rxManager.onRxEvent(RxEvent.MODIFY_FAMILY_DETAIL_NICKNAME)
                .subscribe((Action1<Object>) o -> {
                    name.setText((String) o);
                });
        rxManager.onRxEvent(RxEvent.ON_REFRESH_FAMILY_MEMBER_DETAIL)
                .subscribe((Action1<Object>) o -> {
                    getData(id,true);
                });
        getData(id,false);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        Object tag = view.getTag();
        switch (view.getId()){
            case R.id.delete:
                if (!deleteDialog.isShowing()){
                    deleteDialog.show();
                }
                break;

            case R.id.see_my_author:
                if (null != tag){
                    FamilyMemberDetail data = (FamilyMemberDetail) tag;
                    MyFamilyAuthorityActivity.instance(MyFamilyPersonDetailActivity.this,data);
                }
                break;

            case R.id.edit:
                int id = (int) view.getTag();
                ModifyRemarkNicknameActivity.modifyNickname(this,name.getText().toString(),id+"");
                break;

            case R.id.date:
                if (!calendarFamilyDailog.isShowing())
                    calendarFamilyDailog.show();
                break;
        }
    }


    /**
     *
     * @param id
     * @param isRefreshFamily   发送通知家庭列表页面刷新
     */
    public void getData(int id,boolean isRefreshFamily){
        Map<String,String> param = new HashMap<>();
        param.put("id",String.valueOf(id));
        param.put("dateTime", date.getText().toString());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getMemberDetail(param),
                new CallBack<FamilyMemberDetail>() {

                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(FamilyMemberDetail data) {
                        if (data.code == 1){
                            adapter.replaceData(data.data.healthData);
                            if (adapter.getData().isEmpty()) {
                                adapter.setEmptyView(R.layout.layout_member_nodata, mRecyclerView);
                                adapter.getEmptyView().findViewById(R.id.refresh)
                                        .setOnClickListener(v -> {
                                            getData(id,true);
                                        });
                            }
                            uid.setText("ID："+data.data.member);
                            name.setText(data.data.memberNickName);
                            edit.setTag(data.data.id);
                            GlideUtils.CircleImage(MyFamilyPersonDetailActivity.this,data.data.memberAvatar,user_icon);
                            see_my_author.setTag(data);
                            if (isRefreshFamily){
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON_POSITION);
                            }
                        }else {
                            ToastUtil.showToast(data.msg);
                            if (data.code == 500){
                                //获取不到此人信息后后退回列表并将Postion设为第一个
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
                                finish();
                            }
                        }
                    }
                });
    }

    public static void instance(Context context,int id){
        Intent intent = new Intent(context,MyFamilyPersonDetailActivity.class);
        intent.putExtra(IntentParam.ID,id);
        context.startActivity(intent);
    }

    @Override
    public void onDelete() {
        RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
        finish();
    }
}
