package net.fkm.updateapp;

import java.io.Serializable;
import java.util.List;

/**
 * 版本信息
 */
public class UpdateAppBean1 implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * msg : 查询成功
     * code : 1
     * data : {"explain":[{"content":"1. 首页全新改造升级，业务模块重新划分。","sort":1},{"content":"2. 新增商机报备模块，提高商机报备效率。","sort":2},{"content":"3. 界面整体优化升级，修复部分BUG，完善体验。","sort":3},{"content":"4. 有任何软件问题可以联系客服反馈。","sort":4}],"msg":"state=1,要更新--->isUpdate 1不强制更新 2强制更新","state":"1","versionName":"1.0.7","url":"http://tofans-oss.tofans.com/tofans.apk","isUpdate":2}
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
         * explain : [{"content":"1. 首页全新改造升级，业务模块重新划分。","sort":1},{"content":"2. 新增商机报备模块，提高商机报备效率。","sort":2},{"content":"3. 界面整体优化升级，修复部分BUG，完善体验。","sort":3},{"content":"4. 有任何软件问题可以联系客服反馈。","sort":4}]
         * msg : state=1,要更新--->isUpdate 1不强制更新 2强制更新
         * state : 1
         * versionName : 1.0.7
         * url : http://tofans-oss.tofans.com/tofans.apk
         * isUpdate : 2
         */
        private String msg;
        private String state;
        private String versionName;
        private String url;
        private int isUpdate;
        private List<ExplainBean> explain;

        private String updateLog;

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

        public static class ExplainBean {
            /**
             * content : 1. 首页全新改造升级，业务模块重新划分。
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

        public String getUpdateLog() {
            return updateLog;
        }

        public void setUpdateLog(String updateLog) {
            this.updateLog = updateLog;
        }
    }

    //网络工具，内部使用
    private HttpManager httpManager;
    private String targetPath;
    private boolean mHideDialog;
    private boolean mShowIgnoreVersion;
    private boolean mDismissNotificationProgress;
    private boolean mOnlyWifi;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public HttpManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public boolean isHideDialog() {
        return mHideDialog;
    }

    public void setHideDialog(boolean mHideDialog) {
        this.mHideDialog = mHideDialog;
    }

    public boolean isShowIgnoreVersion() {
        return mShowIgnoreVersion;
    }

    public void showIgnoreVersion(boolean mShowIgnoreVersion) {
        this.mShowIgnoreVersion = mShowIgnoreVersion;
    }

    public boolean isDismissNotificationProgress() {
        return mDismissNotificationProgress;
    }

    public void dismissNotificationProgress(boolean mDismissNotificationProgress) {
        this.mDismissNotificationProgress = mDismissNotificationProgress;
    }

    public boolean isOnlyWifi() {
        return mOnlyWifi;
    }

    public void setOnlyWifi(boolean mOnlyWifi) {
        this.mOnlyWifi = mOnlyWifi;
    }
}
