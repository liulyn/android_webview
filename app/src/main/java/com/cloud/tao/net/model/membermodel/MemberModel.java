package com.cloud.tao.net.model.membermodel;

import com.cloud.tao.bean.etc.AddressObject;
import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.base.BaseDelReq;
import com.cloud.tao.net.base.BaseReq;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.req.BasePageReq;
import com.cloud.tao.net.model.membermodel.req.AddAccountReq;
import com.cloud.tao.net.model.membermodel.req.AddBankCardReq;
import com.cloud.tao.net.model.membermodel.req.AddressReq;
import com.cloud.tao.net.model.membermodel.req.BindCardnoReq;
import com.cloud.tao.net.model.membermodel.req.DelBankCardReq;
import com.cloud.tao.net.model.membermodel.req.MemberReq;
import com.cloud.tao.net.model.membermodel.req.RechargeCardReq;
import com.cloud.tao.net.model.membermodel.req.RegMoneyConfirmReq;
import com.cloud.tao.net.model.membermodel.req.RegmoneyListReq;
import com.cloud.tao.net.model.membermodel.req.ScoreApplyReq;
import com.cloud.tao.net.model.membermodel.req.VipCardPayReq;

/**
 * Created by gezi-pc on 2016/10/9.
 *
 * 会员相关的请求操作
 *
 */

public class MemberModel extends VLModel {



    /**
     * 编辑会员资料
     * @param tag
     */
    public void editMember(String tag,String name,String mobilePhone,short sex,String province ,String city,String district,Long birthday,String weixin,EntityCallBack<String> callBack) {
        MemberReq req = new MemberReq() ;
        req.private_name=name;
        req.private_mobilephone=mobilePhone;
        req.private_sex=sex;
        req.private_province=province;
        req.private_city=city;
        req.private_district  = district;
        req.private_birthday=birthday;
        req.private_weixin=weixin;

        MallOkHttpUtils.post(tag, UrlManager.URL_EDIT_MEMBER,req,callBack);
    }


