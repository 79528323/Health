package com.gzhealthy.health.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.MedicationRecord;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.PickerViewUtils;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class MedicationAddActivity extends BaseAct {
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.tvFrequency)
    TextView tvFrequency;
    @BindView(R.id.etDose)
    EditText etDose;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvBad)
    TextView tvBad;
    @BindView(R.id.etBad)
    EditText etBad;
    @BindView(R.id.vBottom)
    View vBottom;
    @BindView(R.id.delete)
    TextView delete;
    int id = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_medication_add;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("新增用药记录");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        setMedicationRight(v -> {
            save();
        });

        //输入法遮挡输入框处理
        KeyboardUtils.registerSoftInputChangedListener(getWindow(), height -> {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vBottom.getLayoutParams();
            layoutParams.height = height;
            vBottom.setLayoutParams(layoutParams);
        });

        id = getIntent().getIntExtra(IntentParam.ID,id);
        if (id > 0){
            setTitle("编辑用药记录");
            getRecord();
            delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
    }

    @OnClick({R.id.llStartTime, R.id.llEndTime, R.id.llFrequency, R.id.llUnit, R.id.llBad,R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llStartTime://开始时间
                selectStartTime();
                break;
            case R.id.llEndTime://结束时间
                selectEndTime();
                break;
            case R.id.llFrequency://用药频次
                selectFrequency();
                break;
            case R.id.llUnit://剂量单位
                selectUnit();
                break;
            case R.id.llBad://不良反应
                selectBad();
                break;
            case R.id.delete://删除记录
                deleteDailog();
                break;
        }

    }

    private TimePickerView tpvStartTime;

    /**
     * 选择开始时间
     */
    private void selectStartTime() {
        if (tpvStartTime == null) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR) - 100);
            Calendar endData = Calendar.getInstance();
            tpvStartTime = PickerViewUtils.getTimePickerView(this, startDate, endData, endData, "开始时间", (date, v) -> {
                String dateStr = TimeUtils.millis2String(date.getTime(), "yyyy-MM-dd");
                tvStartTime.setText(dateStr);
            });
        }
        KeyboardUtils.hideSoftInput(this);
        tpvStartTime.show();
    }

    private TimePickerView tpvEndTime;

    /**
     * 选择结束时间
     */
    private void selectEndTime() {
        String startTimeStr = tvStartTime.getText().toString();
        if (TextUtils.isEmpty(startTimeStr)) {
            ToastUtil.showToast("请先选择开始时间");
            return;
        }

        Calendar startDate = Calendar.getInstance();
        startDate.setTimeInMillis(TimeUtils.string2Millis(startTimeStr, "yyyy-MM-dd"));
        Calendar endData = Calendar.getInstance();
        endData.set(Calendar.YEAR, endData.get(Calendar.YEAR) + 100);
        tpvEndTime = PickerViewUtils.getTimePickerView(this, startDate, endData, startDate, "结束时间", (date, v) -> {
            String dateStr = TimeUtils.millis2String(date.getTime(), "yyyy-MM-dd");
            tvEndTime.setText(dateStr);
        });
        KeyboardUtils.hideSoftInput(this);
        tpvEndTime.show();
    }


    private OptionsPickerView opvFrequency;
    private List<String> frequencys = Arrays.asList(
            "每天一次",
            "每天两次",
            "每天三次",
            "每天四次",
            "隔天一次",
            "每周一次",
            "每周三次",
            "每月一次",
            "必须时",
            "必要时");

    /**
     * 选择用药频次
     */
    private void selectFrequency() {
        if (opvFrequency == null) {
            opvFrequency =
                    PickerViewUtils.OptionsPickerView(
                            this,
                            "用药频次",
                            (options1, options2, options3, v) -> {
                                String frequency = frequencys.get(options1);
                                tvFrequency.setText(frequency);
                            }
                    );
            opvFrequency.setPicker(frequencys);
        }
        KeyboardUtils.hideSoftInput(this);
        opvFrequency.show();
    }


    private OptionsPickerView opvUnit;
    private List<String> units = Arrays.asList(
            "mg",
            "g",
            "ml",
            "L",
            "u",
            "ul",
            "粒");

    /**
     * 选择剂量单位
     */
    private void selectUnit() {
        if (opvUnit == null) {
            opvUnit =
                    PickerViewUtils.OptionsPickerView(
                            this,
                            "剂量单位",
                            (options1, options2, options3, v) -> {
                                String unit = units.get(options1);
                                tvUnit.setText(unit);
                            }
                    );
            opvUnit.setPicker(units);
        }
        KeyboardUtils.hideSoftInput(this);
        opvUnit.show();
    }

    private OptionsPickerView opvBad;
    private List<String> bads = Arrays.asList(
            "有",
            "无"
    );

    /**
     * 选择不良反应
     */
    private void selectBad() {
        if (opvBad == null) {
            opvBad =
                    PickerViewUtils.OptionsPickerView(
                            this,
                            "不良反应",
                            (options1, options2, options3, v) -> {
                                String bad = bads.get(options1);
                                tvBad.setText(bad);
                                if ("有".equals(bad)) {
                                    etBad.setVisibility(View.VISIBLE);
                                } else {
                                    etBad.setVisibility(View.GONE);
                                    etBad.setText("");
                                }
                            }
                    );
            opvBad.setPicker(bads);
        }
        KeyboardUtils.hideSoftInput(this);
        opvBad.show();
    }

    private void save() {
        String startTime = tvStartTime.getText().toString();
        if (TextUtils.isEmpty(startTime)) {
            ToastUtil.showToast("请选择开始时间");
            return;
        }

        String endTime = tvEndTime.getText().toString();

        String name = etName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("请输入药品名称");
            return;
        }
        String frequency = tvFrequency.getText().toString();
        if (TextUtils.isEmpty(frequency)) {
            ToastUtil.showToast("请选择用药频次");
            return;
        }
        String dose = etDose.getText().toString();
        if (TextUtils.isEmpty(dose)) {
            ToastUtil.showToast("请输入剂量数值");
            return;
        }
        String unit = tvUnit.getText().toString();
        if (TextUtils.isEmpty(unit)) {
            ToastUtil.showToast("请选择剂量单位");
            return;
        }

        String bad = etBad.getText().toString();
        if (bad.length() > 50) {
            ToastUtil.showToast("不良反应限制50字符");
            return;
        }


        Map<String, String> param = new HashMap<>();
        param.put("startTime", startTime);
        if (!TextUtils.isEmpty(endTime)) {
            param.put("endTime", endTime);
        }
        param.put("name", name);
        param.put("frequency", frequency);
        param.put("dose", dose);
        param.put("unit", unit);
        if (!TextUtils.isEmpty(bad)) {
            param.put("ifHarmful", "1");
            param.put("harmfulReactions", bad);
        } else {
            param.put("ifHarmful", "0");
        }
        if (id > 0){
            param.put("id",String.valueOf(id));
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));
        Observable<BaseModel> observable = null;
        if (id > 0){
            observable = InsuranceApiFactory.getmHomeApi().updateMedicationRecord(body);
        }else {
            observable = InsuranceApiFactory.getmHomeApi().addMedicationRecord(body);
        }
        HttpUtils.invoke(this, this,observable,
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            finish();
                        }
                    }
                });
    }


    public void getRecord(){
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().intoEditMedicationRecord(param),
                new CallBack<MedicationRecord>() {

                    @Override
                    public void onResponse(MedicationRecord data) {
                        if (data.code == 1) {
                            tvStartTime.setText(data.data.startTime);
                            tvEndTime.setText(data.data.endTime);
                            tvFrequency.setText(data.data.frequency);
                            tvUnit.setText(data.data.unit);
                            etName.setText(data.data.name);
                            etDose.setText(data.data.dose);
                            tvBad.setText(data.data.ifHarmful==1?"有":"无");
                            if (data.data.ifHarmful==1){
                                etBad.setVisibility(View.VISIBLE);
                                etBad.setText(data.data.harmfulReactions);
                            }
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }


    public void deleteRecord(){
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().deleteMedicationRecord(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {

                        if (data.code == 1) {
                            goBack();
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }


    public void deleteDailog(){
        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_unbind_watch)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        ((TextView)viewHolder.getView(R.id.title)).setText("确定要删除此条用药记录?");
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            deleteRecord();
                            baseLDialog.dismiss();
                        });
                    }
                })
                .show();
    }
}
