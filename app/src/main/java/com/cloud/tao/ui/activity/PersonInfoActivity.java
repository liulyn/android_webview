package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.VipCardInfo;
import com.cloud.tao.databinding.ActivityEditPersionInfoBinding;
import com.cloud.tao.databinding.ActivityPersonInfoBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * 编辑个人信息
 *
 */
public class PersonInfoActivity extends BaseNavBackActivity {


    private static final String TAG = "PersonInfoActivity";
    ActivityPersonInfoBinding mPersionInfoBinding;
    private VipCardInfo vipCardInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPersionInfoBinding =  DataBindingUtil.setContentView(this, R.layout.activity_person_info);
        setNavDefaultBack(mPersionInfoBinding.tb);
        super.onCreate(savedInstanceState);
        mPersionInfoBinding.loadding.showLoadSuccess();

        }


    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).getVipCardInfo(TAG, new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(PersonInfoActivity.this, progressDialog);
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    vipCardInfo= GsonUtil.GsonToBean(jsonObj.optString("data"),VipCardInfo.class);
                    Log.i(TAG,vipCardInfo.toString());
                    mPersionInfoBinding.tvDataRemainder.setText(vipCardInfo.balance+"");
                    mPersionInfoBinding.tvDataAvailableIntegral.setText(vipCardInfo.score_to_exchange+"");
                    mPersionInfoBinding.tvLevenName.setText(vipCardInfo.rank_name);
                    mPersionInfoBinding.tvDataScore.setText(vipCardInfo.score+"");
                    mPersionInfoBinding.tvDataVipCard.setText(vipCardInfo.member_card_num+"");
                    mPersionInfoBinding.tvDataPhoneNumber.setText(vipCardInfo.login_mobilephone);
                    mPersionInfoBinding.tvDataId.setText(vipCardInfo.id+"");
                    mPersionInfoBinding.tvDataUsername.setText( vipCardInfo.name);
                    mPersionInfoBinding.tvStayReturn.setText(vipCardInfo.remain_score+"");

                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }

            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PersonInfoActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });

    }



}

