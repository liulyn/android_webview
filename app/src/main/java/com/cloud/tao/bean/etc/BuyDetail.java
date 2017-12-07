package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/27.
 */

public class BuyDetail {
    public int score_inventory;
    public int score_to_exchange;
    public int remain_score;
    public ArrayList<BuyDetailList> inventory;

    public class BuyDetailList{
        public double total_price;
        public double actual_total_price;
        public String buyer_login_name;
        public long order_create_time;
        public String State;

    }

    @Override
    public String toString() {
        return "BuyDetail{" +
                "score_inventory=" + score_inventory +
                ", score_to_exchange=" + score_to_exchange +
                ", remain_score=" + remain_score +
                ", inventory=" + inventory +
                '}';
    }
}
