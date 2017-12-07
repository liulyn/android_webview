package com.cloud.tao.net.model.appmodel;

import java.util.List;

import com.cloud.tao.bean.ActiveDetailInfo;
import com.cloud.tao.bean.ActiveInfo;
import com.cloud.tao.bean.ConsumeRecordInfo;
import com.cloud.tao.bean.GameDetailInfo;
import com.cloud.tao.bean.GameInfo;
import com.cloud.tao.bean.GameSortInfo;
import com.cloud.tao.bean.LoginGameDayInfo;
import com.cloud.tao.bean.MePointInfo;
import com.cloud.tao.bean.MyGiftInfo;
import com.cloud.tao.bean.ZiXunInfo;
import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.base.BasePageReq;
import com.cloud.tao.net.base.BaseReq;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.req.ActiveDetailReq;
import com.cloud.tao.net.model.appmodel.req.ActivesReq;
import com.cloud.tao.net.model.appmodel.req.CommentsReq;
import com.cloud.tao.net.model.appmodel.req.CommitCommentReq;
import com.cloud.tao.net.model.appmodel.req.CommitGameCommentReq;
import com.cloud.tao.net.model.appmodel.req.FenxiaoAcountTixianReq;
import com.cloud.tao.net.model.appmodel.req.FenxiaoCommissionReq;
import com.cloud.tao.net.model.appmodel.req.FenxiaoCommissionTixianReq;
import com.cloud.tao.net.model.appmodel.req.FenxiaoOrderReq;
import com.cloud.tao.net.model.appmodel.req.FenxiaoUpdateReq;
import com.cloud.tao.net.model.appmodel.req.GameDetailReq;
import com.cloud.tao.net.model.appmodel.req.GamesReq;
import com.cloud.tao.net.model.appmodel.req.GiftCodeReq;
import com.cloud.tao.net.model.appmodel.req.GoodsDetailsReq;
import com.cloud.tao.net.model.appmodel.req.GoodsReq;
import com.cloud.tao.net.model.appmodel.req.GoodsSearchReq;
import com.cloud.tao.net.model.appmodel.req.LoginGameDaysReq;
import com.cloud.tao.net.model.appmodel.req.LoginReq;
import com.cloud.tao.net.model.appmodel.req.ResetMobileReq;
import com.cloud.tao.net.model.appmodel.req.ResetPwdReq;
import com.cloud.tao.net.model.appmodel.req.UpdateFenxiaoLevelReq;
import com.cloud.tao.net.model.appmodel.req.ZiXunDetailReq;
import com.cloud.tao.net.model.appmodel.req.ZixunCommentReq;
import com.cloud.tao.bean.ActiveCommentInfo;
import com.cloud.tao.bean.ChargeRecordInfo;
import com.cloud.tao.bean.GameCommentInfo;
import com.cloud.tao.bean.GiftInfo;
import com.cloud.tao.bean.UserGiftInfo;
import com.cloud.tao.bean.ZiXunDetailInfo;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.model.appmodel.req.GameCommentsReq;
import com.cloud.tao.net.model.appmodel.req.NavImagesReq;
import com.cloud.tao.net.model.appmodel.req.SearchReq;
import com.cloud.tao.net.model.appmodel.req.UserGameRecodReq;
import com.cloud.tao.net.model.appmodel.req.UserGiftsReq;
import com.cloud.tao.net.model.appmodel.req.ZiXunReq;
import com.cloud.tao.net.model.loginmodel.req.VerificationCodeReq;

/**
 * author:janecer on 2016/8/10 10:50
 */


public class AppModel extends VLModel {

    /**
     * 获取推荐（参数：sum返回的条数）
     * @param tag
     * @param callBack GameInfo
     */
    public void getRecommandGames(String tag, EntityCallBack<List<GameInfo>> callBack) {
        NavImagesReq req = new NavImagesReq() ;
        MallOkHttpUtils.post(tag, UrlManager.URL_RECOMMAND_GAME,req,callBack);
    }

