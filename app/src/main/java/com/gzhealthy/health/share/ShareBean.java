package com.gzhealthy.health.share;

public class ShareBean {

    /**
     * msg : 产品分享详情获取成功
     * code : 1
     * data : {"productCode":"cp43998153","productId":3905,"productPhoto":"1542166454884036477.jpg","productThemeRemark":"【广州出发】","sellPrice":395000,"sellerProductName":"<海南蜈支洲岛、天堂森林公园、南山文化苑、国际五星双飞四日游>","shareSlogan":"精致生活，私享旅行"}
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
         * productCode : cp43998153
         * productId : 3905
         * productPhoto : 1542166454884036477.jpg
         * productThemeRemark : 【广州出发】
         * sellPrice : 395000
         * sellerProductName : <海南蜈支洲岛、天堂森林公园、南山文化苑、国际五星双飞四日游>
         * shareSlogan : 精致生活，私享旅行
         */
        private String ewmurl;
        private String productCode;
        private int productId;
        private String productPhoto;
        private String productThemeRemark;
        private int sellPrice;
        private String sellerProductName;
        private String shareSlogan;

        public String getEwmurl() {
            return ewmurl;
        }

        public void setEwmurl(String ewmurl) {
            this.ewmurl = ewmurl;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public String getProductPhoto() {
            return productPhoto;
        }

        public void setProductPhoto(String productPhoto) {
            this.productPhoto = productPhoto;
        }

        public String getProductThemeRemark() {
            return productThemeRemark;
        }

        public void setProductThemeRemark(String productThemeRemark) {
            this.productThemeRemark = productThemeRemark;
        }

        public int getSellPrice() {
            return sellPrice;
        }

        public void setSellPrice(int sellPrice) {
            this.sellPrice = sellPrice;
        }

        public String getSellerProductName() {
            return sellerProductName;
        }

        public void setSellerProductName(String sellerProductName) {
            this.sellerProductName = sellerProductName;
        }

        public String getShareSlogan() {
            return shareSlogan;
        }

        public void setShareSlogan(String shareSlogan) {
            this.shareSlogan = shareSlogan;
        }
    }
}
