package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.MedicationRecordDetailModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MedicationDetailsActivity extends BaseAct {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.tvFrequency)
    TextView tvFrequency;
    @BindView(R.id.tvDose)
    TextView tvDose;
    @BindView(R.id.tvBad)
    TextView tvBad;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_medication_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("用药记录");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        String id = getIntent().getStringExtra(IntentParam.ID);
        getData(id);
    }


    private void getData(String id) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getMedicationRecordDetail(param),
                new CallBack<MedicationRecordDetailModel>() {

                    @Override
                    public void onResponse(MedicationRecordDetailModel data) {
                        if (data.getCode() == 1) {
                            tvTitle.setText(data.getData().getName());
                            tvStartTime.setText(data.getData().getStartTime());
                            tvEndTime.setText(data.getData().getEndTime());
                            tvFrequency.setText(data.getData().getFrequency());
                            tvDose.setText(data.getData().getDose());
                            tvBad.setText(data.getData().getHarmfulReactions());
                        } else {
                            ToastUtil.showToast(data.getMsg());
                        }
                    }
                });
    }
}
