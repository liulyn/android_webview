package com.cloud.tao.net.netadapter;

import android.text.TextUtils;

import com.cloud.tao.net.callBack.INetResultAdapter;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * author:janecer on 2016/8/31 21:58
 */


public class WxResultAdapter<T> implements INetResultAdapter<T> {

    private Gson mGson = new Gson() ;
    private Type mType ;
    private String result ;


    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return !TextUtils.isEmpty(result);
    }

    @Override
    public boolean isNeedResetLogin() {
        return false;
    }

    @Override
    public int getStatusCode() {
        return 0;
    }

    @Override
    public T getResult() {
        if(TextUtils.isEmpty(result)) {
            return null ;
        }
        if(mType == String.class) {
            return (T)result ;
        }
        try {
            T resp = mGson.fromJson(result, mType) ;
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    @Override
    public String getSuccessValue() {
        return result;
    }

    @Override
    public void parseResult(String str, Type type) throws Exception {
        this.result = str ;
        this.mType = type ;
    }
}
