package com.gzhealthy.health.callback;

import android.util.Log;

import com.gzhealthy.health.adapter.DragSortAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/8/3
 */
public class OnDragSortItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private DragSortAdapter adapter;

    public OnDragSortItemTouchHelperCallBack(DragSortAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //在这个回调方法里我们返回我们需要的使用的动作功能
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;  //拖动  这里设置的UP 与 DOWN 表示允许上下拖动
        int swipeFlags = ItemTouchHelper.ACTION_STATE_IDLE;         //滑动  这里设置的ACTION_STATE_IDLE 表示我们将滑动动作设置为空闲
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder
            , @NonNull RecyclerView.ViewHolder target) {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        adapter.onItemMovedDown(actionState);
    }
}
