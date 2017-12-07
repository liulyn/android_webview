package com.cloud.tao.bean.etc;

import java.util.Date;

/**
 * Created by gezi-pc on 2016/10/3.
 */

public class IntegralHistory {

    public String username;
    public String number;
    public float integralCount;
    public float integralCash;
    public Date date;
    public int status;

    public IntegralHistory(String username, String number, float integralCount, float integralCash, Date date, int status) {
        this.username = username;
        this.number = number;
        this.integralCount = integralCount;
        this.integralCash = integralCash;
        this.date = date;
        this.status = status;
    }

    public IntegralHistory() {
    }
}
