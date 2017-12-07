package com.cloud.tao.net.model.loginmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * sunny created at 2016/9/10/09
 * des: 验证码接口参数
 */

public class VerificationCodeReq extends BaseReq {

    public String mobilephone;//（手机号码）
    public String token;//（手机号码+秘钥 签名）
}
