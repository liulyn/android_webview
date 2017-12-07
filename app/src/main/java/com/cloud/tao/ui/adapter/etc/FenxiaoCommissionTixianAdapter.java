package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseLoadMoreViewAdapter;
import com.cloud.tao.base.BaseRecyViewHolder;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailsTixianInfo;
import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业中心创业佣金 提现
 */
public class FenxiaoCommissionTixianAdapter extends BaseLoadMoreViewAdapter {

    Context mContext;

    public FenxiaoCommissionTixianAdapter(Context context, int itemLayout, List list) {
        super(context, itemLayout, list);
        this.mContext=context;
    }

    @Override
    public void onBindViewHolder(BaseRecyViewHolder holder, Object data, int position) {
        final FenxiaoCommissionDetailsTixianInfo detailsInfo=(FenxiaoCommissionDetailsTixianInfo)data;
        holder.setText(R.id.tv_commission_details_tixian_create_date,detailsInfo.apply_start_time);
        holder.setText(R.id.tv_commission_details_tixian_name,"收款人："+detailsInfo.account);
        holder.setText(R.id.tv_commission_details_tixian_price,"￥"+detailsInfo.apply_amount);
        holder.setText(R.id.tv_commission_details_tixian_type,"账户类型："+detailsInfo.type_name);
        holder.setText(R.id.tv_commission_details_tixian_state,detailsInfo.state);
    }

}
