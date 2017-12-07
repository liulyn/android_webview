package com.cloud.tao.net.model.loginmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * sunny created at 2016/10/13
 * des: 忘记密码接口参数
 */
public class ForGetPwdReq extends BaseReq {
    public String sms_token;
    public String mobilephone;
    public String code;
    public String password;
}
