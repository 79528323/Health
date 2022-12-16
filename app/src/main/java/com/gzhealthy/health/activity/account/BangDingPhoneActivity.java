package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.AboutHealthContentActivity;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.RegexUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.ValidateCodeView;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BangDingPhoneActivity extends BaseAct {

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_phone_code)
    EditText tv_phone_code;

    @BindView(R.id.tv_send_code)
    ValidateCodeView tv_send_code;

    @BindView(R.id.agree)
    CheckBox checkBox;

    @BindView(R.id.tv_sure)
    TextView tv_sure;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("绑定手机号码");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(BangDingPhoneActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        tv_phone.addTextChangedListener(TextWatcher);
        tv_phone_code.addTextChangedListener(TextWatcher);
    }

    @OnClick({R.id.tv_user_agreement, R.id.tv_privacy_policy,R.id.tv_sure,R.id.tv_send_code})
    public void OnViewClick(View view){
        switch (view.getId()){
            case R.id.tv_user_agreement:
                getCompanyPrivacy("体安用户协议");
                break;
            case R.id.tv_privacy_policy:
                getCompanyPrivacy("隐私协议");
                break;

            case R.id.tv_sure:
                if (!checkBox.isChecked()){
                    ToastUtil.showToast("请先阅读并勾选同意协议选框");
                    return;
                }
                bindPhonePwd();
                break;

            case R.id.tv_send_code:
                String phone = tv_phone.getText().toString();
                if (TextUtils.isEmpty(phone) || !RegexUtils.isMobileSimple(phone)) {
                    Toast.makeText(aty, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPhonePwd();
                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.bind_phone_activity;
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
    public static void instance(Context context, int logintype, String openid,String wechatNickName) {
        Intent intent = new Intent(context, BangDingPhoneActivity.class);
        intent.putExtra("logintype", logintype);
        intent.putExtra("openid", openid);
        intent.putExtra("wechatNickName", wechatNickName);
        context.startActivity(intent);
    }



    /**
     * 开始发送验证码
     */
    public void sendPhonePwd() {
        int type = 3;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("phone", tv_phone.getText().toString());
        hashMap.put("type", type);
        hashMap.put("sign", Md5Utils.encryptH(tv_phone.getText().toString()+type+"gzhealthy"));
//        hashMap.put("sign", Md5Utils.encryptH(tv_phone.getText().toString().toString() + "nb"));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getRegisterCode(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                Toast.makeText(BangDingPhoneActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
//                    setdownCount(count);
                    tv_send_code.startCountTime();
                }
            }
        });
    }

    /**
     * 绑定手机
     */
    public void bindPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", tv_phone.getText().toString());
        hashMap.put("smsCode", tv_phone_code.getText().toString());
        hashMap.put("loginType", getIntent().getIntExtra("logintype", 0) + "");
        hashMap.put("openId", getIntent().getStringExtra("openid"));
        hashMap.put("wechatNickName",getIntent().getStringExtra("wechatNickName"));
        String regId = JPushInterface.getRegistrationID(this);
        Logger.e("111","regId "+regId);
        hashMap.put("deviceToken",regId);
        hashMap.put("terminal", Constants.MY_TERMINAL);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().bindingPhone(hashMap), new CallBack<BaseModel<String>>() {
            @Override
            public void onResponse(BaseModel<String> data) {
                Toast.makeText(BangDingPhoneActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
//                    SPCache.putBoolean("islogin", true);
                    SPCache.putString("token", data.getData());
                    getpersonInfo();
                }
            }
        });
    }


    /**
     * 获取个人信息
     */
    public void getpersonInfo() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getAccountInfo(hashMap),
                new CallBack<UserInfo>() {
                    @Override
                    public void onResponse(UserInfo data) {
                        if (data.code==1){
                            if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                                DataSupport.deleteAll(UserInfo.DataBean.class);
                            }
                            UserInfo.DataBean userInfo = new UserInfo.DataBean();
                            userInfo = data.getData();
                            userInfo.save();

                            setJPushAlias(userInfo.getLoginName());//登录成功后设置推送别名

                            //登录成功 更新页面广播 目前看首页 目的地首页 都不需要用到更新
                            // 广播有发送 还没去接收 后面有需要只要接收这个广播做数据更新就好
                            Intent intent = new Intent();
                            intent.setAction(Constants.Receiver.INTENT_ACTION_MONEY_VISIBILITY);
                            sendBroadcast(intent);
                            // 登录成功跳转到首页
                            Intent in = new Intent(BangDingPhoneActivity.this, HomeActivity.class);
                            in.putExtra("initPos", "0");
                            showActivity(BangDingPhoneActivity.this, HomeActivity.class);
                            goBack();
                        }

                        ToastUtil.showToast(data.msg);
                    }
                });
    }

    /**
     * 查询三个编辑框输入是否为空
     */
    public void checkState() {
        if (!TextUtils.isEmpty(tv_phone.getText().toString()) && !TextUtils.isEmpty(tv_phone_code.getText().toString())) {
            tv_sure.setEnabled(true);
        } else {
            tv_sure.setEnabled(false);
        }
    }



    /**
     * 获取平台协议
     * @param title
     */
    private void getCompanyPrivacy(String title){
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getPlatformAgreement(title)
                , new CallBack<CompanyPrivacy>() {
                    @Override
                    public void onResponse(CompanyPrivacy data) {
                        AboutHealthContentActivity.instance(BangDingPhoneActivity.this,title,data.data);
                    }
                });
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

    /**
     *  设置JPush别名
     *  sequence
     * 用户自定义的操作序列号，同操作结果一起返回，用来标识一次操作的唯一性，推荐每次都用不同的数字序号。
     * alias
     * 每次调用设置有效的别名，覆盖之前的设置。
     * 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     * 限制：alias 命名长度限制为 40 字节。（判断长度需采用 UTF-8 编码）
     */
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

        JPushInterface.setAlias(BangDingPhoneActivity.this,Integer.valueOf(uid),builder.toString());
    }
}
