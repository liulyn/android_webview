package com.cloud.tao.callback;

import android.view.View;
import android.widget.AdapterView;


/**
 * 防止多次点击
 * Created by zeda on 16/3/2.
 */
public abstract class NoDoubleItemClickListener implements AdapterView.OnItemClickListener {

    private static final long CLICK_SPACE = 500 ;
    private long lastClickTime = 0;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < CLICK_SPACE)
            return;
        lastClickTime = time;
        noDoubleClick(parent, view, position, id);
    }

    public abstract void noDoubleClick(AdapterView<?> parent, View view, int position, long id);
}
