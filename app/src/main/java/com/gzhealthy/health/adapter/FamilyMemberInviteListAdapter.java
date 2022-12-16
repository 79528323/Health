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
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.base.RxManager;
import com.gzhealthy.health.model.FamilyMember;
import com.gzhealthy.health.model.FamilyMemberInvite;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.protocol.LifeSubscription;
import com.gzhealthy.health.protocol.ResponseState;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

public class FamilyMemberInviteListAdapter extends BaseQuickAdapter<FamilyMemberInvite.DataBean, BaseViewHolder> {
    private Context context;
    private OnItemStatusSelectListener onItemStatusSelectListener;
    private LifeSubscription mLifeSubscription;
    private ResponseState mResponseState;

    public FamilyMemberInviteListAdapter(Context context,LifeSubscription mLifeSubscription,ResponseState mResponseState) {
        super(R.layout.item_family_invited);
        this.context = context;
        this.mLifeSubscription = mLifeSubscription;
        this.mResponseState = mResponseState;
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyMemberInvite.DataBean item) {
        helper.itemView.findViewById(R.id.accept)
                .setOnClickListener(v -> {
                    item.ifAgree = 1;
                    inviteAgree(helper,item);
        });
        helper.itemView.findViewById(R.id.refused)
                .setOnClickListener(v -> {
                    item.ifAgree = 2;
                    inviteAgree(helper,item);
                });

        helper.setText(R.id.name,item.memberNickName);
        helper.setText(R.id.uid,"ID："+item.member);
        GlideUtils.CircleImage(this.context,item.memberAvatar,helper.itemView.findViewById(R.id.user_icon));
        setStatus(helper,item);
    }

    public void setOnItemStatusSelectListener(OnItemStatusSelectListener onItemStatusSelectListener) {
        this.onItemStatusSelectListener = onItemStatusSelectListener;
    }

    public interface OnItemStatusSelectListener{
        void refuseOraccept(int ItemId ,String member ,int agree);
    }


    public void setStatus(BaseViewHolder helper,FamilyMemberInvite.DataBean item){
        if (item.ifAgree == 0){
            helper.setVisible(R.id.layout,true);
            helper.setVisible(R.id.status,false);
        }else{
            helper.setVisible(R.id.layout,false);
            helper.setVisible(R.id.status,true);
            switch (item.ifAgree){
                case 1:
                    helper.setText(R.id.status,"已接受");
                    helper.setTextColor(R.id.status,Color.parseColor("#00D0B9"));
                    break;

                case 2:
                    helper.setText(R.id.status,"已拒绝");
                    helper.setTextColor(R.id.status,Color.parseColor("#999999"));
                    break;

                default:
                    helper.setText(R.id.status,"已删除");
                    helper.setTextColor(R.id.status,Color.parseColor("#999999"));
                    break;
            }
        }
    }


    public void inviteAgree(final BaseViewHolder helper,final FamilyMemberInvite.DataBean item){
        Map<String,String> param = new HashMap<>();
        param.put("id",String.valueOf(item.id));
        param.put("member",item.member);
        param.put("ifAgree",String.valueOf(item.ifAgree));
        HttpUtils.invoke(mLifeSubscription, mResponseState, InsuranceApiFactory.getmHomeApi().inviteIfAgree(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1){
                            setStatus(helper,item);
                            if (item.ifAgree == 1){
                                //接受邀请需要刷新家庭成员列表
                                RxBus.getInstance().postEmpty(RxEvent.ON_REFRESH_FAMILY_PERSON);
                            }
                            onItemStatusSelectListener.refuseOraccept(item.id,item.member,item.ifAgree);
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }
}
