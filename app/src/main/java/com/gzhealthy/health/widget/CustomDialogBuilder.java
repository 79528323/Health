package com.gzhealthy.health.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;

public class CustomDialogBuilder extends Dialog implements View.OnClickListener {
    private final String TAG = CustomDialogBuilder.class.getSimpleName();
    private LinearLayout llNavigate;
    private View vDivider;
    private final String defTextColor = "#FFFFFFFF";
    private FrameLayout flContent;
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvCancel;
    private TextView tvComfirm;
    private static int mOrientation = 1;
    private boolean isCancelable = true;
    private int windowWidth = 0;
    private int windowHeight = 0;
    private View.OnClickListener onAbolishListener;
    private View.OnClickListener onComfirmListener;
    private volatile static CustomDialogBuilder instance;
    private Context context;
    private RelativeLayout rl_dialog;
    private View view_line01, view_line02;
    private ImageView ivClose;
    private boolean isCancelDialog = false;
    private int mDuration = -1;

    public CustomDialogBuilder(Context context) {
        super(context);
        init(context);
    }

    public CustomDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    public CustomDialogBuilder(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((WindowManager.LayoutParams) params);

//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        //设置窗体的宽高来控制Dialog的大小
//        if (windowWidth == 0 || windowHeight == 0) {
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        } else {
//            params.width = windowWidth;
//            params.height = windowHeight;
//        }
//        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(isCancelable);
    }

    public static CustomDialogBuilder getInstance(Context context) {
        int ort = context.getResources().getConfiguration().orientation;
        if (mOrientation != ort) {
            mOrientation = ort;
            instance = null;
        }

        if (instance == null || ((Activity) context).isFinishing()) {
            synchronized (CustomDialogBuilder.class) {
                if (instance == null) {
                    instance = new CustomDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        return instance;
    }

    private void init(Context context) {
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        View root = getLayoutInflater().from(context).inflate(R.layout.dialog_custom, null);
        rl_dialog = (RelativeLayout) root.findViewById(R.id.rl_dialog);
        tvTitle = (TextView) root.findViewById(R.id.tv_custom_dialog_title);
        flContent = (FrameLayout) root.findViewById(R.id.fl_custom_conent);
        tvMessage = (TextView) root.findViewById(R.id.tv_custom_dialog_content);
        llNavigate = (LinearLayout) root.findViewById(R.id.ll_custom_dialog_navigate);
        tvCancel = (TextView) root.findViewById(R.id.tv_custom_dialog_cancel);
        view_line01 = (View) root.findViewById(R.id.view_line01);
        view_line02 = (View) root.findViewById(R.id.view_line02);
        tvCancel.setOnClickListener(this);
        tvComfirm = (TextView) root.findViewById(R.id.tv_custom_dialog_comfirm);
        tvComfirm.setOnClickListener(this);
        vDivider = root.findViewById(R.id.v_custom_dialog_divider);
        ivClose = (ImageView) root.findViewById(R.id.iv_dialog_close);
        setContentView(root);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        rl_dialog.setEnabled(false);
        rl_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
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

//    public CustomDialogBuilder setDialogWindows(int width, int height){
//        //设置窗体的宽高来控制Dialog的大小
//        windowWidth = width;
//        windowHeight = height;
//        return this;
//    }


    public CustomDialogBuilder withMessageMiss(int visibility) {
        tvMessage.setVisibility(visibility);
        return this;
    }

    public CustomDialogBuilder withTitle(CharSequence title) {
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

    public CustomDialogBuilder withCancelDialog(boolean isCancelDialog) {
        rl_dialog.setEnabled(isCancelDialog);
        return this;
    }


    public CustomDialogBuilder withTitleColor(String colorString) {
        tvTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }


    public CustomDialogBuilder withMessage(int textResId) {
        toggleView(tvMessage, textResId);
        tvMessage.setText(textResId);
        return this;
    }

    public CustomDialogBuilder withMessage(Spanned text) {
        if (text == null) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.VISIBLE);

        }
        tvMessage.setText(text);
        return this;
    }


    public CustomDialogBuilder withMessage(CharSequence msg) {
        toggleView(tvMessage, msg);
        tvMessage.setText(msg);
        return this;
    }


    public CustomDialogBuilder withMessageColor(String colorString) {
        tvMessage.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public CustomDialogBuilder withCancelText(CharSequence text, View.OnClickListener onCancelListener) {
        llNavigate.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        view_line02.setVisibility(View.VISIBLE);
        ifNeedDivider();
        if (!TextUtils.isEmpty(text))
            tvCancel.setText(text);
        this.onAbolishListener = onCancelListener;
        return this;
    }


    public CustomDialogBuilder withComfirmText(CharSequence text, View.OnClickListener onComfirmListener) {
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

    public CustomDialogBuilder withCloseImage() {
        ivClose.setVisibility(View.VISIBLE);
        return this;
    }


    public CustomDialogBuilder withCustomContentView(View view) {
        flContent.setVisibility(View.VISIBLE);
        if (flContent.getChildCount() > 0) {
            flContent.removeAllViews();
        }
        flContent.addView(view);

        return this;
    }

//    private void start(WithAnimotionType type) {
//        BaseEffects animator = type.getAnimator();
//        if (mDuration != -1) {
//            animator.setDuration(Math.abs(mDuration));
//        }
//        animator.start(rl_dialog);
//    }

    public CustomDialogBuilder isCancelableOnTouchOutside(boolean cancelable) {
        this.isCancelable = cancelable;
        this.setCanceledOnTouchOutside(cancelable);
        return this;
    }


    public CustomDialogBuilder isCancelable(boolean cancelable) {
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
}
