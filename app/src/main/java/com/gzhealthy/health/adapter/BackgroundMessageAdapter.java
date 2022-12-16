package com.gzhealthy.health.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aitsuki.swipe.SwipeMenuRecyclerView;
import com.gzhealthy.health.R;
import com.gzhealthy.health.model.PushMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BackgroundMessageAdapter extends SwipeMenuRecyclerView.Adapter<BackgroundMessageAdapter.ViewHolder> {

    private Context mContext;
    public List<PushMessage.DataBean> mList = new ArrayList<>();
    public OnMessageCallBack onMessageCallBack;

    public BackgroundMessageAdapter(Context context,OnMessageCallBack onMessageCallBack) {
        this.mContext = context;
        this.onMessageCallBack = onMessageCallBack;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_background_message, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.date.setText(isEqualToday(mList.get(position).pushTime));
        holder.content.setText(mList.get(position).extraContent);
        holder.title.setText(mList.get(position).title);
        holder.red_no_read.setVisibility(mList.get(position).ifRead==0?View.VISIBLE:View.INVISIBLE);
        holder.bg.setOnClickListener(v -> {
            onMessageCallBack.onMessage(mList.get(position));
        });
    }

    public void refreshData(List<PushMessage.DataBean> mList){
        this.mList.clear();
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
        }
        notifyDataSetChanged();
    }

    public void appendData(List<PushMessage.DataBean> mList){
        if (mList!=null && !mList.isEmpty()){
            this.mList.addAll(mList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title , content ,date;
        private ImageView red_no_read;
        private LinearLayout bg;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            date = itemView.findViewById(R.id.date);
            red_no_read = itemView.findViewById(R.id.red_no_read);
            bg = itemView.findViewById(R.id.bg);
        }
    }



    /**
     * 判断日期是否当天 如是显示 '今天'
     * @param date
     * @return
     */
    private String isEqualToday(String date){
        Calendar calendar = Calendar.getInstance();
        String curDate = calendar.get(Calendar.YEAR)+"-"+calendar.get(Calendar.MONTH)+1+"-"+calendar.get(Calendar.DATE);
        String day = date.substring(0,date.indexOf(" "));
        String time = date.substring(date.indexOf(" "));
        return TextUtils.equals(curDate,day)?"今天 "+time : date;
    }


    public interface OnMessageCallBack{
        void onMessage(PushMessage.DataBean bean);
    }
}
