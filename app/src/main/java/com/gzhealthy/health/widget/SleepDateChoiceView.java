package com.gzhealthy.health.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.utils.DispUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Created by Justin_Liu
 * on 2021/3/8
 */
public class SleepDateChoiceView extends LinearLayout {
    public final static String TX_LAST_DAY = "昨日夜间";
    private Context context;
    private final int TYPE_LEFE = 0;
    private final int TYPE_RIGHT = 1;
    private final int TYPE_CENTER = 2;
    private TextView view;
    private ImageView icon;
    public OnDateChoiceListener onDateChoiceListener;
    private String date;
    private String centerText;

    public SleepDateChoiceView(Context context){
        super(context);
        init(context);
    }

    public SleepDateChoiceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }



    private void init(Context context){
        this.context = context;
        //默认获取前一天
        date = DateUtil.scrollCalendarDate(DateUtil.getStringDate(),-1);
        setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(lp);
//        initCalender();
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
        addView(createButton(context,TYPE_LEFE));
        addView(createTitle(context,TYPE_CENTER));
        addView(createButton(context,TYPE_RIGHT));
        setDate(date);
    }


    @SuppressLint("ResourceType")
    private View createTitle(Context context, int type){
        LayoutParams params =null;
        switch (type){
            case TYPE_CENTER:
                view = new TextView(context);
                params = new LayoutParams(
                        LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
//                params.leftMargin = params.rightMargin = DispUtil.dipToPx(context,30);
                params.gravity = Gravity.CENTER_VERTICAL;
                int padding = DispUtil.dipToPx(context,15f);
                view.setPadding(padding,padding,padding,padding);
                view.setGravity(Gravity.CENTER);
                view.setTextColor(Color.parseColor("#333333"));
                view.setTextSize(DispUtil.sp2px(context,6f));
                view.setText(date);
                view.setLayoutParams(params);
                return view;
            default:
                break;
        }

        return null;
    }


    private ViewGroup createButton(Context context, int type){
        LayoutParams params  = new LayoutParams(
                LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER);
        ImageView button = new ImageView(context);
        switch (type){
            case TYPE_LEFE:
                button.setBackgroundResource(R.mipmap.icon_left);
                break;

            case TYPE_RIGHT:
                button.setBackgroundResource(R.mipmap.icon_right);
                break;

            default:
                break;
        }

        int padding = DispUtil.dipToPx(context,15f);
        params.leftMargin = params.rightMargin = params.topMargin = params.bottomMargin = padding;
        button.setLayoutParams(params);

        layout.addView(button);
        layout.setOnClickListener(v -> {
            if (onDateChoiceListener!=null) {
//                date = TextUtils.equals(date,TX_LAST_DAY)?DateUtil.scrollCalendarDate(DateUtil.getStringDate(),-1): date;
                if (type == TYPE_LEFE){
                    onDateChoiceListener.onPrevious(date);
                }else
                    onDateChoiceListener.onNext(date);

                setDate(date);
            }
        });
        return layout;
    }


    public void setDate(String date){
        this.date = date;
        centerText = TextUtils.equals(date,
                DateUtil.scrollCalendarDate(DateUtil.getStringDate(),- 1))?TX_LAST_DAY : dateSubs(date) + "夜间";
        if (date!=null) {
            view.setText(centerText);
        }
    }


    public String getDate() {
        return date;
    }


    private String dateSubs(String date){
        String result = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = format.parse(date);
            format = new SimpleDateFormat("MM/dd");
            result = format.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            result = date;
        }

        return result;
    }


    public interface OnDateChoiceListener{
//        void onChoice();

        void onPrevious(String date);

        void onNext(String date);
    }

    public void setOnDateChoiceListener(OnDateChoiceListener onDateChoiceListener) {
        this.onDateChoiceListener = onDateChoiceListener;
    }


}
