package com.cloud.tao.callback;

/**
 * sunny created at 2016/10/20
 * des: 商品选择回调给Activity
 */
public interface GoodsAttrCallBack {

    /**
     * 立即购买
     */
    void toBuNow(String completeMsg);

    /**
     * 加入购物车
     */
    void toAddShoppingCart(String completeMsg);

}
