package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/5/7
 */
public class WatchDeviceModel implements Serializable {


    /**
     * msg : 获取绑定信息成功
     * code : 1
     * data : [{"imei":"890120210423348","photoUrl":"http://gzhealthy.oss-cn-guangzhou.aliyuncs.com/149303398456126.png","uid":"60006006"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * imei : 890120210423348
         * photoUrl : http://gzhealthy.oss-cn-guangzhou.aliyuncs.com/149303398456126.png
         * uid : 60006006
         */

        public String imei;
        public String photoUrl;
        public String uid;
        public String electricity;
    }
}
