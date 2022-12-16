package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.FenceSetting;
import com.gzhealthy.health.model.banner.DeviceDetail;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 安全围栏生效时间
 */
public class SecurityFenceEntryIntoforceTimeActivity extends BaseAct {

    @BindView(R.id.container_time)
    LinearLayout container_time;

    @BindView(R.id.lay_all_day)
    LinearLayout lay_all_day;

    @BindView(R.id.lay_custom)
    LinearLayout lay_custom;

    @BindView(R.id.lay_day)
    LinearLayout lay_day;

    @BindView(R.id.start_time)
    TextView start_time;

    @BindView(R.id.end_time)
    TextView end_time;

    TimePickerView pvTime;

    FenceSetting.DataBean bean;

    @BindView(R.id.select_all_day)
    TextView select_all_day;

    @BindView(R.id.select_day)
    TextView select_day;

    @BindView(R.id.select_custom_day)
    TextView select_custom_day;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_security_fence_entryinto_forcetime;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("安全围栏生效时间");
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        getTvRight().setText("确定");
        getTvRight().setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        getTvRight().setOnClickListener(v -> {
            save();
        });
        initView();
    }


    public void initView(){
        lay_all_day.setOnClickListener(this::onViewClicked);
        lay_day.setOnClickListener(this::onViewClicked);
        lay_custom.setOnClickListener(this::onViewClicked);
        start_time.setOnClickListener(this::onViewClicked);
        end_time.setOnClickListener(this::onViewClicked);
        select_custom_day.setOnClickListener(this::onViewClicked);
        select_all_day.setOnClickListener(this::onViewClicked);
        select_day.setOnClickListener(this::onViewClicked);

        bean = (FenceSetting.DataBean) getIntent().getSerializableExtra(IntentParam.DATA_BEAN);
        switch (bean.fenceEffectTimeTypeStr){
            case "全天":
                lay_all_day.setSelected(true);
                break;

            case "白天":
                lay_day.setSelected(true);
                break;

            default:
                lay_custom.setSelected(true);
                if (lay_custom.isSelected()){
                    container_time.setVisibility(View.VISIBLE);
                }
                break;
        }
        start_time.setText(bean.startTime);
        end_time.setText(bean.endTime);
//        strStartTime = bean.startTime;
//        strEndTime = bean.endTime;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart("我的-账号与安全");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd("我的-账号与安全");
    }

    @OnClick({R.id.lay_day, R.id.lay_all_day, R.id.lay_custom,R.id.start_time,R.id.end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_day:
            case R.id.lay_day:
                select_day.setSelected(true);
                select_all_day.setSelected(false);
                select_custom_day.setSelected(false);
                container_time.setVisibility(View.GONE);
                start_time.setText("08:00");
                end_time.setText("20:00");
                break;

            case R.id.select_all_day:
            case R.id.lay_all_day:
                select_day.setSelected(false);
                select_all_day.setSelected(true);
                select_custom_day.setSelected(false);
                container_time.setVisibility(View.GONE);
                start_time.setText("00:00");
                end_time.setText("23:59");
                break;

            case R.id.select_custom_day:
            case R.id.lay_custom:
                start_time.setText("");
                end_time.setText("");
                select_day.setSelected(false);
                select_all_day.setSelected(false);
                select_custom_day.setSelected(true);
                container_time.setVisibility(View.VISIBLE);
                break;

            case R.id.start_time:
                initTimePicker(start_time);
                break;

            case R.id.end_time:
                initTimePicker(end_time);
                break;
            default:
                break;
        }
    }


    private void initTimePicker(TextView view) {
//        if (pvTime == null) {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        Time t = new Time("GMT+8");
        t.setToNow();
//            selectedDate.set(t.year, t.month, t.monthDay,1,10);
//            selectedDate.set(Calendar.YEAR,t.year);
//            selectedDate.set(Calendar.MONTH,t.month);
//            selectedDate.set(Calendar.DAY_OF_MONTH,t.monthDay);
//            selectedDate.set(Calendar.HOUR_OF_DAY,1);
//            selectedDate.set(Calendar.MINUTE,33);
        startDate.set(t.year, t.month, t.monthDay);
        endDate.set(t.year, t.month, t.monthDay);
        pvTime = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String dateTime = dateFormat.format(date);
            view.setText(dateTime);
            if (view.getId() == R.id.start_time){
                start_time.setText(dateTime);
            }else
                end_time.setText(dateTime);
        })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "：", "", "")
                .isCenterLabel(true)
                .setDividerColor(ContextCompat.getColor(this, R.color.text1))
                .setDividerType(WheelView.DividerType.FILL)
                .setContentSize(21)
                .gravity(Gravity.CENTER)
                .setTitleText(view.getId() == R.id.start_time ? "自定义开始时间" : "自定义结束时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
                .setSubmitColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setCancelColor(ContextCompat.getColor(this, R.color.global_333333))
                .build();
        pvTime.show();
    }


    public void save(){
        if (TextUtils.isEmpty(start_time.getText().toString())){
            ToastUtil.showToast("请选择开始时间");
            return;
        }
        if (TextUtils.isEmpty(start_time.getText().toString())){
            ToastUtil.showToast("请选择结束时间");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(bean.id));
        params.put("startTime",start_time.getText().toString());
        params.put("endTime", end_time.getText().toString());
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().updateSafetyFenceTime(params),
                new CallBack<BaseModel>() {
                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.msg);
                        if (data.code == 1) {
                            goBack();
                        }
                    }
                });
    }

    public static void instance(Context context, FenceSetting.DataBean setting) {
        Intent intent = new Intent(context, SecurityFenceEntryIntoforceTimeActivity.class);
        intent.putExtra(IntentParam.DATA_BEAN,setting);
        context.startActivity(intent);
    }


}
