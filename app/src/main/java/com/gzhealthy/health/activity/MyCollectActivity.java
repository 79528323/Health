package com.gzhealthy.health.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.CollectAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.Constants;
import com.gzhealthy.health.model.CollectListModel;
import com.gzhealthy.health.model.ComModel;
import com.gzhealthy.health.model.CompositeModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.tool.ToastUtils;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.NiUBaiBankDialog;
import com.gzhealthy.health.widget.PopupCompositeUtilsCollect;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * ζηζΆθ
 */
public class MyCollectActivity extends BaseAct {

    private final String TAG = "MyCollectActivity";

    private RecyclerView rv;
    private RelativeLayout rl_msg_list_bottom;
    private TextView tv_msg_list_all, tv_msg_list_delete;
    private PopupCompositeUtilsCollect mCompositeUtilsCollect;

    private CollectAdapter adapter;

    private int page = 1;
    private String typeId = "";
    private Boolean isEdit = true;
    private Boolean isAll = false;
    private HashSet<CollectListModel.DataBean> selectData = new HashSet<>();
    private LinearLayoutManager mLayoutManager;
    private RefreshLayout mRefreshLayout;
    private NiUBaiBankDialog niUBaiBankDialog;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_my_collect;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        setHasBack();
        setTitle("ζηζΆθ");
        setBarRightText("ηΌθΎ");
        setBarLeftIcon(R.mipmap.login_back);
        getCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_order_screen_normal, 0);
        getCenterTextView().setCompoundDrawablePadding(6);

        rl_msg_list_bottom = $(R.id.rl_msg_list_bottom);
        tv_msg_list_all = $(R.id.tv_msg_list_all);
        tv_msg_list_delete = $(R.id.tv_msg_list_delete);

        tv_msg_list_delete.setOnClickListener(this);
        tv_msg_list_all.setOnClickListener(this);

        rv = $(R.id.rv_collect_list);
        ((SimpleItemAnimator) rv.getItemAnimator()).setSupportsChangeAnimations(false);
        mLayoutManager = new LinearLayoutManager(aty);
        rv.setLayoutManager(mLayoutManager);
        adapter = new CollectAdapter();
        rv.setAdapter(adapter);

        View view = LayoutInflater.from(aty).inflate(R.layout.view_state_empty, null);
//        adapter.setEmptyView(view);
        adapter.setOnItemContentListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CollectListModel.DataBean bean = (CollectListModel.DataBean) v.getTag();
//                L.i("η±»εοΌ" + bean.getType());
                if (null != bean) {
                    if (!isEdit) return;
                    switch (bean.getType()) {//ζΆθη±»ε«οΌ1.θ΅θ?―ζη« 
                        case 1:
                            if (!TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""))) {
//                                String title = bean.getName();
                                int id = Integer.valueOf(bean.getCollectionId());
//                                String token = SPCache.getString("token", "");
////                                String url = Constants.NEWS_DETAIL + id + "&token=" + token;
//                                String url = Constants.NEWS_DETAIL + id;
//                                Html5Activity.newIntent(MyCollectActivity.this, "" + title, url, true);
                                ArticleHtmlActivity.newIntent(MyCollectActivity.this,id);
                            } else {
                                ToastUtils.showShort("θ―·εη»ε½ε¦~~");
                            }
                            break;
