//package com.gzhealthy.health.widget;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.drawable.AnimationDrawable;
//import android.os.Bundle;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.gzhealthy.health.R;
//
//public class CustomProgressDialog extends ProgressDialog {
//
//    private AnimationDrawable animation;
//    private Context context;
//    private ImageView iv;
//    private String loadingTip;
//    private TextView tvLoading;
//    private int resId;
//
//    public CustomProgressDialog(Context context, String content, int id) {
//        super(context);
//        this.context = context;
//        this.loadingTip = content;
//        this.resId = id;
//        setCanceledOnTouchOutside(true);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//    }
//
//    private void initData() {
//        iv.setBackgroundResource(resId);
//        animation = (AnimationDrawable) iv.getBackground();
//        iv.post(new Runnable() {
//            @Override
//            public void run() {
//                animation.start();
//
//            }
//        });
//        tvLoading.setText(loadingTip);
//
//    }
//
//    public void setContent(String str) {
//        tvLoading.setText(str);
//    }
//
//    private void initView() {
//        setContentView(R.layout.dialog_wait);
//        tvLoading = (TextView) findViewById(R.id.loadingTv);
//        iv = (ImageView) findViewById(R.id.loadingIv);
//    }
//
//	/*@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		// TODO Auto-generated method stub
//		animation.start();
//		super.onWindowFocusChanged(hasFocus);
//	}*/
//}
