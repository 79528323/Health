package com.gzhealthy.health.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;

import java.util.ArrayList;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class VerificationCodeView extends LinearLayout {
    private ArrayList<String> codes = new ArrayList<>();

    private boolean block = false;

    private TextView tvFirst, tvSecond, tvThird, tvFourth, tvFifth, tvSixth;
    private EditText et;

    public VerificationCodeView(Context context) {
        this(context, null);
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_verification_code_view, this, true);

        tvFirst = view.findViewById(R.id.tvVerification1);
        tvSecond = view.findViewById(R.id.tvVerification2);
        tvThird = view.findViewById(R.id.tvVerification3);
        tvFourth = view.findViewById(R.id.tvVerification4);
        tvFifth = view.findViewById(R.id.tvVerification5);
        tvSixth = view.findViewById(R.id.tvVerification6);
        et = view.findViewById(R.id.etVerification);

        setTextViews();

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    et.setText("");
                    if (codes.size() < 6) {
                        codes.add(s.toString());
                        setTextViews();
                    }
                }
                block = codes.size() == 6;
            }
        });
        et.setOnKeyListener((v, keyCode, event) -> {

            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && codes.size() > 0) {
                codes.remove(codes.size() - 1);
                setTextViews();
                block = codes.size() == 6;
                return true;
            } else {
                return false;
            }
        });
    }

    private void setTextViews() {
        tvFirst.setText("");
        tvSecond.setText("");
        tvThird.setText("");
        tvFourth.setText("");
        tvFifth.setText("");
        tvSixth.setText("");
        if (codes.size() > 0) {
            tvFirst.setText(codes.get(0));
        }
        if (codes.size() > 1) {
            tvSecond.setText(codes.get(1));
        }
        if (codes.size() > 2) {
            tvThird.setText(codes.get(2));
        }
        if (codes.size() > 3) {
            tvFourth.setText(codes.get(3));
        }
        if (codes.size() > 4) {
            tvFifth.setText(codes.get(4));
        }
        if (codes.size() > 5) {
            tvSixth.setText(codes.get(5));
        }
    }

    public String getText() {
        if (codes.size() == 0) {
            return "";
        } else {
            String result = "";
            for (String code : codes) {
                result += code;
            }
            return result;
        }
    }

    public boolean getBlock() {
        return block;
    }

}
