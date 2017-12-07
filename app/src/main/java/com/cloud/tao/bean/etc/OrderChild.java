package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/18.
 */

public class OrderChild {

    public String parent_order_id;
    public String sub_order_id;
    public String actual_total_price;
    public String goods_id;
    public String goods_name;
    public String goods_price;
    public String goods_count;
    public String state;
    public String state_name;
    public String refund_state;
    public String refund_state_name;
    public String goods_picture;
    public String goods_attr_str;

    @Override
    public String toString() {
        return "OrderChild{" +
                "parent_order_id='" + parent_order_id + '\'' +
                ", sub_order_id='" + sub_order_id + '\'' +
                ", actual_total_price='" + actual_total_price + '\'' +
                ", goods_id='" + goods_id + '\'' +
                ", goods_name='" + goods_name + '\'' +
                ", goods_price='" + goods_price + '\'' +
                ", goods_count='" + goods_count + '\'' +
                ", state='" + state + '\'' +
                ", state_name='" + state_name + '\'' +
                ", refund_state='" + refund_state + '\'' +
                ", refund_state_name='" + refund_state_name + '\'' +
                ", goods_picture='" + goods_picture + '\'' +
                ", goods_attr_str='" + goods_attr_str + '\'' +
                '}';
    }
}
