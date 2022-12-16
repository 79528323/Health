package net.fkm.updateapp;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 新版本版本检测回调
 */
public class UpdateCallback {

    /**
     * 解析json,自定义协议
     *
     * @param json 服务器返回的json
     * @return UpdateAppBean
     */
    protected UpdateAppBean1 parseJson(String json) {
        UpdateAppBean1 updateAppBean = new UpdateAppBean1();
        try {

            UpdateAppBean1 updateAppBean1 = new Gson().fromJson(json, UpdateAppBean1.class);
            UpdateAppBean1.DataBean data1 = updateAppBean1.getData();
            String newVersion = data1.getVersionName();
            String apkFileUrl = data1.getUrl();
            int isUpdate = data1.getIsUpdate();
            String updateLog = "";
            List<UpdateAppBean1.DataBean.ExplainBean> explainBeanList = data1.getExplain();
            for (UpdateAppBean1.DataBean.ExplainBean explainBean : explainBeanList) {
                updateLog = String.format("%s\n%s\n", updateLog, explainBean.getContent());
            }

            UpdateAppBean1.DataBean data = new UpdateAppBean1.DataBean();
            data.setVersionName(newVersion);
            data.setUrl(apkFileUrl);
            data.setIsUpdate(isUpdate);
            data.setUpdateLog(updateLog);
            updateAppBean.setData(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return updateAppBean;
    }

    /**
     * 有新版本
     *
     * @param updateApp        新版本信息
     * @param updateAppManager app更新管理器
     */
    protected void hasNewApp(UpdateAppBean1 updateApp, UpdateAppManager updateAppManager) {
        updateAppManager.showDialogFragment();
    }

    /**
     * 网路请求之后
     */
    protected void onAfter() {
    }


    /**
     * 没有新版本
     *
     * @param error HttpManager实现类请求出错返回的错误消息，交给使用者自己返回，有可能不同的应用错误内容需要提示给客户
     */
    protected void noNewApp(String error) {
    }

    /**
     * 网络请求之前
     */
    protected void onBefore() {
    }

}
