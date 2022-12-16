package com.gzhealthy.health.widget;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

/**
 * Created by Justin_Liu
 * on 2021/8/3
 */
public class DragSortRecyclerView extends RecyclerView{


    public DragSortRecyclerView(@NonNull Context context) {
        super(context);
    }

    public DragSortRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    class MyDragOnItemTouchHelper extends ItemTouchHelper{

        /**
         * Creates an ItemTouchHelper that will work with the given Callback.
         * <p>
         * You can attach ItemTouchHelper to a RecyclerView via
         * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
         * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
         *
         * @param callback The Callback which controls the behavior of this touch helper.
         */


        public MyDragOnItemTouchHelper(@NonNull Callback callback) {
            super(callback);
        }


    }





}
