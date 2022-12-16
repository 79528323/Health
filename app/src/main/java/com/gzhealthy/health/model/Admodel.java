package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.BaseModel;

import java.util.List;


public class Admodel extends BaseModel<Admodel.DataBean> {

    public static class DataBean {

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
