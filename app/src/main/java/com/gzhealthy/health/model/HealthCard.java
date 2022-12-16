package com.gzhealthy.health.model;

/**
 * Created by Justin_Liu
 * on 2021/8/3
 */
public class HealthCard {
    public String name;
    /**
     * 1-心率  2-血压  3-睡眠  4-心电  5-血糖  6-血氧  7-BMI   8-体温
      */
    public int type;

    public HealthCard() {
    }

    public HealthCard(String name, int type) {
        this.name = name;
        this.type = type;
    }

}
