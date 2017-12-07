package com.cloud.tao.bean.etc;

import java.util.Arrays;

/**
 * Created by gezi-pc on 2016/10/18.
 */

public class Order {
    public String store_id;
    public String parent_order_id;
    public String total_price;
    public String actual_total_price;
    public String state;
    public String state_name;
    public String refund_state;
    public String refund_state_name;
    public String pay_way;
    public String pay_way_name;
    public String buyer_u_client_id;
    public String buyer_login_name;
    public String order_create_time;
    public String order_pay_time;
    public String order_end_time;
    public int show_action_btn;

    public OrderChild[] son;

    @Override
    public String toString() {
        return "Order{" +
                "store_id='" + store_id + '\'' +
                ", parent_order_id='" + parent_order_id + '\'' +
                ", total_price='" + total_price + '\'' +
                ", actual_total_price='" + actual_total_price + '\'' +
                ", state='" + state + '\'' +
                ", state_name='" + state_name + '\'' +
                ", refund_state='" + refund_state + '\'' +
                ", refund_state_name='" + refund_state_name + '\'' +
                ", pay_way='" + pay_way + '\'' +
                ", pay_way_name='" + pay_way_name + '\'' +
                ", buyer_u_client_id='" + buyer_u_client_id + '\'' +
                ", buyer_login_name='" + buyer_login_name + '\'' +
                ", order_create_time='" + order_create_time + '\'' +
                ", order_pay_time='" + order_pay_time + '\'' +
                ", order_end_time='" + order_end_time + '\'' +
                ", show_action_btn='" + show_action_btn + '\'' +
                ", son=" + Arrays.toString(son) +
                '}';
    }
}
