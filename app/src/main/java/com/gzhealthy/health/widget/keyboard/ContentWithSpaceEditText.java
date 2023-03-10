package com.gzhealthy.health.widget.keyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatEditText;

import com.gzhealthy.health.R;
import com.gzhealthy.health.widget.edittext.TextWatcherAdapter;

public class ContentWithSpaceEditText extends AppCompatEditText implements View.OnTouchListener,
        View.OnFocusChangeListener, TextWatcherAdapter.TextWatcherListener {
    public interface Listener {
        void didClearText();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private Drawable xD;
    private Listener listener;
    private OnTouchListener l;
    private OnFocusChangeListener f;
    public static final int TYPE_PHONE = 0;
    public static final int TYPE_BANK_CARD = 1;
    public static final int TYPE_ID_CARD = 2;
    private int start, count, before;
    private int contentType;
    private int maxLength = 50;
    private String digits;

    public ContentWithSpaceEditText(Context context) {
        this(context, null);
    }

    public ContentWithSpaceEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributeSet(context, attrs);
    }

    public ContentWithSpaceEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributeSet(context, attrs);
    }

    private void parseAttributeSet(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContentWithSpaceEditText, 0, 0);
        contentType = ta.getInt(R.styleable.ContentWithSpaceEditText_input_type, 0);
        // ????????????recycle
        ta.recycle();
        initType();
        setSingleLine();
//        addTextChangedListener(watcher);
        init();
    }

    private void initType() {
        if (contentType == TYPE_PHONE) {
            maxLength = 13;
            digits = "0123456789 ";
            setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (contentType == TYPE_BANK_CARD) {
            maxLength = 31;
            digits = "0123456789 ";
            setInputType(InputType.TYPE_CLASS_NUMBER);
        } else if (contentType == TYPE_ID_CARD) {
            maxLength = 21;
            digits = null;
            setInputType(InputType.TYPE_CLASS_TEXT);
        }
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    @Override
    public void setInputType(int type) {
        if (contentType == TYPE_PHONE || contentType == TYPE_BANK_CARD) {
            type = InputType.TYPE_CLASS_NUMBER;
        } else if (contentType == TYPE_ID_CARD) {
            type = InputType.TYPE_CLASS_TEXT;
        }
        super.setInputType(type);
        /* ????????????:setKeyListener??????setInputType??????????????????????????????*/
        if (!TextUtils.isEmpty(digits)) {
            setKeyListener(DigitsKeyListener.getInstance(digits));
        }
    }

    /**
     * ?????????????????????
     *
     * @param contentType ??????
     */
    public void setContentType(int contentType) {
        this.contentType = contentType;
        initType();
    }

    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            ContentWithSpaceEditText.this.start = start;
            ContentWithSpaceEditText.this.before = before;
            ContentWithSpaceEditText.this.count = count;
            setClearIconVisible(!TextUtils.isEmpty(s));
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s == null) {
                return;
            }
            //???????????????????????????????????????????????????
            boolean isMiddle = (start + count) < (s.length());
            //?????????????????????????????????????????????
            boolean isNeedSpace = false;
            if (!isMiddle && isSpace(s.length())) {
                isNeedSpace = true;
            }
            if (isMiddle || isNeedSpace || count > 1) {
                String newStr = s.toString();
                newStr = newStr.replace(" ", "");
                StringBuilder sb = new StringBuilder();
                int spaceCount = 0;
                for (int i = 0; i < newStr.length(); i++) {
                    sb.append(newStr.substring(i, i + 1));
                    //?????????????????????????????????????????????(i+1+1+spaceCount)?????????i??????0??????????????????????????????????????????????????????1
                    if (isSpace(i + 2 + spaceCount)) {
                        sb.append(" ");
                        spaceCount += 1;
                    }
                }
                removeTextChangedListener(watcher);
                s.replace(0, s.length(), sb);
                //????????????????????????,??????????????????????????????????????????????????????????????????
                if (!isMiddle || count > 1) {
                    setSelection(s.length() <= maxLength ? s.length() : maxLength);
                } else {
                    //???????????????
                    if (count == 0) {
                        //??????????????????????????????????????????????????????????????????????????????
                        if (isSpace(start - before + 1)) {
                            setSelection((start - before) > 0 ? start - before : 0);
                        } else {
                            setSelection((start - before + 1) > s.length() ? s.length() : (start - before + 1));
                        }
                    }
                    //???????????????
                    else {
                        if (isSpace(start - before + count)) {
                            setSelection((start + count - before + 1) < s.length() ? (start + count - before + 1) : s.length());
                        } else {
                            setSelection(start + count - before);
                        }
                    }
                }
                addTextChangedListener(watcher);
            }
        }
    };

    public String getTextWithoutSpace() {
        return super.getText().toString().replace(" ", "");
    }

    public boolean checkTextRight() {
        String text = getTextWithoutSpace();
        //?????????????????????????????????
        if (contentType == TYPE_PHONE) {
            if (TextUtils.isEmpty(text)) {
                showShort(getContext(), "???????????????????????????????????????????????????");
            } else if (text.length() < 11) {
                showShort(getContext(), "???????????????11?????????????????????????????????");
            } else {
                return true;
            }
        } else if (contentType == TYPE_BANK_CARD) {
            if (TextUtils.isEmpty(text)) {
                showShort(getContext(), "?????????????????????????????????????????????????????????");
            } else if (text.length() < 14) {
                showShort(getContext(), "????????????????????????????????????????????????????????????");
            } else {
                return true;
            }
        } else if (contentType == TYPE_ID_CARD) {
            if (TextUtils.isEmpty(text)) {
                showShort(getContext(), "?????????????????????????????????????????????????????????");
            } else if (text.length() < 18) {
                showShort(getContext(), "??????????????????????????????????????????????????????");
            } else {
                return true;
            }
        }
        return false;
    }

    private void showShort(Context context, String msg) {
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private boolean isSpace(int length) {
        if (contentType == TYPE_PHONE) {
//            return isSpacePhone(length);//???????????????????????????????????????
        } else if (contentType == TYPE_BANK_CARD) {
            return isSpaceCard(length);
        } else if (contentType == TYPE_ID_CARD) {
//            return isSpaceIDCard(length);//??????????????????????????????????????????
        }
        return false;
    }

    private boolean isSpacePhone(int length) {
        return length >= 4 && (length == 4 || (length + 1) % 5 == 0);
    }

    private boolean isSpaceCard(int length) {
        return length % 5 == 0;
    }

    private boolean isSpaceIDCard(int length) {
        return length > 6 && (length == 7 || (length - 2) % 5 == 0);
    }

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
        addTextChangedListener(watcher);
//        addTextChangedListener(new TextWatcherAdapter(this, this));
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable x = visible ? xD : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], x, getCompoundDrawables()[3]);
    }
}
