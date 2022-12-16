package com.gzhealthy.health.model;

import java.util.ArrayList;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class RegionBean {
    private ArrayList<String> province;
    private ArrayList<ArrayList<String>> city;
    private ArrayList<ArrayList<ArrayList<String>>> area;

    public ArrayList<String> getProvince() {
        return province;
    }

    public void setProvince(ArrayList<String> province) {
        this.province = province;
    }

    public ArrayList<ArrayList<String>> getCity() {
        return city;
    }

    public void setCity(ArrayList<ArrayList<String>> city) {
        this.city = city;
    }

    public ArrayList<ArrayList<ArrayList<String>>> getArea() {
        return area;
    }

    public void setArea(ArrayList<ArrayList<ArrayList<String>>> area) {
        this.area = area;
    }
}
