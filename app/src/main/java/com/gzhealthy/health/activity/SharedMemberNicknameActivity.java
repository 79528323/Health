package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.widget.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 共享成员
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SharedMemberNicknameActivity extends BaseAct {


    /**
     * 会员手机号
     */
    private String memberMobile = "";
    /**
     * 会员头像
     */
    private String memberAvatar = "";
    /**
     * 会员昵称
     */
    private String memberNickname = "";
    /**
     * 会员ID
     */
    private String memberId = "";

    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tvNickname)
    TextView tvNickname;
    @BindView(R.id.tvId)
    TextView tvId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shared_member_nickname;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("共享成员");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        memberMobile = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_MOBILE);
        memberAvatar = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_AVATAR);
        memberNickname = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_NICKNAME);
        memberId = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_ID);

        GlideUtils.CircleImage(this, memberAvatar, ivAvatar);
        tvNickname.setText(memberNickname);
        tvId.setText("ID:" + memberId);
    }

    @OnClick({R.id.btNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btNext://下一步
                String action = getIntent().getAction();
                if (SharedMemberMobileActivity.class.getName().equals(action)) {
                    //验证码邀请
                    ModifyRemarkNicknameActivity.verifyCodeInvite(this, memberMobile, memberNickname, memberId);
                    finish();
                } else if (ScanRelationActivity.class.getName().equals(action)) {
                    //扫码邀请码
                    ModifyRemarkNicknameActivity.scanCodeInvite(this, memberNickname, memberId);
                    finish();
                }
                break;
        }
    }
}
