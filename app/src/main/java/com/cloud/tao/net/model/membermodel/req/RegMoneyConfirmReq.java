package com.cloud.tao.net.model.membermodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/29.
 */

public class RegMoneyConfirmReq extends BaseReq{
    public int recharge_set_id;
    public double money;
    public int card_id;
    public String card_no;
    public int pay_way;
}
