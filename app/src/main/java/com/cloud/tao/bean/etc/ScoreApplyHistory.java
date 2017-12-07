package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/17.
 */

public class ScoreApplyHistory {

    public String apply_score;
    public String apply_amount;
    public String state;
    public String apply_start_time;
    public String account;
    public String pay_way;
    public String receiver;

    @Override
    public String toString() {
        return "ScoreApplyHistory{" +
                "apply_score='" + apply_score + '\'' +
                ", apply_amount='" + apply_amount + '\'' +
                ", state='" + state + '\'' +
                ", apply_start_time='" + apply_start_time + '\'' +
                ", account='" + account + '\'' +
                ", pay_way='" + pay_way + '\'' +
                '}';
    }
}
