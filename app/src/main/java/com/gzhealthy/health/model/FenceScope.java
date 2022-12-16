package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/11/12
 */
public class FenceScope implements Serializable {

    /**
     * code : 1
     * data : {"id":1,"imei":"12123","location":"2323.2323,232.21","address":"黄浦区大学","fenceType":0,"radiusOrPoints":"2312.2323,232.2323;232.23,23232.22"}
     * msg : 获取成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Parcelable {
        /**
         * id : 1
         * imei : 12123
         * location : 2323.2323,232.21
         * address : 黄浦区大学
         * fenceType : 0
         * radiusOrPoints : 2312.2323,232.2323;232.23,23232.22
         */

        public int id;
        public String imei;
        public String location;
        public String address;
        public int fenceType;
        public String radiusOrPoints;

        protected DataBean(Parcel in) {
            id = in.readInt();
            imei = in.readString();
            location = in.readString();
            address = in.readString();
            fenceType = in.readInt();
            radiusOrPoints = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(imei);
            dest.writeString(location);
            dest.writeString(address);
            dest.writeInt(fenceType);
            dest.writeString(radiusOrPoints);
        }
    }
}
