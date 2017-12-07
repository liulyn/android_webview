package com.cloud.tao.net.callBack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.framwork.vl.VLApplication;
import com.cloud.tao.net.netadapter.NetResultAdapter;
import com.cloud.tao.ui.activity.LoginAccountActivity;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.net.NetCodeList;
import com.zhy.http.okhttp.callback.StringCallback;
import java.lang.reflect.Type;
import com.cloud.tao.framwork.Logger;
import okhttp3.Call;

/**
 * sunny created at 2016/9/10/06
 */

public abstract class EntityCallBack<T> extends StringCallback {

    private INetResultAdapter<T> resultAdapter ;
    private Type mToken ;

    public EntityCallBack(TypeToken<T> token) {
        this.mToken = token.getType() ;
        resultAdapter = new NetResultAdapter<T>() ;
    }

    public EntityCallBack(TypeToken<T> token,INetResultAdapter<T> adapter){
        this.mToken = token.getType() ;
        resultAdapter = adapter ;
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            resultAdapter.parseResult(response,mToken);
            if(resultAdapter.isSuccess()) {
                onSuccess(resultAdapter.getStatusCode(),resultAdapter.getMessage(),resultAdapter.getResult());
                return ;
            }else if(resultAdapter.isNeedResetLogin()){
                VLApplication app = VLApplication.instance();
                Activity mActivity=app.getTopActivity();
                app.finishAllActivities(mActivity);
                Context mContext=app.getApplicationContext();
                ToastUtils.showToastShort(mContext, "账户登录状态已过期，请重新登录");
                mActivity.startActivity(new Intent(mContext, LoginAccountActivity.class));
                AccountInfo.getInstance().realseLoginInfo();
                mActivity.finish();
                return;
            }else{
                onFail(resultAdapter.getStatusCode(),null,resultAdapter.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            onFail(-1,e,"未知错误");
        }
    }


    @Override
    public void onError(Call call, Exception e, int id) {
        if(id==-1){
            onFail(NetCodeList.BASE_NET_FAIL_CODE,e,"请检查当前网络连接状态");
            return;
        }
        onFail(NetCodeList.BASE_FAIL_CODE,e,"服务器异常，请稍后重试");
    }

    public abstract void onSuccess(int code,String msg,T resp) ;

    public abstract void onFail(int code ,Exception e,String msg) ;

}
