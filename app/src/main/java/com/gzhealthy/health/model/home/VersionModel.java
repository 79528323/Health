package com.gzhealthy.health.model.home;

import com.gzhealthy.health.model.base.Base1Model;

import java.io.Serializable;
import java.util.List;


public class VersionModel extends Base1Model {


    /**
     * msg : 查询成功
     * code : 1
     * data : {"explain":[{"content":"1.强制更新","sort":1},{"content":"2.强制更新","sort":2}],"msg":"state=1,要更新--->isUpdate 1不强制更新 2强制更新","state":"1","versionName":"3.0.0","url":"http://if.infuncar.com/yfqc/yunfan/download.html","isUpdate":2}
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

    public static class DataBean implements Serializable {
        /**
         * explain : [{"content":"1.强制更新","sort":1},{"content":"2.强制更新","sort":2}]
         * msg : state=1,要更新--->isUpdate 1不强制更新 2强制更新
         * state : 1
         * versionName : 3.0.0
         * url : http://if.infuncar.com/yfqc/yunfan/download.html
         * isUpdate : 2
         */

        private String msg;
        private String state;
        private String versionName;
        private String url;
        private int isUpdate;
        private List<ExplainBean> explain;

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getIsUpdate() {
            return isUpdate;
        }

        public void setIsUpdate(int isUpdate) {
            this.isUpdate = isUpdate;
        }

        public List<ExplainBean> getExplain() {
            return explain;
        }

        public void setExplain(List<ExplainBean> explain) {
            this.explain = explain;
        }

        public static class ExplainBean implements Serializable {
            /**
             * content : 1.强制更新
             * sort : 1
             */

            private String content;
            private int sort;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }
        }
    }
}
