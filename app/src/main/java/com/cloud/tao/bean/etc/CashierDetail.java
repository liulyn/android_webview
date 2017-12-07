package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/27.
 */

public class CashierDetail {
    public int score_inventory;
    public int score_to_exchange;
    public int remain_score;
    public ArrayList<CashierDetailList> give_order;

    public class CashierDetailList{
        public int giver_u_client_id;
        public int receiver_u_client_id;
        public String giver_name;
        public String receiver_name;
        public int inventory_score;
        public String beizhu;

    }

    @Override
    public String toString() {
        return "CashierDetail{" +
                "score_inventory=" + score_inventory +
                ", score_to_exchange=" + score_to_exchange +
                ", remain_score=" + remain_score +
                ", give_order=" + give_order +
                '}';
    }
}
