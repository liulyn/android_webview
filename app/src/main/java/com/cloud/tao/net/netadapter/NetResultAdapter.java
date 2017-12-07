package com.cloud.tao.net.netadapter;

import android.text.TextUtils;
import com.cloud.tao.MallApplication;
import com.cloud.tao.net.callBack.INetResultAdapter;
import com.cloud.tao.util.SharePrefUtil;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.lang.reflect.Type;
import com.cloud.tao.net.NetCodeList;

/**
 * sunny created at 2016/9/10/06
 */

public class NetResultAdapter<T>  implements INetResultAdapter<T> {

    private Gson mGson = new Gson() ;
    private Type mType ;
    //private BaseStatus status ;
    private int errorCode ; //<0:退出重新登录、=0：成功、>0:失败
    private String errorMessage ;
    private String successValues ;
    private String time;
    //private String myStoreId;

    public NetResultAdapter() {
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public boolean isSuccess() {
        return errorCode==NetCodeList.BASE_RESPONSE_CODE;
    }

    @Override
    public boolean isNeedResetLogin() {
        return errorCode<NetCodeList.BASE_RESPONSE_CODE;
    }

    @Override
    public int getStatusCode() {
        return errorCode;
    }

    @Override
    public T getResult() {
        if(TextUtils.isEmpty(successValues)) {
            return null ;
        }
        if(mType == String.class) {
            return (T)successValues ;
        }
        try {
            T resp = mGson.fromJson(successValues, mType) ;
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
            return null ;
        }
    }

    @Override
    public String getSuccessValue() {
        return successValues;
    }

    @Override
    public void parseResult(String str,Type type) throws Exception{
        this.mType = type ;
        JSONObject json = new JSONObject(str);
        errorCode=json.optInt("errorCode");
        errorMessage=json.optString("errorMessage");
        JSONObject data=json.optJSONObject("data");
        successValues=data.toString();

        time=data.optString("time");
        //myStoreId=data.optString("my_store_id");
        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_time,time);
        //SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_myStoreId,myStoreId);
    }
}
