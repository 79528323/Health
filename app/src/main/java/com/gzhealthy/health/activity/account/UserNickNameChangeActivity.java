package com.gzhealthy.health.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.RegexUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 个人昵称修改
 */
public class UserNickNameChangeActivity extends BaseAct {
    public static int NICKNAME = 1;
    public static int NAMENAME = 2;
    public static int EMAIL = 3;
    public static int ADDRESSDETAIL = 4;
    @BindView(R.id.tv_content)
    ClearableEditText tvContent;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    int type;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_messageitem;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(UserNickNameChangeActivity.this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        type = getIntent().getIntExtra("type", 0);
        String data = getIntent().getStringExtra("content");
        tvContent.setText(data == null ? "" : data);
        if (type == NICKNAME) {
            tvContent.setHint("请输入昵称");
            tvContent.setMaxEms(12);
            setTitle("昵称");
        } else if (type == NAMENAME) {
            tvContent.setHint("请输入姓名");
            setTitle("姓名");
            tvContent.setMaxEms(12);
        } else if (type == EMAIL) {
            tvContent.setHint("请输入邮箱");
            setTitle("邮箱");
        } else if (type == ADDRESSDETAIL) {
            tvContent.setHint("请输入详细地址");
            setTitle("详细地址");
            tvContent.setMaxEms(64);
        }
        checkstate();
    }

    public static void instance(Context context, int type, String content) {
        Intent intent = new Intent(context, UserNickNameChangeActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("content", content);
        context.startActivity(intent);
    }

    /**
     * 更新个人信息
     */
    public void upadatePersonInfo(HashMap<String, String> hashMap) {
//        hashMap.put("token", SPCache.getString("token", ""));

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(hashMap));

        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().editAccountInfo(body,SPCache.getString("token", "")), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                Toast.makeText(UserNickNameChangeActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                if (data.getCode() == 1) {
                    RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_HOME_USER_HEADER_IMAGE);
                    finish();
                }
            }
        });
    }

    @OnClick(R.id.tv_sure)
    public void onViewClicked() {
        if (TextUtils.isEmpty(tvContent.getText().toString())) {
            Toast.makeText(aty, "输入不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        if (type == NICKNAME) {
            map.put("nickName", tvContent.getText().toString());
        } else if (type == NAMENAME) {
            map.put("name", tvContent.getText().toString());
        } else if (type == EMAIL) {
            if (RegexUtils.isEmail(tvContent.getText().toString())) {
                String da = tvContent.getText().toString();
                map.put("email", tvContent.getText().toString());
            } else {
                Toast.makeText(aty, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                return;
            }
        } else if (type == ADDRESSDETAIL) {
            map.put("detailedAddress", tvContent.getText().toString());
        }
        upadatePersonInfo(map);
    }

    public void checkstate() {
        if (type == NICKNAME) {
            tvContent.setLengh(12);
            tvContent.setMaxLines(1);
        } else if (type == NAMENAME) {
            tvContent.setLengh(12);
            tvContent.setMaxLines(1);
        } else if (type == EMAIL) {
            tvContent.setLengh(20);
            tvContent.setMaxLines(1);
        } else if (type == ADDRESSDETAIL) {
            tvContent.setLengh(64);
            tvContent.setMaxLines(4);
        }
    }
}
