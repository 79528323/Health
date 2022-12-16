package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.PushMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 关于应用
 */
public class MyMessageDetailActivity extends BaseAct {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.date)
    TextView date;

    @BindView(R.id.edt_name)
    TextView edt_name;

//    @BindView(R.id.content)
//    TextView content;

    @BindView(R.id.content_view)
    WebView webView;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_background_message_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back, v -> {
            goBack();
        });
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        PushMessage.DataBean bean = (PushMessage.DataBean) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        title.setText(bean.title);
        date.setText(bean.pushTime);
        edt_name.setText("体安运营中心");
//        content.setText(bean.content);
        switch (bean.pushType){
            case 1:
                setTitle("紧急消息");
                edt_name.setVisibility(View.GONE);
                break;
            case 3:
            case 4:
                setTitle("平台消息");
                break;

            case 5:
                setTitle("系统消息");
                break;
        }
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.loadData(bean.content,"text/html","utf-8");
    }

    public static void instance(Context context, PushMessage.DataBean bean) {
        Intent intent = new Intent(context, MyMessageDetailActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN,bean);
        context.startActivity(intent);
    }


}
