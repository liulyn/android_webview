package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.util.glide.GlideUtil;
import java.util.List;

/**
 * sunny created at 2016/9/28
 * des: 首页商品模块下 商品数据填充
 */
public class MainGoodsAdapter extends BaseAdapter {

    public List<GoodsInfo> goodsInfos;
    private Context mContext;
    private boolean isShowOrderCount;
    private OnGoodsItemClickListener mOnGoodsItemClickListener;

    public interface OnGoodsItemClickListener {
        void onGoodsItemClick(String goodsId);
    }

    public void setmOnGoodsItemClickListener(OnGoodsItemClickListener onGoodsItemClickListener){
        this.mOnGoodsItemClickListener=onGoodsItemClickListener;
    }

    public MainGoodsAdapter(Context context, List<GoodsInfo> goodsInfos,boolean isShowOrderCount) {
        this.mContext = context;
        this.goodsInfos = goodsInfos;
        this.isShowOrderCount=isShowOrderCount;
    }

    @Override
    public int getCount() {
        return goodsInfos==null?0:goodsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainGoodsViewHolder viewHolder;
        final GoodsInfo goodsInfo;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_etc_goods_item,null);
            viewHolder=new MainGoodsViewHolder((RelativeLayout)convertView.findViewById(R.id.rl_goods_item),
                    (ImageView) convertView.findViewById(R.id.tv_main_goods_icon),
                    (TextView) convertView.findViewById(R.id.tv_main_goods_name),
                    (TextView) convertView.findViewById(R.id.tv_main_goods_description),
                    (TextView) convertView.findViewById(R.id.tv_main_goods_price));
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (MainGoodsViewHolder) convertView.getTag();
        }
        goodsInfo=goodsInfos.get(position);
        if(null!=goodsInfo.goods_picture_list){
            GlideUtil.getInstance().loadImage(mContext,goodsInfo.goods_picture_list.get(0), viewHolder.ivGoodsIcon);
        }
        viewHolder.tvGoodsName.setText(goodsInfo.goods_name);
        if(isShowOrderCount){
            viewHolder.tvGoodsDescription.setText("销量 "+goodsInfo.ordered_count);
        }
        viewHolder.tvGoodsPrice.setText("￥ "+goodsInfo.goods_price);

        viewHolder.rlGoodsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnGoodsItemClickListener.onGoodsItemClick(goodsInfo.goods_id);
            }
        });
        return convertView;
    }

    public class MainGoodsViewHolder{

        RelativeLayout rlGoodsItem;
        ImageView ivGoodsIcon;
        TextView tvGoodsName;
        TextView tvGoodsDescription;
        TextView tvGoodsPrice;

        public MainGoodsViewHolder(RelativeLayout rlGoodsItem,ImageView ivGoodsIcon, TextView tvGoodsName,TextView tvGoodsDescription,TextView tvGoodsPrice) {
            super();
            this.rlGoodsItem=rlGoodsItem;
            this.ivGoodsIcon = ivGoodsIcon;
            this.tvGoodsName=tvGoodsName;
            this.tvGoodsDescription=tvGoodsDescription;
            this.tvGoodsPrice=tvGoodsPrice;
        }
    }

}
