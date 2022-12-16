package com.gzhealthy.health.base;

import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.manager.ActivityManager;

import static com.gzhealthy.health.base.Constants.Api.H5HOST;
import static com.gzhealthy.health.base.Constants.Api.H5PRE;

public class Constants {
    public static final String MY_TERMINAL = "2";//2为安卓机型
    /**
     * create by liubd
     * 2021-06-23 10: 39: 00
     * 内置版本 (已废除，直接使用gradle的versionCode )
     */
    public static final int UPDATENUM = 3001;//
    public static final String INTENT_ACTION_EXIT_APP = ActivityManager.packageName + ".intent.action.exitapp";
    public static final String FIRST_OPEN = "FIRST_OPEN_20201113";
    public static final String SHARE_TITLE = "体安";
    public static final String shareDes = "体安在手，健康永久";
    public static final String APP_CACAHE_DIRNAME = "/webcache";
    public static String SharePhone = "";//分享推荐背景图
    public static final String SHAREIMG = "https://gzhealthy.oss-cn-guangzhou.aliyuncs.com/logo.png";//分享图标
    public static final String recommentregist = "http://app.tofans.com/fdly/#/register?recommendId=%s";//推荐注册页
    public static final String registerProtocal = "http://app.tofans.com/fdly/#/GVRP";//注册协议
    public static final String privacyPolicy = H5PRE + "tian/#/policy";//隐私政策
    public static final String treaty = H5PRE + "tian/#/treaty";//用户协议
    public static final String aboutUs = H5PRE + "tian/#/introduce"; // 公司介绍
    public static final String businessLicence = H5PRE + "tian/#/businessLicence"; // 营业执照
    public static String askService = "https://im.7x24cc.com/phone_webChat.html?accountId=N000000016899&chatId=f761c3a3-93b6-48a0-84c5-51660280a6e4";//客服聊天
    public static final String productDetail = H5PRE + "tian/#/lineDetails?productCode=";//产品详情
    public static final String HEALTH_DATA = H5PRE + "tian/#/healthData"; // 健康数据
    public static final String NEWS_DETAIL = H5PRE + "tian/#/newsDetail?id="; // 新闻详情
    public static final String LINK_DISEASE_HISTORY = H5HOST + "/disease.html"; // 疾病史
    public static final String LINK_ALLERGY_HISTORY = H5HOST + "/allergy.html"; // 过敏史
    public static final String LINK_LIVING_HABIT = H5HOST + "/livingHabit.html"; // 生活习惯
    public static final String LINK_MEMBER_CENT= H5HOST + "/memberCent.html"; // 会员中心

    public static String ShoptionUrl = "";//商城地址
    public static String CruiseUrl = "http://app.cruise.cnsits.com/index/";//客服聊天地址
    public static String LIVE_PLACE = "";//直播大厅
    public static String FEIDOU_CURTURE = "";//文化
    public static String BUSSNESS_RECODE = "";//商机报备
    public static String TASK_CENTER = "";//任务中心

    public static String gonggaoUrl = "";//平台公告
    public static String CustomerServiceUrl = "";//分销商推荐图URL
    public static String NEWWEIPAYl = "http://app.tofans.com";

    public static String BUGLY_SDK_APP_ID = "8b0763beb0";

    public static class UmengValue{
        public static final String UMENG_SDK_APPKEY = "61454b7d2a91a03cef4c6b69";
        public static final String CHANNEL_ANDROID = "channel_android";
    }


    public static class ResponseStatus {
        public static final int STATE_UNKNOWN = 0;
        public static final int STATE_LOADING = 1;
        public static final int STATE_ERROR = 2;
        public static final int STATE_EMPTY = 3;
        public static final int STATE_SUCCESS = 4;
    }

    public static class Api {
        public static final String BASE_URL1;// = "http://8.129.160.51:8088";//测试环境地址
        //        public static final String BASE_URL1 = "http://watchapi.gzhealthy.com";//正式环境地址
        //        public static final String H5PRE = "http://gzhealthy.com/";//h5地址前缀
        public static final String H5HOST;//h5地址前缀,另外一个地址，跟H5PRE不一样的
        public static final String H5PRE = "http://test.gzhealthy.com/";//h5地址前缀(测试坏境)
        public static String ossPicPre = "http://gzhealthy.oss-cn-guangzhou.aliyuncs.com/"; //获取OSS上传图片

        static {
            if (BuildConfig.DEBUG) {
                H5HOST = "http://testh5.gzhealthy.com/testh5";
                BASE_URL1 = "http://8.129.160.51:8088";//测试环境地址
//                BASE_URL1 = "http://172.16.3.19:8080";//Jerry WU 本地地址
            } else {
                H5HOST = "http://h5.gzhealthy.com/h5";
                BASE_URL1 = "http://watchapi.gzhealthy.com";//正式环境地址
            }
        }
    }

    public static class HealthDataValue{
        public static final String HEALTH_CLOCK_URL = H5HOST + "/healthClock.html";
        public static final String HEALTH_QUETION_URL = H5HOST + "/consQues.html?token=";
    }

    public static class RequestValue {
        public final static int CANCEL_WAIT_DIALOG = 1001;
        public final static int USER_TOKEN_INVALID = 6263;
    }

    public static class ImageResource {
        public final static String OSSHEAD = "http://gzhealthy.oss-cn-guangzhou.aliyuncs.com/";//OSS前缀
    }

    public static class Images {
        public static final int IMAGE_REQUEST_CODE = 110;
        public static final int CAMERA_REQUEST_CODE = 111;
        public static final int CROP_REQUEST_CODE = 112;
    }

    public static class SharePreference {
        public static final String DEVICE_WIDTH = "device_width";
        public static final String DEVICE_HEIGHT = "device_height";
        public static final String ISOPENNEW = "isOpenNew";
        public static final String OLDMAALSHOP = "shoppingMallUrl";
        public static final String NEWSHOPURL = "newShopUrl";
    }

    public static class Receiver {
        public static final String INTENT_ACTION_EXIT_APP = "com.aiten.travel.exit_app";
        public static final String INTENT_ACTION_NET_ERROR = "com.aiten.travel.net_error";
        public static final String INTENT_ACTION_MONEY_VISIBILITY = "com.aiten.money_visibility";
        public static final String INTENT_ACTION_MIAN = "com.aiten.travel.main";
    }

    public static class BannerJumpType {
        /**
         * 无跳转
         */
        public static final String NO_JUMP = "1";
        /**
         * APP模块
         */
        public static final String APP = "2";
        /**
         * 内部链接
         */
        public static final String INNER = "3";
        /**
         * 外部链接
         */
        public static final String OUT = "4";
    }

    public static class UMeng {

        // 友盟 AppKey
        public static final String UMENG_APP_KEY = "5fdc1a9c345b8b53f5724f28";
        // 友盟 Secret
        public static final String UMENG_SECRET = "c072630e0f52f6b76e650f11f80d0f06";
        public static final String WECHAT_APP_ID = "wx85ba693b7491a203";
        public static final String WECHAT_APP_SECRET = "5b6ab432e0407d9caab625090aec351e";
        public static final String HOME_PAGE = "home_page"; // 首页Tab
    }


    public static class OAuth {
        public static final int TYPE_WECHAT = 1;
    }
}
