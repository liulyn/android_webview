package com.cloud.tao.callback;

import com.cloud.tao.ui.widget.FlowTagLayout;
import java.util.List;

/**
 * sunny created at 2016/10/18
 */
public interface OnTagSelectListener {
    void onItemSelect(FlowTagLayout parent, List<Integer> selectedList);
}
