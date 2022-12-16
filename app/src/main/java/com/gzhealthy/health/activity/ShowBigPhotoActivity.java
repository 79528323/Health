package com.gzhealthy.health.activity;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.clj.fastble.utils.HexUtil;
import com.github.chrisbanes.photoview.PhotoView;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BlueToothListAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.List;
import java.util.UUID;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 显示大图
 */
public class ShowBigPhotoActivity extends BaseAct {

    @BindView(R.id.my_photo_view)
    PhotoView mPhotoView;

//    PhotoViewAttacher mAttacher;

    String imageRes = "";

    @Override
    protected int getContentLayout() {
        return R.layout.activity_show_big_photo;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mImmersionBar.statusBarColor(R.color.black).barAlpha(1f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            imageRes = bundle.getString(IntentParam.BIG_PHOTO,"");
            Glide.with(this).load(imageRes).into(mPhotoView);
        }
    }

    public static void instance(Context context,Bundle bundle) {
        Intent intent = new Intent(context, ShowBigPhotoActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
