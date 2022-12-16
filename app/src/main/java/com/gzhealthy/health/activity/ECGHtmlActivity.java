package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.gzhealthy.health.R;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import rx.Observable;


public class ECGHtmlActivity extends BaseAct {
    private static final String TAG = "Html5Activity";
    private RelativeLayout mLayout;
//    protected Html5WebView mWebView;
    private List<Html5WebView> webViewList = new ArrayList<>();

    @Override
    protected void init(Bundle saveInstanceState) {
        initView();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_ecg_html;
    }


    public void initView() {
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back, v -> {
            goBack();
        });
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        mLayout = (RelativeLayout) findViewById(R.id.web_layout);


        String url = getIntent().getStringExtra("url");
        Html5WebView webView = getWebView();
        webView.loadUrl(url);
        webViewList.add(webView);
        mLayout.addView(webView);

        setTitle("心电报告");
    }


    public Html5WebView getWebView(){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Html5WebView mWebView = new Html5WebView(getApplicationContext());
        mWebView.setLayoutParams(params);

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String url = uri.toString();
                Html5WebView webView = getWebView();
                webView.loadUrl(url);
                mLayout.addView(webView);
                webViewList.add(webView);
                return true;
            }
        });
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        return mWebView;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webViewList.size() <= 0){
                finish();
            }else {
                Html5WebView webView = webViewList.get(webViewList.size() - 1);
//                if (webView.canGoBack()){
//                    webView.goBack();
//                }

                webViewList.remove(webView);
                webViewDestory(webView);

                if (webViewList.size() <= 0)
                    finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        Iterator<Html5WebView> iterator = webViewList.iterator();
        while (iterator.hasNext()){
            webViewDestory(iterator.next());
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
    }


    public void webViewDestory(Html5WebView mWebView){
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
//            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mLayout.removeView(mWebView);
            mWebView = null;
        }
    }

    public static void newIntent(Context mContext, NewsListModel.DataBean dataBean) {
        Intent intent = new Intent(mContext, ECGHtmlActivity.class);
        intent.putExtra("NewsListModel",dataBean);
        mContext.startActivity(intent);
    }


    public static void newIntent(Context mContext, int id) {
        Intent intent = new Intent(mContext, ECGHtmlActivity.class);
        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }


    public static void newIntent(Context mContext){
        Intent intent = new Intent(mContext, ECGHtmlActivity.class);
//        intent.putExtra("id",id);
        mContext.startActivity(intent);
    }

}
