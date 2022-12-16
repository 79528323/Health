package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.BloodSugarRecordAddActivity;
import com.gzhealthy.health.activity.BodyInfoActivity;
import com.gzhealthy.health.activity.HealthBodyInfoActivity;

/**
 * 首页弹窗
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class HomeDialog extends Dialog {
    private LinearLayout llLayout;
    private ImageView ivLogo;
    private TextView tvHint;
    private Button btTop, btBottom;

    /**
     * 获取血糖弹窗
     *
     * @param context
     * @param hint
     * @return
     */
    public static HomeDialog getBloodSugarDialog(@NonNull Context context, String hint) {
        HomeDialog dialog = new HomeDialog(context)
                .setHintText(hint)
                .setLayoutBackgroundResource(R.drawable.gradient_fffcef_ffffff_90_8)
                .setLogoImageResource(R.mipmap.dialog_home_blood_sugar)
                .setTopButtonText("去记录")
                .setTopButtonBackgroundResource(R.drawable.shape_ffb72a_36);
        dialog.setTopButtonTextListener(v -> {
            dialog.dismiss();
            context.startActivity(new Intent(context, BloodSugarRecordAddActivity.class));
        });
        return dialog;
    }

    /**
     * 获取体质弹窗
     *
     * @param context
     * @param hint
     * @return
     */
    public static HomeDialog getConstitutionDialog(@NonNull Context context, String hint) {
        HomeDialog dialog = new HomeDialog(context)
                .setHintText(hint)
                .setLayoutBackgroundResource(R.drawable.gradient_d7fcff_ffffff_90_8)
                .setLogoImageResource(R.mipmap.dialog_home_constitution)
                .setTopButtonText("去完成")
                .setTopButtonBackgroundResource(R.drawable.shape_00d0b9_36);
        dialog.setTopButtonTextListener(v -> {
            dialog.dismiss();
            context.startActivity(new Intent(context, BodyInfoActivity.class));
        });
        return dialog;
    }

    /**
     * 获取基础信息弹窗
     *
     * @param context
     * @param hint
     * @return
     */
    public static HomeDialog getBasicInfoDialog(@NonNull Context context, String hint) {
        HomeDialog dialog = new HomeDialog(context)
                .setHintText(hint)
                .setLayoutBackgroundResource(R.drawable.gradient_d7fcff_ffffff_90_8)
                .setLogoImageResource(R.mipmap.dialog_home_basic_info)
                .setTopButtonText("去完善")
                .setTopButtonBackgroundResource(R.drawable.shape_00d0b9_36);
        dialog.setTopButtonTextListener(v -> {
            dialog.dismiss();
            context.startActivity(new Intent(context, HealthBodyInfoActivity.class));
        });
        return dialog;
    }

    public HomeDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_home);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.TOP);
        getWindow().getAttributes().width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getAttributes().y = ScreenUtils.getScreenHeight() / 4;


        llLayout = findViewById(R.id.llLayout);
        ivLogo = findViewById(R.id.ivLogo);
        tvHint = findViewById(R.id.tvHint);
        btTop = findViewById(R.id.btTop);
        btBottom = findViewById(R.id.btBottom);

        btBottom.setOnClickListener(v -> dismiss());
    }


    /**
     * 设置布局背景
     *
     * @param res 背景资源
     * @return
     */
    public HomeDialog setLayoutBackgroundResource(int res) {
        llLayout.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置logo
     *
     * @param res 设置logo资源
     * @return
     */
    public HomeDialog setLogoImageResource(int res) {
        ivLogo.setImageResource(res);
        return this;
    }


    /**
     * 设置提示文本
     *
     * @param hint 提示信息
     * @return
     */
    public HomeDialog setHintText(String hint) {
        tvHint.setText(hint);
        return this;
    }


    /**
     * 设置上方按钮文本
     *
     * @param hint 文本信息
     * @return
     */
    public HomeDialog setTopButtonText(String hint) {
        btTop.setText(hint);
        return this;
    }

    /**
     * 设置上方按钮背景资源
     *
     * @param res 设置logo资源
     * @return
     */
    public HomeDialog setTopButtonBackgroundResource(int res) {
        btTop.setBackgroundResource(res);
        return this;
    }

    /**
     * 设置上方按钮监听
     *
     * @param onClickListener OnClickListener实例
     * @return
     */
    public HomeDialog setTopButtonTextListener(View.OnClickListener onClickListener) {
        btTop.setOnClickListener(onClickListener);
        return this;
    }


}
