package com.cloud.tao.control;

import android.text.TextUtils;

import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.bean.etc.ShoppingCar;
import com.cloud.tao.bean.etc.ShoppingCarGoods;
import com.cloud.tao.net.model.membermodel.req.MemberReq;
import com.cloud.tao.util.SharePrefUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/9/10/06
 * des: 账号登录用户信息管理
 */

public class AccountInfo {

    private volatile static AccountInfo instance ;
    private LoginInfo mLoginInfo ;
    private Gson mGson ;


    private static final String KEY_LOGININFO = "key_logininfo" ;
    private static final String KEY_LASTLOGININFO = "key_lastLogininfo" ;

    private AccountInfo() {
        mGson = new Gson() ;
    }

    public static AccountInfo getInstance() {
        if(null == instance) {
            synchronized (AccountInfo.class) {
                if(null == instance) {
                    instance = new AccountInfo() ;
                }
            }
        }
        return instance ;
    }

    /**
     * 用户是否登录
     * @return
     */
    public boolean isUserLogin() {
        return null != getLoginInfo() ;
    }

    public LoginInfo getLoginInfo() {
       if(null != mLoginInfo) {
           return mLoginInfo ;
       }
       return null ;
    }

    public void setLoginInfo(LoginInfo loginInfo) {
        mLoginInfo = loginInfo;
        SharePrefUtil.saveString(MallApplication.instance(),KEY_LOGININFO,mGson.toJson(mLoginInfo));
        //SharePrefUtil.saveString(MallApplication.instance(),KEY_LASTLOGININFO,mGson.toJson(mLoginInfo));
    }

    /**
     * 获取上一次用户登陆信息
     * @return
     */
    public LoginInfo getLastLoginInfo() {
        String info = SharePrefUtil.getString(MallApplication.instance(),KEY_LASTLOGININFO,null) ;
        if (TextUtils.isEmpty(info)) {
            return null ;
        }
        return mGson.fromJson(info,LoginInfo.class) ;
    }

    /**
     * 保存用户本次登录的账号信息
     * @param loginMobilePhone
     * @param sessionId
     */
    public void saveAccountStatus(String loginTime,String loginToken,String loginuClientId,String loginMobilePhone,String sessionId,com.cloud.tao.bean.etc.AccountInfo info) {
        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_mobile_phone,loginMobilePhone);
        //SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_time,time); 已经提取到每次接口请求后保存，无需重复保存
        //SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_myStoreId,myStoreId);
        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_login_time,loginTime);
        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_login_token,loginToken);
        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_login_u_client_id,loginuClientId);

        SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_sessionId,sessionId);
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_user_info,info);
        saveMemberInfo(info.private_name,Short.parseShort(info.private_sex),info.private_mobilephone,info.private_province,info.private_city,info.private_district,null,null,info.private_headimgurl);
    }

    public void saveMemberInfo(String name,short sex,String mobilePhone,String province,String city,String district,Long birthday,String weixin,String private_headimgurl) {
        MemberReq member = new MemberReq();
        Object obj = SharePrefUtil.getObjectFromShare(MallApplication.instance(),SharePrefUtil.KEY.function_member_info);
        MemberReq spMr = null;
        if(obj!=null&& obj instanceof MemberReq){
            spMr = (MemberReq)obj;
        }else{
            spMr = new MemberReq();
        }
        if(!TextUtils.isEmpty(spMr.private_weixin)){
            member.private_weixin = spMr.private_weixin;
        }
        if(weixin!=null){
            member.private_weixin = weixin;
        }

        if(spMr.private_birthday!=null){
            member.private_birthday = spMr.private_birthday;
        }
        if(birthday!=null){
            member.private_birthday = birthday;
        }

        member.private_name = name;
        member.private_sex = sex;
        member.private_mobilephone = mobilePhone;
        member.private_province = province;
        member.private_city = city;
        member.private_district = district;
        if(!TextUtils.isEmpty(private_headimgurl)){
            member.private_headimgurl=private_headimgurl;
        }
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_member_info,member);
    }

    /**
     * 保存购物车的商品
     * @param goods_id
     * @param goods_name
     * @param goods_price
     * @param goods_count
     * @param goods_attr_id
     * @param attr_id_list
     * @param goods_attr_name
     * @param goods_picture
     * @param goods_description
     */
    public void saveShoppingCarGoods(int goods_id, String goods_name, Double goods_price, int goods_count, int goods_attr_id, String attr_id_list, String goods_attr_name, String goods_picture, String goods_description,ShoppingCarGoods.BuyState buy_state){

        Object obj = SharePrefUtil.getObjectFromShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods);
        ShoppingCar shoppingCar = null;
        if(obj!=null&&obj instanceof ShoppingCar){
            shoppingCar = (ShoppingCar)obj;
        }else{
            shoppingCar = new ShoppingCar();
        }

        ShoppingCarGoods goods = new ShoppingCarGoods();
        goods.goods_id = goods_id;
        goods.goods_name = goods_name;
        goods.goods_price = goods_price;
        goods.goods_count = goods_count;
        goods.goods_attr_id = goods_attr_id;
        goods.attr_id_list = attr_id_list;
        goods.goods_attr_name = goods_attr_name;
        goods.goods_description = goods_description;
        goods.goods_picture = goods_picture;
        goods.buy_state = buy_state;
        shoppingCar.goods.add(goods);
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods,shoppingCar);
    }

    /**
     * 获取购物车的商品
     * @return
     */

    public ShoppingCar getShoppingCar(){
        Object obj = SharePrefUtil.getObjectFromShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods);
        ShoppingCar shoppingCar = null;
        if(obj!=null&&obj instanceof ShoppingCar){
            shoppingCar = (ShoppingCar)obj;
        }
        return shoppingCar;
    }

    /**
     * 清空购物车
     */
    public void cleanShoppingCar(){
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods,null);
    }

    /**
     * 更新购物车
     */

    public void updateShoppingCar(ShoppingCar shoppingCar){
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods,shoppingCar);
    }

    /**
     * 删除购物车的商品
     * @return
     */

    public void removeGoodsByGoodsId(int goodsId){
        Object obj = SharePrefUtil.getObjectFromShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods);
        ShoppingCar shoppingCar = null;
        if(obj!=null&&obj instanceof ShoppingCar){
            shoppingCar = (ShoppingCar)obj;
        }
        List<ShoppingCarGoods> tempGoodss = new ArrayList<ShoppingCarGoods>();
        tempGoodss.addAll(shoppingCar.goods);
        for(ShoppingCarGoods goods:tempGoodss){
            if(goods.goods_id==goodsId){
                shoppingCar.goods.remove(goods);
            }
        }
        SharePrefUtil.setObjectToShare(MallApplication.instance(),SharePrefUtil.KEY.function_shopping_car_goods,shoppingCar);
    }


    public String getLastLoginMobilePhone() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_mobile_phone,null);
    }

   public String getLoginTime() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_time,null);
    }

    public String getLastLoginSessionId() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_sessionId,null);
    }

    public String getLastLoginTime() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_login_time,null);
    }
    public String getLastLoginToken() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_login_token,null);
    }
    public String getLastLoginClientId() {
        return SharePrefUtil.getString(MallApplication.instance(),SharePrefUtil.KEY.function_login_u_client_id,null);
    }

    /**
     * 释放登录信息
     */
    public void realseLoginInfo() {
        mLoginInfo = null ;
        SharePrefUtil.clear(MallApplication.instance());
    }
}
