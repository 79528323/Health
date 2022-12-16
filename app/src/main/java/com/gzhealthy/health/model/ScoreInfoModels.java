package com.gzhealthy.health.model;


import com.gzhealthy.health.model.base.Base1Model;

public class ScoreInfoModels extends Base1Model {

    /**
     * msg : 我的积分获取成功
     * code : 1
     * data : {"createTime":1608520675000,"tiAn":3030}
     */
    private String msg;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * createTime : 1608520675000
         * tiAn : 3030
         */
        private long createTime;
        private int tiAn;

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getTiAn() {
            return tiAn;
        }

        public void setTiAn(int tiAn) {
            this.tiAn = tiAn;
        }
    }

}
