package com.cloud.tao.bean.etc;

import java.io.Serializable;

/**
 * Created by gezi-pc on 2016/10/16.
 */

public class VipCard implements Serializable{


    public  String rank_name;
    public  String score_to_exchange;
    public String commission_total;
    public String commission_confirmed;
    public  double balance;
    public  String member_card_num;
    public  int remain_score;
    public  int show_score_back;
    public  int is_bind_cardno;

    @Override
    public String toString() {
        return "VipCard{" +
                "rank_name='" + rank_name + '\'' +
                ", score_to_exchange='" + score_to_exchange + '\'' +
                ", commission_total=" + commission_total +
                ", commission_confirmed=" + commission_confirmed +
                ", balance=" + balance +
                ", member_card_num='" + member_card_num + '\'' +
                ", remain_score=" + remain_score +
                ", show_score_back=" + show_score_back +
                ", is_bind_cardno=" + is_bind_cardno +
                '}';
    }
}
