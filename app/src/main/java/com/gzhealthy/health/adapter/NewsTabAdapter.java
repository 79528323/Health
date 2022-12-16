package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.NewsListModel;
import com.gzhealthy.health.tool.GlideUtils;

import java.util.List;

public class NewsTabAdapter extends RecyclerView.Adapter<NewsTabAdapter.ViewHolder> {

    private Context mContext;
    private List<NewsListModel.DataBean> mList;
    private NewsListModel.DataBean data;
    private OnItemClikListener mOnItemClikListener;

    public NewsTabAdapter(Context context, List<NewsListModel.DataBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_news_tab, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        data = mList.get(position);

        holder.tv_title.setText(data.getTitle());
        holder.tv_tab.setText(String.format("%s     阅读量%s", data.getAddTime().substring(5), data.getVisit()));
        GlideUtils.DrawableTransformCop(mContext, holder.iv_cover, Constants.Api.ossPicPre + data.getImageInput(),
                4, false, false, false, false);

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

        private TextView tv_title;
        private TextView tv_tab;
        private ImageView iv_cover;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_tab = (TextView) itemView.findViewById(R.id.tv_tab);
            iv_cover = (ImageView) itemView.findViewById(R.id.iv_cover);
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
