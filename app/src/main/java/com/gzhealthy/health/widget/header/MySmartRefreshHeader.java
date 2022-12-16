package com.gzhealthy.health.widget.header;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.GlideUtils;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Justin_Liu
 * on 2021/8/31
 */
@SuppressLint("RestrictedApi")
public class MySmartRefreshHeader extends RelativeLayout implements RefreshHeader {

    int mBackgroundColor;

    RefreshKernel mRefreshKernel;

    TextView desc;
    View layout;
    ImageView image;

    final int mFinishDelay = 500;

    public MySmartRefreshHeader(Context context) {
        super(context);
        initView(context);
    }

    public MySmartRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context){
        layout = LayoutInflater.from(context).inflate(R.layout.layout_my_header,this);
        image = layout.findViewById(R.id.icon);
        desc = layout.findViewById(R.id.desc);
        GlideUtils.loadCustomeImgGif(image,R.drawable.icon_gif_loading);
        image.setVisibility(GONE);
        mBackgroundColor = ContextCompat.getColor(context,R.color.transparent);
//        setBackgroundColor(mBackgroundColor);
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundForHeader(mBackgroundColor);
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        return mFinishDelay;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                desc.setText("数据同步更新");
                image.setVisibility(GONE);
                break;
            case PullDownToRefresh:
                desc.setText("下拉同步更新");
                break;
            case Refreshing:
                desc.setText("数据同步更新中");
                image.setVisibility(VISIBLE);
                break;
            case RefreshReleased:
                break;
            case ReleaseToRefresh:
                desc.setText("松开同步数据更新");
                break;
            case ReleaseToTwoLevel:
                break;
            case Loading:
                break;
            case RefreshFinish:
                desc.setText("数据已同步更新");
                break;
        }
    }
}
