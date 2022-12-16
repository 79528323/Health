package com.gzhealthy.health.callback;

/**
 * Created by Justin_Liu
 * on 2021/8/3
 */
public interface ItemTouchHelperAdapterCallBack {

    //拖动item的回调
    void onItemMove(int formPosition,int toPosition);

    //拖动后手松开
    void onItemMovedDown(int actionState);
}
