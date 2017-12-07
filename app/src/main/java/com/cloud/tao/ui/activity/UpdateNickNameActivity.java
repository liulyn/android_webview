package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityUpdateNicknameBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.util.ToastUtils;

/**
 * author:janecer on 2016/9/6 20:10
 */


public class UpdateNickNameActivity extends BaseNavBackActivity {

    private static final String TAG = "UpdateNickNameActivity";
    ActivityUpdateNicknameBinding mUpdateNicknameBinding ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUpdateNicknameBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_nickname);
        super.onCreate(savedInstanceState);
        setNavDefaultBack(mUpdateNicknameBinding.tb);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
       /* mUpdateNicknameBinding.etNickname.setText(AccountInfo.getInstance().getLoginInfo().nickname);
        mUpdateNicknameBinding.tb.setOnRightNavClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存昵称
                saveNickName();
            }
        });*/
    }


    public void saveNickName() {
        final String nickName = mUpdateNicknameBinding.etNickname.getText().toString();
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.nickname_is_not_null));
            return ;
        }
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(android.R.string.dialog_alert_title),getString(R.string.dialog_common_doing),false) ;
        /*MallApplication.getInstance().getModel(LoginModel.class).updateInfo(TAG, nickName, AccountInfo.getInstance().getLoginInfo().token, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                AccountInfo.getInstance().getLoginInfo().nickname = nickName ;
                ToastUtils.showToastShort(getApplicationContext(),msg);
                setResult(RESULT_OK);
                onBack();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(),msg);
            }

            @Override
            public void onAfter(int id) {
                ViewUtils.dismissDialog(UpdateNickNameActivity.this,progressDialog);
                super.onAfter(id);
            }
        });*/
    }
}
