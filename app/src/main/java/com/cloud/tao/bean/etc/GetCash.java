package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/30.
 */

public class GetCash {
    public FenxiaoInfo fenxiao_info;
    public Account account_info;
    public double fenxiao_tixian_min_amount;
    public double fenxiao_tixian_max_amount;

    @Override
    public String toString() {
        return "GetCash{" +
                "fenxiao_info=" + fenxiao_info +
                ", account_info=" + account_info +
                ", fenxiao_tixian_min_amount=" + fenxiao_tixian_min_amount +
                ", fenxiao_tixian_max_amount=" + fenxiao_tixian_max_amount +
                '}';
    }
}
