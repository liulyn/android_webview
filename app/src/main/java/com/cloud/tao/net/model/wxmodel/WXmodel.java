package com.cloud.tao.net.model.wxmodel;

import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.wxmodel.reqresp.CheckTokenResp;
import com.cloud.tao.net.model.wxmodel.reqresp.GetAccessTokenResp;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;

public class WXmodel extends VLModel {

    public static String W_APPID="wx2ec863f666d338ae";
    public static String W_APISECRET;

    /* 通过code 获取accesstoken */
    public static final String URL_GET_ACCESSTOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code" ;

    /* 刷新token */
    public static final String URL_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=&%s&grant_type=refresh_token&refresh_token=%s" ;

    /* 检查token有效性 */
    public static final String URL_CHECK_TOKEN = "https://api.weixin.qq.com/sns/auth?access_token=%s&openid=%s" ;

    /* 获取用户信息 */
    public static final String URL_GET_USERINFO ="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";


    /* 获取 AppId和Secret */
    public void getAppIdSecret(String tag, EntityCallBack<String> httpCallBack) {
        MallOkHttpUtils.get(tag, UrlManager.URL_WX_APPIDSECRET,null,httpCallBack);
    }

    /* 通过code 获取accesstoken */
    public  void getAccessToken(String tag,String code, EntityCallBack<GetAccessTokenResp> httpCallBack) {
        MallOkHttpUtils.get(tag,String.format(URL_GET_ACCESSTOKEN,WXmodel.W_APPID,WXmodel.W_APISECRET,code),null,httpCallBack);
    }

    /* 刷新token */
    public  void refreshToken(String tag,String refreshToken,EntityCallBack<GetAccessTokenResp> httpCallBack) {
        MallOkHttpUtils.get(tag,String.format(URL_REFRESH_TOKEN,WXmodel.W_APPID,refreshToken),null,httpCallBack);
    }

    /* 检查token有效性 */
    public  void checkToken(String tag,String access_token,String openid,EntityCallBack<CheckTokenResp> httpCallBack) {
        MallOkHttpUtils.get(tag,String.format(URL_CHECK_TOKEN,access_token,openid),null,httpCallBack);
    }

    /* 获取用户信息 */
    public  void getUserInfo(String tag,String access_token, String openid, EntityCallBack<UserInfoResp> httpCallBack) {
        MallOkHttpUtils.get(tag,String.format(URL_GET_USERINFO,access_token,openid),null,httpCallBack);
    }

}
