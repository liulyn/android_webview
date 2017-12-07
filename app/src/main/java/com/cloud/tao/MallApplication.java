package com.cloud.tao;

import com.cloud.tao.framwork.vl.VLApplication;
import com.cloud.tao.net.model.cashier.CashierModel;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.google.gson.Gson;

import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.net.model.loginmodel.LoginModel;
import com.cloud.tao.net.model.wxmodel.WXmodel;

/**
 * 程序自定义Application
 */


public class MallApplication extends VLApplication {

    private static MallApplication instance;
    private Gson netGson ;
    private boolean isDownload; //标识是否下载新版本

    public static MallApplication getInstance() {
        if(null == instance) {
            throw new NullPointerException("MallApplication 尚未初始化");
        }
        return instance ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isDownload = false;
        netGson = new Gson() ;
        this.instance = this ;
    }

    @Override
    protected void onConfigModels() {
        super.onConfigModels();
        mModelManager.registerModel(AppModel.class);
        mModelManager.registerModel(LoginModel.class);
        mModelManager.registerModel(WXmodel.class);
        mModelManager.registerModel(MemberModel.class);
        mModelManager.registerModel(OrderModel.class);
        mModelManager.registerModel(CashierModel.class);
    }

    public Gson getNetGson() {
        return netGson ;
    }

    /**
     * 标识 新版本是否下载中
     * @return
     */
    public boolean isDownload() {
        return isDownload;
    }
    public void setDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

}
