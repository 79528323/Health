package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.HealthApp;
import com.gzhealthy.health.model.SleepInfo;
import com.gzhealthy.health.utils.DispUtil;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class SleepsPopupWindow extends PopupWindow {
    TextView type;
    TextView time;
    TextView date;
    ImageView shape;
    GradientDrawable drawable;
    float Raduii = 20f;

    public SleepsPopupWindow(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sleeps,null);
        type = view.findViewById(R.id.sleep_type);
        time = view.findViewById(R.id.sleep_time);
        date = view.findViewById(R.id.sleep_date);
        shape = view.findViewById(R.id.sleep_shape);

        drawable = new GradientDrawable();
        drawable.setSize(view.getWidth(),view.getHeight());
        drawable.setCornerRadii(new float[]{Raduii,Raduii,0,0,0,0,Raduii,Raduii});
//        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

    }

    public void showPop(View v,SleepInfo.DataBean.SleepDataBean bean){
        if (bean.type.equals("1")){
            //??????
            type.setText("??????");
            type.setTextColor(Color.parseColor("#7A62D1"));
            drawable.setColor(Color.parseColor("#7A62D1"));
        }else if (bean.type.equals("2")){
            //??????
            type.setText("??????");
            type.setTextColor(Color.parseColor("#C0B1FF"));
            drawable.setColor(Color.parseColor("#C0B1FF"));
        }else {
            type.setText("??????");
            type.setTextColor(Color.parseColor("#5294FF"));
            drawable.setColor(Color.parseColor("#5294FF"));
        }

        shape.setBackgroundDrawable(drawable);
        date.setText(bean.startTime+" - "+bean.endTime);
        reformSleep(generateTime(bean.duration),time);
        Log.e("111","contentView W=="+getContentView().getMeasuredWidth()+"    getWidth()="+getWidth());

        showAsDropDown(v,300,0);
    }

    /**
     * ?????????????????????
     *
     * @param time
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
//        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d??????%02d???", hours, minutes) : String.format("%02d???", minutes);
    }


    public void reformSleep(String str,TextView view){
        try {
            if (TextUtils.isEmpty(str)) {
                view.setText("--");
                return;
            }

            int size = DispUtil.sp2px(HealthApp.context,18);
            SpannableStringBuilder builder = new SpannableStringBuilder(str);
            if (str.contains("???")&&str.contains("???")){
                //??????????????????
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("???"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new AbsoluteSizeSpan(size),str.indexOf("???")+1,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if (str.contains("???")){
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("???"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            view.setText(builder);
        }catch (Exception e){
            e.printStackTrace();
            view.setText("--");
        }

    }
}
