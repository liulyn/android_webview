package com.cloud.tao.callback;

import android.view.View;
import com.cloud.tao.ui.widget.FlowTagLayout;

/**
 * sunny created at 2016/10/18
 */
public interface OnTagClickListener {
    void onItemClick(FlowTagLayout parent, View view, int position);
}
