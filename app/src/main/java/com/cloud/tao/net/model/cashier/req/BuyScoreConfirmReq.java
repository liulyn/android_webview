package com.cloud.tao.net.model.cashier.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/27.
 */

public class BuyScoreConfirmReq extends BaseReq{

    public double money;
    public int pay_way;
    public int score_buy_switch;
    public String beizhu;
}
