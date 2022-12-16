package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.EnterInvitePhoneModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.RegexUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 共享成员
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SharedMemberMobileActivity extends BaseAct {
    @BindView(R.id.etMobile)
    EditText etMobile;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SharedMemberMobileActivity.class));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shared_member_mobile;
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
    }


    @OnClick({R.id.btNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btNext://下一步
                enterInvitePhone();
                break;
        }
    }

    /**
     * 共享成员，通过手机号查询用户信息
     */
    private void enterInvitePhone() {
        String mobile = etMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtil.showToast("请输入对方手机号");
            return;
        }
        if (!RegexUtils.isMobileSimple(mobile)) {
            ToastUtil.showToast("手机号格式错误");
            return;
        }

        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("phone", mobile);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().enterInvitePhone(param),
                new CallBack<EnterInvitePhoneModel>() {

                    @Override
                    public void onResponse(EnterInvitePhoneModel data) {
                        if (data.getCode() == 1) {
                            EnterInvitePhoneModel.DataDTO dataDTO = data.getData();
                            Intent intent = new Intent(SharedMemberMobileActivity.this, SharedMemberNicknameActivity.class);
                            intent.setAction(SharedMemberMobileActivity.class.getName());
                            intent.putExtra(IntentParam.EXTRA_MEMBER_MOBILE, mobile);
                            intent.putExtra(IntentParam.EXTRA_MEMBER_AVATAR, dataDTO.getMemberAvatar());
                            intent.putExtra(IntentParam.EXTRA_MEMBER_NICKNAME, dataDTO.getMemberNickName());
                            intent.putExtra(IntentParam.EXTRA_MEMBER_ID, dataDTO.getMember());
                            startActivity(intent);
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });

    }

}
