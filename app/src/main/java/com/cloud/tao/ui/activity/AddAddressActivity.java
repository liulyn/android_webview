package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.AddressList;
import com.cloud.tao.databinding.ActivityAddAddressBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.SelectCityWidget;
import com.cloud.tao.ui.widget.bean.RegionInfo;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;


/**
 * Created by gezi-pc on 2016/10/1.
 */

public class AddAddressActivity extends BaseNavBackActivity {


    public static final String EDIT_ADDRESS = "edit_address";
    private AddressList.Address address;

    private static final String TAG = "AddAddressActivity";
    ActivityAddAddressBinding mActivityAddAddressBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityAddAddressBinding =  DataBindingUtil.setContentView(this, R.layout.activity_add_address);
        setNavDefaultBack(mActivityAddAddressBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityAddAddressBinding.loadding.showLoadSuccess();
        initDatas(savedInstanceState);
        mActivityAddAddressBinding.btSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAddress();
            }
        });
        initCityWidget();
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        Object obj  = this.getIntent().getSerializableExtra(AddAddressActivity.EDIT_ADDRESS);
        if(obj!=null) {
            address = (AddressList.Address) obj;
            mActivityAddAddressBinding.etConsignee.setText(address.consignee);
            mActivityAddAddressBinding.etPhone.setText(address.mobilephone);
            mActivityAddAddressBinding.tvSelectCity.setText(address.province +" "+ address.city +" " + (address.area==null?"":address.area));

            regionInfo = new RegionInfo();
            regionInfo.province = address.province;
            regionInfo.city = address.city;
            regionInfo.district = address.area;
            mActivityAddAddressBinding.etStreet.setText(address.street);
            mActivityAddAddressBinding.cbIsDefault.setChecked(address.is_default==0?false:true);
        }
    }

    private void submitAddress() {
        if(checkNotEmpty()) {
            String consignee = mActivityAddAddressBinding.etConsignee.getText().toString();
            String phone = mActivityAddAddressBinding.etPhone.getText().toString();
            String street = mActivityAddAddressBinding.etStreet.getText().toString();
            short is_default = (short) (mActivityAddAddressBinding.cbIsDefault.isChecked()==true?1:0);
            String dev_id = null;
            if(address!=null){
                dev_id = address.dev_id;
            }
            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);


            MallApplication.getInstance().getModel(MemberModel.class).editAddress(TAG,dev_id,regionInfo.province,regionInfo.city,regionInfo.district,street,consignee,phone,is_default, new EntityCallBack<String>(new TypeToken<String>(){}){
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(AddAddressActivity.this, progressDialog);
                    AddAddressActivity.this.finish();
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(AddAddressActivity.this, progressDialog);
                    ToastUtils.showToastShort(AddAddressActivity.this,msg);
                    Log.i(TAG,msg);
                }
            });
        }
    }


    private boolean checkNotEmpty(){
        if(TextUtils.isEmpty(mActivityAddAddressBinding.etConsignee.getText())){
            ToastUtils.showToastLong(this,"收货人不能为空！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityAddAddressBinding.etPhone.getText())){
            ToastUtils.showToastLong(this,"手机号码不能为空！");
            return false;
        }


        if(TextUtils.isEmpty(mActivityAddAddressBinding.tvSelectCity.getText())){
            ToastUtils.showToastLong(this,"城市不能为空！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityAddAddressBinding.etStreet.getText())){
            ToastUtils.showToastLong(this,"详细地址不能为空！");
            return false;
        }


        return true;
    }


    private RegionInfo regionInfo;
    private SelectCityWidget selectCityWidget;

    /**
     * 初始化选择城市控件
     */
    private void initCityWidget(){


        selectCityWidget = new SelectCityWidget(this,mActivityAddAddressBinding.vMasker);
        selectCityWidget.setOnSelectOptionsCallBack(new SelectCityWidget.OnSelectOptionsCallBack() {
            @Override
            public void OnSelectCityCallBack(RegionInfo info) {

                regionInfo = info;
                mActivityAddAddressBinding.tvSelectCity.setText(info.province+" "+info.city+" "+(info.district==null?"":info.district));
            }
        });

        //点击弹出选项选择器
        mActivityAddAddressBinding.rlCity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectCityWidget.showCityWidget();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(selectCityWidget.isShowing()){
                selectCityWidget.dismiss();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

}
