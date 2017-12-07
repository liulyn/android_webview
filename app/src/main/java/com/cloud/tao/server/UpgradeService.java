package com.cloud.tao.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.cloud.tao.R;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.upgrade.UpgradeBackResultBean;
import com.cloud.tao.ui.activity.upgrade.UpgradeDownloadActivity;
import com.cloud.tao.bean.etc.event.NetWorkActionState;
import com.cloud.tao.util.eventbusutil.EventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * sunny created
 * des: 版本更新后台下载服务
 */
public class UpgradeService extends Service {

    private static final int NOTIFY_ID = 0;
    private int progress;
    private boolean canceled;
    private String downloadUrl;
    private String apkSaveDir; //存放存储设备方式
    private static final String tongduiSoftPath = "/tongduiSoft/tongduiCity/";
    private static String apkSavePath; //apk下载父文件
    private static String saveFileName; //apk完整路径
    //通知
    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;

    private UpgradeDownloadActivity.ICallbackResult callback;
    private DownloadBinder binder;
    private MallApplication application;
    UpgradeBackResultBean mBackResultBean;
    private Context mContext = this;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:  // 下载完成通知安装
                    installApk();
                    closeService();
                    break;
                case 2: //手动取消
                    closeService();
                    break;
                case 1:
                    int rate = msg.arg1;
                    application.setDownload(true);
                    if (rate < 100) {
                        setUpNotification(rate);
                    } else {
                        closeService();
                    }
                    break;
            }
        }
    };

    private void closeService() {
        if (mBackResultBean != null) {
            mBackResultBean.setFinish(true);
            callback.OnBackResult(mBackResultBean);
        }
        mNotificationManager.cancel(NOTIFY_ID); //关闭通知
        application.setDownload(false);
        canceled = true;
        stopSelf();// 停掉服务
    }

    @Override
    public IBinder onBind(Intent intent) {
        downloadUrl = intent.getStringExtra("downloadUrl");
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getStringExtra("downloadUrl")!=null){
            downloadUrl = intent.getStringExtra("downloadUrl");
        }
        return super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            apkSaveDir=Environment.getExternalStorageDirectory().getPath();
        }else{
            apkSaveDir=Environment.getDataDirectory().getPath();
        }
        apkSavePath=apkSaveDir+tongduiSoftPath;
        EventBusUtil.register(this);
        application = (MallApplication) getApplication();
        binder = new DownloadBinder();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        initNotify();

    }

    public class DownloadBinder extends Binder {
        public void start() {
            if (downLoadThread == null || !downLoadThread.isAlive()) {
                progress = 0;
                setUpNotification(0);
                new Thread() {
                    public void run() {
                        // 下载
                        startDownload();
                    }
                }.start();
            }
        }

        public void cancel() {
            canceled = true;
        }
        public boolean isCanceled() {
            return canceled;
        }
        public void cancelNotification() {
            mHandler.sendEmptyMessage(2);
        }
        public void addCallback(UpgradeDownloadActivity.ICallbackResult callback) {
            UpgradeService.this.callback = callback;
        }
    }

    private void startDownload() {
        canceled = false;
        downloadApk();
    }

    public PendingIntent getDefalutIntent() {
        Intent intent = new Intent(this, UpgradeDownloadActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return contentIntent;
    }

    /**
     * 创建通知
     */
    private void setUpNotification(int rate) {
        RemoteViews mRemoteViews = new RemoteViews(getPackageName(), R.layout.layout_upgrade_notification);
        mRemoteViews.setImageViewResource(R.id.custom_progress_icon, R.mipmap.ic_cloud_notification);
        mRemoteViews.setTextViewText(R.id.tv_custom_progress_title, "新版本,下载进度");
        if (rate > 0) {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, rate, false);
            mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, rate + "%");
        } else {
            mRemoteViews.setProgressBar(R.id.custom_progressbar, 100, 0, false);
            mRemoteViews.setTextViewText(R.id.tv_custom_progress_status, "0%");
        }
        mBuilder.setContent(mRemoteViews)
                .setContentIntent(getDefalutIntent())
                .setTicker("新版本 正在下载...");
        Notification nitify = mBuilder.build();
        nitify.contentView = mRemoteViews;
        mNotificationManager.notify(NOTIFY_ID, nitify);
    }

    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setWhen(System.currentTimeMillis())
                .setContentIntent(getDefalutIntent())
                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                .setOngoing(true)
                .setSmallIcon(R.mipmap.ic_cloud_notification);
        // .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
    }

    private Thread downLoadThread;

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        mBackResultBean.setFinish(true);
        callback.OnBackResult(mBackResultBean);
    }

    private int lastRate = 0;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                mBackResultBean = new UpgradeBackResultBean();
                mBackResultBean.setFinish(false);
                mBackResultBean.setError(false);
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20000);
                conn.setRequestProperty("Accept-Encoding", "identity");
                /*conn.connect();*/
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    int length = conn.getContentLength();
                    if (length > 0) {
                        mBackResultBean.setDownloadTotleSize(decimalFormat.format(length / 1024.0 / 1024.0));
                        InputStream is = conn.getInputStream();
                        saveFileName=apkSavePath+"tongdui_city.apk";//+(downloadUrl.substring((downloadUrl.lastIndexOf("/")+1),downloadUrl.length()));
                        File file = new File(apkSavePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File ApkFile = new File(saveFileName);
                        FileOutputStream fos = new FileOutputStream(ApkFile);
                        int count = 0;
                        byte buf[] = new byte[1024];
                        do {
                            int numread = is.read(buf);
                            count += numread;
                            mBackResultBean.setDownloadSize(decimalFormat.format(count / 1024.0 / 1024.0));
                            progress = (int) (((float) count / length) * 100);
                            mBackResultBean.setDownloadProgress(progress);
                            // 更新进度
                            Message msg = mHandler.obtainMessage();
                            msg.what = 1;
                            msg.arg1 = progress;
                            if (progress >= lastRate + 1) {
                                mHandler.sendMessage(msg);
                                lastRate = progress;
                                if (callback != null)
                                    callback.OnBackResult(mBackResultBean);
                            }
                            fos.write(buf, 0, numread);
                            if (progress ==100) {
                                mHandler.sendEmptyMessage(0);
                                canceled = true;
                                break;
                            }
                        } while (!canceled);// 点击取消停止下载
                        fos.close();
                        is.close();
                    } else {
                        setCloseByError();
                    }
                } else {
                    setCloseByError();
                }
            } catch (MalformedURLException e) {
                setCloseByError();
            }catch (IOException e) {
                setCloseByError();
            }
        }
    };

    private void setCloseByError(){
        if (null != mBackResultBean) {
            mBackResultBean.setError(true);
        }
        setBackClose();
    }

    private void setCloseBySuccess(){
        if (null != mBackResultBean) {
            mBackResultBean.setError(false);
        }
        setBackClose();
    }

    @Subscribe
    public void onEventMainThread(NetWorkActionState stateChangeEven) {
        if (stateChangeEven.isNetWorkError) {
            setCloseByError();
        }
    }

    private void setBackClose() {
        mHandler.sendEmptyMessage(2);
    }

}
