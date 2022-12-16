package com.gzhealthy.health.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.FenceSetting;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class SecurityFenceContactsAdapter extends BaseQuickAdapter<FenceSetting.DataBean.RemindPeopleBean, BaseViewHolder> {
    HashMap<Integer,Boolean> isChecked = new HashMap<>();
    public SecurityFenceContactsAdapter() {
        super(R.layout.item_fence_contact);
    }

    @Override
    public void setNewData(@Nullable List<FenceSetting.DataBean.RemindPeopleBean> data) {
        super.setNewData(data);
        for (int index=0;  index < data.size();  index++){
            isChecked.put(index,data.get(index).ifRemind==1?true:false);
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, FenceSetting.DataBean.RemindPeopleBean item) {
        Glide.with(mContext).load(item.memberAvatar).into((ImageView) helper.getView(R.id.user_icon));
        helper.setText(R.id.name,item.nickName);
        helper.setChecked(R.id.checked,isChecked.get(helper.getPosition()));
        helper.getView(R.id.checked).setOnClickListener(v -> {
            boolean check = !isChecked.get(helper.getPosition());
            isChecked.put(helper.getPosition(),check);
//            helper.setChecked(R.id.checked,isChecked.get(helper.getPosition()));
        });
    }


    public HashMap<Integer, Boolean> getIsChecked() {
        return isChecked;
    }
}