    /**
     * 会员信息获取接口
     * @param tag
     * @param callBack
     */
    public void getClientInfo(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_CLIENT_INFO,req,callBack);
    }



    /**
     * 绑定实体卡号接口
     * @param tag
     * @param callBack
     */
    public void useRechargeCard(String tag,String cardno,EntityCallBack<String> callBack){
        BindCardnoReq req = new BindCardnoReq();
        req.cardno = cardno;
        MallOkHttpUtils.post(tag,UrlManager.URL_USE_RECHARGE_CARD,req,callBack);
    }


    /**
     * 绑定实体卡号接口
     * @param tag
     * @param callBack
     */
    public void bindCardno(String tag,String cardno,EntityCallBack<String> callBack){
        BindCardnoReq req = new BindCardnoReq();
        req.cardno = cardno;
        MallOkHttpUtils.post(tag,UrlManager.URL_BIND_CARDNO,req,callBack);
    }


    /**
     * 获取收货地址列表接口
     * @param tag
     * @param callBack
     */
    public void getAddressList(String tag,EntityCallBack<AddressObject> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_ADDRESS_LIST,req,callBack);
    }

    /**
     * 修改默认地址
     * @param tag
     * @param dev_id
     * @param callback
     */
    public void updateAddressDefault(String tag,String dev_id,EntityCallBack<String> callback ){
        AddressReq req = new AddressReq();
        req.dev_id = dev_id;
        MallOkHttpUtils.post(tag,UrlManager.URL_UPDATE_ADDRESS_DEFAULT,req,callback);


    }




    /**
     * 编辑收货地址
     * @param tag
     * @param province
     * @param callBack
     */
    public void editAddress(String tag,String dev_id, String province, String city, String area, String street, String consignee, String mobilephone, short isDefault, EntityCallBack<String> callBack){
        AddressReq req = new AddressReq();
        String reqUrl = UrlManager.URL_ADD_ADDRESS;
        if(dev_id!=null){
            req.dev_id = dev_id;
            reqUrl = UrlManager.URL_UPDATE_ADDRESS;
        }
        req.province = province;
        req.city = city;
        req.area = area;
        req.street = street;
        req.consignee = consignee;
        req.mobilephone = mobilephone;
        req.is_default = isDefault;
        MallOkHttpUtils.post(tag,reqUrl,req,callBack);
    }

    /**
     * 获取充值卡列表
     * @param tag
     * @param callback
     */
    public void getRechargeList(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_RECHARGE_LIST,req,callback);
    }


    /**
     * 添加充值卡
     * @param tag
     * @param type
     * @param cardNumber
     * @param callBack
     */
    public void addRechargeCard(String tag,int type,String cardNumber,EntityCallBack<String> callBack){
        RechargeCardReq req = new RechargeCardReq();
        req.id = type;
        req.card_num = cardNumber;
        MallOkHttpUtils.post(tag,UrlManager.URL_ADD_RECHARGE_CARD,req,callBack);

    }

    /**
     * 删除充值卡
     * @param tag
     * @param id
     * @param callBack
     */
    public void delRechargeCard(String tag,int id,EntityCallBack<String> callBack){

        RechargeCardReq req = new RechargeCardReq();
        req.id = id;
        MallOkHttpUtils.post(tag,UrlManager.URL_DEL_RECHARGE_CARD,req,callBack);
    }

    /**
     *获取我的会员卡信息
     */
    public void getVipCardCenter(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_VIP_CARD_CENTER,req,callBack);
    }

    /**
     *获取我的资料信息获取接口
     */
    public void getVipCardInfo(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_VIP_CARD_INFO,req,callBack);
    }

    /**
     *云豆提现信息获取接口
     */
    public void getScoreApply(String tag,EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_SCORE_APPLY,req,callBack);
    }


    /**
     * 云豆提现明细信息获取接口(分页)
     * @param tag
     * @param pageReq
     * @param callBack
     */
    public void getScoreApplyHistoryList(String tag, BasePageReq pageReq, EntityCallBack<String> callBack){
        MallOkHttpUtils.post(tag,UrlManager.URL_SCORE_APPLY_HISTORY,pageReq,callBack);
    }

    /**
     * 账户列表接口
     * @param tag
     * @param callBack
     */
    public void getAccountList(String tag, EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_ACCOUNT_LIST,req,callBack);
    }

    /**
     * 设置默认账户接口
     * @param tag
     * @param callBack
     */
    public void setAccountDefault(String tag, BaseDelReq req, EntityCallBack<String> callBack){
        MallOkHttpUtils.post(tag,UrlManager.URL_SET_ACCOUNT_DEFAULT,req,callBack);
    }

    /**
     * 添加账户接口
     * @param tag
     * @param receiver
     * @param type
     * @param account
     * @param opening_bank
     * @param is_default
     * @param callBack
     */
    public void addAccount(String tag, AddAccountReq req,  EntityCallBack<String> callBack){
        MallOkHttpUtils.post(tag,UrlManager.URL_ACCOUNT_ADD,req,callBack);
    }

    /**
     * 删除账户
     * @param tag
     * @param del_id
     * @param callBack
     */
    public void delAccount(String tag,String del_id,  EntityCallBack<String> callBack){
        BaseDelReq req = new BaseDelReq();
        req.id = del_id;
        MallOkHttpUtils.post(tag,UrlManager.URL_ACCONT_DEL,req,callBack);
    }


    /**
     * 云豆提现按钮接口
     * @param tag
     * @param tixian_commission
     * @param id
     * @param callBack
     */
    public void submitScoreApplyConfirm(String tag,String tixian_commission,String id,EntityCallBack<String> callBack){
        ScoreApplyReq req = new ScoreApplyReq();
        req.id = id;
        req.tixian_commission = tixian_commission;
        MallOkHttpUtils.post(tag,UrlManager.URL_SCORE_APPLY_CONFIRM,req,callBack);
    }


    /**
     *权益说明信息获取接口
     * @param tag
     * @param callBack
     */
    public void getVipCardExplain(String tag, EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_VIP_CARD_EXPLAIN,req,callBack);
    }

    /**
     * 云豆返还信息获取接口
     * @param tag
     * @param callBack
     */
    public void getScoreBack(String tag, EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_SCORE_BACK,req,callBack);
    }

    /**
     * 升级会员，会员等级信息获取接口
     * @param tag
     * @param callBack
     */
    public void getUpdateVipCardInfo(String tag, EntityCallBack<String> callBack){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_UPDATE_VIP_CARD_INFO,req,callBack);
    }











    /**
     * 获取购买金额列表
     * @param tag
     * @param callback
     */
    public void getPayPriceTagList(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_PAY_TAG_LIST,req,callback);
    }

    /**
     * 确认充值接口
     * @param tag
     * @param callback
     */
    public void regMoneyConfirm(String tag,int recharge_set_id,double money,int card_id,String card_no,int pay_way,EntityCallBack<String> callback){
        RegMoneyConfirmReq req = new RegMoneyConfirmReq();
        req.recharge_set_id = recharge_set_id;
        req.money = money;
        req.card_id = card_id;
        req.card_no = card_no;
        req.pay_way = pay_way;
        MallOkHttpUtils.post(tag,UrlManager.URL_REG_MONEY_CONFIRM,req,callback);
    }



    /**
     * 银行卡管理接口
     * @param tag
     * @param callback
     */
    public void getBankCard(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_BANK_CARD,req,callback);
    }


    /**
     * 删除银行卡接口
     * @param tag
     * @param client_transfer_card_id
     * @param callback
     */
    public void delBankCard(String tag,int client_transfer_card_id,EntityCallBack<String> callback){
        DelBankCardReq req = new DelBankCardReq();
        req.client_transfer_card_id = client_transfer_card_id;
        MallOkHttpUtils.post(tag,UrlManager.URL_DEL_BANK_CARD,req,callback);
    }

    /**
     * 添加银行卡接口
     * @param tag
     * @param callback
     */
    public void addBankCard(String tag,String transfer_card_receiver,String transfer_card_num,String transfer_card_bank,EntityCallBack<String> callback){
        AddBankCardReq req = new AddBankCardReq();
        req.transfer_card_receiver = transfer_card_receiver;
        req.transfer_card_num = transfer_card_num;
        req.transfer_card_bank = transfer_card_bank;
        MallOkHttpUtils.post(tag,UrlManager.URL_ADD_BANK_CARD,req,callback);
    }


    /**
     * 购买记录接口
     * @param tag
     * @param l
     * @param p
     * @param callback
     */
    public void regMoneyList(String tag,int l,int p,EntityCallBack<String> callback){
        RegmoneyListReq req = new RegmoneyListReq();
        req.l = l;
        req.p = p;
        MallOkHttpUtils.post(tag,UrlManager.URL_REG_MONEY_LIST,req,callback);
    }

    /**
     * 中将记录接口
     * @param tag
     * @param l
     * @param p
     * @param callback
     */
    public void winPrizeList(String tag,int l,int p,EntityCallBack<String> callback){
        RegmoneyListReq req = new RegmoneyListReq();
        req.l = l;
        req.p = p;
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_GUAGUALE_RECORD,req,callback);
    }

    /**
     * 获取云豆卡充值云豆历史记录接口
     * @param tag
     * @param l
     * @param p
     * @param callback
     */
    public void rechargeCardList(String tag,int l,int p,EntityCallBack<String> callback){
        RegmoneyListReq req = new RegmoneyListReq();
        req.l = l;
        req.p = p;
        MallOkHttpUtils.post(tag,UrlManager.URL_RECHARGE_CARD_LIST,req,callback);
    }


    /**
     * 购买会员等级（开通或升级会员）接口
     * @param tag
     * @param rank_id
     * @param client_type
     * @param pay_way
     * @param pay_num
     * @param callback
     */
    public void postVipCardPay(String tag,int rank_id,int client_type,int pay_way,double pay_num,EntityCallBack<String> callback){
        VipCardPayReq req = new VipCardPayReq();
        req.rank_id = rank_id;
        req.client_type = client_type;
        req.pay_way = pay_way;
        req.pay_num = pay_num;
        MallOkHttpUtils.post(tag,UrlManager.URL_VIP_CARD_PAY,req,callback);
    }


    /**
     * 开通会员卡 会员等级信息获取接口
     * @param tag
     * @param callback
     */
    public void openVipCardInfo(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_OPEN_VIP_CARD_INFO,req,callback);
    }

    /**
     * 直接开通会员卡接口
     * @param tag
     * @param callback
     */
    public void openVipCardNopay(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_OPEN_VIP_CARD_NOPAY,req,callback);
    }

    /**
     * 获取支付方式列表
     * @param tag
     * @param callback
     */
    public void getPayTypeList(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_PAY_TYPE,req,callback);
    }

    /**
     * 银行列表从接口
     * @param tag
     * @param callback
     */
    public void getBankList(String tag,EntityCallBack<String> callback){
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_BANK_LIST,req,callback);
    }
}
