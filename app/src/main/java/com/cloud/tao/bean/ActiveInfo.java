package com.cloud.tao.bean;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class ActiveInfo {

    public int count ;//（总条数）
    public String title ;// (标题)
    public long createtime;// （创建时间）
    public String smeta;//（缩略图）
    public String app_id ;//(ID，0为平台单独活动) ,不为零多返回以下参数

    public String icon;//（图标）
    public String typename ;//（类型）
    public int gcount ;//（礼包个数）
    public int clicknum;//（玩家数量）
    public String name ;//（名称）

    public String id ;//（活动ID）

    public boolean isHaveGameInfo() {
        return (!"0".equals(app_id)) ;
    }
}
