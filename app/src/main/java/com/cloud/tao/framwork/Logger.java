package com.cloud.tao.framwork;

import android.util.Log;

import com.cloud.tao.BuildConfig;

public class Logger {

    private static final String SYSTAG = "cloud_tao" ;

    private static  boolean isDebug = BuildConfig.LOG_DEBUG;


    /**
     * 打印系统log
     * @param msg
     */
    public static void msg(String msg){
        msg(SYSTAG,msg);
    }

    /**
     * 打印系统log
     * @param msg
     */
    public static void msg(String Tag ,String msg){
        msg(Tag,Log.INFO ,msg);
    }

    public static void msg(String tag,int level ,String msg){
        if(!isDebug){
            return ;
        }
        switch (level){
            case Log.VERBOSE:
                Log.v(tag, msg);
                break;
            case Log.DEBUG:
                Log.d(tag, msg);
                break;
            case Log.INFO:
                Log.i(tag, msg);
                break;
            case Log.WARN:
                Log.w(tag, msg);
                break;
            case Log.ERROR:
                Log.e(tag, msg);
                break;
            default:
                break;
        }

    }
}
