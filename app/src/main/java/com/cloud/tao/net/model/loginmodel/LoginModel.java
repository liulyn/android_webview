package com.cloud.tao.net.model.loginmodel;

import com.cloud.tao.framwork.vl.VLModel;
import com.cloud.tao.net.MallOkHttpUtils;
import com.cloud.tao.net.base.BaseReq;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.loginmodel.req.ForGetPwdReq;
import com.cloud.tao.net.model.loginmodel.req.LoginReq;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.model.loginmodel.req.LoginForWxReq;
import com.cloud.tao.net.model.loginmodel.req.RegisterReq;
import com.cloud.tao.net.model.loginmodel.req.VerificationCodeReq;
import com.cloud.tao.net.model.loginmodel.req.UpdateHeaderReq;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * sunny created at 2016/9/10/06
 */
public class LoginModel extends VLModel {


    /**
     *
     * @param tag
     * @param username
     * @param pwd
     * @param callBack //失败（rcode=110002）
     */
    public void loginNormal(String tag, String username, String pwd , EntityCallBack<LoginInfo> callBack) {
        LoginReq req = new LoginReq() ;
        req.mobilephone = username ;
        req.password = pwd ;
        MallOkHttpUtils.post(tag, UrlManager.URL_LOGIN,req,callBack);
    }

    public void loginForWX(String tag, String accsstoken, UserInfoResp wxUser, EntityCallBack<LoginInfo> callBack) {
        LoginForWxReq req= new LoginForWxReq() ;
        req.convert(accsstoken,wxUser);
        MallOkHttpUtils.post(tag, UrlManager.URL_WX_LOGIN,req,callBack);
    }

    /**
     * 获取验证码（需拿到token）
     * @param tag
     */
    public void verificationTokenCode(String tag, String mobilePhone, String tokenKey, EntityCallBack<String> callBack) {
        VerificationCodeReq req = new VerificationCodeReq() ;
        req.mobilephone=mobilePhone;
        req.token=tokenKey;
        MallOkHttpUtils.post(tag, UrlManager.URL_VERIFICATION_TOKEN_CODE,req,callBack);
    }

    /**
     * 注册账号
     * @param tag
     */
    public void register(String tag,String smsToken,String mobilePhone,String verificationCode,String password ,String remCode,EntityCallBack<LoginInfo> callBack) {
        RegisterReq req = new RegisterReq();
        req.sms_token=smsToken;
        req.mobilephone=mobilePhone;
        req.code=verificationCode;
        req.password=password;
        req.rem_code=remCode;
        MallOkHttpUtils.post(tag, UrlManager.URL_REGISTER,req,callBack);
    }

    /**
     * 忘记密码
     * @param tag
     */
    public void forgetPwd(String tag,String smsToken,String mobilePhone,String verificationCode,String password,EntityCallBack<LoginInfo> callBack) {
        ForGetPwdReq req = new ForGetPwdReq();
        req.sms_token=smsToken;
        req.mobilephone=mobilePhone;
        req.code=verificationCode;
        req.password=password;
        MallOkHttpUtils.post(tag, UrlManager.URL_FORGET_PWD,req,callBack);
    }

    /**
     * 退出登录
     * @param tag
     */
    public void loginOut(String tag,EntityCallBack<LoginInfo> callBack) {
        BaseReq req = new BaseReq();
        MallOkHttpUtils.post(tag, UrlManager.URL_LOGIN_OUT,req,callBack);
    }

    /**
     * 修改头像
     * @param tag
     * @param token 用户id
     * @param picUrl 头像地址
     * @param callBack 成功返回rcode=000000  失败返回rcode=110002，desc为错误说明
     */
    public void updateHeader(String tag,String token,String picUrl,EntityCallBack<String> callBack) {
        UpdateHeaderReq req = new UpdateHeaderReq() ;
        req.token = token ;
        Map<String ,List<String>> pics = new HashMap<>() ;
        List<String> paths = new ArrayList<>() ;
        paths.add(picUrl);
        pics.put("photo_url",paths) ;
        MallOkHttpUtils.postFile(tag,UrlManager.URL_UPDATE_HEADER,pics,req,callBack);
    }
}
