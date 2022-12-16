package com.gzhealthy.health.widget;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gzhealthy.health.R;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.tool.TDevice;
import com.gzhealthy.health.utils.WechatPlatform;

import static com.gzhealthy.health.base.Constants.SHAREIMG;

public class ShareDialog extends Dialog implements View.OnClickListener {
    Activity context;
    private String titles = "";
    private String url = "";
    String description = "";

    public ShareDialog(@NonNull Activity context) {
        super(context, R.style.BottomDialogs);
        this.context = context;
    }

    public void setDatas(String titles, String url, String Description) {
        this.titles = titles;
        this.url = url;
        this.description = Description;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_ll);
        getWindow().setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = TDevice.getScreenWidth(context);
        ImageView cancle = (ImageView) findViewById(R.id.tv_cancle);
        TextView tv_weixin = (TextView) findViewById(R.id.tv_weixin);
        TextView tv_friends = (TextView) findViewById(R.id.tv_friends);
//        TextView tv_QQ = (TextView) findViewById(R.id.tv_QQ);
//        TextView tv_xinrang = (TextView) findViewById(R.id.tv_xinrang);
        tv_weixin.setOnClickListener(this);
        tv_friends.setOnClickListener(this);
//        tv_QQ.setOnClickListener(this);
//        tv_xinrang.setOnClickListener(this);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog.this.dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_weixin:
                WechatPlatform.shareSessionURL(context,url,titles,description
                        , BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_logo));
                break;

            case R.id.tv_friends:
                WechatPlatform.shareMomentsURL(context,url,titles,description
                        ,BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_logo));
                break;
        }
        dismiss();
    }
//
//    private UMShareListener umShareListener = new UMShareListener() {
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//        }
//
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//            if (platform.name().equals("WEIXIN_FAVORITE")) {
//            } else {
//                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
//                        && platform != SHARE_MEDIA.EMAIL
//                        && platform != SHARE_MEDIA.FLICKR
//                        && platform != SHARE_MEDIA.FOURSQUARE
//                        && platform != SHARE_MEDIA.TUMBLR
//                        && platform != SHARE_MEDIA.POCKET
//                        && platform != SHARE_MEDIA.PINTEREST
//                        && platform != SHARE_MEDIA.INSTAGRAM
//                        && platform != SHARE_MEDIA.GOOGLEPLUS
//                        && platform != SHARE_MEDIA.YNOTE
//                        && platform != SHARE_MEDIA.EVERNOTE) {
//                }
//            }
//        }
//
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable throwable) {
////            hideWaitDialog();
//            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
//                    && platform != SHARE_MEDIA.EMAIL
//                    && platform != SHARE_MEDIA.FLICKR
//                    && platform != SHARE_MEDIA.FOURSQUARE
//                    && platform != SHARE_MEDIA.TUMBLR
//                    && platform != SHARE_MEDIA.POCKET
//                    && platform != SHARE_MEDIA.PINTEREST
//                    && platform != SHARE_MEDIA.INSTAGRAM
//                    && platform != SHARE_MEDIA.GOOGLEPLUS
//                    && platform != SHARE_MEDIA.YNOTE
//                    && platform != SHARE_MEDIA.EVERNOTE) {
////                Toast.makeText(context, platform + " 分享失败啦"+throwable.toString(), Toast.LENGTH_SHORT).show();
//                if (throwable != null) {
//                    Logger.d("throw", "throw:" + throwable.getMessage());
//                }
//            }
//        }
//
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//        }
//    };
}
