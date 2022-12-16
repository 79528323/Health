package com.gzhealthy.health.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;

public class UpdateVersionDialogBuilder extends Dialog implements View.OnClickListener {

    private final String TAG = CustomDialogBuilder.class.getSimpleName();
    private LinearLayout llNavigate;
    private View vDivider;
    private final String defTextColor = "#FFFFFFFF";

    private FrameLayout flContent;

    private TextView tvTitle;

    private TextView tvMessage;


    private TextView tvCancel;

    private TextView tvComfirm;

    private int mDuration = -1;

    private static int mOrientation = 1;
    private boolean isCancelable = true;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private View.OnClickListener onAbolishListener;
    private View.OnClickListener onComfirmListener;
    private volatile static UpdateVersionDialogBuilder instance;
    private Context context;
    private boolean isUpdate = false;
    private View view_line01, view_line02;

    public UpdateVersionDialogBuilder(Context context) {
        super(context);
        init(context);
    }

    public UpdateVersionDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public UpdateVersionDialogBuilder(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置窗体的宽高来控制Dialog的大小
        if (windowWidth == 0 || windowHeight == 0) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            params.width = windowWidth;
            params.height = windowHeight;
        }
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(isCancelable);
    }

    public static UpdateVersionDialogBuilder getInstance(Context context) {

        int ort = context.getResources().getConfiguration().orientation;
        if (mOrientation != ort) {
            mOrientation = ort;
            instance = null;
        }

        if (instance == null || ((Activity) context).isFinishing()) {
            synchronized (UpdateVersionDialogBuilder.class) {
                if (instance == null) {
                    instance = new UpdateVersionDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        return instance;

    }

    private void init(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = getLayoutInflater().from(context).inflate(R.layout.dialog_custom1, null);
        tvTitle = (TextView) root.findViewById(R.id.tv_custom_dialog_title);
        flContent = (FrameLayout) root.findViewById(R.id.fl_custom_conent);
        tvMessage = (TextView) root.findViewById(R.id.tv_custom_dialog_content);
        llNavigate = (LinearLayout) root.findViewById(R.id.ll_custom_dialog_navigate);
        view_line01 = (View) root.findViewById(R.id.view_line01);
        tvCancel = (TextView) root.findViewById(R.id.tv_custom_dialog_cancel);
        tvCancel.setOnClickListener(this);
        tvComfirm = (TextView) root.findViewById(R.id.tv_custom_dialog_comfirm);
        tvComfirm.setOnClickListener(this);
        vDivider = root.findViewById(R.id.v_custom_dialog_divider);
        view_line02 = (View) root.findViewById(R.id.view_line02);
        setContentView(root);
    }

    public void setDialogDismiss() {
        instance = null;
    }

    public void toDefault() {
        tvMessage.setVisibility(View.VISIBLE);
    }

    private void ifNeedDivider() {
        if (tvCancel.getVisibility() == View.VISIBLE && tvComfirm.getVisibility() == View.VISIBLE)
            vDivider.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_custom_dialog_cancel && onAbolishListener != null) {
            onAbolishListener.onClick(v);
        } else if (v.getId() == R.id.tv_custom_dialog_comfirm && onComfirmListener != null) {
            onComfirmListener.onClick(v);
        }
    }

    public UpdateVersionDialogBuilder setDialogWindows(int width, int height) {
        //设置窗体的宽高来控制Dialog的大小
        windowWidth = width;
        windowHeight = height;
        return this;
    }


    public UpdateVersionDialogBuilder withMessageMiss(int visibility) {
        tvMessage.setVisibility(visibility);
        return this;
    }


    public UpdateVersionDialogBuilder withTitle(CharSequence title) {
        if (title == null) {
            tvTitle.setVisibility(View.GONE);
            view_line01.setVisibility(View.GONE);
        } else {
            tvTitle.setVisibility(View.VISIBLE);
            view_line01.setVisibility(View.VISIBLE);
        }
        tvTitle.setText(title);
        return this;
    }


    public UpdateVersionDialogBuilder withTitleColor(String colorString) {
        tvTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }


    public UpdateVersionDialogBuilder withMessage(int textResId) {
        toggleView(tvMessage, textResId);
        tvMessage.setText(textResId);
        return this;
    }


    public UpdateVersionDialogBuilder withMessage(CharSequence msg) {
        toggleView(tvMessage, msg);
        tvMessage.setText(msg);
        return this;
    }


    public UpdateVersionDialogBuilder withMessageColor(String colorString) {
        tvMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }


    public UpdateVersionDialogBuilder withDuration(int duration) {
        this.mDuration = duration;
        return this;
    }


    public UpdateVersionDialogBuilder withCancelText(CharSequence text, View.OnClickListener onCancelListener) {
        llNavigate.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        view_line02.setVisibility(View.VISIBLE);
        ifNeedDivider();
        if (!TextUtils.isEmpty(text))
            tvCancel.setText(text);
        this.onAbolishListener = onCancelListener;
        return this;
    }


    public UpdateVersionDialogBuilder withComfirmText(CharSequence text, View.OnClickListener onComfirmListener) {
        llNavigate.setVisibility(View.VISIBLE);
        tvComfirm.setVisibility(View.VISIBLE);
        view_line02.setVisibility(View.VISIBLE);
        ifNeedDivider();
        if (!TextUtils.isEmpty(text))
            tvComfirm.setText(text);
        this.onComfirmListener = onComfirmListener;
        tvComfirm.setText(text);
        return this;
    }


    public UpdateVersionDialogBuilder withCustomContentView(View view) {
        flContent.setVisibility(View.VISIBLE);
        if (flContent.getChildCount() > 0) {
            flContent.removeAllViews();
        }
        flContent.addView(view);

        return this;
    }


    public UpdateVersionDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }


    public UpdateVersionDialogBuilder isCancelable(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCancelable(cancelable);
        return this;
    }

    private void toggleView(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        setDialogDismiss();
    }

    public FrameLayout getCustomContentLayout() {
        return flContent;
    }


    public UpdateVersionDialogBuilder MandatoryUpdate(boolean isMandatoryUpdate) {
        isUpdate = isMandatoryUpdate;
        return this;
    }

    @Override
    public void onBackPressed() {
        if (isUpdate) {
            Intent intent = new Intent(Constants.INTENT_ACTION_EXIT_APP);
            context.sendBroadcast(intent);
        } else {
            super.onBackPressed();
        }
    }

}
