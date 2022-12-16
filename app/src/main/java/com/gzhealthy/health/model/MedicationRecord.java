package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/11/10
 */
public class MedicationRecord implements Serializable {

    /**
     * code : 1
     * data : {"name":"xxx","createTime":1626764697000,"startTime":"2021-09-12","id":1,"endTime":"2021-09-12","frequency":"3次","dose":"3","unit":"升","ifHarmful":0,"harmfulReactions":"无"}
     * msg : 获取成功
     */

    public int code;
    public DataBean data;
    public String msg;

    public static class DataBean implements Parcelable {
        /**
         * name : xxx
         * createTime : 1626764697000
         * startTime : 2021-09-12
         * id : 1
         * endTime : 2021-09-12
         * frequency : 3次
         * dose : 3
         * unit : 升
         * ifHarmful : 0
         * harmfulReactions : 无
         */

        public String name;
        public long createTime;
        public String startTime;
        public int id;
        public String endTime;
        public String frequency;
        public String dose;
        public String unit;
        public int ifHarmful;
        public String harmfulReactions;

        protected DataBean(Parcel in) {
            name = in.readString();
            createTime = in.readLong();
            startTime = in.readString();
            id = in.readInt();
            endTime = in.readString();
            frequency = in.readString();
            dose = in.readString();
            unit = in.readString();
            ifHarmful = in.readInt();
            harmfulReactions = in.readString();
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
            dest.writeString(name);
            dest.writeLong(createTime);
            dest.writeString(startTime);
            dest.writeInt(id);
            dest.writeString(endTime);
            dest.writeString(frequency);
            dest.writeString(dose);
            dest.writeString(unit);
            dest.writeInt(ifHarmful);
            dest.writeString(harmfulReactions);
        }
    }
}
