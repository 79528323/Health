package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.PickerViewUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.BloodPressureValuePicker;
import com.gzhealthy.health.widget.BloodSugarValuePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 血压记录添加
 */
public class BloodPressureRecordAddActivity extends BaseAct{

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_high)
    TextView tv_high;

    @BindView(R.id.tv_low)
    TextView tv_low;

    @BindView(R.id.pressure_commit)
    Button pressure_commit;

    TimePickerView pvTime;
    PickerViewUtils pickerViewUtils;

    BloodPressureValuePicker highValuePicker,lowValuePicker;

    String highvalue,lowvalue;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bloodpressure_record_add;
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, BloodPressureRecordAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("血压");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        initView();
    }


    public void initView(){
        String date = DateUtil.getStringDateNow("yyyy.M.dd HH:mm");
        tv_date.setText(date);
        tv_date.setOnClickListener(this::onClick);
        tv_high.setOnClickListener(this::onClick);
        tv_low.setOnClickListener(this::onClick);
        pressure_commit.setOnClickListener(this::onClick);

        initTimePicker();

        highValuePicker = new BloodPressureValuePicker(this, value -> {
            tv_high.setText(value);
            this.highvalue = value;
        },BloodPressureValuePicker.TYPE_PRESSURE_SYSTOLIC);


        lowValuePicker = new BloodPressureValuePicker(this, value -> {
            tv_low.setText(value);
            this.lowvalue = value;
        },BloodPressureValuePicker.TYPE_PRESSURE_DIASTOLIC);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.tv_date:
                if (!pvTime.isShowing())
                    pvTime.show();
                break;

            case R.id.tv_low:
                lowValuePicker.showPicker();
                break;

            case R.id.tv_high:
                highValuePicker.showPicker();
                break;

            case R.id.pressure_commit:
                if (TextUtils.isEmpty(highvalue)){
                    ToastUtil.showToast("请选择收缩压");
                    return;
                }else if (TextUtils.isEmpty(lowvalue)){
                    ToastUtil.showToast("请选择舒张压");
                    return;
                }

                postPressure();
                break;

            default:
                break;
        }
    }

    private void initTimePicker() {
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 1900);
        startDate.set(Calendar.MONTH, 0);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        Calendar selectData = Calendar.getInstance();
        selectData.set(Calendar.YEAR, selectData.get(Calendar.YEAR));
        selectData.set(Calendar.MONTH, selectData.get(Calendar.MONTH));
        selectData.set(Calendar.DAY_OF_MONTH, selectData.get(Calendar.DAY_OF_MONTH));

        pvTime = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.dd HH:mm");
            tv_date.setText(dateFormat.format(date));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "")
                .isCenterLabel(false)
                .setDividerColor(ContextCompat.getColor(this, R.color.text1))
                .setDividerType(WheelView.DividerType.FILL)
                .setContentSize(18)
                .setRangDate(startDate,selectData)
                .setDate(selectData)
                .gravity(Gravity.CENTER)
                .setTitleText("日期和时间")
                .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
                .setSubmitColor(ContextCompat.getColor(this, R.color.global_333333))
                .setCancelColor(ContextCompat.getColor(this, R.color.global_999999))
                .build();

    }


    public void postPressure(){
        Map<String,String> param = new HashMap<>();
        String date = tv_date.getText().toString();
        date = date.replace(".","-");
        param.put("dateTime", date);
        param.put("high",highvalue);
        param.put("low",lowvalue);

        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().uploadBloodPressure(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1){
                            goBack();
                        }
                        ToastUtil.showToast(data.msg);
                    }
                });
    }

}
