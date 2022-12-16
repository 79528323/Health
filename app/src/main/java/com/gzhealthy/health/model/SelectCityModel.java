package com.gzhealthy.health.model;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class SelectCityModel extends DataSupport implements Serializable {
    private String areaId;
    private String areaName;
    private String letter;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
