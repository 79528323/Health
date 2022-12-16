package com.gzhealthy.health.model;


import com.gzhealthy.health.model.base.BaseModel;

public class AuthorizeLoginModel extends BaseModel<AuthorizeLoginModel.DataBean> {

    /**
     * data : {"tradeTime":"20180917135958","clientId":"10945","sign":"WN/Ctyp20Pj35Z55g+43KIQosA9YiLpblwd9F3nUSCt5Z9HCyp8eRTzSdaXnnL4f70NG5wHuKaAAD6qzV6VelYS+V8lTA2MaGOUVxCZqb6/v59XuPd63MjT4jXynSpBazqkfvAvR6DOZlAuUq1txlC5kGqBjSx8GLAkOw+lygqw=","versionName":"10","encryptType":"RSA"}
     */
    public static class DataBean {

        /**
         * tradeTime : 20180917135958
         * clientId : 10945
         * sign : WN/Ctyp20Pj35Z55g+43KIQosA9YiLpblwd9F3nUSCt5Z9HCyp8eRTzSdaXnnL4f70NG5wHuKaAAD6qzV6VelYS+V8lTA2MaGOUVxCZqb6/v59XuPd63MjT4jXynSpBazqkfvAvR6DOZlAuUq1txlC5kGqBjSx8GLAkOw+lygqw=
         * versionName : 10
         * encryptType : RSA
         */
        private String tradeTime;
        private String clientId;
        private String sign;
        private String versionName;
        private String encryptType;

        public String getTradeTime() {
            return tradeTime;
        }

        public void setTradeTime(String tradeTime) {
            this.tradeTime = tradeTime;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getEncryptType() {
            return encryptType;
        }

        public void setEncryptType(String encryptType) {
            this.encryptType = encryptType;
        }
    }

}
