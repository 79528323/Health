package com.gzhealthy.health.contract;

import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.model.HealthyInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Justin_Liu
 * on 2021/4/19
 */
public class PersonInfoContract {


    public interface View{
        void FailRespone(String msg);
    }

    public interface insertPersonView extends View{
        void getSuccess(List<ContractModel.DataBean> list);
    }


    public interface operationPersonView extends View {
        void operSuccess();
    }


    public interface InsertPresenter{

        void addEmergencyContact(Map<String, String> param);
        void getEmergencyContact();
        void editEmergencyContact(Map<String, String> param);
        void deleteEmergencyContact(Map<String, String> param);

    }

}
