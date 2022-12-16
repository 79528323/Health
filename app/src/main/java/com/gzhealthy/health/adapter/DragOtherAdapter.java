package com.gzhealthy.health.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.callback.ItemTouchHelperAdapterCallBack;
import com.gzhealthy.health.model.HealthCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 未添加滑动列表内容
 */
public class DragOtherAdapter extends RecyclerView.Adapter<DragOtherAdapter.MyItemTouchViewHolder> {

    private Context context;
    private List<HealthCard> mDatas = new ArrayList<>();
    public View.OnClickListener onClickListener;


    public DragOtherAdapter(Context context,View.OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyItemTouchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyItemTouchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_health_card,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemTouchViewHolder holder, int position) {
        holder.name.setText(mDatas.get(position).name);
        holder.menu.setVisibility(View.GONE);
        holder.btn_controller.setSelected(false);
        holder.btn_container.setTag(position);
        holder.btn_container.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class MyItemTouchViewHolder extends RecyclerView.ViewHolder{
        ImageView btn_controller,menu;
        TextView name;
        LinearLayout btn_container;

        public MyItemTouchViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            btn_controller = itemView.findViewById(R.id.btn_controller);
            menu = itemView.findViewById(R.id.menu);
            btn_container = itemView.findViewById(R.id.btn_container);
        }
    }

    public void refreshData(List<HealthCard> mList){
        this.mDatas.clear();
        if (mList!=null && !mList.isEmpty()){
            this.mDatas.addAll(mList);
        }
        notifyDataSetChanged();
    }

    public void appendData(List<HealthCard> mList){
        if (mList!=null && !mList.isEmpty()){
            this.mDatas.addAll(mList);
            notifyDataSetChanged();
        }
    }

}
