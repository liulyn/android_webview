package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoMyTeamInfo;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

public class FenxiaoMyTeamAdapter extends BaseAdapter {
    Context mContext;

    private List<FenxiaoMyTeamInfo> fenxiaoMyTeamInfos;

    public FenxiaoMyTeamAdapter(Context mContext, List<FenxiaoMyTeamInfo> fenxiaoMyTeamInfos) {
        this.mContext = mContext;
        this.fenxiaoMyTeamInfos = fenxiaoMyTeamInfos;
    }

    @Override
    public int getCount() {
        return fenxiaoMyTeamInfos == null ? 0 : fenxiaoMyTeamInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return fenxiaoMyTeamInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FenxiaoMyTeamAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_fenxiao_my_team, parent, false);
            viewHolder = new FenxiaoMyTeamAdapter.ViewHolder((ImageView) convertView.findViewById(R.id.tv_my_team_icon)
                    , (TextView) convertView.findViewById(R.id.tv_my_team_phone)
                    , (TextView) convertView.findViewById(R.id.tv_my_team_rank_name)
                    , (TextView) convertView.findViewById(R.id.tv_my_team_price));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FenxiaoMyTeamAdapter.ViewHolder) convertView.getTag();
        }
        FenxiaoMyTeamInfo mFenxiaoMyTeamInfo = fenxiaoMyTeamInfos.get(position);
        if (!TextUtils.isEmpty(mFenxiaoMyTeamInfo.private_headimgurl)) {
            GlideUtil.getInstance().loadImage(mContext, mFenxiaoMyTeamInfo.private_headimgurl, viewHolder.imageView);
        } else {
            GlideUtil.getInstance().loadImage(mContext, R.mipmap.ic_fenxiao_team_default, viewHolder.imageView);
        }
        viewHolder.teamPhone.setText(mFenxiaoMyTeamInfo.login_mobilephone);
        viewHolder.rank_name.setText(mFenxiaoMyTeamInfo.rank_name);
        viewHolder.teamPrice.setText("+￥" + mFenxiaoMyTeamInfo.commission_total);
        return convertView;
    }

    public class ViewHolder {
        public ImageView imageView;
        public TextView teamPhone, rank_name, teamPrice;

        public ViewHolder(ImageView imageView, TextView teamPhone, TextView rank_name, TextView teamPrice) {
            this.imageView = imageView;
            this.teamPhone = teamPhone;
            this.rank_name = rank_name;
            this.teamPrice = teamPrice;
        }
    }

    /*public FenxiaoMyTeamAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final FenxiaoMyTeamInfo fenxiaoMyTeamInfo =(FenxiaoMyTeamInfo)data;

        if(!TextUtils.isEmpty(fenxiaoMyTeamInfo.private_headimgurl)){
            GlideUtil.getInstance().loadImage(mContext, fenxiaoMyTeamInfo.private_headimgurl,(ImageView) holder.getView(R.id.tv_my_team_icon));
        }else{
            GlideUtil.getInstance().loadImage(mContext,R.mipmap.ic_fenxiao_team_default, (ImageView) holder.getView(R.id.tv_my_team_icon));
        }
        holder.setText(R.id.tv_my_team_phone, fenxiaoMyTeamInfo.login_mobilephone) ;
        holder.setText(R.id.tv_my_team_rank_name, fenxiaoMyTeamInfo.nick_name) ;
        holder.setText(R.id.tv_my_team_num, fenxiaoMyTeamInfo.total_son+"个成员") ;
        holder.setText(R.id.tv_my_team_price,"+"+ fenxiaoMyTeamInfo.commission_total) ;
    }*/

}
