package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/7/5
 */
public class PushMessage implements Serializable {

    /**
     * msg : 消息列表获取成功
     * code : 1
     * data : [{"content":"推送测试","editName":"admin","id":235,"ifRead":1,"pushTime":"2021-05-26 19:05:00","pushType":3,"title":"测试"},{"content":"测试消息推送","editName":"admin","id":234,"ifRead":1,"pushTime":"2021-05-26 18:45:00","pushType":1,"title":"测试"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * content : 推送测试
         * editName : admin
         * id : 235
         * ifRead : 1
         * pushTime : 2021-05-26 19:05:00
         * pushType : 3
         * title : 测试
         */

        public String content;
        public String editName;
        public int id;
        public int ifRead;
        public String pushTime;
        public int pushType;
        public String title;
        public String extraContent;
    }
}
