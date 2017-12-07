package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.FenxiaoInfo;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoUpdateInfoBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/10/25
 * des: 修改创业信息
 */
public class FenxiaoUpdateInfoActivity extends BaseNavBackActivity{

    private static final String TAG="FenxiaoUpdateInfoActivity";
    ActivityFenxiaoUpdateInfoBinding mFenxiaoUpdateInfoBinding;
    String mMyStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoUpdateInfoBinding=DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_update_info);
        setNavDefaultBack(mFenxiaoUpdateInfoBinding.tb);
        mFenxiaoUpdateInfoBinding.btFenxiaoUpdate.setOnClickListener(mOnInToAttrClickListener);
        mMyStoreId=getIntent().getExtras().getString("myStoreId");
        super.onCreate(savedInstanceState);
        getFenxiaoInfo();
    }

    private NoDoubleClickListener mOnInToAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.bt_fenxiao_update:
                    updateFenxiaoInfo();
                    break;
                default:
                    break;
            }
        }
    };

    private void getFenxiaoInfo() {
            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_loading),true);
            MallApplication.getInstance().getModel(AppModel.class).getFenxiaoInfo(TAG,mMyStoreId,new EntityCallBack<String>(new TypeToken<String>(){}){
                @Override
                public void onSuccess(int code,String msg,String resp) {
                    ViewUtils.dismissDialog(FenxiaoUpdateInfoActivity.this,progressDialog);
                    if(resp == null) return ;
                    try {
                        JSONObject jsonObj=new JSONObject(resp);
                        FenxiaoInfo mFenxiaoInfo= GsonUtil.GsonToBean(jsonObj.optJSONObject("data").optString("fenxiao_info"),FenxiaoInfo.class);
                        parseFenxiaoInfo(mFenxiaoInfo);
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,"获取数据异常");
                    }
                }
                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(FenxiaoUpdateInfoActivity.this,progressDialog);
                    ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,msg);
                }

                @Override
                public void onAfter(int id) {
                }
            });
    }

    private boolean validateInput(String mNickName,String storeName){
        boolean isCheck=true;
        if(TextUtils.isEmpty(mNickName)){
            ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,"请输入姓名");
            isCheck=false;
        }
        if(TextUtils.isEmpty(storeName)){
            ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,"请输入商店名称");
            isCheck=false;
        }
        return isCheck;
    }

    private void updateFenxiaoInfo() {
        final String mNickName=mFenxiaoUpdateInfoBinding.etFenxiaoUpdateName.getText().toString().trim();
        String mStoreName=mFenxiaoUpdateInfoBinding.etFenxiaoUpdateStoreName.getText().toString().trim();
        if(!validateInput(mNickName,mStoreName)){
            return;
        }
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(R.string.dialog_common_title),getString(R.string.dialog_common_doing),false);
        MallApplication.getInstance().getModel(AppModel.class).updateFenxiaoInfo(TAG,mMyStoreId,mNickName,mStoreName,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                ViewUtils.dismissDialog(FenxiaoUpdateInfoActivity.this,progressDialog);
                ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,"保存成功");
                Intent intent=new Intent();
                intent.putExtra("nickName",mNickName);
                setResult(RESULT_OK,intent);
                FenxiaoUpdateInfoActivity.this.finish();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(FenxiaoUpdateInfoActivity.this,progressDialog);
                ToastUtils.showToastShort(FenxiaoUpdateInfoActivity.this,msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    public void parseFenxiaoInfo(FenxiaoInfo fenxiaoDataInfo){
        mFenxiaoUpdateInfoBinding.etFenxiaoUpdateName.setText(fenxiaoDataInfo.nick_name);
        mFenxiaoUpdateInfoBinding.etFenxiaoUpdateStoreName.setText(fenxiaoDataInfo.fenxiao_store_name);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }
}
