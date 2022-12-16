package com.gzhealthy.health.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.model.HealthyInfo;
import com.gzhealthy.health.utils.DispUtil;

import java.util.ArrayList;
import java.util.List;

public class HealthCardAdapter extends RecyclerView.Adapter<HealthCardAdapter.ViewHolder> {
    private Context mContext;
    public List<HealthyInfo.DataBean> mList = new ArrayList<>();
    public List<Integer> typeList;
    public View.OnClickListener onClickListener;
    public int spValue = 19;

    public HealthCardAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.onClickListener = onClickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_health_home_card_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        judgeHolderType(position, holder);
    }

    public void judgeHolderType(int position, ViewHolder holder) {
        // 1-心率  2-血压  3-睡眠  4-心电  5-血糖  6-血氧  7-BMI   8-体温
        int type = typeList.get(position);

        holder.cvLayout.setTag(type);
        holder.cvLayout.setOnClickListener(onClickListener);

        HealthyInfo.DataBean info = mList.get(position);

        holder.tvStatus.setVisibility(View.GONE);
        holder.tvHint.setVisibility(View.GONE);
        holder.llData.setVisibility(View.GONE);
        holder.tvEcg.setVisibility(View.GONE);
        holder.llSleepDeep.setVisibility(View.GONE);
        holder.llSleepShallow.setVisibility(View.GONE);
        switch (type) {
            case 1:
                holder.tvTitle.setText("心率");
                if (!TextUtils.isEmpty(info.rateStatus)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    if ("1".equals(info.rateStatus)) {
                        holder.tvStatus.setText("正常");
                        holder.tvStatus.setSelected(false);
                    } else if ("2".equals(info.rateStatus)) {
                        holder.tvStatus.setText("偏低");
                        holder.tvStatus.setSelected(true);
                    } else if ("3".equals(info.rateStatus)) {
                        holder.tvStatus.setText("偏高");
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformValue(String.valueOf(info.rate)));
                    holder.tvUnit.setText("次/分");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_heart_rate);
                break;

            case 2:
                holder.tvTitle.setText("血压");
                if (!TextUtils.isEmpty(info.bloodPressStatus)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(info.bloodPressStatusStr);
                    if ("1".equals(info.bloodPressStatus)) {
                        holder.tvStatus.setSelected(false);
                    } else {
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformBloodPressureOrSugar(info.high + "/" + info.low));
                    holder.tvUnit.setText("mmHg");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_blood_pressure);
                break;

            case 3:
                holder.tvTitle.setText("睡眠");
                if (!TextUtils.isEmpty(info.totalDeepDuration) || !TextUtils.isEmpty(info.totalLightDuration)) {
                    //已检测
                    int textColor = mContext.getColor(R.color.global_333333);
                    if (!TextUtils.isEmpty(info.totalDeepDuration)) {
                        //深睡眠
                        holder.llSleepDeep.setVisibility(View.VISIBLE);
                        holder.llSleepDeep.removeAllViews();

                        TextView tv0 = new TextView(mContext);
                        tv0.setTextColor(textColor);
                        tv0.setTextSize(14f);
                        tv0.setText("深：");

                        TextView tv1 = new TextView(mContext);
                        tv1.setTextColor(textColor);
                        tv1.setTextSize(14f);
//                        tv1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tv1.setText(reformSleep(info.totalDeepDuration));

                        holder.llSleepDeep.addView(tv0);
                        holder.llSleepDeep.addView(tv1);
                    }
                    if (!TextUtils.isEmpty(info.totalLightDuration)) {
                        //浅睡眠
                        holder.llSleepShallow.setVisibility(View.VISIBLE);
                        holder.llSleepShallow.removeAllViews();

                        TextView tv0 = new TextView(mContext);
                        tv0.setTextColor(textColor);
                        tv0.setTextSize(14f);
                        tv0.setText("浅：");

                        TextView tv1 = new TextView(mContext);
                        tv1.setTextColor(textColor);
                        tv1.setTextSize(14f);
//                        tv1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        tv1.setText(reformSleep(info.totalLightDuration));

                        holder.llSleepShallow.addView(tv0);
                        holder.llSleepShallow.addView(tv1);
                    }
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_sleeps);
                break;

            case 4:
                holder.tvTitle.setText("心电");
                if (!TextUtils.isEmpty(info.ecgStatus)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    if ("1".equals(info.ecgStatus)) {
                        holder.tvStatus.setText("正常");
                        holder.tvStatus.setSelected(false);
                    } else {
                        holder.tvStatus.setText("异常");
                        holder.tvStatus.setSelected(true);
                    }
                    holder.tvEcg.setVisibility(View.VISIBLE);
                    holder.tvEcg.setText(info.ecgResult);
                    holder.tvEcg.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    holder.tvEcg.setTextSize(13f);
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_ecg);
                break;

            case 5:
                holder.tvTitle.setText("血糖");
                if (!TextUtils.isEmpty(info.glu)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(info.gluStatusStr);
                    if (TextUtils.equals(info.gluStatusStr, "正常")) {
                        holder.tvStatus.setSelected(false);
                    } else {
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformBloodPressureOrSugar(info.glu));
//                    holder.tvData.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    holder.tvUnit.setText("mmol/L");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_blood_sugar);
                break;

            case 6:
                holder.tvTitle.setText("血氧");
                if (!TextUtils.isEmpty(info.spo2Status)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    if ("1".equals(info.spo2Status)) {
                        holder.tvStatus.setText("正常");
                        holder.tvStatus.setSelected(false);
                    } else {
                        holder.tvStatus.setText("偏低");
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformValue(String.valueOf(info.spo2)));
                    holder.tvUnit.setText("%");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_blood_oxygen);
                break;

            case 7:
                holder.tvTitle.setText("BMI");
                if (!TextUtils.isEmpty(info.bmiType)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    if ("fit".equalsIgnoreCase(info.bmiType)) {
                        holder.tvStatus.setText("标准");
                        holder.tvStatus.setSelected(false);
                    } else if ("thin".equalsIgnoreCase(info.bmiType)) {
                        holder.tvStatus.setText("偏瘦");
                        holder.tvStatus.setSelected(true);
                    } else if ("minFat".equalsIgnoreCase(info.bmiType)) {
                        holder.tvStatus.setText("微胖");
                        holder.tvStatus.setSelected(true);
                    } else if ("fat".equalsIgnoreCase(info.bmiType)) {
                        holder.tvStatus.setText("肥胖");
                        holder.tvStatus.setSelected(true);
                    } else if ("overFat".equalsIgnoreCase(info.bmiType)) {
                        holder.tvStatus.setText("过度肥胖");
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformValue(info.bmi));
                    holder.tvUnit.setText("kg/㎡");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_bmi);
                break;

            case 8:
                holder.tvTitle.setText("体温");
                if (!TextUtils.isEmpty(info.temperatureStatus)) {
                    //已检测
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    if ("1".equals(info.temperatureStatus)) {
                        holder.tvStatus.setText("正常");
                        holder.tvStatus.setSelected(false);
                    } else {
                        holder.tvStatus.setText("异常");
                        holder.tvStatus.setSelected(true);
                    }
                    holder.llData.setVisibility(View.VISIBLE);
                    holder.tvData.setText(reformValue(info.temperature));
                    holder.tvUnit.setText("℃");
                } else {
                    //未检测
                    holder.tvHint.setVisibility(View.VISIBLE);
                    holder.tvHint.setText("尚未测量");
                }
                holder.ivLogo.setImageResource(R.mipmap.icon_temperature);
                break;
        }
    }

    public void refreshData(List<HealthyInfo.DataBean> mList, List<Integer> typeList) {
        this.mList.clear();
        if (typeList != null && !typeList.isEmpty()) {
            this.typeList = typeList;
            if (mList != null && !mList.isEmpty()) {
                this.mList.addAll(mList);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle, tvStatus, tvHint;

        private LinearLayout llData;
        private TextView tvData, tvUnit;

        private TextView tvEcg;

        private LinearLayout llSleepDeep, llSleepShallow;

        private ImageView ivLogo;

        private CardView cvLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvHint = itemView.findViewById(R.id.tvHint);

            llData = itemView.findViewById(R.id.llData);
            tvData = itemView.findViewById(R.id.tvData);
            tvUnit = itemView.findViewById(R.id.tvUnit);

            tvEcg = itemView.findViewById(R.id.tvEcg);

            llSleepDeep = itemView.findViewById(R.id.llSleepDeep);
            llSleepShallow = itemView.findViewById(R.id.llSleepShallow);

            ivLogo = itemView.findViewById(R.id.ivLogo);
            cvLayout = itemView.findViewById(R.id.cvLayout);
        }
    }



    public SpannableStringBuilder reformSleep(String str){
        SpannableStringBuilder builder = null;
        int foreColor = Color.parseColor("#000000");
        try {
            builder = new SpannableStringBuilder(str);
            if (builder.length() <= 0) {
                return builder;
            }

            int size = DispUtil.sp2px(mContext,spValue);
            if (str.contains("小")&&str.contains("分")){
                //有小时和分钟
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(foreColor),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new AbsoluteSizeSpan(size),str.indexOf("时")+1,str.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(foreColor),str.indexOf("时")+1,str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD),str.indexOf("时")+1,str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else if (str.contains("小")){
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(foreColor),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD),0,str.indexOf("小"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }else {
                //剩下分钟
                builder.setSpan(new AbsoluteSizeSpan(size),0,str.indexOf("分"),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new ForegroundColorSpan(foreColor),0,str.indexOf("分"),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(new StyleSpan(Typeface.BOLD),0,str.indexOf("分"),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return builder;
    }



    public SpannableStringBuilder reformBloodPressureOrSugar(String str){
        SpannableStringBuilder builder = null;
        int foreColor = Color.parseColor("#000000");
        try {
            builder = new SpannableStringBuilder(str);
            if (builder.length() <= 0) {
                return builder;
            }

            int size = DispUtil.sp2px(mContext,spValue);
            String flag = "";
            int start , end;
            int flags =  Spannable.SPAN_EXCLUSIVE_EXCLUSIVE;
            if (str.contains("/") || str.contains("-")){
                if (str.contains("/"))
                    flag = "/";
                else
                    flag = "-";
                start = 0;
                end = str.indexOf(flag);
                builder.setSpan(new AbsoluteSizeSpan(size),start,end,flags);
                builder.setSpan(new ForegroundColorSpan(foreColor),start,end,flags);
                builder.setSpan(new StyleSpan(Typeface.BOLD),start,end,flags);

                start = str.indexOf(flag)+1;
                end = str.length();
                builder.setSpan(new AbsoluteSizeSpan(size),start,end,flags);
                builder.setSpan(new ForegroundColorSpan(foreColor),start,end,flags);
                builder.setSpan(new StyleSpan(Typeface.BOLD),start,end,flags);
            }else {
                start = 0;
                end = str.length();
                builder.setSpan(new AbsoluteSizeSpan(size),start,end,flags);
                builder.setSpan(new ForegroundColorSpan(foreColor),start,end,flags);
                builder.setSpan(new StyleSpan(Typeface.BOLD),start,end,flags);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return builder;
    }



    public SpannableStringBuilder reformValue(String str){
        SpannableStringBuilder builder = null;
        int foreColor = Color.parseColor("#000000");
        try {
            builder = new SpannableStringBuilder(str);
            if (builder.length() <= 0) {
                return builder;
            }

            int size = DispUtil.sp2px(mContext,spValue);
            builder.setSpan(new AbsoluteSizeSpan(size),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new ForegroundColorSpan(foreColor),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new StyleSpan(Typeface.BOLD),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return builder;
    }
}
