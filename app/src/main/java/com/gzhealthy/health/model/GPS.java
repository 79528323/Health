package com.gzhealthy.health.model;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2021/7/9
 */
public class GPS implements Serializable {

    /**
     * msg : 获取GPS定位成功
     * code : 1
     * data : 广东省广州市科学大道广州市科学成中心区
     * location : 113.4527007,23.1716287
     */

    public String msg;
    public int code;
    public String data;
    public String location;
}
