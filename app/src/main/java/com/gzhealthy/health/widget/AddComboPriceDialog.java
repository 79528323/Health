package com.gzhealthy.health.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.bigkoo.pickerview.TimePickerView;
import com.gzhealthy.health.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddComboPriceDialog extends Dialog {

    private EditText tv_prebook_time;
    private EditText tv_endbook_time;
    private EditText etComboName;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private EditText tvPrebookTime;
    private TextView tvEndbookTime;
    private TimePickerView pvTime;
    private TimePickerView pvTime2;
    Context context;
    private LinearLayout lvStartTime;
    private LinearLayout lvEndTime;
    int type = 0;
    private LinearLayout lv_sss;
    private Dialog timedialog;
    private LinearLayout linearLayout;

    public AddComboPriceDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    AddTripCombo addTripCombo;
    Date startData;
    Date endData;

    public void setAddTripCombo(AddTripCombo addTripCombo) {
        this.addTripCombo = addTripCombo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_combo_price);
        etComboName = (EditText) findViewById(R.id.vt_price);
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        tvEndTime = (TextView) findViewById(R.id.tv_end_time);

        lvStartTime = (LinearLayout) findViewById(R.id.lv_start_time);
        lvEndTime = (LinearLayout) findViewById(R.id.lv_end_time);
        lv_sss = (LinearLayout) findViewById(R.id.lv_sss);

        tvPrebookTime = (EditText) findViewById(R.id.tv_prebook_date);
        tvEndbookTime = (TextView) findViewById(R.id.tv_endbook_time);

        TextView tvCancle = (TextView) findViewById(R.id.tv_cancle);
        TextView tvSure = (TextView) findViewById(R.id.tv_sure);

        tvCancle.setOnClickListener(onClickListener);
        tvSure.setOnClickListener(onClickListener);
        lvStartTime.setOnClickListener(onClickListener);
        lvEndTime.setOnClickListener(onClickListener);
        tvEndbookTime.setOnClickListener(onClickListener);
        linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        timedialog = new AlertDialog.Builder(context, R.style.quick_option_dialog)
                .setView(linearLayout).create();
        Window window = timedialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        Activity activity = (Activity) context;
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = timedialog.getWindow().getAttributes();
        p.width = d.getWidth();
        timedialog.getWindow().setAttributes(p);

        initTimePicker();
        initTimeDatePicker();
        pvTime.setOnDismissListener(new com.bigkoo.pickerview.listener.OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                timedialog.dismiss();
            }
        });
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
                    AddComboPriceDialog.this.dismiss();
                    break;
                case R.id.tv_sure:
                    if (startData == null || startData == null || TextUtils.isEmpty(etComboName.getText().toString())) {
                        Toast.makeText(context, "请完善信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    addTripCombo.invoke(etComboName.getText().toString(), startData, endData,
                            tvPrebookTime.getText().toString(), tvEndbookTime.getText().toString());
                    break;
                case R.id.lv_start_time:
                    type = 1;
                    pvTime.show();
                    timedialog.show();
                    break;
                case R.id.lv_end_time:
                    type = 2;
                    pvTime.show();
                    timedialog.show();
                    break;
                case R.id.tv_endbook_time:
                    pvTime2.show();
                    timedialog.show();
                    break;
            }
        }
    };

    public interface AddTripCombo {
        void invoke(String etComboName, Date tvStartTime, Date tvEndTime, String tvPrebookTime, String tvEndbookTime);
    }


    /**
     * 时间选择框
     */
    private void initTimeDatePicker() {
        if (pvTime == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(new Date());
            Calendar endDate = Calendar.getInstance();
            Time t = new Time("GMT+8");
            t.setToNow();
            endDate.set(Calendar.DAY_OF_MONTH, endDate.get(Calendar.DAY_OF_MONTH) + 30);
            pvTime = new TimePickerView.Builder(context, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    if (type == 1) {
                        tvStartTime.setText(dateFormat.format(date) + "");
                        startData = date;
                    }
                    if (type == 2) {
                        tvEndTime.setText(dateFormat.format(date) + "");
                        endData = date;
                    }

                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
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
}
