package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompanyPrivacy;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.RegexUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.ValidateCodeView;

import org.litepal.crud.DataSupport;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class ChangePhoneActivity extends BaseAct {

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_phone_code)
    EditText tv_phone_code;

    @BindView(R.id.tv_send_code)
    ValidateCodeView tv_send_code;

//    @BindView(R.id.agree)
//    CheckBox checkBox;

    @BindView(R.id.tv_sure)
    TextView tv_sure;

    @Override
    protected void initData() {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
//        setTitle("绑定手机号码");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(ChangePhoneActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        tv_phone.addTextChangedListener(TextWatcher);
        tv_phone_code.addTextChangedListener(TextWatcher);
    }

    @OnClick({R.id.tv_sure,R.id.tv_send_code})
    public void OnViewClick(View view){
        switch (view.getId()){

            case R.id.tv_sure:
                bindPhonePwd();
                break;

            case R.id.tv_send_code:
                String phone = tv_phone.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    ToastUtil.showToast("请输入你要绑定的新手机号码");
                    return;
                }else if (!RegexUtils.isMobileSimple(phone)){
                    ToastUtil.showToast("请输入正确的手机号码");
                    return;
                }
                sendPhonePwd();
                break;
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.change_phone_activity;
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
    public static void instance(Context context) {
        Intent intent = new Intent(context, ChangePhoneActivity.class);
        context.startActivity(intent);
    }



    /**
     * 开始发送验证码
     */
    public void sendPhonePwd() {
        int type = 3;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("phone", tv_phone.getText().toString());
        hashMap.put("type", type);
        hashMap.put("sign", Md5Utils.encryptH(tv_phone.getText().toString()+type+"gzhealthy"));
//        hashMap.put("sign", Md5Utils.encryptH(tv_phone.getText().toString().toString() + "nb"));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().getRegisterCode(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                Toast.makeText(ChangePhoneActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
//                    setdownCount(count);
                    tv_send_code.startCountTime();
                }
            }
        });
    }

    /**
     * 绑定手机
     */
    public void bindPhonePwd() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("phone", tv_phone.getText().toString());
        hashMap.put("smsCode", tv_phone_code.getText().toString());
        hashMap.put("token", SPCache.getString(SPCache.KEY_TOKEN,""));
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().changePhone(hashMap), new CallBack<BaseModel<String>>() {
            @Override
            public void onResponse(BaseModel<String> data) {
                Toast.makeText(ChangePhoneActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
                    finish();
                }

                ToastUtil.showToast(data.msg);
            }
        });
    }


    /**
     * 查询三个编辑框输入是否为空
     */
    public void checkState() {
        if (!TextUtils.isEmpty(tv_phone.getText().toString()) && !TextUtils.isEmpty(tv_phone_code.getText().toString())) {
            tv_sure.setEnabled(true);
        } else {
            tv_sure.setEnabled(false);
        }
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

}
