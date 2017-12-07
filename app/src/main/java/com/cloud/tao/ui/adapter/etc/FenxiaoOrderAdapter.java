package com.cloud.tao.ui.adapter.etc;

import android.content.Context;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.FenxiaoSubOrderInfo;
import com.cloud.tao.bean.etc.FenxiaoSubOrderSonInfo;
import com.cloud.tao.util.DateUtil;

import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业订单
 */
public class FenxiaoOrderAdapter extends BaseLoadMoreViewAdapter {

    Context mContext;

    public FenxiaoOrderAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final FenxiaoSubOrderInfo orderInfo=(FenxiaoSubOrderInfo)data;
        holder.setText(R.id.tv_fenxiao_order_create_time, DateUtil.formatDateNormalStr(orderInfo.order_create_time,DateUtil.NORMAL_DATE_FORMAT));
        holder.setText(R.id.tv_fenxiao_order_parent_order_live,orderInfo.level+"订单：");
        holder.setText(R.id.tv_fenxiao_order_parent_order,orderInfo.parent_order_id);
        holder.setText(R.id.tv_fenxiao_order_total_reward,"+￥"+orderInfo.total_reward);
        holder.setText(R.id.tv_fenxiao_order_state,orderInfo.state_name);
        holder.setText(R.id.tv_fenxiao_order_fenxiao_levle,orderInfo.level+"创业");
        holder.setText(R.id.tv_fenxiao_order_fenxiao_name,orderInfo.fenxiao_name);

        FenxiaoSubOrderAdapter subOrderAdapter=parseData(orderInfo.son);
        holder.setWrapContentListViewAdapter(R.id.lv_fenxiao_order_list,subOrderAdapter);
    }

    public FenxiaoSubOrderAdapter parseData(List<FenxiaoSubOrderSonInfo> subOrderInfos){
        FenxiaoSubOrderAdapter subOrderAdapter=new FenxiaoSubOrderAdapter(mContext,subOrderInfos);
        return subOrderAdapter;
    }

}
