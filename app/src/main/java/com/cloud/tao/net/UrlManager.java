package com.cloud.tao.net;

import com.cloud.tao.BuildConfig;

public class UrlManager {

    public static final String URL_RECOMMAND_GAME ="";
    public static final String URL_GAME_RECORD_GET ="";
    public static final String URL_ACTIVES ="";
    public static final String URL_GAME_DETAIL_BY_ID ="";
    public static final String URL_ACTIVE_DETAIL_BY_ID ="";
    public static final String URL_ACTIVE_COMMENTS ="";
    public static final String URL_COMMIT_COMMENT ="";
    public static final String URL_GAME_TYPES ="";
    public static final String URL_GAMES ="";
    public static final String URL_ZIXUN ="";
    public static final String URL_GAME_COMMENT ="";
    public static final String URL_COMMIT_GAME_COMMENT ="";
    public static final String URL_GIFTS ="";
    public static final String URL_GIFT_CODE ="";
    public static final String URL_USER_GIFTS ="";
    public static final String URL_LOGIN_GAME_DAYS ="";
    public static final String URL_CHARGE_RECORD ="";
    public static final String URL_CONSUME_RECORD ="";
    public static final String URL_UPDATE_HEADER ="";
    public static final String URL_SEARCH ="";
    public static final String URL_GAME ="" ;
    public static final String URL_GET_COUPON ="";
    public static final String URL_IS_HAVE_POINT ="";
    public static final String URL_ZIXUN_DETAIL ="";
    public static final String URL_ZIXUN_COMMENT ="";
    public static final String URL_COMMIT_ZIXUN_COMMENT ="";


    /**
     * 以下云之道新增
     */
    public static final String CONSTANT_VERIFICATION_SIGNAL_KEY=BuildConfig.CONSTANT_VERIFICATION_SIGNAL_KEY;

    //以下正式地址
    public static final String ETC_BASE_URL =BuildConfig.ETC_BASE_URL;
    public static final String ETC_BASE_FENXIAO_URL =BuildConfig.ETC_BASE_FENXIAO_URL;

    //检查版本信息
//    public static final String URL_APP_VERSION_INFO= ETC_BASE_URL +"/api_client/site_admin/get_version_info" ;
    public static final String URL_APP_VERSION_INFO= ETC_BASE_URL +"/site/get_version_info" ;

    //账号登录
    public static final String URL_LOGIN = ETC_BASE_URL +"/api_client/site_admin/mobilephone_login" ;

    //微信登录
    public static final String URL_WX_LOGIN = ETC_BASE_URL +"/api_client/site_admin/wx_login" ;

    //发送验证码(需拿到Token)
    public static final String URL_VERIFICATION_TOKEN_CODE = ETC_BASE_URL +"/api_client/site_admin/get_mobilephone_code" ;

    //发送验证码(直接调用，无需拿到Token)
    public static final String URL_VERIFICATION_CODE= ETC_BASE_URL +"/api_client/client_admin/get_mobilephone_code" ;

    //微信登录获取AppId和Secret
    public static final String URL_WX_APPIDSECRET= ETC_BASE_URL +"/api_client/site_admin/get_appid" ;

    //注册
    public static final String URL_REGISTER= ETC_BASE_URL +"/api_client/site_admin/mobilephone_register" ;

    //忘记密码
    public static final String URL_FORGET_PWD= ETC_BASE_URL +"/api_client/site_admin/forget_password";

    //绑定手机
    public static final String URL_RESET_MOBILE= ETC_BASE_URL +"/api_client/client_admin/binding_mobilephone";

    //修改密码
    public static final String URL_RESET_PASSWORD= ETC_BASE_URL +"/api_client/client_admin/edit_password";

    //退出账户
    public static final String URL_LOGIN_OUT= ETC_BASE_URL +"/api_client/client_admin/logout";

    //获取首页导航图和商品条目
    public static final String URL_NAV_GOODS= ETC_BASE_URL +"/api_client/store_admin/get_home" ;

