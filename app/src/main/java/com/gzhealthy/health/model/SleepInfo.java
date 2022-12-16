package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/7/20
 */

public class SleepInfo implements Serializable {


    /**
     * code : 1
     * data : {"deepProportion":"0.24","endTime":"14:55","lightProportion":"99.76","paraphrase":"深睡眠是又称\u201c黄金睡眠期\u201d，一般占整个睡眠周期的20%-30%","sleepData":[{"duration":2852000,"endTime":"11:08","sleepStartTime":1626747654000,"startTime":"10:20","timeLength":"47分","type":"2"},{"duration":889000,"endTime":"11:23","sleepStartTime":1626750506000,"startTime":"11:08","timeLength":"14分","type":"1"},{"duration":2400000,"endTime":"12:03","sleepStartTime":1626751395000,"startTime":"11:23","timeLength":"40分","type":"2"},{"duration":3120000,"endTime":"12:55","sleepStartTime":1626753795000,"startTime":"12:03","timeLength":"52分","type":"1"},{"duration":7200000,"endTime":"14:55","sleepStartTime":1626756915000,"startTime":"12:55","timeLength":"2小时0分","type":"2"}],"sleepDuration":"4小时34分","sleepHabit":"null-null","startTime":"10:20","suggest":"中老年的深睡眠时间会变短，多运动会提高睡眠质量","totalDeepDuration":"1小时6分","totalLightDuration":"3小时27分"}
     * msg :
     */

    public int code;
    public DataBean data;
    public String msg;


    public static class DataBean implements Serializable {
        /**
         * deepProportion : 0.24
         * endTime : 14:55
         * lightProportion : 99.76
         * paraphrase : 深睡眠是又称“黄金睡眠期”，一般占整个睡眠周期的20%-30%
         * sleepData : [{"duration":2852000,"endTime":"11:08","sleepStartTime":1626747654000,"startTime":"10:20","timeLength":"47分","type":"2"},{"duration":889000,"endTime":"11:23","sleepStartTime":1626750506000,"startTime":"11:08","timeLength":"14分","type":"1"},{"duration":2400000,"endTime":"12:03","sleepStartTime":1626751395000,"startTime":"11:23","timeLength":"40分","type":"2"},{"duration":3120000,"endTime":"12:55","sleepStartTime":1626753795000,"startTime":"12:03","timeLength":"52分","type":"1"},{"duration":7200000,"endTime":"14:55","sleepStartTime":1626756915000,"startTime":"12:55","timeLength":"2小时0分","type":"2"}]
         * sleepDuration : 4小时34分
         * sleepHabit : null-null
         * startTime : 10:20
         * suggest : 中老年的深睡眠时间会变短，多运动会提高睡眠质量
         * totalDeepDuration : 1小时6分
         * totalLightDuration : 3小时27分
         */

        public String deepProportion;
        public String endTime;
        public String lightProportion;
        public List<String> paraphrase;
        public String sleepDuration;
        public String sleepHabit;
        public String startTime;
        public List<String> suggest;
        public String totalDeepDuration;
        public String totalLightDuration;
        public List<SleepDataBean> sleepData;
        public String imei;


        public static class SleepDataBean implements Serializable {
            /**
             * duration : 2852000
             * endTime : 11:08
             * sleepStartTime : 1626747654000
             * startTime : 10:20
             * timeLength : 47分
             * type : 2
             */

            public int duration;
            public String endTime;
            public long sleepStartTime;
            public String startTime;
            public String timeLength;
            public String type;
        }
    }
}
