package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/4/22
 */
public class ContractModel implements Serializable {
    /**
     * msg : 查询紧急联系人成功
     * code : 1
     * data : [{"createTime":1619087306000,"emerContactName":"小王","emerContactPhone":"13609875431","id":3,"modifyTime":1619087306000,"uid":"60006007"},{"createTime":1619090387000,"emerContactName":"小黄","emerContactPhone":"13522221234","id":4,"modifyTime":1619090387000,"uid":"60006007"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public static class DataBean implements Serializable {
        /**
         * createTime : 1619087306000
         * emerContactName : 小王
         * emerContactPhone : 13609875431
         * id : 3
         * modifyTime : 1619087306000
         * uid : 60006007
         */

        public long createTime;
        public String emerContactName;
        public String emerContactPhone;
        public int id;
        public long modifyTime;
        public String uid;
    }


//    @SerializedName("data")
//    public List<DataBean> dataX;
//
//    public static class DataBean implements Serializable {
//        /**
//         * createTime : 1619087306000
//         * emerContactName : 小王
//         * emerContactPhone : 13609875431
//         * id : 3
//         * modifyTime : 1619087306000
//         * uid : 60006007
//         */
//
//        public long createTime;
//        public String emerContactName;
//        public String emerContactPhone;
//        public int id;
//        public long modifyTime;
//        public String uid;
//    }
}
