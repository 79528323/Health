package com.gzhealthy.health.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.EnterInvitePhoneModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.GlideEngine;
import com.gzhealthy.health.utils.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 设备绑定扫码
 */
public class OperationBindScanActivity extends BaseAct implements QRCodeView.Delegate {
    @BindView(R.id.zXingView)
    ZXingView zXingView;


    public static void startActivity(Context context) {
        PermissionUtils.permissionGroup(PermissionConstants.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        context.startActivity(new Intent(context, OperationBindScanActivity.class));
                    }

                    @Override
                    public void onDenied() {
                    }
                }).request();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_operation_bind_scan;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("添加设备");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        zXingView.setDelegate(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        zXingView.startCamera();
        zXingView.startSpotAndShowRect();
        zXingView.startSpot();
    }

    @Override
    protected void onStop() {
        super.onStop();
        zXingView.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        zXingView.onDestroy();
    }

    private void openAlbum() {
        PermissionUtils.permissionGroup(PermissionConstants.STORAGE)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        PictureSelector.create(OperationBindScanActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .isCompress(true)
                                .selectionMode(PictureConfig.SINGLE)
                                .isSingleDirectReturn(true)
                                .imageEngine(GlideEngine.createGlideEngine())
                                .forResult(new OnResultCallbackListener() {

                                    @Override
                                    public void onResult(List result) {
                                        String path = ((LocalMedia) result.get(0)).getCompressPath();
                                        zXingView.decodeQRCode(path);
                                    }

                                    @Override
                                    public void onCancel() {
                                    }
                                });
                    }

                    @Override
                    public void onDenied() {
                    }
                }).request();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result == null) {
            ToastUtils.showLong("识别图像失败，请重试");
            return;
        }
        VibrateUtils.vibrate(200);

        if (!TextUtils.isEmpty(result)){
//            Logger.e("111","IMEI="+result);
            if (result.startsWith("http")){
                result = result.substring(result.indexOf("=")+1);
            }

            WatchBindWaitingActivity.instance(OperationBindScanActivity.this,result);
            goBack();
        }

    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showLong("打开相机出错");
        finish();
    }
}
