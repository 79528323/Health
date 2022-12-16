package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/11/9
 */
public class FenceSetting {

    /**
     * code : 1
     * data : {"id":1,"imei":"12123","status":0,"fenceEffectTimeTypeStr":"白天","startTime":"00:00","endTime":"00:00","fenceTypeStr":"当前定位500m半径","remindPeople":[{"id":1,"uid":"1000232","nickName":"昵称","memberAvatar":"","ifRemind":0},{"id":1,"uid":"1000232","nickName":"昵称","memberAvatar":"","ifRemind":0}]}
     * msg : 获取成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        /**
         * id : 1
         * imei : 12123
         * status : 0
         * fenceEffectTimeTypeStr : 白天
         * startTime : 00:00
         * endTime : 00:00
         * fenceTypeStr : 当前定位500m半径
         * remindPeople : [{"id":1,"uid":"1000232","nickName":"昵称","memberAvatar":"","ifRemind":0},{"id":1,"uid":"1000232","nickName":"昵称","memberAvatar":"","ifRemind":0}]
         */

        public int id;
        public String imei;
        public int status;
        public String fenceEffectTimeTypeStr;
        public String startTime;
        public String endTime;
        public String fenceTypeStr;
        public List<RemindPeopleBean> remindPeople;

        public static class RemindPeopleBean implements Serializable {
            /**
             * id : 1
             * uid : 1000232
             * nickName : 昵称
             * memberAvatar :
             * ifRemind : 0
             */

            public int id;
            public String uid;
            public String nickName;
            public String memberAvatar;
            public int ifRemind;

        }
    }
}
