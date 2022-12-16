package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.HomeActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 密码设置界面
 */
public class LoginPwsSettingActivity extends BaseAct {

    @BindView(R.id.et_oldpws)
    ClearableEditText etOldpws;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.et_newpws)
    ClearableEditText etNewpws;
    @BindView(R.id.et_againpws)
    ClearableEditText etAgainpws;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.forget_pwd)
    TextView tvforgetPwd;
    private UserInfo.DataBean userInfo;
    Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");

    @Override
    protected int getContentLayout() {
        return R.layout.activity_login_pws_setting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        userInfo = DataSupport.findFirst(UserInfo.DataBean.class);
//        setTitle("密码设置");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(LoginPwsSettingActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etOldpws.addTextChangedListener(textWatcher);
        etNewpws.addTextChangedListener(textWatcher);
        etAgainpws.addTextChangedListener(textWatcher);
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, LoginPwsSettingActivity.class);
        context.startActivity(intent);
    }

    public void checkStatue() {
        if (!TextUtils.isEmpty(etOldpws.getText().toString()) && !TextUtils.isEmpty(etNewpws.getText().toString())
                && !TextUtils.isEmpty(etAgainpws.getText().toString())) {
            tvSure.setEnabled(true);
        } else {
            tvSure.setEnabled(false);
        }
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkStatue();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @OnClick({R.id.tv_sure,R.id.forget_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_sure:
                if (etOldpws.getText().length() < 6 || etNewpws.getText().length() < 6 || etAgainpws.getText().length() < 6) {
                    Toast.makeText(aty, "输入的密码少于6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                Matcher matcher1 = pattern.matcher(etOldpws.getText().toString());
                Matcher matcher2 = pattern.matcher(etNewpws.getText().toString());
                Matcher matcher3 = pattern.matcher(etAgainpws.getText().toString());
                if (!matcher1.find()) {
                    Toast.makeText(aty, "旧密码格式错误，请输入6到16位数字字母组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matcher2.find()) {
                    Toast.makeText(aty, "新密码格式错误，请输入6到16位数字字母组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!matcher3.find()) {
                    Toast.makeText(aty, "确认新密码格式错误，请输入6到16位数字字母组合", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!etNewpws.getText().toString().equals(etAgainpws.getText().toString())) {
                    Toast.makeText(aty, "新密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                setLoginPws();

                break;

            case R.id.forget_pwd:
                OrgforgetPwd1Activity.instance(LoginPwsSettingActivity.this);
                finish();
                break;
        }

    }

    /**
     * 重置密码请求
     */
    public void setLoginPws() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("oldPsd", Md5Utils.encryptH(etOldpws.getText().toString()));
        hashMap.put("newPsd", Md5Utils.encryptH(etNewpws.getText().toString()));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().centerupdatePsd(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
//                Toast.makeText(LoginPwsSettingActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
                    SPCache.putString(SPCache.KEY_TOKEN,"");
//                    SPCache.putBoolean("islogin", false);
                    if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                        DataSupport.deleteAll(UserInfo.DataBean.class);
                    }
                    //重置成功后clearTopActivity并跳回HomeActivity
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(IntentParam.IS_RETRY_LOGIN,true);
                    showActivity(LoginPwsSettingActivity.this, HomeActivity.class,bundle);
                    ToastUtil.showToast("已更换密码，请重新登录");
                }else
                    ToastUtil.showToast(data.msg);
            }
        });
    }
}
