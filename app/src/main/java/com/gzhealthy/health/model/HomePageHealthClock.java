package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/7/23
 */
public class HomePageHealthClock implements Serializable {

    /**
     * code : 1
     * data : {"meridian":"肾经","regimen":"此时可做游泳，太极等有利于肾的运动，晚饭清淡为主哦！","type":10}
     * msg : 获取首页健康时钟成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Serializable {
        /**
         * meridian : 肾经
         * regimen : 此时可做游泳，太极等有利于肾的运动，晚饭清淡为主哦！
         * type : 10
         */

        public String meridian;
        public String regimen;
        public int type;
    }
}
