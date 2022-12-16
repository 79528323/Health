package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/7/5
 */
public class WechatApplet implements Serializable {

    /**
     * code : 1
     * data : {"appId":"12345","appletId":"gh_8807315c264a","invitationCode":"66666","uid":"60006040"}
     * msg : 成功了
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        /**
         * appId : 12345
         * appletId : gh_8807315c264a
         * invitationCode : 66666
         * uid : 60006040
         */

        public String appId;
        public String appletId;
        public String invitationCode;
        public String uid;
    }
}
