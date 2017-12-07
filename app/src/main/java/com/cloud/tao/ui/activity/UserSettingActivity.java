package com.cloud.tao.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.LoginInfo;
import com.cloud.tao.bean.etc.upgrade.UpgradeBean;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.control.login.LoginProxy;
import com.cloud.tao.databinding.ActivityUserSettingBinding;
import com.cloud.tao.framwork.vl.VLApplication;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.activity.upgrade.UpgradeActivity;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/9/10/13
 * des: 设置菜单
 */
public class UserSettingActivity extends BaseNavBackActivity{

    private static final String TAG = "UserSettingActivity";
    AlertDialog alertDialog;
    String mMobilePhone;
    ActivityUserSettingBinding mUserSettingBinding;
    MallApplication application;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserSettingBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_setting);
        setNavDefaultBack(mUserSettingBinding.tb);
        super.onCreate(savedInstanceState);
        mUserSettingBinding.rlSettingMobile.setOnClickListener(mOnDoubleClickListener);
        mUserSettingBinding.rlSettingPwd.setOnClickListener(mOnDoubleClickListener);
        mUserSettingBinding.btForgetAccount.setOnClickListener(mOnDoubleClickListener);
        mUserSettingBinding.rlSettingUpgrade.setOnClickListener(mOnDoubleClickListener);
        mUserSettingBinding.rlAboutWe.setOnClickListener(mOnDoubleClickListener);
        application = (MallApplication) getApplication();
        loadingDialog = ToastUtils.getLoadingDialog(UserSettingActivity.this, "检查版本...", true);
        initDatas(savedInstanceState);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        mMobilePhone = SharePrefUtil.getString(MallApplication.instance(), SharePrefUtil.KEY.function_mobile_phone, null);
        if (TextUtils.isEmpty(mMobilePhone)) {
            mUserSettingBinding.tvBindingMobile.setText("未绑定手机号码");
        } else {
            mUserSettingBinding.tvBindingMobile.setText(CommonUtils.subMobileReplace(mMobilePhone));
        }
    }

    private NoDoubleClickListener mOnDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rl_setting_mobile:
                    if (TextUtils.isEmpty(mMobilePhone)) {
                        UserSettingActivity.this.startActivity(new Intent(UserSettingActivity.this, ResetMobileActivity.class));
                    } else {
                        ToastUtils.showToastShort(getApplicationContext(), "手机号码暂不支持修改");
                    }
                    break;
                case R.id.rl_setting_pwd:
                    if (TextUtils.isEmpty(mMobilePhone)) {
                        ToastUtils.showToastShort(getApplicationContext(), "当前账户未绑定手机号码");
                    } else {
                        Intent intent = new Intent(UserSettingActivity.this, ResetPwdActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("mobilePhone", mMobilePhone);
                        intent.putExtras(bundle);
                        UserSettingActivity.this.startActivity(intent);
                    }
                    break;
                case R.id.rl_setting_upgrade:
                    checkUpgrade();
                    break;
                case R.id.rl_about_we:
                    UserSettingActivity.this.startActivity(new Intent(UserSettingActivity.this, CopyrightActivity.class));
                    break;
                case R.id.bt_forget_account:
                    loginOutShow();
                    break;
            }
        }
    };

    private void loginOut() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(UserSettingActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_login_out_doing), false);
        LoginProxy.getInstance().loginOut(new EntityCallBack<LoginInfo>(new TypeToken<LoginInfo>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, LoginInfo resp) {
                if (0 == code) {
                    releaseAccountInfo(progressDialog);
                    return;
                }
                onFail(code, null, msg);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                releaseAccountInfo(progressDialog);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
            }
        });
    }

    private void releaseAccountInfo(ProgressDialog progressDialog) {
        VLApplication app = VLApplication.instance();
        app.finishAllActivities(UserSettingActivity.this);
        startActivity(new Intent(UserSettingActivity.this, LoginAccountActivity.class));
        AccountInfo.getInstance().realseLoginInfo();
        ViewUtils.dismissDialog(UserSettingActivity.this, progressDialog);
        ToastUtils.showToastShort(getApplicationContext(), "退出账户成功");
        UserSettingActivity.this.finish();
    }

    private void checkUpgrade() {
        final int versionCode = application.appVersionCode();
        if (application.isDownload()) {
            ToastUtils.showToastShort(getApplicationContext(), "新版本正在下载");
            return;
        }
        loadingDialog.show();
        MallApplication.instance().getModel(AppModel.class).getAppVersionInfo(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                loadingDialog.dismiss();
                if (TextUtils.isEmpty(resp)) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    UpgradeBean upgradeBean = GsonUtil.GsonToBean(jsonObj.optString("data"), UpgradeBean.class);
                    if (Integer.valueOf(upgradeBean.version) > versionCode) {
                        Intent intent = new Intent(UserSettingActivity.this, UpgradeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("upgradeBean", upgradeBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToastShort(UserSettingActivity.this, "当前版本为最新版本，无需更新");
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(UserSettingActivity.this, "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                loadingDialog.dismiss();
                ToastUtils.showToastShort(getApplicationContext(), msg);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadingDialog.dismiss();
            }
        });
    }

    private void loginOutShow() {
        alertDialog = VLDialog.showOkCancelDialog(this, getString(R.string.dialog_common_title), "确定要退出当前账户吗？", "退出", "取消", false,
                new VLDialog.DialogCallBack() {
                    @Override
                    public void ok(Object msg) {
                        alertDialog.cancel();
                        loginOut();
                    }

                    @Override
                    public void cancel() {
                    }
                });
    }
}
