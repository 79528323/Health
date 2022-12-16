package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;

/**
 * 通用知道了弹窗
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class CommonKnowDialog extends Dialog {

    private TextView tvHint;
    private CommonKnowDialogCallBack callBack;

    public CommonKnowDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_common_know);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.TOP);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().y = ScreenUtils.getScreenHeight() / 4;

        tvHint = findViewById(R.id.tvHint);

        findViewById(R.id.tvKnow).setOnClickListener(v -> {
            dismiss();
            if (callBack != null) {
                callBack.know();
            }
        });
    }

    /**
     * 设置提示
     *
     * @param hint 提示语
     */
    public CommonKnowDialog setHint(String hint) {
        tvHint.setText(hint);
        return this;
    }

    /**
     * 设置提示
     *
     * @param hint 提示语
     */
    public CommonKnowDialog setHint(SpannableString hint) {
        tvHint.setText(hint);
        return this;
    }

    /**
     * 设置是否可以取消
     *
     * @param isCancelable
     * @return
     */
    public CommonKnowDialog setIsCancelable(boolean isCancelable) {
        setCancelable(isCancelable);
        setCanceledOnTouchOutside(isCancelable);
        return this;
    }

    /**
     * 设置回调
     *
     * @param callBack
     */
    public CommonKnowDialog setCommonKnowDialogCallBack(CommonKnowDialogCallBack callBack) {
        this.callBack = callBack;
        return this;
    }

    public interface CommonKnowDialogCallBack {
        void know();
    }

}
