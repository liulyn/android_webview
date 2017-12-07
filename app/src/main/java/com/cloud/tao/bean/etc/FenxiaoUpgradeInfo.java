package com.cloud.tao.bean.etc;

/**
 * sunny created at 2016/10/24
 * des: 创业中心 fenxiao_upgrade节点
 */
public class FenxiaoUpgradeInfo {

    public int frozen_money;
    public LevelInfoBean next_level_info;
    public LevelInfoBean cur_level_info;

    public static class LevelInfoBean {
        public String level_discount;
        public String level_id;
        public String level_name;
        public String level_price;
        public String reward_level_1;
        public String reward_level_2;
    }
}
