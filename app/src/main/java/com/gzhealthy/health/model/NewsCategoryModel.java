package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.Base1Model;

import java.util.List;


public class NewsCategoryModel extends Base1Model {

    /**
     * msg : 资讯首页以及分类获取成功
     * code : 1
     * data : [{"id":3,"title":"体安故事"},{"id":2,"title":"疾病预防"},{"id":1,"title":"养生保健"}]
     */
    private String msg;
    private int code;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * title : 体安故事
         */
        private int id;
        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}
