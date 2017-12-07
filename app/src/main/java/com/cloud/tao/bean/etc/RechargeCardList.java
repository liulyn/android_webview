package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/29.
 */

public class RechargeCardList {
    public ArrayList<RechargeCard> recharge_card_list;

    public class RechargeCard{
        public String money;
        public String used_time;
        public String cardno;

    }

}
