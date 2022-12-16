package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.account.LoginActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.NewsDetailModel;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.share.ShareBean;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.Html5WebView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * 资讯详情浏览
 */
public class ArticleHtmlActivity extends BaseAct {
    private static final String TAG = "Html5Activity";
    private final int REQUEST_CODE_CAMERA = 2;
    private String mUrl, shareUrl;

    private LinearLayout mLayout;
    protected Html5WebView mWebView;
    private ProgressBar pb;
    protected String title, beanStr, beanStr2, idCode;
    private Boolean isNeedRightIcon, isNeedSecondRightIcon, isHuize=false;
    protected Bundle bundle;
    private File cameraFile;
    private String productCode = "0";
    private ShareBean shareBean;

    private boolean isSCUrl = true;

    boolean firstVisitWXH5PayUrl = true;
    private ImageView collectBtn;
    private LinearLayout collectBg;
//    private boolean isCollect = false;

    @Override
    protected void init(Bundle saveInstanceState) {
        initView();
    }

    public void initView() {
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back, v -> {
            goBack();
        });
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

//        bundle = getIntent().getBundleExtra("bundle");
//        boolean isNew = bundle.getBoolean("isNew",false);
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



//        setBarRighticon(R.mipmap.ic_details_sharet_bg);
//        getIvRight().setOnClickListener(v -> {
//            NewsListModel.DataBean bean = (NewsListModel.DataBean) getIntent().getSerializableExtra("NewsListModel");
//            WechatPlatform.shareMomentsURL(
//                    this
//                    ,getHtmlData(bean.getContent())
//                    ,"体安APP准备上线"
//                    ,"体安（体安在手，健康我有"
//                    ,BitmapFactory.decodeResource(getResources(),R.mipmap.app_logo));
////            WechatPlatform.shareImageSession(this,
////                    "体安在手，健康我有", BitmapFactory.decodeResource(getResources(),R.mipmap.app_logo));
//        });



//        if (isNeedRightIcon) {
//
//        }
//
//        if (isNeedSecondRightIcon) {
//            setBarLeftRighticon(R.mipmap.ic_hong_news_bg);
//        }
//
//        setTitle(title);
////        setHasBack();
        mLayout = (LinearLayout) findViewById(R.id.web_layout);

//        pb = (ProgressBar) findViewById(R.id.service_pb);
//        pb.setMax(100);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .MATCH_PARENT);

        mWebView = new Html5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);
        mWebView.setWebViewClient(webViewClient);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
