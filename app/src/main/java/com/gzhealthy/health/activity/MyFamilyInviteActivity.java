package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.HaveFamilyMember;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.Utils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.FamilyInvitedListDialog;
import com.gzhealthy.health.widget.MyInviteQRDialog;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 家庭成员 邀请页面
 */
public class MyFamilyInviteActivity extends BaseAct {

    @BindView(R.id.sms)
    LinearLayout sms;

    @BindView(R.id.qr)
    LinearLayout qr;

    @BindView(R.id.my_qr)
    TextView my_qr;

    @BindView(R.id.understand)
    TextView understand;

    MyInviteQRDialog dialog;

    int destroyType = 0;//0为默认  大于为要被销毁

    int inviteNum = 0;//右上角邀请数

    FamilyInvitedListDialog familyInvitedListDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_family_invite;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("家庭成员");
        setInviteRightText("新邀请");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        initView();
    }


    public void initView() {
        sms.setOnClickListener(this::onClick);
        qr.setOnClickListener(this::onClick);
        my_qr.setOnClickListener(this::onClick);
        understand.setOnClickListener(this::onClick);
        dialog = new MyInviteQRDialog(this, rxManager, this, this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            inviteNum = bundle.getInt(IntentParam.TYPE_FAMILY_INVITE_NUM, 0);
            String url = bundle.getString(IntentParam.TYPE_FAMILY_INVITE_URL, "");
            understand.setTag(url);
        }
        destroyType = getIntent().getIntExtra(IntentParam.TYPE_FAMILY_INVITE_DESTROY, 0);
        setInviteRightBage(inviteNum);
        familyInvitedListDialog = new FamilyInvitedListDialog(this, this, this);
        familyInvitedListDialog.setOnInviteOperationCallBack(isAccept -> {
            if (isAccept) {
                ActivityUtils.finishActivity(MyFamilyInviteActivity.class);
                MyFamilyPersonActivity.instance(this, true);
            }
        });
        setInviteRightOnClick(v -> {
            if (!familyInvitedListDialog.isShowing()) {
                familyInvitedListDialog.show();
            }
        });
    }


//    public void getData(){
//        Map<String,String> param = new HashMap<>();
//        param.put(SPCache.KEY_TOKEN,getUserToken());
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().healthDataStatistic(param),
//                new CallBack<HealthyStatistics>() {
//
//                    @Override
//                    public void onResponse(HealthyStatistics data) {
//
//                    }
//                });
//    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-家庭成员邀请");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-家庭成员邀请");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (destroyType > 0) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.sms:
                SharedMemberMobileActivity.startActivity(this);
                break;

            case R.id.qr:
                ScanRelationActivity.startActivity(this);
                break;

            case R.id.my_qr:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                break;

            case R.id.understand:
//                String url = (String) view.getTag();
//                WebViewActivity.startLoadLink(this, url, "");
                ishaveFamilyMember();
                break;
        }
    }


    public void ishaveFamilyMember() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().ifHaveFamilyMember(hashMap),
                new CallBack<HaveFamilyMember>() {
                    @Override
                    public void onResponse(HaveFamilyMember data) {
                        if (data.code == 1) {
                            WebViewActivity.startLoadLink(MyFamilyInviteActivity.this, data.data.introduceUrl, "");
                        } else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });

    }

    public static void instance(Context context) {
        context.startActivity(new Intent(context, MyFamilyInviteActivity.class));
    }

}
