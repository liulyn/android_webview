package com.cloud.tao.net.model.ordermodel.req;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/26.
 */

public class ParentOrder {

    public int discount_type;
    public double discount_money;
    public int score_buy_switch;
    public double score_buy_money;
    //public int score_buy_score;
    public double post;
    public int freight_type;
    public double actual_total_price;
    public int pay_way;
    public String receiver_name;
    public String receiver_mobilephone;
    public String receiver_province;
    public String receiver_city;
    public String receiver_district;
    public String receiver_address;
    public String buyer_leave_message;
    public ArrayList<OrderRechargeCard> label_beizhu = new ArrayList<OrderRechargeCard>();


}
