package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.framwork.base.BaseFragmentActivity;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.R;
import com.cloud.tao.databinding.ActivityBindPhoneBinding;

/**
 * author:janecer on 2016/9/6 20:33
 */


public class BindUnBindPhoneActivity extends BaseNavBackActivity {

    private static final String TAG = "BindUnBindPhoneActivity";
    public static final int CODE_MSG = BaseFragmentActivity.BASE_CODE + 1;
    ActivityBindPhoneBinding mBindPhoneBinding ;
    LoginInfo mLoginInfo ;
    private boolean isBindPhone  ;
    public int count = 60 ;//更新次数
    public int time = 1000 ;//更新 计时器的间隔时间

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBindPhoneBinding = DataBindingUtil.setContentView(this, R.layout.activity_bind_phone) ;
        super.onCreate(savedInstanceState);

        setNavDefaultBack(mBindPhoneBinding.tb);
        mLoginInfo = AccountInfo.getInstance().getLoginInfo() ;
        //isBindPhone = TextUtils.isEmpty(mLoginInfo.mobile );
        mBindPhoneBinding.tb.setTitleText(isBindPhone ? getString(R.string.bind_phone):getString(R.string.unbind_phone));


    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        mBindPhoneBinding.tb.setOnRightNavClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindOrUnbind();
            }
        });
        mBindPhoneBinding.tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);
        switch (msg.what) {
            case CODE_MSG :
                if(--count == 0) {
                    mBindPhoneBinding.tvCode.setClickable(true);
                    mBindPhoneBinding.tvCode.setText(getString(R.string.get_msg_code));
                    count = 60 ;
                    return ;
                }
                mBindPhoneBinding.tvCode.setText(String.valueOf(count));
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
                break ;
        }
    }


    public void bindOrUnbind() {
        final String moblie = mBindPhoneBinding.etPhone.getText().toString(); ;
        if (validataPhone(moblie)) return;

        //用户输入的验证码
        String editCode = mBindPhoneBinding.etCode.getText().toString() ;
        if(TextUtils.isEmpty(editCode)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_code));
            return ;
        }

        //短信的验证码
        String msgCode = SharePrefUtil.getString(getApplicationContext(),SharePrefUtil.KEY.function_msg_code,"") ;
        if(TextUtils.isEmpty(msgCode)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.agin_get_code));
            return ;
        }

        //用户收到的验证码与 前端生成的验证码是否一致
        if(!editCode.equals(msgCode)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_code_diffrence));
            return ;
        }

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(android.R.string.dialog_alert_title),getString(R.string.dialog_common_doing),false) ;
       /* MallApplication.getInstance().getModel(LoginModel.class).bindOrunBindMobile(TAG, isBindPhone, moblie, mLoginInfo.token, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                //AccountInfo.getInstance().getLoginInfo().mobile = moblie ;
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setResult(Activity.RESULT_OK);
                onBack();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(),msg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(BindUnBindPhoneActivity.this,progressDialog);
            }
        });*/
    }

    public void sendCode() {
        final String phone = mBindPhoneBinding.etPhone.getText().toString();
        if (validataPhone(phone)) return;
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(android.R.string.dialog_alert_title),getString(R.string.dialog_common_doing),false) ;
        final String codeString = CommonUtils.getRandom() ;
        /*MallApplication.instance().getModel(LoginModel.class).phoneCode(TAG, isBindPhone ? 2 : 3, phone, codeString, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                Logger.msg(TAG,msg +"  resp:" + resp);
                ToastUtils.showToastShort(getApplicationContext(),msg);
                SharePrefUtil.saveString(getApplicationContext(),SharePrefUtil.KEY.function_msg_code,""+codeString);


                mBindPhoneBinding.tvCode.setClickable(false);
                mBindPhoneBinding.tvCode.setText(String.valueOf(count));
                mUiHandler.sendEmptyMessageDelayed(CODE_MSG,1000) ;
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(),msg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                ViewUtils.dismissDialog(BindUnBindPhoneActivity.this,progressDialog);
            }
        });*/
    }

    private boolean validataPhone(String phone) {
        if(TextUtils.isEmpty(phone)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_tip));
            return true;
        }
        if(!CommonUtils.isMobile(phone)){
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.input_phone_format_error_tip));
            return true;
        }
        return false;
    }

}
