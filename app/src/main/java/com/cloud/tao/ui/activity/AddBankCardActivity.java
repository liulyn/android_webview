package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityAddBankcardBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Created by gezi-pc on 2016/10/29.
 * 添加银行卡
 */

public class AddBankCardActivity  extends BaseNavBackActivity {


    private static final String TAG = AddBankCardActivity.class.getSimpleName();
    ActivityAddBankcardBinding mActivityAddBankcardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityAddBankcardBinding= DataBindingUtil.setContentView(this, R.layout.activity_add_bankcard);
        setNavDefaultBack(mActivityAddBankcardBinding.tb);
        mActivityAddBankcardBinding.loadding.showLoadSuccess();
        super.onCreate(savedInstanceState);
        initListener();
    }

    private void initListener() {
        mActivityAddBankcardBinding.btAddBankcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                saveBankCard();
            }
        });
    }

    private void saveBankCard() {
        String username = mActivityAddBankcardBinding.etBankUsername.getText().toString();
        String bankname = mActivityAddBankcardBinding.etBankName.getText().toString();
        String banknumber = mActivityAddBankcardBinding.etBankNumber.getText().toString();
        if(TextUtils.isEmpty(username)){
            ToastUtils.showToastShort(getBaseContext(),"持卡人姓名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(bankname)){
            ToastUtils.showToastShort(getBaseContext(),"银行名称不能为空！");
            return;
        }
        if(TextUtils.isEmpty(banknumber)){
            ToastUtils.showToastShort(getBaseContext(),"银行卡号不能为空！");
            return;
        }
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).addBankCard(TAG, username, banknumber, bankname, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(AddBankCardActivity.this, progressDialog);
                Log.e(TAG,msg);
                AddBankCardActivity.this.finish();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(AddBankCardActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(), msg);
                Log.e(TAG,msg);
            }
        });



    }
}
