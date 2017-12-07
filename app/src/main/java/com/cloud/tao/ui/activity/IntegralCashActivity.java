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
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.TakeCashObj;
import com.cloud.tao.databinding.ActivityIntegralCashBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

/**
 * Created by gezi-pc on 2016/10/1.
 */

public class IntegralCashActivity extends BaseNavBackActivity implements View.OnClickListener{


    private static final String TAG = IntegralCashActivity.class.getSimpleName();
    ActivityIntegralCashBinding mActivityIntegralCashBinding;
    private TakeCashObj takeCashObj;
    private Account defaultAccount;
    public static final int SELECT_ACCOUNT_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityIntegralCashBinding =  DataBindingUtil.setContentView(this, R.layout.activity_integral_cash);
        setNavDefaultBack(mActivityIntegralCashBinding.tb);
        mActivityIntegralCashBinding.tb.setOnRightNavClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntegralCashActivity.this.startActivity(new Intent(IntegralCashActivity.this,IntegralCashHistoryActivity.class));
            }
        });
        super.onCreate(savedInstanceState);
        mActivityIntegralCashBinding.loadding.showLoadSuccess();
        mActivityIntegralCashBinding.rlPromotionLeaguerType.setOnClickListener(this);
        mActivityIntegralCashBinding.btSureCash.setOnClickListener(this);
        initDatas(savedInstanceState);
        }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getScoreApply(TAG, new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(IntegralCashActivity.this, progressDialog);
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    takeCashObj= GsonUtil.GsonToBean(jsonObj.optString("data"),TakeCashObj.class);
                    if(takeCashObj==null||takeCashObj.tixian_set==null){
                        ToastUtils.showToastShort(getBaseContext(),"暂无可提现云豆!");
                        mActivityIntegralCashBinding.tvConsumeContent.setVisibility(View.GONE);
                        return;
                    }
                    if(takeCashObj.account!=null) {
                        for (Account item : takeCashObj.account) {
                            if (item.is_default == 1) {
                                defaultAccount = item;
                                mActivityIntegralCashBinding.tvSelectAccount.setText(defaultAccount.receiver);
                                break;
                            }
                        }
                    }
                    String text  = MessageFormat.format(getResources().getString(R.string.integral_cash_tv_describe).toString(),takeCashObj.tixian_set.score_apply_max,takeCashObj.tixian_set.score_exchange_ratio,takeCashObj.tixian_set.score_apply_limit,takeCashObj.tixian_set.score_apply_poundage_percent);

                    mActivityIntegralCashBinding.tvConsumeContent.setText(text);
                    Log.e(TAG,takeCashObj.tixian_set.toString());

                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }

            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(IntegralCashActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==SELECT_ACCOUNT_REQUEST_CODE&&data!=null){
            defaultAccount = data.getParcelableExtra(ManagerAccountActivity.SELECT_ACCOUNT_RESULT);
            if(defaultAccount!=null) {
                mActivityIntegralCashBinding.tvSelectAccount.setText(defaultAccount.receiver);
            }
        }
    }


    private void submitIntegralToCash(){

        if(TextUtils.isEmpty(mActivityIntegralCashBinding.etIntegralCrash.getText())){
            ToastUtils.showToastLong(getBaseContext(),"提现云豆不能为空！");
            return;
        }

        int commission = Integer.parseInt(mActivityIntegralCashBinding.etIntegralCrash.getText().toString());
        int score_apply_max =  Integer.parseInt(takeCashObj.tixian_set.score_apply_max);
        if(commission>score_apply_max){
            ToastUtils.showToastLong(getBaseContext(),"提现云豆不能大于账户云豆！");
            return;
        }

        if(defaultAccount==null){
            ToastUtils.showToastLong(getBaseContext(),"提现账户不能为空！");
            return;
        }
        String tixian_commission = mActivityIntegralCashBinding.etIntegralCrash.getText().toString();
        String id = defaultAccount.id;

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).submitScoreApplyConfirm(TAG, tixian_commission, id, new EntityCallBack<String>(new TypeToken<String>(){}){


            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(IntegralCashActivity.this, progressDialog);
                ToastUtils.showToastLong(getBaseContext(),msg);
                IntegralCashActivity.this.finish();
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
                ViewUtils.dismissDialog(IntegralCashActivity.this, progressDialog);
                ToastUtils.showToastLong(getBaseContext(),msg);
            }
        });
    }


    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(this,PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM,payH5.url);
        startActivity(intent);
        IntegralCashActivity.this.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_promotion_leaguer_type:
                this.startActivityForResult(new Intent(this,ManagerAccountActivity.class),SELECT_ACCOUNT_REQUEST_CODE);
                break;
            case R.id.bt_sure_cash:
                //this.startActivity(new Intent(this,IntegralCashHistoryActivity.class));
                submitIntegralToCash();
                break;
        }

    }



}
