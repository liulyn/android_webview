package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;


public class MainGoodsActiveDataAdapter extends BaseLoadMoreViewAdapter {

    Context mContext;
    public IMainGoodsDataAdapterFace mIMainGoodsDataAdapterFace;

    public interface IMainGoodsDataAdapterFace{
        void ItemClick(String goodsId);
    }

    public void setIMainGoodsDataAdapterFace(IMainGoodsDataAdapterFace mIMainGoodsDataAdapterFace){
        this.mIMainGoodsDataAdapterFace=mIMainGoodsDataAdapterFace;
    }

    public MainGoodsActiveDataAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final GoodsInfo goodsInfo=(GoodsInfo)data;
        if (goodsInfo==null){ return ;}
        holder.setText(R.id.tv_cloud_active_goods_name,goodsInfo.goods_name);
        holder.setText(R.id.tv_cloud_active_goods_price,"￥"+goodsInfo.goods_price);

        ImageView mImageView=holder.getView(R.id.iv_cloud_active_goods_icon);
        GlideUtil.getInstance().loadImage(mContext,goodsInfo.goods_picture_list.get(0),mImageView);


        LinearLayout llMainGoodItem=holder.getView(R.id.ll_main_good_item);
        llMainGoodItem.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void noDoubleClick(View v) {
                mIMainGoodsDataAdapterFace.ItemClick(goodsInfo.goods_id);
            }
        });
    }

}
