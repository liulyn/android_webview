package com.cloud.tao.ui.activity;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.ScoreBack;
import com.cloud.tao.bean.etc.VipCardExplain;
import com.cloud.tao.databinding.ActivityConsumeInfoBinding;
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
 * Created by gezi-pc on 2016/9/30.
 */

public class ConsumeInfoActivity extends BaseNavBackActivity  {

    private static final String TAG = ConsumeInfoActivity.class.getSimpleName();
    ActivityConsumeInfoBinding mActivityConsumeInfoBinding;
    public final static String IN_ACTIVITY_FLAG = "in_activity_flag";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
        mActivityConsumeInfoBinding =  DataBindingUtil.setContentView(this, R.layout.activity_consume_info);
            setNavDefaultBack(mActivityConsumeInfoBinding.tb);
            super.onCreate(savedInstanceState);
            mActivityConsumeInfoBinding.loadding.showLoadSuccess();
        initDatas(savedInstanceState);

        }


    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        if(getIntent().getStringExtra(IN_ACTIVITY_FLAG).equals("score_back")){
            getScoreBack();
        }else{
            getVipCardExplain();
        }
    }

    /**
     *云豆返还信息
     */
    private void getScoreBack() {
        mActivityConsumeInfoBinding.tvConsumeTitle.setText("云豆返还信息");

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).getScoreBack(TAG, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(ConsumeInfoActivity.this, progressDialog);
                JSONObject jsonObj= null;
                try {
                    jsonObj = new JSONObject(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ScoreBack scoreBack= GsonUtil.GsonToBean(jsonObj.optString("data"),ScoreBack.class);
                String text  = MessageFormat.format(getResources().getString(R.string.consume_info_tv_integral_return_content).toString(),scoreBack.score_back_percent,scoreBack.score_back,scoreBack.total_back_score);
                mActivityConsumeInfoBinding.tvConsumeContent.setText(text);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ConsumeInfoActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });
    }

    /**
     * 权益说明信息
     */
    private void getVipCardExplain() {

        mActivityConsumeInfoBinding.tvConsumeTitle.setText("会员权益说明");
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getVipCardExplain(TAG, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(ConsumeInfoActivity.this, progressDialog);
                JSONObject jsonObj= null;
                try {
                    jsonObj = new JSONObject(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                VipCardExplain vipCardExplain= GsonUtil.GsonToBean(jsonObj.optString("data"),VipCardExplain.class);
                String text  = MessageFormat.format(getResources().getString(R.string.consume_info_tv_integral_explain_content).toString(),vipCardExplain.score_add_ratio,vipCardExplain.score_exchange_ratio);
                mActivityConsumeInfoBinding.tvConsumeContent.setText(text);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(ConsumeInfoActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });
    }
}
