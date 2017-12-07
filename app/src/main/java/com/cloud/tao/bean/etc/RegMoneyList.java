package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/29.
 */

public class RegMoneyList {
    public ArrayList<RegMoney> reg_money_list;

    public class RegMoney{
        public int recharge_order_id;
        public double money;
        public double poundage;
        public double total_money;
        public int score;
        public String state_info;
        public String operator_info;
        public Long create_time;

    }

}
