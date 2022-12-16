//package com.gzhealthy.health.activity;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.ConsoleMessage;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebChromeClient;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//
//import androidx.annotation.RequiresApi;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.alibaba.sdk.android.oss.ClientConfiguration;
//import com.alibaba.sdk.android.oss.ClientException;
//import com.alibaba.sdk.android.oss.OSS;
//import com.alibaba.sdk.android.oss.OSSClient;
//import com.alibaba.sdk.android.oss.ServiceException;
//import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
//import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
//import com.alibaba.sdk.android.oss.common.OSSLog;
//import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
//import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
//import com.alibaba.sdk.android.oss.model.PutObjectRequest;
//import com.alibaba.sdk.android.oss.model.PutObjectResult;
//import com.gzhealthy.health.R;
//import com.gzhealthy.health.api.CallBack;
//import com.gzhealthy.health.api.InsuranceApiFactory;
//import com.gzhealthy.health.base.BaseAct;
//import com.gzhealthy.health.base.Constants;
//import com.gzhealthy.health.model.OssModel;
//import com.gzhealthy.health.model.UserInfo;
//import com.gzhealthy.health.logger.Logger;
//import com.gzhealthy.health.tool.BitmapUtil;
//import com.gzhealthy.health.tool.ChoosePhotoWaysUtils;
//import com.gzhealthy.health.tool.FileUtils;
//import com.gzhealthy.health.tool.HttpUtils;
//import com.gzhealthy.health.tool.KeyboardUtils;
//import com.gzhealthy.health.tool.Md5Utils;
//import com.gzhealthy.health.tool.SPCache;
//import com.gzhealthy.health.tool.ToastUtils;
//import com.gzhealthy.health.widget.Html5WebView;
//import com.yanzhenjie.album.Album;
//import com.yanzhenjie.durban.Durban;
//import com.yanzhenjie.mediascanner.MediaScanner;
//import com.yxp.permission.util.lib.PermissionUtil;
//import com.yxp.permission.util.lib.callback.PermissionResultCallBack;
//
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//import org.litepal.crud.DataSupport;
//
//import java.io.File;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import rx.functions.Func1;
//import top.zibin.luban.Luban;
//import top.zibin.luban.OnCompressListener;
//
//import static com.gzhealthy.health.base.Constants.Images.CAMERA_REQUEST_CODE;
//import static com.gzhealthy.health.base.Constants.Images.CROP_REQUEST_CODE;
//import static com.gzhealthy.health.base.Constants.Images.IMAGE_REQUEST_CODE;
//import static com.gzhealthy.health.base.Constants.NEWWEIPAYl;
//import static org.litepal.crud.DataSupport.findFirst;
//
//
//public class Html5NewShop extends BaseAct {
//
//    private static final String TAG = "Html5NewShop";
//    private final int REQUEST_CODE_CAMERA = 2;
//    private String mUrl, shareUrl;
//
//    private LinearLayout mLayout;
//    protected Html5WebView mWebView;
//    private ProgressBar pb;
//    protected String title, beanStr, beanStr2, idCode;
//    private Boolean isNeedRightIcon, isNeedSecondRightIcon, isHuize;
//    protected Bundle bundle;
//    private File cameraFile;
//    boolean firstVisitWXH5PayUrl = true;
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    protected void init(Bundle saveInstanceState) {
//        initView();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    public void initView() {
////        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
//        setstatueColor(R.color.white);
//        setBarLeftIcon(R.mipmap.login_back);
////        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
////        StatusBarUtil.StatusBarLightMode(Html5Activity.this, true);
//        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
//        getCenterTextView().setTextSize(18);
//        clearWebViewCache();
//        bundle = getIntent().getBundleExtra("bundle");
//        if (bundle != null) {
//            mUrl = bundle.getString("url");
//            Log.d(TAG, "initView: " + mUrl);
//        } else {
//            mUrl = "www.baidu.com";
//        }
//        title = bundle.getString("title", "");
//        beanStr = bundle.getString("beanStr", "");
//        beanStr2 = bundle.getString("beanStr2", "");
//        idCode = bundle.getString("idCode", "");
//        isNeedRightIcon = bundle.getBoolean("isNeedRightIcon");
//        isNeedSecondRightIcon = bundle.getBoolean("isNeedSecondRightIcon");
//        isHuize = bundle.getBoolean("isHuize");
//        Log.d(TAG, "initView: " + beanStr);
//        Log.d(TAG, "initView: " + beanStr2);
//        Log.d(TAG, "initView: " + idCode);
//        if (isNeedRightIcon) {
//            setBarRighticon(R.mipmap.ic_details_sharet_bg);
//            getIvRight().setOnClickListener(this);
//        }
//
//        if (isNeedSecondRightIcon) {
//            setBarLeftRighticon(R.mipmap.ic_hong_news_bg);
//        }
//
//        setTitle(title);
////        setHasBack();
//        mLayout = (LinearLayout) findViewById(R.id.web_layout);
//        pb = (ProgressBar) findViewById(R.id.service_pb);
//        pb.setMax(100);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
//                .MATCH_PARENT);
//        mWebView = new Html5WebView(getApplicationContext());
//        mWebView.setLayoutParams(params);
//
//        mWebView.setWebViewClient(webViewClient);
////        mWebView.setWebViewClient(new WebViewClient());
////        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setAllowFileAccess(true);
//        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
//        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
//        mWebView.getSettings().setDomStorageEnabled(true);
//
//        mWebView.getSettings().setDomStorageEnabled(true);//设置适应Html5 //重点是这个设置
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
//        mWebView.getSettings().setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
//        mWebView.getSettings().setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
//        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
//        mWebView.getSettings().setAllowFileAccessFromFileURLs(false);
//        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
//        mWebView.getSettings().setAllowUniversalAccessFromFileURLs(false);
//
//        String UserAgentString = mWebView.getSettings().getUserAgentString();
////        mWebView.getSettings().setUserAgentString(UserAgentString + "third_program_h5");
//        mWebView.getSettings().setUserAgentString(UserAgentString + "/cjApp");
//
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Log.e(TAG, "msg : " + consoleMessage.message());
//                return super.onConsoleMessage(consoleMessage);
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                setTitle(title);
////                ToastUtil.showToast("拦截标题：" + title);
////                L.i("拦截的URL：" + view.getUrl());
//            }
//        });
//
//        WebViewClient webViewClient = new WebViewClient() {
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
////                ToastUtil.showToastLong("URL：" + url);
////                L.i("WebViewClient URL:" + url);
//
//                if (url.startsWith("weixin://")) {
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                        return true;
//                    } catch (Exception e) {
//                        // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
//                        ToastUtils.showShort("该手机没有安装微信");
//                        return true;
//                    }
//                } else if (url.startsWith("alipays://") || url.startsWith("alipay")) {
//                    try {
//                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                        return true;
//                    } catch (Exception e) {
//                        // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
//                        // 启动支付宝 App 失败，会自行跳转支付宝网页支付
//                        return true;
//                    }
//                }
//
//                // 处理普通 http 请求跳转
//                if (!(url.startsWith("http") || url.startsWith("https"))) {
//                    return true;
//                }
//
//                // 处理微信 H5 支付跳转时验证请求头 referer 失效
//                // 验证不通过会出现“商家参数格式有误，请联系商家解决”
//                if (url.contains("wx.tenpay.com")) {
//
//                    // 申请微信 H5 支付时填写的域名
//                    // 比如经常用来测试网络连通性的 http://www.baidu.com
//                    String referer = NEWWEIPAYl;
//
//                    // 兼容 Android 4.4.3 和 4.4.4 两个系统版本设置 referer 无效的问题
//                    if (("4.4.3".equals(Build.VERSION.RELEASE))
//                            || ("4.4.4".equals(Build.VERSION.RELEASE))) {
//                        if (firstVisitWXH5PayUrl) {
//                            view.loadDataWithBaseURL(referer, "<script>window.location.href=\"" + url + "\";</script>",
//                                    "text/html", "utf-8", null);
//                            // 修改标记位状态，避免循环调用
//                            // 再次进入微信H5支付流程时记得重置状态 firstVisitWXH5PayUrl = true
//                            firstVisitWXH5PayUrl = false;
//                        }
//                        // 返回 false 由系统 WebView 自己处理该 url
//                        return false;
//                    } else {
//                        // HashMap 指定容量初始化，避免不必要的内存消耗
//                        HashMap<String, String> map = new HashMap<>(1);
//                        map.put("Referer", referer);
//                        Log.e("qqq", url + "------" + referer);
////                        ToastUtil.showToastLong("微信支付：" + url);
////                        L.i("微信支付URL:" + url);
//                        if (url.startsWith("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/app.tofans.com")) {
//                            return true;
//                        }
//                        view.loadUrl(url, map);
//                        return true;
//                    }
//                }
//                return false;
//            }
//        };
//        mWebView.setWebViewClient(webViewClient);
//        mLayout.addView(mWebView);
//        mWebView.addJavascriptInterface(this, "nbApp");//代理名
//        shareUrl = mUrl;
//        if (mUrl.startsWith("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/app.tofans.com")) {
//            return;
//        }
//        mWebView.loadUrl(mUrl + appendLoginNameToken());
//        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                WebView.HitTestResult result = ((WebView) view).getHitTestResult();
//                int type = result.getType();
////                saveBitmap(base64ToBitmap(result.getExtra()));
//                try {
////                    String imgdate=   result.getExtra().replace("data:image/png;base64,","");
//                    if (type == 5) {
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmSS");
//                        Boolean isSaveSuc = BitmapUtil.GenerateImage(result.getExtra(), Environment.getExternalStorageDirectory() + "/" + simpleDateFormat.format(new Date()) + ".jpg", Html5NewShop.this);
//                        if (isSaveSuc) {
//                            ToastUtils.showShort("保存成功");
//                        } else {
//                            ToastUtils.showShort("保存失败");
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        });
//        String url = mUrl + appendLoginNameToken();
//        Log.d(TAG, "initView: " + mUrl + appendLoginNameToken());
//    }
//
//
//    /***
//     * 默认在登录状态下有调用这个activity 都拼接token loginName 避免分享带出信息
//     * @return
//     */
//    private String appendLoginNameToken() {
//        String tempStr = "";
//        String token = SPCache.getString("token", "");
//        String loginName = "";
//        if (TextUtils.isEmpty(token)) {
//            return "";
//        }
//        if (null != findFirst(UserInfo.DataBean.class)) {
//            loginName = DataSupport.findFirst(UserInfo.DataBean.class).getLoginName();
//        }
//        if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(loginName)) {
//            // 简单拼接
//            if (mUrl.contains("?")) {
//                tempStr = tempStr + "&token=" + token;
//            } else {
//                tempStr = tempStr + "?token=" + token;
//            }
//        }
//
//        return tempStr;
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
//    public void onEventMainThread(String object) {
//        if (object != null) {
//            Log.d(TAG, "onEventMainThread: " + object);
//            String test = "test";
//            mWebView.loadUrl("javascript:callRefresh ('" + test + "')");
//        }
//    }
//
//    @JavascriptInterface
//    public void appLinkNative(String str, String json) {
//        Log.e(TAG, "appLinkNative: " + str);
//        Log.e(TAG, "appLinkNative: jons->" + json);
//    }
//
//    private void showPhoto() {
//        ChoosePhotoWaysUtils choosePhotoWaysUtils = new ChoosePhotoWaysUtils(this, new ChoosePhotoWaysUtils.DialogOnItemClickListener() {
//            @Override
//            public void onItemClickListener(View v, int position) {
//                if (position == 1) {//拍照
//                    Album.camera(Html5NewShop.this).start(CAMERA_REQUEST_CODE);
//                } else {//相册
//                    Album.albumRadio(Html5NewShop.this)
//                            .toolBarColor(ContextCompat.getColor(Html5NewShop.this, R.color.colorPrimary)) // Toolbar color.
//                            .statusBarColor(ContextCompat.getColor(Html5NewShop.this, R.color.colorPrimaryDark)) // StatusBar color.
//                            .navigationBarColor(ActivityCompat.getColor(Html5NewShop.this, R.color.black)) // NavigationBar color.
//                            .columnCount(2) // span count.
//                            .camera(true) // has fromCamera function.
//                            .start(IMAGE_REQUEST_CODE);
//                }
//            }
//        });
//        choosePhotoWaysUtils.showAtLocation(mWebView, Gravity.BOTTOM, 0, 0);
//    }
//
//    private class TitleModel {
//        private String title;
//    }
//
////    //使用Webview的时候，返回键没有重写的时候会直接关闭程序，这时候其实我们要其执行的知识回退到上一步的操作
////    @Override
////    public boolean onKeyDown(int keyCode, KeyEvent event) {
////        //这是一个监听用的按键的方法，keyCode 监听用户的动作，如果是按了返回键，同时Webview要返回的话，WebView执行回退操作，因为mWebView.canGoBack()返回的是一个Boolean类型，所以我们把它返回为true
////        if(keyCode==KeyEvent.KEYCODE_BACK&&mWebView.canGoBack()){
////            mWebView.goBack();
////            return true;
////        }
////
////        return super.onKeyDown(keyCode, event);
////    }
//
//    @Override
//    protected int getContentLayout() {
//        return R.layout.activity_html5;
//    }
//
//    WebViewClient webViewClient = new WebViewClient() {
//        /**
//         * 多页面在同一个WebView中打开，就是不新建activity或者调用系统浏览器打开
//         */
////        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            //https://payment.huize.com/v2?trade_no=H06118032686601723
//            Log.d(TAG, "shouldOverrideUrlLoading: " + url);
//            if (url.startsWith("http:") || url.startsWith("https:")) {
//                mWebView.loadUrl(url);
//                return false;
//            } else {
//                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                } catch (Exception e) {
//                    return false;
//                }
//
//            }
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            shareUrl = url;
//            if (mWebView != null)
//                mWebView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 注意调用的JS方法名要对应上
//                        // 调用javascript的callJS()方法
//                        String token = SPCache.getString("token", "");
//                        String loginName = "";
//                        if (null != findFirst(UserInfo.DataBean.class)) {
//                            loginName = DataSupport.findFirst(UserInfo.DataBean.class).getLoginName();
//                        }
//
//                        mWebView.loadUrl("javascript:callMethod('" + token + "', '" + loginName + "')");
//
//                    }
//                });
//        }
//    };
//
//
//    protected long mOldTime;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
////            L.i("当前URL：" + mWebView.getUrl());
//            if (isHuize) {
//                Html5NewShop.this.finish();
//            } else {
//                if (System.currentTimeMillis() - mOldTime < 500) {
//                    mWebView.clearHistory();
//                    mWebView.loadUrl(mUrl);
//                } else if (mWebView.canGoBack()) {
//                    mWebView.goBack();
//                } else {
//                    Html5NewShop.this.finish();
//                }
//            }
//
//            mOldTime = System.currentTimeMillis();
//            return true;
//        }
//
//        return super.onKeyDown(keyCode, event);
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        if (mWebView != null) {
//            //清空所有Cookie
//            CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
//            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
//            cookieManager.removeAllCookie();// Removes all cookies.
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                cookieManager.flush();
//            } else {
//                CookieSyncManager.createInstance(this);
//                CookieSyncManager.getInstance().sync();
//            }
//
//            mWebView.setWebChromeClient(null);
//            mWebView.setWebViewClient(null);
//            mWebView.removeAllViews();
//            mWebView.getSettings().setJavaScriptEnabled(false);
//            mWebView.clearCache(true);
//            mWebView.clearFormData();
//
//            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
//            mWebView.clearHistory();
//            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
//            mWebView.destroy();
//            mWebView = null;
//        }
//
//        super.onDestroy();
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.iv_custom_toolbar_left || view.getId() == R.id.tv_custom_toolbar_left) {
//            if (isHuize) {
//                Html5NewShop.this.finish();
//                return;
//            }
//            if (System.currentTimeMillis() - mOldTime < 500) {
//                mWebView.clearHistory();
//                mWebView.loadUrl(mUrl);
//            } else if (mWebView.canGoBack()) {
//                mWebView.goBack();
//            } else {
//                Html5NewShop.this.finish();
//            }
//            mOldTime = System.currentTimeMillis();
//        } else if (view.getId() == R.id.iv_custom_toolbar_right) {
//            getPermission();
//        } else if (view.getId() == R.id.iv_custom_toolbar_left_right) {
////            showActivity(Html5NewShop.this, MessageListActivity.class);
//        }
////        super.onClick(v);
////        if(v.getId() == R.id.iv_custom_toolbar_right){
////            ToastUtils.showShort("share");
////            // 必须另开线程进行JS方法调用(否则无法调用)
////
////        }
//    }
//
//    public static void newIntent(Context mContext, String title, String url, Boolean isNeedRightIcon) {
//        Intent intent = new Intent(mContext, Html5NewShop.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        bundle.putString("title", title);
//        bundle.putBoolean("isNeedRightIcon", isNeedRightIcon);
//        intent.putExtra("bundle", bundle);
//        mContext.startActivity(intent);
//    }
//
//    public static void newIntent(Context mContext, String title, String url, Boolean isNeedRightIcon, boolean isNeedSecondRightIcon) {
//        Intent intent = new Intent(mContext, Html5NewShop.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        bundle.putString("title", title);
//        bundle.putBoolean("isNeedRightIcon", isNeedRightIcon);
//        bundle.putBoolean("isNeedSecondRightIcon", isNeedSecondRightIcon);
//        intent.putExtra("bundle", bundle);
//        mContext.startActivity(intent);
//    }
//
//
//    public static void newIntent(Context mContext, String title, String url, Boolean isNeedRightIcon, String beanStr) {
//        Intent intent = new Intent(mContext, Html5NewShop.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        bundle.putString("title", title);
//        bundle.putBoolean("isNeedRightIcon", isNeedRightIcon);
//        bundle.putString("beanStr", beanStr);
//        intent.putExtra("bundle", bundle);
//        mContext.startActivity(intent);
//    }
//
//    public static void newIntent(Context mContext, String title, String url, Boolean isNeedRightIcon, String beanStr, String beanStr2, String idCode, Boolean isHuize) {
//        Intent intent = new Intent(mContext, Html5NewShop.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        bundle.putString("title", title);
//        bundle.putBoolean("isNeedRightIcon", isNeedRightIcon);
//        bundle.putBoolean("isHuize", isHuize);
//        bundle.putString("beanStr", beanStr);
//        bundle.putString("beanStr2", beanStr2);
//        bundle.putString("idCode", idCode);
//        intent.putExtra("bundle", bundle);
//        mContext.startActivity(intent);
//    }
//
//    //------------------------------------分享相关开始----------------------------------------
//
//    private void getPermission() {
//        String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
//        PermissionUtil.getInstance().request(mPermissionList, new PermissionResultCallBack() {
//            /**
//             * 当所有权限的申请被用户同意之后,该方法会被调用
//             */
//            @Override
//            public void onPermissionGranted() {
//                share();
//            }
//
//            /**
//             * 返回此次申请中通过的权限列表
//             */
//            @Override
//            public void onPermissionGranted(String... strings) {
//                Log.d(TAG, "onPermissionGranted: " + strings);
//            }
//
//            /**
//             * 当权限申请中的某一个或多个权限,在此次申请中被用户否定了,并勾选了不再提醒选项时（权限的申请窗口不能再弹出，
//             * 没有办法再次申请）,该方法将会被调用。该方法调用时机在onRationalShow之前.onDenied和onRationalShow
//             * 有可能都会被触发.
//             */
//            @Override
//            public void onPermissionDenied(String... strings) {
//                Log.d(TAG, "onPermissionDenied: " + strings);
//            }
//
//            /**
//             * 当权限申请中的某一个或多个权限,在此次申请中被用户否定了,但没有勾选不再提醒选项时（权限申请窗口还能再次申请弹出）
//             * 该方法将会被调用.这个方法会在onPermissionDenied之后调用,当申请权限为多个时,onDenied和onRationalShow
//             * 有可能都会被触发.
//             */
//            @Override
//            public void onRationalShow(String... strings) {
//                Log.d(TAG, "onRationalShow: " + strings);
//            }
//        });
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
////        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CAMERA_REQUEST_CODE && resultCode == -1) {
//            List<String> imageList = Album.parseResult(data); // Parse path.
//            if (imageList.size() == 0) {
//                return;
//            }
//            Luban.with(this)
//                    .load(new File(imageList.get(0)))
//                    .setCompressListener(new OnCompressListener() {
//                        @Override
//                        public void onStart() {
//                        }
//
//                        @Override
//                        public void onSuccess(File file) {
//                            if (file.exists()) {
//                                Logger.e("permission", "i come here2222");
//                                getOssMsg(file);
//                            } else {
//                                ToastUtils.showShort("无存储空间, 保存头像");
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                        }
//                    }).launch();
//        } else if (requestCode == IMAGE_REQUEST_CODE && resultCode == -1) {
//            Durban.with(this)
//                    .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary)) // Toolbar color.
//                    .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar color.
//                    .navigationBarColor(ActivityCompat.getColor(this, R.color.black)) // NavigationBar color.
//                    .inputImagePaths(Album.parseResult(data))
//                    .outputDirectory(FileUtils.getRootPath(this).getAbsolutePath())
//                    .aspectRatio(1, 1)
//                    .maxWidthHeight(500, 500)
//                    .requestCode(CROP_REQUEST_CODE)
//                    .start();
//        } else if (requestCode == CROP_REQUEST_CODE && resultCode == -1) {
//            List<String> imageList = Durban.parseResult(data); // Parse path.
//            new MediaScanner(this).scan(imageList); // Scan to system: https://github.com/yanzhenjie/MediaScanner
//            if (imageList.size() == 0) {
//                return;
//            }
//            if (new File(imageList.get(0)).exists()) {
//                Logger.e("permission", "i come here2222");
//                getOssMsg(new File(imageList.get(0)));
//            } else {
//                ToastUtils.showShort("无存储空间, 保存头像");
//            }
//        } else {
//            //单项修改返回处
//        }
//    }
//
//    //------------OSS start------------>
//    public void getOssMsg(final File file) {
//        Map<String, String> params = new HashMap<>();
//        params.put("sign", Md5Utils.encryptH("nb2018"));
//        params.put("token", SPCache.getString("token", ""));
//        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getKey(params).map(new Func1<OssModel, OssModel>() {
//                    @Override
//                    public OssModel call(OssModel ossModel) {
//                        return ossModel;
//                    }
//                })
//                , new CallBack<OssModel>() {
//                    @Override
//                    public void onResponse(OssModel data) {
//                        //获取权限成功，开始上传
//                        sendUpLoadFile(file, data.getData().getAccessKeyId(), data.getData().getAccessKeySecret(), data.getData().getSecurityToken(), data.getData().getDomain(), data.getData().getBucket());
//                    }
//                });
//    }
//
//    private void sendUpLoadFile(final File file, String accessKeyId, String accessKeySecret, String stsToken, String endpoint, final String bucket) {
//        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, stsToken);
//        ClientConfiguration conf = new ClientConfiguration();
//        // 连接超时，默认15秒
//        conf.setConnectionTimeout(15 * 1000);
//        // socket超时，默认15秒
//        conf.setSocketTimeout(15 * 1000);
//        // 最大并发请求书，默认5个
//        conf.setMaxConcurrentRequest(5);
//        // 失败后最大重试次数，默认2次
//        conf.setMaxErrorRetry(2);
//        OSSLog.enableLog();
//        final OSS oss = new OSSClient(aty, endpoint, credentialProvider, conf);
//        String fileName = (int) ((Math.random() * 6 + 1) * 100000) + ".png";
//        sendUpFileRequest(oss, bucket, FileUtils.createContent(fileName), file);
//    }
//
//    private HashMap<String, String> hashMap;
//    private String headPicStr;
//
//    Handler headHandle = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0) {
//                hashMap = new HashMap<>();
//                hashMap.put("headPic", "" + headPicStr);
//                // TODO: 2019/1/12 (chenjy) 调用js把图片给h5
//                Log.d(TAG, "handleMessage: " + headPicStr);
//                mWebView.loadUrl("javascript:getBgImg('" + headPicStr + "')");
//            }
//        }
//    };
//
//    private boolean sendUpFileRequest(OSS oss, String bucket, final String fileName, File file) {
//        // 构造上传请求
//        PutObjectRequest put = new PutObjectRequest(bucket, fileName, file.getPath());
//        // 异步上传时可以设置进度回调
//        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
//            @Override
//            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
//                Logger.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
//            }
//        });
//
//        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
//            @Override
//            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                try {
//                    headPicStr = request.getObjectKey();
//                    headHandle.sendEmptyMessage(0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                // 请求异常
//                if (clientExcepion != null) {
//                    // 本地异常如网络异常等
//                    clientExcepion.printStackTrace();
//                }
//                if (serviceException != null) {
//                    // 服务异常
//                    Logger.e("ErrorCode", serviceException.getErrorCode());
//                    Logger.e("RequestId", serviceException.getRequestId());
//                    Logger.e("HostId", serviceException.getHostId());
//                    Logger.e("RawMessage", serviceException.getRawMessage());
//                }
//            }
//        });
//        if (task == null || !task.isCompleted()) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    //调起友盟分享
//    private void share() {
////        UMWeb web = null;
////        UMImage image = new UMImage(aty, Constants.SHAREIMG);
////        web = new UMWeb(shareUrl.replace("app=1", ""));
//////        http://mweb.gznbly.com/cpxl/#/lineDetails?productCode=cp70272947
////        web.setTitle(title);//标题
//////        web.setDescription(String.format("为您推荐%s详情请点击",title));//描述
////        web.setDescription(Constants.shareDes);//描述
////        web.setThumb(image);  //缩略图
////        new ShareAction(aty)
////                .withMedia(web)
////                .setDisplayList(SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN)
////                .setCallback(umShareListener)
////                .open();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        hideWaitDialog();
//    }
//
////    private UMShareListener umShareListener = new UMShareListener() {
////        @Override
////        public void onStart(SHARE_MEDIA platform) {
////            showWaitDialog();
////        }
////
////        @Override
////        public void onResult(SHARE_MEDIA platform) {
////            hideWaitDialog();
////            if (platform.name().equals("WEIXIN_FAVORITE")) {
//////                Toast.makeText(aty, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
////            } else {
////                if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
////                        && platform != SHARE_MEDIA.EMAIL
////                        && platform != SHARE_MEDIA.FLICKR
////                        && platform != SHARE_MEDIA.FOURSQUARE
////                        && platform != SHARE_MEDIA.TUMBLR
////                        && platform != SHARE_MEDIA.POCKET
////                        && platform != SHARE_MEDIA.PINTEREST
////                        && platform != SHARE_MEDIA.INSTAGRAM
////                        && platform != SHARE_MEDIA.GOOGLEPLUS
////                        && platform != SHARE_MEDIA.YNOTE
////                        && platform != SHARE_MEDIA.EVERNOTE) {
//////                    Toast.makeText(aty, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
////                }
////            }
////        }
////
////        @Override
////        public void onError(SHARE_MEDIA platform, Throwable throwable) {
////            hideWaitDialog();
////            if (platform != SHARE_MEDIA.MORE && platform != SHARE_MEDIA.SMS
////                    && platform != SHARE_MEDIA.EMAIL
////                    && platform != SHARE_MEDIA.FLICKR
////                    && platform != SHARE_MEDIA.FOURSQUARE
////                    && platform != SHARE_MEDIA.TUMBLR
////                    && platform != SHARE_MEDIA.POCKET
////                    && platform != SHARE_MEDIA.PINTEREST
////                    && platform != SHARE_MEDIA.INSTAGRAM
////                    && platform != SHARE_MEDIA.GOOGLEPLUS
////                    && platform != SHARE_MEDIA.YNOTE
////                    && platform != SHARE_MEDIA.EVERNOTE) {
//////                Toast.makeText(aty, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
////                if (throwable != null) {
////                    Log.d("throw", "throw:" + throwable.getMessage());
////                }
////            }
////        }
////
////        @Override
////        public void onCancel(SHARE_MEDIA platform) {
////            hideWaitDialog();
////            getIvRight().postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    KeyboardUtils.hideSoftInput(aty);
////                }
////            }, 200);
////
//////            Toast.makeText(aty, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
////        }
////    };
//    //-------------------------------------分享相关结束---------------------------------------
//
//    /**
//     * 清除WebView缓存
//     */
//    public void clearWebViewCache() {
//
//        //清理Webview缓存数据库
//        try {
//            deleteDatabase("webview.db");
//            deleteDatabase("webviewCache.db");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //WebView 缓存文件
//        File appCacheDir = new File(getFilesDir().getAbsolutePath() + Constants.APP_CACAHE_DIRNAME);
//        Log.e(TAG, "appCacheDir path=" + appCacheDir.getAbsolutePath());
//
//        File webviewCacheDir = new File(getCacheDir().getAbsolutePath() + "/webviewCache");
//        Log.e(TAG, "webviewCacheDir path=" + webviewCacheDir.getAbsolutePath());
//
//        //删除webview 缓存目录
//        if (webviewCacheDir.exists()) {
//            deleteFile(webviewCacheDir);
//        }
//        //删除webview 缓存 缓存目录
//        if (appCacheDir.exists()) {
//            deleteFile(appCacheDir);
//        }
//    }
//
//    /**
//     * 递归删除 文件/文件夹
//     *
//     * @param file
//     */
//    public void deleteFile(File file) {
//
//        Log.i(TAG, "delete file path=" + file.getAbsolutePath());
//
//        if (file.exists()) {
//            if (file.isFile()) {
//                file.delete();
//            } else if (file.isDirectory()) {
//                File files[] = file.listFiles();
//                for (int i = 0; i < files.length; i++) {
//                    deleteFile(files[i]);
//                }
//            }
//            file.delete();
//        } else {
//            Log.e(TAG, "delete file no exists " + file.getAbsolutePath());
//        }
//    }
//
//
//}
