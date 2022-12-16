package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatSeekBar;

import com.bigkoo.pickerview.TimePickerView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.statusBar.StatusBarUtil;
import com.gzhealthy.health.utils.L;
import com.gzhealthy.health.widget.BottomDialog;
import com.gzhealthy.health.widget.CircleImageView;
import com.gzhealthy.health.widget.edittext.ClearableEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 绑定信息
 */
public class BindInfoActivity extends BaseAct {

    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
    @BindView(R.id.tv_body_height)
    TextView tv_body_height;
    @BindView(R.id.tv_body_weight)
    TextView tv_body_weight;
    @BindView(R.id.mRadioGroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.tv_sure)
    TextView tv_sure;

    private TimePickerView pvTime;

    private String name;
    private String birthday;
    private boolean isGender = true;

    private BottomDialog bodyHeightDialog;
    private BottomDialog nameDialog;

    private ClearableEditText inputName;

    private AppCompatSeekBar mSbBodyHeight;
    private TextView tv_height;
    private int bodyHeighMax = 250;
    private int bodyHeighMin = 50;
    private int bodyHeightValue;

    private BottomDialog bodyWeightDialog;
    private AppCompatSeekBar mSbBodyWeight;
    private TextView tv_weight;
    private int bodyWeightMax = 255;
    private int bodyWeightMin = 5;
    private int bodyWeightValue;

