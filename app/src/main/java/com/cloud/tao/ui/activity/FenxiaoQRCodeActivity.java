package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.upgrade.FenxiaoQRInfo;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoQrCodeBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.net.model.wxmodel.WXmodel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.glide.GlideUtil;
import com.cloud.tao.util.qrcode.QRCodeUtil;
import com.cloud.tao.wxapi.WXEntryActivity;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

/**
 * sunny created at 2016/11/3
 * des: 创业中心 我的二维码
 */
public class FenxiaoQRCodeActivity extends BaseNavBackActivity{

    private static final String TAG = "FenxiaoQRCodeActivity";
    private ActivityFenxiaoQrCodeBinding mFenxiaoQrCodeBinding;
    FenxiaoQRInfo mFenxiaoQRInfo;
    String mMyStoreId;
    IWXAPI iwxapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoQrCodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_qr_code);
        setNavDefaultBack(mFenxiaoQrCodeBinding.tb);
        mMyStoreId=getIntent().getExtras().getString("myStoreId");
        getFenxiaoQRInfo();
        /*mFenxiaoQrCodeBinding.ivFenxiaoShareSession.setOnClickListener(mOnInToAttrClickListener);
        mFenxiaoQrCodeBinding.ivFenxiaoShareTimeline.setOnClickListener(mOnInToAttrClickListener);*/
        iwxapi = WXAPIFactory.createWXAPI(FenxiaoQRCodeActivity.this, WXmodel.W_APPID);
        super.onCreate(savedInstanceState);
    }

    /*private NoDoubleClickListener mOnInToAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.iv_fenxiao_share_session:
                    shareWXApp(false);
                    break;
                case R.id.iv_fenxiao_share_timeline:
                    shareWXApp(true);
                    break;
                default:
                    break;
            }
        }
    };*/

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    private void getFenxiaoQRInfo(){
        mFenxiaoQrCodeBinding.loadding.showLoadding();
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoQRInfo(TAG,mMyStoreId,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    mFenxiaoQRInfo= GsonUtil.GsonToBean(jsonObj.optString("data"),FenxiaoQRInfo.class);
                    parseFenxiaoQRInfo(mFenxiaoQRInfo);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoQRCodeActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(FenxiaoQRCodeActivity.this,msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });

    }

    private void parseFenxiaoQRInfo(FenxiaoQRInfo mFenxiaoQRInfo){
        mFenxiaoQrCodeBinding.rlFenxiaoQrImg.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mFenxiaoQRInfo.client_url)) {
            GlideUtil.getInstance().loadCircleImage(FenxiaoQRCodeActivity.this, mFenxiaoQRInfo.client_url, mFenxiaoQrCodeBinding.ivFenxiaoQrHead);
        }
        /*if (!TextUtils.isEmpty(mFenxiaoQRInfo.store_logo_url)) {
            GlideUtil.getInstance().loadImage(FenxiaoQRCodeActivity.this,mFenxiaoQRInfo.store_logo_url, (ImageView) mFenxiaoQrCodeBinding.ivFenxiaoQrStoreIcon);
        }*/

        if (!TextUtils.isEmpty(mFenxiaoQRInfo.store_android_app_download_url)) {
            Bitmap qrCodeBitmap = null;
            String filePath =getApplicationContext().getCacheDir() + File.separator + "tongdui_qrcode.jpg";
            boolean flag = QRCodeUtil.createQRImage(mFenxiaoQRInfo.store_android_app_download_url, 400, 400, null, filePath);
            if (flag) {
                qrCodeBitmap = BitmapFactory.decodeFile(filePath);
                if(null!=qrCodeBitmap){
                    mFenxiaoQrCodeBinding.ivFenxiaoQrAppCode.setImageBitmap(qrCodeBitmap);
                }
            }
        }
        //mFenxiaoQrCodeBinding.tvFenxiaoQrStoreName.setText(TextUtils.isEmpty(mFenxiaoQRInfo.store_name)?"":"店铺名称："+mFenxiaoQRInfo.store_name);
        mFenxiaoQrCodeBinding.tvFenxiaoQrRecommendCode.setText(TextUtils.isEmpty(mFenxiaoQRInfo.recommend_code)?"":"推广码："+mFenxiaoQRInfo.recommend_code);
        mFenxiaoQrCodeBinding.loadding.showLoadSuccess();

    }

    /**
     * 创业分享到微信
     * @param isTimeLine true 朋友圈 false 好友
     */
    private void shareWXApp(boolean isTimeLine){
        if(null==mFenxiaoQRInfo){
            return;
        }
        mFenxiaoQRInfo.isTimeLine=isTimeLine;
        Intent intent = new Intent(FenxiaoQRCodeActivity.this, WXEntryActivity.class) ;
        intent.putExtra("type",0) ;
        intent.putExtra("FenxiaoQRInfo",mFenxiaoQRInfo);
        startActivity(intent);
    }
}
