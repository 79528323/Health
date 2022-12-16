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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gzhealthy.health.R;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.UserInfo;
import com.gzhealthy.health.tool.GlideUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ZXingUtils;
import com.gzhealthy.health.utils.BaseUtil;
import com.gzhealthy.health.widget.CircleImageView;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareDialog extends Dialog {
    @BindView(R.id.rv_share)
    RecyclerView rvShare;
    @BindView(R.id.ll_cancell)
    RelativeLayout llCancel;
    private Context mContext;
    private List<ShareIconBean> listIcons = new ArrayList<>();
    private ShareIconAdapter iconAdapter;
    private String mTitle;
    private Bitmap mBitmap;
    private String url;
    private String describe;
    private ShareBean shareBean;

    public ShareDialog(@NonNull Context context, ShareBean shareBean) {
        super(context, R.style.CommonDialog);
        this.mContext = context;
        this.shareBean = shareBean;
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * 分享
     */
    public void share(String url, String title, String describe) {
        mTitle = title;
        this.url = url;
//        mBitmap = mShareBitmap;
        this.describe = describe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_share);
        ButterKnife.bind(this);
        setUpWindow();
        initView();

    }

    private void initView() {
//        listIcons.add(new ShareIconBean(R.drawable.umeng_socialize_qq, "QQ", SHARE_MEDIA.QQ));
        listIcons = new ArrayList<>();
//        listIcons.add(new ShareIconBean(R.mipmap.icon_wechat_logo, "微信好友", SHARE_MEDIA.WEIXIN));
//        listIcons.add(new ShareIconBean(R.mipmap.icon_wxcircle_logo, "朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
//        listIcons.add(new ShareIconBean(R.mipmap.icon_ewrd, "海报", SHARE_MEDIA.QZONE));
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        rvShare.setLayoutManager(manager);
        iconAdapter = new ShareIconAdapter(null);
        rvShare.setAdapter(iconAdapter);
        iconAdapter.setNewData(listIcons);
        iconAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ShareIconBean bean = (ShareIconBean) adapter.getItem(position);
//                UMImage image = new UMImage(mContext, Constants.SHAREIMG);
////                UMImage image = new UMImage(mContext, mBitmap);
////                UMWeb web = new UMWeb(Constant.SHARE_URL + url);//分享的连接
//                UMWeb web = new UMWeb(url);//分享的连接
//                web.setTitle(mTitle);//标题
//                web.setThumb(image);  //缩略图
//                web.setDescription(describe);//描述
//                switch (bean.getType()) {
//                    case QQ:
//
//                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QQ)
//                                .withMedia(web)
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    case QZONE:
//                        baoshare b = new baoshare(mContext, shareBean);
//                        b.show();
////                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QZONE)
////                                .withMedia(web)
////                                .setCallback(umShareListener)
////                                .share();
//                        break;
//                    case WEIXIN:
//
//                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
//                                .withMedia(web)
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    case WEIXIN_CIRCLE:
//
//                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                                .withMedia(web)
//                                .setCallback(umShareListener)
//                                .share();
//                        break;
//                    default:
//                        break;
//                }
                if (ShareDialog.this.isShowing()) {
                    ShareDialog.this.dismiss();
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

    class baoshare extends Dialog {
        private Context mContext;
        private ShareBean shareBean;

        public baoshare(@NonNull Context context, ShareBean shareBean) {
            super(context, R.style.CommonDialog);
            this.mContext = context;
            this.shareBean = shareBean;
        }

        public baoshare(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        protected baoshare(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.layout_dialog_share2);
            CircleImageView img_cr_head = findViewById(R.id.img_cr_head);
            TextView tv_lname = findViewById(R.id.tv_lname);
            TextView tv_lphone = findViewById(R.id.tv_lphone);
            findViewById(R.id.ll_cancell).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    baoshare.this.dismiss();
                }
            });
            Window window = getWindow();
            window.getDecorView().setPadding(20, 0, 20, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;
            window.setAttributes(layoutParams);
//            window.getDecorView().setBackgroundColor(Color.TRANSPARENT);
            RecyclerView rv_share2 = this.findViewById(R.id.rv_share2);
            if (!TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""))) {
                UserInfo.DataBean dataBean = DataSupport.findFirst(UserInfo.DataBean.class);
                GlideUtils.loadCustomeImg(img_cr_head, Constants.Api.ossPicPre + dataBean.getHeadPic());
                tv_lname.setText(dataBean.getNickName() + "");
//                tv_lphone.setText(dataBean.getPhone() + "");
                tv_lphone.setText(BaseUtil.encryptionPhoneNumber(dataBean.getPhone() + ""));
                img_cr_head.setVisibility(View.VISIBLE);
                tv_lname.setVisibility(View.VISIBLE);
                tv_lphone.setVisibility(View.VISIBLE);
            } else {
                img_cr_head.setVisibility(View.INVISIBLE);
                tv_lname.setVisibility(View.INVISIBLE);
                tv_lphone.setVisibility(View.INVISIBLE);
            }
            listIcons = new ArrayList<>();
//            listIcons.add(new ShareIconBean(R.drawable.umeng_socialize_wechat, "微信", SHARE_MEDIA.WEIXIN));
//            listIcons.add(new ShareIconBean(R.drawable.umeng_socialize_wxcircle, "朋友圈", SHARE_MEDIA.WEIXIN_CIRCLE));
            GridLayoutManager manager = new GridLayoutManager(mContext, 4);
            rv_share2.setLayoutManager(manager);
            ShareIconAdapter iconAdapter2 = new ShareIconAdapter(null);
            rv_share2.setAdapter(iconAdapter2);
            iconAdapter2.setNewData(listIcons);
            ImageView img_ewm = findViewById(R.id.img_ewm);
            ImageView imageView = findViewById(R.id.img_los);
            TextView tvl_title = findViewById(R.id.tvl_title);
            TextView tvl_price = findViewById(R.id.tvl_price);
            TextView tv_ll_code = findViewById(R.id.tv_ll_code);
            TextView tv_ll_dis = findViewById(R.id.ll_dis);
            GlideUtils.loadCustomeImg(imageView, Constants.Api.ossPicPre + shareBean.getData().getProductPhoto());
            Bitmap bitmap = ZXingUtils.createQRImage(shareBean.getData().getEwmurl(), 400, 400);
            img_ewm.setImageBitmap(bitmap);
            tvl_title.setText(shareBean.getData().getSellerProductName());
            tvl_price.setText("" + (shareBean.getData().getSellPrice() + "").substring(0, (shareBean.getData().getSellPrice() + "").length() - 2) + "");
            tv_ll_code.setText("产品编号：" + shareBean.getData().getProductCode());
            tv_ll_dis.setText(shareBean.getData().getShareSlogan());
            iconAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ShareIconBean bean = (ShareIconBean) adapter.getItem(position);

//                    UMImage image = new UMImage(mContext, Constants.SHAREIMG);
//                    UMWeb web = new UMWeb( url);//分享的连接
//                    web.setTitle(mTitle);//标题
//                    web.setThumb(image);  //缩略图
//                    web.setDescription(describe);//描述
//                    ImageView img_los = findViewById(R.id.img_los);
//                    img_los.setDrawingCacheEnabled(true);
//                    Bitmap bitmap = Bitmap.createBitmap(img_los.getDrawingCache());
//                    img_los.setDrawingCacheEnabled(false);
//                    UMImage image = new UMImage(mContext, testViewSnapshot(findViewById(R.id.lv_hh)));
//                    image.setThumb(image);
//                    image.compressStyle = UMImage.CompressStyle.SCALE;
//                    switch (bean.getType()) {
//                        case QQ:
//
////                            new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QQ)
////                                    .withMedia(web)
////                                    .setCallback(umShareListener)
////                                    .share();
//                            break;
//                        case QZONE:
////                            ToastUtils.showErrorShort("海报分享");
////                            baoshare b = new baoshare(mContext);
////                            b.show();
////                        new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QZONE)
////                                .withMedia(web)
////                                .setCallback(umShareListener)
////                                .share();
//                            break;
//                        case WEIXIN:
//                            new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .share();
//                            break;
//                        case WEIXIN_CIRCLE:
//
//                            new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
//                                    .withMedia(image)
//                                    .setCallback(umShareListener)
//                                    .share();
//                            break;
//                        default:
//                            break;
//                    }

                    if (baoshare.this.isShowing()) {
                        baoshare.this.dismiss();
                    }


                }
            });
        }

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
}
