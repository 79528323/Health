package com.gzhealthy.health.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.adapter.FragmentAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.PelletFrameModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.model.home.ConfigModel;
import com.gzhealthy.health.model.home.VersionModel;
import com.gzhealthy.health.receiver.MainReceive;
import com.gzhealthy.health.service.ExitAppService;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.Tools;
import com.gzhealthy.health.utils.UpdateAppHttpUtil;
import com.gzhealthy.health.widget.HomeDialog;
import com.gzhealthy.health.widget.UpdateVersionDialogBuilder;
import com.umeng.commonsdk.UMConfigure;

import net.fkm.updateapp.UpdateAppManager;
import net.fkm.updateapp.listener.ExceptionHandler;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.helper.Logger;
import rx.functions.Func1;

import static com.gzhealthy.health.base.Constants.BUSSNESS_RECODE;
import static com.gzhealthy.health.base.Constants.CruiseUrl;
import static com.gzhealthy.health.base.Constants.CustomerServiceUrl;
import static com.gzhealthy.health.base.Constants.FEIDOU_CURTURE;
import static com.gzhealthy.health.base.Constants.LIVE_PLACE;
import static com.gzhealthy.health.base.Constants.SharePreference.ISOPENNEW;
import static com.gzhealthy.health.base.Constants.SharePreference.NEWSHOPURL;
import static com.gzhealthy.health.base.Constants.SharePreference.OLDMAALSHOP;
import static com.gzhealthy.health.base.Constants.ShoptionUrl;
import static com.gzhealthy.health.base.Constants.TASK_CENTER;
import static com.gzhealthy.health.base.Constants.askService;
import static com.gzhealthy.health.base.Constants.gonggaoUrl;

public class HomeActivity extends BaseAct {

    private final String TAG = HomeActivity.class.getSimpleName();
    private TabLayout tbBottom;
    private FragmentAdapter adapter;
    private FrameLayout FgContainer;
    public int initPos = 0;

    private Handler clientHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.showShort(getString(R.string.quit_app));
                    break;
                case 1:
                    Intent intent = new Intent(Constants.Receiver.INTENT_ACTION_EXIT_APP);
                    sendBroadcast(intent);
                    break;
                case 3:
                    Message msg01 = Message.obtain(null, 1);
                    msg01.replyTo = clientMessenger;
                    try {
                        serviceMsg.send(msg01);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
            return false;
        }

    });
    final Messenger clientMessenger = new Messenger(clientHandler);
    private Messenger serviceMsg = null;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            serviceMsg = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMsg = null;
        }
    };

    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }

    @Override
    protected int getView() {
        needSildeExit = false;
        return R.layout.activity_base_normal;
    }

    @Override
    protected int getContentViewId() {
        return R.id.fl_base_container;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_home;
    }

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        PushAgent.getInstance(HomeActivity.this).onAppStart();
//        mImmersionBar.titleBar(getToolBar()).init();
//        mImmersionBar.statusBarColor(R.color.colorPrimary).init();
//        PushAgent mPushAgent = PushAgent.getInstance(this);
        // 注册推送服务，每次调用register方法都会回调该接口

//        String regId = JPushInterface.getRegistrationID(this);
//        Log.e("111","regId="+regId);
    }

    private void addListener() {
        tbBottom.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("tabPos", tab.getPosition() + "");
                int position = tab.getPosition();
                Fragment fragment = (Fragment) adapter.instantiateItem(FgContainer, position);
                adapter.setPrimaryItem(FgContainer, position, fragment);
                adapter.finishUpdate(FgContainer);
                // 修改状态栏
                if (position == 0 || position == 2 || position == 3) {
                    hideOrShowAppbar(true);
                    mImmersionBar.transparentStatusBar().statusBarDarkFont(true).init();
                } else {
                    hideOrShowAppbar(false);
                    hideOrShowToolbar(true);
                    mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init();
                }

                if (position == 1) {
                    //发通知做刷新
//                    EventBus.getDefault().post("update");
                    MainReceive.sendBroadcast(aty);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initTab() {
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tab_main_bottom_item, null);
            TextView tvTabItem = (TextView) view.findViewById(R.id.tv_tab_main_item);
            if (i == 0) {
                tvTabItem.setText("健康");
                tvTabItem.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_tab_main, 0, 0);
            }
            if (i == 1) {
                tvTabItem.setText("资讯");
                tvTabItem.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_tab_destination, 0, 0);
            }
            if (i == 2) {
                tvTabItem.setText("我的");
                tvTabItem.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.btn_tab_selector, 0, 0);
            }
            TabLayout.Tab tab = tbBottom.newTab();
            tab.setCustomView(view);
            tbBottom.addTab(tab, i == initPos ? true : false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_MESSAGE_BDAGE);
        RxBus.getInstance().postEmpty(RxEvent.REFRESH_BACKGROUND_MESSAGE_BDAGE);

        if (intent != null) {
            //接收是否重新登录
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                boolean isReLog = bundle.getBoolean(IntentParam.IS_RETRY_LOGIN, false);
                if (isReLog) {
                    LoginActivity.instance(this);
                }
            }

        }

    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tbBottom = (TabLayout) findViewById(R.id.tb_main_bottom);
        FgContainer = (FrameLayout) findViewById(R.id.home_fragment_container);
        tbBottom.setTabMode(TabLayout.MODE_FIXED);
        tbBottom.setTabGravity(TabLayout.GRAVITY_FILL);
        tbBottom.setTabRippleColor(getResources().getColorStateList(R.color.transparent));
        adapter = new FragmentAdapter(getSupportFragmentManager());
        initPos = getIntent().getIntExtra("initPos", 0);
        addListener();
        initTab();
        hideOrShowToolbar(true);
        setJPushAlias();//设置极光别名
