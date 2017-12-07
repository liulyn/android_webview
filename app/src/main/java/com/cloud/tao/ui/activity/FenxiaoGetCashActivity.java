package com.cloud.tao.ui.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.Account;
import com.cloud.tao.bean.etc.GetCash;
import com.cloud.tao.bean.etc.event.GetCashMoneyEven;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoGetCashBinding;
import com.cloud.tao.databinding.ActivityIntegralCashBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gezi-pc on 2016/10/1.
 */

public class FenxiaoGetCashActivity extends BaseNavBackActivity{


    private static final String TAG = FenxiaoGetCashActivity.class.getSimpleName();
    ActivityFenxiaoGetCashBinding mActivityFenxiaoGetCashBinding;
    private Account defaultAccount;
    public static final int SELECT_ACCOUNT_REQUEST_CODE = 0;
    private GetCash getCash;
    private String myStoreId;
    double mMaxAmount;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityFenxiaoGetCashBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_get_cash);
        setNavDefaultBack(mActivityFenxiaoGetCashBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityFenxiaoGetCashBinding.loadding.showLoadSuccess();
        mActivityFenxiaoGetCashBinding.rlPromotionLeaguerType.setOnClickListener(mOnDoubleClickListener);
        mActivityFenxiaoGetCashBinding.btSureCash.setOnClickListener(mOnDoubleClickListener);
        iniFenxiaoInfo();
    }

    private NoDoubleClickListener mOnDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rl_promotion_leaguer_type:
                    FenxiaoGetCashActivity.this.startActivityForResult(new Intent(FenxiaoGetCashActivity.this, ManagerAccountActivity.class), SELECT_ACCOUNT_REQUEST_CODE);
                    break;
                case R.id.bt_sure_cash:
                    //this.startActivity(new Intent(this,IntegralCashHistoryActivity.class));
                    submitIntegralToCash();
                    break;
            }
        }
    };

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        myStoreId = getIntent().getStringExtra("myStoreId");
        super.initDatas(savedInstanceState);
    }

    private void iniFenxiaoInfo() {
        progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoAcountTixianIn(TAG, myStoreId, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {

                ViewUtils.dismissDialog(FenxiaoGetCashActivity.this, progressDialog);
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    getCash = GsonUtil.GsonToBean(jsonObj.optString("data"), GetCash.class);
                    if(getCash.account_info!=null) {
                        defaultAccount = getCash.account_info;
                        mActivityFenxiaoGetCashBinding.tvSelectAccount.setText(defaultAccount.receiver);
                    }
                    mMaxAmount = getCash.fenxiao_tixian_max_amount;
                    mActivityFenxiaoGetCashBinding.tvGetCash.setText("￥" + mMaxAmount);
                    mActivityFenxiaoGetCashBinding.etIntegralCrash.setHint("至少" + getCash.fenxiao_tixian_min_amount + "元起");
                    Log.e(TAG, getCash.toString());

                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(), "获取数据异常");
                }

            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastDouble(getBaseContext(),msg);
                ViewUtils.dismissDialog(FenxiaoGetCashActivity.this, progressDialog);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SELECT_ACCOUNT_REQUEST_CODE && data != null) {
            defaultAccount = data.getParcelableExtra(ManagerAccountActivity.SELECT_ACCOUNT_RESULT);
            if (defaultAccount != null) {
                mActivityFenxiaoGetCashBinding.tvSelectAccount.setText(defaultAccount.receiver);
            }
        }
    }


    private void submitIntegralToCash() {

        if (TextUtils.isEmpty(mActivityFenxiaoGetCashBinding.etIntegralCrash.getText())) {
            ToastUtils.showToastLong(getBaseContext(), "提现金额不能为空！");
            return;
        }

        double commission = Double.parseDouble(mActivityFenxiaoGetCashBinding.etIntegralCrash.getText().toString());

        if (commission < getCash.fenxiao_tixian_min_amount) {
            ToastUtils.showToastLong(getBaseContext(), "提现金额不能小于可提现最小于额！");
            return;
        }

        if (commission > getCash.fenxiao_tixian_max_amount) {
            ToastUtils.showToastLong(getBaseContext(), "提现金额不能大于可提现余额！");
            return;
        }

        if (defaultAccount == null) {
            ToastUtils.showToastLong(getBaseContext(), "提现账户不能为空！");
            return;
        }
        final String tixian_commission = mActivityFenxiaoGetCashBinding.etIntegralCrash.getText().toString().trim();
        String id = defaultAccount.id;
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
        final String mTixianCommission = df.format(Double.parseDouble(tixian_commission));
        progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), false);
        MallApplication.getInstance().getModel(AppModel.class).postFenxiaoAcountTixian(TAG, myStoreId, Integer.parseInt(id), Double.parseDouble(mTixianCommission), new EntityCallBack<String>(new TypeToken<String>() {
        }) {

            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(FenxiaoGetCashActivity.this, progressDialog);
                parseCashMoney(mTixianCommission);
                ToastUtils.showToastLong(getBaseContext(), "提现申请提交成功");
                FenxiaoGetCashActivity.this.finish();
//                Log.e(TAG,resp);
//                if(resp == null) return ;
//                try {
//                    JSONObject jsonObj=new JSONObject(resp);
//                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
//                    if(null!=payH5){
//                        readyToPay(payH5);
//                    }
//                } catch (JSONException e) {
//                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
//                }


            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(FenxiaoGetCashActivity.this, progressDialog);
                ToastUtils.showToastLong(getBaseContext(), msg);
            }
        });
    }

    //计算所剩余额
    private void parseCashMoney(String tixianCommission) {
        double mTixianCommission = Double.parseDouble(tixianCommission);
        GetCashMoneyEven cashMoneyEven = new GetCashMoneyEven();
        boolean isHavenBalance = false;
        double cashMoney = (mMaxAmount - mTixianCommission);
        if (cashMoney > 0) {
            isHavenBalance = true;
            cashMoneyEven.cashMoney = cashMoney;
        } else {
            cashMoneyEven.cashMoney = 0.0;
        }
        cashMoneyEven.isHavenBalance = isHavenBalance;
        EventBus.getDefault().post(cashMoneyEven);
    }


    @Override
    protected void onDestroy() {
        ViewUtils.dismissDialog(FenxiaoGetCashActivity.this, progressDialog);
        super.onDestroy();
    }
}
