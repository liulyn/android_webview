package com.cloud.tao.bean;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class ActiveCommentInfo {
    public String id;//(自动增长)
    public String post_id;//(活动ID)
    public String mem_id ;//（发表者评论ID）
    public String content ;//（评论内容）
    public long create_time ;//（发表时间）
    public String parentid ;//(回复的评论ID，默认为0)
    public String nickname ;//(评论者名称),
    public String head_img ;//(评论者头像)
    public String user_id ;//(评论者ID，0为游客)


}
