package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BasePageReq;

/**
 * Created by janecer on 2016/8/14 0014
 * des:获取用户连续登录天数
 */
public class LoginGameDaysReq extends BasePageReq {

    public String app_id ; //(ID，默认0为全部)
    public String id ; //（礼包ID，默认0为全部）
    public String token ;//(用户ID) ;
}
