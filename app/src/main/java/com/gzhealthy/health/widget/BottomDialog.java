package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.UiThread;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.TDevice;


public class BottomDialog {
    protected final Builder mBuilder;

    public final Builder getBuilder() {
        return mBuilder;
    }

    protected BottomDialog(Builder builder) {
        mBuilder = builder;
        mBuilder.bottomDialog = initBottomDialog(builder);
    }

    @UiThread
    public void show() {
        if (mBuilder != null && mBuilder.bottomDialog != null)
            mBuilder.bottomDialog.show();
    }

    @UiThread
    public void dismiss() {
        if (mBuilder != null && mBuilder.bottomDialog != null)
            mBuilder.bottomDialog.dismiss();
    }

    @UiThread
    private Dialog initBottomDialog(final Builder builder) {
        final Dialog bottomDialog = new Dialog(builder.context, R.style.BottomDialogs);
        View view = LayoutInflater.from(builder.context).inflate(R.layout.bottom_dialog, null);

        RelativeLayout vTopTitle = (RelativeLayout) view.findViewById(R.id.top_title_ll);
        TextView vNegative = (TextView) view.findViewById(R.id.bottomDialog_cancel);
        TextView vTitle = (TextView) view.findViewById(R.id.bottomDialog_title);
        TextView vPositive = (TextView) view.findViewById(R.id.bottomDialog_ok);
        TextView vContent = (TextView) view.findViewById(R.id.bottomDialog_content);
        FrameLayout vCustomView = (FrameLayout) view.findViewById(R.id.bottomDialog_custom_view);

        if (builder.title != null && !TextUtils.isEmpty(builder.title)) {
            vTopTitle.setVisibility(View.VISIBLE);
            vTitle.setVisibility(View.VISIBLE);
            vTitle.setText(builder.title);
        } else {
            vTopTitle.setVisibility(View.GONE);
            vTitle.setVisibility(View.GONE);
        }

        if (builder.content != null) {
            vContent.setVisibility(View.VISIBLE);
            vContent.setText(builder.content);
        } else {
            vContent.setVisibility(View.GONE);
        }

        if (builder.customView != null) {
            vCustomView.setVisibility(View.VISIBLE);
            if (builder.customView.getParent() != null)
                ((ViewGroup) builder.customView.getParent()).removeAllViews();
            vCustomView.addView(builder.customView);
            vCustomView.setPadding(builder.customViewPaddingLeft, builder.customViewPaddingTop, builder.customViewPaddingRight, builder.customViewPaddingBottom);
        } else {
            vCustomView.setVisibility(View.GONE);
        }


        if (builder.negativeName != null) {
            vNegative.setVisibility(View.VISIBLE);
            vNegative.setText(builder.negativeName);
            vNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (builder.btn_negative_callback != null)
                        builder.btn_negative_callback.onClick(BottomDialog.this);
                    if (builder.isAutoDismiss)
                        bottomDialog.dismiss();
                }
            });
        }

        if (builder.positiveName != null) {
            vPositive.setVisibility(View.VISIBLE);
            vPositive.setText(builder.positiveName);
            vPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (builder.btn_positive_callback != null)
                        builder.btn_positive_callback.onClick(BottomDialog.this);
                    if (builder.isAutoDismiss)
                        bottomDialog.dismiss();
                }
            });
        }

        if (builder.title != null) {
            vTitle.setVisibility(View.VISIBLE);
            vTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (builder.btn_tile_callback != null)
                        builder.btn_tile_callback.onClick(BottomDialog.this);
//                    if (builder.isAutoDismiss)
//                        bottomDialog.dismiss();
                }
            });
        }

        bottomDialog.setOnDismissListener(builder.onDismissListener);
        bottomDialog.setContentView(view);
        bottomDialog.setCancelable(builder.isCancelable);
        bottomDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);

        return bottomDialog;
    }

    public static class Builder {
        protected Context context;

        // Bottom Dialog
        public Dialog bottomDialog;

        // Title and Content
        protected CharSequence title, content, negativeName, positiveName;


        // Buttons
        protected ButtonCallback btn_negative_callback, btn_positive_callback, btn_tile_callback;
        protected boolean isAutoDismiss;
        private DialogInterface.OnDismissListener onDismissListener;


        // Custom View
        protected View customView;
        protected int customViewPaddingLeft, customViewPaddingTop, customViewPaddingRight, customViewPaddingBottom;

        // Other options
        protected boolean isCancelable;

        public Builder(@NonNull Context context) {
            this.context = context;
            this.isCancelable = true;
            this.isAutoDismiss = true;
        }


        public Builder setNegativeName(@StringRes int negativeName) {
            setTitle(this.context.getString(negativeName));
            return this;
        }

        public Builder setNegativeName(@NonNull CharSequence negativeName) {
            this.negativeName = negativeName;
            return this;
        }

        public Builder setTitle(@StringRes int titleRes) {
            setTitle(this.context.getString(titleRes));
            return this;
        }

        public Builder setTitle(@NonNull CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveName(@StringRes int positiveName) {
            setTitle(this.context.getString(positiveName));
            return this;
        }

        public Builder setPositiveName(@NonNull CharSequence positiveName) {
            this.positiveName = positiveName;
            return this;
        }

        public Builder setContent(@StringRes int contentRes) {
            setContent(this.context.getString(contentRes));
            return this;
        }

        public Builder setContent(@NonNull CharSequence content) {
            this.content = content;
            return this;
        }


        public Builder onPositive(@NonNull ButtonCallback buttonCallback) {
            this.btn_positive_callback = buttonCallback;
            return this;
        }


        public Builder onNegative(@NonNull ButtonCallback buttonCallback) {
            this.btn_negative_callback = buttonCallback;
            return this;
        }

        public Builder onTitleClick(@NonNull ButtonCallback buttonCallback) {
            this.btn_tile_callback = buttonCallback;
            return this;
        }

        public Builder onDissmissClick(@NonNull DialogInterface.OnDismissListener buttonCallback) {
            this.onDismissListener = buttonCallback;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.isCancelable = cancelable;
            return this;
        }

        public Builder autoDismiss(boolean autodismiss) {
            this.isAutoDismiss = autodismiss;
            return this;
        }

        public Builder setCustomView(View customView) {
            this.customView = customView;
            this.customViewPaddingLeft = 0;
            this.customViewPaddingRight = 0;
            this.customViewPaddingTop = 0;
            this.customViewPaddingBottom = 0;
            return this;
        }

        public Builder setCustomView(View customView, int left, int top, int right, int bottom) {
            this.customView = customView;
            this.customViewPaddingLeft = TDevice.dip2px(context, left);
            this.customViewPaddingRight = TDevice.dip2px(context, right);
            this.customViewPaddingTop = TDevice.dip2px(context, top);
            this.customViewPaddingBottom = TDevice.dip2px(context, bottom);
            return this;
        }

        @UiThread
        public BottomDialog build() {
            return new BottomDialog(this);
        }

        @UiThread
        public BottomDialog show() {
            BottomDialog bottomDialog = build();
            bottomDialog.show();
            return bottomDialog;
        }
    }

    public interface ButtonCallback {
        void onClick(@NonNull BottomDialog dialog);
    }
}
