package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.Base1Model;

import java.util.List;

/**
 * 滚动播报消息实体对象
 */
public class RollNewsModel extends Base1Model {


    /**
     * msg : 滚动资讯获取成功
     * code : 1
     * data : [{"id":2,"title":"一觉醒来左臂动弹不得，只因做了这件事！寒冷天气高发！","url":"http://xxx.xxx.com/xx/#/news_detail/2"}]
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
         * id : 2
         * title : 一觉醒来左臂动弹不得，只因做了这件事！寒冷天气高发！
         * url : http://xxx.xxx.com/xx/#/news_detail/2
         */

        private int id;
        private String title;
        private String url;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
