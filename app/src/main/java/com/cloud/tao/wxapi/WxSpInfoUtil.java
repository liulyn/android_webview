package com.cloud.tao.wxapi;

import android.content.Context;

import com.cloud.tao.util.SharePrefUtil;
import com.google.gson.Gson;

import com.cloud.tao.net.model.wxmodel.reqresp.GetAccessTokenResp;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;

/**
 * author:janecer on 2016/6/12 16:56
 */

public class WxSpInfoUtil {

    private Gson mGson ;
    private static WxSpInfoUtil infoUtil ;
    private Context context ;


    private static String KEY_TOKEN_INFO = "key_token_info" ;
    private static String KEY_USER_INFO = "key_user_info" ;

    private WxSpInfoUtil(Context ctx) {
        context = ctx ;
        mGson = new Gson() ;
    }

    public static WxSpInfoUtil getInstance(Context ctx) {
        if(null  == infoUtil) {
            infoUtil = new WxSpInfoUtil(ctx) ;
        }
        return infoUtil ;
    }

    public void saveTokenInfo(GetAccessTokenResp accessTokenResp) {
        if(null != accessTokenResp) {
            SharePrefUtil.saveString(context,KEY_TOKEN_INFO,mGson.toJson(accessTokenResp));
        }
    }

    public GetAccessTokenResp getTokenInfo() {
        String str = SharePrefUtil.getString(context,KEY_TOKEN_INFO,"") ;
        return mGson.fromJson(str,GetAccessTokenResp.class) ;
    }

    public void saveUserInfo(UserInfoResp resp) {
        if(null != resp) {
            SharePrefUtil.saveString(context,KEY_USER_INFO,mGson.toJson(resp));
        }
    }

    public UserInfoResp getUserInfo() {
        String str = SharePrefUtil.getString(context,KEY_USER_INFO,"") ;
        return mGson.fromJson(str,UserInfoResp.class) ;
    }

    public String getUserInfoStr() {
        return  SharePrefUtil.getString(context,KEY_USER_INFO,"") ;
    }
}
