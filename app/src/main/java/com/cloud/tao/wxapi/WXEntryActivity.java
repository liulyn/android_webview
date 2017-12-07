package com.cloud.tao.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.event.WXLoginEven;
import com.cloud.tao.bean.etc.upgrade.FenxiaoQRInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.wxmodel.reqresp.AppIdSecretResp;
import com.cloud.tao.net.model.wxmodel.reqresp.GetAccessTokenResp;
import com.cloud.tao.util.GsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.net.netadapter.WxResultAdapter;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.cloud.tao.R;
import com.cloud.tao.framwork.Logger;
import com.cloud.tao.net.model.wxmodel.WXmodel;
import com.cloud.tao.net.model.wxmodel.reqresp.UserInfoResp;
import com.cloud.tao.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/10/30
 * des: 微信授权登录
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXEntryActivity";
    private final String WX_TYPE_LOGIN = "wx_type_login";
    int mType;
    IWXAPI iwxapi;
    private Gson mGson;
    private WxSpInfoUtil wxSpInfoUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.msg(TAG, "onCraete");
        setContentView(R.layout.activity_wx);

        mGson = new Gson();
        wxSpInfoUtil = WxSpInfoUtil.getInstance(getApplication());
        iwxapi = WXAPIFactory.createWXAPI(WXEntryActivity.this, WXmodel.W_APPID);
        iwxapi.handleIntent(getIntent(), WXEntryActivity.this);

        mType = getIntent().getIntExtra("type", -1);
        if (!iwxapi.isWXAppInstalled()) {
            ToastUtils.showToastShort(getApplicationContext(), getString(R.string.tip_wx_uninstall));
            finish();
            return;
        }
        if (mType == -1) { //登陆
            initWXLoginDatas();
        }else{ //分享
            FenxiaoQRInfo fenxiaoQRInfo=getIntent().getParcelableExtra("FenxiaoQRInfo");
            shareWXApp(fenxiaoQRInfo);
        }
    }

    private void initWXLoginDatas() {
        MallApplication.getInstance().getModel(WXmodel.class).getAppIdSecret(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }, new WxResultAdapter<String>()) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (null != resp) {
                    try {
                        JSONObject jsonObj = new JSONObject(resp);
                        AppIdSecretResp mAppIdSecretResp = GsonUtil.GsonToBean(jsonObj.optJSONObject("data").optString("data"), AppIdSecretResp.class);
                        //WXmodel.W_APPID = mAppIdSecretResp.appid;
                        WXmodel.W_APISECRET = mAppIdSecretResp.app_weixin_pay_appsecret;
                        if(TextUtils.isEmpty(WXmodel.W_APISECRET)){
                            ToastUtils.showToastShort(getApplicationContext(), "操作异常，请稍后再试！");
                            WXEntryActivity.this.finish();
                            return;
                        }
                        wxLoginGetCode();
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(getApplicationContext(), "操作异常，请稍后再试！");
                        WXEntryActivity.this.finish();
                    }
                } else {
                    ToastUtils.showToastShort(getApplicationContext(), "未知错误，请稍后再试！");
                    WXEntryActivity.this.finish();
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(), msg);
                WXEntryActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        Logger.msg(TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.msg(TAG, "onDestroy");
    }

    public void getToken(String code) {
        MallApplication.getInstance().getModel(WXmodel.class).getAccessToken(TAG, code, new EntityCallBack<GetAccessTokenResp>(new TypeToken<GetAccessTokenResp>() {
        }, new WxResultAdapter<GetAccessTokenResp>()) {
            @Override
            public void onSuccess(int code, String msg, GetAccessTokenResp resp) {
                if (null != resp) {
                    wxSpInfoUtil.saveTokenInfo(resp);
                    getUserInfo(resp.access_token, resp.openid);
                    return;
                }
                ToastUtils.showToastShort(getApplicationContext(), "未知错误，请稍后再试！");
                finish();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(), msg);
                finish();
            }
        });
    }

    public void getUserInfo(String token, String openid) {
        MallApplication.getInstance().getModel(WXmodel.class).getUserInfo(TAG, token, openid, new EntityCallBack<UserInfoResp>(new TypeToken<UserInfoResp>() {
        }, new WxResultAdapter<UserInfoResp>()) {
            @Override
            public void onSuccess(int code, String msg, UserInfoResp resp) {
                if (null != resp) {
                    //将获取到的用户数据转给h5
                    WxSpInfoUtil.getInstance(getApplication()).saveUserInfo(resp);
                    EventBus.getDefault().post(new WXLoginEven());
                    WXEntryActivity.this.finish();
                    return;
                }
                ToastUtils.showToastShort(getApplicationContext(), "未知错误，请稍后再试！");
                WXEntryActivity.this.finish();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getApplicationContext(), msg);
                WXEntryActivity.this.finish();
            }
        });
    }

    private void wxLoginGetCode() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = WX_TYPE_LOGIN;
        iwxapi.sendReq(req);
        Logger.msg(TAG, "click login");
    }

  @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        iwxapi.handleIntent(intent, this);
  }

    /**
     * 分享到微信
     */
    private void shareWXApp(FenxiaoQRInfo mFenxiaoQRInfo) {
        if (null == mFenxiaoQRInfo) {
            finish();
            return;
        }
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = mFenxiaoQRInfo.webpageUrl;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mFenxiaoQRInfo.title;
        msg.description = mFenxiaoQRInfo.description;
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),
                R.mipmap.app_ico);
        msg.setThumbImage(thumb);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = mFenxiaoQRInfo.isTimeLine ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        iwxapi.sendReq(req);
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.msg(TAG, "req : " + mGson.toJson(baseReq));
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            SendAuth.Resp resp = (SendAuth.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    String code = resp.code;
                    getToken(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝
                    finish();
                    break;
                default:
                    finish();
                    break;
            }
        }
        if (baseResp instanceof SendMessageToWX.Resp) {
            SendMessageToWX.Resp resp = (SendMessageToWX.Resp) baseResp;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    ToastUtils.showToastShort(getApplicationContext(), "分享成功");
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL://用户取消
                    finish();
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED://用户拒绝
                    finish();
                    break;
                default:
                    finish();
                    break;
            }
        }
    }
}
