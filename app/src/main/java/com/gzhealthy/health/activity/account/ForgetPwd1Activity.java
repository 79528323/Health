package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.BaseUtil;
import com.gzhealthy.health.widget.ValidateCodeView;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ForgetPwd1Activity extends BaseAct {

    @BindView(R.id.phone_cet)
    ClearableEditText phoneCet;
    @BindView(R.id.phone_pws_cet)
    ClearableEditText phonePwsCet;
    @BindView(R.id.tv_phone_sendcode)
    ValidateCodeView tvPhoneSendcode;
    @BindView(R.id.tv_sure)
    TextView reSetPws;
    private Subscription time_sub;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("忘记密码");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(ForgetPwd1Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        phoneCet.addTextChangedListener(textWatcher);
        phonePwsCet.addTextChangedListener(textWatcher);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_forget_pwd1;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }


    public static void instance(Context context) {
        Intent intent = new Intent(context, ForgetPwd1Activity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_phone_sendcode, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_sendcode:
                if (TextUtils.isEmpty(phoneCet.getText().toString()) || !BaseUtil.isChinaMainlandPhone(phoneCet.getText().toString().trim())) {
                    Toast.makeText(aty, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPhonePwd();
                break;
            case R.id.tv_sure:
                ForgetPwd2Activity.instance(this, phoneCet.getText().toString().trim(), phonePwsCet.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkStatue();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void checkStatue() {
        if (!TextUtils.isEmpty(phoneCet.getText().toString()) && !TextUtils.isEmpty(phonePwsCet.getText().toString())) {
            reSetPws.setEnabled(true);
        } else {
            reSetPws.setEnabled(false);
        }
    }

//    public void setdownCount(int count) {
//        tvPhoneSendcode.setTextColor(getResources().getColor(R.color.txt_third_color));
//        tvPhoneSendcode.setEnabled(false);
//        Action1 action = new Action1<Long>() {
//            @Override
//            public void call(Long s) {
//                if (s > 0 && tvPhoneSendcode != null) {
//                    tvPhoneSendcode.setText(String.valueOf(s + "S后重新发送"));
//                }
//                if (s == 0 && tvPhoneSendcode != null) {
//                    tvPhoneSendcode.setEnabled(true);
//                    tvPhoneSendcode.setTextColor(getResources().getColor(R.color.colorPrimary));
//                    tvPhoneSendcode.setText("发送验证码");
//                }
//            }
//        };
//        time_sub = Observable.interval(1, TimeUnit.SECONDS).take(count + 1).map(new Func1<Long, Long>() {
//            @Override
//            public Long call(Long aLong) {
//                return count - aLong;
//            }
//        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(action);
//    }

    /**
     * 开始动态验证码
     */
    public void sendPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneCet.getText().toString().toString());
        hashMap.put("sign", Md5Utils.encryptH(phoneCet.getText().toString().toString() + "2gzhealthy"));
        hashMap.put("type", "2");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getFindPasswordCode(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
//                            setdownCount(60);
                            tvPhoneSendcode.startCountTime();
                        }
                        Toast.makeText(ForgetPwd1Activity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
