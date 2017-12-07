package com.cloud.tao.framwork.base;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.cloud.tao.bean.etc.event.BaseFragmentEventBus;
import com.cloud.tao.util.eventbusutil.EventBusUtil;

import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author:janecer on 2016/2/10 16:37
 *
 * 提供刷新Ui的Fragment
 */

public class BaseFragment extends BaseV4Fragment implements IActivity,IWorkActivity{
    protected final static int BASE_CODE = 10 ;
    public UiHandler mUiHandler = new UiHandler(this) ;

    protected IntentFilter mIntentFilter ;

    private BaseBroadCastReceiver mReceiver ;


    protected BackgroundHandle mBackgroundHandler = null ;

    private HandlerThread mHandlerThread ;

    public static class BackgroundHandle extends Handler {

        private final WeakReference<IWorkActivity> mWeakBackgroundFragmentReference  ;

        public BackgroundHandle(IWorkActivity baseWorkFragmentActivity , Looper looper){
            super(looper) ;
            mWeakBackgroundFragmentReference = new WeakReference<IWorkActivity>(baseWorkFragmentActivity) ;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mWeakBackgroundFragmentReference.get() != null) {
                mWeakBackgroundFragmentReference.get().handleBackgroundMsg(msg);
            }
        }
    }

    public static class UiHandler extends Handler {

        private final WeakReference<IActivity> mWeakFragmentReference  ;

        public UiHandler(IActivity baseFragmentActivity) {
            mWeakFragmentReference  = new WeakReference<IActivity>(baseFragmentActivity) ;
        }
        public void handleMessage(Message msg) {
            super.handleMessage(msg) ;
            if(mWeakFragmentReference.get() != null) {
                mWeakFragmentReference.get().handleUiMessage(msg) ;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        EventBusUtil.register(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusUtil.unRegister(this);
        mIntentFilter = null ;
        if(mBackgroundHandler != null && mBackgroundHandler.getLooper() != null) {
            mBackgroundHandler.getLooper().quit() ;
        }
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
                getActivity().registerReceiver(mReceiver , mIntentFilter) ;
            }
        }
    } ;

    @Override
    public void onStart() {
        super.onStart();
        if(mReceiver != null) {
            getActivity().registerReceiver(mReceiver , mIntentFilter) ;
        }
    }

    @Override
    public void onStop() {
        EventBusUtil.unRegister(this);
        super.onStop();
        if(mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }


    @Override
    public void initBackgroundHandler() {
        if(mHandlerThread != null && mHandlerThread.getLooper() != null) {
            return ;
        }
        mHandlerThread = new HandlerThread("Fragment..." + BaseWorkService.class.getSimpleName()) ;
        mHandlerThread.start() ;
        mBackgroundHandler = new BackgroundHandle(this,mHandlerThread.getLooper()) ;
    }
    /**
     * 刷新界面
     *
     * @param msg
     */
    @Override
    public void handleUiMessage(Message msg) {

    }
    @Override
    public void handleBackgroundMsg(Message msg) {

    }
    /**
     * 处理广播
     *
     * @param context
     * @param intent
     */
    @Override
    public void handleBroadCast(Context context, Intent intent) {

    }

    @Subscribe
    public void onEventMainThread(BaseFragmentEventBus eventBus) {
    }

}
