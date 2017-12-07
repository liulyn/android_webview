package com.cloud.tao.framwork.base;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * author:janecer on 2016/2/10 16:10
 * 提供BackgroundHandler
 */

public class BaseWorkService extends BaseService implements IWorkActivity {

    protected BackgroundHandle mBackgroundHandler = null ;

    private HandlerThread mHandlerThread ;


    public static class BackgroundHandle extends Handler {

        private final WeakReference<IWorkActivity> mWeakBackgroundServiceReference  ;

        public BackgroundHandle(IWorkActivity baseWorkService , Looper looper){
            super(looper) ;
            mWeakBackgroundServiceReference = new WeakReference(baseWorkService) ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mWeakBackgroundServiceReference.get() != null) {
                mWeakBackgroundServiceReference.get().handleBackgroundMsg(msg);
            }
        }
    }

    @Override
    public void initBackgroundHandler() {
        if(mHandlerThread != null && mHandlerThread.getLooper() != null) {
            return ;
        }
        mHandlerThread = new HandlerThread("Service..." + BaseWorkService.class.getSimpleName()) ;
        mHandlerThread.start() ;
        mBackgroundHandler = new BackgroundHandle(this,mHandlerThread.getLooper()) ;
    }

    @Override
    public void handleBackgroundMsg(Message msg) {

    }

    @Override
    public void onCreate() {

    }


    @Override
    public void onDestroy(){
        if(mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit() ;
        }
    }

}
