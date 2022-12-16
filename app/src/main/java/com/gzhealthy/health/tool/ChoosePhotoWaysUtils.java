package com.gzhealthy.health.tool;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;


public class ChoosePhotoWaysUtils extends PopupWindow implements View.OnClickListener {

    public interface DialogOnItemClickListener {
        void onItemClickListener(View v, int position);
    }

    private Context context;
    private TextView tv_select_photos, tv_take_camera, tv_cancle;
    private DialogOnItemClickListener mDialogOnItemClickListener;
    private View view;

    public ChoosePhotoWaysUtils(Context context, DialogOnItemClickListener dialogOnItemClickListener) {
        super(context);
        this.context = context;
        this.mDialogOnItemClickListener = dialogOnItemClickListener;
        view = View.inflate(context, R.layout.dialog_choose_ways, null);
        tv_select_photos = (TextView) view.findViewById(R.id.tv_select_photos);
        tv_take_camera = (TextView) view.findViewById(R.id.tv_take_camera);
        tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);

        tv_select_photos.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_take_camera.setOnClickListener(this);

        //设置SelectPicPopupWindow的View
        this.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() ==R.id.tv_order_pfm){
//            ToastUtils.showShort("1");
//        }else
        if (view.getId() == R.id.tv_select_photos) {
            mDialogOnItemClickListener.onItemClickListener(view, 0);
        } else if (view.getId() == R.id.tv_take_camera) {
            mDialogOnItemClickListener.onItemClickListener(view, 1);
        }

        if (this.isShowing()) {
            this.dismiss();
        }
    }
}
