package com.javon.packetrecyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


public class EventDriverFooter implements BaseRecyclerAdapter.ItemView {

    private FrameLayout mContainer;
    private View mMoreView;
    private View mNoMoreView;
    private View mErrorView;
    private int mFlag = 0;
    private DefaultEventDriver mDriver;
    private Context mContext;


    public EventDriverFooter(Context mContext, DefaultEventDriver mDriver) {
        this.mDriver = mDriver;
        this.mContext = mContext;

    }

    @Override
    public View onCreateView(ViewGroup parent) {
//        Log.i("recycler", "onCreateView");
        View root = LayoutInflater.from(mContext).inflate(R.layout.view_footer, parent, false);
        mContainer = (FrameLayout) root.findViewById(R.id.footer_container);
//        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return root;
    }

    @Override
    public void onBindView(View headerView) {
//        Log.i("recycler", "onBindView");
        switch (mFlag) {
            case 1:
//                Log.e("recycler11", "trigger more");
                showView(mMoreView);
                mDriver.triggerOnMore();
                break;
            case 2:
//                Log.e("recycler11", "trigger error");
                showView(mErrorView);
                mDriver.triggerOnError();
                break;
            case 3:
                showView(mNoMoreView);
                break;
        }
    }

    private void showView(View mView) {
//        Log.e("recycler11", "mContainer child's count : " + mContainer.getChildCount());
        if (mView != null) {
            if (mContainer.getVisibility() != View.VISIBLE) {
//                Log.e("recycler11", "parent is visible");
                mContainer.setVisibility(View.VISIBLE);
            }
            if (mView.getParent() == null) {
//                Log.e("recycler11", "parent is null");
                mContainer.addView(mView);
            } else {
//                Log.e("recycler11", "parent is not null");
            }
//            Log.e("recycler11", "mContainer child's count11 : " + mContainer.getChildCount());
            for (int i = 0; i < mContainer.getChildCount(); i++) {
                if (mContainer.getChildAt(i) == mView) {
//                    Log.e("recycler11", "i come here 111");
                    mContainer.getChildAt(i).setVisibility(View.VISIBLE);
//                    Log.e("recycler11", "i come here 222");
                } else {
//                    Log.e("recycler11", "i come here 123");
                    mContainer.getChildAt(i).setVisibility(View.GONE);
//                    mContainer.removeViewAt(i);
//                    Log.e("recycler11", "i come here 333");
                }
            }
        } else {
//            Log.e("recycler11", "i come here nothing");
            mContainer.setVisibility(View.GONE);
        }

    }


    public void showMore() {
//        showView(mMoreView);
//        mDriver.triggerOnMore();
        mFlag = 1;
    }

    public void showError() {
//        showView(mErrorView);
//        mDriver.triggerOnError();
        mFlag = 2;
    }


    public void showNoMore() {
//        showView(mNoMoreView);
        mFlag = 3;
    }

    public void showErrorRetry() {
        showView(mMoreView);
    }

    //初始化
    public void hide() {
        mFlag = 0;
        if (mContainer != null) {
            mContainer.setVisibility(View.GONE);
        } else {
            Log.e("footer", "footer is null");
        }

    }

    public void setMoreView(View moreView) {
        this.mMoreView = moreView;
    }

    public void setmNoMoreView(View mNoMoreView) {
        this.mNoMoreView = mNoMoreView;
    }

    public void setmErrorView(View mErrorView) {
        this.mErrorView = mErrorView;
    }

    public View getmErrorView() {
        return mErrorView;
    }
}
