package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.AddressList;
import com.cloud.tao.bean.etc.AddressObject;
import com.cloud.tao.databinding.ActivityManagerAddressBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gezi-pc on 2016/10/2.
 */

public class ManagerAddressActivity extends BaseNavBackActivity implements View.OnClickListener {


    private static final String TAG = "ManagerAddressActivity";
    public static int SELECT_ADDRESS_REQUEST_CODE = 0;
    ActivityManagerAddressBinding mActivityManagerAddressBinding;
    public static final String SELECT_ADDRESS_RESULT = "select_address_result";

    private ListView lvManagerAddress;
    private AddressList.Address selectAddress;
    private List<AddressList.Address> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityManagerAddressBinding = DataBindingUtil.setContentView(this, R.layout.activity_manager_address);
        setNavDefaultBack(mActivityManagerAddressBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityManagerAddressBinding.loadding.showLoadSuccess();
        mActivityManagerAddressBinding.btAddAddress.setOnClickListener(this);
        lvManagerAddress = mActivityManagerAddressBinding.lvManagerAddress;

    }


    private void initData() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getAddressList(TAG, new EntityCallBack<AddressObject>(new TypeToken<AddressObject>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, AddressObject resp) {
                addressList.clear();
                if (resp.data.address_list != null && resp.data.address_list.size() > 0) {
                    addressList.addAll(resp.data.address_list);
                }
                fillAddressDatas();
                ViewUtils.dismissDialog(ManagerAddressActivity.this, progressDialog);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ManagerAddressActivity.this, progressDialog);
                ToastUtils.showToastShort(ManagerAddressActivity.this, msg);
                Log.i(TAG, msg);
            }
        });

    }

    protected void setResult() {
        if (selectAddress != null && ManagerAddressActivity.SELECT_ADDRESS_REQUEST_CODE == 1) {
            Intent mIntent = new Intent();
            mIntent.putExtra(ManagerAddressActivity.SELECT_ADDRESS_RESULT, selectAddress);
            this.setResult(OrderPayActivity.SELECT_ADDRESS_REQUEST_CODE, mIntent);
            //this.finish();
        }

    }

    private RadioButton lastRb;
    private CommonAdapter commonAdapter;

    private void fillAddressDatas() {
        commonAdapter = new CommonAdapter<AddressList.Address>(this, addressList, R.layout.activity_manager_address_item) {
            @Override
            protected void convert(ViewHolder vh, final AddressList.Address item) {
                ((TextView) vh.getConvertView().findViewById(R.id.tv_username)).setText(item.consignee);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_phone_number)).setText(item.mobilephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
                ((TextView) vh.getConvertView().findViewById(R.id.tv_address)).setText(item.province + " " + item.city + " " + (item.area == null ? "" : item.area) + " " + item.street);
                vh.getConvertView().findViewById(R.id.ll_select_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectAddress = item;
                        setResult();
                    }
                });

                RadioButton rb = (RadioButton) vh.getConvertView().findViewById(R.id.rb_select_address);

                if (item.is_default == 1) {
                    rb.setChecked(true);
                    lastRb = rb;

                    selectAddress = item;
                    setResult();
                } else {
                    //rb.setBackgroundResource(R.drawable.rbtn_sex_selector_off);
                    rb.setChecked(false);
                }
                rb.setTag(item);
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (lastRb != v) {
                            if (lastRb != null) {
                                lastRb.setChecked(false);
                            }
                            ((RadioButton) v).setChecked(true);
                            final AddressList.Address address = (AddressList.Address) v.getTag();
                            final ProgressDialog progressDialog = VLDialog.showProgressDialog(ManagerAddressActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_doing), true);
                            MallApplication.getInstance().getModel(MemberModel.class).updateAddressDefault(TAG, item.dev_id, new EntityCallBack<String>(new TypeToken<String>() {
                            }) {
                                @Override
                                public void onSuccess(int code, String msg, String resp) {
                                    ViewUtils.dismissDialog(ManagerAddressActivity.this, progressDialog);
                                    selectAddress = address;
                                    setResult();
                                }

                                @Override
                                public void onFail(int code, Exception e, String msg) {
                                    ViewUtils.dismissDialog(ManagerAddressActivity.this, progressDialog);
                                    ToastUtils.showToastShort(ManagerAddressActivity.this, msg);
                                    Log.i(TAG, msg);
                                }
                            });


                            lastRb = (RadioButton) v;
                        }
                    }
                });
                vh.getConvertView().findViewById(R.id.iv_edit_address).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ManagerAddressActivity.this, AddAddressActivity.class);
                        intent.putExtra(AddAddressActivity.EDIT_ADDRESS, item);
                        ManagerAddressActivity.this.startActivity(intent);

                    }
                });
            }
        };
        lvManagerAddress.setAdapter(commonAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_address:
                this.startActivity(new Intent(this, AddAddressActivity.class));
                break;
        }


    }
}
