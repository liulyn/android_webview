package com.cloud.tao.bean.etc;

/**
 * sunny created at 2016/9/26
 * des: 个人中心导航菜单 装载Bean
 */
public class MeActiveInfo {

    public static final String ACTIVE_STAY_PAY = "active_stay_pay";
    public static final String ACTIVE_GET_GOODS = "active_get_goods";
    public static final String ACTIVE_ALREADY_FINISHED = "active_already_finished";

    public int activeIconId;
    //public int bgColor;
    public String activeTitle;
    public String activeType;

    public MeActiveInfo(int activeIconId, String activeTitle, String activeType) {
        this.activeIconId = activeIconId;
        this.activeTitle = activeTitle;
        this.activeType = activeType;
        //this.bgColor = bgColor;
    }

    public int getActiveIconId() {
        return activeIconId;
    }

    public String getActiveTitle() {
        return activeTitle;
    }

    public String getActiveType() {
        return activeType;
    }

    /*public int getBgColor() {
        return bgColor;
    }*/
}
