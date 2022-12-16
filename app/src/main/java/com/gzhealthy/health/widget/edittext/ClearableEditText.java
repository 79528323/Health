package com.gzhealthy.health.widget.edittext;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * To change clear icon, set
 * <p/>
 * <pre>
 * android:drawableRight="@drawable/custom_icon"
 * </pre>
 */
public class ClearableEditText extends AppCompatEditText implements OnTouchListener,
        OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {
    public interface Listener {
        void didClearText();
    }

    public int lengh = 100;

    public void setLengh(int lengh) {
        this.lengh = lengh;
        setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(lengh)});
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;

    public ClearableEditText(Context context) {
        super(context);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.l = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.f = f;
    }

    private OnTouchListener l;
    private OnFocusChangeListener f;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tappedX = event.getX() > (getWidth() - getPaddingRight() - xD
                    .getIntrinsicWidth());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (listener != null) {
                        listener.didClearText();
                    }
                }
                return true;
            }
        }
        if (l != null) {
            return l.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (f != null) {
            f.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public void onTextChanged(EditText view, String text) {
        if (isFocused()) {
            setClearIconVisible(!TextUtils.isEmpty(text));
        }
    }

    private void init() {
        xD = getCompoundDrawables()[2];
        if (xD == null) {
            xD = getResources().getDrawable(R.mipmap.ic_login_delete_bg);
        }
        xD.setBounds(0, 0, xD.getIntrinsicWidth(), xD.getIntrinsicHeight());
        setClearIconVisible(false);
        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
//        addTextChangedListener(new TextWatcherAdapter(this, this));
        setFilters(new InputFilter[]{inputFilter, new InputFilter.LengthFilter(lengh)});
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }

    InputFilter inputFilter = new InputFilter() {

        Pattern pattern = Pattern.compile("[\\ud83c\\udc00-\\ud83c\\udfff]|[\\ud83d\\udc00-\\ud83d\\udfff]|[\\u2600-\\u27ff]");

        @Override
        public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
            Matcher matcher = pattern.matcher(charSequence);
            if (!matcher.find()) {
                return null;
            } else {
                ToastUtils.showShort("输入内容不能包含表情字符");
                return "";
            }
        }
    };
}
