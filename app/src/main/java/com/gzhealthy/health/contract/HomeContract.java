package com.gzhealthy.health.contract;

import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.model.GPS;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.TestData;

import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HomeContract {

    public interface View{
        void QueryInfoSuccess(HealthyInfo info);
        void QueryInfoFail(String msg);

        void goBindApply(String msg);
        void failBindApply(String msg);

//        void WatchBindSituationSuccess(String msg);
//        void WatchBindSituationFail(String msg,String IMEI);

        void confirmSuccess(String msg);
        void confirmFail(String msg);

        void getLocSuccess(GPS msg);
        void getLocFail(String msg);
    }


    public static abstract class Presenter{
        public Presenter() {}

        public abstract void QueryHomeHealthyInfo(Map<String, String> param);

        public abstract void watchBindApply(Map<String, String> param);

//        public abstract void getWatchBindSituation(Map<String, String> param);

        public abstract void watchBindConfirm(Map<String, String> param);

        public abstract void getLocation(Map<String,String> param);
    }

}
