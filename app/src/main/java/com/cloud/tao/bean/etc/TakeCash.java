package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/17.
 */

public class TakeCash {

    public String score_apply_poundage_percent;
    public String score_apply_limit;
    public String score_apply_max;
    public String score_exchange_ratio;

    @Override
    public String toString() {
        return "TakeCash{" +
                "score_apply_poundage_percent='" + score_apply_poundage_percent + '\'' +
                ", score_apply_limit='" + score_apply_limit + '\'' +
                ", score_apply_max='" + score_apply_max + '\'' +
                ", score_exchange_ratio='" + score_exchange_ratio + '\'' +
                '}';
    }
}
