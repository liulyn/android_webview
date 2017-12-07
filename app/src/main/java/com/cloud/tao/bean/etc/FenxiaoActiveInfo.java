package com.cloud.tao.bean.etc;

/**
 * sunny created at 2016/9/26
 * des: 主页导航菜单 装载Bean
 */
public class FenxiaoActiveInfo {

    public static final String ACTIVE_FENXIAO_COMMISSION = "active_fenxiao_commission";
    public static final String ACTIVE_FENXIAO_TEAM = "active_fenxiao_team";
    public static final String ACTIVE_FENXIAO_ORDER= "active_fenxiao_order";
    public static final String ACTIVE_FENXIAO_INFO = "active_fenxiao_info";
    public static final String ACTIVE_FENXIAO_SHOP = "active_fenxiao_shop";
    public static final String ACTIVE_FENXIAO_SHOP_HOME="active_fenxiao_shop_home";
    public static final String ACTIVE_FENXIAO_MY_QR_CODE = "active_fenxiao_my_qr_code";
    public static final String ACTIVE_FENXIAO_PUBLIC_QR_CODE = "active_fenxiao_public_qr_code";

    public int activeIconId;
    public String activeTitle;
    public String activeType;

    public FenxiaoActiveInfo(int activeIconId, String activeTitle, String activeType) {
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
