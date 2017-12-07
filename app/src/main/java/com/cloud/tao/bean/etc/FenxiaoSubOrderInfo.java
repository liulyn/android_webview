package com.cloud.tao.bean.etc;

import java.util.List;

/**
 * sunny created at 2016/10/27
 * des: 创业订单 fenxiao_order_list节点
 */
public class FenxiaoSubOrderInfo {

    public String actual_total_price;
    public String buyer_login_name;
    public String buyer_nick_name;
    public String buyer_u_client_id;
    public String fenxiao_level_2_u_client_id;
    public String fenxiao_level_3_u_client_id;
    public String fenxiao_name;
    public String fenxiao_u_client_id;
    public String level;
    public String order_create_time;
    public String order_end_time;
    public String order_pay_time;
    public String parent_order_id;
    public String pay_way;
    public String pay_way_name;
    public String refund_state;
    public String reward_level_1;
    public String reward_level_2;
    public String reward_level_3;
    public String state;
    public String state_name;
    public String store_id;
    public int success;
    public String total_price;
    public String total_reward;
    public String transfer_state;
    public List<FenxiaoSubOrderSonInfo> son;

}
