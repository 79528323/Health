package com.gzhealthy.health.model.home;

import com.gzhealthy.health.model.base.Base1Model;

/**
 * App应用配置信息
 */
public class ConfigModel extends Base1Model {

    /**
     * msg : APP配置获取成功
     * code : 1
     * data : {"cruiseUrl":"https://im.7x24cc.com/phone_webChat.html?accountId=N000000016899&chatId=f761c3a3-93b6-48a0-84c5-51660280a6e4","customerServiceUrl":"https://im.7x24cc.com/phone_webChat.html?accountId=N000000016899&chatId=f761c3a3-93b6-48a0-84c5-51660280a6e4","distributorPhotoUrl":"defaultPhoto.png","shoppingMallUrl":"https://mall.tofans.com/wsy_pub/web/index.php?m=app_index&a=index&customer_id=czo0OiIzODQ0Ijs&template_id=1126&app=web_app_h5"}
     */
    private String msg;
    private int code;
    private DataBean data;
    private int isOpenNew;

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

    public class DataBean {
        /**
         * cruiseUrl : https://im.7x24cc.com/phone_webChat.html?accountId=N000000016899&chatId=f761c3a3-93b6-48a0-84c5-51660280a6e4
         * customerServiceUrl : https://im.7x24cc.com/phone_webChat.html?accountId=N000000016899&chatId=f761c3a3-93b6-48a0-84c5-51660280a6e4
         * distributorPhotoUrl : defaultPhoto.png
         * shoppingMallUrl : https://mall.tofans.com/wsy_pub/web/index.php?m=app_index&a=index&customer_id=czo0OiIzODQ0Ijs&template_id=1126&app=web_app_h5
         */

        private String cruiseUrl;
        private String customerServiceUrl;
        private String distributorPhotoUrl;
        private String shoppingMallUrl;
        private String noticeUrl;
        private int isOpenNew;
        private String newShopUrl;
        private String businessReportUrl;
        private String cultureUrl;
        private String liveBroadcastUrl;
        private String taskHallUrl;

        public String getBusinessReportUrl() {
            return businessReportUrl;
        }

        public void setBusinessReportUrl(String businessReportUrl) {
            this.businessReportUrl = businessReportUrl;
        }

        public String getCultureUrl() {
            return cultureUrl;
        }

        public void setCultureUrl(String cultureUrl) {
            this.cultureUrl = cultureUrl;
        }

        public String getLiveBroadcastUrl() {
            return liveBroadcastUrl;
        }

        public void setLiveBroadcastUrl(String liveBroadcastUrl) {
            this.liveBroadcastUrl = liveBroadcastUrl;
        }

        public String getTaskHallUrl() {
            return taskHallUrl;
        }

        public void setTaskHallUrl(String taskHallUrl) {
            this.taskHallUrl = taskHallUrl;
        }

        public int getIsOpenNew() {
            return isOpenNew;
        }

        public void setIsOpenNew(int isOpenNew) {
            this.isOpenNew = isOpenNew;
        }

        public String getNewShopUrl() {
            return newShopUrl;
        }

        public void setNewShopUrl(String newShopUrl) {
            this.newShopUrl = newShopUrl;
        }

        public String getCruiseUrl() {
            return cruiseUrl;
        }

        public String getNoticeUrl() {
            return noticeUrl;
        }

        public void setNoticeUrl(String noticeUrl) {
            this.noticeUrl = noticeUrl;
        }

        public void setCruiseUrl(String cruiseUrl) {
            this.cruiseUrl = cruiseUrl;
        }

        public String getCustomerServiceUrl() {
            return customerServiceUrl;
        }

        public void setCustomerServiceUrl(String customerServiceUrl) {
            this.customerServiceUrl = customerServiceUrl;
        }

        public String getDistributorPhotoUrl() {
            return distributorPhotoUrl;
        }

        public void setDistributorPhotoUrl(String distributorPhotoUrl) {
            this.distributorPhotoUrl = distributorPhotoUrl;
        }

        public String getShoppingMallUrl() {
            return shoppingMallUrl;
        }

        public void setShoppingMallUrl(String shoppingMallUrl) {
            this.shoppingMallUrl = shoppingMallUrl;
        }
    }

}
