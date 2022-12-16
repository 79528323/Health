package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.model.ContractModel;
import com.gzhealthy.health.model.QuePaper;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ReportQueAdapter extends RecyclerView.Adapter<ReportQueAdapter.ViewHolder> {

    private Context mContext;
    private List<QuePaper.DataBean.QuestionListBean> mList;
    private View.OnClickListener onClickListener;

    public ReportQueAdapter(Context context, List<QuePaper.DataBean.QuestionListBean> mList, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mList = mList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_report_que, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        int num = position+1;
        holder.content.setText(num+"."+mList.get(position).question);
        holder.content.setSelected(mList.get(position).selected > 0);
        holder.content.setTag(mList.get(position));
        holder.content.setOnClickListener(this.onClickListener);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView content;

        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }


    public void refreshData(List<QuePaper.DataBean.QuestionListBean> list){
        this.mList.clear();
        if (!list.isEmpty()){
            mList.addAll(list);
            notifyDataSetChanged();
        }
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
