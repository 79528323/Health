package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.RegexUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class RealNameCheckActivity extends BaseAct {

    @BindView(R.id.et_name)
    ClearableEditText etName;
    @BindView(R.id.et_cardno)
    ClearableEditText etCardno;
    @BindView(R.id.tv_commit)
    TextView tvCommit;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_realname;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("实名认证");
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, RealNameCheckActivity.class);
        context.startActivity(intent);
    }


    @OnClick(R.id.tv_commit)
    public void onViewClicked() {
        startRealName();
    }

    /**
     * 实名认证
     */
    public void startRealName() {
        if (TextUtils.isEmpty(etName.getText().toString().trim())) {
            Toast.makeText(aty, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(etCardno.getText().toString().trim()) || !RegexUtils.isIDCard18(etCardno.getText().toString().trim())) {
            Toast.makeText(aty, "请输入正确的身份证号码", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("realName", etName.getText().toString().trim());
        hashMap.put("idCard", etCardno.getText().toString().trim());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().realName(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {

                Toast.makeText(RealNameCheckActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
                    finish();
                }
            }
        });
    }
}
