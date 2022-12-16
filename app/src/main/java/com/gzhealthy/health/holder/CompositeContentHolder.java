package com.gzhealthy.health.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.CompositeAdapter;
import com.gzhealthy.health.model.CompositeModel;


public class CompositeContentHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "CompositeContentHolder";
    private CompositeAdapter adapter;
    private ImageView iv;
    private TextView tv, ad_type_tv, price_tv;
    private LinearLayout content_ll;
    private String imgUrl;

    public CompositeContentHolder(View itemView, CompositeAdapter compositeAdapter) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.composite_des_tv);
        iv = (ImageView) itemView.findViewById(R.id.iv_composite_select);
        content_ll = (LinearLayout) itemView.findViewById(R.id.content_ll);
        this.adapter = compositeAdapter;
    }

    public void bindData(CompositeModel dataBean, CompositeAdapter indexHotTypeAdapter, int position) {
        tv.setText(dataBean.getContent());
        iv.setVisibility(dataBean.isSelect() ? View.VISIBLE : View.GONE);
        tv.setTextColor(dataBean.isSelect() ? itemView.getContext().getResources().getColor(R.color.colorPrimary)
                : itemView.getContext().getResources().getColor(R.color.text_color_1));
        content_ll.setTag(position);
        content_ll.setOnClickListener(onItemClickListener);
    }


    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (adapter != null && adapter.getOnClickListener() != null) {
                adapter.getOnClickListener().onClick(view);
            }
        }
    };

}
