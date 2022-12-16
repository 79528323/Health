package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.gzhealthy.health.R;

public class AddImgContentDialog extends Dialog implements View.OnClickListener {
    Addcontent addcontent;
    private final EditText editText;
    Context context;
    private final TextView tv_tip;

    public AddImgContentDialog(@NonNull Context context, Addcontent addcontent) {
        super(context);
        this.addcontent = addcontent;
        this.context = context;
        setContentView(R.layout.dialog_addimg_cont);
        editText = (EditText) findViewById(R.id.tv_content);
        findViewById(R.id.tv_cancle).setOnClickListener(this);
        findViewById(R.id.tv_sure).setOnClickListener(this);
        tv_tip = (TextView) findViewById(R.id.tv_tip);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_tip.setText(s.length() + "/150");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancle:
                AddImgContentDialog.this.dismiss();
                break;
            case R.id.tv_sure:
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(context, "请输入描述内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                addcontent.addcontent(editText.getText().toString());
                AddImgContentDialog.this.dismiss();
                break;

        }
    }

    public interface Addcontent {
        void addcontent(String context);
    }
}
