package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * sunny created at 2016/9/24
 * des: 重新绑定手机号
 */
public class ResetMobileReq extends BaseReq {
    public String mobilephone;
    public String code;
}

