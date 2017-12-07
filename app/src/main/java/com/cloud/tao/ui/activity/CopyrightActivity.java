package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityCopyrightBinding;

/**
 * Created by gezi-pc on 2016/10/29.
 */

public class CopyrightActivity extends BaseNavBackActivity {


    ActivityCopyrightBinding mActivityCopyrightBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityCopyrightBinding =  DataBindingUtil.setContentView(this, R.layout.activity_copyright);
        mActivityCopyrightBinding.tvAboutWeAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CopyrightActivity.this,UserAgreementActivity.class));
            }
        });
        setNavDefaultBack(mActivityCopyrightBinding.tb);
        MallApplication application = (MallApplication) getApplication();
        mActivityCopyrightBinding.tvAboutVersion.setText("云之道 V"+application.appVersionName());
        super.onCreate(savedInstanceState);
    }
}
