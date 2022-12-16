package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/6/18
 */
public class HealthyReport implements Serializable {
    /**
     * code : 1
     * data : {"body":{"bmi":{"maxScore":"24.9","miniScore":"18.5","score":"2.42","type":"thin"},"waist":{"overSize":"90","sex":"1","size":"80","type":"fit"}},"createTime":1624241958076,"ehr":{"bloodPress":{"avgHigh":"104","avgLow":"73","highList":[{"date":"2021-06-11","high":104}],"lowList":[{"date":"2021-06-11","low":74}],"maxHigh":"140","maxLow":"90","minHigh":"90","minLow":"60","status":"1"},"ecg":{"data":[],"status":"1","times":"0"},"rate":{"avgRate":"82","data":[{"date":"2021-06-11","rate":81}],"maxRate":"100","minRate":"50","status":"1"},"spo2":{"avgSpo2":"97","data":[{"date":"2021-06-11","spo2":97}],"minSpo2":"95","status":"1"}},"property":{"data":[{"convertScore":100,"propertyId":1,"propertyName":"平和质","result":[]},{"convertScore":100,"propertyId":2,"propertyName":"阳虚质","result":[]},{"convertScore":100,"propertyId":3,"propertyName":"阴虚质","result":[]},{"convertScore":100,"propertyId":4,"propertyName":"气虚质","result":[]},{"convertScore":100,"propertyId":5,"propertyName":"痰湿质","result":[]},{"convertScore":100,"propertyId":6,"propertyName":"湿热质","result":[]},{"convertScore":100,"propertyId":7,"propertyName":"血瘀质","result":[]},{"convertScore":100,"propertyId":8,"propertyName":"特禀质","result":[]},{"convertScore":100,"propertyId":9,"propertyName":"气郁质","result":[]}],"id":2,"name":"阳虚质","note":""},"score":50,"sport":{"kcal":343.3,"step":9232},"tips":{"food":"无","living":"无","sport":"无"},"type":"0"}
     * msg :
     */

    public int code;
    public DataBeanXXX data;
    public String msg;

    public static class DataBeanXXX implements Serializable {
        /**
         * body : {"bmi":{"maxScore":"24.9","miniScore":"18.5","score":"2.42","type":"thin"},"waist":{"overSize":"90","sex":"1","size":"80","type":"fit"}}
         * createTime : 1624241958076
         * ehr : {"bloodPress":{"avgHigh":"104","avgLow":"73","highList":[{"date":"2021-06-11","high":104}],"lowList":[{"date":"2021-06-11","low":74}],"maxHigh":"140","maxLow":"90","minHigh":"90","minLow":"60","status":"1"},"ecg":{"data":[],"status":"1","times":"0"},"rate":{"avgRate":"82","data":[{"date":"2021-06-11","rate":81}],"maxRate":"100","minRate":"50","status":"1"},"spo2":{"avgSpo2":"97","data":[{"date":"2021-06-11","spo2":97}],"minSpo2":"95","status":"1"}}
         * property : {"data":[{"convertScore":100,"propertyId":1,"propertyName":"平和质","result":[]},{"convertScore":100,"propertyId":2,"propertyName":"阳虚质","result":[]},{"convertScore":100,"propertyId":3,"propertyName":"阴虚质","result":[]},{"convertScore":100,"propertyId":4,"propertyName":"气虚质","result":[]},{"convertScore":100,"propertyId":5,"propertyName":"痰湿质","result":[]},{"convertScore":100,"propertyId":6,"propertyName":"湿热质","result":[]},{"convertScore":100,"propertyId":7,"propertyName":"血瘀质","result":[]},{"convertScore":100,"propertyId":8,"propertyName":"特禀质","result":[]},{"convertScore":100,"propertyId":9,"propertyName":"气郁质","result":[]}],"id":2,"name":"阳虚质","note":""}
         * score : 50
         * sport : {"kcal":343.3,"step":9232}
         * tips : {"food":"无","living":"无","sport":"无"}
         * type : 0
         */

        public BodyBean body;
        public long createTime;
        public EhrBean ehr;
        public PropertyBean property;
        public int score;
        public SportBean sport;
        public TipsBean tips;
        public String type;
        public HealthStatusBean healthStatus;

        public static class BodyBean implements Serializable {
            /**
             * bmi : {"maxScore":"24.9","miniScore":"18.5","score":"2.42","type":"thin"}
             * waist : {"overSize":"90","sex":"1","size":"80","type":"fit"}
             */

            public BmiBean bmi;
            public WaistBean waist;

            public static class BmiBean implements Serializable {
                /**
                 * maxScore : 24.9
                 * miniScore : 18.5
                 * score : 2.42
                 * type : thin
                 */

                public String maxScore;
                public String miniScore;
                public String score;
                public String type;
            }

            public static class WaistBean implements Serializable {
                /**
                 * overSize : 90
                 * sex : 1
                 * size : 80
                 * type : fit
                 */

                public String overSize;
                public String sex;
                public String size;
                public String type;
            }
        }

