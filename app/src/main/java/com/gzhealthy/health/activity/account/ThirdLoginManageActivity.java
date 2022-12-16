package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WeChatAuthModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.NiUBaiBankDialog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.functions.Action1;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * 第三方登录
 */
public class ThirdLoginManageActivity extends BaseAct {

    @BindView(R.id.tv_weixin_bind)
    TextView tvWeixinBind;
    @BindView(R.id.rl_weixin_bind)
    RelativeLayout rlWeixinBind;
    @BindView(R.id.tv_qq_bing)
    TextView tvQqBing;
    @BindView(R.id.rl_qq_bind)
    RelativeLayout rlQqBind;
    @BindView(R.id.tv_sina_bind)
    TextView tvSinaBind;
    @BindView(R.id.rl_sina_bind)
    RelativeLayout rlSinaBind;
    private UserInfo.DataBean dataBean;
//    private UMShareAPI umShareAPI;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_third_loginmanage;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        setTitle("第三方登录账号管理");
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        dataBean = findFirst(UserInfo.DataBean.class);
        loadingPageView.showPage();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(ThirdLoginManageActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        onSubscribe();
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, ThirdLoginManageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 解绑
     */
    public void unBanding(int type, String openId) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type", "" + type);
        hashMap.put("openId", openId);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().untie(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                Toast.makeText(ThirdLoginManageActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
                    switch (type) {
                        case 1:
                            tvWeixinBind.setText("立即绑定");
                            getpersonInfo();
                            break;
                        case 2:
                            tvQqBing.setText("立即绑定");
                            getpersonInfo();
                            break;
                        case 3:
                            tvSinaBind.setText("立即绑定");
                            getpersonInfo();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    @OnClick({R.id.rl_weixin_bind, R.id.rl_qq_bind, R.id.rl_sina_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_weixin_bind:
                if (!TextUtils.isEmpty(dataBean.getWeChat())) {
                    showUnBindTip(1, dataBean.getWeChat());
                } else {
//                    authorization(SHARE_MEDIA.WEIXIN);
                }
                break;
            case R.id.rl_qq_bind:
                if (!TextUtils.isEmpty(dataBean.getQq())) {
                    showUnBindTip(2, dataBean.getQq());
                } else {
//                    authorization(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.rl_sina_bind:
                if (!TextUtils.isEmpty(dataBean.getSina())) {
                    showUnBindTip(3, dataBean.getSina());
                } else {
//                    authorization(SHARE_MEDIA.SINA);
                }
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
                    bindNo(1, model);
//                    judgeIsbandingBefore(Constants.OAuth.TYPE_WECHAT,model);
                });
    }

    /**
     * 授权
     */
//    public void authorization(SHARE_MEDIA share_media) {
////        umShareAPI = UMShareAPI.get(ThirdLoginManageActivity.this);
////        umShareAPI.doOauthVerify(ThirdLoginManageActivity.this, share_media, umAuthListener);
//        WechatPlatform.weChatAuth(this);
//    }

//    UMAuthListener umAuthListener = new UMAuthListener() {
//        @Override
//        public void onStart(SHARE_MEDIA share_media) {
//
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
//            Toast.makeText(ThirdLoginManageActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
////            if (share_media == SHARE_MEDIA.QQ) {
////                bindNo(2, map.get("uid"));
////            } else if (share_media == SHARE_MEDIA.WEIXIN) {
////                if (TextUtils.isEmpty(map.get("uid"))) {
////                    bindNo(1, map.get("openid"));
////                } else {
////                    bindNo(1, map.get("uid"));
////                }
////            } else {
////                bindNo(3, map.get("uid"));
////            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
//            Toast.makeText(ThirdLoginManageActivity.this, "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA share_media, int i) {
//        }
//    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        umShareAPI.onActivityResult(requestCode, resultCode, data);
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
                        dataBean = data.getData();
                        if (TextUtils.isEmpty(dataBean.getQq())) {//QQ
                            tvQqBing.setText("立即绑定");
                        } else {
                            tvQqBing.setText("已绑定");
                        }
                        if (TextUtils.isEmpty(dataBean.getWeChat())) {//微信
                            tvWeixinBind.setText("立即绑定");
                        } else {
                            tvWeixinBind.setText("已绑定");
//                            upadatePersonInfo();
                        }
                        if (TextUtils.isEmpty(dataBean.getSina())) {//微博
                            tvSinaBind.setText("立即绑定");
                        } else {
                            tvSinaBind.setText("已绑定");
                        }
                    }
                });

    }


    /**
     * 更新个人信息
     */
    public void upadatePersonInfo(WeChatAuthModel model) {
        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("nickName", model.nickname);
        hashMap.put("sex",String.valueOf(model.sex));
        hashMap.put("headPic",model.headimgurl);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(hashMap));

        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().editAccountInfo(body,SPCache.getString("token", "")),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
                            getpersonInfo();
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getpersonInfo();
    }

    /**
     * bindNo
     */
//    public void bindNo(int type, String openid) {
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("token", SPCache.getString("token", ""));
//        hashMap.put("type", "" + type);
//        hashMap.put("openId", openid);
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().yunPay(hashMap), new CallBack<ComModel>() {
//            @Override
//            public void onResponse(ComModel data) {
//                Toast.makeText(ThirdLoginManageActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
//                if (data.code == 1){
////                    upadatePersonInfo();
//                }
////                getpersonInfo();
//            }
//        });
//    }

    public void bindNo(int type, WeChatAuthModel model) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type", "" + type);
        hashMap.put("openId", model.openid);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().yunPay(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                Toast.makeText(ThirdLoginManageActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.code == 1){
                    upadatePersonInfo(model);
                }
//                getpersonInfo();
            }
        });
    }

    public void showUnBindTip(int type, String openId) {
        NiUBaiBankDialog niUBaiBankDialog = new NiUBaiBankDialog(ThirdLoginManageActivity.this);
        niUBaiBankDialog.setTitles("温馨提示");
        niUBaiBankDialog.setContennt("是否确认解绑");
        niUBaiBankDialog.setleftOnClick("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                niUBaiBankDialog.dismiss();
            }
        });
        niUBaiBankDialog.setrightOnClick("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                niUBaiBankDialog.dismiss();
                unBanding(type, openId);
            }
        });
        niUBaiBankDialog.show();
    }
}
