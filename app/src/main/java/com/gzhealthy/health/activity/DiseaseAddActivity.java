package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FeedBackDetailAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.base.IntentParam;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.DiseaseRecord;
import com.gzhealthy.health.model.DiseaseRecordDetail;
import com.gzhealthy.health.model.MedicationRecord;
import com.gzhealthy.health.model.OssModel;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.utils.GlideEngine;
import com.gzhealthy.health.utils.PickerViewUtils;
import com.gzhealthy.health.utils.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.functions.Func1;
import top.limuyang2.ldialog.LDialog;
import top.limuyang2.ldialog.base.BaseLDialog;
import top.limuyang2.ldialog.base.ViewHandlerListener;
import top.limuyang2.ldialog.base.ViewHolder;

/**
 * →_→
 * 769856557@qq.com
 * yangyong
 */
public class DiseaseAddActivity extends BaseAct {
    /**
     * 最大上传图片数量
     */
    int maxUpload = 6;

    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.etHospitalName)
    EditText etHospitalName;
    @BindView(R.id.etDepartMent)
    EditText etDepartMent;
    @BindView(R.id.rvAlbum)
    RecyclerView rvAlbum;
    @BindView(R.id.etDesc)
    EditText etDesc;
    @BindView(R.id.etDiagnosis)
    EditText etDiagnosis;
    @BindView(R.id.vBottom)
    View vBottom;
    @BindView(R.id.delete)
    TextView delete;
    int id = 0;
    List<String> imageUrl = new ArrayList<>();
    FeedBackDetailAdapter mFeedBackDetailAdapter = new FeedBackDetailAdapter();
    int uploadCount = 0;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_disease_add;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("新增病历记录");
        getToolBar().setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        getCenterTextView().setTextColor(ContextCompat.getColor(this, R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        setMedicationRight(v -> {
            save();
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rvAlbum.setLayoutManager(gridLayoutManager);
        rvAlbum.setAdapter(mFeedBackDetailAdapter);

        mFeedBackDetailAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            switch (view.getId()) {
                case R.id.cvAdd://添加图片
                    checkPermission(maxUpload - imageUrl.size());
                    break;
                case R.id.ivDel://删除图片
                    adapter.remove(position);
                    if (imageUrl.size() >= maxUpload) {
                        imageUrl.remove(position);
                        mFeedBackDetailAdapter.addData("");
                    } else {
                        imageUrl.remove(position);
                    }
                    break;
            }
        });

        //输入法遮挡输入框处理
        KeyboardUtils.registerSoftInputChangedListener(getWindow(), height -> {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vBottom.getLayoutParams();
            layoutParams.height = height;
            vBottom.setLayoutParams(layoutParams);
        });

        id = getIntent().getIntExtra(IntentParam.ID,id);
        if (id > 0){
            setTitle("编辑病历记录");
            getDetail();
            delete.setVisibility(View.VISIBLE);
        }else
            mFeedBackDetailAdapter.setNewData(Arrays.asList(""));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.unregisterSoftInputChangedListener(getWindow());
    }

    @OnClick({R.id.llType, R.id.llDate,R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llType:
                selectType();
                break;
            case R.id.llDate:
                selectSeeTime();
                break;
            case R.id.delete://删除记录
                deleteDailog();
                break;
        }

    }

    private TimePickerView seeTimePick;

    /**
     * 选择开始时间
     */
    private void selectSeeTime() {
        if (seeTimePick == null) {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.YEAR, startDate.get(Calendar.YEAR) - 100);
            Calendar endData = Calendar.getInstance();
            seeTimePick = PickerViewUtils.getTimePickerView(this, startDate, endData, endData, "就诊日期", (date, v) -> {
                String dateStr = TimeUtils.millis2String(date.getTime(), "yyyy-MM-dd");
                tvDate.setText(dateStr);
            });
        }
        KeyboardUtils.hideSoftInput(this);
        seeTimePick.show();
    }

//    private TimePickerView tpvEndTime;

