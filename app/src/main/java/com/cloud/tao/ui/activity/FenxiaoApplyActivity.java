package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoApplyBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

/**
 * sunny created at 2016/10/24
 * des: 创业申请
 */
public class FenxiaoApplyActivity extends BaseNavBackActivity {

    private static final String TAG = "FenxiaoApplyActivity";
    ActivityFenxiaoApplyBinding mFenxiaoApplyBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mFenxiaoApplyBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_apply);
        setNavDefaultBack(mFenxiaoApplyBinding.tb);
        mFenxiaoApplyBinding.btFenxiaoApply.setOnClickListener(mOnInToAttrClickListener);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }

    private NoDoubleClickListener mOnInToAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.bt_fenxiao_apply:
                    getFenxiaoInfo();
                    break;
                default:
                    break;
            }
        }
    };

    private void getFenxiaoInfo() {
        mFenxiaoApplyBinding.btFenxiaoApply.setEnabled(false);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false);
        MallApplication.getInstance().getModel(AppModel.class).toFenxiaoApply(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (code == 4) {
                    ToastUtils.showToastShort(FenxiaoApplyActivity.this, "没有绑定手机号，需绑定之后才可以申请分成为销商");
                } else {
                    ToastUtils.showToastShort(FenxiaoApplyActivity.this, "已经提交申请，请等待审核");
                    FenxiaoApplyActivity.this.finish();
                }
                ViewUtils.dismissDialog(FenxiaoApplyActivity.this,progressDialog);
                mFenxiaoApplyBinding.btFenxiaoApply.setEnabled(true);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(FenxiaoApplyActivity.this,progressDialog);
                ToastUtils.showToastShort(FenxiaoApplyActivity.this, msg);
                mFenxiaoApplyBinding.btFenxiaoApply.setEnabled(true);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }


}
