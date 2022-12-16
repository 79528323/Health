package com.gzhealthy.health.widget.edittext;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherAdapter implements TextWatcher {

    public interface TextWatcherListener {

        void onTextChanged(EditText view, String text);

    }

    private final EditText view;
    private final TextWatcherListener listener;

    public TextWatcherAdapter(EditText editText, TextWatcherListener listener) {
        this.view = editText;
        this.listener = listener;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        listener.onTextChanged(view, s.toString());

        if (s.toString().contains(".")) {
            if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0,
                        s.toString().indexOf(".") + 3);
                view.setText(s);
                view.setSelection(s.length());
            }
        }

        //如果.在起始位置,则起始位置自动补0
        if (s.toString().trim().substring(0).equals(".")) {
            s = "0" + s;
            view.setText(s);
            view.setSelection(2);
        }

        //如果起始位置为0并且第二位跟的不是".",则无法后续输入
        if (s.toString().startsWith("0")
                && s.toString().trim().length() > 1) {
            if (!s.toString().substring(1, 2).equals(".")) {
                view.setText(s.subSequence(0, 1));
                view.setSelection(1);
                return;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // pass
    }

    @Override
    public void afterTextChanged(Editable s) {
        // pass
    }

}

