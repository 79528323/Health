package com.gzhealthy.health.model;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public  class MedicationRecordDetailModel {
    private int code;
    private DataBean data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private String name;
        private String createTime;
        private String startTime;
        private String id;
        private String endTime;
        private String frequency;
        private String dose;
        private String harmfulReactions;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getDose() {
            return dose;
        }

        public void setDose(String dose) {
            this.dose = dose;
        }

        public String getHarmfulReactions() {
            return harmfulReactions;
        }

        public void setHarmfulReactions(String harmfulReactions) {
            this.harmfulReactions = harmfulReactions;
        }
    }
}
