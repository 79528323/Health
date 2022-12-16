package com.gzhealthy.health.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.home.VersionModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.KeyboardUtils;
import com.gzhealthy.health.tool.TDevice;
import com.gzhealthy.health.tool.Tools;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.HttpConstants;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.utils.UpdateAppHttpUtil;
import com.gzhealthy.health.widget.ShareDialog;
import com.gzhealthy.health.widget.UpdateVersionDialogBuilder;
import com.yxp.permission.util.lib.PermissionUtil;
import com.yxp.permission.util.lib.callback.PermissionResultCallBack;

import net.fkm.updateapp.UpdateAppManager;
import net.fkm.updateapp.listener.ExceptionHandler;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Func1;

/**
 * 关于应用
 */
public class AboutHealthActivity extends BaseAct {
    private String url;
    private ShareDialog shareDialog;
    @BindView(R.id.tv_code)
    TextView tv_code;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_about_health;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setTitle("关于体安");
//        mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init();
//        mImmersionBar.navigationBarColor("#FFFFFF").init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back, v -> {
            goBack();
        });
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        loadingPageView.findViewById(R.id.lv_copany).setOnClickListener(this);
        loadingPageView.findViewById(R.id.lv_secrete).setOnClickListener(this);

        tv_code.setText("当前版本v"+Tools.getVersion(this));
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, AboutHealthActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        String title = ((TextView)view).getText().toString();
        getCompanyPrivacy(title);
//        switch (view.getId()) {
//            case R.id.lv_copany:
//                getCompanyPrivacy(title);
//                break;
////            case R.id.lv_shopph:
////                Html5Activity.newIntent(aty, "营业执照", Constants.businessLicence, false);
////                break;
//            case R.id.lv_secrete:
//                getCompanyPrivacy(title);
//                break;
//        }
    }



    private void getCompanyPrivacy(String title){
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getPlatformAgreement(title)
                , new CallBack<CompanyPrivacy>() {
                    @Override
                    public void onResponse(CompanyPrivacy data) {
                        AboutHealthContentActivity.instance(AboutHealthActivity.this,title,data.data);
                    }
                });
    }

}
