package com.cloud.tao.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.UserGiftInfo;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

/**
 * author:janecer on 2016/9/15 10:04
 *
 * 我的礼包列表
 */


public class MyGiftAdapter extends BaseLoadMoreViewAdapter<UserGiftInfo> {

    OnUseringClickListener  mUseringClickListener ;


    public MyGiftAdapter(Context context, int itemLayout, List<UserGiftInfo> list) {
        super(context, itemLayout, list);
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, final UserGiftInfo data, int position) {
        GlideUtil.getInstance().loadImage(mContext,data.icon,(ImageView) holder.getView(R.id.iv_icon));
        holder.setText(R.id.tv_name,data.title) ;
        holder.setText(R.id.tv_des,data.content) ;
        holder.setText(R.id.tv_user_time,data.starttime+"  " + data.endtime) ;
        if(null != mUseringClickListener) {
            holder.getView(R.id.tv_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUseringClickListener.onUserClick(data);
                }
            }) ;
        }
    }

    public void setUseringClickListener(OnUseringClickListener useringClickListener) {
        mUseringClickListener = useringClickListener;
    }

    public interface OnUseringClickListener {
       void onUserClick(UserGiftInfo data) ;
    }

}
