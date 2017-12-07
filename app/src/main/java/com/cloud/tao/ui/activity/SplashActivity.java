package com.cloud.tao.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.cloud.tao.MainWebActivity;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.R;
import com.cloud.tao.control.AccountInfo;

/**
 * Created by sunny
 * des: 启动逻辑判断 程序主入口
 */

public class SplashActivity extends Activity {

    private Handler handler = new Handler();
    String loginMobilePhone;
    String loginLastTime;
    String loginSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        loginLastTime = AccountInfo.getInstance().getLastLoginTime() ;
        loginSessionId = AccountInfo.getInstance().getLastLoginSessionId() ;
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
//                login();
                enter2Web();
            }
        }, 500);
    }

    private void login() {
        if(TextUtils.isEmpty(loginLastTime)|| TextUtils.isEmpty(loginSessionId)) {
            enter2Login();
        }else{
            enter2Main();
        }
    }

    private void enter2Login() {
        startActivity(new Intent(SplashActivity.this, LoginAccountActivity.class));
        finish();
    }

    private void enter2Main() {
        startActivity(new Intent(SplashActivity.this, MainTabActivity.class));
        finish();
    }

    private void enter2Web(){
        startActivity(new Intent(SplashActivity.this, MainWebActivity.class));
        finish();
    }
}
