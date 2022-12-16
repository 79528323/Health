package com.javon.packetrecyclerview;

import android.animation.Animator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 底部：数据==>>事件==>>View展示
 * 主干：数据==>>View展示
 */

public abstract class BaseRecyclerAdapter<M> extends RecyclerView.Adapter<BaseViewHolder> {


    protected List<M> mObjects;
    private EventDriver mEventDrive;  //事件驱动对象
    private final Object mLock = new Object();
    private boolean mNotifyOnChange = true;
    private Context mContext;
    private boolean mIsError = false;  //添加数据出错标识
    ArrayList<ItemView> mHeaders = new ArrayList<>();  //添加头部
    ArrayList<ItemView> mFooters = new ArrayList<>();  //添加底部
    private Interpolator mInterpolator = new LinearInterpolator(); //动画插值器
    private boolean mOpenAnimationEnable = false;  //是否打开动画
    private boolean mFirstOnlyEnable = true;  //动画是否第一次加入时生效
    private int mDuration = 300;
    private int mLastPosition = -1;
    private int mCurrentLastPosition = 0;
    @AnimationType
    private BaseAnimation mCustomAnimation;
    private BaseAnimation mSelectAnimation = new AlphaInAnimation();
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    @IntDef({ALPHAIN, SCALEIN, SLIDEIN_BOTTOM, SLIDEIN_LEFT, SLIDEIN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimationType {
    }

    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;

    public interface ItemView {
        View onCreateView(ViewGroup mParent);

        void onBindView(View mHeaderView);
    }

    public interface OnEventDriver {
        void onLoadMore();

        void onRetryMore();

        void onRetryError();
    }

    public interface OnItemClickListener {
        void onItemClick(View mView, int mPosition, Object mObj);
    }

    public interface OnItemLongClickListener {
        boolean onItemClick(View mView, int mPosition, Object mObj);
    }

    public void setOnItemClickListener(OnItemClickListener mListener) {
        this.mItemClickListener = mListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mListener) {
        this.mItemLongClickListener = mListener;
    }

    public class GridSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        private int mMaxCount;

        public GridSpanSizeLookup(int mMaxCount) {
            this.mMaxCount = mMaxCount;
        }

        @Override
        public int getSpanSize(int mPosition) {
            if (mHeaders.size() != 0) {
                if (mPosition < mHeaders.size()) return mMaxCount;
            }
            if (mFooters.size() != 0) {
                int i = mPosition - mFooters.size() - mObjects.size();
                if (i >= 0) {
                    return mMaxCount;
                }
            }
            return 1;
        }
    }

    public GridSpanSizeLookup obtainGridSpanSizeLookUp(int mMaxCount) {
        return new GridSpanSizeLookup(mMaxCount);
    }

    public BaseRecyclerAdapter(Context mContext) {
        init(mContext, new ArrayList<M>());
    }


    public BaseRecyclerAdapter(Context mContext, M[] mObjects) {
        init(mContext, Arrays.asList(mObjects));
    }


    public BaseRecyclerAdapter(Context mContext, List<M> mObjects) {
        init(mContext, mObjects);
    }


    private void init(Context mContext, List<M> mObjects) {
        this.mContext = mContext;
        this.mObjects = mObjects;
    }

    public Context getContext() {
        return this.mContext;
    }


    public void setData(List<M> mObjects, boolean mIsError) {
        this.mIsError = mIsError;
        this.mCurrentLastPosition = 0;
        if (mObjects != null) {
            synchronized (mLock) {
                this.mObjects.addAll(mObjects);
            }
        }
        if (mEventDrive != null) mEventDrive.setData(mObjects, mIsError);
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void addData(M mObj, boolean mIsError) {
        this.mIsError = mIsError;
        this.mCurrentLastPosition = getCount();
        if (mObj != null) {
            synchronized (mLock) {
                mObjects.add(mObj);
            }
        }
        if (mEventDrive != null) mEventDrive.addData(mObj == null ? 0 : 1, mIsError);
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void addAllData(Collection<? extends M> mCollection, boolean mIsError) {
        this.mIsError = mIsError;
        this.mCurrentLastPosition = getCount();
        if (mCollection != null && mCollection.size() != 0) {
            synchronized (mLock) {
                mObjects.addAll(mCollection);
            }
        }
        if (mEventDrive != null) {
            mEventDrive.addData(mCollection == null ? 0 : mCollection.size(), mIsError);
        }

        if (mNotifyOnChange) notifyDataSetChanged();
//        Log.e("adapter", "mCurrentLastPosition : " + this.mCurrentLastPosition + ", change counter : " + (getCount()  + getFooterCount()- this.mCurrentLastPosition));
//        if (mNotifyOnChange) notifyItemRangeChanged(this.mCurrentLastPosition == 0 ? 0 : this.mCurrentLastPosition, getCount()  + getFooterCount()- this.mCurrentLastPosition);
    }


    public void addAllData(boolean mIsError, M... mItems) {
        this.mIsError = mIsError;
        this.mCurrentLastPosition = getCount();
        if (mItems != null && mItems.length != 0) {
            synchronized (mLock) {
                Collections.addAll(mObjects, mItems);
            }
        }
        if (mEventDrive != null) mEventDrive.addData(mItems == null ? 0 : mItems.length, mIsError);

        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void stopMore() {
        if (mEventDrive == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDrive.stopLoadMore();
    }

    public void pauseMore() {
        if (mEventDrive == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDrive.pauseLoadMore();
    }

    public void resumeMore() {
        if (mEventDrive == null)
            throw new NullPointerException("You should invoking setLoadMore() first");
        mEventDrive.resumeLoadMore();
    }


    public void addHeader(ItemView mView) {
        if (mView == null) throw new NullPointerException("ItemView can't be null");
        mHeaders.add(mView);
    }

    public void addFooter(ItemView mView) {
        if (mView == null) throw new NullPointerException("ItemView can't be null");
        mFooters.add(mView);
    }

    public void removeAllHeader() {
        mHeaders.clear();
    }

    public void removeAllFooter() {
        mFooters.clear();
    }

    public void getHeader(int index) {
        mHeaders.get(index);
    }

    public void getFooter(int index) {
        mFooters.get(index);
    }

    public int getHeaderCount() {
        return mHeaders.size();
    }

    public int getFooterCount() {
        return mFooters.size();
    }

    public void removeHeader(ItemView view) {
        mHeaders.remove(view);
    }

    public void removeFooter(ItemView view) {
        mHeaders.remove(view);
    }


    private EventDriver getEventDelegate() {
        if (mEventDrive == null) mEventDrive = new DefaultEventDriver(this);
        return mEventDrive;
    }

    public View setMore(final int mRes, final OnEventDriver mListener) {
        FrameLayout mContainer = new FrameLayout(getContext());
        mContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(mRes, mContainer);
        getEventDelegate().setMore(mContainer, mListener);
        return mContainer;
    }

    public View setMore(final View mView, OnEventDriver mListener) {
        getEventDelegate().setMore(mView, mListener);
        return mView;
    }

    public View setNoMore(final int mRes) {
        FrameLayout mContainer = new FrameLayout(getContext());
        mContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(mRes, mContainer);
        getEventDelegate().setNoMore(mContainer);
        return mContainer;
    }

    public View setNoMore(final View mView) {
        getEventDelegate().setNoMore(mView);
        return mView;
    }

    public View setError(final int mRes, OnEventDriver mListener) {
        FrameLayout mContainer = new FrameLayout(getContext());
        mContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater.from(getContext()).inflate(mRes, mContainer);
        getEventDelegate().setErrorMore(mContainer, mListener);
        return mContainer;
    }

    public View setError(final View mView, OnEventDriver mListener) {
        getEventDelegate().setErrorMore(mView, mListener);
        return mView;
    }


    public void insert(M mObj, int mIndex) {
        synchronized (mLock) {
            mObjects.add(mIndex, mObj);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void remove(M mObj) {
        synchronized (mLock) {
            mObjects.remove(mObj);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void remove(int mPosition) {
        synchronized (mLock) {
            mObjects.remove(mPosition);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void clear() {
        if (mEventDrive != null) mEventDrive.clear();
        synchronized (mLock) {
            mObjects.clear();
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void sort(Comparator<? super M> mComparator) {
        synchronized (mLock) {
            Collections.sort(mObjects, mComparator);
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }


    public void setNotifyOnChange(boolean mNotifyOnChange) {
        this.mNotifyOnChange = mNotifyOnChange;
    }


    @Deprecated
    @Override
    public final int getItemCount() {
        return getCount() + mHeaders.size() + mFooters.size();
    }


    public int getCount() {
        return mObjects.size();
    }

    public int getTotalCount() {
        return mObjects.size() + mHeaders.size() + mFooters.size();
    }

    private View createSpViewByType(ViewGroup mParent, int mViewType) {
        for (ItemView mHeaderView : mHeaders) {
            if (mHeaderView.hashCode() == mViewType) {
                View mView = mHeaderView.onCreateView(mParent);
                StaggeredGridLayoutManager.LayoutParams mLayoutParams = new StaggeredGridLayoutManager.LayoutParams(mView.getLayoutParams());
                mLayoutParams.setFullSpan(true);
                mView.setLayoutParams(mLayoutParams);
                return mView;
            }
        }
        for (ItemView mFooterview : mFooters) {
            if (mFooterview.hashCode() == mViewType) {
                View mView = mFooterview.onCreateView(mParent);
                StaggeredGridLayoutManager.LayoutParams mLayoutParams = new StaggeredGridLayoutManager.LayoutParams(mView.getLayoutParams());
                mLayoutParams.setFullSpan(true);
                mView.setLayoutParams(mLayoutParams);
                return mView;
            }
        }
        return null;
    }

    @Override
    public final BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = createSpViewByType(parent, viewType);
        if (mView != null) {
            return new StateViewHolder(mContext, mView);
        }

        final BaseViewHolder mViewHolder = OnCreateViewHolder(parent, viewType);

        //itemView 的点击事件
        if (mItemClickListener != null) {
            mViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, mViewHolder.getAdapterPosition() - mHeaders.size(), mObjects.get(mViewHolder.getAdapterPosition() - mHeaders.size()));
                }
            });
        }

        if (mItemLongClickListener != null) {
            mViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mItemLongClickListener.onItemClick(v, mViewHolder.getAdapterPosition() - mHeaders.size(), mObjects.get(mViewHolder.getAdapterPosition() - mHeaders.size()));
                }
            });
        }
        return mViewHolder;
    }

    abstract public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType);


    @Override
    public final void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setId(position);
        addAnimation(holder);
        if (mHeaders.size() != 0 && position < mHeaders.size()) {
            mHeaders.get(position).onBindView(holder.itemView);
            return;
        }

//        int i = position - headers.size() - mObjects.size();
        int i = position - mHeaders.size() - getCount();
        if (mFooters.size() != 0 && i >= 0) {
            mFooters.get(i).onBindView(holder.itemView);
            return;
        }
        OnBindViewHolder(holder, position - mHeaders.size());

    }


    public void OnBindViewHolder(BaseViewHolder mHolder, final int mPosition) {
        mHolder.setData(getItem(mPosition));
        mHolder.setData(getItem(mPosition), mPosition);
    }


    @Deprecated
    @Override
    public final int getItemViewType(int position) {
        if (mHeaders.size() != 0) {
            if (position < mHeaders.size()) return mHeaders.get(position).hashCode();
        }
        if (mFooters.size() != 0) {
            /*
            eg:
            0:header1
            1:header2   2
            2:object1
            3:object2
            4:object3
            5:object4
            6:footer1   6(position) - 2 - 4 = 0
            7:footer2
             */
//            int i = position - headers.size() - mObjects.size();
            int i = position - mHeaders.size() - getCount();
//            Log.e("footer loader", "footer item position :" + i);
            if (i >= 0) {
                return mFooters.get(i).hashCode();
            }
        }
        return getViewType(position - mHeaders.size());
    }

    public int getViewType(int mPosition) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public M getItem(int mPosition) {
        return mObjects.get(mPosition);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param mItem The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(M mItem) {
        return mObjects.indexOf(mItem);
    }

    public boolean ismIsError() {
        return mIsError;
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int mPosition) {
        return mPosition;
    }

    private class StateViewHolder extends BaseViewHolder {

        public StateViewHolder(Context mContext, View mItemView) {
            super(mContext, mItemView);
        }
    }

    private void addAnimation(RecyclerView.ViewHolder mHolder) {
        if (mOpenAnimationEnable) {
            if (!mFirstOnlyEnable || mHolder.getLayoutPosition() > mLastPosition) {
                BaseAnimation animation = null;
                if (mCustomAnimation != null) {
                    animation = mCustomAnimation;
                } else {
                    animation = mSelectAnimation;
                }
                for (Animator anim : animation.getAnimators(mHolder.itemView)) {
                    startAnim(anim, mHolder.getLayoutPosition());
                }
                mLastPosition = mHolder.getLayoutPosition();
            }
        }
    }

    protected void startAnim(Animator mAnim, int mIndex) {
        mAnim.setDuration(mDuration).start();
        mAnim.setInterpolator(mInterpolator);
    }


    /**
     * Set the view animation type.
     *
     * @param mAnimationType One of {@link #ALPHAIN}, {@link #SCALEIN}, {@link #SLIDEIN_BOTTOM}, {@link #SLIDEIN_LEFT}, {@link #SLIDEIN_RIGHT}.
     */
    public void openLoadAnimation(@AnimationType int mAnimationType, boolean mFirstOnlyEnable) {
        this.mOpenAnimationEnable = true;
        this.mFirstOnlyEnable = mFirstOnlyEnable;
        mCustomAnimation = null;
        switch (mAnimationType) {
            case ALPHAIN:
                mSelectAnimation = new AlphaInAnimation();
                break;
            case SCALEIN:
                mSelectAnimation = new ScaleInAnimation();
                break;
            case SLIDEIN_BOTTOM:
                mSelectAnimation = new SlideInBottomAnimation();
                break;
            case SLIDEIN_LEFT:
                mSelectAnimation = new SlideInLeftAnimation();
                break;
            case SLIDEIN_RIGHT:
                mSelectAnimation = new SlideInRightAnimation();
                break;
            default:
                break;
        }
    }

    /**
     * Set Custom ObjectAnimator
     *
     * @param mAnimation ObjectAnimator
     */
    public void openLoadAnimation(BaseAnimation mAnimation) {
        this.mOpenAnimationEnable = true;
        this.mCustomAnimation = mAnimation;
    }

    public void openLoadAnimation() {
        this.mOpenAnimationEnable = true;
    }


}
