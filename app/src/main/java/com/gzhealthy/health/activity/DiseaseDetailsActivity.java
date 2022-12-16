package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FeedBackDetailAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.model.DiseaseRecordDetail;
import com.gzhealthy.health.model.MedicationRecordDetailModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class DiseaseDetailsActivity extends BaseAct {
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvHospital)
    TextView tvHospital;
    @BindView(R.id.tvDiagnosis)
    TextView tvDiagnosis;
    @BindView(R.id.tvDepartment)
    TextView tvDepartment;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.rvAlbum)
    RecyclerView rvAlbum;

    FeedBackDetailAdapter mFeedBackDetailAdapter = null;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_disease_details;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("病历详情");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        mFeedBackDetailAdapter = new FeedBackDetailAdapter(R.layout.item_feed_back_detail,true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rvAlbum.setLayoutManager(gridLayoutManager);
        rvAlbum.setAdapter(mFeedBackDetailAdapter);
        mFeedBackDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.ivImage){//v1.8.0再加这功能
//                Bundle bundle = new Bundle();
//                String path = mFeedBackDetailAdapter.getData().get(position);
//                bundle.putString(IntentParam.BIG_PHOTO,path);
//                ShowBigPhotoActivity.instance(DiseaseDetailsActivity.this,bundle);
            }

        });

        int id = getIntent().getIntExtra(IntentParam.ID,0);
        getData(String.valueOf(id));
    }


    private void getData(String id) {
        Map<String, String> param = new HashMap<>();
        param.put("id", id);
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getDiseaseRecordDetail(param),
                new CallBack<DiseaseRecordDetail>() {

                    @Override
                    public void onResponse(DiseaseRecordDetail data) {
                        if (data.code == 1) {
                            tvType.setText(data.data.type);
                            tvHospital.setText(data.data.hospital);
                            tvDepartment.setText(data.data.department);
                            tvDescription.setText(data.data.description);
                            tvDiagnosis.setText(data.data.diagnosis);
                            tvDate.setText(data.data.seeDate);
                            if (data.data.imgUrl!=null && !data.data.imgUrl.isEmpty()){
                                rvAlbum.setVisibility(View.VISIBLE);
                                mFeedBackDetailAdapter.setNewData(data.data.imgUrl);
                            }
                        } else {
                            ToastUtil.showToast(data.msg);
                        }
                    }
                });
    }
}
