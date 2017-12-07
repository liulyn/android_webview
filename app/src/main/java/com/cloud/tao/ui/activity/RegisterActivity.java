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
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.control.login.LoginProxy;
import com.cloud.tao.databinding.ActivityRegisterAccountBinding;
import com.cloud.tao.framwork.vl.VLApplication;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.loginmodel.LoginModel;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.MD5Util;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/9/10/08
 * des: 手机号注册
 */

public class RegisterActivity extends BaseNavBackActivity implements View.OnClickListener {

    public static final int CODE_MSG = BASE_CODE + 1;
    public static final int REGISTER_MSG = BASE_CODE + 2;
    private static final String TAG = "RegisterActivity";
    ActivityRegisterAccountBinding mRegisterAccountBinding ;
    public int count = 60 ;//更新次数
    public int time = 1000 ;//更新 计时器的间隔时间

    String mSmsToken="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRegisterAccountBinding = DataBindingUtil.setContentView(this,R.layout.activity_register_account);
        setNavDefaultBack(mRegisterAccountBinding.tb);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        mRegisterAccountBinding.btRegisterSendVerification.setOnClickListener(this);
        mRegisterAccountBinding.btRegisterAccount.setOnClickListener(this);
        mRegisterAccountBinding.tvRegisterAgreement.setOnClickListener(this);
        mRegisterAccountBinding.etRegisterMobile.addTextChangedListener(new OnRegisterTextWatcherListener(true));
        mRegisterAccountBinding.etRegisterVerification.addTextChangedListener(new OnRegisterTextWatcherListener(false));
        mRegisterAccountBinding.etRegisterPassword.addTextChangedListener(new OnRegisterTextWatcherListener(false));
        mRegisterAccountBinding.etRemCode.addTextChangedListener(new OnRegisterTextWatcherListener(false));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_register_send_verification :
                sendVerificationCode();
                break;
            case R.id.bt_register_account:
                toRegister();
                break;
            case R.id.tv_register_agreement:
                startActivity(new Intent(RegisterActivity.this,UserAgreementActivity.class));
                break;
        }
    }

    //文本输入监听
    private class OnRegisterTextWatcherListener implements TextWatcher {
        String mobilePhone;
        String verificationCode;
        String registerPassword;
        //String remCode;
        private boolean isPhoneEt;
        public OnRegisterTextWatcherListener(boolean isPhoneEt){
            this.isPhoneEt=isPhoneEt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            mobilePhone= mRegisterAccountBinding.etRegisterMobile.getText().toString().trim();
            verificationCode= mRegisterAccountBinding.etRegisterVerification.getText().toString().trim();
            registerPassword= mRegisterAccountBinding.etRegisterPassword.getText().toString().trim();

            checkIsEmpty();
        }
        private void checkIsEmpty(){
            if(!TextUtils.isEmpty(mobilePhone)&&!TextUtils.isEmpty(verificationCode)&&!TextUtils.isEmpty(registerPassword)){
                setBtnEnabled(mRegisterAccountBinding.btRegisterAccount, true);
            }else{
                setBtnEnabled(mRegisterAccountBinding.btRegisterAccount, false);
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
                    mRegisterAccountBinding.btRegisterSendVerification.setEnabled(true);
                    setBtnEnabled(mRegisterAccountBinding.etRegisterMobile,true);
                    mRegisterAccountBinding.btRegisterSendVerification.setText(getString(R.string.reset_get_verification));
                    count = 60 ;
                    return ;
                }
                mRegisterAccountBinding.btRegisterSendVerification.setText(String.valueOf(count)+"s");
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                break ;
            case REGISTER_MSG:
                boolean btnEnabled= (boolean) msg.obj;
                if(btnEnabled){
                    setBtnEnabled(mRegisterAccountBinding.btRegisterAccount,true);
                }
                break;
        }
    }

    public void sendVerificationCode() {
        final String mobilePhone = mRegisterAccountBinding.etRegisterMobile.getText().toString().trim();
        if (validatePhone(mobilePhone)) return;
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false) ;
        setBtnEnabled(mRegisterAccountBinding.btRegisterSendVerification, false);
        String tokenAppend=mobilePhone+UrlManager.CONSTANT_VERIFICATION_SIGNAL_KEY;
        String tokenKey=MD5Util.getMD5(tokenAppend,true);
        MallApplication.instance().getModel(LoginModel.class).verificationTokenCode(TAG,mobilePhone,tokenKey, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                try {
                    JSONObject object =new JSONObject(resp);
                    mSmsToken=object.optJSONObject("data").optString("sms_token");
                    String tips = getString(R.string.send_verification_hint);
                    tips = String.format(tips, mobilePhone);
                    ToastUtils.showToastShort(getApplicationContext(),tips);
                    mRegisterAccountBinding.btRegisterSendVerification.setText(String.valueOf(count)+"s");
                    setBtnEnabled(mRegisterAccountBinding.etRegisterMobile,false);
                    mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                } catch (JSONException e) {}
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(RegisterActivity.this,progressDialog);
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setBtnEnabled(mRegisterAccountBinding.btRegisterSendVerification,true);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(RegisterActivity.this,progressDialog);
            }
        });
    }

    public void toRegister(){
        String mobilePhone= mRegisterAccountBinding.etRegisterMobile.getText().toString().trim();
        String verificationCode= mRegisterAccountBinding.etRegisterVerification.getText().toString().trim();
        String registerPassword= mRegisterAccountBinding.etRegisterPassword.getText().toString().trim();
        String remCode= mRegisterAccountBinding.etRemCode.getText().toString().trim();
        if (validateInput(mobilePhone,registerPassword)) return;
        setBtnEnabled(mRegisterAccountBinding.btRegisterAccount,false);
        String lockPassword=MD5Util.getMD5(registerPassword,false);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_register_doing),false) ;
        LoginProxy.getInstance().register(mSmsToken,mobilePhone,verificationCode,lockPassword,remCode, new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>(){}) {
        @Override
            public void onSuccess(int code, String msg, LoginInfo resp) {
                if(code==0) {
                    ToastUtils.showToastShort(getApplicationContext(), "注册成功！");
                    AccountInfo.getInstance().saveAccountStatus(resp.data.login_time,resp.data.login_token,resp.data.login_u_client_id,resp.data.client_info.login_mobilephone,resp.data.session_id,resp.data.client_info);
                    enter2Main();
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
                ViewUtils.dismissDialog(RegisterActivity.this,progressDialog);
                Message mg=new Message();
                mg.what=REGISTER_MSG;
                mg.obj=true;
                mUiHandler.sendMessage(mg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(RegisterActivity.this,progressDialog);
            }
        });
    }

    private void enter2Main() {
        VLApplication app = VLApplication.instance();
        app.finishAllActivities(RegisterActivity.this);
        startActivity(new Intent(RegisterActivity.this, MainTabActivity.class));
        finish();
    }

}