    /**
     * 获取轮播图(参数：sum返回的条数)
     * @param tag
     * @param type 1为轮播图，2为广告
     * @param callBack NavImgInfo
     */
   /* public void getNavImg(String tag,int type,EntityCallBack<List<NavImgInfo>> callBack) {
        NavImagesReq req = new NavImagesReq() ;
        req.type_id = type ;
        MallOkHttpUtils.post(tag, UrlManager.URL_NAV_IMG_GET,req,callBack);
    }*/

    /**
     * 获取用户记录
     * @param tag
     * @param token 用户ID
     * @param callBack  GameInfo  返回rcode=110002(用户ID有误) 返回rcode=000000 (获取用户记录成功)
     */
    public void getGameRecord(String tag,String token,EntityCallBack<List<GameInfo>> callBack) {
        UserGameRecodReq req = new UserGameRecodReq() ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GAME_RECORD_GET,req,callBack);
    }

    /**
     * 获取活动列表
     * @param tag
     * @param pageSize 一页的条数，默认5
     * @param pageIndex 第几页，默认1
     * @param callBack ActiveInfo
     */
    public void getActives(String tag,int pageSize,int pageIndex,EntityCallBack<List<ActiveInfo>> callBack) {
        ActivesReq req = new ActivesReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVES,req,callBack);
    }

    /**
     * 根据ID  获取详情
     * @param tag
     * @param gameId ID
     * @param callBack  GameDetailInfo
     */
    public void getGameDetailById(String tag,String gameId,EntityCallBack<GameDetailInfo> callBack) {
        GameDetailReq req = new GameDetailReq() ;
        req.gameid = gameId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GAME_DETAIL_BY_ID,req,callBack);
    }

    /**
     * 根据活动ID 获取活动详情
     * @param tag
     * @param activeId 活动ID
     * @param callBack ActiveDetailInfo
     */
    public void getActiveDetail(String tag,String activeId,EntityCallBack<ActiveDetailInfo> callBack) {
        ActiveDetailReq req = new ActiveDetailReq() ;
        req.huodongid = activeId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_DETAIL_BY_ID,req,callBack);
    }

    /**
     * 获取活动评论列表
     * @param tag
     * @param pageSize 一页的条数，默认5
     * @param pageIndex 第几页，默认1
     * @param activeId 活动ID
     * @param callBack ActiveCommentInfo
     */
    public void getComments(String tag,int pageSize,int pageIndex,String activeId,EntityCallBack<List<ActiveCommentInfo>> callBack) {
        CommentsReq req = new CommentsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.huodongid = activeId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_COMMENTS,req,callBack);
    }

    /**
     * 提交活动评论和回复
     * @param tag
     * @param type type(1,评论，2，回复)
     * @param comment 评论内容
     * @param post_id 活动ID
     * @param mem_id 发表评论用户ID
     * @param parentid 被回复评论ID    针对回复评价 否则传递null
     * @param to_mem_id 发表回复人ID   针对回复评价 否则传递null
     * @param callBack （rcode=000000）失败（rcode=110002）
     */
    public void commitComment(String tag,int type,String comment,String post_id,String mem_id,String parentid,String to_mem_id,
                              EntityCallBack<String> callBack) {
        CommitCommentReq req = new CommitCommentReq() ;
        req.type = type ;
        req.content = comment ;
        req.post_id = post_id ;
        req.mem_id = mem_id ;
        req.parentid = parentid ;
        req.to_mem_id = to_mem_id ;
        MallOkHttpUtils.post(tag,UrlManager.URL_COMMIT_COMMENT,req,callBack);
    }

    /**
     * 获取分类
     * @param tag
     * @param callBack
     */
    public void gameSorts(String tag, EntityCallBack<List<GameSortInfo>> callBack) {
        BaseReq req = new BaseReq() ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GAME_TYPES,req,callBack);
    }

    /**
     * 获取列表
     * @param tag
     * @param pageSize 每页的条数，默认为10
     * @param pageIndex 分页的页码，默认为1
     * @param type （分类ID.默认为0代表全部分类）
     * @param sort （排列顺序类型，默认为1。 1,最新时间2评分最高3人气最高）
     * @param gameType （平台类型 默认为1,0为全部，1，2为小）
     * @param callBack
     */
    public void games(String tag,int pageSize,int pageIndex,String type,int sort,int gameType,EntityCallBack<List<GameInfo>> callBack) {
        GamesReq req = new GamesReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.where1 = type ;
        req.where5 = sort ;
        req.where7 = gameType ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GAMES,req,callBack);
    }

    /**
     * 获取资讯
     * @param tag
     * @param pageSize 一页条数，默认为10
     * @param pageIndex 分页的页码，默认为1
     * @param appId (ID,默认为0，0代表全部)
     * @param postType 资讯类型。默认为0，0代表全部，1代表动态，2代表攻略
     * @param callBack
     */
    public void zixuns(String tag, int pageSize, int pageIndex, String appId,int postType, EntityCallBack<List<ZiXunInfo>> callBack) {
        ZiXunReq req = new ZiXunReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.app_id = appId ;
        req.post_type = postType ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ZIXUN,req,callBack);
    }

    /**
     * 获取评论
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param appId ID
     * @param callBack
     */
    public void gameComments(String tag, int pageSize, int pageIndex, String appId, EntityCallBack<List<GameCommentInfo>> callBack){
        GameCommentsReq req = new GameCommentsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.app_id = appId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GAME_COMMENT,req,callBack);
    }

    /**
     * 评论和回复功能
     * @param tag
     * @param type
     * @param comment 回复内容
     * @param appId ID
     * @param memId 被回复用户ID
     * @param parentId 被回复评论ID 回复评论必填
     * @param toMemId 发表回复人ID 回复评价必填
     * @param callBack （rcode=000000）失败（rcode=110002）
     */
    public void commitGameComment(String tag,int type,String comment,String appId,String memId,String parentId,String toMemId,EntityCallBack<String> callBack){
        CommitGameCommentReq req = new CommitGameCommentReq() ;
        req.type = type ;
        req.content = comment ;
        req.app_id = appId ;
        req.mem_id = memId ;
        req.parentid = parentId ;
        req.to_mem_id = toMemId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_COMMIT_GAME_COMMENT,req,callBack);
    }

    /**
     * 获取礼包列表
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param gameId ID，默认0为全部
     * @param giftId  （礼包ID，默认0为全部）
     * @param callBack
     */
    public void Gifts(String tag, int pageSize, int pageIndex, String gameId,String giftId, EntityCallBack<List<GiftInfo>> callBack){
        GameCommentsReq req = new GameCommentsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.app_id = gameId ;
        req.id = giftId ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GIFTS,req,callBack);
    }

    /**
     * 领取礼包
     * @param tag
     * @param token (用户ID
     * @param giftId 礼包id
     * @param callBack
     */
    public void giftCode(String tag,  String token,String giftId, EntityCallBack<String> callBack){
        GiftCodeReq req = new GiftCodeReq() ;
        req.id = giftId ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GIFT_CODE,req,callBack);
    }

    /**
     * 获取用户礼包列表
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param token 用户ID
     * @param callBack
     */
    public void userGifts(String tag, int pageSize, int pageIndex, String token, EntityCallBack<List<UserGiftInfo>> callBack){
        UserGiftsReq req = new UserGiftsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_USER_GIFTS,req,callBack);
    }

    /**
     * 获取用户连续登录天数
     * @param tag
     * @param pageSize 一页条数，默认10
     * @param pageIndex 页码，默认1
     * @param gameId (ID，默认0为全部)
     * @param giftId 礼包ID，默认0为全部
     * @param token 用户ID
     * @param callBack
     */
    public void LoginGameDays(String tag, int pageSize, int pageIndex, String gameId, String giftId, String token,
                              EntityCallBack<List<LoginGameDayInfo>> callBack) {
        LoginGameDaysReq req = new LoginGameDaysReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.app_id = gameId ;
        req.id = giftId ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_LOGIN_GAME_DAYS,req,callBack);
    }

    /**
     * 获取用户购买记录
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param token
     * @param callBack
     */
    public void userChargeRecords(String tag, int pageSize, int pageIndex, String token, EntityCallBack<List<ChargeRecordInfo>> callBack){
        UserGiftsReq req = new UserGiftsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_CHARGE_RECORD,req,callBack);
    }

    /**
     * 获取用户消费记录
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param token
     * @param callBack
     */
    public void userConsumeRecords(String tag, int pageSize, int pageIndex, String token, EntityCallBack<List<ConsumeRecordInfo>> callBack){
        UserGiftsReq req = new UserGiftsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_CONSUME_RECORD,req,callBack);
    }

    /**
     * 搜索
     * @param tag
     * @param searchTxt
     * @param callBack
     */
    public void search(String tag,String searchTxt,EntityCallBack<List<GameInfo>> callBack) {
        SearchReq req = new SearchReq() ;
        req.keyword = searchTxt ;
        MallOkHttpUtils.post(tag,UrlManager.URL_SEARCH,req,callBack);
    }

    /**
     * 地址
     * @param gameId
     * @param token
     * @return
     */
    public String getGameUrl(String gameId,String token) {
        return String.format(UrlManager.URL_GAME,gameId,token) ;
    }






    /**
     * 查询用户未使用的优惠券礼包
     * @param tag
     * @param pageSize
     * @param pageIndex
     * @param token
     * @param callBack
     */
    public void getUnUseGift(String tag, int pageSize, int pageIndex, String token, EntityCallBack<List<MyGiftInfo>> callBack) {
        UserGiftsReq req = new UserGiftsReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_GET_COUPON,req,callBack);
    }

    /**
     * 是否有 最新充值优惠，跟是否未使用的礼包
     * @param tag
     * @param token
     * @param callBack
     */
    public void getPoint(String tag,String token, EntityCallBack<MePointInfo> callBack) {
        LoginReq req = new LoginReq() ;
        req.token = token ;
        MallOkHttpUtils.post(tag,UrlManager.URL_IS_HAVE_POINT,req,callBack);
    }

    /**
     * 根据资讯Id获取资讯详情
     * @param tag
     * @param id
     * @param callBack
     */
    public void getZixunDetailById(String tag, String id, EntityCallBack<ZiXunDetailInfo> callBack) {
        ZiXunDetailReq req = new ZiXunDetailReq() ;
        req.id = id ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ZIXUN_DETAIL,req,callBack);
    }


    /**
     * 获取资讯评论列表
     * @param tag
     * @param pageSize 一页的条数，默认5
     * @param pageIndex 第几页，默认1
     * @param id 活动ID
     * @param callBack ActiveCommentInfo
     */
    public void getZixunComments(String tag,int pageSize,int pageIndex,String id,EntityCallBack<List<ActiveCommentInfo>> callBack) {
        ZixunCommentReq req = new ZixunCommentReq() ;
        req.num = pageSize ;
        req.p = pageIndex ;
        req.id = id ;
        MallOkHttpUtils.post(tag,UrlManager.URL_ZIXUN_COMMENT,req,callBack);
    }

    /**
     * 提交资讯评论
     * @param tag
     * @param type type(1,评论，2，回复)
     * @param comment 评论内容
     * @param post_id 活动ID
     * @param mem_id 发表评论用户ID
     * @param parentid 被回复评论ID    针对回复评价 否则传递null
     * @param to_mem_id 发表回复人ID   针对回复评价 否则传递null
     * @param callBack （rcode=000000）失败（rcode=110002）
     */
    public void commitZixunComment(String tag,int type,String comment,String post_id,String mem_id,String parentid,String to_mem_id,
                              EntityCallBack<String> callBack) {
        CommitCommentReq req = new CommitCommentReq() ;
        req.type = type ;
        req.content = comment ;
        req.post_id = post_id ;
        req.mem_id = mem_id ;
        req.parentid = parentid ;
        req.to_mem_id = to_mem_id ;
        MallOkHttpUtils.post(tag,UrlManager.URL_COMMIT_ZIXUN_COMMENT,req,callBack);
    }

    //云之道新增

    /**
     * 获取验证码（无需拿到token）
     * @param tag
     */
    public void verificationCode(String tag, String mobilePhone, String tokenKey, EntityCallBack<String> callBack) {
        VerificationCodeReq req = new VerificationCodeReq() ;
        req.mobilephone=mobilePhone;
        req.token=tokenKey;
        MallOkHttpUtils.post(tag, UrlManager.URL_VERIFICATION_CODE,req,callBack);
    }

    /**
     * 重置手机号
     * @param tag
     */
    public void resetMobilePhone(String tag, String newMobilePhone, String verificationCode, EntityCallBack<String> callBack) {
        ResetMobileReq req = new ResetMobileReq() ;
        req.mobilephone=newMobilePhone;
        req.code=verificationCode;
        MallOkHttpUtils.post(tag, UrlManager.URL_RESET_MOBILE,req,callBack);
    }

    /**
     * 重置密码
     * @param tag
     */
    public void resetPassword(String tag, String mobilePhone, String verificationCode,String newPassword,EntityCallBack<String> callBack) {
        ResetPwdReq req = new ResetPwdReq() ;
        req.mobilephone=mobilePhone;
        req.code=verificationCode;
        req.password=newPassword;
        MallOkHttpUtils.post(tag, UrlManager.URL_RESET_PASSWORD,req,callBack);
    }

    /**
     * 获取首页导航图和商品条目
     * @param tag
     * @param callBack
     */
    public void getNavImgAndGoods(String tag,boolean isRefresh,String storeId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        String URL=UrlManager.URL_NAV_GOODS;
        if(isRefresh){
            URL=appendFenxiaoUrl(storeId,UrlManager.URL_FENXIAO_SHOP);
        }
        MallOkHttpUtils.post(tag,URL,req,callBack);
    }

    /**
     * 拼接商品铺地址
     * @param storeId 商铺Id
     * @param BaseUrl
     * @return
     */
    private String appendFenxiaoUrl(String storeId,String BaseUrl){
        String fenxiaoUrlStr=BaseUrl;
        fenxiaoUrlStr = String.format(fenxiaoUrlStr, storeId);
        return fenxiaoUrlStr;
    }

    /**
     * 获取创业申请
     * @param tag
     * @param callBack
     */
    public void toFenxiaoApply(String tag,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_FENXIAO_APPLY,req,callBack);
    }

    /**
     * 获取创业中心信息
     * @param tag
     * @param callBack
     */
    public void getFenxiaoCenter(String tag,String myStoreId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_CENTER),req,callBack);
    }

    /**
     * 获取创业我的团队
     * @param tag
     * @param callBack
     */
    public void getFenxiaoMyTeamInfo(String tag,String myStoreId,int pageIndex,EntityCallBack<String> callBack) {
        BasePageReq req = new BasePageReq();
        req.p=pageIndex;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_MY_TEAM),req,callBack);
    }

    /**
     * 获取创业佣金
     * @param tag
     * @param callBack
     */
    public void getFenxiaoCommissionInfo(String tag,String myStoreId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_COMMISSION),req,callBack);
    }

    /**
     * 获取创业佣金明细
     * @param tag
     * @param callBack
     */
    public void getFenxiaoCommissionDetailsInfo(String tag,String myStoreId,int state,int p,EntityCallBack<String> callBack) {
        FenxiaoCommissionReq req = new FenxiaoCommissionReq();
        req.state=state;
        req.p=p;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_COMMISSION_DETAILS),req,callBack);
    }

    /**
     * 获取创业订单
     * @param tag
     * @param callBack
     */
    public void getFenxiaoOrderInfo(String tag,String myStoreId,String state,int p,EntityCallBack<String> callBack) {
        FenxiaoOrderReq req = new FenxiaoOrderReq();
        req.state=state;
        req.p=p;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_ORDER),req,callBack);
    }

    /**
     * 获取创业佣金明细提现
    * @param tag
    * @param callBack
    */
    public void getFenxiaoCommissionDetailsTiXianInfo(String tag,String myStoreId,int p,EntityCallBack<String> callBack) {
        FenxiaoCommissionTixianReq req = new FenxiaoCommissionTixianReq();
        req.p=p;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_COMMISSION_DETAILS_TIXIAN),req,callBack);
    }

    /**
     * 获取是否创业
     * @param tag
     * @param callBack
     */
    public void getIsFenxiaoAccount(String tag,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_IS_FENXIAO_ACCOUNT,req,callBack);
    }

    /**
     * 获取创业个人信息
     * @param tag
     * @param callBack
     */
    public void getFenxiaoInfo(String tag,String myStoreId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_INFO),req,callBack);
    }

    /**
     * 获取创业我的二维码个人信息
     * @param tag
     * @param callBack
     */
    public void getFenxiaoQRInfo(String tag,String myStoreId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_QR_INFO),req,callBack);
    }

    /**
     * 修改创业信息
     * @param tag
     * @param callBack
     */
    public void updateFenxiaoInfo(String tag,String myStoreId,String nickName,String storeName,EntityCallBack<String> callBack) {
        FenxiaoUpdateReq req = new FenxiaoUpdateReq();
        req.nick_name=nickName;
        req.store_name=storeName;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_UPDATE_INFO),req,callBack);
    }

    /**
     * 获取商品分类
     * @param tag
     * @param callBack
     */
    public void getActiveGoods(String tag,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_GOODS,req,callBack);
    }

    /**
     * 获取商品列表
     * @param tag
     * @param callBack
     */
    public void getAllGoodsList(String tag,String searchContent,int pageSize,int page,EntityCallBack<String> callBack){
        GoodsSearchReq req=new GoodsSearchReq();
        req.f=searchContent;
        req.l=String.valueOf(pageSize) ;
        req.p=String.valueOf(page);
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_GOODS_LIST_SEARCH,req,callBack);
    }

    /**
     * 获取商品列表
     * @param tag
     * @param callBack
     */
    public void getGoodsList(String tag,String activeId,int pageSize,int page,EntityCallBack<String> callBack){
        GoodsReq req=new GoodsReq();
        req.c=activeId;
        req.l=String.valueOf(pageSize) ;
        req.p=String.valueOf(page);
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_GOODS_LIST_GOODS,req,callBack);
    }

    /**
     * 获取搜索商品列表
     * @param tag
     * @param callBack
     */
    public void getSearchGoodsList(String tag,String searchContent,int pageSize,int page,EntityCallBack<String> callBack){
        GoodsSearchReq req=new GoodsSearchReq();
        req.f=searchContent;
        req.l=String.valueOf(pageSize) ;
        req.p=String.valueOf(page);
        MallOkHttpUtils.post(tag,UrlManager.URL_ACTIVE_GOODS_LIST_SEARCH,req,callBack);
    }

    /**
     * 获取商品详情
     * @param tag
     * @param callBack
     */
    public void getGoodsDetails(String tag,String goodsId,EntityCallBack<String> callBack) {
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.g=goodsId;
        MallOkHttpUtils.post(tag,UrlManager.URL_GOODS_DETAILS,req,callBack);
    }

    /**
     * 创业提现页面接口
     * @param tag
     * @param callBack
     */
    public void getFenxiaoAcountTixianIn(String tag,String myStoreId,EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_ACOUNT_TIXIAN_IN),req,callBack);
    }

    /**
     * 创业提现接口
     * @param tag
     * @param callBack
     */
    public void postFenxiaoAcountTixian(String tag,String myStoreId,int id,double tixian_commission,EntityCallBack<String> callBack) {
        FenxiaoAcountTixianReq req = new FenxiaoAcountTixianReq();
        req.id = id;
        req.tixian_commission = tixian_commission;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_FENXIAO_ACOUNT_TIXIAN),req,callBack);
    }

    /**
     * 创业升级请求支付接口
     * @param tag
     * @param callBack
     */
    public void updateFenxiaoLevel(String tag,String myStoreId,int pay_way,EntityCallBack<String> callBack) {
        UpdateFenxiaoLevelReq req = new UpdateFenxiaoLevelReq();
        req.pay_way = pay_way;
        MallOkHttpUtils.post(tag,appendFenxiaoUrl(myStoreId,UrlManager.URL_UPDATE_FENXIAO_LEVEL),req,callBack);
    }

    public void getFenxiaoScoreRecommend(String tag,int pageIndex,EntityCallBack<String> callBack) {
        BasePageReq req = new BasePageReq();
        req.p=pageIndex;
        MallOkHttpUtils.post(tag,UrlManager.URL_VIP_RECOMMEND,req,callBack);
    }

    /**
     * 获取最新版本信息
     * @param tag
     */
    public void getAppVersionInfo(String tag, EntityCallBack<String> callBack) {
        BaseReq req = new BaseReq() ;
        MallOkHttpUtils.post(tag, UrlManager.URL_APP_VERSION_INFO,req,callBack);
    }




}
