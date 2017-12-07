package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/17.
 */

public class VipCardInfo {

    public String score;
    public String score_to_exchange;
    public String rank_name;
    public String remain_score;
    public double balance;
    public String member_card_num;
    public String name;
    public String id;
    public String login_mobilephone;

    @Override
    public String toString() {
        return "VipCardInfo{" +
                "score=" + score +
                ", score_to_exchange=" + score_to_exchange +
                ", rank_name=" + rank_name +
                ", remain_score=" + remain_score +
                ", balance=" + balance +
                ", member_card_num='" + member_card_num + '\'' +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", login_mobilephone='" + login_mobilephone + '\'' +
                '}';
    }
}
