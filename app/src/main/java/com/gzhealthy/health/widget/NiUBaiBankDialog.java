package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gzhealthy.health.R;

public class NiUBaiBankDialog extends Dialog {
    Context context;
    TextView tvTitle;
    TextView tvContent;
    TextView tvCancle;
    TextView tvSure;
    String title;
    String contennt;
    String lefttext;
    String righttext;
    View.OnClickListener leftOnClick;
    View.OnClickListener rightOnClick;


    public void setTitles(String title) {
        this.title = title;
    }

    public void setContennt(String contennt) {
        this.contennt = contennt;
    }

    public void setleftOnClick(String lefttext, View.OnClickListener yes) {
        this.lefttext = lefttext;
        this.leftOnClick = yes;
    }

    public void setrightOnClick(String righttext, View.OnClickListener yes) {
        this.righttext = righttext;
        this.rightOnClick = yes;
    }

    public NiUBaiBankDialog(@NonNull Context context) {
        super(context, R.style.BottomDialogs);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_but_check);
        initdialog();

    }

    public void initdialog() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvCancle = (TextView) findViewById(R.id.tv_cancle);
        tvSure = (TextView) findViewById(R.id.tv_sure);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
        if (!TextUtils.isEmpty(contennt)) {
            tvContent.setText(contennt);
        }

        if (!TextUtils.isEmpty(lefttext)) {
            tvCancle.setText(lefttext);
        }
        if (!TextUtils.isEmpty(righttext)) {
            tvSure.setText(righttext);
        }

        if (null == leftOnClick) {
            tvCancle.setVisibility(View.GONE);
            findViewById(R.id.div).setVisibility(View.GONE);
        } else {
            tvCancle.setOnClickListener(leftOnClick);
        }

        if (null == rightOnClick) {
            tvSure.setVisibility(View.GONE);
            findViewById(R.id.div).setVisibility(View.GONE);
        } else {
            tvSure.setOnClickListener(rightOnClick);
        }
    }

}
