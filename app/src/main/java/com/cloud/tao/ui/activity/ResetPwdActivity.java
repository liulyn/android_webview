package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
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
import com.cloud.tao.databinding.ActivityResetPwdBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.MD5Util;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

/**
 * sunny created at 2016/9/10/13
 * des: 修改密码
 */
public class ResetPwdActivity extends BaseNavBackActivity implements View.OnClickListener{

    ActivityResetPwdBinding mResetPwdBinding;
    public static final int CODE_MSG = BASE_CODE + 1;
    public static final int RESET_MSG = BASE_CODE + 2;
    private static final String TAG = "RegisterActivity";
    public int count = 60 ;//更新次数
    public int time = 1000 ;//更新 计时器的间隔时间

    String mMobilePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResetPwdBinding= DataBindingUtil.setContentView(this, R.layout.activity_reset_pwd);
        setNavDefaultBack(mResetPwdBinding.tb);
        super.onCreate(savedInstanceState);
        mResetPwdBinding.btResetPwdSendVerification.setOnClickListener(this);
        mResetPwdBinding.btResetPwdAccount.setOnClickListener(this);
        mResetPwdBinding.etResetPwdMobile.addTextChangedListener(new OnResetPwdTextWatcherListener());
        mResetPwdBinding.etResetPwdVerification.addTextChangedListener(new OnResetPwdTextWatcherListener());
        mResetPwdBinding.etResetNewPassword.addTextChangedListener(new OnResetPwdTextWatcherListener());
        mResetPwdBinding.etResetConfirmNewPassword.addTextChangedListener(new OnResetPwdTextWatcherListener());
        initDatas(savedInstanceState);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        mMobilePhone=getIntent().getExtras().getString("mobilePhone");
        mResetPwdBinding.etResetPwdMobile.setText(CommonUtils.subMobileReplace(mMobilePhone));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset_pwd_send_verification :
                sendVerificationCode();
                break;
            case R.id.bt_reset_pwd_account:
                toResetPwd();
            default:break;
        }
    }

    //文本输入监听
    private class OnResetPwdTextWatcherListener implements TextWatcher {

        String pwdVerification;
        String newPassword;
        String confirmNewPassword;

        public OnResetPwdTextWatcherListener(){}

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            pwdVerification=mResetPwdBinding.etResetPwdVerification.getText().toString().trim();
            newPassword=mResetPwdBinding.etResetNewPassword.getText().toString().trim();
            confirmNewPassword=mResetPwdBinding.etResetConfirmNewPassword.getText().toString().trim();

            checkIsEmpty();
        }
        private void checkIsEmpty(){
            if(!TextUtils.isEmpty(pwdVerification)&&!TextUtils.isEmpty(newPassword)&&!TextUtils.isEmpty(confirmNewPassword)){
                setBtnEnabled(mResetPwdBinding.btResetPwdAccount, true);
            }else{
                setBtnEnabled(mResetPwdBinding.btResetPwdAccount, false);
            }
        }
    }

    private void setBtnEnabled(View view, boolean isEnabled){
        view.setEnabled(isEnabled);
    }


    private boolean validateInput(String newPassword,String confirmNewPassword) {
        if(!CommonUtils.checkPassword(newPassword)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_pwd_format_error_tip));
            return true;
        }
        if(!newPassword.equals(confirmNewPassword)){
            ToastUtils.showToastShort(getApplicationContext(),"输入的新密码与确认密码不一致");
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
                    mResetPwdBinding.btResetPwdSendVerification.setEnabled(true);
                    mResetPwdBinding.btResetPwdSendVerification.setText(getString(R.string.reset_get_verification));
                    count = 60 ;
                    return ;
                }
                mResetPwdBinding.btResetPwdSendVerification.setText(String.valueOf(count)+"s");
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                break ;
            case RESET_MSG:
                boolean btnEnabled= (boolean) msg.obj;
                if(btnEnabled){
                    setBtnEnabled(mResetPwdBinding.btResetPwdAccount,true);
                }
                break;
        }
    }

    public void sendVerificationCode() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false) ;
        setBtnEnabled(mResetPwdBinding.btResetPwdSendVerification, false);
        String tokenAppend=mMobilePhone+ UrlManager.CONSTANT_VERIFICATION_SIGNAL_KEY;
        String tokenKey= MD5Util.getMD5(tokenAppend,true);
        MallApplication.instance().getModel(AppModel.class).verificationCode(TAG,mMobilePhone,tokenKey, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                String tips = getString(R.string.send_verification_hint);
                tips = String.format(tips,CommonUtils.subMobileReplace(mMobilePhone));
                ToastUtils.showToastShort(getApplicationContext(),tips);
                mResetPwdBinding.btResetPwdSendVerification.setText(String.valueOf(count)+"s");
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ResetPwdActivity.this,progressDialog);
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setBtnEnabled(mResetPwdBinding.btResetPwdSendVerification,true);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ResetPwdActivity.this,progressDialog);
            }
        });
    }

    public void toResetPwd(){
        String verificationCode=mResetPwdBinding.etResetPwdVerification.getText().toString().trim();
        String newPassword=mResetPwdBinding.etResetNewPassword.getText().toString().trim();
        String confirmNewPassword=mResetPwdBinding.etResetConfirmNewPassword.getText().toString().trim();
        if (validateInput(newPassword,confirmNewPassword)) return;
        setBtnEnabled(mResetPwdBinding.btResetPwdAccount,false);

        String lockNewPassword=MD5Util.getMD5(newPassword,false);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_reset_pwd_doing),false) ;
        MallApplication.getInstance().getModel(AppModel.class).resetPassword(TAG,mMobilePhone,verificationCode,lockNewPassword, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if(code==0) {
                    ToastUtils.showToastShort(getApplicationContext(), "密码修改成功");
                    ResetPwdActivity.this.finish();
                    return;
                }
                onFail(code,null,msg);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                if(code==201){
                    ToastUtils.showToastShort(getApplicationContext(), "输入的验证码错误");
                }else{
                    ToastUtils.showToastShort(getApplicationContext(),msg);
                }
                ViewUtils.dismissDialog(ResetPwdActivity.this,progressDialog);
                Message mg=new Message();
                mg.what=RESET_MSG;
                mg.obj=true;
                mUiHandler.sendMessage(mg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ResetPwdActivity.this,progressDialog);
            }
        });
    }
}
