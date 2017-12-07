package com.cloud.tao.net.callBack;

import java.lang.reflect.Type;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public interface INetResultAdapter<T> {

    String getMessage() ;

    boolean isSuccess() ;

    boolean isNeedResetLogin();

    int getStatusCode() ;

    T getResult() ;

    String getSuccessValue() ;

    void parseResult(String str,Type type) throws Exception;
}
