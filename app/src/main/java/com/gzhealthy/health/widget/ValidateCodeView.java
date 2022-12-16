package com.gzhealthy.health.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;

import com.gzhealthy.health.R;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created by Justin_Liu
 * on 2021/6/29
 * 自定义验证码
 */
public class ValidateCodeView extends AppCompatTextView {
    private int time = 60;

    private String text = "获取验证码";

    private CountHandler countHandler;

    private Context context;

    public ValidateCodeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        countHandler = new CountHandler(context);
        setText(text);
        setGravity(Gravity.CENTER);
        setTextColor(context.getColor(R.color.app_btn_parimary));
        setBackgroundResource(R.drawable.selector_btn_validate);
        setEnabled(true);
    }


    public void startCountTime(){
        setEnabled(false);
        countHandler.sendEmptyMessage(0);
    }



    class CountHandler extends Handler{
        String unit = "S";
        WeakReference<Context> reference;

        public CountHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (reference.get()!=null){
                if (time > 0){
                    setText(time + unit);
                    time--;
                    setTextColor(context.getColor(R.color.global_999999));
                    countHandler.sendEmptyMessageDelayed(0,1000);
                }else {
                    setTextColor(context.getColor(R.color.app_btn_parimary));
                    setText(text);
                    time = 60;
                    setEnabled(true);
                    invalidate();
                }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        context = null;
        super.finalize();
    }
}
