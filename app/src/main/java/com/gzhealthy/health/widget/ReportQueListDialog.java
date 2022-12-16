package com.gzhealthy.health.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.ReportQueAdapter;
import com.gzhealthy.health.model.QuePaper;
import com.gzhealthy.health.widget.decoration.HeartRateDividerItemDecoration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Justin_Liu
 * on 2021/6/17
 */
public class ReportQueListDialog extends Dialog {
    private Context context;
    private RecyclerView recyclerView;
    private ReportQueAdapter adapter;

    public ReportQueListDialog(Context context, QuePaper.DataBean bean,OnSelctedQuestionCallBack onSelctedQuestionCallBack) {
        super(context);
        this.context = context;
        adapter = new ReportQueAdapter(context, bean.questionList, v -> {
            QuePaper.DataBean.QuestionListBean tag = (QuePaper.DataBean.QuestionListBean) v.getTag();
            onSelctedQuestionCallBack.selectedQue(tag.questionId);
        });

        View root = getLayoutInflater().from(context).inflate(R.layout.dialog_report_que_list, null);
        recyclerView =root.findViewById(R.id.que_recyler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new HeartRateDividerItemDecoration(context));
        recyclerView.setAdapter(adapter);
        setContentView(root);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        //设置窗体的宽高来控制Dialog的大小
//        if (windowWidth == 0 || windowHeight == 0) {
//            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        } else {
//            params.width = windowWidth;
//            params.height = windowHeight;
//        }
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = metrics.heightPixels - ((metrics.heightPixels/7) * 2);
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCanceledOnTouchOutside(true);
    }


    public interface OnSelctedQuestionCallBack{
        void selectedQue(int questionId);
    }
}
