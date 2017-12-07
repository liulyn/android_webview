package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.FenxiaoMyTeamInfo;
import com.cloud.tao.bean.etc.MyVipRecommendItemInfo;
import com.cloud.tao.ui.widget.WrapContentListView;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

public class MyVipRecommendParentAdapter extends BaseLoadMoreViewAdapter {
    Context mContext;

    public MyVipRecommendParentAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final MyVipRecommendItemInfo myVipRecommendItemInfo = (MyVipRecommendItemInfo) data;
        holder.setText(R.id.ic_vip_recommend_phone, myVipRecommendItemInfo.login_mobilephone);
        holder.setText(R.id.tv_vip_parent_recommend_price, "+￥"+myVipRecommendItemInfo.reward);
        final ImageView mImvExRight=holder.getView(R.id.ic_vip_recommend_ex_right);
        final WrapContentListView mWrapContentListView=holder.getView(R.id.wl_vip_recommend_parent);
        final RelativeLayout rlRecommendInfo=holder.getView(R.id.rl_vip_recommend_parent);
        if ("0".equals(myVipRecommendItemInfo.total_son) || myVipRecommendItemInfo.total_son == null) {
            holder.getView(R.id.ic_vip_recommend_ex_right).setVisibility(View.GONE);
            mWrapContentListView.setVisibility(View.GONE);
            rlRecommendInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
        }else{
            holder.getView(R.id.ic_vip_recommend_ex_right).setVisibility(View.VISIBLE);
            MyVipRecommendAdapter mMyVipRecommendAdapter = new MyVipRecommendAdapter(this.mContext, myVipRecommendItemInfo.son);
            mWrapContentListView.setAdapter(mMyVipRecommendAdapter);
            rlRecommendInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(myVipRecommendItemInfo.isShow==1){
                        myVipRecommendItemInfo.isShow=0;
                        mImvExRight.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_ex_right));
                        mWrapContentListView.setVisibility(View.GONE);
                    }else{
                        myVipRecommendItemInfo.isShow=1;
                        mImvExRight.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_ex_down));
                        mWrapContentListView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

}
