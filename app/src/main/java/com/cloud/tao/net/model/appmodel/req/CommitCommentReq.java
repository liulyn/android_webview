package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class CommitCommentReq extends BaseReq {

    //    评论必填参数：content（评论内容）post_id（活动ID）mem_id（发表评论用户ID）
    //    回复必填参数：content（回复内容）post_id（活动ID）mem_id（被回复用户ID）parentid（被回复评论ID）to_mem_id(发表回复人ID)

    public int type ;//type(1,评论，2，回复)

    public String content ;//（回复内容）
    public String post_id ;//（活动ID）
    public String mem_id ;//（被回复用户ID）
    public String parentid ;//（被回复评论ID）
    public String to_mem_id ;//(发表回复人ID)

}
