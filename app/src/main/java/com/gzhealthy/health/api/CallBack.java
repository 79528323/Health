package com.gzhealthy.health.api;


import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.NetworkUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import rx.Subscriber;

public abstract class CallBack<T> extends Subscriber<T> {

    private final String TAG = CallBack.class.getSimpleName();

    private ResponseState state;

    public void setTarget(ResponseState state) {
        this.state = state;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        onfail(e);
    }

//    BaseModel baseModel;

    @Override
    public void onNext(T data) {
        //  这边网络请求成功返回都不一样所以不能在这里统一写了（如果是自己公司需要规定一套返回方案）
        //  这里先统一处理为成功   我们要是想检查返回结果的集合是否是空，只能去子类回掉中完成了。
        //  统一处理10086
        if (data instanceof BaseModel) {
            BaseModel baseModel = (BaseModel) data;
            if (baseModel.getCode() == 10086) {
                SPCache.putString(SPCache.KEY_TOKEN,"");
                if (state instanceof Activity) {
                    LoginActivity.instance((Activity) state, "10086");
                    return;
                }
                if (state instanceof Fragment) {
                    LoginActivity.instance(((Fragment) state).getActivity(), "10086");
                    return;
                }
                ToastUtil.showToast(baseModel.getMsg());
            }
        }
        if (state!=null) {
            state.setState(Constants.ResponseStatus.STATE_SUCCESS);
        }
        onResponse(data);
    }

    public abstract void onResponse(T data);


    public void onfail(Throwable e) {
        String errorMsg = HealthApp.getInstance().getString(R.string.server_net_error);
        if (!NetworkUtils.isAvailableByPing()) {
            errorMsg = HealthApp.getInstance().getString(R.string.native_net_error);
        }else {
            if (e instanceof SocketTimeoutException || e instanceof HttpException
                || e instanceof ConnectException || e instanceof UnknownHostException){
                errorMsg = HealthApp.getInstance().getString(R.string.server_net_exception);
            }
        }
        ToastUtil.showToast(errorMsg);
    }
}
