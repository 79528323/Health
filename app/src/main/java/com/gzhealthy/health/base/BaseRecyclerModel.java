package com.gzhealthy.health.base;

import com.gzhealthy.health.model.base.BaseModel;

import java.util.Comparator;


public class BaseRecyclerModel extends BaseModel<BaseRecyclerModel> implements Comparator<BaseRecyclerModel> {
    private int baseRecylerType;
    private boolean isEdit;
    private boolean isCheck;

    public BaseRecyclerModel(int baseRecylerType) {
        this.baseRecylerType = baseRecylerType;
    }

    public BaseRecyclerModel() {
    }

    public int getBaseRecylerType() {
        return baseRecylerType;
    }

    public void setBaseRecylerType(int baseRecylerType) {
        this.baseRecylerType = baseRecylerType;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int compare(BaseRecyclerModel o1, BaseRecyclerModel o2) {
        return o1.getBaseRecylerType() - o2.getBaseRecylerType();
    }
}
