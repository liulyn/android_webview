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
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.ui.widget.WrapContentListView;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

/**
 * sunny created at 2016/10/26
 * des: 我的团队
 */
public class FenxiaoMyTeamParentAdapter extends BaseLoadMoreViewAdapter {
    Context mContext;

    public FenxiaoMyTeamParentAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final FenxiaoMyTeamInfo fenxiaoMyTeamInfo = (FenxiaoMyTeamInfo) data;
        if (!TextUtils.isEmpty(fenxiaoMyTeamInfo.private_headimgurl)) {
            GlideUtil.getInstance().loadImage(mContext, fenxiaoMyTeamInfo.private_headimgurl, (ImageView) holder.getView(R.id.tv_my_team_parent_icon));
        } else {
            GlideUtil.getInstance().loadImage(mContext, R.mipmap.ic_fenxiao_team_default, (ImageView) holder.getView(R.id.tv_my_team_parent_icon));
        }
        holder.setText(R.id.tv_my_team_parent_phone, fenxiaoMyTeamInfo.login_mobilephone);
        holder.setText(R.id.tv_my_team_parent_rank_name, fenxiaoMyTeamInfo.rank_name);
        holder.setText(R.id.tv_my_team_parent_num, fenxiaoMyTeamInfo.total_son + "个成员");
        holder.setText(R.id.tv_my_team_parent_price, "+￥" + fenxiaoMyTeamInfo.commission_total);
        final ImageView mImvExRight=holder.getView(R.id.ic_fenxiao_my_team_ex_right);
        final WrapContentListView mWrapContentListView=holder.getView(R.id.wl_fenxiao_my_parent_team);
        final RelativeLayout rlTeamInfo=holder.getView(R.id.rl_fenxiao_my_parent_team_info);
        if ("0".equals(fenxiaoMyTeamInfo.total_son) || fenxiaoMyTeamInfo.total_son == null) {
            holder.getView(R.id.tv_my_team_parent_num).setVisibility(View.GONE);
            holder.getView(R.id.ic_fenxiao_my_team_ex_right).setVisibility(View.GONE);
            mWrapContentListView.setVisibility(View.GONE);
            rlTeamInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
        }else{
            holder.getView(R.id.tv_my_team_parent_num).setVisibility(View.VISIBLE);
            holder.getView(R.id.ic_fenxiao_my_team_ex_right).setVisibility(View.VISIBLE);
            FenxiaoMyTeamAdapter mFenxiaoMyTeamAdapter = new FenxiaoMyTeamAdapter(this.mContext, fenxiaoMyTeamInfo.son);
            mWrapContentListView.setAdapter(mFenxiaoMyTeamAdapter);
            rlTeamInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(fenxiaoMyTeamInfo.isShow==1){
                        fenxiaoMyTeamInfo.isShow=0;
                        mImvExRight.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_ex_right));
                        mWrapContentListView.setVisibility(View.GONE);
                    }else{
                        fenxiaoMyTeamInfo.isShow=1;
                        mImvExRight.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.icon_ex_down));
                        mWrapContentListView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

}
