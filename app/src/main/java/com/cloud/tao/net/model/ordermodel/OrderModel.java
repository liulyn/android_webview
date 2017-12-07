package com.cloud.tao.net.model.ordermodel;

import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.ordermodel.req.AddMessage;
import com.cloud.tao.net.model.ordermodel.req.ApplyRefundReq;
import com.cloud.tao.net.model.ordermodel.req.MyOrderReq;
import com.cloud.tao.net.model.ordermodel.req.OrderDetailReq;
import com.cloud.tao.net.model.ordermodel.req.OrderPayReq;
import com.cloud.tao.net.model.ordermodel.req.OrderReq;
import com.cloud.tao.net.model.ordermodel.req.ToOrderReq;

/**
 * Created by gezi-pc on 2016/10/9.
 */

public class OrderModel extends VLModel {

    /**
     * 我的订单接口
     * @param tag
     * @param p 页码
     * @param state 状态
     * @param callBack
     */
    public void getMyOrder(String tag,int p,String state,EntityCallBack<String> callBack){
        MyOrderReq req = new MyOrderReq();
        req.p = p;
        req.state = state;
        MallOkHttpUtils.post(tag, UrlManager.URL_MY_ORDER,req,callBack);

    }


    /**
     * 订单详情接口
     * @param tag
     * @param orderId
     * @param callBack
     */
    public void getOrderDetail(String tag,String orderId,EntityCallBack<String> callBack){
        OrderDetailReq req = new OrderDetailReq();
        req.parent_order_id = orderId;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_DETAIL,req,callBack);

    }
    /**
     * 取消订单接口
     * @param tag
     * @param orderId
     * @param callBack
     */
    public void updateOrderCancel(String tag,String orderId,EntityCallBack<String> callBack){
        OrderDetailReq req = new OrderDetailReq();
        req.parent_order_id = orderId;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_CANCEL,req,callBack);

    }
    /**
     * 确认收货接口
     * @param tag
     * @param orderId
     * @param callBack
     */
    public void updateOrderReceive(String tag,String orderId,EntityCallBack<String> callBack){
        OrderDetailReq req = new OrderDetailReq();
        req.parent_order_id = orderId;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_RECEIVE,req,callBack);

    }
    /**
     * 申请退款退货接口
     * @param tag
     * @param callBack
     */
    public void orderApplyRefund(String tag,String parent_order_id,String sub_order_id,String type,String refund_money,String logistics_company_name,String logistics_number,String message,EntityCallBack<String> callBack){
        ApplyRefundReq req = new ApplyRefundReq();
        req.parent_order_id = parent_order_id;
        req.sub_order_id = sub_order_id;
        req.type = type;
        req.refund_money = refund_money;
        req.logistics_company_name = logistics_company_name;
        req.logistics_number = logistics_number;
        req.message = message;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_APPLY_REFUND,req,callBack);
    }


    /**
     * 退款退货详情
     * @param tag
     * @param parent_order_id
     * @param sub_order_id
     * @param callBack
     */
    public void getOrderRefundDetail(String tag,String parent_order_id,String sub_order_id,EntityCallBack<String> callBack){
        ApplyRefundReq req = new ApplyRefundReq();
        req.parent_order_id = parent_order_id;
        req.sub_order_id = sub_order_id;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_REFUND_DETAIL,req,callBack);
    }

    /**
     * 退款退货详情的留言接口
     * @param tag
     * @param parent_order_id
     * @param sub_order_id
     * @param message
     * @param callBack
     */
    public void addOrderMessage(String tag,String parent_order_id,String sub_order_id,String message,EntityCallBack<String> callBack){
        AddMessage req = new AddMessage();
        req.parent_order_id = parent_order_id;
        req.sub_order_id = sub_order_id;
        req.message = message;
        MallOkHttpUtils.post(tag,UrlManager.URL_ADD_MESSAGE,req,callBack);
    }

    /**
     * 购物车【去结算】按钮接口
     * @param tag
     * @param setAccountJson
     * @param callBack
     */
    public void toSubmitOrder(String tag,String setAccountJson,EntityCallBack<String> callBack){
        ToOrderReq req = new ToOrderReq();
        req.setAccount = setAccountJson;
        MallOkHttpUtils.post(tag,UrlManager.URL_SET_ACCOUNT,req,callBack);
    }


    /**
     * 提交订单接口
     * @param tag
     * @param parentOrderJson
     * @param subOrderJson
     * @param callBack
     */
    public void addOrder(String tag,String orderToken,int store_id,int u_client_id,String parentOrderJson,String subOrderJson,EntityCallBack<String> callBack){
        OrderReq req = new OrderReq();
        req.order_token = orderToken;
        req.store_id = store_id;
        req.u_client_id = u_client_id;
        req.parent_order = parentOrderJson;
        req.sub_order = subOrderJson;
        MallOkHttpUtils.post(tag,UrlManager.URL_ADD_ORDER,req,callBack);
    }


    /**
     * 待支付订单请求支付接口
     * @param tag
     * @param order_id
     * @param pay_way
     * @param callBack
     */
    public void orderPay(String tag,String order_id,String pay_way,EntityCallBack<String> callBack){
        OrderPayReq req = new OrderPayReq();
        req.order_id = order_id;
        req.pay_way = pay_way;
        MallOkHttpUtils.post(tag,UrlManager.URL_ORDER_PAY,req,callBack);

    }


}
