package com.cloud.tao.framwork.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author:janecer on 2016/2/10 10:43
 *
 * 提供 刷新Ui
 * 在子类Activity销毁时，注意mUiHandle是否有，没有处理完的消息，避免内存泄露 需要及时移除
 */

public abstract  class BaseFragmentActivity extends BaseActivity implements IActivity,IWorkActivity {

    protected final static int BASE_CODE = 10 ;

    protected UiHandler mUiHandler = new UiHandler(this) ;

    private BaseBroadCastReceiver mReceiver ;
    private IntentFilter mIntentFilter ;


    protected BackgroundHandle mBackgroundHandler = null ;
    private HandlerThread mHandlerThread;
    public static class BackgroundHandle extends Handler {

        private final WeakReference<IWorkActivity> mWeakBackgroundActivityReference  ;

        public BackgroundHandle(IWorkActivity baseWorkFragmentActivity , Looper looper){
            super(looper) ;
            mWeakBackgroundActivityReference = new WeakReference<IWorkActivity>(baseWorkFragmentActivity) ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mWeakBackgroundActivityReference.get() != null) {
                mWeakBackgroundActivityReference.get().handleBackgroundMsg(msg);
            }
        }
    }

    public static class UiHandler extends Handler {

        private final WeakReference<IActivity> mWeakActivityReference  ;

        public UiHandler(IActivity baseFragmentActivity) {
            mWeakActivityReference  = new WeakReference<IActivity>(baseFragmentActivity) ;
        }
        public void handleMessage(Message msg) {
            super.handleMessage(msg) ;
            if(mWeakActivityReference.get() != null) {
                mWeakActivityReference.get().handleUiMessage(msg) ;
            }
        }
    }

    @Override
    public void initBackgroundHandler() {
        if(mHandlerThread != null && mHandlerThread.getLooper() != null) {
            return ;
        }
        mHandlerThread = new HandlerThread("BackgroudnThread " + this.getClass().getSimpleName()) ;
        mHandlerThread.start();
        mBackgroundHandler = new BackgroundHandle(this, mHandlerThread.getLooper()) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mReceiver != null) {
            this.registerReceiver(mReceiver , mIntentFilter) ;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mReceiver != null) {
            this.unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIntentFilter = null ;

        if(mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit();
        }
    }

    @Override
    public void handleUiMessage(Message msg) {

    }

    @Override
    public void handleBackgroundMsg(Message msg) {

    }
    @Override
    public void setUpActions(List<String> mActions) {
        if(null != mActions && mActions.size() > 0) {
            if( null  == mIntentFilter){
                mIntentFilter = new IntentFilter() ;
                int size = mActions.size() ;
                for(int i = 0 ; i < size ; i ++) {
                    mIntentFilter.addAction(mActions.get(i));
                }
                mReceiver = new BaseBroadCastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        handleBroadCast(context, intent);
                    }
                } ;
            } else {
                int size = mActions.size() ;
                for(int i = 0 ; i < size ; i ++) {
                    mIntentFilter.addAction(mActions.get(i));
                }
                this.registerReceiver(mReceiver , mIntentFilter) ;
            }
        }
    } ;

    @Override
    public void handleBroadCast(Context ctx, Intent intent) {

    }


}
