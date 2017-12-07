package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.Bank;
import com.cloud.tao.bean.etc.BankCard;
import com.cloud.tao.bean.etc.BankCardSet;
import com.cloud.tao.bean.etc.BankWay;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.databinding.ActivityAddAccountBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.net.model.membermodel.req.AddAccountReq;
import com.cloud.tao.ui.widget.SelectCityWidget;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.ui.widget.bean.RegionInfo;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gezi-pc on 2016/10/1.
 */

public class AddAccountActivity extends BaseNavBackActivity {


    private static final String TAG = AddAccountActivity.class.getSimpleName();
    ActivityAddAccountBinding mActivityAddAccountBinding;
    private SelectSingleWidget selectSingleWidget;
    private SelectSingleWidget selectSingleBankWay;
    private PayWay selectPayWay;
    private String selectBankWay;
    private RegionInfo selectCityInfo;
    private SelectCityWidget selectCityWidget;
    private static HashMap<String,Bank> bankMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityAddAccountBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        setNavDefaultBack(mActivityAddAccountBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityAddAccountBinding.loadding.showLoadSuccess();
        mActivityAddAccountBinding.btAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccount();
            }
        });
        initSelectPayWay();
        initSelectBankWay();
        initBankWay();
        initCityWidget();
        //initBankData();
    }


    private void initBankWay() {
        mActivityAddAccountBinding.rlOpenBank.setVisibility(View.VISIBLE);
        mActivityAddAccountBinding.rlOpenCity.setVisibility(View.VISIBLE);
        selectPayWay = PayWay.accountPayways.get(2);
        mActivityAddAccountBinding.tvPayWay.setText(selectPayWay.name);
    }

    private void initBankData() {
        if(bankMap.isEmpty()) {
            List<Bank> banks = GsonUtil.fromJsonList(readRawCityFile(), Bank.class);
            //经过测试Map在，这种场景下比List性能更好。
            for(Bank bank:banks){
                bankMap.put(bank.bin,bank);
            }
        }
        mActivityAddAccountBinding.etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable number) {
                //s:变化后的所有字符
                //ToastUtils.showToastShort(getApplicationContext(),"number:"+number.toString());
                if(number.toString().length()>=4&&number.toString().length()<=9){
                    if(bankMap.get(number.toString())!=null){
                        //Toast.makeText(getApplicationContext(), "number:"+number.toString()+";name:"+bank.bankName, Toast.LENGTH_SHORT).show();
                        mActivityAddAccountBinding.etOpenBank.getText().clear();
                        mActivityAddAccountBinding.etOpenBank.setText(bankMap.get(number.toString()).bankName);
                    }
                }
                if(number.toString().length()==0){
                    mActivityAddAccountBinding.etOpenBank.getText().clear();
                }

            }
        });
    }


    private void addAccount() {
        if (checkEmpty()) {
            String receiver = mActivityAddAccountBinding.etPayee.getText().toString();
            String type = selectPayWay.id + "";
            String account = mActivityAddAccountBinding.etAgainAccount.getText().toString();
            String opening_bank_province = selectCityInfo.province;
            String opening_bank_city = selectCityInfo.city;
            String opening_bank_sub = mActivityAddAccountBinding.etOpenBank.getText().toString();
            int is_default = (mActivityAddAccountBinding.cbIsDefault.isChecked() == true ? 1 : 0);

            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
            AddAccountReq req = new AddAccountReq();
            req.receiver= receiver;
            req.type= type;
            req.account= account;
            req.opening_bank= selectBankWay;
            req.is_default= is_default;
            req.opening_bank_city = opening_bank_city;
            req.opening_bank_province = opening_bank_province;
            req.opening_bank_sub = opening_bank_sub;
            MallApplication.getInstance().getModel(MemberModel.class).addAccount(TAG, req, new EntityCallBack<String>(new TypeToken<String>() {
            }) {
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(AddAccountActivity.this, progressDialog);
                    AddAccountActivity.this.finish();
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ToastUtils.showToastShort(getBaseContext(), msg);
                    ViewUtils.dismissDialog(AddAccountActivity.this, progressDialog);
                }
            });
        }

    }

    private boolean checkEmpty() {
        if (TextUtils.isEmpty(mActivityAddAccountBinding.etPayee.getText())) {
            ToastUtils.showToastLong(this, "收款人不能为空！");
            return false;
        }

        if (selectPayWay == null) {
            ToastUtils.showToastLong(this, "提现方式不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(mActivityAddAccountBinding.etAccount.getText())) {
            ToastUtils.showToastLong(this, "卡号/账号不能为空！");
            return false;
        }

        if (TextUtils.isEmpty(selectBankWay)) {
            ToastUtils.showToastLong(this, "银行名称不能为空！");
            return false;
        }


        if (TextUtils.isEmpty(mActivityAddAccountBinding.etAgainAccount.getText()) ||
                !mActivityAddAccountBinding.etAgainAccount.getText().toString().equals(
                        mActivityAddAccountBinding.etAccount.getText().toString())) {
            ToastUtils.showToastLong(this, "两次输入的卡号/账号不一致！");
            return false;
        }

        if (selectPayWay != null && selectPayWay.id == 3) {
            if (TextUtils.isEmpty(mActivityAddAccountBinding.tvOpenCity.getText())) {
                ToastUtils.showToastLong(this, "开户城市不能为空！");
                return false;
            }
            if (TextUtils.isEmpty(mActivityAddAccountBinding.etOpenBank.getText())) {
                ToastUtils.showToastLong(this, "开户支行不能为空！");
                return false;
            }
        }

        return true;
    }


    private void initSelectBankWay() {


        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getBankList(TAG,new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(AddAccountActivity.this, progressDialog);
                Log.e(TAG,msg+";"+resp);
                if(resp == null) return ;
                try {

                    JSONObject jsonObj=new JSONObject(resp);
                    BankWay bankWay= GsonUtil.GsonToBean(jsonObj.optString("data"), BankWay.class);
                    fillBankWayData(bankWay);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(AddAccountActivity.this,"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getBaseContext(), msg);
                ViewUtils.dismissDialog(AddAccountActivity.this, progressDialog);
            }
        });



    }

    private void fillBankWayData(BankWay bankWay) {
        selectSingleBankWay = new SelectSingleWidget(this, bankWay.bank_list, bankWay.bank_list, "选择银行名称", mActivityAddAccountBinding.vMasker);
        selectSingleBankWay.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<String>() {
            @Override
            public void OnSelectSingleCallBack(String result) {
                mActivityAddAccountBinding.tvOpenBankWay.setText(result);
                selectBankWay = result;
            }
        });

        //点击弹出选项选择器
        mActivityAddAccountBinding.rlOpenBankWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSingleBankWay.showWidget();
            }
        });

    }


    private void initSelectPayWay() {
        ArrayList<String> datas = new ArrayList<>();
        ArrayList<PayWay> pays = new ArrayList<>();
        for (PayWay pay : PayWay.accountPayways) {
            if(pay.id==3) {
                datas.add(pay.name);
                pays.add(pay);
            }
        }

        selectSingleWidget = new SelectSingleWidget(this, pays, datas, "选择提现方式", mActivityAddAccountBinding.vMasker);
        selectSingleWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<PayWay>() {
            @Override
            public void OnSelectSingleCallBack(PayWay result) {
                mActivityAddAccountBinding.tvPayWay.setText(result.name);
                selectPayWay = result;
                if (selectPayWay.id == 3) {
                    mActivityAddAccountBinding.rlOpenBank.setVisibility(View.VISIBLE);
                    mActivityAddAccountBinding.rlOpenCity.setVisibility(View.VISIBLE);
                } else {
                    mActivityAddAccountBinding.rlOpenBank.setVisibility(View.GONE);
                    mActivityAddAccountBinding.rlOpenCity.setVisibility(View.GONE);
                }
            }
        });

        //点击弹出选项选择器
        mActivityAddAccountBinding.rlCashWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSingleWidget.showWidget();
            }
        });
    }


    /**
     * 初始化选择城市控件
     */
    private void initCityWidget() {


        selectCityWidget = new SelectCityWidget(this, mActivityAddAccountBinding.vMasker);
        selectCityWidget.setOnSelectOptionsCallBack(new SelectCityWidget.OnSelectOptionsCallBack() {
            @Override
            public void OnSelectCityCallBack(RegionInfo info) {
                selectCityInfo = info;
                StringBuffer selectcity = new StringBuffer();
                selectcity.append(info.province);
                if (!TextUtils.isEmpty(info.district)) {
                    selectcity.append("省" + info.city + "市");
                    selectcity.append(info.district);
                } else {
                    selectcity.append("市"+info.city );
                }
                mActivityAddAccountBinding.tvOpenCity.setText(selectcity.toString());
            }
        });

        //点击弹出选项选择器
        mActivityAddAccountBinding.rlOpenCity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectCityWidget.showCityWidget();
            }
        });

    }


    private String readRawCityFile()
    {
        String content = null;
        Resources resources=getResources();
        InputStream is=null;
        try{
            is=resources.openRawResource(R.raw.bank_data);
            byte buffer[]=new byte[is.available()];
            is.read(buffer);
            content=new String(buffer);
        }
        catch(IOException e)
        {
            Log.e(TAG, "write file",e);
        }
        finally
        {
            if(is!=null)
            {
                try{
                    is.close();
                }catch(IOException e)
                {
                    Log.e(TAG, "close file",e);
                }
            }
        }
        return content;
    }
}
