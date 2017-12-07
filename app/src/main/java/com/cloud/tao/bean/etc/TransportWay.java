package com.cloud.tao.bean.etc;

/**
 * Created by gezi-pc on 2016/10/25.
 */

public class TransportWay {

    public String name;
    public Double price;
    public String type;

    public TransportWay(String name, double price,String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    public TransportWay() {
    }
}
