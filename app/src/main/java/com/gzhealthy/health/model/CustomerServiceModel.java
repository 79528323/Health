package com.gzhealthy.health.model;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class CustomerServiceModel {
    private int code;
    private DataDTO data;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataDTO getData() {
        return data;
    }

    public void setData(DataDTO data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataDTO {
        private String phone;
        private String weChat;
        private String qrCodePath;
        private String workTime;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getWeChat() {
            return weChat;
        }

        public void setWeChat(String weChat) {
            this.weChat = weChat;
        }

        public String getQrCodePath() {
            return qrCodePath;
        }

        public void setQrCodePath(String qrCodePath) {
            this.qrCodePath = qrCodePath;
        }

        public String getWorkTime() {
            return workTime;
        }

        public void setWorkTime(String workTime) {
            this.workTime = workTime;
        }
    }
}
