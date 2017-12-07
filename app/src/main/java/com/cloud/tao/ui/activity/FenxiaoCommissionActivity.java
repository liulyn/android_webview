package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.FenxiaoCommissionDataInfo;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoCommissionBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sunny created at 2016/10/25
 * des: 创业佣金
 */
public class FenxiaoCommissionActivity extends BaseNavBackActivity {

    private static final String TAG = "FenxiaoCommissionActivity";
    ActivityFenxiaoCommissionBinding mFenxiaoCommissionBinding;
    String mMyStoreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoCommissionBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_commission);
        setNavDefaultBack(mFenxiaoCommissionBinding.tb);
        mMyStoreId = getIntent().getExtras().getString("myStoreId");
        mFenxiaoCommissionBinding.rlFenxiaoDetail.setOnClickListener(mOnClickListener);
        super.onCreate(savedInstanceState);
        getFenxiaoCommissionInfo();
    }

    private NoDoubleClickListener mOnClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rl_fenxiao_detail:
                    Intent mIntent = new Intent(FenxiaoCommissionActivity.this, FenxiaoCommissionDetailsActivity.class);
                    mIntent.putExtra("myStoreId", mMyStoreId);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    };

    private void getFenxiaoCommissionInfo() {
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoCommissionInfo(TAG, mMyStoreId, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    FenxiaoCommissionDataInfo commissionDataInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), FenxiaoCommissionDataInfo.class);
                    parseData(commissionDataInfo);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoCommissionActivity.this, "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(FenxiaoCommissionActivity.this, msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    private void parseData(FenxiaoCommissionDataInfo commissionDataInfo) {
        mFenxiaoCommissionBinding.tvFenxiaoCommissionPrice.setText("￥"+commissionDataInfo.fenxiao_info.commission_confirmed);
        mFenxiaoCommissionBinding.tvFenxiaoCommissionCount.setText("累计结算佣金：￥"+commissionDataInfo.fenxiao_info.commission_total);
        mFenxiaoCommissionBinding.tvFenxiaoCommissionUnconfirmed.setText("未结算佣金：￥"+commissionDataInfo.fenxiao_info.commission_unconfirmed);
        mFenxiaoCommissionBinding.tvFenxiaoCommissionSettlementDay.setText("结算期（"+commissionDataInfo.fenxiao_settlement_day+"）天后，佣金可提现。");
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }
}


