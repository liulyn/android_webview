package com.cloud.tao.bean;

/**
 * author:janecer on 2016/8/22 16:31
 */


public class ChargeRecordInfo {

    public String orderid ;//（订单号）
    public int type_id ;//（类型 1 为充值平台币 2 为充值
    public String mem_id;//（用户ID）
    public String app_id;//（充值来源，0为首页充值）
    public float money ;//（购买金额）
    public int ptbcnt ;//（充值平台币数量）
    public float discount ;//（折扣）
    public String payway ;//（充值类型）
    public String paywayname;//（充值类型对应名称）
    public String ip ;//（用户充值时使用的网络终端IP）
    public String create_time;//（时间）
}
