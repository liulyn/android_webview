package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/25.
 */

public class SubmitOrder {

    public Double actual_total_price;
    public int actual_total_score;
    public double discount_money;
    public int is_label;
    public String order_token;
    public int store_id;
    public int u_client_id;
    public AddressList.Address address;
    public SoClientScore client_score;
    public SoFenxiaoCiscount fenxiao_discount;
    public SoPostage postage;
    public ArrayList<SoSetAccount> setAccount;
    public SoScore score;
    public ArrayList<RechargeCardOrder> label;

}
