package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.GoodsDetail;
import com.cloud.tao.databinding.ActivityApplyRefundBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

/**
 * Created by gezi-pc on 2016/10/21.
 */

public class ApplyRefundActivity  extends BaseNavBackActivity implements View.OnClickListener{


    private static final String TAG = ApplyRefundActivity.class.getSimpleName();
    ActivityApplyRefundBinding mActivityApplyRefundBinding;
    public static final String APPLY_REFUND_ORDER_DETAIL_TAG ="apply_refund_order_detail_tag";
    private GoodsDetail goodsDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityApplyRefundBinding =  DataBindingUtil.setContentView(this, R.layout.activity_apply_refund);
        setNavDefaultBack(mActivityApplyRefundBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityApplyRefundBinding.loadding.showLoadSuccess();
        mActivityApplyRefundBinding.tvExplain.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initListener();
        initDatas(savedInstanceState);
    }

    private void initListener() {
        mActivityApplyRefundBinding.rlRefund.setOnClickListener(this);
        mActivityApplyRefundBinding.rlReturnGoods.setOnClickListener(this);
        mActivityApplyRefundBinding.rbRefund.setOnClickListener(this);
        mActivityApplyRefundBinding.rbReturnGoods.setOnClickListener(this);
        mActivityApplyRefundBinding.btSubmitApply.setOnClickListener(this);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        goodsDetail = getIntent().getParcelableExtra(ApplyRefundActivity.APPLY_REFUND_ORDER_DETAIL_TAG);
        Log.e(TAG,goodsDetail.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_refund:
            case R.id.rb_refund:
                changeSelectStyle(mActivityApplyRefundBinding.tvRefund,mActivityApplyRefundBinding.rbRefund);
                recoverySelectStyle(mActivityApplyRefundBinding.tvReturnGoods,mActivityApplyRefundBinding.rbReturnGoods);
                mActivityApplyRefundBinding.rlLogisticsCompanyName.setVisibility(View.GONE);
                mActivityApplyRefundBinding.rlLogisticsNumber.setVisibility(View.GONE);
                mActivityApplyRefundBinding.llLogisticsCompanyName.setVisibility(View.GONE);
                mActivityApplyRefundBinding.llLogisticsNumber.setVisibility(View.GONE);
                break;
            case R.id.rl_return_goods:
            case R.id.rb_return_goods:
                changeSelectStyle(mActivityApplyRefundBinding.tvReturnGoods,mActivityApplyRefundBinding.rbReturnGoods);
                recoverySelectStyle(mActivityApplyRefundBinding.tvRefund,mActivityApplyRefundBinding.rbRefund);
                mActivityApplyRefundBinding.rlLogisticsCompanyName.setVisibility(View.VISIBLE);
                mActivityApplyRefundBinding.rlLogisticsNumber.setVisibility(View.VISIBLE);
                mActivityApplyRefundBinding.llLogisticsCompanyName.setVisibility(View.VISIBLE);
                mActivityApplyRefundBinding.llLogisticsNumber.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_submit_apply:
                submitRefund();
                break;
        }

    }

    private void submitRefund() {
        if(checkEmpty()){
            String parent_order_id = goodsDetail.parent_order_id;
            String sub_order_id=goodsDetail.sub_order_id;
            String type = (mActivityApplyRefundBinding.rbRefund.isChecked()?"1":"2");
            String refund_money = (TextUtils.isEmpty(mActivityApplyRefundBinding.etRefundPrice.getText())?goodsDetail.actual_total_price:mActivityApplyRefundBinding.etRefundPrice.getText().toString());
            String logistics_company_name= mActivityApplyRefundBinding.etLogisticsCompanyName.getText().toString();
            String logistics_number = mActivityApplyRefundBinding.etLogisticsNumber.getText().toString();
            String message = (TextUtils.isEmpty(mActivityApplyRefundBinding.etRefundComment.getText())?null:mActivityApplyRefundBinding.etRefundComment.getText().toString());



            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
            MallApplication.getInstance().getModel(OrderModel.class).orderApplyRefund(TAG, parent_order_id, sub_order_id, type, refund_money, logistics_company_name, logistics_number, message, new EntityCallBack<String>(new TypeToken<String>(){}) {
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(ApplyRefundActivity.this, progressDialog);
                    Log.e(TAG,msg);
                    Intent intent = new Intent(getBaseContext(),OrderRefundDetailActivity.class);

                    intent.putExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_PARENT_ORDER_ID,goodsDetail.parent_order_id);
                    intent.putExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_SUB_ORDER_ID,goodsDetail.sub_order_id);
                    ApplyRefundActivity.this.startActivity(intent);

                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(ApplyRefundActivity.this, progressDialog);
                    ToastUtils.showToastShort(getBaseContext(),msg);
                    Log.e(TAG,msg);
                }
            });


        }


    }

    private boolean checkEmpty() {
        if(goodsDetail==null){
            ToastUtils.showToastLong(this,"申请退款，加载失败！");
            return false;
        }
        String type = (mActivityApplyRefundBinding.rbRefund.isChecked()?"1":"2");

        if("2".equals(type)) {
            if (TextUtils.isEmpty(mActivityApplyRefundBinding.etLogisticsCompanyName.getText())) {
                ToastUtils.showToastLong(this, "快递公司不能为空！");
                return false;
            }

            if (TextUtils.isEmpty(mActivityApplyRefundBinding.etLogisticsNumber.getText())) {
                ToastUtils.showToastLong(this, "快递编号不能为空！");
                return false;
            }

        }
        if(!TextUtils.isEmpty(mActivityApplyRefundBinding.etRefundPrice.getText())&&Double.parseDouble(mActivityApplyRefundBinding.etRefundPrice.getText().toString())>Double.parseDouble(goodsDetail.actual_total_price)){
            ToastUtils.showToastLong(this,"退款金额不能大于，实际金额！");
            return false;
        }
        return true;
    }

    private void recoverySelectStyle(TextView tv, RadioButton rb) {
        tv.setTextColor(Color.parseColor("#525252"));
        rb.setChecked(false);
    }

    private void changeSelectStyle(TextView tv, RadioButton rb) {
        tv.setTextColor(Color.parseColor("#F85153"));
        rb.setChecked(true);
    }
}
