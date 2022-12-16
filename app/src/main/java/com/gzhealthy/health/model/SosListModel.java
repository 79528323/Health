package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SosListModel {

    private String msg;
    private int code;
    private List<DataModel> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }


    public static class DataModel implements Serializable {
        private String content;
        private String createTime;
        private String extraContent;
        private int id;
        private int ifRead;
        private int state;
        private String title;
        private int type;
        private List<RateWarnListModel> rateWarnList;
        private List<TemperatureWarnListModel> temperatureWarnList;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getExtraContent() {
            return extraContent;
        }

        public void setExtraContent(String extraContent) {
            this.extraContent = extraContent;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIfRead() {
            return ifRead;
        }

        public void setIfRead(int ifRead) {
            this.ifRead = ifRead;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<RateWarnListModel> getRateWarnList() {
            return rateWarnList;
        }

        public void setRateWarnList(List<RateWarnListModel> rateWarnList) {
            this.rateWarnList = rateWarnList;
        }

        public List<TemperatureWarnListModel> getTemperatureWarnList() {
            return temperatureWarnList;
        }

        public void setTemperatureWarnList(List<TemperatureWarnListModel> temperatureWarnList) {
            this.temperatureWarnList = temperatureWarnList;
        }

        public static class RateWarnListModel implements Serializable {
            private String checkTime;
            private int rate;

            public String getCheckTime() {
                return checkTime;
            }

            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
            }

            public int getRate() {
                return rate;
            }

            public void setRate(int rate) {
                this.rate = rate;
            }
        }

        public static class TemperatureWarnListModel implements Serializable {
            private String checkTime;
            private String imei;
            private String temperature;
            private String uid;

            public String getCheckTime() {
                return checkTime;
            }

            public void setCheckTime(String checkTime) {
                this.checkTime = checkTime;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getTemperature() {
                return temperature;
            }

            public void setTemperature(String temperature) {
                this.temperature = temperature;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }
        }
    }
}
