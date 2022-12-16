package com.gzhealthy.health.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.CustomerServiceModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 客服
 */
public class CustomerServiceActivity extends BaseAct {
    @BindView(R.id.tvPhone)
    public TextView tvPhone;
    @BindView(R.id.tvTime)
    public TextView tvTime;
    @BindView(R.id.tvWeiXin)
    public TextView tvWeiXin;
    @BindView(R.id.tvCopy)
    public TextView tvCopy;
    @BindView(R.id.ivCode)
    public ImageView ivCode;
    @BindView(R.id.btCall)
    public Button btCall;

    private String phone = "";
    private String weiXin = "";

    public static void instance(Context context) {
        Intent intent = new Intent(context, CustomerServiceActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_customer_service;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("联系客服");
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        tvCopy.setOnClickListener(this);
        btCall.setOnClickListener(this);

        getData();
    }

    private void getData() {
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().customerService(),
                new CallBack<CustomerServiceModel>() {

                    @Override
                    public void onResponse(CustomerServiceModel data) {
                        if (data.getCode() == 1) {
                            phone = data.getData().getPhone();
                            weiXin = data.getData().getWeChat();
                            tvPhone.setText("客服热线：" + phone);
                            tvTime.setText("（工作时间：" + data.getData().getWorkTime() + "）");
                            tvWeiXin.setText("微信号：" + weiXin);
                            btCall.setText("拨号" + phone);

                            Glide.with(CustomerServiceActivity.this)
                                    .load(data.getData().getQrCodePath())
                                    .asBitmap()
                                    .skipMemoryCache(true)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(new ImageViewTarget<Bitmap>(ivCode) {

                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            ivCode.setImageBitmap(resource);
                                            ivCode.setOnLongClickListener(v -> {
                                                saveQrCode(resource);
                                                return true;
                                            });
                                        }
                                    });
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }

    private void saveQrCode(Bitmap resource) {
        if (resource == null) {
            return;
        }
        new Thread(() -> {
            try {
                ImageUtils.save2Album(resource, Bitmap.CompressFormat.JPEG);
                ToastUtils.showLong("二维码已保存");
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showLong("二维码保存失败，请手动截图");
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tvCopy://复制
                try {
                    ClipboardUtils.copyText(weiXin);
                    ToastUtil.showToast("已复制");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btCall:
                PermissionUtils.permissionGroup(PermissionConstants.STORAGE, PermissionConstants.PHONE)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + phone));
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                } else {
                                    ToastUtil.showToast("没有找到相关应用");
                                }
                            }

                            @Override
                            public void onDenied() {

                            }
                        }).request();
                break;
        }
    }


}
