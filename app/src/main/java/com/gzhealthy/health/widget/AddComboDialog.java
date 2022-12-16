package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.bigkoo.pickerview.TimePickerView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.TDevice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddComboDialog extends Dialog {

    private AppCompatCheckBox ckCheck;
    private EditText tv_prebook_time;
    private EditText tv_endbook_time;
    private EditText etComboName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private EditText tvPrebookTime;
    private TextView tvEndbookTime;

    // 开始日期时间选择器
    private TimePickerView pvTimeStart;
    // 结束日期时间选择器
    private TimePickerView pvTimeEnd;

    private TimePickerView pvTime2;

    Context context;
    private LinearLayout lvStartTime;
    private LinearLayout lvEndTime;
    private LinearLayout lv_sss;
    private Dialog timedialog;
    private LinearLayout linearLayout;
    String ComboName, startTime, endtime, statue, predate, endbookdate;

    public AddComboDialog(@NonNull Context context) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
    }

    public AddComboDialog(@NonNull Context context, String ComboName, String startTime, String endtime, String statue, String predate, String endbookdate) {
        super(context, R.style.ActionSheetDialogStyle);
        this.context = context;
        this.ComboName = ComboName;
        this.startTime = startTime;
        this.endtime = endtime;
        this.statue = statue;
        this.predate = predate;
        this.endbookdate = endbookdate;
    }

    AddTripCombo addTripCombo;

    public void setAddTripCombo(AddTripCombo addTripCombo) {
        this.addTripCombo = addTripCombo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_combo);
        etComboName = (EditText) findViewById(R.id.et_combo_name);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);

        lvStartTime = (LinearLayout) findViewById(R.id.lv_start_time);
        lvEndTime = (LinearLayout) findViewById(R.id.lv_end_time);
        lv_sss = (LinearLayout) findViewById(R.id.lv_sss);
        ckCheck = (AppCompatCheckBox) findViewById(R.id.ck_check);

        tvPrebookTime = (EditText) findViewById(R.id.tv_prebook_time);
        tvEndbookTime = (TextView) findViewById(R.id.tv_endbook_time);
        tvEndbookTime.setOnClickListener(onClickListener);
        TextView tvCancle = (TextView) findViewById(R.id.tv_cancle);
        TextView tvSure = (TextView) findViewById(R.id.tv_sure);
        if (!TextUtils.isEmpty(ComboName)) {
            etComboName.setText(ComboName);
            tvStartTime.setText(startTime);
            tvEndTime.setText(endtime);
            tvPrebookTime.setText(predate);
            tvEndbookTime.setText(endbookdate);
            /*if ("1".equals(statue)) {
                ckCheck.setChecked(true);
            } else {
                ckCheck.setChecked(false);
            }*/
            ckCheck.setEnabled(false);
        }
        tvCancle.setOnClickListener(onClickListener);
        tvSure.setOnClickListener(onClickListener);
        lvStartTime.setOnClickListener(onClickListener);
        lvEndTime.setOnClickListener(onClickListener);

        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        timedialog = new AlertDialog.Builder(context, R.style.quick_option_dialog)
                .setView(linearLayout).create();
        Window window = timedialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
