package com.gzhealthy.health.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.VerificationCodeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SharedMemberVerifyCodeActivity extends BaseAct {
    /**
     * 会员手机号
     */
    private String memberMobile = "";
    /**
     * 会员昵称
     */
    private String memberNickname = "";
    /**
     * 会员ID
     */
    private String memberId = "";

    @BindView(R.id.vcvCode)
    VerificationCodeView vcvCode;
    @BindView(R.id.btSend)
    Button btSend;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_shared_member_verify_code;
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
        memberNickname = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_NICKNAME);
        memberId = getIntent().getStringExtra(IntentParam.EXTRA_MEMBER_ID);

        setCountDown();
    }

    /**
     * 设置倒计时
     */
    private void setCountDown() {
        new CountDownTimer(60 * 1000L, 1000L) {

            @Override
            public void onTick(long tick) {
                int countTime = (int) ((tick / 1000) + 1);
                setButtonHint(countTime, btSend);
            }

            @Override
            public void onFinish() {
                setButtonHint(0, btSend);
            }
        }.start();

    }

    /**
     * 设置按钮提示
     *
     * @param countTime 倒计时（秒）
     * @param button    按钮
     */
    private void setButtonHint(int countTime, Button button) {
        try {
            if (countTime < 1) {
                button.setEnabled(true);
                button.setText("重新发送");
            } else {
                button.setEnabled(false);
                button.setText("重新发送（" + countTime + "s）");
            }
        } catch (Exception e) {
            button.setEnabled(true);
            button.setText("重新发送");
        }
    }

    @OnClick({R.id.btSend, R.id.btOk})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btSend://发送验证码
                sendSmsCode();
                break;
            case R.id.btOk://确定
                smsCodeInvite();
                break;
        }
    }

    /**
     * 共享成员，发送邀请验证码
     */
    private void sendSmsCode() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("phone", memberMobile);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().sendSmsCode(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            setCountDown();
                        }
                    }
                });
    }

    private void smsCodeInvite() {
        boolean block = vcvCode.getBlock();
        if (!block) {
            ToastUtil.showToast("请输入对方收到的验证码");
            return;
        }
        String smsCode = vcvCode.getText();

        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("smsCode", smsCode);
        param.put("member", memberId);
        param.put("memberNickName", memberNickname);
        param.put("phone", memberMobile);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().smsCodeInvite(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            ToastUtil.showToast("已成功和" + memberNickname + "互为成员！");
                            if (ActivityUtils.isActivityExistsInStack(MyFamilyPersonActivity.class)) {
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
                            } else {
                                startActivity(new Intent(SharedMemberVerifyCodeActivity.this, MyFamilyPersonActivity.class));
                            }
                            ActivityUtils.finishActivity(SharedMemberMobileActivity.class);
                            ActivityUtils.finishActivity(MyFamilyInviteActivity.class);
                            finish();
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }

}
