package com.gzhealthy.health.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.gzhealthy.health.activity.report.HealthyReportLoadingActivity;
import com.gzhealthy.health.activity.report.HealthyReportResultActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;

import java.util.HashMap;
import java.util.Map;


/**
 * WebView的js交互
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class WebViewJavascriptInterface {
    public static final String INTERFACE_NAME = "app";
    private Activity mActivity;

    public WebViewJavascriptInterface(Activity activity) {
        mActivity = activity;
    }

    @JavascriptInterface
    public void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        ToastUtil.showToast(msg);
    }

    @JavascriptInterface
    public void finish() {
        mActivity.finish();
    }

    @JavascriptInterface
    public void onGotoConstitution(){
        RxBus.getInstance().postEmpty(RxEvent.ON_GET_HEALTH_REPORT);
    }



//    public void isGetReport(){
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().isGetReport(
//                SPCache.getString(SPCache.KEY_TOKEN,"")),
//                new CallBack<BaseModel<Boolean>>() {
//
//                    @Override
//                    public void onResponse(BaseModel<Boolean> model) {
//                        if (model.code != 1){
//                            ToastUtil.showToast(model.msg);
////                            goBack();
//                        }else {
//                            if (!model.data){
//                                ToastUtil.showToast(model.msg);
//                            }else {
//                                getReport();
//                            }
//                        }
//                    }
//                });
//    }


}
