package com.gzhealthy.health.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.report.HealthyReportQuestionActivity;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.PersonHealthInfo;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 身体信息
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class BodyInfoActivity extends BaseAct implements View.OnClickListener {
    @BindView(R.id.ivAvatar)
    CircleImageView ivAvatar;
    @BindView(R.id.tvNickname)
    TextView tvNickname;
    @BindView(R.id.tvSex)
    TextView tvSex;
    @BindView(R.id.tvBirthday)
    TextView tvBirthday;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvArea)
    TextView tvArea;
    @BindView(R.id.tvMarriage)
    TextView tvMarriage;
    @BindView(R.id.tvHeight)
    TextView tvHeight;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvWaist)
    TextView tvWaist;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_body_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("身体信息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("首页-体质分析");
        getData();
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("首页-体质分析");
    }

    private void getData() {
        Map<String, String> param = new HashMap<>();
        param.put("token", SPCache.getString(SPCache.KEY_TOKEN, ""));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getAccountHealthRecord(param),
                new CallBack<PersonHealthInfo>() {

                    @Override
                    public void onResponse(PersonHealthInfo data) {
                        if (data.code == 1) {
                            if (data.data.province == null) {
                                data.data.province = "";
                            }
                            if (data.data.city == null) {
                                data.data.city = "";
                            }
                            if (data.data.county == null) {
                                data.data.county = "";
                            }
                            if (data.data.marriage == null) {
                                data.data.marriage = "";
                            }

                            GlideUtils.CircleImage(BodyInfoActivity.this, data.data.headPic, ivAvatar);
                            tvNickname.setText(data.data.nickName);
                            tvSex.setText(data.data.sex == 1 ? "男" : "女");
                            tvBirthday.setText(data.data.birthday);
                            tvAge.setText(data.data.age);
                            if (data.data.province != null && data.data.city != null && data.data.province.equals(data.data.city)) {
                                tvArea.setText(data.data.city + data.data.county);
                            } else {
                                tvArea.setText(data.data.province + data.data.city + data.data.county);
                            }
                            tvMarriage.setText(data.data.marriage);
                            if (Integer.parseInt(data.data.height) > 0) {
                                tvHeight.setText(data.data.height + "cm");
                            }
                            if (Integer.parseInt(data.data.weight) > 0) {
                                tvWeight.setText(data.data.weight + "kg");
                            }
                            if (Integer.parseInt(data.data.waist) > 0) {
                                tvWaist.setText(data.data.waist + "cm");
                            }
                        } else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });
    }


    @OnClick({R.id.btModify, R.id.btTest})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btModify://修改信息
                startActivity(new Intent(BodyInfoActivity.this, HealthBodyInfoActivity.class));
                break;
            case R.id.btTest://无需修改，开始测评
                startActivity(new Intent(BodyInfoActivity.this, HealthyReportQuestionActivity.class));
                break;
        }
    }
}
