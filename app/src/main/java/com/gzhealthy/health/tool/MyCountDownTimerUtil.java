package com.gzhealthy.health.tool;


import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;


public class MyCountDownTimerUtil extends CountDownTimer {
    private TextView mbtn_check_code;

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MyCountDownTimerUtil(View btn_check_code, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mbtn_check_code = (TextView) btn_check_code;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        //防止计时过程中重复点击
        mbtn_check_code.setClickable(false);
        mbtn_check_code.setText(millisUntilFinished / 1000 + "s");
    }

    @Override
    public void onFinish() {
        //重新给Button设置文字
        mbtn_check_code.setText("重新获取验证码");
        //设置可点击
        mbtn_check_code.setClickable(true);
    }
}
