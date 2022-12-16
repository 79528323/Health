package com.gzhealthy.health.model;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class PelletFrameModel {
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
        private String description;//文案
        private int type;//int类型，模块类型：1-体质分析，未完成首次体质问卷；2-体质分析，90天以上未完成过体质问卷；3-健康档案，基础信息；4-健康数据,血糖数据
        private int status;//int类型，状态：0-不弹框，1-弹框

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
