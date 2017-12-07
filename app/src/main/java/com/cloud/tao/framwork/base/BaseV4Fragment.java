package com.cloud.tao.framwork.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;


/**
 * Created by Administrator on 2016/3/8.
 */
public class BaseV4Fragment extends android.support.v4.app.Fragment  {


    protected View mContentView ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
