package com.gzhealthy.health.tool;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.utils.ToastUtil;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class HttpUtils {

    public static <T> void invoke(LifeSubscription lifecycle, ResponseState responseState,
                                  Observable<T> observable, CallBack<T> callback) {
        callback.setTarget(responseState);
        /**
         * 先判断网络连接状态和网络是否可用，放在回调那里好呢，还是放这里每次请求都去判断下网络是否可用好呢？
         * 如果放在请求前面太耗时了，如果放回掉提示的速度慢，要10秒钟请求超时后才提示。
         * 最后采取的方法是判断网络是否连接放在外面，网络是否可用放在回掉。
         */
        if (!NetworkUtils.isConnected(HealthApp.getInstance())) {
            ToastUtil.showToast(HealthApp.getInstance().getString(R.string.net_error_check));
//            if (responseState != null) {
//                responseState.setState(Constants.ResponseStatus.STATE_ERROR);
//            }
            return;
        }

        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
        lifecycle.bindSubscription(subscription);

    }

}
