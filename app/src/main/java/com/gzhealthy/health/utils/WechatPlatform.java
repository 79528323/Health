package com.gzhealthy.health.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import androidx.core.content.ContextCompat;


public class WechatPlatform {
    private IWXAPI api;


    private void register(Context context, String appId) {
        api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
    }

    private boolean isWXAppInstalled(Context context, String appId){
        api = WXAPIFactory.createWXAPI(context, appId);
        return api.isWXAppInstalled();
    }

    public static boolean isWXAppInstalled(Context context){
        WechatPlatform wechatPlatform = new WechatPlatform();
        return wechatPlatform.isWXAppInstalled(context, Constants.UMeng.WECHAT_APP_ID);
    }


//    /**
//     * 微信支付
//     * @param context
//     * @param paramsBean
//     */
//    public static void PayForWeChat(Context context,WxPays.ParamsBean paramsBean){
//        if (cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform.isWXAppInstalled(context)){
//            cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform wechatPlatform=new cn.mofufin.morf.ui.util.thirdPlatform.WechatPlatform();
//            wechatPlatform.pay(context,paramsBean);
//        }else {
//            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
//        }
//    }


    /**
     * 微信授权登录
     * @param context
     */
    public static void weChatAuth(Context context){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.onAuth(context,Constants.UMeng.WECHAT_APP_ID);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 跳转微信小程序
     * @param context
     * @param uid
     * @param path
     */
    public static void weChatMiniProgram(Context context,String uid,String path){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.onMiniProgram(context,Constants.UMeng.WECHAT_APP_ID,uid,path);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 链接分享到朋友圈
     * @param context
     * @param url
     * @param title
     * @param description
     * @param thumbBitmap
     */
    public static void shareMomentsURL(Context context, String url, String title, String description, Bitmap thumbBitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareMomentsURL(context, Constants.UMeng.WECHAT_APP_ID, url, title, description, thumbBitmap);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 链接分享到会话
     * @param context
     * @param url
     * @param title
     * @param description
     * @param thumbBitmap
     */
    public static void shareSessionURL(Context context, String url, String title, String description, Bitmap thumbBitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareSessionURL(context, Constants.UMeng.WECHAT_APP_ID, url, title, description, thumbBitmap);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 图片分享到微信会话
     * @param context
     * @param description
     * @param bitmap
     */
    public static void shareImageSession(Context context,String description,Bitmap bitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareImageSession(context,Constants.UMeng.WECHAT_APP_ID,description,bitmap);
        }else{
            Toast.makeText(context,R.string.wechat_not_installed,Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 图片分享到微信朋友圈
     * @param context
     * @param description
     * @param bitmap
     */
    public static void shareImageMoments(Context context,String description,Bitmap bitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareImageMoments(context,Constants.UMeng.WECHAT_APP_ID,description,bitmap);
        }else{
            Toast.makeText(context,R.string.wechat_not_installed,Toast.LENGTH_LONG).show();
        }

    }

    private  void shareImageCustomSession(Context context,String appid,String description,Bitmap bitmap,int w,int h){
        shardImage(context,appid,description,bitmap, SendMessageToWX.Req.WXSceneSession,w,h);
    }

    private  void shareImageSession(Context context,String appid,String description,Bitmap bitmap){
        shardImage(context,appid,description,bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    private  void shareImageMoments(Context context,String appid,String description,Bitmap bitmap){
        shardImage(context,appid,description,bitmap, SendMessageToWX.Req.WXSceneTimeline);
    }

    private void shareMomentsURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap){
        shareURL(context, appId, url, title, description, thumbBitmap, SendMessageToWX.Req.WXSceneTimeline);
    }

    private void shareSessionURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap){
        shareURL(context, appId, url, title, description, thumbBitmap, SendMessageToWX.Req.WXSceneSession);
    }

    private void shareURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap, int scene){
//        Logger.i(TAG, "shareURL url=" + url + ",title=" + title);
        register(context, appId);

        WXWebpageObject wxWebpageObject=new WXWebpageObject();
        wxWebpageObject.webpageUrl=url;
        //wxWebpageObject.extInfo="ext";

        WXMediaMessage msg=new WXMediaMessage(wxWebpageObject);
        if(TextUtils.isEmpty(title)) {
            msg.title = " ";
        }else{
            msg.title = title;
        }
        msg.description=description;
        msg.setThumbImage(thumbBitmap);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=String.valueOf(System.currentTimeMillis());
        Log.i("WXD","String.valueOf==>"+System.currentTimeMillis());
        req.message=msg;
        req.scene = scene;

        api.sendReq(req);
    }

    private void shardImage(Context context,String appId,String description,Bitmap bitmap,int scene){
        register(context, appId);

        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,200,200,true);
        bitmap.recycle();

//        msg.thumbData =
        msg.description=description;
        msg.setThumbImage(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }

    private void shardImage(Context context,String appId,String description,Bitmap bitmap,int scene,int w,int h){
        register(context, appId);

        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,w,h,true);
        bitmap.recycle();

//        msg.thumbData =
        msg.description=description;
        msg.setThumbImage(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }


    private void onMiniProgram(Context context,String appId,String userName,String path){
        register(context, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = userName; // 填小程序原始id
        req.path = path;                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
    }

//    private void pay(Context context,WxPays.ParamsBean params){
//        register(context, Common.WECHAT_APP_ID);
//
//        PayReq payReq = new PayReq();
//        payReq.appId = params.appid;
//        payReq.nonceStr = params.noncestr;
//        payReq.partnerId = params.partnerid;
//        payReq.prepayId = params.prepayid;
//        String value = params.packageX;
//        Logger.e("value "+value);
//        payReq.packageValue = value;
//        payReq.timeStamp = params.timestamp;
//        payReq.sign = params.sign;
//
//        api.sendReq(payReq);
//    }


    private void onAuth(Context context,String appId){
        register(context, appId);
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = context.getPackageName();
        api.sendReq(req);
    }
}