    public static void instance(Context context,String header) {
        Intent intent = new Intent(context, BindInfoActivity.class);
        intent.putExtra("HEADER",header);
        context.startActivity(intent);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bind_info;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
//        setstatueColor(R.color.white);
////        setBarLeftIcon(R.mipmap.login_back);
//        setIvLeftGone(true);
//        setTitle("绑定信息");
//        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
//        StatusBarUtil.StatusBarLightMode(BindInfoActivity.this, true);
//        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
//        getCenterTextView().setTextSize(18);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("绑定信息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        StatusBarUtil.StatusBarLightMode(this, true);
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        String header = getIntent().getStringExtra("HEADER");
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.header);
        GlideUtils.CircleImage(this,header,circleImageView);
        // 先把性别判断一下
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_boy) {
                    isGender = true;
                } else if (checkedId == R.id.rb_girl) {
                    isGender = false;
                }
            }
        });

        tv_name.addTextChangedListener(textWatcher);
        tv_birthday.addTextChangedListener(textWatcher);
        tv_body_height.addTextChangedListener(textWatcher);
        tv_body_weight.addTextChangedListener(textWatcher);

        rxManager.onRxEvent(RxEvent.FINISH_BINDINFO_ACTIVITY)
                .subscribe(o->{
                    finish();
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @OnClick({R.id.ll_name, R.id.ll_birthday, R.id.ll_body_height,
            R.id.ll_body_weight, R.id.rb_boy, R.id.rb_girl, R.id.tv_sure, R.id.tv_jump})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_name:   // 姓名
                setNameView();
                break;
            case R.id.ll_birthday:   // 生日
                initTimePicker();
                break;
            case R.id.ll_body_height:   // 身高
                setBodyHeightView();
                break;
            case R.id.ll_body_weight:   // 体重
                setBodyWeightView();
                break;
            case R.id.rb_boy:   // 性别：男
                break;
            case R.id.rb_girl:   // 性别：女
                break;
            case R.id.tv_sure:   // 下一步
                L.i("姓名：" + name);
                L.i("生日：" + birthday);
                L.i("身高：" + bodyHeightValue);
                L.i("体重：" + bodyWeightValue);
                L.i("性别：" + isGender);
                BindInfo2Activity.instance(this, name, birthday, bodyHeightValue + "",
                        bodyWeightValue + "", isGender, 1);
                break;
            case R.id.tv_jump:   // 跳过
                BindInfo2Activity.instance(this, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_submit:
                hiddenKeybroad(view);
                name = inputName.getText().toString().trim();
                if (nameDialog != null) {
                    nameDialog.dismiss();
                }
                tv_name.setText(name);
                break;
            case R.id.tv_cancle:
                if (nameDialog != null) {
                    nameDialog.dismiss();
                }
                break;
            case R.id.tv_body_submit:
                if (bodyHeightDialog != null) {
                    bodyHeightDialog.dismiss();
                }
                tv_body_height.setText(String.format("%s", bodyHeightValue + ""));
                break;
            case R.id.tv_body_cancle:
                if (bodyHeightDialog != null) {
                    bodyHeightDialog.dismiss();
                }
                break;
            case R.id.tv_body_weight_submit:
                if (bodyWeightDialog != null) {
                    bodyWeightDialog.dismiss();
                }
                tv_body_weight.setText(String.format("%s", bodyWeightValue + ""));
                break;
            case R.id.tv_body_weight_cancle:
                if (bodyWeightDialog != null) {
                    bodyWeightDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 时间选择框
     */
    private void initTimePicker() {
        if (pvTime == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.set(1900, 1, 1);
            Calendar endDate = Calendar.getInstance();
            Time t = new Time("GMT+8");
            t.setToNow();
            endDate.set(t.year, t.month, t.monthDay);
            pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    birthday = dateFormat.format(date);
//                    hashMap = new HashMap<>();
//                    hashMap.put("birthday", "" + dateFormat.format(date));
//                    upadatePersonInfo(hashMap);
                    tv_birthday.setText(birthday);
                }
            })
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("年", "月", "日", "时", "分", "秒")
                    .isCenterLabel(false)
                    .setDividerColor(Color.parseColor("#FFeeeeee"))
                    .setContentSize(21)
                    .setTitleText("选择出生日期")
                    .setDate(selectedDate)
                    .setRangDate(startDate, endDate)
                    .setDecorView(null)
                    .setSubmitColor(Color.parseColor("#FF22D393"))
                    .setCancelColor(Color.parseColor("#FF22D393"))
                    .build();
        }
        pvTime.show();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkState();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 查询所所有的编辑框是否为空
     */
    public void checkState() {

        if (!TextUtils.isEmpty(tv_name.getText().toString())
                && !TextUtils.isEmpty(tv_birthday.getText().toString())
                && !TextUtils.isEmpty(tv_body_height.getText().toString())
                && !TextUtils.isEmpty(tv_body_weight.getText().toString())
                && (bodyHeightValue != 0) && (bodyWeightValue != 0)) {
            tv_sure.setEnabled(true);
        } else {
            tv_sure.setEnabled(false);
        }
    }

    /**
     * 身高选择
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBodyHeightView() {
        if (bodyHeightDialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_body_height_layout, null);
            mSbBodyHeight = view.findViewById(R.id.sb_body_height);
            tv_height = view.findViewById(R.id.tv_height);
            mSbBodyHeight.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
            mSbBodyHeight.setMax(bodyHeighMax);
            mSbBodyHeight.setMin(bodyHeighMin);
            view.findViewById(R.id.tv_body_submit).setOnClickListener(this);
            view.findViewById(R.id.tv_body_cancle).setOnClickListener(this);
            bodyHeightDialog = new BottomDialog.Builder(BindInfoActivity.this).setCustomView(view).show();
        } else {
            bodyHeightDialog.show();
        }
    }

    /**
     * 体重选择
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setBodyWeightView() {
        if (bodyWeightDialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_body_weight_layout, null);
            mSbBodyWeight = view.findViewById(R.id.sb_body_weight);
            tv_weight = view.findViewById(R.id.tv_weight);
            mSbBodyWeight.setOnSeekBarChangeListener(mWeightOnSeekBarChangeListener);
            mSbBodyWeight.setMax(bodyWeightMax);
            mSbBodyWeight.setMin(bodyWeightMin);
            view.findViewById(R.id.tv_body_weight_submit).setOnClickListener(this);
            view.findViewById(R.id.tv_body_weight_cancle).setOnClickListener(this);
            bodyWeightDialog = new BottomDialog.Builder(BindInfoActivity.this).setCustomView(view).show();
        } else {
            bodyWeightDialog.show();
        }
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tv_height.setText(String.format("%scm", progress));
            bodyHeightValue = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private SeekBar.OnSeekBarChangeListener mWeightOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tv_weight.setText(String.format("%skg", progress));
            bodyWeightValue = progress;
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    /**
     * 姓名选择
     */
    private void setNameView() {
        if (nameDialog == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.pop_name_layout, null);
            inputName = view.findViewById(R.id.et_name);
            view.findViewById(R.id.tv_submit).setOnClickListener(this);
            view.findViewById(R.id.tv_cancle).setOnClickListener(this);
            nameDialog = new BottomDialog.Builder(BindInfoActivity.this).setCustomView(view).show();

        } else {
            nameDialog.show();
        }

    }

    public void hiddenKeybroad(View view){
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager!=null){
            manager.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
