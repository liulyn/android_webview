package com.cloud.tao.bean.etc;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/23.
 */

public class OrderDetailMessage {

    public OrderDetail parent_order;
    public GoodsDetail sub_order;
    public ArrayList<OrderMessage> message;

}
