package com.cloud.tao.bean.etc;

/**
 * sunny created at 2016/9/26
 * des: 主页导航菜单 装载Bean
 */
public class MainActiveInfo {

    public static final String ACTIVE_ORDER = "active_main_order";
    public static final String ACTIVE_USER= "active_main_user";
    public static final String ACTIVE_DISTRIBUTION= "active_main_distribution";
    //public static final String ACTIVE_CHECKSTAND = "active_main_checkstand";
    public static final String ACTIVE_PAYSTAND = "active_main_paystand";

    public int activeIconId;
    public String activeTitle;
    public String activeType;

    public MainActiveInfo(int activeIconId, String activeTitle, String activeType) {
        this.activeIconId = activeIconId;
        this.activeTitle = activeTitle;
        this.activeType = activeType;
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

}
