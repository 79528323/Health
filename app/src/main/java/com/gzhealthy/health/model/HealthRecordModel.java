package com.gzhealthy.health.model;

import java.util.List;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class HealthRecordModel {
    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String reportUrl;
        private long createTime;
        private String device;
        private String store;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReportUrl() {
            return reportUrl;
        }

        public void setReportUrl(String reportUrl) {
            this.reportUrl = reportUrl;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }
    }
}
