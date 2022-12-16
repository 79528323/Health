package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.tool.TDevice;

import java.util.ArrayList;

public class TypeSelectButtonDialog extends Dialog {
    Context context;
    ArrayList<String> data;
    private RecyclerView re_view;
    View.OnClickListener onClickListener;

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TypeSelectButtonDialog(@NonNull Context context, @NonNull ArrayList<String> data) {
        super(context, R.style.BottomDialogs);
        this.context = context;
        this.data = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_type_select);
        getWindow().setGravity(Gravity.BOTTOM);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().getAttributes().width = TDevice.getScreenWidth(context);
        TextView textView = (TextView) findViewById(R.id.tv_cancle);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TypeSelectButtonDialog.this.dismiss();
            }
        });
        re_view = (RecyclerView) findViewById(R.id.re_view);
        re_view.setLayoutManager(new LinearLayoutManager(context));
        re_view.setAdapter(new DataAdapter());
    }

    class DataHodel extends RecyclerView.ViewHolder {

        private TextView textView;

        public DataHodel(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_show);
        }
    }

    class DataAdapter extends RecyclerView.Adapter<DataHodel> {
        @Override
        public DataHodel onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DataHodel(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_select, parent, false));
        }

        @Override
        public void onBindViewHolder(DataHodel holder, int position) {
            holder.textView.setText(data.get(position));
            if (null != onClickListener) {
                holder.textView.setTag(holder.textView.getText().toString());
                holder.textView.setOnClickListener(onClickListener);
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
