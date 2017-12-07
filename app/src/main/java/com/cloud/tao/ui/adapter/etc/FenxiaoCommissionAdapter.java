package com.cloud.tao.ui.adapter.etc;

import android.content.Context;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailSubOrderInfo;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailsInfo;

import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业中心创业佣金 （已结算、待结算、冻结状态共用）
 */
public class FenxiaoCommissionAdapter extends BaseLoadMoreViewAdapter {

    Context mContext;

    public FenxiaoCommissionAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final FenxiaoCommissionDetailsInfo detailsInfo=(FenxiaoCommissionDetailsInfo)data;
        holder.setText(R.id.tv_commission_details_create_date,detailsInfo.create_time);
        holder.setText(R.id.tv_commission_details_create_order,"订单编号："+detailsInfo.order_id);
        CommissionDetailsSubOrderAdapter subOrderAdapter=parseData(detailsInfo.sub_order);
        holder.setWrapContentListViewAdapter(R.id.lv_commission_details_item_list,subOrderAdapter);
    }

    public CommissionDetailsSubOrderAdapter parseData(List<FenxiaoCommissionDetailSubOrderInfo> subOrderInfos){
        CommissionDetailsSubOrderAdapter subOrderAdapter=new CommissionDetailsSubOrderAdapter(mContext,subOrderInfos);
        return subOrderAdapter;
    }

}