    //申请创业
    public static final String URL_FENXIAO_APPLY= ETC_BASE_URL +"/api_client/fenxiao_admin/fenxiao_shenqing" ;

    //检查是否创业
    public static final String URL_IS_FENXIAO_ACCOUNT= ETC_BASE_URL +"/api_client/fenxiao_admin/check_fenxiao_state" ;

    //获取创业中心信息
    public static final String URL_FENXIAO_CENTER= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_center" ;

    //获取店铺信息
    public static final String URL_FENXIAO_SHOP= ETC_BASE_FENXIAO_URL +"/api_client/store_admin/get_home" ;

    //获取创业我的团队
    public static final String URL_FENXIAO_MY_TEAM= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_team_list_2" ;

    //获取创业佣金
    public static final String URL_FENXIAO_COMMISSION= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_reward_in" ;

    //获取创业订单
    public static final String URL_FENXIAO_ORDER= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_order_list" ;

    //获取创业佣金明细
    public static final String URL_FENXIAO_COMMISSION_DETAILS= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_reward_list" ;

    //获取创业佣金明细提现
    public static final String URL_FENXIAO_COMMISSION_DETAILS_TIXIAN= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_tixian_list" ;

    //获取创业中心个人信息
    public static final String URL_FENXIAO_INFO= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_info" ;

    //获取创业中心我的二维码
    public static final String URL_FENXIAO_QR_INFO= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_qrcode" ;

    //修改创业中心个人信息
    public static final String URL_FENXIAO_UPDATE_INFO= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/update_fenxiao_info" ;

    //商品分类
    public static final String URL_ACTIVE_GOODS= ETC_BASE_URL +"/api_client/category_admin/get_category" ;

    //商品详情
    public static final String URL_GOODS_DETAILS= ETC_BASE_URL +"/api_client/goods_admin/goods_detail" ;

    //商品列表
    public static final String URL_ACTIVE_GOODS_LIST_GOODS= ETC_BASE_URL +"/api_client/goods_admin/get_goods_by_category" ;

    //商品搜索
    public static final String URL_ACTIVE_GOODS_LIST_SEARCH= ETC_BASE_URL +"/api_client/goods_admin/search_goods" ;

    //添加收货地址接口
    public static final String URL_ADD_ADDRESS= ETC_BASE_URL +"/api_client/client_admin/add_address" ;

    //更新收货地址接口
    public static final String URL_UPDATE_ADDRESS= ETC_BASE_URL +"/api_client/client_admin/edit_address" ;

    //修改默认收货地址接口
    public static final String URL_UPDATE_ADDRESS_DEFAULT= ETC_BASE_URL +"/api_client/client_admin/set_default_address" ;

    //收货地址列表接口
    public static final String URL_GET_ADDRESS_LIST= ETC_BASE_URL +"/api_client/client_admin/get_address_list" ;

    //会员信息编辑接口
    public static final String URL_EDIT_MEMBER= ETC_BASE_URL +"/api_client/client_admin/update_client" ;

    //绑定实体卡号接口
    public static final String URL_BIND_CARDNO= ETC_BASE_URL +"/api_client/member_admin/bind_cardno" ;

    //使用云豆卡充值云豆接口
    public static final String URL_USE_RECHARGE_CARD= ETC_BASE_URL +"/api_client/member_admin/use_recharge_card" ;

    //会员信息获取接口
    public static final String URL_CLIENT_INFO= ETC_BASE_URL +"/api_client/client_admin/client_info" ;

    //获取充值卡列表
    public static final String URL_GET_RECHARGE_LIST= ETC_BASE_URL +"/api_client/recharge_card_admin/card_list" ;

    //添加充值卡接口
    public static final String URL_ADD_RECHARGE_CARD= ETC_BASE_URL +"/api_client/recharge_card_admin/add_recharge_card" ;

    //删除充值卡接口
    public static final String URL_DEL_RECHARGE_CARD= ETC_BASE_URL +"/api_client/recharge_card_admin/del_recharge_card" ;

