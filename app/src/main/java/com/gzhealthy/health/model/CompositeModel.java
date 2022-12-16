package com.gzhealthy.health.model;

public class CompositeModel {

    private int id;
    private boolean isSelect;

    public boolean isConfirm() {
        return isConfirm;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    private boolean isConfirm;

    public boolean isReset() {
        return isReset;
    }

    public void setReset(boolean reset) {
        isReset = reset;
    }

    private boolean isReset;
    private String content;

    public CompositeModel() {
    }

    public CompositeModel(int id, boolean isSelect, String content) {
        this.id = id;
        this.isSelect = isSelect;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
