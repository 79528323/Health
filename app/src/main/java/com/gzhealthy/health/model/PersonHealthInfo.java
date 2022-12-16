package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/5/24
 */
public class PersonHealthInfo implements Serializable {

    /**
     * msg : 健康档案获取成功
     * code : 1
     * data : {"birthday":"1998-05-24","chronicDiseaseHis":"无病史,","height":"178","high":90,"id":9,"low":60,"name":"刘海","sex":1,"waist":"110","weight":"86"}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * birthday : 1998-05-24
         * chronicDiseaseHis : 无病史,
         * height : 178
         * high : 90
         * id : 9
         * low : 60
         * name : 刘海
         * sex : 1
         * waist : 110
         * weight : 86
         */

        public String birthday;
        public String chronicDiseaseHis;
        public String height;
        public int high;
        public int id;
        public int low;
        public String name;
        public int sex;
        public String waist;
        public String weight;
        public String glu;
        public String mealGlu;
        public String nickName;
        public String headPic;
        public String marriage;
        public String province;
        public String city;
        public String county;
        public String age;
    }
}
