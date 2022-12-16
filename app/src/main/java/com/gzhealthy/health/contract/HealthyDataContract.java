package com.gzhealthy.health.contract;

import com.gzhealthy.health.base.BaseContractView;
import com.gzhealthy.health.model.EKGModel;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.model.HealthyListDataModel;
import com.gzhealthy.health.model.TestData;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class HealthyDataContract {

    public interface View extends BaseContractView {
        void getInfoSuccess(HealthyListDataModel model);
        void getInfoFail(String msg);
    }

    public interface EKGView{
        void getSuccess(List<EKGModel.DataBean> list,String statisticUrl);
        void getFail(String msg,String statisticUrl);
    }

    public interface GluView extends BaseContractView{
        void getInfoRespone(HealthyListDataModel model);
        void getInfoFail(String msg);
    }

    public static abstract class Presenter{
        public Presenter() {}

        public abstract void getHealthyInfo(Map<String, String> param);

        public abstract void getEKGInfo(Map<String,String> param);

        public abstract void getGluInfo(Map<String,String> param);
    }

}