//        getConfig();
        subscribe();
        // 检查和更新应用
//        updateApp();
        getVersionUpdate();
    }

    public void postRouter() {
        new Handler().postDelayed(() -> {
            int type = getIntent().getIntExtra(IntentParam.START_APP_JUMP_SOURCE, -1);
            Logger.e(TAG, "HomeActivity type=" + type);
            if (type > -1) {
                switch (type) {
                    case 0:
                        SosMessageActivity.instance(HomeActivity.this);
                        break;

                    case 1:
                        HealthDataActivity.instance(HomeActivity.this, 3, false);
                        break;
                }
            }
        }, 700);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, ExitAppService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        pelletFrame();
//        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        unbindService(serviceConnection);
//        MobclickAgent.onPause(this);
    }


    @Override
    public void onBackPressed() {
        clientHandler.sendEmptyMessage(3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void getConfig() {
        Map<String, String> params = new HashMap<>();
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().mainConfig(params).map(new Func1<ConfigModel, ConfigModel>() {
                    @Override
                    public ConfigModel call(ConfigModel homeDrawDetailModel) {
                        return homeDrawDetailModel;
                    }
                }), new CallBack<ConfigModel>() {
                    @Override
                    public void onResponse(ConfigModel data) {
                        if (null != data) {
                            if (data.getCode() == 1) {
                                ShoptionUrl = data.getData().getShoppingMallUrl();
                                CruiseUrl = data.getData().getCruiseUrl();
                                gonggaoUrl = data.getData().getNoticeUrl();
                                LIVE_PLACE = data.getData().getLiveBroadcastUrl();
                                FEIDOU_CURTURE = data.getData().getCultureUrl();
                                BUSSNESS_RECODE = data.getData().getBusinessReportUrl();
                                TASK_CENTER = data.getData().getTaskHallUrl();
                                CustomerServiceUrl = data.getData().getCustomerServiceUrl();
                                askService = data.getData().getCustomerServiceUrl();
                                SPCache.putInt(ISOPENNEW, data.getData().getIsOpenNew());
                                SPCache.putString(OLDMAALSHOP, data.getData().getShoppingMallUrl());
                                SPCache.putString(NEWSHOPURL, data.getData().getNewShopUrl());
                            } else if (data.getCode() == 2) {
                            }
                        }

                    }
                });
    }

    /**
     * 检查App是否有新版本
     */
    private void getVersionUpdate() {
        String versionCode = String.valueOf(Tools.getVersionCode(this));
        Map<String, String> params = new HashMap<>();
        params.put("type", String.valueOf("2"));//2->Android
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
                    VersionModel.DataBean bean = data.getData();
                    SPCache.putString(SPCache.KEY_APP_VER, bean.getVersionName());
                    if (bean == null) {
                        postRouter();
                    } else {
                        if (TextUtils.equals("0", bean.getState())) {
                            postRouter();
                        } else {
                            try {
                                updateApp();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    postRouter();
                }
            }
        });

    }

    /**
     * 更新/下载APK
     */
    private void updateApp() {
        Map<String, String> params = new HashMap<>();
        params.put("type", Constants.MY_TERMINAL);//2->Android
        params.put("versionNum", String.valueOf(Constants.UPDATENUM));
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

    private void pelletFrame() {
        if (!isUserLogin()) {
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().pelletFrame(param),
                new CallBack<PelletFrameModel>() {

                    @Override
                    public void onResponse(PelletFrameModel data) {
                        if (data.getCode() == 1) {
                            PelletFrameModel.DataBean bean = data.getData();
                            if (bean.getStatus() == 1) {
                                //展示弹窗
                                switch (data.getData().getType()) {
                                    case 1:
                                    case 2:
                                        HomeDialog.getConstitutionDialog(HomeActivity.this, bean.getDescription()).show();
                                        break;
                                    case 3:
                                        HomeDialog.getBasicInfoDialog(HomeActivity.this, bean.getDescription()).show();
                                        break;
                                    case 4:
                                        HomeDialog.getBloodSugarDialog(HomeActivity.this, bean.getDescription()).show();
                                        break;
                                }
                            }
                        }
                    }
                });
    }


    /**
     * 绑定个人健康信息
     */
    private void subscribe() {

        rxManager.onRxEvent(RxEvent.JPUSH_RETRY_CONNECT)
                .subscribe(o -> {
                    boolean isConnect = (Boolean) o;
                    if (!isConnect) {
                        JPushInterface.stopPush(this);
                        //监听到JPush断开后重置 先停止 1秒后 重新连接
                        new Handler().postDelayed(() -> {
                            JPushInterface.resumePush(this);
                        }, 1000);
                    }

                });

        rxManager.onRxEvent(RxEvent.JPUSH_RETRY_REGISTER)
                .subscribe(o -> {
                    JPushInterface.init(this);
                    JPushInterface.setThirdPushEnable(this, true);
                });

        rxManager.onRxEvent(RxEvent.JPUSH_REGID_ADD)
                .subscribe(o -> {
                    addDevToken();
                });

        rxManager.onRxEvent(RxEvent.JUMP_ACTIVITY_SOS_MESSAGE)
                .subscribe(o -> {
                    SosMessageActivity.instance(this);
                });

        rxManager.onRxEvent(RxEvent.JUMP_ACTIVITY_ECG)
                .subscribe(o -> {
                    HealthDataActivity.instance(this, 3, false);
                });
        rxManager.onRxEvent(RxEvent.ON_APP_EXIT)
                .subscribe(o -> {
                    clientHandler.sendEmptyMessage(3);
                });
    }


    private void addDevToken() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString("token", ""));
        param.put("deviceToken", JPushInterface.getRegistrationID(this));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().addDeviceToken(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        Log.e(TAG, "添加regid " + data.msg);
                    }
                });
    }

    /**
     * 设置JPush别名
     * sequence
     * 用户自定义的操作序列号，同操作结果一起返回，用来标识一次操作的唯一性，推荐每次都用不同的数字序号。
     * alias
     * 每次调用设置有效的别名，覆盖之前的设置。
     * 有效的别名组成：字母（区分大小写）、数字、下划线、汉字、特殊字符@!#$&*+=.|。
     * 限制：alias 命名长度限制为 40 字节。（判断长度需采用 UTF-8 编码）
     */
    private void setJPushAlias() {
        UserInfo.DataBean bean = DataSupport.findFirst(UserInfo.DataBean.class);
        if (bean != null) {
            String chart = "T";
            String uid = bean.getLoginName();
            String underline = "_";
            String chinese = "推";
            String specialChart = "$";
            StringBuilder builder = new StringBuilder();
            builder.append(chart)
                    .append(uid)
                    .append(underline)
                    .append(chinese)
                    .append(specialChart);

            JPushInterface.setAlias(this, Integer.valueOf(uid), builder.toString());
        }

        if (JPushInterface.isPushStopped(this)) {
            JPushInterface.resumePush(this);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }


//    @RequiresApi(api = Build.VERSION_CODES.P)
//    private void endcall() {
//
//        TelecomManager manager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
//        PermissionUtil.getInstance().request(new String[]{Manifest.permission.ANSWER_PHONE_CALLS},
//                new PermissionResultCallBack() {
//
//                    @Override
//                    public void onPermissionGranted() {
//                        if (ActivityCompat.checkSelfPermission(
//                                HomeActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) !=
//                                PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        manager.endCall();
//                    }
//
//                    @Override
//                    public void onPermissionGranted(String... strings) {
//
//                    }
//
//                    @Override
//                    public void onPermissionDenied(String... strings) {
//
//                    }
//
//                    @Override
//                    public void onRationalShow(String... strings) {
//
//                    }
//                });
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.P)
//    @Override
//    public void onEndCall() {
//        endcall();
//    }
}