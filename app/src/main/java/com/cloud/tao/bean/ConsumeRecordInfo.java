package com.cloud.tao.bean;

/**
 * author:janecer on 2016/8/22 16:39
 */


public class ConsumeRecordInfo {

    public String orderid ;//（订单号）
    public String mem_id;//（用户ID）
    public String app_id;//（消费ID）
    public int gold ;//（消费平台币数量）
    public float amount ;//（消费金额）
    public int status ;//（支付状态，1失败，2成功）
    public String productname;//（充值商品名称）
    public String username;//（用户账号）
    public String game;//（名称）
}
