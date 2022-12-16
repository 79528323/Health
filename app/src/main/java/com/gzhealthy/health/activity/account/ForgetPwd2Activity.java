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
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPwd2Activity extends BaseAct {

    public static String PHONE_NUMBER = "phoneNumber";
    public static String PHONE_CODE = "phoneCode";

    @BindView(R.id.et_psw1)
    EditText etPsw1;
    @BindView(R.id.et_psw2)
    EditText etPsw2;
    @BindView(R.id.tv_sure)
    TextView tvSure;

    private String Tag = ForgetPwd2Activity.class.getSimpleName();

    private String psw1, psw2;
    private String phoneNumber;
    private String phoneCode;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("忘记密码");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(ForgetPwd2Activity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        etPsw1.addTextChangedListener(TextWatcher);
        etPsw2.addTextChangedListener(TextWatcher);
        etPsw1.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        etPsw2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        phoneNumber = getIntent().getStringExtra(PHONE_NUMBER);
        phoneCode = getIntent().getStringExtra(PHONE_CODE);
    }

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
        return R.layout.activity_forget_pwd2;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
    }


    /**
     * 初始化
     *
     * @param context context
     */
    public static void instance(Context context, String phoneNumber, String phoneCode) {
        Intent intent = new Intent(context, ForgetPwd2Activity.class);
        intent.putExtra(PHONE_NUMBER, phoneNumber);
        intent.putExtra(PHONE_CODE, phoneCode);
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
                registByPhone();
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
     * 发送修改密码请求
     */
    public void registByPhone() {
        L.i("phoneNumber:" + phoneNumber);
        L.i("phoneCode:" + phoneCode);
        String psw = etPsw1.getText().toString();
        Matcher matcher = pattern.matcher(psw);
        if (!matcher.find()) {
            Toast.makeText(aty, "密码格式错误请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", phoneNumber);
        hashMap.put("newPassWord", Md5Utils.encryptH(etPsw1.getText().toString().trim()));
        hashMap.put("messageCode", phoneCode);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().updatePsd(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        if (data.getCode() == 1) {
                            Toast.makeText(ForgetPwd2Activity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                            finish();
                            // 修改密码成功跳转到首页
                            Intent in = new Intent(ForgetPwd2Activity.this, HomeActivity.class);
                            in.putExtra("initPos", 0);
                            showActivity(ForgetPwd2Activity.this, HomeActivity.class);
                        } else {
                            Toast.makeText(ForgetPwd2Activity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
