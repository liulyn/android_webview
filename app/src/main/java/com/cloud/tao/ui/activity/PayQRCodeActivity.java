package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityPayQrCodeBinding;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.qrcode.QRCodeUtil;
import java.io.File;

/**
 * sunny created at 2016/11/3
 * des: 二维码付款
 */
public class PayQRCodeActivity extends BaseNavBackActivity {

    private ActivityPayQrCodeBinding mActivityPayQrCodeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityPayQrCodeBinding = DataBindingUtil.setContentView(this, R.layout.activity_pay_qr_code);
        setNavDefaultBack(mActivityPayQrCodeBinding.tb);
        super.onCreate(savedInstanceState);
        parseFenxiaoQRInfo();
    }


    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    private void parseFenxiaoQRInfo() {
        String mMobilePhone = SharePrefUtil.getString(MallApplication.instance(), SharePrefUtil.KEY.function_mobile_phone, null);
        if (TextUtils.isEmpty(mMobilePhone)) {
            ToastUtils.showToastShort(PayQRCodeActivity.this, "没有绑定手机号，绑定之后可使用云豆付款");
            return;
        } else {
            Bitmap qrCodeBitmap = null;
            String filePath = getApplicationContext().getCacheDir() + File.separator + "score_pay_qrcode.jpg";
            boolean flag = QRCodeUtil.createQRImage(mMobilePhone, 600, 600, null, filePath);
            if (flag) {
                qrCodeBitmap = BitmapFactory.decodeFile(filePath);
                if (null != qrCodeBitmap) {
                    mActivityPayQrCodeBinding.ivScorePayQrCode.setImageBitmap(qrCodeBitmap);
                }
            }
        }
    }

}
