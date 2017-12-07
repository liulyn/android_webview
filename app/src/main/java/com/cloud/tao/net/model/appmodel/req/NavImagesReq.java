package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * author:janecer on 2016/8/28 09:19
 *
 * 获取推荐，获取广告列别公用类
 */


public class NavImagesReq extends BaseReq {

    public int sum  ;//(返回的条数)



    public int type_id ;//(1为轮播图，2为广告) ;
}
