package com.cloud.tao.framwork.base;

import android.os.Message;

/**
 * author:janecer on 2016/2/10 12:08
 */

public interface IWorkActivity {

    void initBackgroundHandler() ;

    void handleBackgroundMsg(Message msg) ;
}
