package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.HealthClockAdapter;
import com.gzhealthy.health.model.HealthClockInfo;
import com.gzhealthy.health.utils.DispUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/23
 */
public class FamilyInvitePopupWindow extends PopupWindow implements View.OnClickListener {
    final int MAX_BAGE_COUNT = 99;

    TextView invite_friend;

    TextView new_invite;

    TextView bage;

    OnSelectInviteListener onSelectInviteListener;

    int bageCount = 199;

    public FamilyInvitePopupWindow(Context context,OnSelectInviteListener onSelectClockListener) {
        super(context);
        this.onSelectInviteListener = onSelectClockListener;
        init(context,bageCount);
    }

    private void init(Context context, int bageCount){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_family_invite,null);
        invite_friend = view.findViewById(R.id.invite_friend);
        invite_friend.setOnClickListener(this);
        new_invite = view.findViewById(R.id.new_invite);
        new_invite.setOnClickListener(this);
        bage = view.findViewById(R.id.bage);
        bage.setVisibility(bageCount>0?View.VISIBLE:View.INVISIBLE);
        bage.setText(String.valueOf(bageCount));

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        setWidth(DispUtil.dp2px(context,179));
//        setHeight(metrics.widthPixels/2);
        setContentView(view);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
    }

    public void setBageCount(int bageCount) {
        this.bageCount = bageCount;
        bage.setVisibility(bageCount>0?View.VISIBLE:View.INVISIBLE);
        bage.setText(bageCount > MAX_BAGE_COUNT ? MAX_BAGE_COUNT+"+" : String.valueOf(bageCount));
    }

//    public void setOnSelectClockListener(OnSelectInviteListener onSelectClockListener) {
//        this.onSelectInviteListener = onSelectClockListener;
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.invite_friend:
                this.onSelectInviteListener.onInvitefriend();
                dismiss();
                break;

            case R.id.new_invite:
                this.onSelectInviteListener.onNewInvite();
                dismiss();
                break;
        }
    }

    public interface OnSelectInviteListener{
        void onInvitefriend();

        void onNewInvite();
    }
}
