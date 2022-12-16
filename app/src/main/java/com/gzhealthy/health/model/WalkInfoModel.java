package com.gzhealthy.health.model;

import com.gzhealthy.health.model.base.BaseModel;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/4/22
 */
public class WalkInfoModel extends BaseModel<WalkInfoModel.DataBean> {


    public static class DataBean implements Serializable {
        public float distance;
        public float kcal;
        public int walk;
    }
}
