package com.gzhealthy.health.presenter;

import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.contract.HealthyDataContract;
import com.gzhealthy.health.contract.HomeContract;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.model.TestData;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HealthyDataPresenter extends HealthyDataContract.Presenter {
    private LifeSubscription lifeSubscription;
    private ResponseState responseState;
    private HealthyDataContract.View view;
    private HealthyDataContract.EKGView ekgView;
    private HealthyDataContract.GluView gluView;

    public HealthyDataPresenter(HealthyDataContract.View view, LifeSubscription lifeSubscription, ResponseState responseState) {
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
        this.view = view;
    }

    public HealthyDataPresenter(HealthyDataContract.EKGView view, LifeSubscription lifeSubscription, ResponseState responseState){
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
        this.ekgView = view;
    }

    public HealthyDataPresenter(HealthyDataContract.GluView view, LifeSubscription lifeSubscription, ResponseState responseState){
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
        this.gluView = view;
    }

    @Override
    public void getHealthyInfo(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getHealthInfo(param),
                new CallBack<HealthyListDataModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        view.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        view.dimissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        view.dimissLoading();
                    }

                    @Override
            public void onResponse(HealthyListDataModel model) {
               if (model.code == 1){
                    view.getInfoSuccess(model);
               }else
                   view.getInfoFail(model.msg);
            }
        });
    }

    @Override
    public void getEKGInfo(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getEKGInfo(param), new CallBack<EKGModel>() {
            @Override
            public void onResponse(EKGModel model) {
                if (model.code == 1){
                    ekgView.getSuccess(model.data,model.statisticUrl);
                }else
                    ekgView.getFail(model.msg,model.statisticUrl);
            }
        });
    }

    @Override
    public void getGluInfo(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getHealthInfo(param),
                new CallBack<HealthyListDataModel>() {


                    @Override
                    public void onStart() {
                        super.onStart();
                        gluView.showLoading();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        gluView.dimissLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        gluView.dimissLoading();
                    }

            @Override
            public void onResponse(HealthyListDataModel model) {
                if (model.code == 1){
                    gluView.getInfoRespone(model);
                }else {
                    gluView.getInfoRespone(null);
                }
            }
        });
    }
}
