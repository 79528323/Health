package com.gzhealthy.health.utils;

/**
 * @function: 所有请求相关地址
 */
public class HttpConstants {

    public static final String ROOT_URL = "http://39.104.73.97:8081/";

    public static final String ROOT_URL_1 = "http://app.tofans.com/";

    /**
     * 滚动播报消息URL
     */
    public static final String ROLL_NEWS_URL = ROOT_URL + "api/index/rollNews";

    /**
     * 首页菜单URL
     */
    public static final String MENU_URL = ROOT_URL + "api/index/menu";

    /**
     * 钱包模块首页URL
     */
    public static final String GET_WALLET_INFO_URL = ROOT_URL + "api/wallet/getWalletInfo";

    /**
     * 金豆兑换飞豆URL
     */
    public static final String JIN_CHANGE_FEI_URL = ROOT_URL + "api/wallet/jinChangeFei";

    /**
     * 金豆兑换飞豆记录URL
     */
    public static final String JIN_CHANGE_FEI_RECORD_URL = ROOT_URL + "api/wallet/jinChangeFeiRecord";

    /**
     * app更新URL
     */
    public static final String GET_APP_VERSION_URL = ROOT_URL + "api/appInfo/getAppVersion";

}


