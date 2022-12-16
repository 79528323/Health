package com.gzhealthy.health.model;


public class TestData {
    private String text;
    private String groupName;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public TestData(String text, String groupName) {
        this.text = text;
        this.groupName = groupName;
    }
}
