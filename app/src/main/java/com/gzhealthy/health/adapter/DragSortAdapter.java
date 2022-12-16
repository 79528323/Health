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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 拖动列表内容
 */
public class DragSortAdapter extends RecyclerView.Adapter<DragSortAdapter.MyItemTouchViewHolder> implements ItemTouchHelperAdapterCallBack {

    private boolean isDrag = false;
    private Context context;
    private List<HealthCard> mDatas = new ArrayList<>();
    public OnMoveListItemCallBack onMoveListItemCallBack;//拖动完成后状态回调
//    public OnDragItemStateCallBack onDragItemStateCallBack;
    public View.OnClickListener onClickListener;

    public DragSortAdapter(Context context,OnMoveListItemCallBack onMoveListItemCallBack
            ,View.OnClickListener onClickListener) {
        this.context = context;
        this.onMoveListItemCallBack = onMoveListItemCallBack;
        this.onClickListener = onClickListener;
    }

    @Override
    public void onItemMove(int formPosition, int toPosition) {
        Collections.swap(mDatas,formPosition,toPosition);
//        HealthCard card = this.mDatas.remove(formPosition);
//        mDatas.add(toPosition > formPosition ? toPosition -1 : toPosition,card);
        notifyItemMoved(formPosition,toPosition);
    }

    /**
     * 回调拖动状态
     * @param actionState
     */
    @Override
    public void onItemMovedDown(int actionState) {
        if (this.onMoveListItemCallBack!=null){
            if (actionState == ItemTouchHelper.ACTION_STATE_IDLE)
                this.onMoveListItemCallBack.moveList(mDatas);
        }
    }

    @NonNull
    @Override
    public MyItemTouchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyItemTouchViewHolder(LayoutInflater.from(context).inflate(R.layout.item_health_card,null));
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemTouchViewHolder holder, int position) {
        holder.name.setText(mDatas.get(position).name);
        holder.btn_controller.setSelected(true);
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

    public boolean isDrag() {
        return isDrag;
    }

    public void setDrag(boolean drag) {
        isDrag = drag;
    }

    public interface OnMoveListItemCallBack{
        void moveList(List<HealthCard> list);
    }
}
