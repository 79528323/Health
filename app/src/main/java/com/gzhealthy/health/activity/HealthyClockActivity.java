package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.WechatApplet;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.HealthClockPopupWindow;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 健康时钟（十二时辰）
 */
public class HealthyClockActivity extends BaseAct {

//    @BindView(R.id.pop_window)
//    TextView tv_pop_window;

    @BindView(R.id.img)
    ImageView img;

    @BindView(R.id.img_container)
    LinearLayout img_container;

    int type;

    HealthClockPopupWindow popupWindow;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_healthy_clock;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("健康时钟");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(HealthyClockActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);


        type = getIntent().getIntExtra("type",1);
        popupWindow= new HealthClockPopupWindow(this,type);
        popupWindow.setOnSelectClockListener(type -> {
            this.type = type;
            switchImg();
            popupWindow.dismiss();
        });

        setClockOnClick(v -> {
            int[] location = new int[2];
            v.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            int windowWidth = ScreenUtils.getScreenWidth()/3 - 20;
            popupWindow.showAsDropDown(v,
                    (x - windowWidth/2),-y+DispUtil.dp2px(this,10));
        });

//        tv_pop_window.setOnClickListener(v -> {
//
//        });

        switchImg();
    }



    public void switchImg(){
        switch (type){
            case 1:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_1,R.drawable.img_sleep_time_1,img);
                break;
            case 2:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_2,R.drawable.img_sleep_time_2,img);
                break;
            case 3:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_3,R.drawable.img_sleep_time_3,img);
                break;
            case 4:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_4,R.drawable.img_sleep_time_4,img);
                break;
            case 5:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_5,R.drawable.img_sleep_time_5,img);
                break;
            case 6:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_6,R.drawable.img_sleep_time_6,img);
                break;
            case 7:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_7,R.drawable.img_sleep_time_7,img);
                break;
            case 8:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_8,R.drawable.img_sleep_time_8,img);
                break;
            case 9:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_9,R.drawable.img_sleep_time_9,img);
                break;
            case 10:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_10,R.drawable.img_sleep_time_10,img);
                break;
            case 11:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_11,R.drawable.img_sleep_time_11,img);
                break;
            case 12:
                loadIntoUseFitWidth(this,R.drawable.img_sleep_time_12,R.drawable.img_sleep_time_12,img);
                break;
        }
    }

    public void getClock(){
        Map<String,String> params = new HashMap<>();
        params.put("token",SPCache.getString("token",""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().intoApplet(params), new CallBack<WechatApplet>() {
                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onResponse(WechatApplet data) {
                        hideWaitDialog();
                        if (data.code == 1){
                            WechatPlatform.weChatMiniProgram(HealthyClockActivity.this,data.data.appletId,"");
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }
                });
    }

    /**
     * 自适应宽度加载图片。保持图片的长宽比例不变，通过修改imageView的高度来完全显示图片。
     */
    public void loadIntoUseFitWidth(Context context, final int resId, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .listener(new RequestListener<Integer, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Integer model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Integer model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        if (imageView == null) {
                            return false;
                        }
                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
                        imageView.setLayoutParams(params);
                        return false;
                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }


    public static void instance(Context context,int type) {
        Intent intent = new Intent(context, HealthyClockActivity.class);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

}
