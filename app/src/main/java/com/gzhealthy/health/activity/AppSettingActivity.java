package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.CleanUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.activity.account.LoginPwsSettingActivity;
import com.gzhealthy.health.activity.account.ThirdLoginManageActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.model.home.VersionModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.Tools;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.CheckNetwork;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.UpdateAppHttpUtil;
import com.gzhealthy.health.widget.CustomDialogBuilder;
import com.gzhealthy.health.widget.UpdateVersionDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import net.fkm.updateapp.UpdateAppManager;
import net.fkm.updateapp.listener.ExceptionHandler;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import rx.functions.Func1;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

/**
 * 设置界面
 */
public class AppSettingActivity extends BaseAct {

    @BindView(R.id.rl_pws_setting)
    RelativeLayout rlPwsSetting;
    @BindView(R.id.rl_third_login)
    RelativeLayout rlThirdLogin;
    @BindView(R.id.rl_message)
    RelativeLayout rlMessage;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rl_new_band)
    RelativeLayout rlNewBand;
    @BindView(R.id.re_clear)
    RelativeLayout reClear;
    @BindView(R.id.version_update)
    RelativeLayout version_update;
    @BindView(R.id.rl_loginout)
    TextView rlLoginout;
    public boolean messageIsOpen;
    @BindView(R.id.img_message)
    ImageView imgMessage;
    private CustomDialogBuilder customDialogBuilder;
    private TextView tv_clear;
    private boolean islogin;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_appsetting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        islogin = !TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""));
        messageIsOpen = SPCache.getBoolean("isMessageIsOpen", true);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("设置");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(AppSettingActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        loadingPageView.findViewById(R.id.rl_loginout_lay).setVisibility(islogin ? View.VISIBLE : View.GONE);
        loadingPageView.findViewById(R.id.rl_loginoff_lay).setVisibility(islogin ? View.VISIBLE : View.GONE);
        loadingPageView.findViewById(R.id.rl_logoff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!islogin) {
                    ToastUtils.showShort("请先登录");
                    return;
                }
                if (!CheckNetwork.isNetworkConnected(AppSettingActivity.this)) {
                    ToastUtil.showToastLong(getString(R.string.network_unavailable));
                    return;
                }
//                showLogoutDialog();
                showUnBindDailog();
            }
        });

        String version = Tools.getVersion(this);
        String bg_ver = SPCache.getString(SPCache.KEY_APP_VER, "");
        version_update.setVisibility(!bg_ver.equals(version) ? View.VISIBLE : View.GONE);
        tv_clear = (TextView) loadingPageView.findViewById(R.id.tv_clear);
        reClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCache();
            }
        });
        if (!islogin) {
            loadingPageView.findViewById(R.id.rl_loginout).setEnabled(false);
        } else {
            loadingPageView.findViewById(R.id.rl_loginout).setEnabled(true);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-设置");
        tv_clear.setText(FileUtils.getSize(PathUtils.getExternalAppDataPath()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-设置");
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, AppSettingActivity.class);
        context.startActivity(intent);
    }


    @OnClick({R.id.rl_pws_setting, R.id.rl_third_login, R.id.rl_message, R.id.rl_new_band, R.id.rl_loginout, R.id.version_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.version_update:
                getVersionUpdate();
                break;
            case R.id.rl_pws_setting:
                if (islogin) {
                    LoginPwsSettingActivity.instance(AppSettingActivity.this);
                } else {
                    LoginActivity.instance(AppSettingActivity.this);
                }
                break;
            case R.id.rl_third_login:
                if (islogin) {
                    ThirdLoginManageActivity.instance(AppSettingActivity.this);
                } else {
                    LoginActivity.instance(AppSettingActivity.this);
                }
                break;
            case R.id.rl_message:
                if (messageIsOpen) {
                    imgMessage.setImageResource(R.mipmap.ic_general_switch_pressed);
                    SPCache.putBoolean("isMessageIsOpen", true);
                } else {
                    imgMessage.setImageResource(R.mipmap.ic_general_switch_normal);
                    SPCache.putBoolean("isMessageIsOpen", false);
                }
                messageIsOpen = !messageIsOpen;
                break;
            case R.id.rl_new_band:
                AboutHealthActivity.instance(AppSettingActivity.this);
                break;
            case R.id.rl_loginout:
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_MESSAGE_BDAGE);
                RxBus.getInstance().postEmpty(RxEvent.REFRESH_BACKGROUND_MESSAGE_BDAGE);
                SPCache.putString(SPCache.KEY_TOKEN, "");
                JPushInterface.setAlias(this,Integer.parseInt(DataSupport.findFirst(UserInfo.DataBean.class).getLoginName()),"");
//                JPushInterface.deleteAlias(this, Integer.parseInt(DataSupport.findFirst(UserInfo.DataBean.class).getLoginName()));
//                JPushInterface.stopPush(this);
                DataSupport.deleteAll(UserInfo.DataBean.class);
                RxBus.getInstance().postEmpty(RxEvent.ON_LOG_OUT);
                Intent intent = new Intent(AppSettingActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    private void getVersionUpdate() {
        String versionCode = String.valueOf(Tools.getVersionCode(this));
        Map<String, String> params = new HashMap<>();
        params.put("type", Constants.MY_TERMINAL);//2->Android
        params.put("versionNum", versionCode);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi()
                .getAppVersion(params).map(new Func1<VersionModel, VersionModel>() {
                    @Override
                    public VersionModel call(VersionModel homeDrawDetailModel) {
                        return homeDrawDetailModel;
                    }
                }), new CallBack<VersionModel>() {
            private UpdateVersionDialogBuilder customDialogBuilder;

            @Override
            public void onResponse(VersionModel data) {
                if (data.getCode() == 1) {
                    if (TextUtils.equals("1", data.getData().getState())) {
                        try {
                            updateApp();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        ToastUtil.showToast("暂无更新");
                    }
                }
            }
        });

    }

    /**
     * 更新/下载APK
     */
    private void updateApp() {
        int versionCode = Tools.getVersionCode(this);
        Map<String, String> params = new HashMap<>();
        params.put("type", Constants.MY_TERMINAL);//2->Android
        params.put("versionNum", String.valueOf(versionCode));
        new UpdateAppManager
                .Builder()
                .setParams(params)
                .setActivity(this).setUpdateUrl(Constants.Api.BASE_URL1 + "/api/appInfo/getAppVersion")
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                }).setHttpManager(new UpdateAppHttpUtil()).build().update();
    }

    private void ClearCache() {
        customDialogBuilder = new CustomDialogBuilder(aty)
                .isCancelableOnTouchOutside(false)
                .withTitle("清除缓存")
                .withMessage("目前缓存共有" + FileUtils.getSize(PathUtils.getExternalAppDataPath()) + "\n\n是否确认清除？")
                .withCancelText("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialogBuilder.dismiss();
                    }
                }).withComfirmText("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialogBuilder.dismiss();
                        new ClearCacheTask().execute(AppSettingActivity.this);
                    }
                });
        customDialogBuilder.show();
    }


    private void loginOut() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().logout(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在注销");
                    }

                    @Override
                    public void onResponse(BaseModel data) {
                        hideWaitDialog();
                        if (data.code == 1) {
                            SPCache.putString(SPCache.KEY_TOKEN, "");
                            LoginActivity.instance(AppSettingActivity.this);
                            finish();
                        }
                        ToastUtil.showToast(data.msg);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }
                });
    }

    class ClearCacheTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showWaitDialog();
        }

        @Override
        protected Void doInBackground(Context... args) {
            CleanUtils.cleanCustomDir(PathUtils.getExternalAppDataPath());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            hideWaitDialog();
            ToastUtils.showShort("缓存清除成功");
            tv_clear.setText("0B");
        }
    }


    private void showUnBindDailog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_unbind_watch, null);
        TextView title = view.findViewById(R.id.title);
        title.setText("注销账号后，该账号将不能再使用及登录");

        TextView btn_cancel = view.findViewById(R.id.cancel);
        TextView btn_confirm = view.findViewById(R.id.confirm);
        btn_confirm.setText("确认注销");
        btn_cancel.setText("考虑一下");

        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutView(view)
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
                            baseLDialog.dismiss();
                            loginOut();
                        });
                    }
                })
                .show();
    }

//    private void showLogoutDialog() {
//        IOSMsgDialog.Companion.init(getSupportFragmentManager())
////                .setTitle("注销账号提示")
//                .setMessage("注销账号后，该账号将不能再使用及登录")
//                .setAnimStyle(R.style.LDialogScaleAnimation)
//                .setNegativeButton("再考虑一下", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    }
//                })
//                .setPositiveButton("确认注销", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        MaterialMsgDialog.Companion.init(getSupportFragmentManager())
////                                .setMessage("您的账号注销信息已提交成功，后台管理人员正在审核中，符合注销账号的用户预计三到五个工作日之内完成注销的操作。")
////                                .setCancelableAll(false)
////                                .setPositiveButton("确定", new View.OnClickListener() {
////                                    @Override
////                                    public void onClick(View v) {
////                                        startActivity(new Intent(AppSettingActivity.this, LoginActivity.class));
////                                    }
////                                }).show();
//
//
//
//                    }
//                }, Color.RED)
//                .setDismissListener(new OnDialogDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialog) {
//                    }
//                }).setCancelableOutside(true).show();
//    }

}
