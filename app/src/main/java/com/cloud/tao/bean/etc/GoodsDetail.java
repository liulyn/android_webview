package com.cloud.tao.bean.etc;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gezi-pc on 2016/10/21.
 */

public class GoodsDetail implements Parcelable {

    public String parent_order_id;
    public String sub_order_id;
    public String actual_total_price;
    public String goods_id;
    public String goods_name;
    public String goods_price;
    public String goods_count;
    public String state;
    public String state_name;
    public int refund_state;
    public String refund_state_name;
    public String goods_picture;
    public String goods_attr_str;
    public String logistics_company_id;
    public String logistics_company_name;
    public String logistics_number;
    public String join_store_id;
    public String allow_refund_time;    //新增可退款时间


    public GoodsDetail(Parcel in) {
        parent_order_id = in.readString();
        sub_order_id = in.readString();
        actual_total_price = in.readString();
        goods_id = in.readString();
        goods_name = in.readString();
        goods_price = in.readString();
        goods_count = in.readString();
        state = in.readString();
        state_name = in.readString();
        refund_state = in.readInt();
        refund_state_name = in.readString();
        goods_picture = in.readString();
        goods_attr_str = in.readString();
        logistics_company_id = in.readString();
        logistics_company_name = in.readString();
        logistics_number = in.readString();
        join_store_id = in.readString();
        allow_refund_time=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parent_order_id);
        dest.writeString(sub_order_id);
        dest.writeString(actual_total_price);
        dest.writeString(goods_id);
        dest.writeString(goods_name);
        dest.writeString(goods_price);
        dest.writeString(goods_count);
        dest.writeString(state);
        dest.writeString(state_name);
        dest.writeString(refund_state_name);
        dest.writeString(goods_picture);
        dest.writeString(goods_attr_str);
        dest.writeString(logistics_company_id);
        dest.writeString(logistics_company_name);
        dest.writeString(logistics_number);
        dest.writeString(join_store_id);

        dest.writeInt(refund_state);
        dest.writeString(allow_refund_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoodsDetail> CREATOR = new Creator<GoodsDetail>() {
        @Override
        public GoodsDetail createFromParcel(Parcel in) {
            return new GoodsDetail(in);
        }

        @Override
        public GoodsDetail[] newArray(int size) {
            return new GoodsDetail[size];
        }
    };



}
