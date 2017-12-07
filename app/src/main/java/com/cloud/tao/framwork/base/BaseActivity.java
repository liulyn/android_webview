package com.cloud.tao.framwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.cloud.tao.framwork.vl.VLActivity;
import com.cloud.tao.util.SoftInputUtils;


/**
 * author:janecer on 2016/2/10 11:55
 *
 * 简单的界面展示可继承此类
 */

public class BaseActivity extends VLActivity {

    private boolean isFirst = true ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initViews();

        initDatas(savedInstanceState);
    }

    protected  void initViews() {

    };


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus && isFirst) {
            isFirst =false ;
            loadData() ;
        }
    }

    protected void loadData(){} ;

    protected  void initDatas(Bundle savedInstanceState){};

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    protected void onBack() {
        if (this.getCurrentFocus() != null) {
            SoftInputUtils.closeSoftInput(getApplicationContext(), this.getCurrentFocus());
        }
        ActivityCompat.finishAfterTransition(this);
    }

}
