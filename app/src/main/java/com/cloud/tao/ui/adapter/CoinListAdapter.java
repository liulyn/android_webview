package com.cloud.tao.ui.adapter;

import android.content.Context;

import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;

import java.util.List;

/**
 * author:janecer on 2016/9/13 17:19
 */


public class CoinListAdapter extends BaseLoadMoreViewAdapter<String> {

    public CoinListAdapter(Context context, int itemLayout, List<String> list) {
        super(context, itemLayout, list);
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, String data, int position) {

    }
}
