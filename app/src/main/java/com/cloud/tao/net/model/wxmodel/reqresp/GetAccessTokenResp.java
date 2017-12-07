package com.cloud.tao.net.model.wxmodel.reqresp;


import com.cloud.tao.net.model.wxmodel.BaseWxResp;

/**
 * author:janecer on 2016/6/12 16:32
 */

public class GetAccessTokenResp extends BaseWxResp {
    public String access_token ;//	接口调用凭证
    public String expires_in ;//	access_token接口调用凭证超时时间，单位（秒）
    public String refresh_token;//	用户刷新access_token
    public String openid	;//授权用户唯一标识
    public String scope;//	用户授权的作用域，使用逗号（,）分隔

    /* 刷新token不返回这个字段 */
    public String unionid;//	 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
}
