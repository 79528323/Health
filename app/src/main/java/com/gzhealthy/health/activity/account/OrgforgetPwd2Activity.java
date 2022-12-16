package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrgforgetPwd2Activity extends BaseAct {

    public static String PHONE_NUMBER = "phoneNumber";
    public static String PHONE_CODE = "phoneCode";

    @BindView(R.id.et_psw1)
    EditText etPsw1;
    @BindView(R.id.et_psw2)
    EditText etPsw2;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private String Tag = OrgforgetPwd2Activity.class.getSimpleName();

    private String psw1, psw2;
    private String phoneNumber;
    private String phoneCode;

    TextWatcher TextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_org_forget_pwd2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("注册");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(OrgforgetPwd2Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etPsw1.addTextChangedListener(TextWatcher);
        etPsw2.addTextChangedListener(TextWatcher);
        etPsw1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etPsw2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
        phoneCode = getIntent().getStringExtra(PHONE_CODE);
    }


    /**
     * 初始化
     *
     * @param context context
     */
    public static void instance(Context context, String phoneNumber, String phoneCode) {
        Intent intent = new Intent(context, OrgforgetPwd2Activity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(PHONE_CODE, phoneCode);
        context.startActivity(intent);
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, OrgforgetPwd2Activity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.tv_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sure:
                psw1 = etPsw1.getText().toString().trim();
                psw2 = etPsw2.getText().toString().trim();
                if (!psw1.equals(psw2)) {
                    ToastUtil.showToast("两次输入的密码不一致！");
                    return;
                }
                setPassWord();
                break;
            default:
                break;
        }
    }

    /**
     * 查询两个个编辑框输入是否为空
     */
    public void checkState() {
        if (!TextUtils.isEmpty(etPsw1.getText().toString()) && !TextUtils.isEmpty(etPsw1.getText().toString())) {
            tvSure.setEnabled(true);
        } else {
            tvSure.setEnabled(false);
        }
    }

    Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$");

    /**
     * 设置密码
     */
    public void setPassWord() {
        L.i("phoneNumber:" + phoneNumber);
        L.i("phoneCode:" + phoneCode);
        String psw = etPsw2.getText().toString();
        Matcher matcher = pattern.matcher(psw);
        if (!matcher.find()) {
            ToastUtil.showToast("密码格式错误请重新输入");
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("newPsd", psw);
        hashMap.put("token", SPCache.getString(SPCache.KEY_TOKEN,""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().setPsd(hashMap),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.getCode() == 1) {
//                            finish();
                            SPCache.putString(SPCache.KEY_TOKEN,"");
//                            SPCache.putBoolean("islogin", false);
                            if (null != DataSupport.findFirst(UserInfo.DataBean.class)) {
                                DataSupport.deleteAll(UserInfo.DataBean.class);
                            }

                            //重置成功后clearTopActivity并跳回HomeActivity
                            Bundle bundle = new Bundle();
                            bundle.putBoolean(IntentParam.IS_RETRY_LOGIN,true);
                            showActivity(OrgforgetPwd2Activity.this, HomeActivity.class,bundle);
                            ToastUtil.showToast("已更换密码，请重新登录");
                        }else
                            ToastUtil.showToast(data.getMsg());
                    }
                });
    }

}
