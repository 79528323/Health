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

import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.RateWarnHelpModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.push.MyJPushMessageReceiver;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import rx.functions.Action1;

/**
 * 心率异常提醒弹窗
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class FamilyDeleteDialog extends Dialog implements View.OnClickListener {
    private LifeSubscription mLifeSubscription;
    private ResponseState mResponseState;
    private OnDeleteMemberCallBack onDeleteMemberCallBack;
    private int id;

    public FamilyDeleteDialog(@NonNull Context context, LifeSubscription lifeSubscription, ResponseState responseState) {
        super(context);
        mLifeSubscription = lifeSubscription;
        mResponseState = responseState;
    }

    public FamilyDeleteDialog setMemberId(int id) {
        this.id = id;
        return this;
    }

    public void setOnDeleteMemberCallBack(OnDeleteMemberCallBack onDeleteMemberCallBack) {
        this.onDeleteMemberCallBack = onDeleteMemberCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_family_delete);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.TOP);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().y = ScreenUtils.getScreenHeight() / 4;

        findViewById(R.id.delete).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                delete();
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    private void delete() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        param.put("id", String.valueOf(id));
        HttpUtils.invoke(mLifeSubscription, mResponseState,
                InsuranceApiFactory.getmHomeApi().deleteMember(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
                            dismiss();
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                        if (onDeleteMemberCallBack!=null)
                            onDeleteMemberCallBack.onDelete();
                    }
                });
    }


    public interface OnDeleteMemberCallBack{
        void onDelete();
    }
}
