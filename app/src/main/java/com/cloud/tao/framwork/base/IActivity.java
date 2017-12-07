package com.cloud.tao.framwork.base;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import java.util.List;

/**
 * author:janecer on 2016/2/10 11:14
 *
 * 在提供Ui处理动作的接口
 * 设置广播动作的监听与广播的动作处理的接口
 */

public interface IActivity {

    /**
     * 提供刷新Uihandle
     * @param msg
     */
    void handleUiMessage(Message msg) ;

    /**
     * 让子类设置广播的监听动作,手动调用（支持多次调用）
     * @return
     */
    void setUpActions(List<String> actions) ;

    /**
     * 处理监听到的广播通知
     * @param ctx
     * @param intent
     */
    void handleBroadCast(Context ctx, Intent intent) ;
}
