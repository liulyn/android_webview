package com.cloud.tao.bean.etc;

import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/10/17
 * des: 商品详情
 */
public class GoodsInfo{

    public String goods_id; //商品ID
    public String goods_name; //商品名称
    public String ordered_count; //商品销量
    public String original_price; //商品原价
    public String goods_price; //商品实际价格
    public String goods_description;//商品介绍
    public ArrayList<String> goods_picture_list; //商品缩略图
    public String stock_switch; //1：启用库存 2：不启用库存
    public String score_switch;
    public String goods_score;
    public String goods_amount; //商品库存
    public String attribute_type; //0：无属性规格 1：属性规格
    public List<List<GoodsDetailAttrInfo>> attr; //按等级把属性分组
    public List<GoodsDetailGoodsAttrInfo> goods_attr; //商品属性信息
    public String buyshow_switch; //0：不开启买家秀 1：开启买家秀
    public String is_display_amount; //0：不显示销量 1：显示销量
    public String has_activity; //0：没有限时活动 1：正在参与限时活动
    public List<GoodsActivityInfo> activity; //活动


}
