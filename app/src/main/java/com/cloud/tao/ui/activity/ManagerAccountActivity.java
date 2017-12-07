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
import com.cloud.tao.bean.etc.Account;
import com.cloud.tao.bean.etc.AccountList;
import com.cloud.tao.databinding.ActivityManagerAccountBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.base.BaseDelReq;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/2.
 */

public class ManagerAccountActivity extends BaseNavBackActivity implements View.OnClickListener{


    private static final String TAG = ManagerAccountActivity.class.getSimpleName();
    ActivityManagerAccountBinding mActivityManagerAccountBinding;
    public static final String SELECT_ACCOUNT_RESULT = "select_account_result";
    private Account selectAccount;
    private ListView lvManagerAccount;
    private ArrayList<Account> accounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityManagerAccountBinding =  DataBindingUtil.setContentView(this, R.layout.activity_manager_account);
        setNavDefaultBack(mActivityManagerAccountBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityManagerAccountBinding.loadding.showLoadSuccess();
        mActivityManagerAccountBinding.btAddAccount.setOnClickListener(this);
        lvManagerAccount = mActivityManagerAccountBinding.lvManagerAccount;

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

    protected void initData() {

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).getAccountList(TAG, new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(ManagerAccountActivity.this, progressDialog);

                Log.e(TAG,resp.toString());
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    AccountList list = GsonUtil.GsonToBean(jsonObj.optString("data"),AccountList.class);
                    accounts.clear();
                    if(list.account_list!=null&&list.account_list.size()>0) {
                        accounts.addAll(list.account_list);
                    }
                    fillData();
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }

            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ManagerAccountActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });
    }

    RadioButton currentRb;
    private CommonAdapter commonAdapter;

    private void fillData() {
        if(commonAdapter==null){
            commonAdapter = new CommonAdapter<Account>(this,accounts,R.layout.activity_manager_account_item) {
                @Override
                protected void convert(ViewHolder vh,final Account item) {
                    TextView username =(TextView) vh.getConvertView().findViewById(R.id.tv_account_username);
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_account_number)).setText(item.account);
                    username.setText(item.receiver+"（"+item.type_name+"）");

                    RadioButton rb = (RadioButton)vh.getConvertView().findViewById(R.id.rb_select_account);
                    rb.setTag(item);
                    if(item.is_default==1){
                        rb.setChecked(true);
                        currentRb = rb;
                    }else{
                        rb.setChecked(false);
                    }
                    rb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(currentRb!=null){
                                currentRb.setChecked(false);
                                RadioButton selectRb =((RadioButton)v);
                                selectRb.setChecked(true);
                                setAccountDefault(selectRb);
                            }else{
                                currentRb.setChecked(true);
                                setAccountDefault(currentRb);
                            }
                            currentRb = (RadioButton)v;
                            selectAccount = (Account) currentRb.getTag();

                        }
                    });

                    vh.getConvertView().findViewById(R.id.iv_del_account).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MallApplication.getInstance().getModel(MemberModel.class).delAccount(TAG, item.id, new EntityCallBack<String>(new TypeToken<String>(){}) {
                                @Override
                                public void onSuccess(int code, String msg, String resp) {
                                    accounts.remove(item);
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFail(int code, Exception e, String msg) {
                                    ToastUtils.showToastShort(getBaseContext(),msg);
                                }
                            });
                        }
                    });

                    vh.getConvertView().findViewById(R.id.ll_select_account).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectAccount = item;
                            setResult();
                        }
                    });
                }

            };
            lvManagerAccount.setAdapter(commonAdapter);
        }else{
            commonAdapter.notifyDataSetChanged();
        }

    }

    private void setResult(){
        if(selectAccount!=null) {
            Intent mIntent = new Intent();
            mIntent.putExtra(ManagerAccountActivity.SELECT_ACCOUNT_RESULT, selectAccount);
            this.setResult(IntegralCashActivity.SELECT_ACCOUNT_REQUEST_CODE, mIntent);
            this.finish();
        }
    }


    private void setAccountDefault(RadioButton currentRb) {

        BaseDelReq req = new BaseDelReq();
        if(currentRb.getTag()!=null && currentRb.getTag() instanceof Account ){
            req.id = ((Account)currentRb.getTag()).id;
        }
        MallApplication.getInstance().getModel(MemberModel.class).setAccountDefault(TAG,req, new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {

            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_add_account:
                this.startActivity(new Intent(this,AddAccountActivity.class));
                break;
        }


    }
}
