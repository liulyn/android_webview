package com.cloud.tao.net.base;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.framwork.Logger;

/**
 * author:janecer on 2016/8/9 20:51
 */


public class BaseReq {

    public String time;
    public String login_time;
    public String login_token;
    public String login_u_client_id;
    public String session_id;

    private final static String TAG = BaseReq.class.getSimpleName() ;

    public final String key = "bc123"  ;

    public HashMap<String,String> toHashMaps(){
        time = AccountInfo.getInstance().getLoginTime();
        session_id = AccountInfo.getInstance().getLastLoginSessionId();
        login_time=AccountInfo.getInstance().getLastLoginTime();
        login_token=AccountInfo.getInstance().getLastLoginToken();
        login_u_client_id=AccountInfo.getInstance().getLastLoginClientId();
        Class<?> clazz = this.getClass() ;
        Field[] fields = clazz.getFields() ;
        if(null != fields) {
            HashMap<String,String> maps = new HashMap<>() ;
            int length = fields.length ;
            Logger.msg(TAG , " length : " + length);
            for(int i = 0 ; i<length ; i++) {
                String key = fields[i].getName() ;
                String val = null ;
                try {
                    val = fields[i].get(this) + "";
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Logger.msg(TAG ,"key : " + key +"   val:" + val );
                if(!"null" .equals(val) ) {
                    maps.put(key , val) ;
                }
            }
            return maps ;
        }
        return null ;
    } ;
}
