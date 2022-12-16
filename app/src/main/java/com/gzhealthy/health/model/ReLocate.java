package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Justin_Liu
 * on 2021/11/12
 */
public class ReLocate {
    public int code;
    public String msg;

    public static class DataBean implements Parcelable {
        String location;
        String address;

        protected DataBean(Parcel in) {
            location = in.readString();
            address = in.readString();
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
            dest.writeString(location);
            dest.writeString(address);
        }
    }
}
