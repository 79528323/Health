//package com.gzhealthy.health.activity;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import com.gzhealthy.health.R;
//import com.gzhealthy.health.base.BaseAct;
//import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
//import com.journeyapps.barcodescanner.CaptureManager;
//import com.journeyapps.barcodescanner.DecoratedBarcodeView;
//
//public class QRCaptureActivity extends BaseAct {
//    private DecoratedBarcodeView barcodeView;
//    private CaptureManager manager;
//
//    @Override
//    protected int getContentLayout() {
//        return R.layout.activity_qr_capture;
//    }
//
//    @Override
//    protected void init(Bundle savedInstanceState) {
//        ButterKnife.bind(this);
//        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("添加设备");
//        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
//        StatusBarUtil.StatusBarLightMode(this, true);
//        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
//        getCenterTextView().setTextSize(18);
//
//        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_view);
//        manager = new CaptureManager(this,barcodeView);
//        manager.initializeFromIntent(getIntent(),savedInstanceState);
//        manager.decode();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        manager.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        manager.onResume();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        manager.onDestroy();
//    }
//
//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        manager.onSaveInstanceState(outState);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        manager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//}