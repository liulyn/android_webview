package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * sunny created at 2016/9/24
 * des: 修改密码
 */
public class ResetPwdReq extends BaseReq {
    public String mobilephone;
    public String code;
    public String password;
}

