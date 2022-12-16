package com.gzhealthy.health.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.activity.OperationContactsActivity;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.tool.GlideUtils;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ContractAdapter extends RecyclerView.Adapter<ContractAdapter.ViewHolder> {

    private Context mContext;
    public List<ContractModel.DataBean> mList;
    private View.OnClickListener onClickListener;

    public ContractAdapter(Context context, List<ContractModel.DataBean> mList,View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_contrac, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setText(mList.get(position).emerContactName);
        holder.phone.setText(mList.get(position).emerContactPhone);
        holder.edit.setTag(mList.get(position));
        holder.edit.setOnClickListener(this.onClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView phone,name;
        private LinearLayout edit;

        public ViewHolder(View itemView) {
            super(itemView);
            phone = (TextView) itemView.findViewById(R.id.phone);
            name = (TextView) itemView.findViewById(R.id.name);
            edit = (LinearLayout) itemView.findViewById(R.id.edit);
        }
    }


    public void refreshData(List<ContractModel.DataBean> list){
        this.mList.clear();
        if (!list.isEmpty()){
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

//    public interface OnItemClikListener {
//        void onItemClik(View view, int position);
//
//        void onItemLongClik(View view, int position);
//    }
//
//    public void setItemClikListener(OnItemClikListener mOnItemClikListener) {
//        this.mOnItemClikListener = mOnItemClikListener;
//    }

}
