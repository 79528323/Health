package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.home.VersionModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.KeyboardUtils;
import com.gzhealthy.health.tool.TDevice;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.HttpConstants;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.UpdateAppHttpUtil;
import com.gzhealthy.health.widget.ShareDialog;
import com.gzhealthy.health.widget.UpdateVersionDialogBuilder;

import net.fkm.updateapp.UpdateAppManager;
import net.fkm.updateapp.listener.ExceptionHandler;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Func1;

/**
 * 公司介绍 隐私政策内容
 */
public class AboutHealthContentActivity extends BaseAct {
    @BindView(R.id.content)
    public TextView tx_content;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_health_content;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);


        String title = getIntent().getStringExtra(IntentParam.TITLE);
        String content = getIntent().getStringExtra(IntentParam.CONTENT);
        setTitle(title);
        tx_content.setText(content);
    }

    public static void instance(Context context,String title,String content) {
        Intent intent = new Intent(context, AboutHealthContentActivity.class);
        intent.putExtra(IntentParam.TITLE,title);
        intent.putExtra(IntentParam.CONTENT,content);
        context.startActivity(intent);
    }

}
