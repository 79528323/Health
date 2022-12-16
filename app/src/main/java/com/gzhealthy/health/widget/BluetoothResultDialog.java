package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BlueToothListAdapter;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.widget.decoration.LineDividerItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class BluetoothResultDialog extends Dialog implements View.OnClickListener {
    Context context;

    ImageView close;

    OnShareResultCallBack onShareResultCallBack;

    TextView tx_rate1_desc;

    TextView tx_rate2_desc;

    TextView tx_rate3_desc;

    TextView desc;

    TextView tv_ra1 ,tv_ra2, tv_ra3;

    ProgressBar parbar1,parbar2,parbar3;

    TextView tv_weixin,tv_friends;

    String s1,s2,s3,min;

    int type;

    View mContentView;

    View shareView;

    public BluetoothResultDialog(Context context, OnShareResultCallBack onShareResultCallBack) {
        super(context);
        this.context = context;
        this.onShareResultCallBack = onShareResultCallBack;
        init(context);
    }

    private void init(Context context){
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_bluetooth_result,null);
        close = mContentView.findViewById(R.id.close);
        close.setOnClickListener(this);

        setContentView(mContentView);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        getWindow().setBackgroundDrawable(new ColorDrawable());
        getWindow().setGravity(Gravity.CENTER);
        getWindow().getAttributes().width = ScreenUtils.getScreenWidth()/10 * 7;
        getWindow().getAttributes().height = ScreenUtils.getScreenHeight() / 6 * 4;

        tv_ra1 = mContentView.findViewById(R.id.tv_ra1);
        tv_ra2 = mContentView.findViewById(R.id.tv_ra2);
        tv_ra3 = mContentView.findViewById(R.id.tv_ra3);
        parbar1 = mContentView.findViewById(R.id.parbar1);
        parbar2 = mContentView.findViewById(R.id.parbar2);
        parbar3 = mContentView.findViewById(R.id.parbar3);
        tx_rate1_desc = mContentView.findViewById(R.id.tx_rate1_desc);
        tx_rate2_desc = mContentView.findViewById(R.id.tx_rate2_desc);
        tx_rate3_desc = mContentView.findViewById(R.id.tx_rate3_desc);
        tv_friends = mContentView.findViewById(R.id.tv_friends);
        tv_friends.setOnClickListener(this::onClick);
        tv_weixin = mContentView.findViewById(R.id.tv_weixin);
        tv_weixin.setOnClickListener(this::onClick);
        desc = mContentView.findViewById(R.id.desc);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void setS1(String s1) {
        this.s1 = s1;
        tx_rate1_desc.setText("大肠杆茵"+s1+"%的危害");
        parbar1.setProgress(Integer.parseInt(s1));
        tv_ra1.setText(s1+"%");
    }

    public void setS2(String s2) {
        this.s2 = s2;
        tx_rate2_desc.setText("农药残留"+s2+"%的危害");
        parbar2.setProgress(Integer.parseInt(s2));
        tv_ra2.setText(s2+"%");
    }

    public void setS3(String s3) {
        this.s3 = s3;
        tx_rate3_desc.setText("农药残留"+s3+"%的危害");
        parbar3.setProgress(Integer.parseInt(s3));
        tv_ra3.setText(s3+"%");
    }

    public void setMin(String min) {
        this.min = min;
    }

    public void setType(int type) {
        this.type = type;
        String temp = "";
        switch (type){
            case 1:
                temp="餐具";
                break;
            case 2:
                temp="果类蔬菜";
                break;
            case 3:
                temp="叶类蔬菜";
                break;
            case 4:
                temp="五谷";
                break;
            case 5:
                temp="肉类";
                break;
        }
        String str = "本次"+temp+"消毒耗时"+min+"分钟,成功消除";
        buildSpan(str,desc);
    }

    public View getShareView() {
        shareView = LinearLayout.inflate(context,R.layout.layout_bluetooth_share_image,null);
        ((TextView)shareView.findViewById(R.id.tx_rate1_desc)).setText("大肠杆茵"+s1+"%的危害");
        ((TextView)shareView.findViewById(R.id.tx_rate2_desc)).setText("农药残留"+s2+"%的危害");
        ((TextView)shareView.findViewById(R.id.tx_rate3_desc)).setText("农药残留"+s3+"%的危害");
        ((TextView)shareView.findViewById(R.id.tv_ra1)).setText(s1+"%");
        ((TextView)shareView.findViewById(R.id.tv_ra2)).setText(s2+"%");
        ((TextView)shareView.findViewById(R.id.tv_ra3)).setText(s3+"%");
        ((ProgressBar)shareView.findViewById(R.id.parbar1)).setProgress(Integer.parseInt(s1));
        ((ProgressBar)shareView.findViewById(R.id.parbar2)).setProgress(Integer.parseInt(s2));
        ((ProgressBar)shareView.findViewById(R.id.parbar3)).setProgress(Integer.parseInt(s3));
        String temp = "";
        switch (type){
            case 1:
                temp="餐具";
                break;
            case 2:
                temp="果类蔬菜";
                break;
            case 3:
                temp="叶类蔬菜";
                break;
            case 4:
                temp="五谷";
                break;
            case 5:
                temp="肉类";
                break;
        }
        String desc = "本次"+temp+"消毒耗时"+min+"分钟,成功消除";
//        SpannableString spannableString = new SpannableString(desc);
//        int start = desc.indexOf("时")+1;
//        int end = desc.indexOf(",");
//        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB828")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new RelativeSizeSpan(1.3f),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////        spannableString.setSpan(new StyleSpan(Typeface.BOLD),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ((TextView)shareView.findViewById(R.id.desc)).setText(spannableString);
        TextView view = (TextView)shareView.findViewById(R.id.desc);
        buildSpan(desc,view);
        return shareView;
    }


    public void buildSpan(String str,TextView view){
//        String desc = "本次"+temp+"消毒耗时"+min+"分钟,成功消除";
        SpannableString spannableString = new SpannableString(str);
        int start = str.indexOf("时")+1;
        int end = str.indexOf(",");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB828")),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.3f),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        spannableString.setSpan(new StyleSpan(Typeface.BOLD),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.close:
                RxBus.getInstance().postEmpty(RxEvent.ON_BLUETOOTH_BREAK_READ_DATA_LOOP);
                dismiss();
                break;

            case R.id.tv_weixin:
                if (onShareResultCallBack!=null){
                    onShareResultCallBack.onShare(1);
                }
                break;

            case R.id.tv_friends:
                if (onShareResultCallBack!=null){
                    onShareResultCallBack.onShare(2);
                }
                break;
        }
    }


    public interface OnShareResultCallBack{
        void onShare(int selected);
    }
}
