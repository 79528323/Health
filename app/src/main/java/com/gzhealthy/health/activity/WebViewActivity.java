package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.report.HealthyReportResultActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.HealthyReport;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.WebViewJavascriptInterface;
import com.gzhealthy.health.utils.WebViewUtils;
import com.gzhealthy.health.widget.ShareDialog;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.webkit.WebSettings.LOAD_NO_CACHE;

/**
 * 通用WebView
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class WebViewActivity extends BaseAct {

    /**
     * 标题
     */
    private static final String EXTRA_TITLE = "extra_title";

    /**
     * 链接
     */
    private static final String EXTRA_LINK = "extra_link";

    /**
     * 富文本
     */
    private static final String EXTRA_TEXT = "extra_text";


    private WebView mWebView;
    private WebSettings mWebSettings;

    private ShareDialog shareDialog;

    @BindView(R.id.llLinearLayout)
    LinearLayout llLinearLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.vBottom)
    View vBottom;

    @BindView(R.id.bg_img)
    ImageView bg_img;
    @BindView(R.id.app_toobar)
    FrameLayout app_toobar;
    @BindView(R.id.back)
    LinearLayout back;
    @BindView(R.id.app_bar_title)
    TextView app_bar_title;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_web_view;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        mWebView = new WebView(this);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setLayoutParams(params);
        mWebView.addJavascriptInterface(new WebViewJavascriptInterface(this), WebViewJavascriptInterface.INTERFACE_NAME);

        llLinearLayout.addView(mWebView);

        mWebSettings = mWebView.getSettings();
        mWebSettings.setGeolocationEnabled(true);//允许定位
        mWebSettings.setCacheMode(LOAD_NO_CACHE);


        //标题,不论有没有传，优先取网页的标题
        String title = getIntent().getStringExtra(EXTRA_TITLE);
        //链接（链接、富文本，两者传一）
        String link = getIntent().getStringExtra(EXTRA_LINK);
        //富文本（链接、富文本，两者传一）
        String text = getIntent().getStringExtra(EXTRA_TEXT);
        setTitle(title);
        if (link != null) {
            if (BuildConfig.DEBUG) {
                Log.i("link", "link=" + link);
            }
            mWebView.loadUrl(link);
        } else if (text != null) {
            mWebView.loadDataWithBaseURL(
                    Constants.Api.BASE_URL1,
                    WebViewUtils.screenAdapter(text),
                    "text/html",
                    "UTF-8",
                    null
            );
        }

        //输入法遮挡输入框处理
        KeyboardUtils.registerSoftInputChangedListener(getWindow(), height -> {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vBottom.getLayoutParams();
            layoutParams.height = height;
            vBottom.setLayoutParams(layoutParams);
        });


        //跳转会员中心时改变头部样式
        if (link.contains("/memberCent.html")){
            hideOrShowToolbar(true);
            hideOrShowAppbar(true);
            mImmersionBar.transparentStatusBar().statusBarDarkFont(false).init();
            app_toobar.setVisibility(View.VISIBLE);
            bg_img.setVisibility(View.VISIBLE);
            llLinearLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
            mWebView.setBackgroundColor(ContextCompat.getColor(this,R.color.transparent));
            app_bar_title.setText("会员中心");
            back.setOnClickListener(v -> {
                goBack();
            });
        }

        //健康时间
        if (link.contains("healthClock")){
            shareDialog();
            setBarRighticon(R.mipmap.icon_share);
            getIvRight().setOnClickListener(v -> {
                if (!shareDialog.isShowing()){
                    shareDialog.show();
                }
            });
        }


        rxManager.onRxEvent(RxEvent.ON_GET_HEALTH_REPORT)
                .subscribe(o -> {
            getReport();
        });
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
            super.onPageStarted(webView, s, bitmap);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView webView, String s) {
            super.onPageFinished(webView, s);
            progressBar.setVisibility(View.INVISIBLE);

            String title = webView.getTitle();
            if (title != null && !title.equalsIgnoreCase("about:blank")) {
                setTitle(title);
            }
        }
    };

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView webView, int newProgress) {
            super.onProgressChanged(webView, newProgress);
            progressBar.setProgress(newProgress);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mWebSettings.setJavaScriptEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mWebSettings.setJavaScriptEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        llLinearLayout.removeView(mWebView);
        mWebView.removeJavascriptInterface(WebViewJavascriptInterface.INTERFACE_NAME);
        mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
        mWebView.clearHistory();
        mWebView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 拼接token
     *
     * @param link
     */
    public static String splicingToken(String link) {
        link = link
                + "?token=" + SPCache.getString(SPCache.KEY_TOKEN, "")
                + "&agent=" + "Android"
                + "&version=" + BuildConfig.VERSION_NAME;
        return link;
    }

    /**
     * 加载链接
     *
     * @param context Context实例
     * @param link    链接
     * @param title   标题,不论有没有传，优先取网页的标题
     */
    public static void startLoadLink(Context context, String link, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_LINK, link);
        context.startActivity(intent);
    }

    /**
     * 加载富文本
     *
     * @param context Context实例
     * @param text    富文本
     * @param title   标题,不论有没有传，优先取网页的标题
     */
    public static void startLoadText(Context context, String text, String title) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_TEXT, text);
        intent.putExtra(EXTRA_TITLE, title);
        context.startActivity(intent);
    }


    /**
     * 加载链接
     * @param context
     * @param link
     * @param title
     * @param bundle
     */
    public static void startLoadLink(Context context, String link, String title,Bundle bundle) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_LINK, link);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public void shareDialog(){
        if (shareDialog==null)
            shareDialog = new ShareDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            String title = bundle.getString("title","");
            String desc = bundle.getString("description","");
            shareDialog.setDatas(title,getIntent().getStringExtra(EXTRA_LINK),desc);
        }
    }



    //获取报告
    public void getReport(){
        Map<String,String> param = new HashMap<>();
        String token = SPCache.getString(SPCache.KEY_TOKEN,"");
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getReportInfo(param,token),
                new CallBack<HealthyReport>() {
                    @Override
                    public void onResponse(HealthyReport data) {
                        if (data.code == 1){
                            HealthyReportResultActivity.inStance(WebViewActivity.this, data);
                            finish();
                        }else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });
    }
}
