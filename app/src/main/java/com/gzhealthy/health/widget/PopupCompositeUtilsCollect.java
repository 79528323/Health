package com.gzhealthy.health.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.CompositeAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.model.CompositeModel;

import java.util.ArrayList;
import java.util.List;


public class PopupCompositeUtilsCollect extends PopupWindow implements View.OnClickListener {
    private BaseAct baseAct;

    public View getParentView() {
        return parentView;
    }

    private View parentView;
    private int type;
    private OnStateClick mOnStateClick;
    private RecyclerView rv_composite;
    private CompositeAdapter adapter;
    private List<CompositeModel> datas;

    public BaseQuickAdapter.OnItemClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(BaseQuickAdapter.OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private BaseQuickAdapter.OnItemClickListener onClickListener;


    public interface OnStateClick {
        void stateClick(CompositeModel type);
    }


    public PopupCompositeUtilsCollect(Context context, OnDismissListener onDismissListener, OnStateClick onStateClick) {
        super(context);
        this.baseAct = (BaseAct) context;
        this.mOnStateClick = onStateClick;
        this.parentView = LayoutInflater.from(context).inflate(R.layout.popup_for_more_composite, null);
        setContentView(parentView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        setOnDismissListener(onDismissListener);
        initUI();
    }


    private void initUI() {
        rv_composite = (RecyclerView) parentView.findViewById(R.id.rv_composite);
        parentView.findViewById(R.id.view_transparent).setOnClickListener(this);
        rv_composite.setLayoutManager(new LinearLayoutManager(baseAct));
        datas = new ArrayList<>();
        datas.add(new CompositeModel(0, false, "????????????"));
        datas.add(new CompositeModel(1, false, "??????"));
        datas.add(new CompositeModel(2, false, "??????"));
        adapter = new CompositeAdapter(datas);
        rv_composite.setAdapter(adapter);
        adapter.setOnClickListener(onItemClickListener);
    }


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = (int) view.getTag();//??????????????????

            for (int i = 0; i < adapter.getData().size(); i++) {
                CompositeModel cm = adapter.getData().get(i);

                if (i == pos) {
                    cm.setSelect(true);
                } else {
                    cm.setSelect(false);
                }

            }
            mOnStateClick.stateClick(adapter.getData().get(pos));
            adapter.notifyDataSetChanged();
            closeWindow();
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.view_transparent) {
            closeWindow();
        }
    }

    public void closeWindow() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

//    /**
//     * ???android7.0???????????????????????????PopuWindow?????????????????????????????????????????? MATCH_PARENT,??????PopuWindow????????????????????????
//     * ????????? view???????????????????????????PopuWindow?????????view??????????????????????????????PopuWindow???????????????????????????????????????????????????
//     * ??????????????????????????????PopuWindow????????????????????????showAsDropDown?????????
//     *
//     * @param anchor
//     */
//    @Override
//    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            Rect visibleFrame = new Rect();
//            anchor.getGlobalVisibleRect(visibleFrame);
//            int height = anchor.getResources().getDisplayMetrics().heightPixels;// - visibleFrame.bottom;
//            setHeight(height);
//        }
//        super.showAsDropDown(anchor);
//    }

}
