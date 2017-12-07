package com.cloud.tao.net.model.membermodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/18.
 */

public class AddAccountReq extends BaseReq{

    public String receiver;
    public String type;
    public String account;
    public String opening_bank;
    public String opening_bank_province;
    public String opening_bank_city;
    public String opening_bank_sub;
    public int is_default;

}