    //我的会员卡信息获取接口
    public static final String URL_VIP_CARD_CENTER= ETC_BASE_URL +"/api_client/member_admin/vip_card_center" ;

    //开通会员卡，会员等级信息获取接口
    public static final String URL_OPEN_VIP_CARD_INFO= ETC_BASE_URL +"/api_client/member_admin/open_vip_card_info" ;

    //我的资料信息获取接口
    public static final String URL_VIP_CARD_INFO= ETC_BASE_URL +"/api_client/member_admin/vip_card_info" ;

    //权益说明信息获取接口
    public static final String URL_VIP_CARD_EXPLAIN= ETC_BASE_URL +"/api_client/member_admin/vip_card_explain" ;

    //云豆提现信息获取接口
    public static final String URL_SCORE_APPLY= ETC_BASE_URL +"/api_client/member_admin/score_apply" ;

    //云豆推荐获取接口
    public static final String URL_VIP_RECOMMEND= ETC_BASE_URL +"/api_client/member_admin/yd_bonus_api" ;

    //云豆提现按钮接口
    public static final String URL_SCORE_APPLY_CONFIRM= ETC_BASE_URL +"/api_client/member_admin/score_apply_confirm" ;

    //云豆提现明细信息获取接口
    public static final String URL_SCORE_APPLY_HISTORY= ETC_BASE_URL +"/api_client/member_admin/score_apply_history" ;

    //云豆返还信息获取接口
    public static final String URL_SCORE_BACK= ETC_BASE_URL +"//api_client/member_admin/score_back" ;

    //升级会员，会员等级信息获取接口
    public static final String URL_UPDATE_VIP_CARD_INFO= ETC_BASE_URL +"/api_client/member_admin/update_vip_card_info" ;

    //购买会员等级（开通或升级会员）接口
    public static final String URL_VIP_CARD_PAY= ETC_BASE_URL +"/api_client/member_admin/vip_card_pay" ;

    //直接开通会员卡接口
    public static final String URL_OPEN_VIP_CARD_NOPAY= ETC_BASE_URL +"/api_client/member_admin/open_vip_card_nopay" ;

    //账户列表接口
    public static final String URL_ACCOUNT_LIST= ETC_BASE_URL +"/api_client/account_admin/account_list" ;

    //添加账户接口
    public static final String URL_ACCOUNT_ADD= ETC_BASE_URL +"/api_client/account_admin/account_add" ;

    //设置默认账户接口
    public static final String URL_SET_ACCOUNT_DEFAULT= ETC_BASE_URL +"/api_client/account_admin/set_account_default" ;

    //删除账户接口
    public static final String URL_ACCONT_DEL= ETC_BASE_URL +"/api_client/account_admin/accont_del" ;

    //我的订单接口
    public static final String URL_MY_ORDER= ETC_BASE_URL +"/api_client/order_admin/my_order" ;

    //订单详情接口
    public static final String URL_ORDER_DETAIL= ETC_BASE_URL +"/api_client/order_admin/order_detail" ;

    //取消订单接口
    public static final String URL_ORDER_CANCEL= ETC_BASE_URL +"/api_client/order_admin/order_cancel" ;

    //确认收货接口
    public static final String URL_ORDER_RECEIVE= ETC_BASE_URL +"/api_client/order_admin/order_receive" ;

    //申请退款退货接口
    public static final String URL_ORDER_APPLY_REFUND= ETC_BASE_URL +"/api_client/order_admin/order_apply_refund" ;

    //退款退货详情
    public static final String URL_ORDER_REFUND_DETAIL= ETC_BASE_URL +"/api_client/order_admin/order_refund_detail" ;

    //退款退货详情的留言接口
    public static final String URL_ADD_MESSAGE= ETC_BASE_URL +"/api_client/order_admin/add_message" ;

    //购物车【去结算】按钮接口
    public static final String URL_SET_ACCOUNT= ETC_BASE_URL +"/api_client/order_admin/setAccount" ;

