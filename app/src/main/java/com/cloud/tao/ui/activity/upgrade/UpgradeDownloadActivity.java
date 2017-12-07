package com.cloud.tao.ui.activity.upgrade;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.upgrade.UpgradeBackResultBean;
import com.cloud.tao.server.UpgradeService;
import com.cloud.tao.util.ToastUtils;

/**
 * Created by created
 * des: 版本更新下载进度提示
 */
public class UpgradeDownloadActivity extends Activity {

    private ProgressBar pb_upgrade_download;
    private TextView tv_dlownload_size;
    private TextView tv_upgrade_download_progress;
    private MallApplication application;
    private UpgradeService.DownloadBinder binder;
    private boolean isBinded;
    private boolean isDestroy = true;
    String downloadUrl;
    Intent downloadIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upgrade_download);
        pb_upgrade_download=(ProgressBar) findViewById(R.id.pb_upgrade_download);
        tv_dlownload_size= (TextView) findViewById(R.id.tv_upgrade_dlownload_size);
        tv_upgrade_download_progress= (TextView) findViewById(R.id.tv_upgrade_download_progress);
        application= (MallApplication) getApplication();
        initData();
        initListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }


    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if(bundle==null){
            this.finish();
        }else{
            downloadUrl=bundle.getString("downloadUrl");
        }
    }

    /**
     * 接收service下载回调
     */
    private ICallbackResult callback = new ICallbackResult() {
        @Override
        public void OnBackResult(Object result) {
            UpgradeBackResultBean backResultBean= (UpgradeBackResultBean) result;
            if (backResultBean.isFinish()) {
                if(backResultBean.isError()){
                    ToastUtils.showToastShort(UpgradeDownloadActivity.this,"新版本下载失败");
                }else{
                    application.finishAllActivities(UpgradeDownloadActivity.this);
                    UpgradeDownloadActivity.this.finish();
                }
                return;
            }
            // tv_progress.postInvalidate();
            Message msg=new Message();
            msg.what=0;
            msg.obj=backResultBean;
            mHandler.sendMessage(msg);
        }
    };

    /**
     * 更新页面下载进度
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==0){
                UpgradeBackResultBean backResultBean= (UpgradeBackResultBean) msg.obj;
                int downloadProgress=backResultBean.getDownloadProgress();
                pb_upgrade_download.setProgress(downloadProgress);
                tv_dlownload_size.setText(backResultBean.getDownloadSize()+"/"+backResultBean.getDownloadTotleSize()+" M");
                tv_upgrade_download_progress.setText("当前进度: " +downloadProgress+ "%");
            }
        }
    };

    private void initListener(){
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (UpgradeService.DownloadBinder) service;
            isBinded = true;
            binder.addCallback(callback);
            binder.start();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        application.setDownload(true);
        if (isDestroy && application.isDownload()) {
            downloadIntent = new Intent(UpgradeDownloadActivity.this, UpgradeService.class);
            Bundle downloadBundle=new Bundle();
            downloadBundle.putString("downloadUrl",downloadUrl);
            downloadIntent.putExtras(downloadBundle);
            startService(downloadIntent);
            bindService(downloadIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isDestroy && application.isDownload()) {
            Intent downloadIntent = new Intent(UpgradeDownloadActivity.this, UpgradeService.class);
            startService(downloadIntent);
            bindService(downloadIntent, conn, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isDestroy = false;
    }

    @Override
    protected void onDestroy() {
        if (isBinded) {
            unbindService(conn);
        }
        if (binder!=null&&!binder.isCanceled()) {
            binder.cancelNotification();
            stopService(downloadIntent);
        }
        super.onDestroy();
    }

    public interface ICallbackResult {
        void OnBackResult(Object result);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }

}
