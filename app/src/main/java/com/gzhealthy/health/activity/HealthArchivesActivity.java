package com.gzhealthy.health.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.DiseaseRecord;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class HealthArchivesActivity extends BaseAct {


    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_archives;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("健康档案");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
    }

    @OnClick({R.id.rlMenu0, R.id.rlMenu1, R.id.rlMenu2, R.id.rlMenu3, R.id.rlMenu4, R.id.rlMenu5,R.id.rlMenu6})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlMenu0://基本信息
                startActivity(new Intent(this, HealthBodyInfoActivity.class));
                break;
            case R.id.rlMenu1://中医体质
                startActivity(new Intent(this, ChineseMedicineBodyActivity.class));
                break;
            case R.id.rlMenu2://疾病史
                WebViewActivity.startLoadLink(this, WebViewActivity.splicingToken(Constants.LINK_DISEASE_HISTORY), "");
                break;
            case R.id.rlMenu3://用药记录
                startActivity(new Intent(this, MedicationRecordActivity.class));
                break;
            case R.id.rlMenu4://过敏史
                WebViewActivity.startLoadLink(this, WebViewActivity.splicingToken(Constants.LINK_ALLERGY_HISTORY), "");
                break;
            case R.id.rlMenu5://生活习惯
                WebViewActivity.startLoadLink(this, WebViewActivity.splicingToken(Constants.LINK_LIVING_HABIT), "");
                break;
            case R.id.rlMenu6://病例记录
                DiseaseRecordActivity.instance(this);
                break;
        }

    }
}
