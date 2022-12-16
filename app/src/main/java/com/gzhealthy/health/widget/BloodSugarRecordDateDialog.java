package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * 心率异常提醒弹窗
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class BloodSugarRecordDateDialog extends Dialog implements View.OnClickListener {
    private LifeSubscription mLifeSubscription;
    private ResponseState mResponseState;
    private OnDateSelectedCallBack onDateSelectedCallBack;
    private TimePickerView pvTime;
    private Context context;
    private LinearLayout decorView;

    public BloodSugarRecordDateDialog(@NonNull Context context) {
        super(context,R.style.CalendarBottomDialogStyle);
        this.context = context;
    }

    public void setOnDeleteMemberCallBack(OnDateSelectedCallBack onDateSelectedCallBack) {
        this.onDateSelectedCallBack = onDateSelectedCallBack;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bloodsugar_record_date);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().y = 0;//ScreenUtils.getScreenHeight() / 4;

        setOnDismissListener(dialog -> {
            if (pvTime!=null)
                pvTime.dismiss();
        });
        decorView = findViewById(R.id.picker_decorView);
        initTimePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete:
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
//        if (pvTime!=null)
//            pvTime.show(false);
    }


    private void initTimePicker() {
        pvTime = new TimePickerView.Builder(this.context, (date, v) -> {
//            Log.e("111","TimePickerView="+date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
//            tv_date.setText(dateFormat.format(date));
            if (onDateSelectedCallBack!=null)
                onDateSelectedCallBack.onSelected(dateFormat.format(date));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(false)
                .setDividerColor(ContextCompat.getColor(this.context, R.color.text1))
                .setDividerType(WheelView.DividerType.FILL)
                .setContentSize(21)
                .gravity(Gravity.CENTER)
                .setTitleText("日期和时间")
//                .setDate(selectedDate)
//                .setRangDate(startDate, endDate)
                .setDecorView(decorView)
//                .isDialog(true)
                .setTitleBgColor(ContextCompat.getColor(this.context, R.color.white))
                .setSubmitColor(ContextCompat.getColor(this.context, R.color.global_333333))
                .setCancelColor(ContextCompat.getColor(this.context, R.color.global_999999))
                .build();
        pvTime.setOnDismissListener(o -> {
            if (isShowing())
                dismiss();
        });
        pvTime.show(this.getWindow().getDecorView(),false);
    }

    public interface OnDateSelectedCallBack{
        void onSelected(String date);
    }
}
