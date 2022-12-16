package com.gzhealthy.health.model;


import com.gzhealthy.health.model.base.BaseModel;

import java.io.Serializable;
import java.util.List;


public class CollectListModel extends BaseModel<List<CollectListModel.DataBean>> {

    /**
     * msg : 我的收藏获取成功
     * code : 1
     * data : [{"accountId":11,"collectionId":"348","createTime":1545029164000,"id":50,"name":"武汉木兰草原","photo":"1537115845700174141.png","type":2,"typeName":"景点"}]
     */

//    private String msg;
//    private int code;
//    private List<DataBean> data;
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public List<DataBean> getData() {
//        return data;
//    }
//
//    public void setData(List<DataBean> data) {
//        this.data = data;
//    }

    public static class DataBean implements Serializable {
        /**
         * accountId : 11
         * collectionId : 348
         * createTime : 1545029164000
         * id : 50
         * name : 武汉木兰草原
         * photo : 1537115845700174141.png
         * type : 2
         * typeName : 景点
         */

        private int accountId;
        private String collectionId;
        private long createTime;
        private int id;
        private String name;
        private String photo;
        private int type;
        private String typeName;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        private String price;

        public boolean isEditMode() {
            return isEditMode;
        }

        public void setEditMode(boolean editMode) {
            isEditMode = editMode;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        private boolean isEditMode;
        private boolean isChoose;

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
            this.accountId = accountId;
        }

        public String getCollectionId() {
            return collectionId;
        }

        public void setCollectionId(String collectionId) {
            this.collectionId = collectionId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

}
