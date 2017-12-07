package com.cloud.tao.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import com.cloud.tao.bean.ConsumeRecordInfo;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.ChargeRecordInfo;

import java.util.List;


// TODO: 2016/9/15 按照月份分类显示，消费记录缺乏一个时间字段
public class ChargerRecordAdapter extends BaseLoadMoreViewAdapter {


    public ChargerRecordAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        if(data instanceof ChargeRecordInfo) {
            bindChargerRecorderInfoViews(holder,(ChargeRecordInfo)data,position);
        } else if(data instanceof ConsumeRecordInfo) {
            bindConsumRecorderInfoViews(holder,(ConsumeRecordInfo)data,position);
        }
    }

    //绑定购买记录
    private void bindChargerRecorderInfoViews(BaseRecyViewHolder holder, ChargeRecordInfo data,int position) {
        //holder.getView(R.id.tv_mouth).setVisibility(position);
        holder.setText(R.id.tv_time,data.create_time) ;
        holder.setText(R.id.tv_game_name,"充值¥" + data.money) ;

        String coin = "获得" + data.ptbcnt + "金币" ;
        ForegroundColorSpan colorPopSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)) ;
        SpannableString spnner = new SpannableString(coin) ;
        spnner.setSpan(colorPopSpan,2,coin.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.setText(R.id.tv_get_coin,spnner.toString()) ;

    }
    //绑定消费记录
    private void bindConsumRecorderInfoViews(BaseRecyViewHolder holder, ConsumeRecordInfo data,int position) {
       // holder.setText(R.id.tv_time,data.create_time) ;
        holder.setText(R.id.tv_time,"") ;
        holder.setText(R.id.tv_game_name,data.game) ;

        String coin = "消费" + data.gold + "金币" ;
        ForegroundColorSpan colorPopSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)) ;
        SpannableString spnner = new SpannableString(coin) ;
        spnner.setSpan(colorPopSpan,2,coin.length() - 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.setText(R.id.tv_get_coin,spnner.toString()) ;
    }
}
