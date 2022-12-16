package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.RateWarnHelpModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.push.MyJPushMessageReceiver;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import rx.functions.Action1;

/**
 * 心率异常提醒弹窗
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class RateWarnDialog extends Dialog implements View.OnClickListener {
    private RxManager mRxManager;
    private String mRemark;
    private LifeSubscription mLifeSubscription;
    private ResponseState mResponseState;

    public RateWarnDialog(@NonNull Context context, RxManager rxManager, LifeSubscription lifeSubscription, ResponseState responseState) {
        super(context);
        mRxManager = rxManager;
        mLifeSubscription = lifeSubscription;
        mResponseState = responseState;
    }

    public RateWarnDialog setRemark(String remark) {
        mRemark = remark;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_rate_warn);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.TOP);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().y = ScreenUtils.getScreenHeight() / 4;

        findViewById(R.id.tvHelp).setOnClickListener(this);
        findViewById(R.id.tvCancle).setOnClickListener(this);

        mRxManager.onRxEvent(MyJPushMessageReceiver.CONTENT_TYPE_CANCEL)
                .subscribe((Action1<Object>) o -> {
                    dismiss();
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvHelp:
                help();
                break;
            case R.id.tvCancle:
                dismiss();
                appRateWarnCancel();
                break;
        }
    }

    private void help() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString("token", ""));
        param.put("remark", mRemark);
        HttpUtils.invoke(mLifeSubscription, mResponseState,
                InsuranceApiFactory.getmHomeApi().rateWarnHelp(param),
                new CallBack<RateWarnHelpModel>() {

                    @Override
                    public void onResponse(RateWarnHelpModel data) {
                        if (data.getCode() == 1) {
                            String hint = "体安客服将马上拨通你的绑定手机请保持电话" + data.getData().getPhone() + "畅通";
                            SpannableString spannableString = new SpannableString(hint);
                            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                            spannableString.setSpan(foregroundColorSpan, 20, 20 + data.getData().getPhone().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            new CommonKnowDialog(getContext()).setHint(spannableString).show();
                            dismiss();
                            appRateWarnCancel();
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }

    private void appRateWarnCancel() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString("token", ""));
        param.put("remark", mRemark);
        HttpUtils.invoke(mLifeSubscription, mResponseState,
                InsuranceApiFactory.getmHomeApi().appRateWarnCancel(param),
                new CallBack<ComModel>() {

                    @Override
                    public void onResponse(ComModel data) {
                    }
                });
    }


}
