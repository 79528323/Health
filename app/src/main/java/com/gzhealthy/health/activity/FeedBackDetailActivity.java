package com.gzhealthy.health.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.FeedBackDetailAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.logger.Logger;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.OssModel;
import com.gzhealthy.health.tool.EditTextFilterUtils;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.Md5Utils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.GlideEngine;
import com.gzhealthy.health.utils.ToastUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Func1;

/**
 * 报故障
 */
public class FeedBackDetailActivity extends BaseAct implements View.OnClickListener,
        BaseQuickAdapter.OnItemChildClickListener {

    private final int MAX_TEXT_COUNT = 200;
    private final int MIN_TEXT_COUNT = 10;

    /**
     * 最大上传图片数量
     */
    private int maxUpload = 4;

    /**
     * 图片路径列表
     */
    private ArrayList<String> imgPaths = new ArrayList();

    /**
     * 图片链接列表
     */
    private ArrayList<String> imgLinks = new ArrayList();

    private FeedBackDetailAdapter mFeedBackDetailAdapter = new FeedBackDetailAdapter();

    @BindView(R.id.tvFunctionAbnormal)
    TextView tvFunctionAbnormal;

    @BindView(R.id.tvOptimizationSuggestions)
    TextView tvOptimizationSuggestions;

    @BindView(R.id.tvOhter)
    TextView tvOhter;

    @BindView(R.id.etContent)
    EditText etContent;

    @BindView(R.id.tvContentLimit)
    TextView tvContentLimit;

    @BindView(R.id.rvAlbum)
    RecyclerView rvAlbum;

    @BindView(R.id.btSubmit)
    Button btSubmit;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_feed_back_detail;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("意见反馈");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);

        tvFunctionAbnormal.setOnClickListener(this);
        tvOptimizationSuggestions.setOnClickListener(this);
        tvOhter.setOnClickListener(this);

        EditTextFilterUtils.getInstance().setEmojiFilter(etContent);
        etContent.addTextChangedListener(watcher);
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_TEXT_COUNT)});
        tvContentLimit.setText("0/" + MAX_TEXT_COUNT);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAlbum.setLayoutManager(linearLayoutManager);
        rvAlbum.setAdapter(mFeedBackDetailAdapter);

        mFeedBackDetailAdapter.setOnItemChildClickListener(this);
        mFeedBackDetailAdapter.setNewData(Arrays.asList(""));

        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        switch (id) {
            case R.id.tvFunctionAbnormal:
            case R.id.tvOptimizationSuggestions:
            case R.id.tvOhter:
                setType(id);
                break;
            case R.id.btSubmit:
                if (tvFunctionAbnormal.isSelected() == false
                        && tvOptimizationSuggestions.isSelected() == false
                        && tvOhter.isSelected() == false) {
                    ToastUtil.showToast("请选择类型");
                    return;
                } else if (etContent.getText().toString().trim().length() < MIN_TEXT_COUNT) {
                    ToastUtil.showToast("请至少用" + MIN_TEXT_COUNT + "个字描述问题");
                    return;
                }

                if (imgPaths.isEmpty()) {
                    submitData();
                } else {
                    submitImg();
                }
                break;
        }
    }


    /**
     * 设置类型
     */
    private void setType(int id) {
        tvFunctionAbnormal.setSelected(false);
        tvOptimizationSuggestions.setSelected(false);
        tvOhter.setSelected(false);
        switch (id) {
            case R.id.tvFunctionAbnormal:
                tvFunctionAbnormal.setSelected(true);
                break;
            case R.id.tvOptimizationSuggestions:
                tvOptimizationSuggestions.setSelected(true);
                break;
            case R.id.tvOhter:
                tvOhter.setSelected(true);
                break;
        }
    }


    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvContentLimit.setText(s.length() + "/" + MAX_TEXT_COUNT);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.cvAdd://添加图片
                checkPermission(maxUpload - imgPaths.size());
                break;
            case R.id.ivDel://删除图片
                adapter.remove(position);
                if (imgPaths.size() >= maxUpload) {
                    imgPaths.remove(position);
                    mFeedBackDetailAdapter.addData("");
                } else {
                    imgPaths.remove(position);
                }
                break;
        }

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
                        imgPaths.addAll(paths);


                        ArrayList<String> data = new ArrayList();
                        data.addAll(imgPaths);
                        if (imgPaths.size() < maxUpload) {
                            data.add("");
                        }
                        mFeedBackDetailAdapter.setNewData(data);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    /**
     * 提交照片后，再提交数据
     */
    private void submitImg() {
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
                    }

                    @Override
                    public void onResponse(OssModel data) {
                        //获取权限成功，开始上传
                        sendUpLoadFile(data.getData().getAccessKeyId(), data.getData().getAccessKeySecret(), data.getData().getSecurityToken(), data.getData().getDomain(), data.getData().getBucket());
                    }
                });
    }

    private void sendUpLoadFile(String accessKeyId, String accessKeySecret, String stsToken, String endpoint, final String bucket) {
        OSSStsTokenCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(accessKeyId, accessKeySecret, stsToken);
        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求书，默认5个
        conf.setMaxConcurrentRequest(5);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        final OSS oss = new OSSClient(aty, endpoint, credentialProvider, conf);

        imgLinks.clear();
        for (String item : imgPaths) {
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
                imgLinks.add(request.getObjectKey());
                if (imgLinks.size() == imgPaths.size()) {
                    hideWaitDialog();
                    submitData();
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                hideWaitDialog();
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

    /**
     * 提交意见反馈
     */
    public void submitData() {
        String type = "-1";
        if (tvFunctionAbnormal.isSelected()) {
            type = "1";
        } else if (tvOptimizationSuggestions.isSelected()) {
            type = "2";
        } else if (tvOhter.isSelected()) {
            type = "3";
        }
        if ("-1".equals(type)) {
            ToastUtil.showToast("请选择类型");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < imgLinks.size(); i++) {
            String img = imgLinks.get(i);
            if (img == null) {
                continue;
            }
            if (i != 0) {
                sb.append(",");
            }
            sb.append(img);
        }

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("type", type);
        hashMap.put("content", etContent.getText().toString());
        hashMap.put("imgs", sb.toString());

        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().addFeedBack(hashMap),
                new CallBack<ComModel>() {
                    @Override
                    public void onResponse(ComModel data) {
                        Toast.makeText(FeedBackDetailActivity.this, "" + data.getMsg(), Toast.LENGTH_SHORT).show();
                        if (data.getCode() == 1) {
                            finish();
                        }
                    }
                });
    }


}
