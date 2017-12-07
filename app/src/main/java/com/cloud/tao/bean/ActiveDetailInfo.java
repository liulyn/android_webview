package com.cloud.tao.bean;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class ActiveDetailInfo {

    public int id;//(活动ID)
    public String title;//（标题）
    public long createtime;//（发布时间）
    public String smeta;//（缩略图）
    public String content;//（活动内容）
    public String startime;//（开始时间）
    public String endtime;//（结束时间）
    public int renqi;//（人气）
    public int istop ;//（是否置顶）默认0，1为置顶
    public String  app_id;//(ID，0为平台单独活动) //不为零多返回以下参数：

    public String icon ;//（图标）
    public String typename ;//（类型）
    public int  gcount ;//（礼包个数）
    public int clicknum ;//（玩家数量）
    public String name ;//（名称）



    /**
     * 不为零多返回  信息
     * @return
     */
    public boolean isHaveGameInfo() {
        return !"0".equals(app_id) ;
    }

}
