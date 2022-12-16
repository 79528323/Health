package net.fkm.updateapp.listener;

import net.fkm.updateapp.UpdateAppBean1;

public interface IUpdateDialogFragmentListener {
    /**
     * 当默认的更新提示框被用户点击取消的时候调用
     *
     * @param updateApp updateApp
     */
    void onUpdateNotifyDialogCancel(UpdateAppBean1 updateApp);
}
