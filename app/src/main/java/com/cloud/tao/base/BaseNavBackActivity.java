package com.cloud.tao.base;

import android.view.View;

import com.cloud.tao.framwork.base.BaseFragmentActivity;
import com.cloud.tao.ui.widget.TitleBar;


public abstract class BaseNavBackActivity extends BaseFragmentActivity {



    public void setNavDefaultBack(TitleBar titleBar) {
        titleBar.setOnLeftNavClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
    }
}