//        Activity activity=(Activity) context ;
//        WindowManager m = activity.getWindowManager();
//        Display d = m.getDefaultDisplay();
//        WindowManager.LayoutParams p = timedialog.getWindow().getAttributes();
//        p.width=d.getWidth();
//        timedialog.getWindow().setAttributes(p);
        timedialog.getWindow().getAttributes().width = TDevice.getScreenWidth(context);

        initTimePicker();
        pvTime2.setOnDismissListener(new com.bigkoo.pickerview.listener.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                timedialog.dismiss();
            }
        });

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_cancle:
                    AddComboDialog.this.dismiss();
                    break;
                case R.id.tv_sure:
                    addTripCombo.invoke(etComboName.getText().toString(), tvStartTime.getText().toString(), tvEndTime.getText().toString(),
                            tvPrebookTime.getText().toString(), tvEndbookTime.getText().toString(), TextUtils.isEmpty(ComboName));
                    break;
                case R.id.lv_start_time:
                    showStartTimePicker();
                    break;
                case R.id.lv_end_time:
                    if (TextUtils.isEmpty(tvStartTime.getText().toString())) {
                        Toast.makeText(context, "请先选择销售开始时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showEndTimePicker();
                    break;
                case R.id.tv_endbook_time:
                    pvTime2.show();
                    timedialog.show();
                    break;
            }
        }
    };

    public interface AddTripCombo {
        void invoke(String etComboName, String tvStartTime, String tvEndTime, String tvPrebookTime, String tvEndbookTime, boolean isEdit);
    }

    private void showStartTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        /*if (!tvStartTime.getText().toString().contains("时间")) {
            long time = getStringToDate(tvStartTime.getText().toString(), "yyyy-MM-dd");
            selectedDate.setTimeInMillis(time);
        }*/
        // 构建起始日期
        Calendar startDate = Calendar.getInstance();
        // 其实日期不能选当天
        startDate.setTimeInMillis(System.currentTimeMillis() + 60 * 60 * 24 * 1000);

        // 构建结束日期
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) + 30);

        pvTimeStart = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tvStartTime.setText(dateFormat.format(date) + "");

                if (!TextUtils.isEmpty(tvEndTime.getText().toString())) {
                    long endTime = getStringToDate(tvEndTime.getText().toString(), "yyyy-MM-dd");
                    if (date.getTime() > endTime) {
                        tvEndTime.setText("");
                        tvEndTime.setHint("请输入销售结束时间");
                    }
                }
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .setDividerColor(Color.parseColor("#FFeeeeee"))
                .setContentSize(21)
                .setTitleText("选择时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(linearLayout)
                .setSubmitColor(Color.parseColor("#FF03b68f"))
                .setCancelColor(Color.parseColor("#FF03b68f"))
                .build();
        pvTimeStart.setOnDismissListener(new com.bigkoo.pickerview.listener.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                timedialog.dismiss();
            }
        });
        pvTimeStart.show();
        timedialog.show();
    }

    private void showEndTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        /*if (!tvEndTime.getText().toString().contains("时间")) {
            long time = getStringToDate(tvEndTime.getText().toString(), "yyyy-MM-dd");
            selectedDate.setTimeInMillis(time);
        }*/

        // 构建起始日期
        Calendar startDate = Calendar.getInstance();
        long time = getStringToDate(tvStartTime.getText().toString(), "yyyy-MM-dd");
        // 其实日期不能选当天
        startDate.setTimeInMillis(time);

        // 构建结束日期
        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) + 30);

        pvTimeEnd = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                tvEndTime.setText(dateFormat.format(date) + "");
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(false)
                .setDividerColor(Color.parseColor("#FFeeeeee"))
                .setContentSize(21)
                .setTitleText("选择时间")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(linearLayout)
                .setSubmitColor(Color.parseColor("#FF03b68f"))
                .setCancelColor(Color.parseColor("#FF03b68f"))
                .build();
        pvTimeEnd.setOnDismissListener(new com.bigkoo.pickerview.listener.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                timedialog.dismiss();
            }
        });
        pvTimeEnd.show();
        timedialog.show();
    }

    /**
     * 时间选择框
     */
    private void initTimePicker() {
        if (pvTime2 == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(new Date());
            Calendar endDate = Calendar.getInstance();
            Time t = new Time("GMT+8");
            t.setToNow();
            endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) + 30);
            pvTime2 = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    tvEndbookTime.setText(dateFormat.format(date) + "");
                }
            })
                    .setType(new boolean[]{false, false, false, true, true, true})
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCenterLabel(false)
                    .setDividerColor(Color.parseColor("#FFeeeeee"))
                    .setContentSize(21)
                    .setTitleText("选择日期")
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setDecorView(linearLayout)
                    .setSubmitColor(Color.parseColor("#FF03b68f"))
                    .setCancelColor(Color.parseColor("#FF03b68f"))
                    .build();
        }

    }

    private long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
