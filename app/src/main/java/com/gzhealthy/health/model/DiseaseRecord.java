package com.gzhealthy.health.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/11/10
 */
public class DiseaseRecord implements Serializable {

    /**
     * msg : 获取成功了
     * code : 1
     * data : [{"id":121,"type":"门诊","seeDate":"2021-08-09","diagnosis":"xx","hospital":"xx"}]
     */

    public String msg;
    public int code;
    public List<DataBean> data;

    public static class DataBean implements Parcelable {
        /**
         * id : 121
         * type : 门诊
         * seeDate : 2021-08-09
         * diagnosis : xx
         * hospital : xx
         */

        public int id;
        public String type;
        public String seeDate;
        public String diagnosis;
        public String hospital;

        protected DataBean(Parcel in) {
            id = in.readInt();
            type = in.readString();
            seeDate = in.readString();
            diagnosis = in.readString();
            hospital = in.readString();
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
            dest.writeString(type);
            dest.writeString(seeDate);
            dest.writeString(diagnosis);
            dest.writeString(hospital);
        }
    }
}
