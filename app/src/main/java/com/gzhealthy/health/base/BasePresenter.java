package com.gzhealthy.health.base;

import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.protocol.BaseView;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.tool.HttpUtils;

import java.util.List;

import rx.Observable;


public class BasePresenter<T extends LifeSubscription, V extends BaseView> {

    protected T lifeSubscription;

    protected V baseView;


    public void setLifeSubscription(LifeSubscription lifeSubscription) {
        this.lifeSubscription = (T) lifeSubscription;
    }

    public void setBaseView(BaseView baseView) {
        this.baseView = (V) baseView;
    }


    protected <T> void invoke(Observable<T> observable, CallBack<T> callback) {
        HttpUtils.invoke(lifeSubscription, baseView, observable, callback);
    }

    public void loadData(String... params) {
    }



    /**
     * 给子类检查返回集合是否为空
     * 这样子做虽然耦合度高，但是接口都不是统一定的，我们没有什么更好的办法
     *
     * @param list
     */
    public void checkState(List list) {
        if (list == null || list.size() == 0) {
            baseView.setState(Constants.ResponseStatus.STATE_EMPTY);
            return;
        }
    }

}
