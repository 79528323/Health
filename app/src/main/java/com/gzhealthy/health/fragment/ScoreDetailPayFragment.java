package com.gzhealthy.health.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gzhealthy.health.R;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseFra;
import com.gzhealthy.health.model.ScoreDetailModel;
import com.gzhealthy.health.protocol.BaseView;
import com.gzhealthy.health.tool.DoubleUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * 积分收支明细 -> 支出
 */

public class ScoreDetailPayFragment extends BaseFra implements BaseView {

    private RecomentAdapter mRecomentAdapter;
    private Observable request;

    @BindView(R.id.rv_detail)
    RecyclerView rv_detail;

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this, loadingPageView);

        rv_detail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecomentAdapter = new RecomentAdapter();
        rv_detail.setAdapter(mRecomentAdapter);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_state_empty, null);
        mRecomentAdapter.setEmptyView(view);

        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_score_detail_all;
    }


    @Override
    protected void widgetClick(View view) {
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    private void getData() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type", "1");
        request = InsuranceApiFactory.getmHomeApi().getRecord(hashMap);
        HttpUtils.invoke(this, this, request, new CallBack<ScoreDetailModel>() {
            @Override
            public void onResponse(ScoreDetailModel data) {
                if (data.getCode() == 1) {
                    if (data.getData() == null || data.getData().size() == 0) {

                    } else {
                        mRecomentAdapter.setNewData(data.getData());
                    }
                }
            }
        });
    }

    class RecomentAdapter extends BaseQuickAdapter<ScoreDetailModel.DataBean, BaseViewHolder> {
        public View.OnClickListener getOnClickListener() {
            return mOnClickListener;
        }

        @NonNull
        @Override
        public List<ScoreDetailModel.DataBean> getData() {
            return super.getData();
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        private View.OnClickListener mOnClickListener;

        public RecomentAdapter() {
            super(R.layout.item_score_detail);
        }

        @Override
        protected void convert(BaseViewHolder helper, ScoreDetailModel.DataBean item) {
            if (!TextUtils.isEmpty(item.getCreateTime())) {
                String[] dates = item.getCreateTime().split("-");
                if (dates.length > 2) {
                    String year = dates[0];
                    String month = dates[1];
                    String day = dates[2];
                    helper.setText(R.id.tv_group_year, String.format("%s年%s月", year, month));

                }
            }
            int position = helper.getAdapterPosition();
            if (position != 0) {
                if (!getItemMonth(item).equals(getItemMonth(mData.get(position - 1))) || !getItemYear(item).equals(getItemYear(mData.get(position - 1)))) {
                    helper.setGone(R.id.tv_group_year, true);
                } else {
                    helper.setGone(R.id.tv_group_year, false);
                }
            } else {
                helper.setGone(R.id.tv_group_year, true);
            }
            helper.setText(R.id.tv_date, item.getCreateTime());
            helper.setText(R.id.tv_code, item.getContent());

            if (item.getType() == 1) {//1：支出 2：收入
                helper.setText(R.id.tv_detail, String.format("-%s", item.getNum() + ""));
                helper.setTextColor(R.id.tv_detail, getResources().getColor(R.color.black_text));
            } else {
                helper.setTextColor(R.id.tv_detail, getResources().getColor(R.color.colorPrimary));
                helper.setText(R.id.tv_detail, String.format("+%s",  item.getNum() + ""));
            }

            helper.getView(R.id.ll_detail).setTag(item);
            helper.getView(R.id.ll_detail).setOnClickListener(mItemContentListener);
        }

        private View.OnClickListener mItemContentListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getOnClickListener() != null) {
                    getOnClickListener().onClick(view);
                }
            }
        };

        private String getItemMonth(ScoreDetailModel.DataBean item) {
            String month = "";
            if (!TextUtils.isEmpty(item.getCreateTime())) {
                String[] dates = item.getCreateTime().split("-");
                if (dates.length > 1) {
                    String year = dates[0];
                    month = dates[1];
                }
            }
            return month;
        }

        private String getItemYear(ScoreDetailModel.DataBean item) {
            String year = "";
            if (!TextUtils.isEmpty(item.getCreateTime())) {
                String[] dates = item.getCreateTime().split("-");
                if (dates.length > 0) {
                    year = dates[0];
                }
            }
            return year;
        }

    }

}
