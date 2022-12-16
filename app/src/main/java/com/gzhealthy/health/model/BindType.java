package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/7/26
 */
public class BindType implements Serializable {

    /**
     * code : 1
     * data : {"content_type":"apply","imei":"890120210423207"}
     * msg : 获取手表状态成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        /**
         * content_type : apply
         * imei : 890120210423207
         */

        public String content_type;
        public String imei;
    }
}
