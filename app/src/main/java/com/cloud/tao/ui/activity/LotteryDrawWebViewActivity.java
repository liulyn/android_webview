package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityLotteryDrawWebviewBinding;
import com.cloud.tao.databinding.ActivityPayWebviewBinding;


/**
 * 抽奖的加载
 * Created by gezi-pc on 2016/10/29.
 */

public class LotteryDrawWebViewActivity extends BaseNavBackActivity {

    ActivityLotteryDrawWebviewBinding mActivityLotteryDrawWebviewBinding;
    public static final String LOAD_LOTTERY_DRAW_URL_PARAM = "load_lottery_draw_url_param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityLotteryDrawWebviewBinding= DataBindingUtil.setContentView(this, R.layout.activity_lottery_draw_webview);
        setNavDefaultBack(mActivityLotteryDrawWebviewBinding.tb);
        String url = getIntent().getStringExtra(LOAD_LOTTERY_DRAW_URL_PARAM);

        WebSettings webSettings = mActivityLotteryDrawWebviewBinding.wvPay.getSettings();
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(true);
        //加载需要显示的网页
        mActivityLotteryDrawWebviewBinding.wvPay.loadUrl(url);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mActivityLotteryDrawWebviewBinding.wvPay.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        super.onCreate(savedInstanceState);
    }


}
