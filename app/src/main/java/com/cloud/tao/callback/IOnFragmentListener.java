package com.cloud.tao.callback;

import android.os.Message;


/**
 * Created by janecer on 2016/6/6 0006
 * des:
 */
public interface IOnFragmentListener {
    void deliverMsg2Activity(IPullAction action, Message msg);
}
