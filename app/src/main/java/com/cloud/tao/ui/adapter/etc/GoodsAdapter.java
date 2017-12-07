package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

/**
 * sunny created at 2016/10/17
 * des: 商品列表页
 */
public class GoodsAdapter extends BaseLoadMoreViewAdapter {

    Context mContext;

    public GoodsAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    private OnGoodsListListener mOnGoodsListListener;

    public interface OnGoodsListListener{
        void onGoodsItemClick(String goodsId);
    }

    public void setOnGoodsListListener(OnGoodsListListener onGoodsListListener){
        this.mOnGoodsListListener=onGoodsListListener;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final GoodsInfo goodsInfo=(GoodsInfo)data;
        if(null!=goodsInfo.goods_picture_list&&goodsInfo.goods_picture_list.size()>0){
            GlideUtil.getInstance().loadImage(mContext,goodsInfo.goods_picture_list.get(0), (ImageView) holder.getView(R.id.tv_main_goods_icon));
        }else{
            GlideUtil.getInstance().loadImage(mContext,R.mipmap.dangkr_no_picture_small, (ImageView) holder.getView(R.id.tv_main_goods_icon));
        }
        holder.setText(R.id.tv_main_goods_name,goodsInfo.goods_name==null?"":goodsInfo.goods_name) ;
        if(!TextUtils.isEmpty(goodsInfo.is_display_amount)){
            if("1".equals(goodsInfo.is_display_amount)){
                holder.setText(R.id.tv_main_goods_description,"销量 "+goodsInfo.ordered_count) ;
            }
        }
        holder.setText(R.id.tv_main_goods_price,goodsInfo.goods_price==null?"":"￥ "+goodsInfo.goods_price) ;
        holder.getView(R.id.rl_goods_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnGoodsListListener.onGoodsItemClick(goodsInfo.goods_id);
            }
        });


    }


}
