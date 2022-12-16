package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.model.BindInfoListModel;

import java.util.List;

public class BindInfo2Adapter extends RecyclerView.Adapter<BindInfo2Adapter.ViewHolder> {

    private Context mContext;
    private List<BindInfoListModel.DataBean> mList;
    private BindInfoListModel.DataBean data;
    private OnItemClikListener mOnItemClikListener;

    public BindInfo2Adapter(Context context, List<BindInfoListModel.DataBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bind_info2, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data = mList.get(position);

        holder.tv_name.setText(data.getName());

        if (data.isSelected()) {
            holder.tv_name.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_bind_press));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.color_13D1AE));
        } else {
            holder.tv_name.setBackground(ContextCompat.getDrawable(mContext, R.drawable.btn_bind_nomal));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.color_999999));
        }

        if (mOnItemClikListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemClik(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClikListener.onItemLongClik(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public interface OnItemClikListener {
        void onItemClik(View view, int position);

        void onItemLongClik(View view, int position);
    }

    public void setItemClikListener(OnItemClikListener mOnItemClikListener) {
        this.mOnItemClikListener = mOnItemClikListener;
    }

}
