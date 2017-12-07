package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityUserAgreementBinding;
import com.cloud.tao.util.Constants;

/**
 * sunny created at 2016/10/28
 * des: 用户协议
 */
public class UserAgreementActivity extends BaseNavBackActivity {

    ActivityUserAgreementBinding mUserAgreementBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserAgreementBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_agreement);
        setNavDefaultBack(mUserAgreementBinding.tb);
        mUserAgreementBinding.tvUserAgreement.setText(Constants.userAgreement);
        super.onCreate(savedInstanceState);
    }
}
