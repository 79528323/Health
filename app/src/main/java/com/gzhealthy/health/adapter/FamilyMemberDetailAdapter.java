package com.gzhealthy.health.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.model.FamilyMemberDetail;
import com.gzhealthy.health.utils.DispUtil;

public class FamilyMemberDetailAdapter extends BaseQuickAdapter<FamilyMemberDetail.DataBean.HealthDataBean, BaseViewHolder> {
    private Context context;

    public FamilyMemberDetailAdapter(Context context) {
        super(R.layout.item_family_member);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyMemberDetail.DataBean.HealthDataBean item) {
        helper.setText(R.id.value, TextUtils.isEmpty(item.value)?"尚未检测":getSpannable(item.value));
        helper.setTextColor(R.id.value,
                TextUtils.isEmpty(item.value)? Color.parseColor("#999999") :Color.parseColor("#333333"));
        helper.setText(R.id.name,item.name);
        int res = 0;
        String units=null;
        switch (item.type){
            case 1:
                res = R.mipmap.icon_member_rate;
                units="次/分";
                break;

            case 2:
                res = R.mipmap.icon_member_pressure;
                units="mmHg";
                break;

            case 3:
                res = R.mipmap.icon_member_spo2;
                units="%";
                break;

            case 4:
                res = R.mipmap.icon_member_walk;
                units="步";
                break;

            case 5:
                res = R.mipmap.icon_member_sugar;
                units="mmol/L";
                break;

            default:
                res = R.mipmap.icon_member_temperature;
                units="℃";
                break;
        }
        helper.setBackgroundRes(R.id.icon,res);
        helper.setText(R.id.unit,units);
        helper.setGone(R.id.unit,!TextUtils.isEmpty(item.value));
    }


    public SpannableString getSpannable(String str){
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(
                DispUtil.sp2px(context,24)),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
