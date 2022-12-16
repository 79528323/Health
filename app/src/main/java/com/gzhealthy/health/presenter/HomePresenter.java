package com.gzhealthy.health.presenter;

import com.gzhealthy.health.api.ApiInterface;
import com.gzhealthy.health.api.ApiService;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.contract.HomeContract;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.TestData;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;

import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HomePresenter extends HomeContract.Presenter {
    private LifeSubscription lifeSubscription;
    private ResponseState responseState;
    private HomeContract.View view;

    public HomePresenter(HomeContract.View view,LifeSubscription lifeSubscription,ResponseState responseState) {
        this.lifeSubscription = lifeSubscription;
        this.responseState = responseState;
        this.view = view;
    }


    /**
     * 查询首页最新数据
     * @param param
     */
    @Override
    public void QueryHomeHealthyInfo(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().queryLatestHealthInfo(param), new CallBack<HealthyInfo>() {
            @Override
            public void onResponse(HealthyInfo info) {
                if (info.code == 1)
                    view.QueryInfoSuccess(info);
                else
                    view.QueryInfoFail(info.msg);
            }
        });
    }


    /**
     * 绑定手表申请
     * @param param
     */
    @Override
    public void watchBindApply(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().watchBindApply(param), new CallBack<BaseModel>() {
            @Override
            public void onResponse(BaseModel data) {
                if (data.getCode() == 1){
                    view.goBindApply(data.getMsg());
                }else
                    view.failBindApply(data.getMsg());
            }
        });
    }

//    @Override
//    public void getWatchBindSituation(Map<String, String> param) {
//        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getWatchBindSituation(param), new CallBack<BaseModel>() {
//            @Override
//            public void onResponse(BaseModel data) {
//                String msg = data.getMsg();
//                if (data.isStatus())
//                    view.WatchBindSituationSuccess(msg);
//                else
//                    view.WatchBindSituationFail(msg,param.get("imei"));
//            }
//        });
//    }

    @Override
    public void watchBindConfirm(Map<String, String> param) {
//        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getWatchBindSituation(param), new CallBack<BaseModel>() {
//            @Override
//            public void onResponse(BaseModel data) {
//                String msg = data.getMsg();
//                if (data.getCode()==200)
//                    view.confirmSuccess(msg);
//                else
//                    view.confirmFail(msg);
//            }
//        });
    }

    @Override
    public void getLocation(Map<String, String> param) {
        HttpUtils.invoke(lifeSubscription, responseState, InsuranceApiFactory.getmHomeApi().getLatestGPS(param),
                new CallBack<GPS>() {
            @Override
            public void onResponse(GPS data) {
                if (data.code == 1)
                    view.getLocSuccess(data);
                else {
                    view.getLocFail("暂无手表定位");
                }
            }
        });
    }


}
