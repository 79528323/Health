package com.gzhealthy.health.model.banner;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/7/2
 */
public class DeviceDetail implements Serializable {


    /**
     * msg : 获取手表设备详情成功
     * code : 1
     * data : {"createTime":1618995382000,"id":67,"imei":"867400020316612","model":"Tian watch","modifyTime":1624355558000,"screenStatus":0,"sedentaryStatus":0,"state":5,"ver":"ZW37_TA01_202106191958"}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * createTime : 1618995382000
         * id : 67
         * imei : 867400020316612
         * model : Tian watch
         * modifyTime : 1624355558000
         * screenStatus : 0
         * sedentaryStatus : 0
         * state : 5
         * ver : ZW37_TA01_202106191958
         */

        public long createTime;
        public int id;
        public String imei;
        public String model;
        public long modifyTime;
        public int screenStatus;
        public int sedentaryStatus;
        public int rateWarnStatus;
        public int state;
        public String ver;
        public String sleepEndTime;
        public String sleepStartTime;

        public int sportStatus;
        public String sportRemindTime;
        public int breakfastStatus;
        public String breakfastRemindTime;
        public int sleepStatus;
        public String sleepRemindTime;
        public String photoUrl;
        public int safetyFenceStatus;
    }
}
