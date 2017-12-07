package com.cloud.tao.net.model.ordermodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/23.
 */

public class AddMessage extends BaseReq{

    public String parent_order_id;
    public String sub_order_id;
    public String message;
}
