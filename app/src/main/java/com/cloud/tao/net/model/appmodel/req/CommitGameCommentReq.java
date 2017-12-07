package com.cloud.tao.net.model.appmodel.req;

import com.cloud.tao.net.base.BaseReq;

/**
 * Created by janecer on 2016/8/14 0014
 * des:
 */
public class CommitGameCommentReq extends BaseReq {

    public int type ;//type(1,评论，2，回复)

    public String content ;//（回复内容）
    public String app_id ;//（活动ID）
    public String mem_id ;//（被回复用户ID）
    public String parentid ;//（被回复评论ID） 回复必填
    public String to_mem_id ;//(发表回复人ID) 回复必填
}
