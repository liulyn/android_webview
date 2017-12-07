package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.control.login.LoginProxy;
import com.cloud.tao.databinding.ActivityForgetPwdBinding;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.util.MD5Util;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.R;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.loginmodel.LoginModel;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/9/10/13
 * des: 忘记密码
 */
public class ForgetPwdActivity extends BaseNavBackActivity implements View.OnClickListener {

    public static final int CODE_MSG = BASE_CODE + 1;
    public static final int RESET_MSG = BASE_CODE + 2;
    private static final String TAG = "ForgetPwdActivity";
    ActivityForgetPwdBinding mForgetPwdBinding ;
    public int count = 60 ;//更新次数
    public int time = 1000 ;//更新 计时器的间隔时间

    String mSmsToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mForgetPwdBinding = DataBindingUtil.setContentView(this,R.layout.activity_forget_pwd);
        setNavDefaultBack(mForgetPwdBinding.tb);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        mForgetPwdBinding.btForgetSendVerification.setOnClickListener(this);
        mForgetPwdBinding.btForgetAccount.setOnClickListener(this);
        mForgetPwdBinding.etForgetMobile.addTextChangedListener(new OnForgetTextWatcherListener(true));
        mForgetPwdBinding.etForgetVerification.addTextChangedListener(new OnForgetTextWatcherListener(false));
        mForgetPwdBinding.etForgetPassword.addTextChangedListener(new OnForgetTextWatcherListener(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_forget_send_verification :
                sendVerificationCode();
                break;
            case R.id.bt_forget_account:
                toResetPwd();
            default:break;
        }
    }

    //文本输入监听
    private class OnForgetTextWatcherListener implements TextWatcher {
        String mobilePhone;
        String verificationCode;
        String resetPassword;
        private boolean isPhoneEt;
        public OnForgetTextWatcherListener(boolean isPhoneEt){
            this.isPhoneEt=isPhoneEt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            mobilePhone= mForgetPwdBinding.etForgetMobile.getText().toString().trim();
            verificationCode= mForgetPwdBinding.etForgetVerification.getText().toString().trim();
            resetPassword= mForgetPwdBinding.etForgetPassword.getText().toString().trim();

            checkIsEmpty();
        }
        private void checkIsEmpty(){
            if(!TextUtils.isEmpty(mobilePhone)&&!TextUtils.isEmpty(verificationCode)&&!TextUtils.isEmpty(resetPassword)){
                setBtnEnabled(mForgetPwdBinding.btForgetAccount, true);
            }else{
                setBtnEnabled(mForgetPwdBinding.btForgetAccount, false);
            }
        }
    }

    private void setBtnEnabled(View view, boolean isEnabled){
        view.setEnabled(isEnabled);
    }

    private boolean validatePhone(String phone) {
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showToastShort(getApplicationContext(),"请输入手机号");
            return true;
        }
        if(!CommonUtils.isMobile(phone)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_format_error_tip));
            return true;
        }
        return false;
    }

    private boolean validateInput(String mobilePhone,String passWord) {
        if(TextUtils.isEmpty(mSmsToken)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_empty_error_tip));
            return true;
        }
        if(validatePhone(mobilePhone)){
            return true;
        }
        if(!CommonUtils.checkPassword(passWord)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_pwd_format_error_tip));
            return true;
        }
        return false;
    }



    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case CODE_MSG :
                if(--count == 0) {
                    setBtnEnabled(mForgetPwdBinding.etForgetMobile,true);
                    mForgetPwdBinding.btForgetSendVerification.setEnabled(true);
                    mForgetPwdBinding.btForgetSendVerification.setText(getString(R.string.reset_get_verification));
                    count = 60 ;
                    return ;
                }
                mForgetPwdBinding.btForgetSendVerification.setText(String.valueOf(count)+"s");
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                break ;
            case RESET_MSG:
                boolean btnEnabled= (boolean) msg.obj;
                if(btnEnabled){
                    setBtnEnabled(mForgetPwdBinding.btForgetAccount,true);
                }
                break;
        }
    }

    public void sendVerificationCode() {
        final String mobilePhone = mForgetPwdBinding.etForgetMobile.getText().toString().trim();
        if (validatePhone(mobilePhone)){
            return;
        }
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false) ;
        setBtnEnabled(mForgetPwdBinding.btForgetSendVerification, false);
        String tokenAppend=mobilePhone+ UrlManager.CONSTANT_VERIFICATION_SIGNAL_KEY;
        String tokenKey= MD5Util.getMD5(tokenAppend,true);
        MallApplication.instance().getModel(LoginModel.class).verificationTokenCode(TAG,mobilePhone,tokenKey, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                try {
                    JSONObject object =new JSONObject(resp);
                    mSmsToken=object.optJSONObject("data").optString("sms_token");
                    String tips = getString(R.string.send_verification_hint);
                    tips = String.format(tips, mobilePhone);
                    ToastUtils.showToastShort(getApplicationContext(),tips);
                    mForgetPwdBinding.btForgetSendVerification.setText(String.valueOf(count)+"s");
                    setBtnEnabled(mForgetPwdBinding.etForgetMobile,false);
                    mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                } catch (JSONException e) {}
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ForgetPwdActivity.this,progressDialog);
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setBtnEnabled(mForgetPwdBinding.btForgetSendVerification,true);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ForgetPwdActivity.this,progressDialog);
            }
        });
    }

    public void toResetPwd(){
        String mobilePhone= mForgetPwdBinding.etForgetMobile.getText().toString().trim();
        String verificationCode= mForgetPwdBinding.etForgetVerification.getText().toString().trim();
        String registerPassword= mForgetPwdBinding.etForgetPassword.getText().toString().trim();
        if (validateInput(mobilePhone,registerPassword)) return;
        setBtnEnabled(mForgetPwdBinding.btForgetAccount,false);
        String lockPassword=MD5Util.getMD5(registerPassword,false);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_reset_pwd_doing),false) ;
        LoginProxy.getInstance().forgetPwd(mSmsToken,mobilePhone,verificationCode,lockPassword, new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>(){}) {
            @Override
            public void onSuccess(int code, String msg, LoginInfo resp) {
                if(code==0) {
                    ToastUtils.showToastShort(getApplicationContext(), "密码重置成功，请登录！");
                    enter2Login();
                    return;
                }
                onFail(code,null,msg);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                if(code==201){
                    ToastUtils.showToastShort(getApplicationContext(), "输入的验证码错误");  //code==201
                }else{
                    ToastUtils.showToastShort(getApplicationContext(),msg);
                }
                ViewUtils.dismissDialog(ForgetPwdActivity.this,progressDialog);
                Message mg=new Message();
                mg.what=RESET_MSG;
                mg.obj=true;
                mUiHandler.sendMessage(mg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ForgetPwdActivity.this,progressDialog);
            }
        });
    }

    private void enter2Login() {
        startActivity(new Intent(ForgetPwdActivity.this, LoginAccountActivity.class));
        finish();
    }
}
