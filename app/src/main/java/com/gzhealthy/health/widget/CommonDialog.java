package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.TDevice;

public class CommonDialog extends Dialog {
    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommonDialog(Context context) {
        super(context);
    }

    public static final class Builder {
        protected Context context;
        protected Drawable icon;
        protected CharSequence title;
        protected CharSequence message;
        protected CharSequence positiveButtonText;
        protected CharSequence negativeButtonText;
        protected View contentView;
        protected boolean showTitle = true;
        protected OnClickListener positiveButtonClickListener;
        protected OnClickListener negativeButtonClickListener;

        private LayoutParams contentViewLayoutParam;
        private LayoutParams dialogLayoutParam;

        public Builder(Context context) {
            this.context = context;
        }

        public CommonDialog.Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public CommonDialog.Builder setMessage(int message) {
            this.message = (String) this.context.getText(message);
            return this;
        }

        public CommonDialog.Builder setTitle(int title) {
            this.title = (String) this.context.getText(title);
            return this;
        }

        public CommonDialog.Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public CommonDialog.Builder setIcon(int icon) {
            this.icon = this.context.getResources().getDrawable(icon);
            return this;
        }

        public CommonDialog.Builder setIcon(Drawable icon) {
            this.icon = icon;
            return this;
        }

        public CommonDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public CommonDialog.Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setPositiveButton(CharSequence positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommonDialog.Builder setNegativeButton(CharSequence negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setShowTitle(boolean showTitle) {
            this.showTitle = showTitle;
        }

        public void setContentViewLayoutParam(LayoutParams lp) {
            contentViewLayoutParam = lp;
        }

        public void setDialogLayoutParam(LayoutParams lp) {
            dialogLayoutParam = lp;
        }

        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog(context, R.style.Dialog);
//            dialog.getWindow().setWindowAnimations(R.style.mobo_dialog_animation_style);
            dialog.setContentView(R.layout.mobo_dialog_layout);
            ViewGroup layout = (ViewGroup) dialog.findViewById(R.id.common_dialog_layout);
            layout.setBackgroundResource(R.mipmap.common_dialog_bg);
            if (dialogLayoutParam == null) {
                dialogLayoutParam = new FrameLayout.LayoutParams((int) (TDevice.getScreenWidth(context) * 0.8f), LayoutParams.WRAP_CONTENT);
            }
            layout.setLayoutParams(dialogLayoutParam);

            // 标题 title
            if (icon != null) {
                ((ImageView) layout.findViewById(R.id.common_dialog_top_icon)).setImageDrawable(icon);
            } else {
                layout.findViewById(R.id.common_dialog_top_icon).setVisibility(View.GONE);
            }
            ((TextView) layout.findViewById(R.id.common_dialog_top_title)).setText(title);

            CharSequence dialogLeftBtnTxt = positiveButtonText;
            CharSequence dialogRightBtnTxt = negativeButtonText;
            if (getApiLev() >= 14) {
                dialogLeftBtnTxt = negativeButtonText;
                dialogRightBtnTxt = positiveButtonText;
            }
            final OnClickListener dialogLeftBtnClickListener =
                    getApiLev() >= 14 ? negativeButtonClickListener : positiveButtonClickListener;
            final OnClickListener dialogRightBtnClickListener =
                    getApiLev() >= 14 ? positiveButtonClickListener : negativeButtonClickListener;

            // 确定按钮
            if (dialogLeftBtnTxt != null) {

                ((TextView) layout.findViewById(R.id.common_dialog_left_button)).setText(dialogLeftBtnTxt);

                if (dialogLeftBtnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.common_dialog_left_button)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            dialogLeftBtnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_left_button).setVisibility(View.GONE);
            }
            //取消按钮
            if (dialogRightBtnTxt != null) {

                ((TextView) layout.findViewById(R.id.common_dialog_right_button)).setText(dialogRightBtnTxt);

                if (dialogRightBtnClickListener != null) {
                    ((TextView) layout.findViewById(R.id.common_dialog_right_button)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            dialogRightBtnClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            dialog.dismiss();
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.common_dialog_right_button).setVisibility(View.GONE);
            }

            TextView textView = (TextView) layout.findViewById(R.id.common_dialog_content);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
            // 消息 message
            if (message != null) {
                textView.setText(message);
            } else {
                textView.setVisibility(View.GONE);
            }

            if (contentView != null) {
                // 视图 contentView
                if (contentViewLayoutParam == null) {
                    contentViewLayoutParam = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                }
                ((LinearLayout) layout.findViewById(R.id.common_dialog_custom_view_layout)).addView(contentView, contentViewLayoutParam);
            } else {
                layout.findViewById(R.id.common_dialog_custom_view_layout).setVisibility(View.GONE);
            }

            return dialog;
        }

        private int getApiLev() {
            int apiLevel = 7;
            try {
                apiLevel = Build.VERSION.SDK_INT;
            } catch (Exception var2) {
                var2.printStackTrace();
            }
            return apiLevel;

        }

    }

    /**
     * 通用对话框
     *
     * @param ctx
     * @param title   标题
     * @param message 内容
     * @param ok      确定回调
     * @param cancle  取消回调
     * @return CommonDialog
     */
    public static CommonDialog getAlertDialog(Context ctx, int title, int message, final OnClickListener ok, final OnClickListener cancle) {
        return getAlertDialog(ctx, -1, ctx.getResources().getString(title), ctx.getResources().getString(message), ok, cancle);
    }

    /**
     * 通用对话框
     *
     * @param ctx
     * @param title   标题
     * @param message 内容
     * @param ok      确定回调
     * @param cancle  取消回调
     * @return CommonDialog
     */
    public static CommonDialog getAlertDialog(Context ctx, CharSequence title, CharSequence message, final OnClickListener ok, final OnClickListener cancle) {
        return getAlertDialog(ctx, -1, title, message, ok, cancle);
    }

    /**
     * 通用对话框
     *
     * @param ctx
     * @param icon    图标
     * @param title   标题
     * @param message 内容
     * @param ok      确定回调
     * @param cancle  取消回调
     * @return CommonDialog
     */
    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, final OnClickListener ok, final OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, ctx.getText(R.string.common_confirm), ctx.getText(R.string.common_cancel), ok, cancle);
    }

    /**
     * 通用对话框
     *
     * @param ctx
     * @param icon     图标
     * @param title    标题
     * @param message  内容
     * @param positive 确定按钮文字
     * @param negative 取消按钮文字
     * @param ok       确定回调
     * @param cancle   取消回调
     * @return CommonDialog
     */
    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence negative, final OnClickListener ok,
                                              final OnClickListener cancle) {
        return getAlertDialog(ctx, icon, title, message, null, positive, negative, ok, cancle);
    }


    public static CommonDialog getAlertDialog(Context ctx, int icon, CharSequence title,
                                              CharSequence message, View view, CharSequence positive, CharSequence negative,
                                              final OnClickListener ok, final OnClickListener cancle) {
        CommonDialog.Builder result = new CommonDialog.Builder(ctx);
        if (icon != -1)
            result.setIcon(icon);
        result.setTitle(title).setMessage(message).setContentView(view).setPositiveButton(positive, ok);
        if (cancle != null)
            result.setNegativeButton(negative, cancle);
        else
            result.setNegativeButton(negative, new OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
        return result.create();
    }

}
