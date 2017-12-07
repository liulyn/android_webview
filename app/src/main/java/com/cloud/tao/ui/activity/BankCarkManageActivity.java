package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.BankCard;
import com.cloud.tao.bean.etc.BankCardSet;
import com.cloud.tao.databinding.ActivityBankCardManageBinding;
import com.cloud.tao.databinding.ActivityPayStandBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.adapter.etc.BankCardAdapter;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * sunny created at 2016/10/28
 * des: 银行卡管理
 */

public class BankCarkManageActivity extends BaseNavBackActivity {

    private static final String TAG = "BankCarkManageActivity";
    ActivityBankCardManageBinding mBankCardManageBinding;
    private  ArrayList<BankCard> datas = new ArrayList<>();
    private BankCardAdapter bankCardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mBankCardManageBinding= DataBindingUtil.setContentView(this, R.layout.activity_bank_card_manage);
        setNavDefaultBack(mBankCardManageBinding.tb);
        
        mBankCardManageBinding.loadding.showLoadSuccess();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        initData();
        super.onStart();
    }

    protected void initData() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getBankCard(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(BankCarkManageActivity.this, progressDialog);
                Log.e(TAG,msg+";"+resp);
                if(resp == null) return ;
                try {

                    JSONObject jsonObj=new JSONObject(resp);
                    BankCardSet bankCardSet= GsonUtil.GsonToBean(jsonObj.optString("data"), BankCardSet.class);
                    datas.clear();
                    if(bankCardSet.card_set!=null&&bankCardSet.card_set.size()>0) {
                        datas.addAll(bankCardSet.card_set);
                    }
                    BankCard bc = new BankCard();
                    bc.type = 1;
                    datas.add(bc);
                    fillData();
                } catch (JSONException e) {
                    ToastUtils.showToastShort(BankCarkManageActivity.this,"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(BankCarkManageActivity.this, progressDialog);
                ToastUtils.showToastShort(BankCarkManageActivity.this, msg);
                Log.e(TAG,msg);
            }
        });

    }

    private void fillData() {
        if(bankCardAdapter==null){
        bankCardAdapter = new BankCardAdapter(this,datas);
        bankCardAdapter.setOnClickBankCardCallBack(new BankCardAdapter.OnClickBankCardCallBack() {
            @Override
            public void onClickAddToBankCard() {
                Intent intent = new Intent(getBaseContext(),AddBankCardActivity.class);
                BankCarkManageActivity.this.startActivity(intent);
            }


            @Override
            public void onClickDelBankCard(final BankCard item) {
                final ProgressDialog progressDialog = VLDialog.showProgressDialog(BankCarkManageActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
                MallApplication.getInstance().getModel(MemberModel.class).delBankCard(TAG, item.client_transfer_card_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        ViewUtils.dismissDialog(BankCarkManageActivity.this, progressDialog);
                        datas.remove(item);
                        bankCardAdapter.notifyDataSetChanged();
                        ToastUtils.showToastShort(BankCarkManageActivity.this, msg);
                        Log.e(TAG,msg);
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ViewUtils.dismissDialog(BankCarkManageActivity.this, progressDialog);
                        ToastUtils.showToastShort(BankCarkManageActivity.this, msg);
                        Log.e(TAG,msg);
                    }
                });

            }
        });
        mBankCardManageBinding.lvBankCard.setAdapter(bankCardAdapter);
    }else{
        bankCardAdapter.notifyDataSetChanged();
        }
    }

}