//    /**
//     * 选择结束时间
//     */
//    private void selectEndTime() {
//        String startTimeStr = tvStartTime.getText().toString();
//        if (TextUtils.isEmpty(startTimeStr)) {
//            ToastUtil.showToast("请选择就诊日期");
//            return;
//        }
//
//        Calendar startDate = Calendar.getInstance();
//        startDate.setTimeInMillis(TimeUtils.string2Millis(startTimeStr, "yyyy-MM-dd"));
//        Calendar endData = Calendar.getInstance();
//        endData.set(Calendar.YEAR, endData.get(Calendar.YEAR) + 100);
//        tpvEndTime = PickerViewUtils.getTimePickerView(this, startDate, endData, startDate, "结束时间", (date, v) -> {
//            String dateStr = TimeUtils.millis2String(date.getTime(), "yyyy-MM-dd");
//            tvEndTime.setText(dateStr);
//        });
//        KeyboardUtils.hideSoftInput(this);
//        tpvEndTime.show();
//    }


    private OptionsPickerView typeListPick;
    private List<String> typeList = Arrays.asList(
            "门诊",
            "住院");

    /**
     * 选择用药频次
     */
    private void selectType() {
        if (typeListPick == null) {
            typeListPick =
                    PickerViewUtils.OptionsPickerView(
                            this,
                            "病历类型",
                            (options1, options2, options3, v) -> {
                                String type = typeList.get(options1);
                                tvType.setText(type);
                            }
                    );
            typeListPick.setPicker(typeList);
        }
        KeyboardUtils.hideSoftInput(this);
        typeListPick.show();
    }

    private void save() {
        String type = tvType.getText().toString();
        if (TextUtils.isEmpty(type)) {
            ToastUtil.showToast("请选择病历类型");
            return;
        }

        String name = etHospitalName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast("请输入医院名称");
            return;
        }
        if (name.length() >30){
            ToastUtil.showToast("医院名称请在30字内");
            return;
        }

        String department = etDepartMent.getText().toString();
        if (!TextUtils.isEmpty(department)){
            if (department.length() > 30){
                ToastUtil.showToast("科室请在30字内");
                return;
            }
        }

        String seeDate = tvDate.getText().toString();
        if (TextUtils.isEmpty(seeDate)) {
            ToastUtil.showToast("请选择就诊日期");
            return;
        }

        String diagnosis = etDiagnosis.getText().toString();
        if (TextUtils.isEmpty(diagnosis)) {
            ToastUtil.showToast("请输入诊断");
            return;
        }else if (diagnosis.length() > 50){
            ToastUtil.showToast("诊断请在50字内");
            return;
        }

        String desc = etDesc.getText().toString();
        if (!TextUtils.isEmpty(desc)){
            if (desc.length() > 200){
                ToastUtil.showToast("病况描述请在200字内");
                return;
            }
        }

        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("hospital", name);
        param.put("department", department);
        param.put("seeDate", seeDate);
        param.put("diagnosis", diagnosis);
        param.put("description", desc);
        if (id > 0){
            param.put("id",String.valueOf(id));
        }

        if (!imageUrl.isEmpty()){
            String[] arrayUrl = imageUrl.toArray(new String[imageUrl.size()]);
            param.put("imgUrl",arrayUrl);
        }

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));
        Observable<BaseModel> observable = null;
        if (id > 0){
            observable = InsuranceApiFactory.getmHomeApi().udpateDiseaseRecord(body);
        }else {
            observable = InsuranceApiFactory.getmHomeApi().saveDiseaseRecord(body);
        }
        HttpUtils.invoke(this, this,observable,
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        ToastUtil.showToast(data.getMsg());
                        if (data.getCode() == 1) {
                            finish();
                        }
                    }
                });
    }



    public void deleteRecord(){
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().deleteDiseaseRecord(param),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {

                        if (data.code == 1) {
                            goBack();
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }



    /**
     * 检查相关权限
     *
     * @param max 图片最大选择数
     */
    private void checkPermission(int max) {
        PermissionUtils.permissionGroup(PermissionConstants.STORAGE, PermissionConstants.CAMERA)
                .callback(new PermissionUtils.SimpleCallback() {
                    @Override
                    public void onGranted() {
                        openPictureSelector(max);
                    }

                    @Override
                    public void onDenied() {

                    }
                }).request();
    }



    /**
     * 打开图片选择器
     *
     * @param max 图片最大选择数
     */
    private void openPictureSelector(int max) {
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .isCompress(true)
                .maxSelectNum(max)
                .imageEngine(GlideEngine.createGlideEngine())
                .forResult(new OnResultCallbackListener() {
                    @Override
                    public void onResult(List result) {
                        ArrayList<String> paths = new ArrayList();
                        for (Object item : result) {
                            String compressPath = ((LocalMedia) item).getCompressPath();
                            if (compressPath != null) {
                                paths.add(compressPath);
                            }
                        }
                        uploadCount = paths.size();
                        submitImg(paths);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }



    public void getDetail(){
        Map<String, String> param = new HashMap<>();
        param.put("id", String.valueOf(id));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getDiseaseRecordDetail(param),
                new CallBack<DiseaseRecordDetail>() {

                    @Override
                    public void onResponse(DiseaseRecordDetail data) {
                        if (data.code == 1) {
                            tvType.setText(data.data.type);
                            tvDate.setText(data.data.seeDate);
                            etHospitalName.setText(data.data.hospital);
                            etDepartMent.setText(data.data.department);
                            etDiagnosis.setText(data.data.diagnosis);
                            etDesc.setText(data.data.description);
                            if (data.data.imgUrl!=null&&!data.data.imgUrl.isEmpty()){
                                imageUrl.addAll(data.data.imgUrl);
                                mFeedBackDetailAdapter.setNewData(data.data.imgUrl);
                                if (imageUrl.size() < maxUpload){
                                    mFeedBackDetailAdapter.addData("");
                                }
                            }else
                                mFeedBackDetailAdapter.setNewData(Arrays.asList(""));
                        }else
                            ToastUtil.showToast(data.msg);
                    }
                });
    }



    /**
     * 提交照片后，再提交数据
     */
    private void submitImg(List<String> uploadImgList) {
        Map<String, String> params = new HashMap<>();
        params.put("sign", Md5Utils.encryptH("gzhealthy"));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getKey(params).map((Func1<OssModel, OssModel>) ossModel -> ossModel)
                , new CallBack<OssModel>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        showWaitDialog("正在上传图片");
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideWaitDialog();
                        uploadCount = 0;
                    }

                    @Override
                    public void onResponse(OssModel data) {
                        //获取权限成功，开始上传
                        sendUpLoadFile(uploadImgList,data.getData().getAccessKeyId(), data.getData().getAccessKeySecret(), data.getData().getSecurityToken(), data.getData().getDomain(), data.getData().getBucket());
                    }
                });
    }

    private void sendUpLoadFile(List<String> uploadImgList,String accessKeyId, String accessKeySecret, String stsToken, String endpoint, final String bucket) {
        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, stsToken);
        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求书，默认5个
        conf.setMaxConcurrentRequest(6);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        final OSS oss = new OSSClient(aty, endpoint, credentialProvider, conf);

//        imgLinks.clear();
        for (int index=0; index < uploadImgList.size(); index++) {
            String item = uploadImgList.get(index);
            sendUpFileRequest(oss, bucket, System.currentTimeMillis() + ".png", new File(item));
        }
    }

    private void sendUpFileRequest(OSS oss, String bucket, final String fileName, File file) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(bucket, fileName, file.getPath());
        // 异步上传时可以设置进度回调
        put.setProgressCallback((request, currentSize, totalSize) -> Logger.e("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize));

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                uploadCount -=1;
                String uploadUrl = Constants.ImageResource.OSSHEAD + request.getObjectKey();
                Log.e("111","sendUpFileRequest==="+uploadUrl);
                imageUrl.add(uploadUrl);

                if (uploadCount <= 0) {
                    hideWaitDialog();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.addAll(imageUrl);
                            mFeedBackDetailAdapter.setNewData(arrayList);
                            if (arrayList.size() < maxUpload){
                                mFeedBackDetailAdapter.addData("");
                            }
                        }
                    });
                }

            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                hideWaitDialog();
                uploadCount = 0;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Logger.e("ErrorCode", serviceException.getErrorCode());
                    Logger.e("RequestId", serviceException.getRequestId());
                    Logger.e("HostId", serviceException.getHostId());
                    Logger.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }


    public void deleteDailog(){
        LDialog.Companion.init(getSupportFragmentManager())
                .setLayoutRes(R.layout.dialog_unbind_watch)
                .setGravity(Gravity.CENTER)
                .setWidthScale(0.8f)
//                .setVerticalMargin(0.09f)
                .setViewHandlerListener(new ViewHandlerListener() {
                    @Override
                    public void convertView(ViewHolder viewHolder, BaseLDialog<?> baseLDialog) {
                        ((TextView)viewHolder.getView(R.id.title)).setText("病历删除后将不可恢复,\n是否确认删除");
                        viewHolder.getView(R.id.cancel).setOnClickListener(v -> {
                            baseLDialog.dismiss();
                        });

                        viewHolder.getView(R.id.confirm).setOnClickListener(v -> {
                            deleteRecord();
                            baseLDialog.dismiss();
                        });
                    }
                })
                .show();
    }
}
