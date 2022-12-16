package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/6/18
 */
public class HealthyEcgReport implements Serializable {

    /**
     * code : 1
     * data : {"firstPage":true,"hasNextPage":false,"hasPreviousPage":false,"lastPage":true,"list":[{"content":"{\"type\":\"0\",\"score\":50.00,\"property\":{\"id\":2,\"name\":\"阳虚质\",\"note\":\"\",\"data\":[{\"propertyId\":1,\"propertyName\":\"平和质\",\"convertScore\":100.00},{\"propertyId\":2,\"propertyName\":\"阳虚质\",\"convertScore\":100.00},{\"propertyId\":3,\"propertyName\":\"阴虚质\",\"convertScore\":100.00},{\"propertyId\":4,\"propertyName\":\"气虚质\",\"convertScore\":100.00},{\"propertyId\":5,\"propertyName\":\"痰湿质\",\"convertScore\":100.00},{\"propertyId\":6,\"propertyName\":\"湿热质\",\"convertScore\":100.00},{\"propertyId\":7,\"propertyName\":\"血瘀质\",\"convertScore\":100.00},{\"propertyId\":8,\"propertyName\":\"特禀质\",\"convertScore\":100.00},{\"propertyId\":9,\"propertyName\":\"气郁质\",\"convertScore\":100.00}]},\"body\":{\"waist\":{\"size\":\"80\",\"type\":\"fit\",\"sex\":\"1\",\"overSize\":\"90\"},\"bmi\":{\"type\":\"thin\",\"score\":\"2.42\",\"miniScore\":\"18.5\",\"maxScore\":\"24.9\"}},\"sport\":{\"step\":9232,\"kcal\":343.3},\"tips\":{\"food\":\"无\",\"living\":\"无\",\"sport\":\"无\"},\"ehr\":{\"bloodPress\":{\"avgHigh\":\"104\",\"avgLow\":\"73\",\"status\":\"1\",\"highData\":{\"2021-06-11\":104.6547619047619,\"2021-06-12\":102.88888888888889,\"2021-06-13\":104.33333333333333},\"lowData\":{\"2021-06-11\":74.45238095238095,\"2021-06-12\":73.19444444444444,\"2021-06-13\":73.41666666666667},\"minHigh\":\"90\",\"maxHigh\":\"140\",\"minLow\":\"60\",\"maxLow\":\"90\"},\"rate\":{\"avgRate\":\"82\",\"status\":\"1\",\"minRate\":\"50\",\"maxRate\":\"100\",\"data\":{\"2021-06-11\":81.81927710843374,\"2021-06-12\":85.41666666666667,\"2021-06-13\":81.91666666666667}},\"spo2\":{\"avgSpo2\":\"97\",\"status\":\"1\",\"minSpo2\":\"95\",\"data\":{\"2021-06-11\":97.37974683544304,\"2021-06-12\":97.33333333333333,\"2021-06-13\":97.31914893617021}},\"ecg\":{\"times\":\"0\",\"status\":\"1\",\"data\":[]}}}","createTime":1624013186000,"id":1,"modifyTime":1624013186000,"uid":9}],"pageNum":1,"pageSize":1,"pages":1,"size":1}
     * msg :
     */

    public int code;
    public DataBean data;
    public String msg;
//    public List<DataBean> data;

    public static class DataBean implements Serializable {
        public String statisticUrl;
    }
}
