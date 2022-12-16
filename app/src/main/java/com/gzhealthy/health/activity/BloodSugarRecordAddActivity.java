package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.BuildConfig;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.SosMessageAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.model.SosListModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.DateUtil;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.PickerViewUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.BloodSugarRecordDateDialog;
import com.gzhealthy.health.widget.BloodSugarValuePicker;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 血糖记录添加
 */
public class BloodSugarRecordAddActivity extends BaseAct{

    @BindView(R.id.tv_date)
    TextView tv_date;

    @BindView(R.id.tv_mmol)
    TextView tv_mmol;

    @BindView(R.id.type_after)
    TextView type_after;

    @BindView(R.id.type_front)
    TextView type_front;

    @BindView(R.id.type_special_after)
    TextView type_special_after;

    @BindView(R.id.type_special_front)
    TextView type_special_front;

    @BindView(R.id.decorview)
    FrameLayout decorview;

    @BindView(R.id.sugar_commit)
    Button sugar_commit;

    TimePickerView pvTime;

    BloodSugarValuePicker valuePicker;

    int type = -1;

    String value;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bloodsugar_record_add;
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, BloodSugarRecordAddActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("血糖");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        initView();
    }


    public void initView(){
        String date = DateUtil.getStringDateNow("yyyy.M.dd HH:mm");
        tv_date.setText(date);
        tv_date.setOnClickListener(this::onClick);
        type_after.setOnClickListener(this::onClick);
        type_front.setOnClickListener(this::onClick);
        type_special_after.setOnClickListener(this::onClick);
        type_special_front.setOnClickListener(this::onClick);
        tv_mmol.setOnClickListener(this::onClick);
        sugar_commit.setOnClickListener(this::onClick);

        initTimePicker();

        valuePicker = new BloodSugarValuePicker(this, value -> {
            tv_mmol.setText(value);
            this.value = value;
        });

        value = "";
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch (view.getId()){
            case R.id.tv_date:
                if (!pvTime.isShowing())
                    pvTime.show();
                break;

            case R.id.tv_mmol:
                valuePicker.showPicker();
                break;

            case R.id.sugar_commit:
                if (type < 0){
                    ToastUtil.showToast("请选择类型");
                    return;
                }else if (TextUtils.isEmpty(value)){
                    ToastUtil.showToast("请输入血糖值");
                    return;
                }

                postSugar();
                break;

            default:
                judeTypeSelector(view);
                break;
        }
    }

    private void initTimePicker() {

        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, 1900);
        startDate.set(Calendar.MONTH, 0);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        Calendar selectData = Calendar.getInstance();
        selectData.set(Calendar.YEAR, selectData.get(Calendar.YEAR));
        selectData.set(Calendar.MONTH, selectData.get(Calendar.MONTH));
        selectData.set(Calendar.DAY_OF_MONTH, selectData.get(Calendar.DAY_OF_MONTH));

        pvTime = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.dd HH:mm");
            tv_date.setText(dateFormat.format(date));
        }).setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "")
                .isCenterLabel(false)
                .setDividerColor(ContextCompat.getColor(this, R.color.text1))
                .setDividerType(WheelView.DividerType.FILL)
                .setContentSize(18)
                .setRangDate(startDate,selectData)
                .setDate(selectData)
                .gravity(Gravity.CENTER)
                .setTitleText("日期和时间")
                .setTitleBgColor(ContextCompat.getColor(this, R.color.white))
                .setSubmitColor(ContextCompat.getColor(this, R.color.global_333333))
                .setCancelColor(ContextCompat.getColor(this, R.color.global_999999))
                .build();
    }


    public void postSugar(){
        Map<String,String> param = new HashMap<>();
        param.put("type",String.valueOf(type));

        String date = tv_date.getText().toString();
        date = date.replace(".","-");
        param.put("dateTime", date);

        double sugar = 0;
        try {
            sugar = Double.parseDouble(tv_mmol.getText().toString());
        }catch (Exception e){
            e.printStackTrace();
            sugar = 0;
        }
        param.put("bloodSugar",String.valueOf(sugar));

        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().uploadBloodSugar(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onStart() {
                        showWaitDialog();
                        super.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        hideWaitDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                    }

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1){
                            goBack();
                        }
                        ToastUtil.showToast(data.msg);
                    }
                });
    }


    public void judeTypeSelector(View view){
        type_after.setSelected(false);
        type_front.setSelected(false);
        type_special_after.setSelected(false);
        type_special_front.setSelected(false);
        switch (view.getId()){
            case R.id.type_after:
                type = 1;
                type_after.setSelected(true);
                break;

            case R.id.type_front:
                type = 0;
                type_front.setSelected(true);
                break;

            case R.id.type_special_after:
                type = 3;
                type_special_after.setSelected(true);
                break;

            case R.id.type_special_front:
                type = 2;
                type_special_front.setSelected(true);
                break;
        }
    }
}
