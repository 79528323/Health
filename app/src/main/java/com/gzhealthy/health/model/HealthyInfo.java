package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.BaseModel;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HealthyInfo implements Serializable {

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * bloodPressStatus : 1
         * bmi : 24.2
         * bmiType : fit
         * distance : 0
         * ecgResult :
         * ecgStatus :
         * high : 110
         * kcal : 0
         * low : 90
         * rate : 90
         * rateStatus : 1
         * spo2 : 97
         * spo2Status : 1
         * temperature : 0
         * temperatureStatus :
         * walk : 0
         */

        public String bloodPressStatus;
        public String bloodPressStatusStr;
        public String bmi;
        public String bmiType;
        public float distance;
        public String ecgResult;
        public String ecgStatus;
        public int high;
        public float kcal;
        public int low;
        public int rate;
        public String rateStatus;
        public int spo2;
        public String spo2Status;
        public String temperature;
        public String temperatureStatus;
        public int walk;
        public String totalDeepDuration;
        public String totalLightDuration;

        public int type;
        public String meridian;
        public String regimen;

        public int card_type;

        public String glu;//血糖
        public String gluStatusStr;
    }

//    public static class DataBean implements Serializable {
//        /**
//         * msg : 获取最新一条健康记录成功了
//         * code : 1
//         * data : {"bloodPressStatus":"1","bmi":"24.2","bmiType":"fit","distance":0,"ecgResult":"","ecgStatus":"","high":110,"kcal":0,"low":90,"rate":90,"rateStatus":"1","spo2":97,"spo2Status":"1","temperature":"0","temperatureStatus":"","walk":0}
//         */
//
//
////        /**
////         * af : 0
////         * createTime : 1618209078000
////         * high : 115
////         * imei : 891818020411721
////         * kcal : 0
////         * low : 98
////         * motion : 0
////         * rate : 80
////         * spo2 : 97
////         * walk : 1540
////         */
////
////
//////        public int af;
//////        public long createTime;
//////        public int high;
//////        public String imei;
//////        public float kcal;
//////        public int low;
//////        public int motion;
//////        public int rate;
//////        public int spo2;
//////        public int walk;
//
//
//
//    }
}
