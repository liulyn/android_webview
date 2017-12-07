package com.cloud.tao.ui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.bean.GameInfo;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

/**
 * Created by janecer on 2016/9/12 0012
 * des:
 */
public class GameSortAdapter extends BaseLoadMoreViewAdapter<GameInfo> {

    private NoDoubleClickListener mOnInGameClickListener ;//进入点击事件

    public void setOnInGameClickListener(NoDoubleClickListener onInGameClickListener) {
        mOnInGameClickListener = onInGameClickListener;
    }

    public GameSortAdapter(Context context, int itemLayout, List<GameInfo> list) {
        super(context, itemLayout, list);
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder vh, GameInfo item, int position) {
        GlideUtil.getInstance().loadImage(mContext,item.icon,(ImageView)vh.getView(R.id.iv_icon));
        ((TextView)vh.getView(R.id.tv_name)).setText(item.name);
        ((TextView)vh.getView(R.id.tv_sort)).setText("/" + item.typename);
        String popNum = item.clicknum  + "人玩过/" + item.gcount + "个礼包可领取" ;

        ForegroundColorSpan colorPopSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorPrimary)) ;
        ForegroundColorSpan colorGiftSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.red_gift_num)) ;

        SpannableString spnner = new SpannableString(popNum) ;
        spnner.setSpan(colorPopSpan,0,popNum.length() - 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spnner.setSpan(colorGiftSpan,4 + (""+item.clicknum).length(),popNum.length() - 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE );
        TextView tvInfo = vh.getView(R.id.tv_last_login_time);
        tvInfo.setAlpha(1.0f);
        tvInfo.setTextColor(mContext.getResources().getColor(R.color.gray_text));
        tvInfo.setText(spnner);

        if(null != mOnInGameClickListener) {
            TextView tvInGame = vh.getView(R.id.tv_in_game);
            tvInGame.setTag(item.id);
            tvInGame.setOnClickListener(mOnInGameClickListener);
        }

    }
}
