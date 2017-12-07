package com.cloud.tao.net.model.membermodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by gezi-pc on 2016/10/9.
 * 添加收货地址
 */

public class AddressReq  extends BaseReq {

    public String dev_id;
    public String province;//省
    public String city;//市
    public String area;//区
    public String street;//详细地址
    public String consignee;//收货人
    public String mobilephone;//手机号
    public short is_default;

}
