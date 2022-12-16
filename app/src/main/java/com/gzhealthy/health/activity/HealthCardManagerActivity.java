package com.gzhealthy.health.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gzhealthy.health.R;
import com.gzhealthy.health.adapter.DragOtherAdapter;
import com.gzhealthy.health.adapter.DragSortAdapter;
import com.gzhealthy.health.base.BaseAct;
import com.gzhealthy.health.callback.OnDragSortItemTouchHelperCallBack;
import com.gzhealthy.health.model.HealthCard;
import com.gzhealthy.health.tool.SPCache;
import com.gzhealthy.health.utils.ToastUtil;
import com.gzhealthy.health.widget.decoration.HealthCardItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 客服
 */
public class HealthCardManagerActivity extends BaseAct {
    @BindView(R.id.drag_recycler)
    RecyclerView drag_recycler;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    DragSortAdapter adapter;
    DragOtherAdapter otherAdapter;

    List<HealthCard> mList , otherList;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_health_card_manager;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("卡片管理");
        ButterKnife.bind(this);
        setstatueColor(R.color.white);
        setBarLeftIcon(R.mipmap.login_back);
        getToolBar().setBackgroundColor(getResources().getColor(R.color.white));
        getCenterTextView().setTextColor(getResources().getColor(R.color.text_color_1));
        getCenterTextView().setTextSize(18);
        hideOrShowToolbar(false);

        mList = new ArrayList<>();
        otherList = new ArrayList<>();
        getDBList(getSPList());

        adapter = new DragSortAdapter(this, list -> {
            mList.clear();
            mList.addAll(list);
            insertDB();
        },v -> {
            int position = (int) v.getTag();
            if (mList.size() > 2){
                HealthCard card = mList.remove(position);
                adapter.refreshData(mList);
                otherList.add(0,card);
                otherAdapter.refreshData(otherList);
                insertDB();
            }else {
                ToastUtil.showToast("请至少选择两个");
            }

        });
        drag_recycler.setLayoutManager(new LinearLayoutManager(this));
        drag_recycler.addItemDecoration(new HealthCardItemDecoration(this));
        drag_recycler.setHasFixedSize(true);
        drag_recycler.setNestedScrollingEnabled(false);
        drag_recycler.setAdapter(adapter);
        adapter.refreshData(mList);

        ItemTouchHelper.Callback callback = new OnDragSortItemTouchHelperCallBack(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(drag_recycler);

        otherAdapter = new DragOtherAdapter(this,v -> {
            int position = (int) v.getTag();
            HealthCard card = otherList.remove(position);
            mList.add(card);
            adapter.refreshData(mList);
            otherAdapter.refreshData(otherList);
            insertDB();
        });
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.addItemDecoration(new HealthCardItemDecoration(this));
        recycler.setHasFixedSize(true);
        recycler.setNestedScrollingEnabled(false);
        recycler.setAdapter(otherAdapter);
        otherAdapter.refreshData(otherList);
    }

    public static void instance(Context context) {
        Intent intent = new Intent(context, HealthCardManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 封装添加的类型  入数据库
     */
    public void insertDB(){
        StringBuffer typeList = new StringBuffer();
        for (int i=0; i < mList.size(); i++){
            typeList.append(mList.get(i).type).append(",");
        }
        SPCache.putString(SPCache.KEY_HEALTH_CARD_LIST,typeList.toString());
    }


    public void getDBList(List<Integer> spList){
        if (spList!=null  && !spList.isEmpty()) {
            for (int index=0; index < spList.size(); index++){
                int type = spList.get(index);
                mList.add(new HealthCard(getTypeName(type),type));
            }

            if (mList.size() >=8)
                return;

            setOtherList(spList,otherList);

        }/*else {

            for (int k=1; k < 9; k++){
                if (k < 5){
                    mList.add(new HealthCard(getTypeName(k),k));
                }else {
                    otherList.add(new HealthCard(getTypeName(k),k));
                }
            }
        }*/
    }


    /**
     * 1-心率  2-血压  3-睡眠  4-心电  5-血糖  6-血氧  7-BMI   8-体温
     * @param type
     * @return
     */
    public String getTypeName(int type){
        String name="";
        switch (type){//1-心率  2-血压  3-睡眠  4-心电  5-血糖  6-血氧  7-BMI   8-体温
            case 1:
                name = "心率";
                break;
            case 2:
                name = "血压";
                break;
            case 3:
                name = "睡眠";
                break;
            case 4:
                name = "心电";
                break;
            case 5:
                name = "血糖";
                break;
            case 6:
                name = "血氧";
                break;
            case 7:
                name = "BMI";
                break;
            default:
                name = "体温";
                break;
        }
        return name;
    }


    public void setOtherList(List<Integer> orgList,List<HealthCard> list){
        for (int i=1; i < 9; i++){
            if (orgList.contains(i)){
                continue;
            }

            list.add(new HealthCard(getTypeName(i),i));
        }
    }


    /**
     * 获取Sharedperence数据并转换List
     * @return
     */
    public static List<Integer> getSPList(){
        String sp = SPCache.getString(SPCache.KEY_HEALTH_CARD_LIST,"");
        if (TextUtils.isEmpty(sp))
            return null;

        List<Integer> list = new ArrayList<>();
        String[] arry = sp.split(",");
        for (int i=0; i < arry.length; i++){
            list.add(Integer.parseInt(arry[i]));
        }

        return list;
    }
}
