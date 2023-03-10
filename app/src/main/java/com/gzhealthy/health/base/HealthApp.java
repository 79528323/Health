package com.gzhealthy.health.base;

import android.app.Service;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.StrictMode;
import android.os.Vibrator;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.facebook.stetho.Stetho;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.ApiService;
import com.gzhealthy.health.db.RealmConstant;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.DynamicTimeFormat;
import com.gzhealthy.health.utils.ShareUtils;
import com.gzhealthy.health.widget.header.MySmartRefreshHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import org.litepal.LitePal;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class HealthApp extends MultiDexApplication {

    private final String TAG = HealthApp.class.getSimpleName();
    public static Context context;
    //    public LocationService locationService;
    private Vibrator mVibrator;

    private static HealthApp healthApp;

    public static HealthApp getInstance1() {
        return healthApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        healthApp = this;
        initTextSize();
        if (context == null)
            context = this;
        MultiDex.install(this);
        initLog();
        initSharePreference();
        initStetho();
        ToastUtils.register(this);
        ApiService.init();
        LitePal.initialize(this);
//        initLocation();
        initRefreshHeaderFooter();
//        initUMPUSH();//???????????????
//        initUmengShared();//?????????????????????
//        initUmauthorization();//????????????????????????
//        initBaiduMap(); // ?????????????????????SDK
//        initLocation();
//        initRefreshHeaderFooter();
//        initUMPUSH();//???????????????
//        initUmengShared();//?????????????????????
//        initUmauthorization();//????????????????????????
//        initBaiduMap(); // ?????????????????????SDK
        //??????7.0??????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
//        CrashHandler.getInstance().init(this);
        // ?????????Realm?????????
        initRealm(this);
        initJPush();
        initBugly();
        initUmengAsms();
    }

    /**
     * ????????????????????????????????????
     */
    private void initTextSize() {
        Resources res = getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Override
    public Resources getResources() {//??????????????????
        Resources res = super.getResources();
        Configuration configuration = res.getConfiguration();
        if (configuration.fontScale != 1.0f) {
            configuration.fontScale = 1.0f;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
        }
        return res;
    }

    private static void initRefreshHeaderFooter() {
        //static ?????????????????????????????????
        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//????????????????????????
//            return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("????????? %s"));//???????????????Header???????????? ???????????????Header
            return new MySmartRefreshHeader(context);
        });
        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //???????????????Footer???????????? BallPulseFooter
            return new ClassicsFooter(context).setDrawableSize(20);
        });
    }

    /**
     * ????????? logger
     */
    private void initLog() {
        Logger.init(true);
    }

    /**
     * ????????? SharePreference
     */
    private void initSharePreference() {
        SPCache.init(this);
    }

    /**
     * ????????? stetho
     */
    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }


    public static Context getInstance() {
        return context;
    }

    private void initLocation() {
        /***
         * ???????????????sdk????????????Application?????????
         */
//        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
//        SDKInitializer.initialize(getApplicationContext());
    }


//    private void initUMPUSH() {
//        PushAgent mPushAgent = PushAgent.getInstance(getInstance());
//        UMConfigure.init(this, Constants.UMeng.UMENG_APP_KEY, "default_channel",
//                UMConfigure.DEVICE_TYPE_PHONE, Constants.UMeng.UMENG_SECRET);
//        mPushAgent.register(new IUmengRegisterCallback() {
//            @Override
//            public void onSuccess(String deviceToken) {
////                L.i("??????????????????deviceToken?????????" + deviceToken);
//                SPCache.putString("deviceToken", deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
////                L.i("??????????????????deviceToken?????????" + s + "---------" + s1);
//            }
//        });
//
//        mPushAgent.setPushIntentServiceClass(UmengPushIntentService.class);
//        mPushAgent.setDisplayNotificationNumber(10);
//
//        UmengNotificationClickHandler umengMessageHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage uMessage) {
//            }
//        };
//        mPushAgent.setNotificationClickHandler(umengMessageHandler);
//
//
//        // ??????????????????????????????????????????
//        UMConfigure.setProcessEvent(true);
//
//    }

//    private void initUmengShared() {
//        PlatformConfig.setWeixin(Constants.UMeng.WECHAT_APP_ID, Constants.UMeng.WECHAT_APP_SECRET);
//        PlatformConfig.setSinaWeibo("756264773", "f1188bb64c9f9d8edbb831b1d40f11e7", "http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("101504720", "7835964c5201c9a865e2dfdef204132a");
//    }

//    public void initUmauthorization() {
//        UMShareConfig config = new UMShareConfig();
//        config.isNeedAuthOnGetUserInfo(true);
//        UMShareAPI.get(this).setShareConfig(config);
//    }

    /**
     * ?????????Realm?????????
     *
     * @param context
     */
    private void initRealm(Context context) {
        try {
            Realm.init(context);
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .name(RealmConstant.REALM_DB_NAME)
                    .schemaVersion(RealmConstant.REALM_DB_VERSION)
                    .build();
            Realm.setDefaultConfiguration(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initBaiduMap() {
        //?????????????????????SDK
//        SDKInitializer.initialize(this);
//        // ???4.3.0??????????????????SDK????????????????????????????????????????????????????????????????????????????????????????????????.
//        // ??????BD09LL???GCJ02????????????????????????BD09LL?????????
//        SDKInitializer.setCoordType(CoordType.BD09LL);
//        // ????????????????????????????????????
//        MyBDLocation myBDLocation = MyBDLocation.getInstance();
//        myBDLocation.init(getApplicationContext());
    }

    /**
     * ????????????
     */
    private void initJPush() {
        boolean isDebug = BuildConfig.DEBUG ? true : false;
        JPushInterface.setDebugMode(isDebug);
        JPushInterface.init(context);
        JPushInterface.setThirdPushEnable(context, true);

        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(context
                , R.layout.layout_customer_notitfication,R.id.iv_icon,R.id.tv_title,R.id.tv_content,R.id.tv_time);
        builder.statusBarDrawable = R.mipmap.app_logo;
        builder.layoutIconDrawable = R.mipmap.app_logo;
        JPushInterface.setPushNotificationBuilder(1,builder);
    }


    /**
     * Bugly SDK?????????
     */
    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), Constants.BUGLY_SDK_APP_ID, BuildConfig.DEBUG ? true : false);
    }


    private void initUmengAsms(){
        UMConfigure.preInit(this,Constants.UmengValue.UMENG_SDK_APPKEY
                ,Constants.UmengValue.CHANNEL_ANDROID);
        UMConfigure.setLogEnabled(BuildConfig.DEBUG?true:false);
        if (ShareUtils.getBoolean(this,"isAcceptPrivacy",false)){
            UMConfigure.init(HealthApp.context
                    ,Constants.UmengValue.UMENG_SDK_APPKEY
                    ,Constants.UmengValue.CHANNEL_ANDROID
                    ,UMConfigure.DEVICE_TYPE_PHONE
                    ,null);
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.MANUAL);
        }
    }
}
