package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.gzhealthy.health.R;


public class ChangePriceDialog extends Dialog {
    ChangePrice changePrice;
    public boolean ischeck = false;
    private final EditText et_price;

    public ChangePriceDialog(@NonNull Context context, ChangePrice changePrice) {
        super(context);
        this.changePrice = changePrice;
        setContentView(R.layout.dialog_change_price);
        et_price = (EditText) findViewById(R.id.et_price);
        findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePriceDialog.this.dismiss();
            }
        });
        findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_price.getText().toString())) {
                    Toast.makeText(context, "请输入修改价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                ChangePriceDialog.this.dismiss();
                changePrice.iseffective(ischeck, et_price.getText().toString());
            }
        });
        AppCompatCheckBox ck_check = (AppCompatCheckBox) findViewById(R.id.ck_check);
        ck_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ischeck = isChecked;
            }
        });
    }

    public interface ChangePrice {
        void iseffective(boolean eff, String price);
    }
}
