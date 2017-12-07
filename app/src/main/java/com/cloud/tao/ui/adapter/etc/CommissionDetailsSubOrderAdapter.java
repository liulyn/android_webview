package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailSubOrderInfo;
import com.cloud.tao.util.glide.GlideUtil;
import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业佣金明细商品
 */
public class CommissionDetailsSubOrderAdapter extends BaseAdapter {

    public List<FenxiaoCommissionDetailSubOrderInfo> subOrderInfos;
    private Context mContext;

    public CommissionDetailsSubOrderAdapter(Context context, List<FenxiaoCommissionDetailSubOrderInfo> subOrderInfos) {
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
        final FenxiaoCommissionDetailSubOrderInfo subOrderInfo;
        if(convertView==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.item_commission_details_sub_order,null);
            viewHolder=new MainGoodsViewHolder(
                    (ImageView) convertView.findViewById(R.id.tv_commission_details_icon),
                    (TextView) convertView.findViewById(R.id.tv_commission_details_name),
                    (TextView) convertView.findViewById(R.id.tv_commission_details_number),
                    (TextView) convertView.findViewById(R.id.tv_commission_details_price),
                    (TextView) convertView.findViewById(R.id.tv_commission_details_fenxiao_level),
                    (TextView) convertView.findViewById(R.id.tv_commission_details_fenxiao_name)
                    );
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (MainGoodsViewHolder) convertView.getTag();
        }
        subOrderInfo=subOrderInfos.get(position);
        if(null!=subOrderInfo.goods_cover){
            GlideUtil.getInstance().loadImage(mContext,subOrderInfo.goods_cover, viewHolder.ivDetailsIcon);
        }else{
            GlideUtil.getInstance().loadImage(mContext,R.mipmap.dangkr_no_picture_small,viewHolder.ivDetailsIcon);
        }
        viewHolder.tvDetailsName.setText(subOrderInfo.goods_name);
        viewHolder.tvDetailsName.setText(subOrderInfo.goods_name);
        viewHolder.tvDetailsNumber.setText("x"+subOrderInfo.goods_count);
        viewHolder.tvDetailsPrice.setText("￥"+subOrderInfo.self_reward);
        viewHolder.tvDetailsFenxiaoLevel.setText(subOrderInfo.level);
        viewHolder.tvDetailsFenxiaoName.setText(subOrderInfo.reseller_name);

        return convertView;
    }

    public class MainGoodsViewHolder{
        ImageView ivDetailsIcon;
        TextView tvDetailsName;
        TextView tvDetailsNumber;
        TextView tvDetailsPrice;
        TextView tvDetailsFenxiaoLevel;
        TextView tvDetailsFenxiaoName;

        public MainGoodsViewHolder(ImageView ivDetailsIcon, TextView tvDetailsName,TextView tvDetailsNumber,TextView tvDetailsPrice
                ,TextView tvDetailsFenxiaoLevel,TextView tvDetailsFenxiaoName) {
            super();
            this.ivDetailsIcon = ivDetailsIcon;
            this.tvDetailsName=tvDetailsName;
            this.tvDetailsNumber=tvDetailsNumber;
            this.tvDetailsPrice=tvDetailsPrice;
            this.tvDetailsFenxiaoLevel=tvDetailsFenxiaoLevel;
            this.tvDetailsFenxiaoName=tvDetailsFenxiaoName;
        }
    }

}
