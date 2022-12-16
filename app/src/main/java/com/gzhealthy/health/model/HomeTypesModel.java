package com.gzhealthy.health.model;

/**
 * Created by Justin_Liu
 * on 2021/7/29
 */
public class HomeTypesModel {
    public int resId;
    public int type; // 1：健康档案  2：SOS联系人

    public HomeTypesModel() {
    }

    public HomeTypesModel(int resId, int type) {
        this.resId = resId;
        this.type = type;
    }
}
