package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.CarOnlyIdUtils;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class Regist2Activity extends BaseAct {

    public static String PHONE_NUMBER = "phoneNumber";
    public static String PHONE_CODE = "phoneCode";

    @BindView(R.id.et_psw1)
    EditText etPsw1;
    @BindView(R.id.et_psw2)
    EditText etPsw2;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private String Tag = Regist2Activity.class.getSimpleName();

    private String psw1, psw2;
    private String phoneNumber;
    private String phoneCode;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("注册");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(Regist2Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etPsw1.addTextChangedListener(TextWatcher);
        etPsw2.addTextChangedListener(TextWatcher);
        etPsw1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etPsw2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
        phoneCode = getIntent().getStringExtra(PHONE_CODE);
    }

    TextWatcher TextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_regist2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }


    /**
     * 初始化
     *
     * @param context context
     */
    public static void instance(Context context, String phoneNumber, String phoneCode) {
        Intent intent = new Intent(context, Regist2Activity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(PHONE_CODE, phoneCode);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                psw1 = etPsw1.getText().toString().trim();
                psw2 = etPsw2.getText().toString().trim();
                if (!psw1.equals(psw2)) {
                    ToastUtil.showToast("两次输入的密码不一致！");
                    return;
                }
                registByPhone();
                break;
            default:
                break;
        }
    }

    /**
     * 查询两个个编辑框输入是否为空
     */
    public void checkState() {
        if (!TextUtils.isEmpty(etPsw1.getText().toString()) && !TextUtils.isEmpty(etPsw1.getText().toString())) {
            tvSure.setEnabled(true);
        } else {
            tvSure.setEnabled(false);
        }
    }

    Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");

    /**
     * 发送注册请求
     */
    public void registByPhone() {
        L.i("phoneNumber:" + phoneNumber);
        L.i("phoneCode:" + phoneCode);
        String psw = etPsw1.getText().toString();
        Matcher matcher = pattern.matcher(psw);
        if (!matcher.find()) {
            Toast.makeText(aty, "密码格式错误请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNumber);
        hashMap.put("password", Md5Utils.encryptH(etPsw1.getText().toString().trim()));
        hashMap.put("smsCode", phoneCode);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().register(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
                            loginByName();
                        } else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });
    }



    public void loginByName() {
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNumber);
        hashMap.put("password", Md5Utils.encryptH(etPsw1.getText().toString().trim()));

        String regId = SPCache.getString(SPCache.KEY_JPUSH_REG_ID,"");//JPushInterface.getRegistrationID(this);
        Logger.e("111","regId "+regId);
        hashMap.put("deviceToken", TextUtils.isEmpty(regId)? JPushInterface.getRegistrationID(this):regId);
        hashMap.put("terminal", Constants.MY_TERMINAL);
        hashMap.put("deviceMark", CarOnlyIdUtils.getANDROID_ID(this));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().login(hashMap),
                new CallBack<ComModel>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(ComModel data) {
                        hideWaitDialog();
                        ToastUtil.showToast(data.msg);
                        if (data.getCode() == 1) {
                            SPCache.putString("token", data.getData());
//                            SPCache.putBoolean("islogin", true);
                            getpersonInfo();
                        }
                    }
                });
    }


    public void getpersonInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getAccountInfo(hashMap),
                new CallBack<UserInfo>() {
                    @Override
                    public void onResponse(UserInfo data) {
                        if (data.getCode() == 10086) {
                            LoginActivity.instance(Regist2Activity.this);
                            SPCache.putBoolean("islogin", false);
                            return;
                        }
                        if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                        }
                        UserInfo.DataBean userInfo = new UserInfo.DataBean();
                        userInfo = data.getData();
                        userInfo.save();

//                        JPushInterface.getAlias(LoginActivity.this,userInfo.getId());
                        setJPushAlias(userInfo.getLoginName());//登录成功后设置推送别名

                        //登录成功 更新页面广播 目前看首页 目的地首页 都不需要用到更新
                        // 广播有发送 还没去接收 后面有需要只要接收这个广播做数据更新就好
                        Intent intent = new Intent();
                        intent.setAction(Constants.Receiver.INTENT_ACTION_MONEY_VISIBILITY);
                        sendBroadcast(intent);
                        // 登录成功跳转到首页
                        Intent in = new Intent(Regist2Activity.this, HomeActivity.class);
                        in.putExtra("initPos", "0");
                        showActivity(Regist2Activity.this, HomeActivity.class);
                    }
                });
    }



    private void setJPushAlias(String uid){
        String chart = "T";
//        int number = uid;
        String underline="_";
        String chinese = "推";
        String specialChart = "$";
        StringBuilder builder = new StringBuilder();
        builder.append(chart)
                .append(uid)
                .append(underline)
                .append(chinese)
                .append(specialChart);

        JPushInterface.setAlias(Regist2Activity.this,Integer.valueOf(uid),builder.toString());
    }
}
