package com.cloud.tao;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.tao.bean.etc.event.NetWorkActionState;
import com.cloud.tao.bean.etc.event.UserCenterEven;
import com.cloud.tao.bean.etc.upgrade.UpgradeBean;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.ui.activity.upgrade.UpgradeActivity;
import com.cloud.tao.ui.widget.CustomRadioButton;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.NetworkStatUtils;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.eventbusutil.EventBusUtil;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.BuildConfig;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Liu on 2017/12/2.
 */

public class MainWebActivity extends AppCompatActivity {
    private static final String TAG = "MainWebActivity";
    private WebView webview;
    MallApplication application;

    private BroadcastReceiver networkBroadcast= new BroadcastReceiver() { //注册网络监听
        @Override
        public void onReceive(Context context, Intent intent) {
            if(NetworkStatUtils.isNetworkAvailable(context)){
                checkUpgrade();
            }else{
                EventBus.getDefault().post(new NetWorkActionState(true));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        application=(MallApplication)getApplication();

        //实例化WebView对象
        webview = new WebView(this);
        //设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAppCacheEnabled(true);
        // 开启 DOM storage API 功能
        webview.getSettings().setDomStorageEnabled(true);
        // 支持图片上传
        webview.setWebChromeClient( new MyChromeClient (this));
        webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if( url.startsWith("http://wpa.qq.com") || url.startsWith("http://wpa.qq.com") ) {
                    try{
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity( intent );
                    }catch(Exception e){}
                    return false;
                }
                view.loadUrl(url);
                return true;
            }
        });
        //加载需要显示的网页
        webview.loadUrl(BuildConfig.ETC_BASE_URL);
        //设置Web视图
        setContentView(webview);


        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        registerNetworkReceiver();
        EventBusUtil.register(this);
        super.onResume();
    }

    @Override
    protected void onPause(){
        unRegisterNetworkReceiver();
        EventBusUtil.unRegister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }

//    @Override
    //设置回退
    //覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
//            webview.goBack(); //goBack()表示返回WebView的上一页面
//            return true;
//        }
//        return false;
//    }

    private long lastClickBackTime ;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastClickBackTime < 2 * 1000) {
            finish();
        } else {
            if (webview.canGoBack()) {
                webview.goBack(); //goBack()表示返回WebView的上一页面
            }else{
                lastClickBackTime = System.currentTimeMillis() ;
                ToastUtils.showToastShort(getApplicationContext(),getString(R.string.tip_exit_app));
            }
        }
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkBroadcast, filter);
    }

    private void unRegisterNetworkReceiver() {
        this.unregisterReceiver(networkBroadcast);
    }

    /**
     * 检查版本更新
     */
    private void checkUpgrade() {
        final int versionCode = application.appVersionCode();
        MallApplication.instance().getModel(AppModel.class).getAppVersionInfo(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (TextUtils.isEmpty(resp)) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    UpgradeBean upgradeBean = GsonUtil.GsonToBean(jsonObj.optString("data"), UpgradeBean.class);
                    if (Integer.valueOf(upgradeBean.version) > versionCode) {
                        Intent intent = new Intent(MainWebActivity.this, UpgradeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("upgradeBean", upgradeBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getApplicationContext(),"检查版本信息失败");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                Log.i(TAG, "onFail: 网络错误");
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(UserCenterEven mUserCenterEven) {

    }
}