        public static class EhrBean implements Serializable {
            /**
             * bloodPress : {"avgHigh":"104","avgLow":"73","highList":[{"date":"2021-06-11","high":104}],"lowList":[{"date":"2021-06-11","low":74}],"maxHigh":"140","maxLow":"90","minHigh":"90","minLow":"60","status":"1"}
             * ecg : {"data":[],"status":"1","times":"0"}
             * rate : {"avgRate":"82","data":[{"date":"2021-06-11","rate":81}],"maxRate":"100","minRate":"50","status":"1"}
             * spo2 : {"avgSpo2":"97","data":[{"date":"2021-06-11","spo2":97}],"minSpo2":"95","status":"1"}
             */

            public BloodPressBean bloodPress;
            public EcgBean ecg;
            public RateBean rate;
            public Spo2Bean spo2;

            public static class BloodPressBean implements Serializable {
                /**
                 * avgHigh : 104
                 * avgLow : 73
                 * highList : [{"date":"2021-06-11","high":104}]
                 * lowList : [{"date":"2021-06-11","low":74}]
                 * maxHigh : 140
                 * maxLow : 90
                 * minHigh : 90
                 * minLow : 60
                 * status : 1
                 */

                public String avgHigh;
                public String avgLow;
                public String maxHigh;
                public String maxLow;
                public String minHigh;
                public String minLow;
                public String status;
                public List<HighListBean> highList;
                public List<LowListBean> lowList;
                public String[] description;

                public static class HighListBean implements Serializable {
                    /**
                     * date : 2021-06-11
                     * high : 104
                     */

                    public String date;
                    public int high;
                }

                public static class LowListBean implements Serializable {
                    /**
                     * date : 2021-06-11
                     * low : 74
                     */

                    public String date;
                    public int low;
                }
            }

            public static class EcgBean implements Serializable {
                /**
                 * data : []
                 * status : 1
                 * times : 0
                 */

                public String status;
                public String times;
                public List<String> data;
            }

            public static class RateBean implements Serializable {
                /**
                 * avgRate : 82
                 * data : [{"date":"2021-06-11","rate":81}]
                 * maxRate : 100
                 * minRate : 50
                 * status : 1
                 */

                public String avgRate;
                public String maxRate;
                public String minRate;
                public String status;
                public List<DataBean> data;
                public String[] description;

                public static class DataBean implements Serializable {
                    /**
                     * date : 2021-06-11
                     * rate : 81
                     */

                    public String date;
                    public int rate;
                }
            }

            public static class Spo2Bean implements Serializable {
                /**
                 * avgSpo2 : 97
                 * data : [{"date":"2021-06-11","spo2":97}]
                 * minSpo2 : 95
                 * status : 1
                 */

                public String avgSpo2;
                public String minSpo2;
                public String status;
                public List<DataBeanX> data;
                public String[] description;

                public static class DataBeanX implements Serializable {
                    /**
                     * date : 2021-06-11
                     * spo2 : 97
                     */

                    public String date;
                    public int spo2;
                }
            }
        }

        public static class PropertyBean implements Serializable {
            /**
             * data : [{"convertScore":100,"propertyId":1,"propertyName":"平和质","result":[]},{"convertScore":100,"propertyId":2,"propertyName":"阳虚质","result":[]},{"convertScore":100,"propertyId":3,"propertyName":"阴虚质","result":[]},{"convertScore":100,"propertyId":4,"propertyName":"气虚质","result":[]},{"convertScore":100,"propertyId":5,"propertyName":"痰湿质","result":[]},{"convertScore":100,"propertyId":6,"propertyName":"湿热质","result":[]},{"convertScore":100,"propertyId":7,"propertyName":"血瘀质","result":[]},{"convertScore":100,"propertyId":8,"propertyName":"特禀质","result":[]},{"convertScore":100,"propertyId":9,"propertyName":"气郁质","result":[]}]
             * id : 2
             * name : 阳虚质
             * note :
             */

            public int id;
            public String name;
            public String note;
            public List<DataBeanXX> data;

            public static class DataBeanXX implements Serializable {
                /**
                 * convertScore : 100
                 * propertyId : 1
                 * propertyName : 平和质
                 * result : []
                 */

                public float convertScore;
                public int propertyId;
                public String propertyName;
                public List<?> result;
            }
        }

        public static class SportBean implements Serializable {
            /**
             * kcal : 343.3
             * step : 9232
             */

            public double kcal;
            public int step;
            public List<WalkInfoBean> walkInfoList;

            public static class WalkInfoBean implements Serializable {
                /**
                 * kcal : 343.3
                 * step : 9232
                 */

                public String date;
                public int step;
            }
        }

        public static class TipsBean implements Serializable {
            /**
             * food : 无
             * living : 无
             * sport : 无
             */

            public String food;
            public String living;
            public String sport;
        }

        public static class HealthStatusBean implements Serializable{
            public String[] description;
            public int riskCount;
            public int status;
        }
    }



}
