package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.MyVipRecommendItemInfo;
import java.util.List;

public class MyVipRecommendAdapter extends BaseAdapter {
    Context mContext;

    private List<MyVipRecommendItemInfo> myVipRecommendItemInfo;

    public MyVipRecommendAdapter(Context mContext, List<MyVipRecommendItemInfo> myVipRecommendItemInfo) {
        this.mContext = mContext;
        this.myVipRecommendItemInfo = myVipRecommendItemInfo;
    }

    @Override
    public int getCount() {
        return myVipRecommendItemInfo == null ? 0 : myVipRecommendItemInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return myVipRecommendItemInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyVipRecommendAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_vip_recommend_item, parent, false);
            viewHolder = new MyVipRecommendAdapter.ViewHolder((TextView) convertView.findViewById(R.id.tv_vip_recommend_phone)
                    , (TextView) convertView.findViewById(R.id.tv_vip_recommend_price));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyVipRecommendAdapter.ViewHolder) convertView.getTag();
        }
        MyVipRecommendItemInfo mMyVipRecommendItemInfo = myVipRecommendItemInfo.get(position);
        viewHolder.recommendPhone.setText(mMyVipRecommendItemInfo.login_mobilephone);
        viewHolder.recommendPrice.setText("+￥" + mMyVipRecommendItemInfo.reward);
        return convertView;
    }

    public class ViewHolder {
        public TextView recommendPhone,recommendPrice;

        public ViewHolder(TextView recommendPhone,TextView recommendPrice) {
            this.recommendPhone = recommendPhone;
            this.recommendPrice = recommendPrice;
        }
    }

}
