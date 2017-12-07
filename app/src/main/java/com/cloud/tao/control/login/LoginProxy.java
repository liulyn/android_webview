package com.cloud.tao.control.login;

import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;

import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/9/10/06
 */

public class LoginProxy implements ILoginAction {

    private static final String TAG = "LoginProxy";
    private  volatile static  LoginProxy instance ;

    private List<OnLoginStateChangedListener> mOnLoginStateChangedListenerList;
    private ILoginAction mLoginAction ;


    public LoginProxy() {
        mLoginAction = new LoginImpl(this) ;
        mOnLoginStateChangedListenerList = new ArrayList<>() ;
    }

    public static LoginProxy getInstance() {
        if(null == instance) {
            synchronized (LoginProxy.class) {
                if(null == instance) {
                    instance = new LoginProxy() ;
                }
            }
        }
        return instance ;
    }

    @Override
    public void login(String userName, String pwd, EntityCallBack<LoginInfo> callBack) {
        mLoginAction.login(userName,pwd,callBack);
    }

    @Override
    public void register(String smsToken,String mobilePhone,String verificationCode,String registerPassword,String remCode, EntityCallBack<LoginInfo> callBack) {
        mLoginAction.register(smsToken,mobilePhone,verificationCode,registerPassword,remCode,callBack);
    }

    @Override
    public void forgetPwd(String smsToken,String mobilePhone,String verificationCode,String registerPassword, EntityCallBack<LoginInfo> callBack) {
        mLoginAction.forgetPwd(smsToken,mobilePhone,verificationCode,registerPassword,callBack);
    }

    @Override
    public void loginWx(String accsstoken, UserInfoResp wxUser, EntityCallBack<LoginInfo> callBack) {
        mLoginAction.loginWx(accsstoken,wxUser,callBack);
    }

    @Override
    public void refreshToken() {
        mLoginAction.refreshToken();
    }

    @Override
    public void loginOut(EntityCallBack<LoginInfo> callBack) {
        mLoginAction.loginOut(callBack);
    }

    @Override
    public int getLoginState() {
        return mLoginAction.getLoginState();
    }

    @Override
    public void setLoginState(int state) {
        mLoginAction.setLoginState(state);
        notifyLoginStateChanged(state);
    }

    private void notifyLoginStateChanged(int loginState) {
        for (OnLoginStateChangedListener listener : mOnLoginStateChangedListenerList) {
            if (listener != null) {
                listener.onStateChanged(loginState);
            }
        }
    }
}
