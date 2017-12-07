package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/20.
 */
public class OrderDetail{

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
    public String order_auto_end_time;
    public Long allow_refund_time;
    public String delivery_time;
    public String receiver_time;
    public String freight_money;
    public String receiver_name;
    public String receiver_mobilephone;
    public String receiver_address;
    public String receiver_province;
    public String receiver_district;
    public String receiver_city;
    public String store_mobilephone;
    public String store_name;

    @Override
    public String toString() {
        return "OrderDetail{" +
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
                ", show_action_btn=" + show_action_btn +
                ", order_auto_end_time='" + order_auto_end_time + '\'' +
                ", allow_refund_time='" + allow_refund_time + '\'' +
                ", delivery_time='" + delivery_time + '\'' +
                ", receiver_time='" + receiver_time + '\'' +
                ", freight_money='" + freight_money + '\'' +
                ", receiver_name='" + receiver_name + '\'' +
                ", receiver_mobilephone='" + receiver_mobilephone + '\'' +
                ", receiver_address='" + receiver_address + '\'' +
                ", receiver_province='" + receiver_province + '\'' +
                ", receiver_district='" + receiver_district + '\'' +
                ", receiver_city='" + receiver_city + '\'' +
                ", store_mobilephone='" + store_mobilephone + '\'' +
                '}';
    }
}
