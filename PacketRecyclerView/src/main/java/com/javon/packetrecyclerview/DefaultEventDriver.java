package com.javon.packetrecyclerview;

import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.logging.Logger;


public class DefaultEventDriver<M> implements EventDriver<M> {

    private final String TAG = DefaultEventDriver.class.getSimpleName();
    private EventDriverFooter footer;
    private BaseRecyclerAdapter.OnEventDriver mEventDriver;

    private boolean mIsLoadingMore = false;
    private static final int STATUS_INITIAL = 291;
    private static final int STATUS_MORE = 292;
    private static final int STATUS_ERROR = 294;
    private static final int STATUS_NOMORE = 293;
    private int status = STATUS_INITIAL;

    public DefaultEventDriver(BaseRecyclerAdapter adapter) {
        footer = new EventDriverFooter(adapter.getContext(), this);
        adapter.addFooter(footer);
    }

    public void triggerOnMore() {
//        Log.e(TAG, "trigger on more");
        mIsLoadingMore = true;
        if (status == STATUS_MORE && mEventDriver != null) {
            mEventDriver.onLoadMore();
        }
    }

    public void triggerOnError() {
//        Log.e(TAG, "trigger on error");
        mIsLoadingMore = false;
        if (status == STATUS_ERROR && mEventDriver != null) {
            footer.getmErrorView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    footer.showErrorRetry();
                    mEventDriver.onRetryMore();
                }
            });
        }
    }

    @Override
    public void setData(List<M> obj, boolean mIsError) {
//        Log.e(TAG, "mIsLoadingMore : false, status is initial");
        mIsLoadingMore = false;
        status = STATUS_INITIAL;
    }

    @Override
    public void addData(int mLength, boolean mIsError) {
        if (mLength == 0) {
//            Log.e(TAG, "mIsLoadingMore : false , " + (mIsLoadingMore ? "no_more" : "error" + "111"));
            if (!mIsError) {
                stopLoadMore();
            } else {
                pauseLoadMore();
            }
        } else {
//            Log.e(TAG, "mIsLoadingMore : true, status is more");
            resumeLoadMore();
        }
    }

    @Override
    public void clear() {
//        Log.e(TAG, "mIsLoadingMore : false, status is initial, clear");
        mIsLoadingMore = false;
        status = STATUS_INITIAL;
        footer.hide();
    }

    @Override
    public void stopLoadMore() {
//        Log.e(TAG, "mIsLoadingMore : false, status is no_more, stop load more");
        mIsLoadingMore = false;
        status = STATUS_NOMORE;
        footer.showNoMore();
    }

    @Override
    public void pauseLoadMore() {
//        Log.e(TAG, "mIsLoadingMore : false, status is error, pause load more");
        mIsLoadingMore = false;
        status = STATUS_ERROR;
        footer.showError();
//        triggerOnError();
    }

    @Override
    public void resumeLoadMore() {
//        Log.e(TAG, "mIsLoadingMore : false, status is more, resume load more");
        mIsLoadingMore = true;
        status = STATUS_MORE;
        footer.showMore();
//        triggerOnMore();
    }

    @Override
    public void setMore(View mView, BaseRecyclerAdapter.OnEventDriver mListener) {
        footer.setMoreView(mView);
        this.mEventDriver = mListener;

    }

    @Override
    public void setNoMore(View mView) {
        footer.setmNoMoreView(mView);
    }

    @Override
    public void setErrorMore(View mView, final BaseRecyclerAdapter.OnEventDriver mListener) {
        footer.setmErrorView(mView);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventDriver = mListener;
            }
        });
    }
}
