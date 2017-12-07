package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityPayWebviewBinding;


/**
 * Created by gezi-pc on 2016/10/29.
 */

public class PayWebViewActivity  extends BaseNavBackActivity {

    ActivityPayWebviewBinding mActivityPayWebviewBinding;
    public static final String LOAD_PAY_URL_PARAM = "load_pay_url_param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityPayWebviewBinding= DataBindingUtil.setContentView(this, R.layout.activity_pay_webview);
        setNavDefaultBack(mActivityPayWebviewBinding.tb);
        super.onCreate(savedInstanceState);
        String url = getIntent().getStringExtra(LOAD_PAY_URL_PARAM);
        toOtherWebView(url);
        //initWebViewParam(url);

    }

    private void toOtherWebView(String url){
        Uri  uri = Uri.parse(url);
        Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
        this.finish();
    }

    private void initWebViewParam(String url) {


        WebSettings webSettings = mActivityPayWebviewBinding.wvPay.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        mActivityPayWebviewBinding.wvPay.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mActivityPayWebviewBinding.wvPay.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }


}
