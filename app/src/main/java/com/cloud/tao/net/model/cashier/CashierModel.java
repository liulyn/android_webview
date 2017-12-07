package com.cloud.tao.net.model.cashier;

import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.base.BaseReq;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.cashier.req.BuyScoreConfirmReq;
import com.cloud.tao.net.model.cashier.req.GiveInventoryScoreConfirmReq;
import com.cloud.tao.net.model.cashier.req.GiveListReq;
import com.cloud.tao.net.model.cashier.req.SearchDetailReq;

/**
 * Created by gezi-pc on 2016/10/27.
 */

public class CashierModel extends VLModel {

    /**
     * 购买云豆信息获取接口
     * @param tag
     * @param callBack
     */
    public void buyScore(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag, UrlManager.URL_BUY_SCORE,req,callBack);

    }


    /**
     *购买明细信息获取接口
     * @param tag
     * @param l
     * @param p
     * @param start
     * @param end
     * @param callBack
     */
    public void inventoryList(String tag,int l,int p,String start,String end,EntityCallBack<String> callBack){
        SearchDetailReq req = new SearchDetailReq();
        req.l = l;
        req.p = p;
        req.start = start;
        req.end = end;
        MallOkHttpUtils.post(tag, UrlManager.URL_INVENTORY_LIST,req,callBack);

    }


    /**
     * 我要收银信息获取接口
     * @param tag
     * @param callBack
     */
    public void giveInventoryScore(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag, UrlManager.URL_GIVE_INVENTORY_SCORE,req,callBack);

    }


    /**
     * 我要收银--确认收银接口
     * @param tag
     * @param buyer_id
     * @param comment
     * @param callBack
     */
    public void giveInventoryScoreConfirm(String tag,int buyer_id,String comment,EntityCallBack<String> callBack){
        GiveInventoryScoreConfirmReq req = new GiveInventoryScoreConfirmReq();
        req.buyer_id = buyer_id;
        req.comment = comment;
        MallOkHttpUtils.post(tag, UrlManager.URL_GIVE_INVENTORY_SCORE_CONFIRM,req,callBack);

    }


    /**
     * 收银明细信息获取接口
     * @param tag
     * @param callBack
     */
    public void giveList(String tag,int l,int p,String name,EntityCallBack<String> callBack){
        GiveListReq req = new GiveListReq();
        req.name = name;
        req.l=l;
        req.p=p;
        MallOkHttpUtils.post(tag, UrlManager.URL_GIVE_LIST,req,callBack);

    }


    /**
     * 购买云豆支付接口
     * @param tag
     * @param money
     * @param pay_way
     * @param score_buy_switch
     * @param callBack
     */
    public void buyScoreConfirm(String tag,double money,int pay_way,int score_buy_switch,String beizhu,EntityCallBack<String> callBack){
        BuyScoreConfirmReq req = new BuyScoreConfirmReq();
        req.money = money;
        req.pay_way = pay_way;
        req.score_buy_switch = score_buy_switch;
        req.beizhu = beizhu;
        MallOkHttpUtils.post(tag, UrlManager.URL_BUY_SCORE_CONFIRM,req,callBack);

    }




}
