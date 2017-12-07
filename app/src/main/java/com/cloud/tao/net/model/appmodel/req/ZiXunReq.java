package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BasePageReq;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class ZiXunReq extends BasePageReq {

    public String app_id = 0 +"" ; //(ID,默认为0，0代表全部)
    public int post_type = 0 ;//(资讯类型。默认为0，0代表全部，1代表动态，2代表攻略)
}
