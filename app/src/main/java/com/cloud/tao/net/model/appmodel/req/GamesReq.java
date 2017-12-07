package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BasePageReq;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class GamesReq extends BasePageReq {

    public String where1 ;//（分类ID.默认为0代表全部分类）
    public int where5 = 1;//（排列顺序类型，默认为1。 1,最新时间2评分最高3人气最高）
    public int where7 = 1 ;//（平台类型 默认为1,0为全部，1为微网游，2为小）

}
