package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.activity.AboutHealthActivity;
import com.gzhealthy.health.activity.AboutHealthContentActivity;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WeChatAuthModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.utils.BaseUtil;
import com.gzhealthy.health.utils.CarOnlyIdUtils;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ThirdAuthorEnum;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.protocol.BaseView;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.widget.ValidateCodeView;
import com.heytap.msp.push.mode.BaseMode;

import javax.annotation.Nullable;

public class LoginActivity extends BaseAct implements BaseView {

    int LoginType = 1;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_login_psw_forget)
    TextView tvLoginPswForget;
    @BindView(R.id.tv_login_regist)
    TextView tvLoginRegist;

    @BindView(R.id.tv_password_login)
    TextView passwordLoginTv;
    @BindView(R.id.tv_sms_login)
    TextView smsLoginTv;

    @BindView(R.id.ll_password)
    LinearLayout ll_password;
    @BindView(R.id.ll_sms)
    LinearLayout ll_sms;

    @BindView(R.id.ic_pws_visiable)
    ImageView icPwsVisiable;
    @BindView(R.id.ic_pws_unvisiable)
    ImageView icPwsUnvisiable;

    @BindView(R.id.et_phone_psws)
    EditText etPhonePsws;
    @BindView(R.id.tv_sendcode)
    ValidateCodeView tvSendcode;
    @BindView(R.id.agree)
    CheckBox tv_agree;

    private String loginType;
    private String Tag = LoginActivity.class.getSimpleName();
//    private UMShareAPI umShareAPI;

    // 是否是密码类型登录
    private boolean isPasswordLogin;

