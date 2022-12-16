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
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.MyFamilyPersonDetailActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.model.FamilyQRCode;
import com.gzhealthy.health.model.RateWarnHelpModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.push.MyJPushMessageReceiver;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import rx.functions.Action1;

/**
 * 被邀请列表弹窗
 * →_→
 */
public class MyInviteQRDialog extends Dialog implements View.OnClickListener {
    Context context;

    ImageView userIcon;
    ImageView close;
    ImageView img_qr;
    TextView name;
    TextView uid;

    RxManager mRxManager;
    LifeSubscription mLifeSubscription;
    ResponseState mResponseState;

    public MyInviteQRDialog(@NonNull Context context,RxManager rxManager
            , LifeSubscription lifeSubscription, ResponseState responseState) {
        super(context);
        this.context = context;
        this.mRxManager = rxManager;
        this.mLifeSubscription = lifeSubscription;
        this.mResponseState = responseState;
    }

//    public MyInviteQRDialog setRemark(String remark) {
//        return this;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_invite_qr);

        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.TOP);
        getWindow().getAttributes().width = ScreenUtils.getScreenWidth()/8 * 7;
        getWindow().getAttributes().y = ScreenUtils.getScreenHeight() / 4;

        userIcon = findViewById(R.id.user_icon);
        userIcon.setOnClickListener(this);
        close = findViewById(R.id.close);
        close.setOnClickListener(this);
        name = findViewById(R.id.name);
        name.setOnClickListener(this);
        uid = findViewById(R.id.uid);
        uid.setOnClickListener(this);
        img_qr = findViewById(R.id.img_qr);

        getQRCodeImg();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
        }
    }


    public void getQRCodeImg(){
        Map<String,String> param = new HashMap<>();
        HttpUtils.invoke(mLifeSubscription, mResponseState, InsuranceApiFactory.getmHomeApi().getQcode(param),
                new CallBack<FamilyQRCode>() {

                    @Override
                    public void onResponse(FamilyQRCode data) {
                        if (data.code == 1){
                            uid.setText("ID："+data.data.uid);
                            name.setText(data.data.nickName);
                            GlideUtils.CircleImage(context,data.data.avatar,userIcon);

                            String pathQR = Constants.Api.BASE_URL1 + data.data.qrCodePath;
                            GlideUtils.loadCustomeImgNoCache(img_qr,pathQR);
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }

}
