package com.gzhealthy.health.model;


import com.gzhealthy.health.model.base.BaseModel;

import java.util.List;

public class ScoreDetailModel extends BaseModel<List<ScoreDetailModel.DataBean>> {


    public static class DataBean {

        /**
         * content : 阅读文章
         * createTime : 2020-12-21
         * num : 30
         * type : 2
         */
        private String content;
        private String createTime;
        private int num;
        private int type;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }
}