//    private Subscription time_sub;

    //发送验证码倒计时
    private int count = 60;

    private int pos = 2;

    private Class<?> intentClass;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loginType = getIntent().getStringExtra("loginType");
        if (TextUtils.equals(loginType, "10086")){
            //登录失效后重置首页头像
            RxBus.getInstance().post(RxEvent.ON_REFRESH_HOME_USER_HEADER_IMAGE,"");
        }
        pos = getIntent().getIntExtra(IntentParam.LOGIN_JUMP_POSITION,pos);
        intentClass = (Class<?>) getIntent().getSerializableExtra(IntentParam.LOGIN_INTENT_CLASS);
        ButterKnife.bind(this);
        isPasswordLogin = true;
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(LoginActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etName.addTextChangedListener(textWatcher);
        etPsw.addTextChangedListener(textWatcher);
        etPhonePsws.addTextChangedListener(textWatcher);

        clearUserToken();
        if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
            DataSupport.deleteAll(UserInfo.DataBean.class);
        }

        onSubscribe();
    }


    /**
     * 订阅事件
     */
    private void onSubscribe(){
        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_SUCCESS)
                .subscribe((Action1<Object>) o -> {
                    WeChatAuthModel model = (WeChatAuthModel) o;
                    loginByAuthor(model,ThirdAuthorEnum.WeChat.toString());
                });

        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_FAIL)
                .subscribe((Action1<Object>) o -> {
                    runOnUiThread(() -> {
                        ToastUtil.showToast((String) o);
                    });
                });
    }

    TextWatcher textWatcher = new TextWatcher() {
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

    public static void instance(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void instance(Context context, String loginType) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra("loginType", loginType);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void instance(Context context, Class<?> intentClass, @Nullable Bundle bundle) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentParam.LOGIN_INTENT_CLASS,intentClass);
        if (bundle!=null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_sure, R.id.tv_login_regist, R.id.btn_weixin_login, R.id.tv_login_psw_forget,
            R.id.tv_password_login, R.id.tv_sms_login, R.id.ic_pws_visiable, R.id.ic_pws_unvisiable,
            R.id.tv_sendcode, R.id.tv_user_agreement, R.id.tv_privacy_policy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                if (!tv_agree.isChecked()){
                    ToastUtil.showToast("请先阅读并勾选同意协议选框");
                    return;
                }
                if (isPasswordLogin) {
                    if (LoginType == 1) {
                        loginByName();
                    }
                } else {
                    // 短信验证码登录
                    loginBySms();
                }
                break;
            case R.id.tv_login_regist:
                RegistActivity.instance(LoginActivity.this);
                break;
            case R.id.tv_login_psw_forget:
                ForgetPwd1Activity.instance(LoginActivity.this);
                break;
            case R.id.btn_weixin_login:
                if (!tv_agree.isChecked()){
                    ToastUtil.showToast("请先阅读并勾选同意协议选框");
                    return;
                }
                WechatPlatform.weChatAuth(this);
                break;
            case R.id.tv_password_login:  // 密码登录
//                passwordLoginTv.setTextColor(getResources().getColor(R.color.login_tab_selected));
//                passwordLoginTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                smsLoginTv.setTextColor(getResources().getColor(R.color.login_tab));
//                smsLoginTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                passwordLoginTv.setVisibility(View.GONE);
                smsLoginTv.setVisibility(View.VISIBLE);
                ll_password.setVisibility(View.VISIBLE);
                ll_sms.setVisibility(View.GONE);
                isPasswordLogin = true;
                break;
            case R.id.tv_sms_login: // 短信登录
//                smsLoginTv.setTextColor(getResources().getColor(R.color.login_tab_selected));
//                smsLoginTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
//                passwordLoginTv.setTextColor(getResources().getColor(R.color.login_tab));
//                passwordLoginTv.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                passwordLoginTv.setVisibility(View.VISIBLE);
                smsLoginTv.setVisibility(View.GONE);
                ll_sms.setVisibility(View.VISIBLE);
                ll_password.setVisibility(View.GONE);
                isPasswordLogin = false;
                break;
            case R.id.ic_pws_visiable:
                icPwsVisiable.setVisibility(View.GONE);
                icPwsUnvisiable.setVisibility(View.VISIBLE);
                etPsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                break;
            case R.id.ic_pws_unvisiable:
                icPwsVisiable.setVisibility(View.VISIBLE);
                icPwsUnvisiable.setVisibility(View.GONE);
                etPsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                break;
            case R.id.tv_sendcode:
                //倒计时
                if (TextUtils.isEmpty(etName.getText().toString().trim()) || !BaseUtil.isChinaMainlandPhone(etName.getText().toString().trim())) {
                    Toast.makeText(aty, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPhonePwd();
                break;
            case R.id.tv_user_agreement:
//                Html5Activity.newIntent(aty, "用户协议", Constants.treaty, false);
                getCompanyPrivacy("体安用户协议");
                break;
            case R.id.tv_privacy_policy:
                getCompanyPrivacy("隐私协议");
                break;
            default:
                break;
        }
    }

    /**
     * 账号密码登录
     */
    public void loginByName() {
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", etName.getText().toString().trim());
//        L.i("密码MD5加密：" + Md5Utils.encryptH(etPsw.getText().toString().trim()));
        hashMap.put("password", Md5Utils.encryptH(etPsw.getText().toString().trim()));

        String regId = SPCache.getString(SPCache.KEY_JPUSH_REG_ID,"");//JPushInterface.getRegistrationID(this);
        Logger.e("111","regId "+regId);
//        SPCache.putString("deviceToken", regId);
//        String deviceToken = SPCache.getString("deviceToken", "");
//        Logger.e("111","loginByName_deviceToken=="+deviceToken);
        //先查找本地ID，如被清除再从极光API上获取
        hashMap.put("deviceToken", TextUtils.isEmpty(regId)?JPushInterface.getRegistrationID(this):regId);
        hashMap.put("terminal", Constants.MY_TERMINAL);
        hashMap.put("deviceMark", CarOnlyIdUtils.getANDROID_ID(this));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().login(hashMap),
                new CallBack<ComModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在登录..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(ComModel data) {
                        hideWaitDialog();
                        Toast.makeText(LoginActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                        if (data.code == 1) {
                            SPCache.putString("token", data.getData());
                            getpersonInfo();
                        }
                    }
                });
    }

    /**
     * 短信验证码登录
     */
    public void loginBySms() {
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", etName.getText().toString().trim());
        hashMap.put("messageCode", etPhonePsws.getText().toString().trim());
        String deviceToken = JPushInterface.getRegistrationID(this);//SPCache.getString("deviceToken", "");
        hashMap.put("deviceToken", deviceToken);
        hashMap.put("terminal", "2");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().dynamicLogin(hashMap),
                new CallBack<ComModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在登录..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(ComModel data) {
                        hideWaitDialog();
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            SPCache.putString("token", data.data);
                            getpersonInfo();
                        }
                    }
                });
    }

    /**
     * 授权第三方登录
     */
    public void loginByAuthor(WeChatAuthModel model,String type){
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("openId", model.openid);
        hashMap.put("loginType", type.equals(ThirdAuthorEnum.WeChat.toString())?"1":"2");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isExist(hashMap),
                new CallBack<BaseModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在登录..");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(BaseModel data) {
                        hideWaitDialog();
                        if (data.code == 800){
                            BangDingPhoneActivity.instance(LoginActivity.this,
                                    type.equals(ThirdAuthorEnum.WeChat.toString())?1:type.equals(ThirdAuthorEnum.TencentQQ.toString())?2:3
                                    ,model.openid,model.nickname);
                        }else if (data.code == 1){
                            String token = (String)data.data;
                            if (!TextUtils.isEmpty(token)){
                                SPCache.putString(SPCache.KEY_TOKEN, token);
                                loginType = "";
                                getpersonInfo();
                            }
                        }

                        ToastUtil.showToast(data.msg);
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
                        if (data.getCode() == 10086) {
                            LoginActivity.instance(LoginActivity.this);
                            SPCache.putString(SPCache.KEY_TOKEN,"");
                            return;
                        }
                        if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                        }
                        UserInfo.DataBean userInfo = data.getData();
                        userInfo.save();

                        setJPushAlias(userInfo.getLoginName());//登录成功后设置推送别名
                        RxBus.getInstance().post(RxEvent.ON_LOG_IN_SUCCESS,userInfo);
                        gotoIntentActivity();
                        goBack();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 查询两个编辑框输入是否为空
     */
    public void checkState() {
        if (isPasswordLogin) {
            if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())) {
                tvSure.setEnabled(true);
            } else {
                tvSure.setEnabled(false);
            }
        } else {
            if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPhonePsws.getText().toString())) {
                tvSure.setEnabled(true);
            } else {
                tvSure.setEnabled(false);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 开始发送验证码
     */
    public void sendPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", etName.getText().toString().toString());
        hashMap.put("sign", Md5Utils.encryptH(etName.getText().toString() + "4gzhealthy"));
        hashMap.put("type", "4");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getRegisterCode(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
//                            setdownCount(count);
                            tvSendcode.startCountTime();
                        }
                        ToastUtil.showToast(data.msg);
                    }
                });
    }



    private void getCompanyPrivacy(String title){
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getPlatformAgreement(title)
                , new CallBack<CompanyPrivacy>() {
                    @Override
                    public void onResponse(CompanyPrivacy data) {
                        AboutHealthContentActivity.instance(LoginActivity.this,title,data.data);
                    }
                });
    }


    /**
     *  设置JPush别名
     *  sequence
     * 用户自定义的操作序列号，同操作结果一起返回，用来标识一次操作的唯一性，推荐每次都用不同的数字序号。
     * alias
     * 每次调用设置有效的别名，覆盖之前的设置。
     * 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     * 限制：alias 命名长度限制为 40 字节。（判断长度需采用 UTF-8 编码）
     * T60006006_推$  T10006007_推$
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

        JPushInterface.setAlias(LoginActivity.this,Integer.valueOf(uid),builder.toString());
        if (JPushInterface.isPushStopped(this)){
            JPushInterface.resumePush(this);
        }
    }


    /**
     * 负责跳转传入的Activity
     */
    private void gotoIntentActivity(){
        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle ==null)
                showActivity(LoginActivity.this, intentClass);
            else {
                showActivity(LoginActivity.this, intentClass,bundle);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }finally {
            goBack();
        }
    }

}

