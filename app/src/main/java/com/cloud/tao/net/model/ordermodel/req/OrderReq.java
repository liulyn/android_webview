package com.cloud.tao.net.model.ordermodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/26.
 */

public class OrderReq extends BaseReq {

    public String order_token;
    public int store_id;
    public int u_client_id;
    public String parent_order;
    public String sub_order;

}
