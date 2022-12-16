package com.javon.packetrecyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;


public abstract class BaseViewHolder<M> extends RecyclerView.ViewHolder {

    /**
     * Views indexed with their IDs
     */
    private final SparseArray<View> mViews;

    private final Context mContext;

    public View mConvertView;


    public BaseViewHolder(Context mContext, View mItemView) {
        super(mItemView);
        this.mContext = mContext;
        this.mViews = new SparseArray<>();
        mConvertView = mItemView;
    }


    public void setData(M mData) {
    }

    public void setData(M mData, int mposition) {
    }

    protected <T extends View> T $(@IdRes int mId) {
        return (T) itemView.findViewById(mId);
    }

    protected Context getContext() {
        return itemView.getContext();
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * Will set the text of a TextView.
     *
     * @param mViewId The view id.
     * @param mValue  The text to put in the text view.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setText(int mViewId, CharSequence mValue) {
        AppCompatTextView mView = $(mViewId);
        mView.setText(mValue);
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param mViewId     The view id.
     * @param mImageResId The image resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageResource(int mViewId, int mImageResId) {
        ImageView mView = $(mViewId);
        mView.setImageResource(mImageResId);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param mViewId The view id.
     * @param mColor  A color, not a resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundColor(int mViewId, int mColor) {
        View mView = $(mViewId);
        mView.setBackgroundColor(mColor);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param mViewId        The view id.
     * @param mBackgroundRes A resource to use as a background.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setBackgroundRes(int mViewId, int mBackgroundRes) {
        View mView = $(mViewId);
        mView.setBackgroundResource(mBackgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param mViewId    The view id.
     * @param mTextColor The text color (not a resource id).
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextColor(int mViewId, int mTextColor) {
        TextView mView = $(mViewId);
        mView.setTextColor(mTextColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param mViewId       The view id.
     * @param mTextColorRes The text color resource id.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTextColorRes(int mViewId, int mTextColorRes) {
        TextView mView = $(mViewId);
        mView.setTextColor(mContext.getResources().getColor(mTextColorRes));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param mViewId   The view id.
     * @param mDrawable The image drawable.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageDrawable(int mViewId, Drawable mDrawable) {
        ImageView mView = $(mViewId);
        mView.setImageDrawable(mDrawable);
        return this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.<br/>
     * It uses Square's Picasso library to download the image asynchronously and put the result into the ImageView.<br/>
     * Picasso manages recycling of views in a ListView.<br/>
     * If you need more control over the Picasso settings, use {BaseViewHolder#setImageBuilder}.
     *
     * @param mViewId  The view id.
     * @param mBuilder The glid instance.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setImageUrl(int mViewId, DrawableRequestBuilder mBuilder) {
        ImageView mView = $(mViewId);
        mBuilder.into(mView);
        return this;
    }


    /**
     * Add an action to set the image of an image view. Can be called multiple times.
     */
    public BaseViewHolder setImageBitmap(int mViewId, Bitmap mBitmap) {
        ImageView mView = $(mViewId);
        mView.setImageBitmap(mBitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    public BaseViewHolder setAlpha(int mViewId, float mValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            $(mViewId).setAlpha(mValue);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(mValue, mValue);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            $(mViewId).startAnimation(alpha);
        }
        return this;
    }

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param mViewId  The view id.
     * @param mVisible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setVisible(int mViewId, boolean mVisible) {
        View view = $(mViewId);
        view.setVisibility(mVisible ? View.VISIBLE : View.GONE);
        return this;
    }

    /**
     * Set a view visibility to INVISIBLE (true) or VISIBLE (false).
     *
     * @param mViewId    The view id.
     * @param mInVisible True for VISIBLE, false for GONE.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setInVisible(int mViewId, boolean mInVisible) {
        View view = $(mViewId);
        view.setVisibility(mInVisible ? View.INVISIBLE : View.VISIBLE);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param mViewId The id of the TextView to linkify.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder linkify(int mViewId) {
        TextView view = $(mViewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public BaseViewHolder setTypeface(int mViewId, Typeface mTypeface) {
        TextView mView = $(mViewId);
        mView.setTypeface(mTypeface);
        mView.setPaintFlags(mView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel rendering.
     */
    public BaseViewHolder setTypeface(Typeface mTypeface, int... mViewIds) {
        for (int mViewId : mViewIds) {
            TextView mView = $(mViewId);
            mView.setTypeface(mTypeface);
            mView.setPaintFlags(mView.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param mViewId   The view id.
     * @param mProgress The progress.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setProgress(int mViewId, int mProgress) {
        ProgressBar mView = $(mViewId);
        mView.setProgress(mProgress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param mViewId   The view id.
     * @param mProgress The progress.
     * @param mMax      The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setProgress(int mViewId, int mProgress, int mMax) {
        ProgressBar mView = $(mViewId);
        mView.setMax(mMax);
        mView.setProgress(mProgress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param mViewId The view id.
     * @param mMax    The max value of a ProgressBar.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setMax(int mViewId, int mMax) {
        ProgressBar mView = $(mViewId);
        mView.setMax(mMax);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param mViewId The view id.
     * @param mRating The rating.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(int mViewId, float mRating) {
        RatingBar mView = $(mViewId);
        mView.setRating(mRating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param mViewId The view id.
     * @param mRating The rating.
     * @param mMax    The range of the RatingBar to 0...max.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setRating(int mViewId, float mRating, int mMax) {
        RatingBar mView = $(mViewId);
        mView.setMax(mMax);
        mView.setRating(mRating);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param mViewId   The view id.
     * @param mListener The on click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnClickListener(int mViewId, View.OnClickListener mListener) {
        View mView = $(mViewId);
        mView.setOnClickListener(mListener);
        return this;
    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param mViewId   The view id.
     * @param mListener The on touch listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnTouchListener(int mViewId, View.OnTouchListener mListener) {
        View mView = $(mViewId);
        mView.setOnTouchListener(mListener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param mViewId   The view id.
     * @param mListener The on long click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnLongClickListener(int mViewId, View.OnLongClickListener mListener) {
        View mView = $(mViewId);
        mView.setOnLongClickListener(mListener);
        return this;
    }

    /**
     * Sets the listview or gridview's item click listener of the view
     *
     * @param mViewId   The view id.
     * @param mListener The item on click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemClickListener(int mViewId, AdapterView.OnItemClickListener mListener) {
        AdapterView mView = $(mViewId);
        mView.setOnItemClickListener(mListener);
        return this;
    }

    /**
     * Sets the listview or gridview's item long click listener of the view
     *
     * @param mViewId   The view id.
     * @param mListener The item long click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemLongClickListener(int mViewId, AdapterView.OnItemLongClickListener mListener) {
        AdapterView mView = $(mViewId);
        mView.setOnItemLongClickListener(mListener);
        return this;
    }

    /**
     * Sets the listview or gridview's item selected click listener of the view
     *
     * @param mViewId   The view id.
     * @param mListener The item selected click listener;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnItemSelectedClickListener(int mViewId, AdapterView.OnItemSelectedListener mListener) {
        AdapterView mView = $(mViewId);
        mView.setOnItemSelectedListener(mListener);
        return this;
    }

    /**
     * Sets the on checked change listener of the view.
     *
     * @param mViewId   The view id.
     * @param mListener The checked change listener of compound button.
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setOnCheckedChangeListener(int mViewId, CompoundButton.OnCheckedChangeListener mListener) {
        CompoundButton mView = $(mViewId);
        mView.setOnCheckedChangeListener(mListener);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param mViewId The view id.
     * @param mTag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(int mViewId, Object mTag) {
        View mView = $(mViewId);
        mView.setTag(mTag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param mViewId The view id.
     * @param mKey    The key of tag;
     * @param mTag    The tag;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setTag(int mViewId, int mKey, Object mTag) {
        View mView = $(mViewId);
        mView.setTag(mKey, mTag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param mViewId  The view id.
     * @param mChecked The checked status;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setChecked(int mViewId, boolean mChecked) {
        View mView = $(mViewId);
        // View unable cast to Checkable
        if (mView instanceof CompoundButton) {
            ((CompoundButton) mView).setChecked(mChecked);
        } else if (mView instanceof CheckedTextView) {
            ((CheckedTextView) mView).setChecked(mChecked);
        }
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param mViewId  The view id.
     * @param mAdapter The adapter;
     * @return The BaseViewHolder for chaining.
     */
    public BaseViewHolder setAdapter(int mViewId, Adapter mAdapter) {
        AdapterView mView = $(mViewId);
        mView.setAdapter(mAdapter);
        return this;
    }

}