//        String UserAgentString = mWebView.getSettings().getUserAgentString();
//        mWebView.getSettings().setUserAgentString(UserAgentString + "third_program_h5");
//        mWebView.getSettings().setUserAgentString(UserAgentString + "/cjApp");
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e(TAG, "msg : " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public void onReceivedTitle(WebView view, String title1) {
                super.onReceivedTitle(view, title);
            }

        });



        init(params);
        getNewsDetail();
    }


    /**
     * 为富文本添加head,解决加载时字体图片无法适配手机屏幕
     * @param bodyHTML
     * @return
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }



    public void init(LinearLayout.LayoutParams params){
        RelativeLayout relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(mWebView);
        RelativeLayout.LayoutParams relparam = new RelativeLayout.LayoutParams(
                DispUtil.dipToPx(this,40),DispUtil.dipToPx(this,40));
        relparam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relparam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relparam.rightMargin = DispUtil.dipToPx(this,20);
        relparam.bottomMargin = DispUtil.dipToPx(this,50);

        collectBg = new LinearLayout(this);
        collectBg.setLayoutParams(relparam);
        collectBg.setGravity(Gravity.CENTER);
        collectBg.setBackgroundResource(R.drawable.btn_collect_bg);

        collectBtn = new ImageView(this);
        collectBtn.setBackgroundResource(R.drawable.selector_collect_btn);
        collectBtn.setLayoutParams(relparam);
        collectBtn.setOnClickListener(v -> {
            collect();
        });

        LinearLayout.LayoutParams bglp = new LinearLayout.LayoutParams(
                DispUtil.dipToPx(this,20),DispUtil.dipToPx(this,20));
        collectBtn.setLayoutParams(bglp);
        collectBg.addView(collectBtn);
        relativeLayout.addView(collectBg);

        mLayout.addView(relativeLayout);
    }


    public void collect(){
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        if (TextUtils.isEmpty(token)){
            LoginActivity.instance(this);
            return;
        }
        NewsListModel.DataBean dataBean = (NewsListModel.DataBean) getIntent().getSerializableExtra("NewsListModel");
        Map<String,Object> param = new HashMap<>();
        param.put("collectionId",dataBean.getId());
        param.put("token",token);
        param.put("type",1);

        Observable observable = collectBtn.isSelected()?
                InsuranceApiFactory.getmHomeApi().cancelCollection(param)
                :InsuranceApiFactory.getmHomeApi().addCollection(param);
        HttpUtils.invoke(this, this, observable,
                new CallBack<ComModel>() {

                    @Override
                    public void onResponse(ComModel data) {
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            collectBtn.setSelected(!collectBtn.isSelected());
                        }
                    }
                });


    }


    public void getNewsDetail(){
        NewsListModel.DataBean dataBean = (NewsListModel.DataBean) getIntent().getSerializableExtra("NewsListModel");
        Map<String,Object> param = new HashMap<>();
        if (dataBean!=null) {
            param.put("id", dataBean.getId());
        }else {
            int id = getIntent().getIntExtra("id",0);
            param.put("id", id);
            collectBg.setVisibility(View.GONE);
        }
        param.put("token",SPCache.getString(SPCache.KEY_TOKEN,""));

        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getNewsdetail(param),
                new CallBack<NewsDetailModel>() {

                    @Override
                    public void onResponse(NewsDetailModel data) {

                        //TODO 使用loadDataWithBaseURL解决webview乱码
                        mWebView.loadDataWithBaseURL(null,data.data.content,"text/html", "UTF-8", null);
                        setTitle(data.data.title);
                        collectBtn.setSelected(data.data.isCollection==1?true:false);
                    }
                });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_html5;
    }

    WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//
////                ToastUtil.showToastLong("URL：" + url);
////                L.i("WebViewClient URL:" + url);
//
//            if (url.startsWith("weixin://")) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    return true;
//                } catch (Exception e) {
//                    // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
//                    ToastUtils.showShort("该手机没有安装微信");
//                    return true;
//                }
//            } else if (url.startsWith("alipays://") || url.startsWith("alipay")) {
//                try {
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    return true;
//                } catch (Exception e) {
//                    // 防止手机没有安装处理某个 scheme 开头的 url 的 APP 导致 crash
//                    // 启动支付宝 App 失败，会自行跳转支付宝网页支付
//                    return true;
//                }
//            }
//
//            // 处理普通 http 请求跳转
//            if (!(url.startsWith("http") || url.startsWith("https"))) {
//                return true;
//            }
//
//            // 处理微信 H5 支付跳转时验证请求头 referer 失效
//            // 验证不通过会出现“商家参数格式有误，请联系商家解决”
//            if (url.contains("wx.tenpay.com")) {
//
//                // 申请微信 H5 支付时填写的域名
//                // 比如经常用来测试网络连通性的 http://www.baidu.com
//                String referer = NEWWEIPAYl;
//
//                // 兼容 Android 4.4.3 和 4.4.4 两个系统版本设置 referer 无效的问题
//                if (("4.4.3".equals(Build.VERSION.RELEASE))
//                        || ("4.4.4".equals(Build.VERSION.RELEASE))) {
//                    if (firstVisitWXH5PayUrl) {
//                        view.loadDataWithBaseURL(referer, "<script>window.location.href=\"" + url + "\";</script>",
//                                "text/html", "utf-8", null);
//                        // 修改标记位状态，避免循环调用
//                        // 再次进入微信H5支付流程时记得重置状态 firstVisitWXH5PayUrl = true
//                        firstVisitWXH5PayUrl = false;
//                    }
//                    // 返回 false 由系统 WebView 自己处理该 url
//                    return false;
//                } else {
//                    // HashMap 指定容量初始化，避免不必要的内存消耗
//                    HashMap<String, String> map = new HashMap<>(1);
//                    map.put("Referer", referer);
//                    Log.e("qqq", url + "------" + referer);
////                        ToastUtil.showToastLong("微信支付：" + url);
////                        L.i("微信支付URL:" + url);
//                    if (url.startsWith("https://wx.tenpay.com/cgi-bin/mmpayweb-bin/app.tofans.com")) {
//                        return true;
//                    }
//                    view.loadUrl(url, map);
                    return true;
//                }
//            }
//            return false;
        }
    };


    protected long mOldTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isHuize) {
                ArticleHtmlActivity.this.finish();
            } else {
                if (System.currentTimeMillis() - mOldTime < 500) {
                    mWebView.clearHistory();
                    mWebView.loadUrl(mUrl);
                } else if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    ArticleHtmlActivity.this.finish();
                }
            }

            mOldTime = System.currentTimeMillis();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            //清空所有Cookie
            CookieSyncManager.createInstance(this);  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cookieManager.flush();
            } else {
                CookieSyncManager.createInstance(this);
                CookieSyncManager.getInstance().sync();
            }

            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.removeAllViews();
            mWebView.getSettings().setJavaScriptEnabled(false);
            mWebView.clearCache(true);
            mWebView.clearFormData();

            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.iv_custom_toolbar_left || view.getId() == R.id.tv_custom_toolbar_left) {
//            if (isHuize) {
//                ArticleHtmlActivity.this.finish();
//                return;
//            }
//            if (System.currentTimeMillis() - mOldTime < 500) {
//                mWebView.clearHistory();
//                mWebView.loadUrl(mUrl);
//            } else if (mWebView.canGoBack()) {
//                mWebView.goBack();
//            } else {
//                ArticleHtmlActivity.this.finish();
//            }
//            mOldTime = System.currentTimeMillis();
//        } else if (view.getId() == R.id.iv_custom_toolbar_right) {
////            ToastUtil.showToast("分享");
//            getPermission();
//        } else if (view.getId() == R.id.iv_custom_toolbar_left_right) {
////            showActivity(Html5Activity.this, MessageListActivity.class);
//        }
    }


    public static void newIntent(Context mContext, NewsListModel.DataBean dataBean) {
        Intent intent = new Intent(mContext, ArticleHtmlActivity.class);
        intent.putExtra("NewsListModel",dataBean);
        mContext.startActivity(intent);
    }


    public static void newIntent(Context mContext, int id) {
        Intent intent = new Intent(mContext, ArticleHtmlActivity.class);
        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }



}
