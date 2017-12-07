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
import com.cloud.tao.bean.etc.AccountInfo;
import com.cloud.tao.databinding.ActivityResetMobileBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.MD5Util;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

/**
 * sunny created at 2016/9/10/13
 * des: 绑定手机
 */
public class ResetMobileActivity extends BaseNavBackActivity implements View.OnClickListener{
    
    public static final int CODE_MSG = BASE_CODE + 1;
    public static final int RESET_MSG = BASE_CODE + 2;
    private static final String TAG = "ResetMobileActivity";
    public int count = 60 ;//更新次数
    public int time = 1000 ;//更新 计时器的间隔时间
    ActivityResetMobileBinding mResetMobileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mResetMobileBinding= DataBindingUtil.setContentView(this, R.layout.activity_reset_mobile);
        setNavDefaultBack(mResetMobileBinding.tb);
        super.onCreate(savedInstanceState);
        mResetMobileBinding.etResetMobile.addTextChangedListener(new OnResetMobileTextWatcherListener(true));
        mResetMobileBinding.etResetMobileVerification.addTextChangedListener(new OnResetMobileTextWatcherListener(false));
        mResetMobileBinding.btResetMobileVerification.setOnClickListener(this);
        mResetMobileBinding.btResetMobileAccount.setOnClickListener(this);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
    }


    //文本输入监听
    private class OnResetMobileTextWatcherListener implements TextWatcher {
        String mobilePhone;
        String verificationCode;
        private boolean isPhoneEt;
        public OnResetMobileTextWatcherListener(boolean isPhoneEt){
            this.isPhoneEt=isPhoneEt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            mobilePhone= mResetMobileBinding.etResetMobile.getText().toString().trim();
            verificationCode= mResetMobileBinding.etResetMobileVerification.getText().toString().trim();
            if(isPhoneEt){
                if(!TextUtils.isEmpty(mobilePhone)){
                    setBtnEnabled(mResetMobileBinding.btResetMobileVerification,true);
                }else{
                    setBtnEnabled(mResetMobileBinding.btResetMobileVerification,false);
                }
            }

            checkIsEmpty();
        }
        private void checkIsEmpty(){
            if(!TextUtils.isEmpty(mobilePhone)&&!TextUtils.isEmpty(verificationCode)){
                setBtnEnabled(mResetMobileBinding.btResetMobileAccount, true);
            }else{
                setBtnEnabled(mResetMobileBinding.btResetMobileAccount, false);
            }
        }
    }

    private void setBtnEnabled(View view, boolean isEnabled){
        view.setEnabled(isEnabled);
    }

    private boolean validatePhone(String phone) {
        if(!CommonUtils.isMobile(phone)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_format_error_tip));
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
                    mResetMobileBinding.btResetMobileVerification.setEnabled(true);
                    setBtnEnabled(mResetMobileBinding.etResetMobile,true);
                    mResetMobileBinding.btResetMobileVerification.setText(getString(R.string.reset_get_verification));
                    count = 60 ;
                    return ;
                }
                mResetMobileBinding.btResetMobileVerification.setText(String.valueOf(count)+"s");
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                break ;
            case RESET_MSG:
                boolean btnEnabled= (boolean) msg.obj;
                if(btnEnabled){
                    setBtnEnabled(mResetMobileBinding.btResetMobileAccount,true);
                }
                break;
        }
    }

    public void sendVerificationCode() {
        final String mobilePhone = mResetMobileBinding.etResetMobile.getText().toString().trim();
        if (validatePhone(mobilePhone)) return;
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false) ;
        setBtnEnabled(mResetMobileBinding.btResetMobileVerification, false);
        String tokenAppend=mobilePhone+ UrlManager.CONSTANT_VERIFICATION_SIGNAL_KEY;
        String tokenKey= MD5Util.getMD5(tokenAppend,true);
        MallApplication.instance().getModel(AppModel.class).verificationCode(TAG,mobilePhone,tokenKey, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                String tips = getString(R.string.send_verification_hint);
                tips = String.format(tips, mobilePhone);
                ToastUtils.showToastShort(getApplicationContext(),tips);
                mResetMobileBinding.btResetMobileVerification.setText(String.valueOf(count)+"s");
                setBtnEnabled(mResetMobileBinding.etResetMobile,false);
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ResetMobileActivity.this,progressDialog);
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setBtnEnabled(mResetMobileBinding.btResetMobileVerification,true);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ResetMobileActivity.this,progressDialog);
            }
        });
    }

    public void toResetMobilePhone(){
        final String newMobilePhone= mResetMobileBinding.etResetMobile.getText().toString().trim();
        String verificationCode= mResetMobileBinding.etResetMobileVerification.getText().toString().trim();
        if (validatePhone(newMobilePhone)) return;
        setBtnEnabled(mResetMobileBinding.btResetMobileAccount,false);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_reset_pwd_doing),false) ;
        MallApplication.getInstance().getModel(AppModel.class).resetMobilePhone(TAG,newMobilePhone,verificationCode, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if(code==0) {
                    AccountInfo info = (AccountInfo) SharePrefUtil.getObjectFromShare(ResetMobileActivity.this,SharePrefUtil.KEY.function_user_info);
                    info.setLogin_mobilephone(newMobilePhone);
                    SharePrefUtil.setObjectToShare(ResetMobileActivity.this,SharePrefUtil.KEY.function_user_info,info);
                    SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_mobile_phone,newMobilePhone);
                    ToastUtils.showToastShort(getApplicationContext(), "手机号绑定成功");
                    ResetMobileActivity.this.finish();
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
                ViewUtils.dismissDialog(ResetMobileActivity.this,progressDialog);
                Message mg=new Message();
                mg.what=RESET_MSG;
                mg.obj=true;
                mUiHandler.sendMessage(mg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(ResetMobileActivity.this,progressDialog);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset_mobile_verification:
                sendVerificationCode();
                break;
            case R.id.bt_reset_mobile_account:
                toResetMobilePhone();
                break;
            default:break;
        }
    }
}
