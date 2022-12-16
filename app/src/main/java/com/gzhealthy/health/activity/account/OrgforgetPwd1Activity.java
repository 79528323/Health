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
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.BaseUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.ValidateCodeView;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import org.litepal.crud.DataSupport;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;

public class OrgforgetPwd1Activity extends BaseAct {

    @BindView(R.id.phone_num_desc)
    TextView tv_phone_num_desc;
    @BindView(R.id.phone_pws_cet)
    ClearableEditText phonePwsCet;
    @BindView(R.id.tv_phone_sendcode)
    ValidateCodeView tvPhoneSendcode;
    @BindView(R.id.tv_sure)
    TextView reSetPws;
    @BindView(R.id.verify_error)
    TextView tv_verify_error;
    String phoneNum;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_org_forget_pwd1;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("忘记密码");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(OrgforgetPwd1Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
//        phoneCet.addTextChangedListener(textWatcher);

        UserInfo.DataBean info = DataSupport.findFirst(UserInfo.DataBean.class);
        if (info!=null){
            phoneNum= info.getPhone();
            StringBuilder builder = new StringBuilder();
            builder.append(phoneNum.substring(0,4))
                    .append("****")
                    .append(phoneNum.substring(8));
            tv_phone_num_desc.setText(getString(R.string.org_forget_pwd,builder.toString()));
        }

        phonePwsCet.addTextChangedListener(textWatcher);
    }


    public static void instance(Context context) {
        Intent intent = new Intent(context, OrgforgetPwd1Activity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_phone_sendcode, R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_sendcode:
                if (!TextUtils.isEmpty(phoneNum)){
                    sendPhonePwd();
                }else {
                    ToastUtil.showToast("获取失败，手机号码为空");
                }
                break;
            case R.id.tv_sure:
                verifyCode(phonePwsCet.getText().toString());
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
            tv_verify_error.setVisibility(View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void checkStatue() {
        if (!TextUtils.isEmpty(phonePwsCet.getText().toString())) {
            reSetPws.setEnabled(true);
        } else {
            reSetPws.setEnabled(false);
        }
    }

    /**
     * 开始动态验证码
     */
    public void sendPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNum);
        hashMap.put("sign", Md5Utils.encryptH(phoneNum + "2gzhealthy"));
        hashMap.put("type", "2");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getFindPasswordCode(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
//                            setdownCount(60);
                            tvPhoneSendcode.startCountTime();
                        }
                        ToastUtil.showToast(data.getMsg());
                    }
                });
    }


    /**
     * 校验验证码
     * @param code
     */
    public void verifyCode(String code){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNum);
        hashMap.put("messageCode", code);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().verifyMessageCode(hashMap),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            OrgforgetPwd2Activity.instance(OrgforgetPwd1Activity.this);
                            finish();
                        }else {
                            tv_verify_error.setText(data.msg);
                            tv_verify_error.setVisibility(View.VISIBLE);
                        }

                    }
                });
    }

}
