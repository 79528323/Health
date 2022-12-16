package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/6/16
 */
public class HealthyStatistics implements Serializable {

    /**
     * msg : 健康数据统计成功
     * code : 1
     * data : {"amount":183,"avgHigh":0,"avgLow":0,"avgRate":91,"highestHigh":0,"highestLow":0,"highestRate":115,"lowestHigh":0,"lowestLow":0,"lowestRate":78,"serviceDays":12,"wearDays":0}
     */

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {
        /**
         * amount : 183
         * avgHigh : 0
         * avgLow : 0
         * avgRate : 91
         * highestHigh : 0
         * highestLow : 0
         * highestRate : 115
         * lowestHigh : 0
         * lowestLow : 0
         * lowestRate : 78
         * serviceDays : 12
         * wearDays : 0
         */

        public int amount;
        public int avgHigh;
        public int avgLow;
        public int avgRate;
        public int highestHigh;
        public int highestLow;
        public int highestRate;
        public int lowestHigh;
        public int lowestLow;
        public int lowestRate;
        public int serviceDays;
        public int wearDays;
        public int ecgAbnormalNum;
        public int ecgNormalNum;
        public int ecgTimes;
        public String statisticUrl;
    }
}
