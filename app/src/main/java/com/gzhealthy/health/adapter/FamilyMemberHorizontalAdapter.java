package com.gzhealthy.health.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.CircleImageView;

import androidx.core.content.ContextCompat;

public class FamilyMemberHorizontalAdapter extends BaseQuickAdapter<FamilyMember.DataBeanX.DataBean, BaseViewHolder> {

    Context context;
    //当前选中DataBean ID  默认=0 为开始第一个
    int isSelectedId = 0;

    int zoomOffset = 0;
    int avatarWidth = 0;
    int avatarHeight = 0;

    public FamilyMemberHorizontalAdapter(Context context) {
        super(R.layout.layout_family_person_tab);
        this.context = context;
        zoomOffset = DispUtil.dp2px(context,10);
        avatarWidth = avatarHeight = DispUtil.dp2px(context,50);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyMember.DataBeanX.DataBean item) {
        helper.addOnClickListener(R.id.container);
        helper.setText(R.id.family_tab_name,item.memberNickName);
        CircleImageView avatar = helper.itemView.findViewById(R.id.user_icon);
        GlideUtils.CircleImage(this.context,item.memberAvatar,avatar);
        int id = item.id;
        helper.setTextColor(R.id.family_tab_name, ContextCompat.getColor(
                context,isSelectedId==id?R.color.colorPrimary : R.color.global_333333));
        ViewGroup.LayoutParams lp = avatar.getLayoutParams();
        if (isSelectedId==id){
            lp.width = lp.height = (avatarWidth + zoomOffset);
            helper.setBackgroundRes(R.id.user_icon_lay,R.drawable.member_img_bg);
        }else {
            lp.width = lp.height = avatarWidth;
            helper.setBackgroundRes(R.id.user_icon_lay,R.drawable.member_img_un_bg);
        }
        avatar.setLayoutParams(lp);
    }

    public void setIsSelectedId(int isSelectedId) {
        this.isSelectedId = isSelectedId;
        notifyDataSetChanged();
    }

    public SpannableString getSpannable(String str){
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new AbsoluteSizeSpan(
                DispUtil.sp2px(context,24)),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),0,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
}
