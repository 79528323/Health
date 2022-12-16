package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/5/8
 */
public class SosModel implements Serializable {

    /**
     * msg : 消息列表获取成功
     * code : 1
     * data : [{"content":"你的imei为890120210423231的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 17:44:46"},{"content":"你的imei为890120210423231的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 17:37:06"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 17:07:25"},{"content":"你的imei为890120210423231的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 16:48:18"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 16:40:24"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 16:28:55"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 16:28:25"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 16:14:26"},{"content":"你的imei为890120210423207的手表发出的sos求助已确认,客服已处理完成！","pushTime":"2021-05-07 15:51:46"},{"content":"你的imei为890120210423207的手表发出sos求助,sos求助已到达，客服在处理！","pushTime":"2021-05-07 15:50:45"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * content : 你的imei为890120210423231的手表发出sos求助,sos求助已到达，客服在处理！
         * pushTime : 2021-05-07 17:44:46
         */

        public String content;
        public String pushTime;
    }
}
