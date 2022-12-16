package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.BangDingPhoneActivity;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.activity.account.LoginPwsSettingActivity;
import com.gzhealthy.health.activity.account.ThirdLoginManageActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WeChatAuthModel;
import com.gzhealthy.health.tool.CacheClearTools;
import com.gzhealthy.health.tool.CacheUtils;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.CheckNetwork;
import com.gzhealthy.health.utils.ThirdAuthorEnum;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.CustomDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.functions.Action1;
import top.limuyang2.customldialog.IOSMsgDialog;
import top.limuyang2.customldialog.MaterialMsgDialog;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.OnDialogDismissListener;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * 账户安全
 */
public class AccountSafeActivity extends BaseAct {

    @BindView(R.id.password)
    LinearLayout linear_password;

    @BindView(R.id.usr_id)
    TextView tv_usr_id;

    @BindView(R.id.wechat_id)
    TextView tv_wechat_id;

    @BindView(R.id.phone_num)
    TextView tv_phone_num;

    @BindView(R.id.set_pwd)
    TextView tv_set_pwd;

    UserInfo.DataBean bean;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_account_safe;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("账号与安全");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        initView();
        onSubscribe();
    }


    public void initView(){
        bean = DataSupport.findFirst(UserInfo.DataBean.class);
        if (bean!=null){
            tv_usr_id.setText(bean.getLoginName());
            String phone = bean.getPhone();
            StringBuilder builder  = new StringBuilder();
            builder.append(phone.substring(0,3))
                    .append("****")
                    .append(phone.substring(7));
            tv_phone_num.setText(builder.toString());
            tv_wechat_id.setText(TextUtils.isEmpty(bean.getWechatNickName())?"未绑定":bean.getWechatNickName());
            tv_set_pwd.setText(TextUtils.equals(bean.getPassword(),"1")?"已设置":"未设置");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-账号与安全");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-账号与安全");
    }

    @OnClick({R.id.password, R.id.wechat_id, R.id.phone_num})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.password:
                LoginPwsSettingActivity.instance(this);
                break;

            case R.id.wechat_id:
                if (TextUtils.isEmpty(bean.getWeChat())){
                    WechatPlatform.weChatAuth(this);
                }else
                    showUnBindDailog();
                break;

            case R.id.phone_num:
                ChangePhoneActivity.instance(this);
                break;
            default:
                break;
        }
    }



    /**
     * 订阅事件
     */
    private void onSubscribe(){
        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_SUCCESS)
                .subscribe((Action1<Object>) o -> {
                    WeChatAuthModel model = (WeChatAuthModel) o;
//                    loginByAuthor(model, ThirdAuthorEnum.WeChat.toString());
                    bindWeChat(1,model);
                });

        rxManager.onRxEvent(RxEvent.WECHAT_ON_AUTH_LOGIN_FAIL)
                .subscribe((Action1<Object>) o -> {
                    runOnUiThread(() -> {
                        ToastUtil.showToast((String) o);
                    });
                });
    }


    private void showUnBindDailog(){
        LDialog.Companion.init(getSupportFragmentManager())
                .setBackgroundDrawableRes(R.drawable.bg_unbind_wechat)
                .setLayoutRes(R.layout.dialog_unbind_wechat)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            unBind();
                            baseLDialog.dismiss();
                        });
                    }
                })
                .show();
    }


    private void unBind(){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type","1");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().untie(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.code == 1){
                            tv_wechat_id.setText("未绑定");
                            getpersonInfo();
                        }
                        ToastUtil.showToast(data.msg);
                    }
                });
    }


    public void bindWeChat(int type, WeChatAuthModel model) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type", "" + type);
        hashMap.put("openId", model.openid);
        hashMap.put("wechatNickName",model.nickname);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().yunPay(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                if (data.code == 1){
                    getpersonInfo();
                }
                ToastUtil.showToast(data.msg);
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
                        if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                            DataSupport.deleteAll(UserInfo.DataBean.class);
                        }
                        UserInfo.DataBean userInfo = data.getData();
                        userInfo.save();
                        bean= DataSupport.findFirst(UserInfo.DataBean.class);
                        String wechatName = userInfo.getWechatNickName();
                        tv_wechat_id.setText(TextUtils.isEmpty(wechatName)?"未绑定":wechatName);
                    }
                });
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, AccountSafeActivity.class);
        context.startActivity(intent);
    }


}
