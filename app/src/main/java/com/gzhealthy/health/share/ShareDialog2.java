package com.gzhealthy.health.share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.tool.ZXingUtils;
import com.gzhealthy.health.utils.BaseUtil;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gzhealthy.health.base.Constants.Api.ossPicPre;
import static com.gzhealthy.health.base.Constants.SharePhone;

public class ShareDialog2 extends Dialog {
    @BindView(R.id.rv_share)
    RecyclerView rvShare;
    private Context mContext;
    private List<ShareIconBean> listIcons = new ArrayList<>();
    private ShareIconAdapter iconAdapter;
    private String mTitle;
    private Bitmap mBitmap;
    private String url;
    private String describe;

    public ShareDialog2(@NonNull Context context) {
        super(context, R.style.CommonDialog);
        this.mContext = context;
    }


    public ShareDialog2(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShareDialog2(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 分享
     */
    public void share(String url, String title, String describe) {
        mTitle = title;
        this.url = url;

        this.describe = describe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_share3);
        ButterKnife.bind(this);
        setUpWindow();
        initView();
        TextView tv_comend = findViewById(R.id.tv_comend);
        ImageView img_share = findViewById(R.id.img_share);
        String da = ossPicPre + SharePhone;
//        GlideUtils.loadCustomeImg(img_share, da);
        Glide.with(img_share.getContext()).load(da).crossFade().into(img_share);

        ImageView imgere = findViewById(R.id.imgere);
        if (!TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""))) {
            UserInfo.DataBean userInfo = DataSupport.findFirst(UserInfo.DataBean.class);
            String lea = String.format(Constants.recommentregist, TextUtils.isEmpty(userInfo.getPhone()) ? "" : userInfo.getPhone());
//            tv_comend.setText(userInfo.getPhone() + "");
            tv_comend.setText(BaseUtil.encryptionPhoneNumber(userInfo.getPhone() + ""));
            Bitmap bitmap = ZXingUtils.createQRImage(lea, 800, 800);
            imgere.setImageBitmap(bitmap);
        } else {
            ShareDialog2.this.dismiss();
            ToastUtils.showErrorShort("请先登录");
        }
    }

    private void initView() {
        listIcons = new ArrayList<>();
//        listIcons.add(new ShareIconBean(R.drawable.umeng_socialize_wechat, "微信", SHARE_MEDIA.WEIXIN));
//        listIcons.add(new ShareIconBean(R.drawable.umeng_socialize_wxcircle, "朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));

        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        rvShare.setLayoutManager(manager);
        iconAdapter = new ShareIconAdapter(null);
        rvShare.setAdapter(iconAdapter);
        iconAdapter.setNewData(listIcons);
        iconAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareIconBean bean = (ShareIconBean) adapter.getItem(position);
//                switch (bean.getType()) {
//                    case QQ:
//                        break;
//                    case QZONE:
//                        break;
//                    case WEIXIN:
//                        UMImage image = new UMImage(mContext, testViewSnapshot(findViewById(R.id.rl_share)));
//                        image.setThumb(image);
//                        image.compressStyle = UMImage.CompressStyle.SCALE;
//                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
//                                .withMedia(image)
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    case WEIXIN_CIRCLE:
//                        UMImage image2 = new UMImage(mContext, testViewSnapshot(findViewById(R.id.rl_share)));
//                        image2.setThumb(image2);
//                        image2.compressStyle = UMImage.CompressStyle.SCALE;
//                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                                .withMedia(image2)
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    default:
//                        break;
//                }

                if (ShareDialog2.this.isShowing()) {
                    ShareDialog2.this.dismiss();
                }


            }
        });

    }

    //设置Dialog显示的位置
    private void setUpWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }

    @OnClick(R.id.ll_cancell)
    public void onViewClicked() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

//    private UMShareListener umShareListener = new UMShareListener() {
//        /**
//         * @param platform 平台类型
//         * @descrption 分享开始的回调
//         */
//        @Override
//        public void onStart(SHARE_MEDIA platform) {
//
//
//        }
//
//        /**
//         * @param platform 平台类型
//         * @descrption 分享成功的回调
//         */
//        @Override
//        public void onResult(SHARE_MEDIA platform) {
//
//        }
//
//        /**
//         * @param platform 平台类型
//         * @param t        错误原因
//         * @descrption 分享失败的回调
//         */
//        @Override
//        public void onError(SHARE_MEDIA platform, Throwable t) {
//
//        }
//
//        /**
//         * @param platform 平台类型
//         * @descrption 分享取消的回调
//         */
//        @Override
//        public void onCancel(SHARE_MEDIA platform) {
//
//        }
//    };

    private Bitmap testViewSnapshot(View view) {
        //使控件可以进行缓存
        view.setDrawingCacheEnabled(true);
        //获取缓存的 Bitmap
        Bitmap drawingCache = view.getDrawingCache();
        //复制获取的 Bitmap
        drawingCache = Bitmap.createBitmap(drawingCache);
        //关闭视图的缓存
        view.setDrawingCacheEnabled(false);
        return drawingCache;
    }
}