//                        case 2:
//                            Intent intentAsk = new Intent(aty, AdvanceH5WithCollectActivity.class);
//                            intentAsk.putExtra("title", bean.getName());
//                            String url = Constants.scenicDetail2 + bean.getCollectionId() + "&recommendId=";
//                            intentAsk.putExtra("url", url);//recommendId=111θ·ηΊΏθ·―θ―¦ζδΈζ ·
//                            intentAsk.putExtra("isNeedRightIcon", true);
//                            intentAsk.putExtra("scenicSpotId", String.valueOf(bean.getCollectionId()));
//                            showActivity(aty, intentAsk);
//                            break;
//                        case 3:
//                            Html5Activity.newIntent(aty, "" + bean.getName(), NBAppUtils.getUrlByGuideTirpOrder(bean.getCollectionId()), true);
//                            break;
//                        case 4:
//                            Html5NewShop.newIntent(aty, "" + bean.getName(), "http://app.tofans.com/sc/#/detail/" + bean.getCollectionId(), true);
//                            break;
                        default:
                            break;
                    }
                }
            }
        });

        adapter.setOnItemListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = (int) v.getTag();
                CollectListModel.DataBean dataBean = adapter.getData().get(pos);
                boolean isChooseItem = dataBean.isChoose();
                if (isChooseItem) {
                    dataBean.setChoose(false);
                    selectData.remove(dataBean);
                } else {
                    dataBean.setChoose(true);
                    selectData.add(dataBean);
                }

                //ε€ζ­δΎζ¬‘ιδΈ­ε¨ι
                if (selectData.size() == adapter.getData().size()) {
//                    tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_b, 0, 0, 0);
                    setDrawableBounds(R.mipmap.icon_hook_b);
                    isAll = true;
                } else {
                    setDrawableBounds(R.mipmap.icon_hook_a);
//                    tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_a, 0, 0, 0);
                    isAll = false;
                }
                adapter.notifyItemChanged(pos);
            }
        });

        getCenterTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ηΌθΎηΆζδΈδΈθ½θΏθ‘ηΉε»εζ’
                if (!isEdit) return;
                getCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_order_screen_pressed, 0);
                if (mCompositeUtilsCollect == null) {
                    mCompositeUtilsCollect = new PopupCompositeUtilsCollect(aty,
                            new PopupWindow.OnDismissListener() {
                                @Override
                                public void onDismiss() {
                                    getCenterTextView().setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_order_screen_normal, 0);
                                }
                            }, new PopupCompositeUtilsCollect.OnStateClick() {
                        @Override
                        public void stateClick(CompositeModel type) {
                            getCenterTextView().setText(type.getContent());
                            page=1;
                            typeId = "" + type.getId();
                            collectList();
                        }
                    });
                }
                mCompositeUtilsCollect.showAsDropDown(getCenterTextView());
            }
        });

//        adapter.addAllData(initModelData(), false);

        mRefreshLayout = (RefreshLayout) $(R.id.ptr_layout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                page = 1;
                collectList();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                page++;
                collectList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ζη-ζηζΆθ");
        collectList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ζη-ζηζΆθ");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            //ηΌθΎγεζΆ
            case R.id.tv_custom_toolbar_right:
                // ε€§εζ - θ¦ζζΆθ
                if (adapter.getData().size() > 0) {
                    if (isEdit) {
                        rl_msg_list_bottom.setVisibility(View.VISIBLE);
                        setBarRightText("εζΆ");
                        for (CollectListModel.DataBean bean : adapter.getData()) {
                            bean.setEditMode(true);
                        }
                        isEdit = false;
                        setDrawableBounds(R.mipmap.icon_hook_a);
                    } else {
                        rl_msg_list_bottom.setVisibility(View.GONE);
                        setBarRightText("ηΌθΎ");
                        for (CollectListModel.DataBean bean : adapter.getData()) {
                            bean.setEditMode(false);
                            bean.setChoose(false);
                        }
                        isEdit = true;
                        if (null != selectData && selectData.size() > 0) selectData.clear();//12.26
                        isAll = false;
//                        tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_a, 0, 0, 0);
                        setDrawableBounds(R.mipmap.icon_hook_a);
                    }
                    mRefreshLayout.setEnableRefresh(isEdit);
                    mRefreshLayout.setEnableLoadMore(isEdit);
                }
                break;

            //ε¨ι
            case R.id.tv_msg_list_all:
                selectData.clear();
                if (isAll) {
                    // θ?Ύη½?item ε¨δΈι
                    setDrawableBounds(R.mipmap.icon_hook_a);
//                    tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_a, 0, 0, 0);
                    List<CollectListModel.DataBean> data = adapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        CollectListModel.DataBean bean = data.get(i);
                        bean.setChoose(false);
                    }
                    isAll = false;
                } else {
                    //θ?Ύη½?item ε¨ι
                    setDrawableBounds(R.mipmap.icon_hook_b);
//                    tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_b, 0, 0, 0);
                    List<CollectListModel.DataBean> data = adapter.getData();
                    for (int i = 0; i < data.size(); i++) {
                        CollectListModel.DataBean bean = data.get(i);
                        bean.setChoose(true);
                        selectData.add(bean);
                    }
                    isAll = true;
                }
                break;

            //ε ι€
            case R.id.tv_msg_list_delete:
                //ε¨selectData θΏδΈͺset δΈ­θΏθ‘ε€η
                delCollection();
