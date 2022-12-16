package com.gzhealthy.health.presenter;

import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.contract.HomeContract;
import com.gzhealthy.health.contract.PersonInfoContract;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class PersonInsertPresenter implements PersonInfoContract.InsertPresenter {
    private LifeSubscription lifeSubscription;
    private ResponseState responseState;
    private PersonInfoContract.insertPersonView insertview;
    private PersonInfoContract.operationPersonView operView;

    public PersonInsertPresenter(PersonInfoContract.View view, LifeSubscription lifeSubscription, ResponseState responseState) {
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
        this.insertview = (PersonInfoContract.insertPersonView) view;
//        this.operView = (PersonInfoContract.operationPersonView) view;
    }

    public PersonInsertPresenter(PersonInfoContract.operationPersonView view, LifeSubscription lifeSubscription, ResponseState responseState) {
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
//        this.insertview = (PersonInfoContract.insertPersonView) view;
        this.operView = view;
    }


    /**
     * 添加联系人
     * @param param
     */
    @Override
    public void addEmergencyContact(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState,
                InsuranceApiFactory.getmHomeApi().addEmergencyContact(param, SPCache.getString("token","")),
                new CallBack<BaseModel>() {
            @Override
            public void onResponse(BaseModel info) {
                if (info.getCode() == 1){
                    operView.operSuccess();
                }else {
                    operView.FailRespone(info.getMsg());
                }
            }
        });
    }

    @Override
    public void getEmergencyContact() {
        HttpUtils.invoke(lifeSubscription, responseState,
                InsuranceApiFactory.getmHomeApi().getEmergencyContact(SPCache.getString("token","")),
                new CallBack<ContractModel>() {
                    @Override
                    public void onResponse(ContractModel model) {
                        if (model.code == 1){
                            insertview.getSuccess(model.data);
                        }else {
                            insertview.FailRespone(model.msg);
                        }
                    }
                });
    }

    @Override
    public void editEmergencyContact(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState,
                InsuranceApiFactory.getmHomeApi().editEmergencyContact(param,SPCache.getString("token","")),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel model) {
                        if (model.getCode() == 1){
                            operView.operSuccess();
                        }else {
                            operView.FailRespone(model.getMsg());
                        }
                    }
                });
    }

    @Override
    public void deleteEmergencyContact(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState,
                InsuranceApiFactory.getmHomeApi().deleteEmergencyContact(param,SPCache.getString("token","")),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel model) {
                        if (model.getCode() == 1){
                            operView.operSuccess();
                        }else {
                            operView.FailRespone(model.getMsg());
                        }
                    }
                });
    }
}
