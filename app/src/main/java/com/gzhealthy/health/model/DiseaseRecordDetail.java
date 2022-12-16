package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

import kotlin.collections.ArrayDeque;

/**
 * Created by Justin_Liu
 * on 2021/11/10
 */
public class DiseaseRecordDetail implements Serializable {

    /**
     * code : 1
     * data : {"id":121,"type":"门诊","seeDate":"2021-08-09","diagnosis":"xx","department":"xx","hospital":"xx","description":"xxx","imgUrl":["http://xxxx/3706013930797290.jpeg,http://xxxx/3706013930797290.jpeg"]}
     * msg : 获取成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        /**
         * id : 121
         * type : 门诊
         * seeDate : 2021-08-09
         * diagnosis : xx
         * department : xx
         * hospital : xx
         * description : xxx
         * imgUrl : ["http://xxxx/3706013930797290.jpeg,http://xxxx/3706013930797290.jpeg"]
         */

        public int id;
        public String type;
        public String seeDate;
        public String diagnosis;
        public String department;
        public String hospital;
        public String description;
        public List<String> imgUrl = new ArrayDeque<>();

    }
}
