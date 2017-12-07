package com.cloud.tao.bean.etc.event;

/**
 * Created by wyk on 2016/6/23.
 * 描述:关于网络状态改变的监听的
 */
public class NetWorkActionState {

    public NetWorkActionState(boolean isNetWorkError){

        this.isNetWorkError = isNetWorkError;
    }

    public boolean isNetWorkError;          //true表示Error
}
