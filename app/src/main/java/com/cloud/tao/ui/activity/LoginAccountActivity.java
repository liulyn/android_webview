package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.cloud.tao.bean.etc.event.WXLoginEven;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.framwork.base.BaseFragmentActivity;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.wxmodel.WXmodel;
import com.cloud.tao.net.model.wxmodel.reqresp.GetAccessTokenResp;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.MD5Util;
import com.cloud.tao.util.eventbusutil.EventBusUtil;
import com.cloud.tao.wxapi.WXEntryActivity;
import com.cloud.tao.wxapi.WxSpInfoUtil;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.R;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.control.login.LoginProxy;
import com.cloud.tao.databinding.ActivityLoginAccountBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;

import org.greenrobot.eventbus.Subscribe;

/**
 * sunny created at 2016/9/10/06
 * des: 账号登录
 */

public class LoginAccountActivity extends BaseFragmentActivity{

    ActivityLoginAccountBinding mLoginAccountBinding ;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLoginAccountBinding = DataBindingUtil.setContentView(this,R.layout.activity_login_account);

        mLoginAccountBinding.btLoginAccount.setOnClickListener(mOnDoubleClickListener);
        mLoginAccountBinding.btLoginRegister.setOnClickListener(mOnDoubleClickListener);
        mLoginAccountBinding.tvForgetPwd.setOnClickListener(mOnDoubleClickListener);
        //mLoginAccountBinding.ivLoginWechat.setOnClickListener(mOnDoubleClickListener);

        mLoginAccountBinding.etAccount.addTextChangedListener(new OnLoginTextWatcherListener());
        mLoginAccountBinding.etPwd.addTextChangedListener(new OnLoginTextWatcherListener());

        EventBusUtil.register(this);
        super.onCreate(savedInstanceState);
    }

    private NoDoubleClickListener mOnDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.bt_login_account:
                    login();
                    break;
                case R.id.bt_login_register:
                    startActivity(new Intent(LoginAccountActivity.this, RegisterActivity.class));
                    break;
                case R.id.tv_forget_pwd:
                    startActivity(new Intent(LoginAccountActivity.this, ForgetPwdActivity.class));
                    break;
               /*case R.id.iv_login_wechat:
                    Intent intent = new Intent(LoginAccountActivity.this, WXEntryActivity.class) ;
                    intent.putExtra("type",-1) ;
                    startActivity(intent);
                    break;*/
            }
        }
    };

    @Subscribe
    public void onEventMainThread(WXLoginEven wxLoginEven) {
        loginForWX();
    }

    public void loginForWX() {
        GetAccessTokenResp tokenInfo  = WxSpInfoUtil.getInstance(getApplicationContext()).getTokenInfo() ;
        if(null == tokenInfo || TextUtils.isEmpty(tokenInfo.access_token)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.login_fail));
            return ;
        }
        UserInfoResp wxUser = WxSpInfoUtil.getInstance(getApplicationContext()).getUserInfo() ;
        if(null == wxUser) {
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.login_fail));
            return ;
        }
        mProgressDialog =VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_login_doing),false);
        String appendToken= WXmodel.W_APPID+WXmodel.W_APISECRET+ UrlManager.CONSTANT_VERIFICATION_SIGNAL_KEY;
        String loginToken=MD5Util.getMD5(appendToken,true);
        LoginProxy.getInstance().loginWx(loginToken, wxUser, new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>(){}) {
            @Override
            public void onSuccess(int code, String msg, LoginInfo resp) {
                if(null != resp) {
                    AccountInfo.getInstance().saveAccountStatus(resp.data.login_time,resp.data.login_token,resp.data.login_u_client_id,resp.data.client_info.login_mobilephone,resp.data.session_id,resp.data.client_info);
                    enter2Main();
                    return ;
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(),msg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(LoginAccountActivity.this,mProgressDialog);
            }
        });
    }

    private boolean validatePhone(String phone) {
        if(!CommonUtils.isMobile(phone)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_format_error_tip));
            return true;
        }
        return false;
    }

    private void setBtnEnabled(View view, boolean isEnabled){
        view.setEnabled(isEnabled);
    }

    //文本输入监听
    private class OnLoginTextWatcherListener implements TextWatcher {
        public OnLoginTextWatcherListener(){}
        String account;
        String pwd;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            account= mLoginAccountBinding.etAccount.getText().toString().trim();
            pwd= mLoginAccountBinding.etPwd.getText().toString().trim();
            checkIsEmpty();
        }
        private void checkIsEmpty(){
            if(!TextUtils.isEmpty(account)&&!TextUtils.isEmpty(pwd)){
                setBtnEnabled(mLoginAccountBinding.btLoginAccount, true);
            }else{
                setBtnEnabled(mLoginAccountBinding.btLoginAccount, false);
            }
        }
    }

    public void login() {
        final String account = mLoginAccountBinding.etAccount.getText().toString().trim();
        final String pwd = mLoginAccountBinding.etPwd.getText().toString().trim();
        if(validatePhone(account)) return;
        String lockPwd= MD5Util.getMD5(pwd,false);
        mProgressDialog= VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_login_doing),false);
        LoginProxy.getInstance().login(account, lockPwd, new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>(){}) {
            @Override
            public void onSuccess(int code, String msg, LoginInfo resp){
                if(null != resp) {
                    AccountInfo.getInstance().saveAccountStatus(resp.data.login_time,resp.data.login_token,resp.data.login_u_client_id,resp.data.client_info.login_mobilephone,resp.data.session_id,resp.data.client_info);
                    enter2Main();
                    return ;
                }
                onFail(code,null,msg);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(LoginAccountActivity.this,mProgressDialog);
                ToastUtils.showToastShort(getApplicationContext(),msg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(LoginAccountActivity.this,mProgressDialog);
            }
        });
    }

    private void enter2Main() {
        startActivity(new Intent(LoginAccountActivity.this, MainTabActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unRegister(this);
        super.onDestroy();
    }
}
