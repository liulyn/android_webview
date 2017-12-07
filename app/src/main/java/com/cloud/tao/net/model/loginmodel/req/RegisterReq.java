package com.cloud.tao.net.model.loginmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * sunny created at 2016/10/7
 * des: 注册接口参数
 */
public class RegisterReq extends BaseReq {
    public String sms_token;
    public String mobilephone;
    public String code;
    public String password;
    public String rem_code;
}
