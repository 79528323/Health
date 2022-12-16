package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.AboutHealthContentActivity;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WeChatAuthModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.BaseUtil;
import com.gzhealthy.health.utils.ThirdAuthorEnum;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.ValidateCodeView;

import org.litepal.crud.DataSupport;

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

public class RegistActivity extends BaseAct {

    //发送验证码倒计时
    int count = 60;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_phone_psws)
    EditText etPhonePsws;
    @BindView(R.id.tv_sendcode)
    ValidateCodeView tvSendcode;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.agree)
    CheckBox checkBox;

//    private Subscription time_sub;

//    private UMShareAPI umShareAPI;

    private String Tag = RegistActivity.class.getSimpleName();

    private String loginType;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("注册");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(RegistActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etName.addTextChangedListener(TextWatcher);
        etPhonePsws.addTextChangedListener(TextWatcher);
        etName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        onSubscribe();
    }


    /**
     * 订阅事件
     */
    private void onSubscribe(){
        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_SUCCESS)
                .subscribe((Action1<Object>) o -> {
                    WeChatAuthModel model = (WeChatAuthModel) o;
                    registerByAuthor(model, ThirdAuthorEnum.WeChat.toString());
                });

        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_FAIL)
                .subscribe((Action1<Object>) o -> {
                    runOnUiThread(() -> {
                        ToastUtil.showToast((String) o);
                    });
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_regist;
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
    public static void instance(Context context) {
        Intent intent = new Intent(context, RegistActivity.class);
        context.startActivity(intent);
    }


    @OnClick({R.id.tv_sendcode, R.id.tv_sure, R.id.tv_user_agreement, R.id.tv_privacy_policy, R.id.btn_weixin_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendcode:
                //倒计时
//                if (TextUtils.isEmpty(etName.getText().toString().trim()) || !RegexUtils.isMobileExact(etName.getText().toString().trim())) {
//                    Toast.makeText(aty, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (TextUtils.isEmpty(etName.getText().toString().trim()) || !BaseUtil.isChinaMainlandPhone(etName.getText().toString().trim())) {
                    Toast.makeText(aty, "请填写正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendPhonePwd();
                break;
            case R.id.btn_weixin_login:
                if (!checkBox.isChecked()){
                    ToastUtil.showToast("请先阅读并勾选同意协议选框");
                    return;
                }
                WechatPlatform.weChatAuth(this);
                break;
            case R.id.tv_user_agreement:
                getCompanyPrivacy("体安用户协议");
//                Html5Activity.newIntent(aty, "用户协议", Constants.treaty, false);
                break;
            case R.id.tv_privacy_policy:
                getCompanyPrivacy("隐私协议");
//                Html5Activity.newIntent(aty, "隐私政策", Constants.privacyPolicy, false);
                break;
            case R.id.tv_sure:
                if (!checkBox.isChecked()){
                    ToastUtil.showToast("请先阅读并勾选同意协议选框");
                    return;
                }
                Regist2Activity.instance(this, etName.getText().toString().trim(), etPhonePsws.getText().toString().trim());
                break;
            default:
                break;
        }
    }

    /**
     * 授权第三方登录
     */
    public void registerByAuthor(WeChatAuthModel model,String type){
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("openId", model.openid);
        hashMap.put("loginType", type.equals(ThirdAuthorEnum.WeChat.toString())?"1":"2");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isExist(hashMap),
                new CallBack<BaseModel>() {

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(BaseModel data) {
                        hideWaitDialog();
                        if (data.code == 800){
                            BangDingPhoneActivity.instance(RegistActivity.this,
                                    type.equals(ThirdAuthorEnum.WeChat.toString())?1:type.equals(ThirdAuthorEnum.TencentQQ.toString())?2:3
                                    ,model.openid,model.nickname);
                        }else if (data.code == 1){
                            String token = (String)data.data;
                            if (!TextUtils.isEmpty(token)){
                                SPCache.putString(SPCache.KEY_TOKEN, token);
                                loginType = "";
//                                SPCache.putBoolean("islogin", true);
                                getpersonInfo();
                            }
                        }

                        ToastUtil.showToast(data.msg);
                    }
                });
    }

    /**
     * 开始发送验证码
     */
    public void sendPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", etName.getText().toString().toString());
        hashMap.put("sign", Md5Utils.encryptH(etName.getText().toString() + "1gzhealthy"));
        hashMap.put("type", "1");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getRegisterCode(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
                            tvSendcode.startCountTime();
                        }
                        Toast.makeText(RegistActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 查询两个个编辑框输入是否为空
     */
    public void checkState() {
        if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPhonePsws.getText().toString())) {
            tvSure.setEnabled(true);
        } else {
            tvSure.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //关闭倒计时线程
//        if (null != time_sub) {
//            time_sub.unsubscribe();
//        }
    }

    /**
     * 授权
     */
//    public void authorization(SHARE_MEDIA share_media) {
//        umShareAPI = UMShareAPI.get(RegistActivity.this);
//        umShareAPI.doOauthVerify(RegistActivity.this, share_media, umAuthListener);
//    }
//
//    UMAuthListener umAuthListener = new UMAuthListener() {
//        @Override
//        public void onStart(SHARE_MEDIA share_media) {
//            Logger.e(Tag, "授权开始");
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            Toast.makeText(RegistActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//            if (share_media == SHARE_MEDIA.QQ) {
//                judgeIsbandingBefore(2, map.get("uid"));
//            } else if (share_media == SHARE_MEDIA.WEIXIN) {
//                if (!TextUtils.isEmpty(map.get("uid"))) {
//                    judgeIsbandingBefore(1, map.get("uid"));
//                } else {
//                    judgeIsbandingBefore(1, map.get("openid"));
//                }
//            } else {//新浪微博的
//                judgeIsbandingBefore(3, map.get("uid"));
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            Logger.e(Tag, "授权失败" + throwable.getMessage());
//            Toast.makeText(RegistActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//            Logger.e(Tag, "授权取消");
//        }
//    };

//    /**
//     * 判断三方登录是否绑定过手机号码
//     */
//    public void judgeIsbandingBefore(int type, String id) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("loginType", type + "");
//        hashMap.put("openId", id);
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isExist(hashMap),
//                new CallBack<ComModel>() {
//                    @Override
//                    public void onResponse(ComModel data) {
//                        Toast.makeText(RegistActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
//                        if (data.getCode() == 1) {
//                            //绑定过
//                            SPCache.putString("token", data.getData());
//                            SPCache.putBoolean("islogin", true);
//                            getpersonInfo();
//                            Intent in = new Intent(RegistActivity.this, HomeActivity.class);
//                            in.putExtra("initPos", "0");
//                            showActivity(RegistActivity.this, HomeActivity.class);
//                            finish();
//                        } else {
//                            BangDingPhoneActivity.instance(RegistActivity.this, type, id);
//                        }
//                    }
//                });
//    }


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
                            LoginActivity.instance(RegistActivity.this);
                            SPCache.putString(SPCache.KEY_TOKEN,"");
                            return;
                        }
                        if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                        }
                        UserInfo.DataBean userInfo = new UserInfo.DataBean();
                        userInfo = data.getData();
                        userInfo.save();
                        //登录成功
                        if (null != loginType) {
                            //被抢登成功 统一跳个人中心
                            Intent in = new Intent(RegistActivity.this, HomeActivity.class);
                            in.putExtra("initPos", "3");
                            showActivity(RegistActivity.this, HomeActivity.class);
                        } else {
                            //登录成功 更新页面广播 目前看首页 目的地首页 都不需要用到更新
                            // 广播有发送 还没去接收 后面有需要只要接收这个广播做数据更新就好
                            Intent intent = new Intent();
                            intent.setAction(Constants.Receiver.INTENT_ACTION_MONEY_VISIBILITY);
                            sendBroadcast(intent);
                            // 登录成功跳转到首页
                            Intent in = new Intent(RegistActivity.this, HomeActivity.class);
                            in.putExtra("initPos", "0");
                            showActivity(RegistActivity.this, HomeActivity.class);
                        }
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
     * 获取平台协议
     * @param title
     */
    private void getCompanyPrivacy(String title){
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getPlatformAgreement(title)
                , new CallBack<CompanyPrivacy>() {
                    @Override
                    public void onResponse(CompanyPrivacy data) {
                        AboutHealthContentActivity.instance(RegistActivity.this,title,data.data);
                    }
                });
    }

}
