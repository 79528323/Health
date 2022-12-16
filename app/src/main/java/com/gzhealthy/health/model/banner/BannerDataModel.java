package com.gzhealthy.health.model.banner;


import com.gzhealthy.health.model.base.Base1Model;

import java.util.List;

public class BannerDataModel extends Base1Model {


    /**
     * msg : 广告获取成功
     * code : 1
     * data : {"adVoList":[{"adId":3,"adImg":"1608087147864074326.png","adName":"资讯详情","skipPosition":"url","skipType":"3"},{"adId":4,"adImg":"1608087147864074326.png","adName":"无跳转","skipType":"1"}],"advId":2,"advName":"安卓资讯首页","location":"1","showType":"2","terminal":"2"}
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
         * adVoList : [{"adId":3,"adImg":"1608087147864074326.png","adName":"资讯详情","skipPosition":"url","skipType":"3"},{"adId":4,"adImg":"1608087147864074326.png","adName":"无跳转","skipType":"1"}]
         * advId : 2
         * advName : 安卓资讯首页
         * location : 1
         * showType : 2
         * terminal : 2
         */
        private int advId;
        private String advName;
        private String location;
        private String showType;
        private String terminal;
        private List<AdVoListBean> adVoList;

        public int getAdvId() {
            return advId;
        }

        public void setAdvId(int advId) {
            this.advId = advId;
        }

        public String getAdvName() {
            return advName;
        }

        public void setAdvName(String advName) {
            this.advName = advName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }

        public String getTerminal() {
            return terminal;
        }

        public void setTerminal(String terminal) {
            this.terminal = terminal;
        }

        public List<AdVoListBean> getAdVoList() {
            return adVoList;
        }

        public void setAdVoList(List<AdVoListBean> adVoList) {
            this.adVoList = adVoList;
        }

        public static class AdVoListBean {
            /**
             * adId : 3
             * adImg : 1608087147864074326.png
             * adName : 资讯详情
             * skipPosition : url
             * skipType : 3
             */

            private int adId;
            private String adImg;
            private String adName;
            private String skipPosition;
            private String skipType;

            public int getAdId() {
                return adId;
            }

            public void setAdId(int adId) {
                this.adId = adId;
            }

            public String getAdImg() {
                return adImg;
            }

            public void setAdImg(String adImg) {
                this.adImg = adImg;
            }

            public String getAdName() {
                return adName;
            }

            public void setAdName(String adName) {
                this.adName = adName;
            }

            public String getSkipPosition() {
                return skipPosition;
            }

            public void setSkipPosition(String skipPosition) {
                this.skipPosition = skipPosition;
            }

            public String getSkipType() {
                return skipType;
            }

            public void setSkipType(String skipType) {
                this.skipType = skipType;
            }
        }
    }

}
