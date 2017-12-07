package com.cloud.tao.control.login;

import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;

/**
 * author:janecer on 2016/9/3 18:19
 */


public interface ILoginAction {

    /**
     * 登录
     */
    void login(String userName,String pwd, EntityCallBack<LoginInfo> callBack) ;

    /**
     * 注册
     */
    void register(String smsToken,String mobilePhone,String verificationCode,String registerPassword,String remCode, EntityCallBack<LoginInfo> callBack) ;

    /**
     * 忘记密码
     */
    void forgetPwd(String smsToken,String mobilePhone,String verificationCode,String registerPassword, EntityCallBack<LoginInfo> callBack);

    /**
     * 微信方式 请求云之道服务器登录
     * @param accsstoken
     * @param wxUser
     * @param callBack
     */
    void loginWx(String accsstoken, UserInfoResp wxUser, EntityCallBack<LoginInfo> callBack) ;

    /**
     * 刷新token
     */
    void refreshToken() ;

    /**
     * 退出登录
     */
    void loginOut(EntityCallBack<LoginInfo> callBack) ;

    /**
     *
     * @return
     */
    int getLoginState() ;

    void setLoginState(int state) ;

}
