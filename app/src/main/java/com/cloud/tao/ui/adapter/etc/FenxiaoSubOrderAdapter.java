package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoSubOrderSonInfo;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业订单
 */
public class FenxiaoSubOrderAdapter extends BaseAdapter {

    public List<FenxiaoSubOrderSonInfo> subOrderInfos;
    private Context mContext;

    public FenxiaoSubOrderAdapter(Context context, List<FenxiaoSubOrderSonInfo> subOrderInfos) {
        this.mContext = context;
        this.subOrderInfos = subOrderInfos;
    }

    @Override
    public int getCount() {
        return subOrderInfos==null?0:subOrderInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return subOrderInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MainGoodsViewHolder viewHolder;
        final FenxiaoSubOrderSonInfo subOrderInfo;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_fenxiao_order_sub_order,null);
            viewHolder=new MainGoodsViewHolder(
                    (ImageView) convertView.findViewById(R.id.tv_order_details_icon),
                    (TextView) convertView.findViewById(R.id.tv_order_details_name),
                    (TextView) convertView.findViewById(R.id.tv_order_details_number),
                    (TextView) convertView.findViewById(R.id.tv_order_details_price));
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (MainGoodsViewHolder) convertView.getTag();
        }
        subOrderInfo=subOrderInfos.get(position);
       if(null!=subOrderInfo.goods_picture){
            GlideUtil.getInstance().loadImage(mContext,subOrderInfo.goods_picture, viewHolder.ivDetailsIcon);
        }else{
            GlideUtil.getInstance().loadImage(mContext,R.mipmap.dangkr_no_picture_small,viewHolder.ivDetailsIcon);
        }
        viewHolder.tvDetailsName.setText(subOrderInfo.goods_name);
        viewHolder.tvDetailsNumber.setText("x"+subOrderInfo.goods_count);
        viewHolder.tvDetailsPrice.setText("￥"+subOrderInfo.goods_price);

        return convertView;
    }

    public class MainGoodsViewHolder{
        ImageView ivDetailsIcon;
        TextView tvDetailsName;
        TextView tvDetailsNumber;
        TextView tvDetailsPrice;

        public MainGoodsViewHolder(ImageView ivDetailsIcon, TextView tvDetailsName,TextView tvDetailsNumber,TextView tvDetailsPrice) {
            super();
            this.ivDetailsIcon = ivDetailsIcon;
            this.tvDetailsName=tvDetailsName;
            this.tvDetailsNumber=tvDetailsNumber;
            this.tvDetailsPrice=tvDetailsPrice;
        }
    }

}
