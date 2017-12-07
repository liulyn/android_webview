package com.cloud.tao.net;

import com.cloud.tao.net.base.BaseReq;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.request.RequestCall;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cloud.tao.MallApplication;
import com.cloud.tao.framwork.Logger;
import com.cloud.tao.util.NetworkStatUtils;

/**
 * author:janecer on 2016/8/9 20:48
 */


public class MallOkHttpUtils {

    public static <Req extends BaseReq,T> void post(String requestTag, String url , Req req , final Callback<T> resultListener) {
        if (checkNetWork(resultListener)) return;
        HashMap params = req.toHashMaps();
        logger(url, params);
        RequestCall call = OkHttpUtils
                .post()
                .url(url).params(params)
                .tag(requestTag) //用来取消指定的网络请求
                .build() ;
        call.execute(resultListener);
    }


    public static <Req extends BaseReq,T> void get(String requestTag, String url , Req req , final Callback<T> resultListener) {
        Logger.msg("request: url :"+url);
        RequestCall call = OkHttpUtils.get().url(url).tag(requestTag).build();
        call.execute(resultListener) ;
    }

    public static <Req extends BaseReq,T> void postFile(String requestTag, String url, Map<String ,List<String>> pics , Req req, final Callback resultListener) {
        // RequestCall call = OkHttpUtils.postFile().url(url).tag(requestTag).
        if (checkNetWork(resultListener)) return;

        HashMap params = req.toHashMaps();
        PostFormBuilder builder =  OkHttpUtils.post();
        builder.params(params) ;
        Set<String> keys =pics.keySet();

        List<String> list = null;
        int length  = 0 ;
        for(String key : keys) {
            list = pics.get(key) ;
            length = list.size() ;
            for(int j = 0 ; j < length; j++){
                File files = new File(list.get(j)) ;
                if(files.exists()) {
                    builder.addFile(key, list.get(j), files);
                }
            }
        }
        RequestCall call = builder .url(url)
                .tag(requestTag) //用来取消指定的网络请求
                .build();
        call.execute(resultListener) ;
    }


    private static void logger(String url, HashMap params) {
        Logger.msg("request: url :"+url);
        Logger.msg("request data : " + params.toString());
    }

    private static <T> boolean checkNetWork(Callback<T> resultListener) {
        if(!NetworkStatUtils.isNetworkAvailable(MallApplication.getInstance().getApplicationContext())){
            resultListener.onError(null ,new Exception("请检查当前网络连接状态！"),-1);
            return true;
        }
        return false;
    }

}
