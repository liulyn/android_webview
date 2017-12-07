package com.cloud.tao.callback;

import android.view.View;

import com.cloud.tao.base.BaseRecyViewHolder;


/**
 * holderView长按事件
 */
public abstract class OnHolderViewLongClickListener implements View.OnLongClickListener {

    private BaseRecyViewHolder holder;

    public OnHolderViewLongClickListener(BaseRecyViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean onLongClick(View v) {
        return onLongClick(holder, v);
    }

    public abstract boolean onLongClick(BaseRecyViewHolder holder, View v);
}
