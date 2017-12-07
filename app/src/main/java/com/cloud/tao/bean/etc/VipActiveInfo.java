package com.cloud.tao.bean.etc;

public class VipActiveInfo {

    public static final String ACTIVE_RECORD= "active_record";
    public static final String ACTIVE_GROW= "active_grow";
    public static final String ACTIVE_RECOMMEND= "active_recommend";
    public static final String ACTIVE_EQUITY = "active_equity";

    public int activeIconId;
    public String activeTitle;
    public String activeType;

    public VipActiveInfo(int activeIconId, String activeTitle, String activeType) {
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
