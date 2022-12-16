package com.gzhealthy.health.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Justin_Liu
 * on 2021/7/5
 */
public class HaveFamilyMember implements Serializable {

    public String msg;
    public int code;
    public DataBean data;

    public static class DataBean implements Serializable {

        public int ifExist;
        public String introduceUrl;
        public int inviteNum;
    }
}
