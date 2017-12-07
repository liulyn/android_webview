package com.cloud.tao.control.login;

import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.loginmodel.LoginModel;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;
import com.cloud.tao.control.AccountInfo;

/**
 * sunny created at 2016/9/10/06
 */

public class LoginImpl implements ILoginAction {

    private static final String TAG = "LoginImpl";
    private LoginProxy mProxy ;
    private int loginState = LoginState.LOGINOUT ;

    public LoginImpl(LoginProxy proxy) {
        this.mProxy = proxy ;
    }


    private EntityCallBack<LoginInfo> callBack ; //调用登陆接口时，传递的回调
    private EntityCallBack<LoginInfo> loginCallBack = new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>(){}) {
        @Override
        public void onSuccess(int code, String msg, LoginInfo resp) {
            AccountInfo.getInstance().setLoginInfo(resp);

            callBack.onSuccess(code,msg,resp);
            mProxy.setLoginState(LoginState.LOGINED);

        }

        @Override
        public void onFail(int code, Exception e, String msg) {
            callBack.onFail(code,e,msg);
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            callBack.onAfter(id);
        }
    } ;

    @Override
    public void login(String userName, String pwd, final EntityCallBack<LoginInfo> callBack) {
        this.callBack = callBack ;
        MallApplication.getInstance().getModel(LoginModel.class).loginNormal(TAG, userName, pwd, loginCallBack);
    }

    @Override
    public void register(String smsToken,String mobilePhone,String verificationCode,String registerPassword,String remCode, final EntityCallBack<LoginInfo> callBack) {
        this.callBack = callBack ;
        MallApplication.getInstance().getModel(LoginModel.class).register(TAG,smsToken,mobilePhone,verificationCode,registerPassword,remCode, loginCallBack);
    }

    @Override
    public void forgetPwd(String smsToken,String mobilePhone,String verificationCode,String registerPassword, final EntityCallBack<LoginInfo> callBack) {
        this.callBack = callBack ;
        MallApplication.getInstance().getModel(LoginModel.class).forgetPwd(TAG,smsToken,mobilePhone,verificationCode,registerPassword, loginCallBack);
    }

    @Override
    public void loginOut(EntityCallBack<LoginInfo> callBack) {
        this.callBack = callBack ;
        MallApplication.getInstance().getModel(LoginModel.class).loginOut(TAG,loginCallBack);
    }

    @Override
    public void loginWx(String accsstoken, UserInfoResp wxUser, final EntityCallBack<LoginInfo> callBack) {
        this.callBack = callBack ;
        MallApplication.getInstance().getModel(LoginModel.class).loginForWX(TAG, accsstoken, wxUser,loginCallBack);
    }

    @Override
    public void refreshToken() {}

    @Override
    public int getLoginState() {
        return loginState;
    }

    @Override
    public void setLoginState(int state) {
        this.loginState = state ;
    }


}
