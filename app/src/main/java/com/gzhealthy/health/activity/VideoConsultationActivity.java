package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.activity.account.LoginPwsSettingActivity;
import com.gzhealthy.health.activity.account.ThirdLoginManageActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.WatchDeviceModel;
import com.gzhealthy.health.model.WechatApplet;
import com.gzhealthy.health.tool.CacheClearTools;
import com.gzhealthy.health.tool.CacheUtils;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.CheckNetwork;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WechatPlatform;
import com.gzhealthy.health.widget.CustomDialogBuilder;
import com.umeng.analytics.MobclickAgent;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import top.limuyang2.customldialog.IOSMsgDialog;
import top.limuyang2.customldialog.MaterialMsgDialog;
import top.limuyang2.ldialog.base.OnDialogDismissListener;

import static org.litepal.crud.DataSupport.findFirst;

/**
 * 视频问诊
 */
public class VideoConsultationActivity extends BaseAct {

    @BindView(R.id.connect)
    TextView tv_connect;


    @BindView(R.id.img)
    ImageView img;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_video_consultation;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("视频问诊");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(VideoConsultationActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        tv_connect.setOnClickListener(v -> {
            applet();
        });

//        Glide.with(this).load(R.drawable.consult_img).into(img);
        loadIntoUseFitWidth(this,R.drawable.consult_img,R.drawable.consult_img,img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-视频医生");
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-视频医生");
    }

    public void applet(){
        String token = SPCache.getString("token","");
        if (TextUtils.isEmpty(token)){
            LoginActivity.instance(this);
            return;
        }
        Map<String,String> params = new HashMap<>();
        params.put("token",token);
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
                            WechatPlatform.weChatMiniProgram(VideoConsultationActivity.this,data.data.appletId,"");
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
    public static void loadIntoUseFitWidth(Context context, final int resId, int errorImageId, final ImageView imageView) {
        Glide.with(context)
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

//                    @Override
//                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        return false;
//                    }

//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        if (imageView == null) {
//                            return false;
//                        }
//                        if (imageView.getScaleType() != ImageView.ScaleType.FIT_XY) {
//                            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                        }
//                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
//                        int vw = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
//                        float scale = (float) vw / (float) resource.getIntrinsicWidth();
//                        int vh = Math.round(resource.getIntrinsicHeight() * scale);
//                        params.height = vh + imageView.getPaddingTop() + imageView.getPaddingBottom();
//                        imageView.setLayoutParams(params);
//                        return false;
//                    }
                })
                .placeholder(errorImageId)
                .error(errorImageId)
                .into(imageView);
    }


    public static void instance(Context context) {
        Intent intent = new Intent(context, VideoConsultationActivity.class);
        context.startActivity(intent);
    }

}
