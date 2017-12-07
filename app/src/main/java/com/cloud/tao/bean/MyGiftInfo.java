package com.cloud.tao.bean;

/**
 * author:janecer on 2016/9/15 09:40
 */


public class MyGiftInfo {

    public String title;//(礼包标题)
    public String id ;//(礼包ID，用户支付上传的礼包ID)
    public String update_time ;//（领取时间）
    public int type ;//（礼包类型：优惠礼包：1，充值y送x,2,折扣礼包,3立减礼包）


    //（礼包规则：优惠礼包规则举例：（500,200）代表充值500送200;
    // （0.8）代表打8折;(500,100)满500减100要跟礼包类型结合起来，比如
    // 当type=1，guize=500,200  代表的意思是改礼包为充值500送200。
    // 当type=2，guize=0.7  代表礼包为折扣礼包，折扣优惠7折。
    // 当type=3，guize=500,200 代表礼包为立减礼包，充值500减200）
    public String guize ;
}
