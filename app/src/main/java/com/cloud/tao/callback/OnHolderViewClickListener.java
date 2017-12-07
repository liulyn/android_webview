package com.cloud.tao.callback;

import android.view.View;

import com.cloud.tao.base.BaseRecyViewHolder;


/**
 * holderView点击事件
 */
public abstract class OnHolderViewClickListener extends NoDoubleClickListener {

    private BaseRecyViewHolder holder;

    public OnHolderViewClickListener(BaseRecyViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void noDoubleClick(View v) {
        onClick(holder, v);
    }

    public abstract void onClick(BaseRecyViewHolder holder, View v);
}
