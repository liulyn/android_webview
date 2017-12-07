package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/18.
 */

public class PayWay {

    public static ArrayList<PayWay>  payways = new ArrayList<>();
    public static ArrayList<PayWay>  accountPayways = new ArrayList<>();



    public int id;
    public String name;
    public String type;

    public PayWay() {
    }

    public PayWay(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public enum TypeWay{
        ALIPAY,
        TENPAY,
        BANK_CARD,
        WE_CHAT
    }

    static {
        accountPayways.add(new PayWay(1,"支付宝",TypeWay.ALIPAY.name()));
        accountPayways.add(new PayWay(2,"财付通",TypeWay.TENPAY.name()));
        accountPayways.add(new PayWay(3,"银行卡",TypeWay.BANK_CARD.name()));
        accountPayways.add(new PayWay(4,"微信号",TypeWay.WE_CHAT.name()));

        //payways.add(new PayWay(64,"银行卡",TypeWay.ALIPAY.name()));
        //payways.add(new PayWay(128,"微信扫码支付",TypeWay.TENPAY.name()));
        payways.add(new PayWay(256,"LePay融合支付",TypeWay.TENPAY.name()));
    }
}
