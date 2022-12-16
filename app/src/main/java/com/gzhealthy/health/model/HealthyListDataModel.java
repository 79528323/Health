package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.BaseModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HealthyListDataModel extends BaseModel<HealthyListDataModel.DataBeanX> {
    /**
     * msg : 获取血压数据成功
     * code : 1
     * data : {"avgHigh":108,"avgLow":75,"data":[{"af":0,"checkTime":1620408092000,"createTime":1620408091000,"high":122,"id":6230,"imei":"890120210423348","low":78,"modifyTime":1620408091000,"motion":0,"rate":126,"spo2":98,"walk":0},{"af":0,"checkTime":1620473894000,"createTime":1620473894000,"high":101,"id":6609,"imei":"890120210423348","low":78,"modifyTime":1620473894000,"motion":0,"rate":80,"spo2":98,"walk":106}],"num":39,"time":18.28}
     */

//    public String msg;
//    public int code;
//    public DataBeanX data;

    public static class DataBeanX implements Serializable {
        /**
         * avgHigh : 108
         * avgLow : 75
         * data : [{"af":0,"checkTime":1620408092000,"createTime":1620408091000,"high":122,"id":6230,"imei":"890120210423348","low":78,"modifyTime":1620408091000,"motion":0,"rate":126,"spo2":98,"walk":0},{"af":0,"checkTime":1620473894000,"createTime":1620473894000,"high":101,"id":6609,"imei":"890120210423348","low":78,"modifyTime":1620473894000,"motion":0,"rate":80,"spo2":98,"walk":106}]
         * num : 39
         * time : 18.28
         */
        public int avgRate;
        public String endTime;
        public String startTime;

        public int num;
        public float time;

        public int avgLow;
        public int avgHigh;

        public int highestSpo2;
        public int lowestSpo2;
        public int avgSpo2;

        public int highestHigh;
        public int highestLow;
        public int lowestHigh;
        public int lowestLow;

        public double avgGlu;
        public double highestGlu;
        public double lowestGlu;
        public String description;

        public List<DataBean> data;
        public List<ReferenceBean> reference;

        public List<HungerDataBean> hungerData;
        public List<MealDataBean> mealData;
        public List<SugarDataBean> sugarData;

        public double gluDif;

        public int gluFlag;//是否有糖尿病 0-没有  1-有

        public static class ReferenceBean implements Serializable {
            public String condition;
            public double max;
            public double min;
        }

        public static class HungerDataBean implements Serializable{
            public double glu;
            public long createTime;
        }

        public static class MealDataBean implements Serializable{
            public double glu;
            public long createTime;
        }

        public static class SugarDataBean implements Serializable{
            public double glu;
            public long createTime;
        }

        public static class DataBean implements Serializable {
            /**
             * af : 0
             * checkTime : 1620408092000
             * createTime : 1620408091000
             * high : 122
             * id : 6230
             * imei : 890120210423348
             * low : 78
             * modifyTime : 1620408091000
             * motion : 0
             * rate : 126
             * spo2 : 98
             * walk : 0
             */

            public int af;
            public long checkTime;
            public long createTime;
            public int high;
            public int id;
            public String imei;
            public int low;
            public long modifyTime;
            public int motion;
            public int rate;
            public int spo2;
            public int walk;


            //血糖
            public String bloodSugarUrl;
            public double glu;
            public int type;
        }
    }
//
//    public int code;
//    public String msg;
//    public DataBeanX data;
//
//    public static class DataBeanX implements Serializable {
//        /**
//         * avgRate : 80
//         * data : [{"af":0,"checkTime":1618209918000,"createTime":1618209078000,"high":115,"id":16,"imei":"891818020411721","low":98,"modifyTime":1618209078000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209098000,"createTime":1618209098000,"high":115,"id":18,"imei":"891818020411721","low":98,"modifyTime":1618209098000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209078000,"createTime":1618209118000,"high":115,"id":21,"imei":"891818020411721","low":98,"modifyTime":1618209118000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209078000,"createTime":1618209150000,"high":115,"id":22,"imei":"891818020411721","low":98,"modifyTime":1618209150000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209078000,"createTime":1618209150000,"high":115,"id":23,"imei":"891818020411721","low":98,"modifyTime":1618209150000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209078000,"createTime":1618209170000,"high":115,"id":24,"imei":"891818020411721","low":98,"modifyTime":1618209170000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209078000,"createTime":1618209170000,"high":115,"id":25,"imei":"891818020411721","low":98,"modifyTime":1618209170000,"motion":0,"rate":80,"spo2":97,"walk":1540},{"af":0,"checkTime":1618209018000,"createTime":1618209078000,"high":115,"id":17,"imei":"891818020411721","low":98,"modifyTime":1618209078000,"motion":0,"rate":80,"spo2":97,"walk":1540}]
//         * endTime : 2021-04-12 14:45:18
//         * startTime : 2021-04-12 14:30:18
//         */
//
//        public int avgRate;
//        public String endTime;
//        public String startTime;
//
//        public int num;
//        public float time;
//        public int avgLow;
//        public int avgHigh;
//
//        public int highestSpo2;
//        public int lowestSpo2;
//        public int avgSpo2;
//
//        public List<DataBean> data;
//
//        public static class DataBean implements Serializable {
//            /**
//             * af : 0
//             * checkTime : 1618209918000
//             * createTime : 1618209078000
//             * high : 115
//             * id : 16
//             * imei : 891818020411721
//             * low : 98
//             * modifyTime : 1618209078000
//             * motion : 0
//             * rate : 80
//             * spo2 : 97
//             * walk : 1540
//             */
//
//            public int af;
//            public long checkTime;
//            public long createTime;
//            public int high;
//            public int id;
//            public String imei;
//            public int low;
//            public long modifyTime;
//            public int motion;
//            public int rate;
//            public int spo2;
//            public int walk;
//        }
//    }




}