    //提交订单接口
    public static final String URL_ADD_ORDER= ETC_BASE_URL +"/api_client/order_admin/add_order" ;

    //待支付订单请求支付接口
    public static final String URL_ORDER_PAY= ETC_BASE_URL +"/api_client/order_admin/order_pay" ;

    //购买云豆信息获取接口
    public static final String URL_BUY_SCORE= ETC_BASE_URL +"/api_client/score_cashier_admin/buy_score" ;

    //购买明细信息获取接口
    public static final String URL_INVENTORY_LIST= ETC_BASE_URL +"/api_client/score_cashier_admin/inventory_list" ;

    //我要收银信息获取接口
    public static final String URL_GIVE_INVENTORY_SCORE= ETC_BASE_URL +"/api_client/score_cashier_admin/give_inventory_score" ;

    //我要收银--确认收银接口
    public static final String URL_GIVE_INVENTORY_SCORE_CONFIRM= ETC_BASE_URL +"/api_client/score_cashier_admin/give_inventory_score_confirm" ;

    //收银明细信息获取接口
    public static final String URL_GIVE_LIST= ETC_BASE_URL +"/api_client/score_cashier_admin/give_list" ;

    //购买云豆支付接口
    public static final String URL_BUY_SCORE_CONFIRM= ETC_BASE_URL +"/api_client/score_cashier_admin/buy_score_confirm" ;

    //获取购买金额列表
    public static final String URL_GET_PAY_TAG_LIST= ETC_BASE_URL +"/api_client/recharge_admin/reg_money_set" ;

    //确认充值接口
    public static final String URL_REG_MONEY_CONFIRM= ETC_BASE_URL +"/api_client/recharge_admin/reg_money_confirm" ;

    //购买记录接口
    public static final String URL_REG_MONEY_LIST= ETC_BASE_URL +"/api_client/recharge_admin/reg_money_list" ;

    //银行卡管理接口
    public static final String URL_BANK_CARD= ETC_BASE_URL +"/api_client/recharge_admin/bank_card" ;

    //添加银行卡接口
    public static final String URL_ADD_BANK_CARD= ETC_BASE_URL +"/api_client/recharge_admin/add_bank_card" ;

    //删除银行卡接口
    public static final String URL_DEL_BANK_CARD= ETC_BASE_URL +"/api_client/recharge_admin/del_bank_card" ;

    //创业提现页面接口
    public static final String URL_FENXIAO_ACOUNT_TIXIAN_IN= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_acount_tixian_in" ;

    //创业提现接口
    public static final String URL_FENXIAO_ACOUNT_TIXIAN= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/fenxiao_acount_tixian" ;

    //创业升级请求支付接口
    public static final String URL_UPDATE_FENXIAO_LEVEL= ETC_BASE_FENXIAO_URL +"/api_client/fenxiao_admin/update_fenxiao_level" ;

    //我要抽奖（H5跳转到抽奖页面）
    public static final String URL_LOTTERYDRAW= ETC_BASE_URL +"/api_client/guaguale_admin/index" ;

    //中奖记录接口
    public static final String URL_GET_GUAGUALE_RECORD= ETC_BASE_URL +"/api_client/member_admin/get_guaguale_record" ;

    //获取云豆卡充值云豆历史记录接口
    public static final String URL_RECHARGE_CARD_LIST= ETC_BASE_URL +"/api_client/member_admin/recharge_card_list" ;

    //获取支付方式列表
    public static final String URL_PAY_TYPE=ETC_BASE_URL +"/api_client/site_admin/get_pay_way_list";

    //获取支付方式列表
    public static final String URL_UPAGRADE_MEMBER_PAY_TYPE=ETC_BASE_URL +"/api_client/site_admin/get_pay_way_list";

    //银行列表从接口
    public static final String URL_GET_BANK_LIST= ETC_BASE_URL +"/api_client/account_admin/get_bank_list" ;
}
