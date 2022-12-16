package com.javon.packetrecyclerview;

import android.view.View;

import java.util.List;


public interface EventDriver<M> {

    void setData(List<M> obj, boolean mIsError);

    void addData(int mLength, boolean mIsError);

    void clear();

    void stopLoadMore();

    void pauseLoadMore();

    void resumeLoadMore();

    void setMore(View mView, BaseRecyclerAdapter.OnEventDriver mListener);

    void setNoMore(View mView);

    void setErrorMore(View mView, BaseRecyclerAdapter.OnEventDriver mListener);


}
