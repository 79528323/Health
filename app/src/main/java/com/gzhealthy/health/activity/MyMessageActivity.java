package com.gzhealthy.health.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.BackgroundMessageAdapter;
import com.gzhealthy.health.api.CallBack;
import com.gzhealthy.health.api.InsuranceApiFactory;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.base.RxBus;
import com.gzhealthy.health.base.RxEvent;
import com.gzhealthy.health.model.PushMessage;
import com.gzhealthy.health.model.base.BaseModel;
import com.gzhealthy.health.tool.HttpUtils;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.DispUtil;
import com.gzhealthy.health.widget.decoration.BgMessageItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 后台消息
 */
public class MyMessageActivity extends BaseAct {

    private boolean islogin;
    private BackgroundMessageAdapter adapter;
    @BindView(R.id.bg_msg_recyler)
    RecyclerView bg_msg_recyler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.img_nodata)
    TextView img_nodata;
    private int page=1;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_background_message;
    }

    public static void instance(Context context){
        Intent intent = new Intent(context, MyMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        ButterKnife.bind(this);
//        mImmersionBar.statusBarColor(R.color.white).barAlpha(0.5f).statusBarDarkFont(true).init();
        islogin = !TextUtils.isEmpty(SPCache.getString(SPCache.KEY_TOKEN,""));
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        setTitle("我的消息");
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);
        getTvRight().setText("一键已读");
        getTvRight().setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        getTvRight().setOnClickListener(v -> {
            List<Integer> list = new ArrayList<>();
            for (int index=0; index < adapter.mList.size(); index++){
                PushMessage.DataBean bean = adapter.mList.get(index);
                if (bean.ifRead == 0){
                    list.add(adapter.mList.get(index).id);
                }
            }

            Integer[] idsArry = list.toArray(new Integer[list.size()]);
            oneKeyRead(idsArry);
        });

        img_nodata.setOnClickListener(v -> {
            page = 1;
            getList();
        });

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page+=1;
            getList();
        });
        refreshLayout.setOnRefreshListener(refreshLayout1 -> {
            page=1;
            getList();
        });

        adapter = new BackgroundMessageAdapter(this, bean -> {
            MyMessageDetailActivity.instance(this,bean);
            oneKeyRead(new Integer[]{bean.id});
        });
        bg_msg_recyler.setLayoutManager(new LinearLayoutManager(this));
        bg_msg_recyler.addItemDecoration(new BgMessageItemDecoration(this, DispUtil.dipToPx(this,15f)));
        bg_msg_recyler.setAdapter(adapter);

        getList();
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("我的-我的消息");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的-我的消息");
    }

    public void getList(){
        Map<String,String> param = new HashMap<>();
        param.put("token",SPCache.getString("token",""));
        param.put("pageNum",String.valueOf(page));
        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().getMyMessageList(param),
                new CallBack<PushMessage>() {

                    @Override
                    public void onResponse(PushMessage data) {
                        if (data.code == 1){
                            if (page <= 1){
                                adapter.refreshData(data.data);
                                bg_msg_recyler.setVisibility(data.data.isEmpty()?View.GONE:View.VISIBLE);
                            }else {
                                adapter.appendData(data.data);
                            }
                        }
                        refreshLayout.finishLoadMore();
                        refreshLayout.finishRefresh();
                    }
                });
    }


    /**
     * 一键已读
     */
    public void oneKeyRead(Integer[] idsArry){
        Map<String,Object> param = new HashMap<>();
        param.put("ids",idsArry);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=UTF-8"), JSON.toJSONString(param));

        HttpUtils.invoke(this, this,
                InsuranceApiFactory.getmHomeApi().setRead(body),
                new CallBack<BaseModel>() {

                    @Override
                    public void onResponse(BaseModel data) {
                        if (data.code == 1){
                            RxBus.getInstance().postEmpty(RxEvent.REFRESH_BACKGROUND_MESSAGE_BDAGE);
                            if (adapter!=null && adapter.getItemCount() > 0){
                                for (int index=0; index < idsArry.length; index++){
                                    Iterator<PushMessage.DataBean> iterator = adapter.mList.iterator();
                                    while (iterator.hasNext()){
                                        PushMessage.DataBean temp = iterator.next();
                                        if (idsArry[index] == temp.id){
                                            temp.ifRead = 1;
//                                            int count = SPCache.getInt(SPCache.KEY_BAGE_COUNT_BACKGROUND_NO_READ,0);
//                                            if (count > 0) {
//                                                count -= 1;
//                                                SPCache.putInt(SPCache.KEY_BAGE_COUNT_BACKGROUND_NO_READ,count);//要把SOS角标数清零
//                                            }

                                            break;
                                        }
                                    }
                                }


                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

//    @Override
//    public void setState(int state) {
//        super.setState(state);
//    }
}
