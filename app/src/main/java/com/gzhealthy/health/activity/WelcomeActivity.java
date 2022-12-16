package com.gzhealthy.health.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.WelcomeBannerAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.HealthCard;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.LauchIndicator;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.indicator.RectangleIndicator;
import com.youth.banner.indicator.RoundLinesIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.listener.OnPageChangeListener;
import com.youth.banner.util.BannerUtils;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionResultCallBack;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.helper.Logger;


public class WelcomeActivity extends BaseAct {
    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.btn_banner_into)
    TextView btn_banner_into;

    private static final String TAG = "WelcomeActivity";
    private boolean isFirstOpen;

    @Override
    protected int getView() {
        return super.getView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_welcome;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        initView();
        initOps();
    }



    public void initView(){
        //适配刘海屏顶部
        Window window = getWindow();
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        window.setAttributes(lp);
        mImmersionBar.transparentNavigationBar().init();
        hideOrShowAppbar(true);
        setSwipeBackEnable(false);
    }


    protected void initOps() {
        //预设首页卡片默认类
        List<Integer> list = HealthCardManagerActivity.getSPList();
        if (list==null){
            StringBuffer buffer = new StringBuffer();
            for (int k=1; k < 3; k++){
                buffer.append(k).append(",");
            }

            SPCache.putString(SPCache.KEY_HEALTH_CARD_LIST,buffer.toString());
        }

        isFirstOpen = SPCache.getBoolean(Constants.FIRST_OPEN, true);
        findViewById(R.id.rl_welocom).postDelayed(() -> {
            if (isFirstOpen) {
                //TODO 安装后首次进入
                findViewById(R.id.rl_welocom).setVisibility(View.GONE);
                initBanner();
            } else {
                startActivity();
            }
        }, 2000);
    }



    public void initBanner(){
        List<Integer> list = new ArrayList<>();
        list.add(R.drawable.lauch_img_1);
        list.add(R.drawable.lauch_img_2);
        list.add(R.drawable.lauch_img_3);

        mBanner.isAutoLoop(false);
        mBanner.setIndicator(new LauchIndicator(this));
//        mBanner.setIndicatorSelectedWidth((int) BannerUtils.dp2px(22));
//        mBanner.setIndicatorSpace((int) BannerUtils.dp2px(4));
//        mBanner.setIndicatorRadius(8);

        IndicatorConfig config = mBanner.getIndicatorConfig();
//        config.setIndicatorSpace((int) BannerUtils.dp2px(10));
//        config.setNormalColor(getColor(R.color.colorPrimary));
//        config.setSelectedColor(Color.parseColor("#C1EAE9"));
//        config.setNormalWidth((int) BannerUtils.dp2px(10));
//        config.setSelectedWidth((int) BannerUtils.dp2px(10) * 2);
//        config.setHeight((int) BannerUtils.dp2px(10));
        config.getMargins().bottomMargin = DispUtil.dp2px(this,180);
        mBanner.setIndicatorMargins(config.getMargins());

        mBanner.setAdapter(new WelcomeBannerAdapter(this,list),false)
                .addBannerLifecycleObserver(this)
                .addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position+1==list.size()){
                    btn_banner_into.setVisibility(View.VISIBLE);
                    btn_banner_into.setOnClickListener(v -> {
                        startActivity();
                    });
                }else {
                    btn_banner_into.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    public void startActivity(){
        SPCache.putBoolean(Constants.FIRST_OPEN, false);
        Intent intent = null;
        int type = getIntent().getIntExtra(IntentParam.START_APP_JUMP_SOURCE,-1);
        Logger.e(TAG,"WelcomeActivity type="+type);
        intent = new Intent(WelcomeActivity.this, HomeActivity.class);
        intent.putExtra(IntentParam.START_APP_JUMP_SOURCE, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.screen_zoom_in, R.anim.screen_zoom_out);
        goBack();
    }
}