//                if (selectData.size() > 0) {
//                    niUBaiBankDialog = new NiUBaiBankDialog(MyCollectActivity.this);
//                    niUBaiBankDialog.setContennt("ζ―ε¦η‘?θ?€εζΆζΆθοΌ");
//                    niUBaiBankDialog.setleftOnClick("εζΆ", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            niUBaiBankDialog.dismiss();
//                        }
//                    });
//                    niUBaiBankDialog.setrightOnClick("η‘?ε?", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            delCollection();
//                            niUBaiBankDialog.dismiss();
//                        }
//                    });
//                    niUBaiBankDialog.show();
//                } else {
//                    Toast.makeText(this, "θ―·ιζ©θ¦εζΆηζΆθ", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
        adapter.notifyItemRangeChanged(0, adapter.getItemCount());
    }

    private String deleteIds() {
        String result = "";
        StringBuilder sb = new StringBuilder();

        for (CollectListModel.DataBean bean : selectData) {
            sb.append(bean.getId()).append(",");
        }

        /*Iterator iterator = selectData.iterator();
        while (iterator.hasNext()) {
            Integer position = (Integer) iterator.next();
            sb.append(adapter.getItem(position).getId()).append(",");
        }*/
        if (sb.length() > 0)
            result = sb.substring(0, sb.length() - 1).toString();
        return result;
    }


    public void collectList() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        if (!typeId.equals("0"))
            hashMap.put("type", "" + typeId);//ζΆθη±»ε«οΌ1δΊ§ε 2ζ―ηΉ3ε―ΌζΈΈηΊΏθ·―,ε¨ι¨δΈδΌ 
        hashMap.put("pageNum", "" + page);
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().collectList(hashMap), new CallBack<CollectListModel>() {
            @Override
            public void onResponse(CollectListModel data) {
                if (data.getCode() == 1) {
                    if (mRefreshLayout.getState()== RefreshState.Refreshing) {
                        mRefreshLayout.finishRefresh(0);
                        adapter.setNewData(data.getData());
                    }else if(mRefreshLayout.getState()==RefreshState.Loading){
                        mRefreshLayout.finishLoadMore(0);
                        adapter.addData(data.getData());
                    }else {
                        adapter.setNewData(data.getData());
                    }
                    if (adapter.getData().isEmpty()) {
                        rv.setVisibility(View.GONE);
                    }else{
                        rv.setVisibility(View.VISIBLE);
                    }

                } else {
                    rv.setVisibility(View.GONE);
                    Toast.makeText(MyCollectActivity.this, data.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void delCollection() {
        showWaitDialog();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SPCache.getString("token", ""));
        hashMap.put("ids", "" + deleteIds());
        HttpUtils.invoke(this, this, InsuranceApiFactory.getmHomeApi().delCollection(hashMap), new CallBack<ComModel>() {
            @Override
            public void onResponse(ComModel data) {
                hideWaitDialog();
                if (data.getCode() == 1) {
                    ToastUtils.showShort("εζΆζΆθζε");
                    List<CollectListModel.DataBean> list = adapter.getData();
                    /*for (CollectListModel.DataBean bean : selectData) {
                        list.remove(bean);
                    }*/
                    list.removeAll(selectData);
                    selectData.clear();
                    adapter.notifyDataSetChanged();

                    if (adapter.getData().size() == 0) {
                        rl_msg_list_bottom.setVisibility(View.GONE);
                        setBarRightText("ηΌθΎ");
                        for (CollectListModel.DataBean bean : adapter.getData()) {
                            bean.setEditMode(false);
                            bean.setChoose(false);
                        }
                        isEdit = true;
                        if (null != selectData && selectData.size() > 0) selectData.clear();//12.26
                        isAll = false;
//                        tv_msg_list_all.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_hook_a, 0, 0, 0);
                        setDrawableBounds(R.mipmap.icon_hook_a);
                        mRefreshLayout.setEnableRefresh(isEdit);
                        mRefreshLayout.setEnableLoadMore(isEdit);
                        rv.setVisibility(View.GONE);
                    }
                } else {
                    ToastUtils.showShort(data.getMsg());
                }
            }

            @Override
            public void onfail(Throwable e) {
                super.onfail(e);
                hideWaitDialog();
            }
        });
    }


    public void setDrawableBounds(int resId){
        Drawable drawable = ContextCompat.getDrawable(this,resId);
        drawable.setBounds(0,0, DispUtil.dp2px(this,20),DispUtil.dp2px(this,20));
        tv_msg_list_all.setCompoundDrawables(drawable, null,null,null);
    }


    @Override
    public void setState(int state) {
        loadingPageView.state = Constants.ResponseStatus.STATE_SUCCESS;
        loadingPageView.showPage();
    }


}
