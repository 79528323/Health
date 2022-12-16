package com.gzhealthy.health.tool;


import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.HealthApp;


public class ToastUtils {

    private static Toast mToast;
    private static TextView tvMsg;
    private static Long lastTime = 0L;
    private static String lastMsg;

    public static void register(Context context) {
        if (mToast == null) {
            View root = View.inflate(context, R.layout.view_toast, null);
            tvMsg = (TextView) root.findViewById(R.id.tv_toast);
            mToast = new Toast(context);
            mToast.setView(root);
        }
    }

    private static void check() {
        if (HealthApp.getInstance() == null) {
            throw new NullPointerException(
                    "Must initial call ToastUtils.register(Context context) in your " +
                            "<? " +
                            "extends Application class>");
        }
    }

    public static void showLong(int textResId) {
        showLong(HealthApp.getInstance().getString(textResId));
    }

    public static void showLong(int textResId, int iconResId, int direction) {
        showLong(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }


    public static void showLong(String msg) {
        show(msg, true, -1, -1, 0);
    }

    public static void showLong(String msg, int iconResId, int direction) {
        show(msg, true, iconResId, direction, 0);
    }


    public static void showShort(int textResId) {
        showShort(HealthApp.getInstance().getString(textResId));
    }

    public static void showShort(int textResId, int iconResId, int direction) {
        showShort(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }

    public static void showShort(String msg) {
        show(msg, false, -1, -1, 0);
    }

    public static void showShort(String msg, int iconResId, int direction) {
        show(msg, false, iconResId, direction, 0);
    }


    public static void showSuccessLong(int textResId) {
        showSuccessLong(HealthApp.getInstance().getString(textResId));
    }

    public static void showSuccessLong(int textResId, int iconResId, int direction) {
        showSuccessLong(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }


    public static void showSuccessLong(String msg) {
        show(msg, true, -1, -1, 1);
    }

    public static void showSuccessLong(String msg, int iconResId, int direction) {
        show(msg, true, iconResId, direction, 1);
    }


    public static void showSuccessShort(int textResId) {
        showSuccessShort(HealthApp.getInstance().getString(textResId));
    }

    public static void showSuccessShort(int textResId, int iconResId, int direction) {
        showSuccessShort(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }

    public static void showSuccessShort(String msg) {
        show(msg, false, -1, -1, 1);
    }

    public static void showSuccessShort(String msg, int iconResId, int direction) {
        show(msg, false, iconResId, direction, 1);
    }


    public static void showErrorLong(int textResId) {
        showErrorLong(HealthApp.getInstance().getString(textResId));
    }

    public static void showErrorLong(int textResId, int iconResId, int direction) {
        showErrorLong(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }


    public static void showErrorLong(String msg) {
        show(msg, true, -1, -1, 2);
    }

    public static void showErrorLong(String msg, int iconResId, int direction) {
        show(msg, true, iconResId, direction, 2);
    }


    public static void showErrorShort(int textResId) {
        showErrorShort(HealthApp.getInstance().getString(textResId));
    }

    public static void showErrorShort(int textResId, int iconResId, int direction) {
        showErrorShort(HealthApp.getInstance().getString(textResId), iconResId, direction);
    }

    public static void showErrorShort(String msg) {
        show(msg, false, -1, -1, 2);
    }

    public static void showErrorShort(String msg, int iconResId, int direction) {
        show(msg, false, iconResId, direction, 2);
    }

    private static void show(String msg, boolean isLong, int iconResId, int direction, int status) {
        check();
        if (TextUtils.isEmpty(lastMsg)) {
            if (status == 0) {
                tvMsg.setTextColor(tvMsg.getResources().getColor(R.color.white));
            } else {
                tvMsg.setTextColor(status == 1 ? tvMsg.getResources().getColor(R.color.toast_success_color) : tvMsg.getResources().getColor(R.color.toast_error_color));
            }
            tvMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
            if (iconResId != -1) {
                switch (direction) {
                    case 0:
                        tvMsg.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
                        break;
                    case 1:
                        tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
                        break;
                    case 2:
                        tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
                        break;
                    case 3:
                        tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, iconResId);
                        break;
                }
            }
            mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            long currentTime = System.currentTimeMillis();
            if (!lastMsg.equalsIgnoreCase(msg) || (currentTime - lastTime) >= 3000) {
                if (status == 0) {
                    tvMsg.setTextColor(tvMsg.getResources().getColor(R.color.white));
                } else {
                    tvMsg.setTextColor(status == 1 ? tvMsg.getResources().getColor(R.color.toast_success_color) : tvMsg.getResources().getColor(R.color.toast_error_color));
                }
                tvMsg.setText(TextUtils.isEmpty(msg) ? "" : msg);
                if (iconResId != -1) {
                    switch (direction) {
                        case 0:
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
                            break;
                        case 1:
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, iconResId, 0, 0);
                            break;
                        case 2:
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, iconResId, 0);
                            break;
                        case 3:
                            tvMsg.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, iconResId);
                            break;
                    }
                }
                mToast.setDuration(isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                mToast.show();
                lastTime = System.currentTimeMillis();
                lastMsg = msg;
            }
        }
    }

}
