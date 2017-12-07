package com.cloud.tao.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ExternalStorageUtil {

	private static BroadcastReceiver mReceiver;

	private static final List<WeakReference<ExternalStorageStateListener>> mListeners = new ArrayList<WeakReference<ExternalStorageStateListener>>();

	private static Handler mUIHandler;

	public static void addExternalStorageStateListener(ExternalStorageStateListener l) {
		synchronized (mListeners) {
			if (l != null && mListeners != null) {
				for (WeakReference<ExternalStorageStateListener> ref : mListeners) {
					if (l.equals(ref.get())) {
						return;
					}
				}
				WeakReference<ExternalStorageStateListener> ref = new WeakReference<ExternalStorageStateListener>(l);
				mListeners.add(ref);
			}
		}
	}

	/* 监听主存储状态 */

	public static boolean isSDCardExist() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/* 统一下发外部存储状态改变 */

	/**
	 * 监听外部存储挂载情况
	 */
	public static void registerRecevier(Context context) {
		if (mReceiver != null) {
			return;
		}

		mReceiver = new ExternalStorageStateReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(Integer.MAX_VALUE);
		intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
		intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		intentFilter.addDataScheme("file");
		context.registerReceiver(mReceiver, intentFilter);

//		mListeners = new ArrayList<WeakReference<ExternalStorageStateListener>>();
		mUIHandler = new Handler();
	}

	public static void removeExternalStorageStateListener(ExternalStorageStateListener l) {
		synchronized (mListeners) {
			if (l != null && mListeners != null) {
				Iterator<WeakReference<ExternalStorageStateListener>> iterator = mListeners.iterator();
				while (iterator.hasNext()) {
					WeakReference<ExternalStorageStateListener> ref = iterator.next();
					if (l.equals(ref.get())) {
						ref.clear();
						iterator.remove();
						return;
					}
				}
			}
		}
	}

	/**
	 * 取消监听外部存储挂载情况
	 */
	public static void unRegisterReceiver(Context context) {
		if (mReceiver == null) {
			return;
		}

		try {
			context.unregisterReceiver(mReceiver);
		} catch (Exception e) {
		}
		mReceiver = null;

		for (WeakReference<ExternalStorageStateListener> ref : mListeners) {
			ref.clear();
		}
		mListeners.clear();
//		mListeners = null;
	}

	private static void notifyChanged(final boolean mounted) {
		synchronized (mListeners) {
			for (WeakReference<ExternalStorageStateListener> ref : mListeners) {
				final ExternalStorageStateListener listener = ref.get();
				if (listener != null) {
					mUIHandler.post(new Runnable() {
						@Override
						public void run() {
							listener.onExternalStorageStateChanged(mounted);
						}
					});
				}
			}
		}
	}

	public static interface ExternalStorageStateListener {

		void onExternalStorageStateChanged(boolean mounted);

	}

	public static final class ExternalStorageStateReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String state = Environment.getExternalStorageState();
			String action = intent.getAction();
			Log.d("", "Media state changed: " + state);
			if ((Intent.ACTION_MEDIA_MOUNTED.equals(action) || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)
					|| Intent.ACTION_MEDIA_REMOVED.equals(action) || Intent.ACTION_MEDIA_BAD_REMOVAL.equals(action))
					&& (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_UNMOUNTED.equals(state))) {
				boolean mounted = isExternalStorageExists();
				notifyChanged(mounted);
			}
		}
	}
	/**
     * 检查外部存储是否挂载
     */
    public static boolean isExternalStorageExists() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
    
    /**
     * @return 多少个 字节
     * @Description:
     */
    @SuppressWarnings("deprecation")
	public static long getExternalStorageTotalSize(){
    	if(!isExternalStorageExists()){
    		return 0;
    	}
    	
        //取得SD卡文件路径 
        File path = Environment.getExternalStorageDirectory();  
        StatFs sf = new StatFs(path.getPath());  
        //获取单个数据块的大小(Byte) 
        long blockSize = sf.getBlockSize();  
        //获取所有数据块数 
        long allBlocks = sf.getBlockCount(); 
        
        //返回SD卡大小 
        return allBlocks * blockSize;
      }     
}
