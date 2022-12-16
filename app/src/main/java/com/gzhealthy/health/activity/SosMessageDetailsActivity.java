package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.HeartRateAdapter;
import com.gzhealthy.health.adapter.TemperatureAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.SosListModel;
import com.gzhealthy.health.utils.ToastUtil;

/**
 * 紧急消息详情
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SosMessageDetailsActivity extends BaseAct {
    private TextView tvTitle, tvDate;

    private LinearLayout llSos;
    private TextView tvProcessed, tvProcessing;

    private LinearLayout llHeartRate;
    private RecyclerView rvHeartRate;

    private LinearLayout llTemperature;
    private RecyclerView rvTemperature;


    @Override
    protected int getContentLayout() {
        return R.layout.activity_sos_message_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setBarLeftIcon(R.mipmap.login_back);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDate = (TextView) findViewById(R.id.tvDate);

        llSos = (LinearLayout) findViewById(R.id.llSos);
        tvProcessed = (TextView) findViewById(R.id.tvProcessed);
        tvProcessing = (TextView) findViewById(R.id.tvProcessing);

        llHeartRate = (LinearLayout) findViewById(R.id.llHeartRate);
        rvHeartRate = (RecyclerView) findViewById(R.id.rvHeartRate);

        llTemperature = (LinearLayout) findViewById(R.id.llTemperature);
        rvTemperature = (RecyclerView) findViewById(R.id.rvTemperature);


        SosListModel.DataModel dataModel = (SosListModel.DataModel) getIntent().getSerializableExtra(IntentParam.DATA);
        tvTitle.setText(dataModel.getTitle());
        tvDate.setText(dataModel.getCreateTime());
        switch (dataModel.getType()) {
            case 1:
                setTitle("SOS求助");
                llSos.setVisibility(View.VISIBLE);
                if (dataModel.getState() == 0) {
                    tvProcessing.setVisibility(View.VISIBLE);
                } else if (dataModel.getState() == 1) {
                    tvProcessed.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                setTitle("心率异常");
                llHeartRate.setVisibility(View.VISIBLE);
                HeartRateAdapter heartRateAdapter = new HeartRateAdapter();
                rvHeartRate.setAdapter(heartRateAdapter);
                heartRateAdapter.setNewData(dataModel.getRateWarnList());
                break;
            case 3:
                setTitle("体温异常");
                llTemperature.setVisibility(View.VISIBLE);
                TemperatureAdapter temperatureAdapter = new TemperatureAdapter();
                rvTemperature.setAdapter(temperatureAdapter);
                temperatureAdapter.setNewData(dataModel.getTemperatureWarnList());
                break;
            default:
                ToastUtil.showToast("参数异常");
                finish();
                return;
        }
    }
}
