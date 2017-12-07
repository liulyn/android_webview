package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.VipActiveInfo;
import com.cloud.tao.bean.etc.VipCard;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.databinding.ActivityMyVipCradBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.UrlManager;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.adapter.etc.VipActiveAdapter;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gezi-pc on 2016/9/30.
 */

public class MyVipCardActivity extends BaseNavBackActivity  implements View.OnClickListener {


    private static final String TAG = "MyVipCardActivity";
    public static final String MY_VIP_BASE_IFNO = "my_vip_base_ifno";


    ActivityMyVipCradBinding mActivityMyVipCradBinding;
    private VipCard vipCard;
    private List<VipActiveInfo> mActiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityMyVipCradBinding =  DataBindingUtil.setContentView(this, R.layout.activity_my_vip_crad);
        setNavDefaultBack(mActivityMyVipCradBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityMyVipCradBinding.loadding.showLoadSuccess();
        initListener();
        getActiveMain();

    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void getActiveMain(){
        mActiveData= new ArrayList<>();
        mActiveData.add(new VipActiveInfo(R.mipmap.icon_exchange_records, "兑换记录", VipActiveInfo.ACTIVE_RECORD));
        mActiveData.add(new VipActiveInfo(R.mipmap.icon_growth_record, "成长记录", VipActiveInfo.ACTIVE_GROW));
        mActiveData.add(new VipActiveInfo(R.mipmap.icon_recommend, "推荐云豆", VipActiveInfo.ACTIVE_RECOMMEND));
        mActiveData.add(new VipActiveInfo(R.mipmap.icon_rights_vip, "权益说明", VipActiveInfo.ACTIVE_EQUITY));
        VipActiveAdapter mMainActiveAdapter =new VipActiveAdapter(MyVipCardActivity.this,mActiveData);
        mMainActiveAdapter.setActiveMainListener(new VipActiveAdapter.ActiveMainListener() {
            @Override
            public void onActiveItemClick(String activeType) {
                if(activeType.equals(VipActiveInfo.ACTIVE_RECORD)){
                    startActivity(new Intent(MyVipCardActivity.this,IntegralCashHistoryActivity.class));
                }else if(activeType.equals(VipActiveInfo.ACTIVE_GROW)){
                    Intent intent = new Intent(MyVipCardActivity.this,ConsumeInfoActivity.class);
                    intent.putExtra(ConsumeInfoActivity.IN_ACTIVITY_FLAG,"score_back");
                    startActivity(intent);
                }else if(activeType.equals(VipActiveInfo.ACTIVE_RECOMMEND)){
                    Intent intent = new Intent(MyVipCardActivity.this,MyVipRecommendActivity.class);
                    startActivity(intent);
                }else if(activeType.equals(VipActiveInfo.ACTIVE_EQUITY)){
                    Intent intent = new Intent(MyVipCardActivity.this,ConsumeInfoActivity.class);
                    intent.putExtra(ConsumeInfoActivity.IN_ACTIVITY_FLAG,"vip_card_explain");
                    startActivity(intent);
                }
            }
        });
        mActivityMyVipCradBinding.gvAboutActive.setAdapter(mMainActiveAdapter);
    }

    protected void initData() {
//        if(vipCard!=null) {
//            mActivityMyVipCradBinding.tvMyRemainder.setText(vipCard.balance + "");
//            mActivityMyVipCradBinding.tvAvailableIntegral.setText(vipCard.score_to_exchange + "");
//        }else{
            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

            MallApplication.getInstance().getModel(MemberModel.class).getVipCardCenter(TAG, new EntityCallBack<String>(new TypeToken<String>(){}){
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                    try {
                        JSONObject jsonObj=new JSONObject(resp);
                        vipCard= GsonUtil.GsonToBean(jsonObj.optString("data"),VipCard.class);
                        Log.i(TAG,vipCard.toString());
                        //mActivityMyVipCradBinding.tvMyRemainder.setText("￥"+vipCard.balance + "");
                        mActivityMyVipCradBinding.tvAvailableIntegral.setText("大云豆："+(TextUtils.isEmpty(vipCard.score_to_exchange)?"0":vipCard.score_to_exchange));
                        mActivityMyVipCradBinding.tvVipRankName.setText(vipCard.rank_name);
                        mActivityMyVipCradBinding.tvVipId.setText("ID："+vipCard.member_card_num);
                        mActivityMyVipCradBinding.tvRemainSocre.setText("小云豆："+vipCard.remain_score);
                        mActivityMyVipCradBinding.tvCommissionTotal.setText("累计佣金："+(TextUtils.isEmpty(vipCard.commission_total)?"0":vipCard.commission_total));
                        mActivityMyVipCradBinding.tvCommissionConfirmed.setText("可兑佣金："+(TextUtils.isEmpty(vipCard.commission_confirmed)?"0":vipCard.commission_confirmed));
                        /*if(vipCard.is_bind_cardno==0) {
                            mActivityMyVipCradBinding.rlBindEntityCard.setVisibility(View.VISIBLE);
                        }*/
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                    }
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                    ToastUtils.showToastShort(getBaseContext(),msg);
                }
            });


 //       }

    }

    private void initListener() {
        mActivityMyVipCradBinding.rlMyInfo.setOnClickListener(this);
       // mActivityMyVipCradBinding.rlIntegralReturn.setOnClickListener(this);
        mActivityMyVipCradBinding.rlPromotionVip.setOnClickListener(this);
        mActivityMyVipCradBinding.btCustomization.setOnClickListener(this);
        mActivityMyVipCradBinding.btCustomizationExchange.setOnClickListener(this);
        //mActivityMyVipCradBinding.rlIntegralCash.setOnClickListener(this);
        //mActivityMyVipCradBinding.rlLegalRightExplain.setOnClickListener(this);
        //mActivityMyVipCradBinding.rlScorePayQrCode.setOnClickListener(this);
        //mActivityMyVipCradBinding.rlBindEntityCard.setOnClickListener(this);
        mActivityMyVipCradBinding.rlMyNeedPrize.setOnClickListener(this);
        mActivityMyVipCradBinding.rlWinningPrize.setOnClickListener(this);
        mActivityMyVipCradBinding.rlPrepaidCardRecharge.setOnClickListener(this);
        mActivityMyVipCradBinding.rlPrepaidCardRechargeHis.setOnClickListener(this);

        mActivityMyVipCradBinding.btCommissionExchange.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_my_info:
                this.startActivity(new Intent(this,PersonInfoActivity.class));
                break;
            /*case R.id.rl_integral_return:
                Intent intent = new Intent(this,ConsumeInfoActivity.class);
                intent.putExtra(ConsumeInfoActivity.IN_ACTIVITY_FLAG,"score_back");
                this.startActivity(intent);
                break;*/
            case R.id.rl_promotion_vip:
                intent = new Intent(this,PromotionLeaguerActivity.class);
                intent.putExtra(PromotionLeaguerActivity.IN_PROMOTION_LEAGUER_WAY,2);
                this.startActivity(intent);
                break;
            /*case R.id.rl_integral_cash:
                this.startActivity(new Intent(this,IntegralCashActivity.class));
                break;*/
            /*case R.id.rl_legal_right_explain:
                intent = new Intent(this,ConsumeInfoActivity.class);
                intent.putExtra(ConsumeInfoActivity.IN_ACTIVITY_FLAG,"vip_card_explain");
                this.startActivity(intent);
                break;*/
            /*case R.id.rl_score_pay_qr_code:
                intent = new Intent(this,PayQRCodeActivity.class);
                this.startActivity(intent);
                break;
            case R.id.rl_bind_entity_card:
                showBindDialog();
                break;*/
            case R.id.rl_my_need_prize:
                String time = AccountInfo.getInstance().getLastLoginTime();
                String session_id = AccountInfo.getInstance().getLastLoginSessionId();
                intent = new Intent(this,LotteryDrawWebViewActivity.class);
                String url = UrlManager.URL_LOTTERYDRAW+"?session_id="+session_id+"&time="+time;
                Log.e(TAG,url);
                intent.putExtra(LotteryDrawWebViewActivity.LOAD_LOTTERY_DRAW_URL_PARAM,url);
                this.startActivity(intent);
                break;
            case R.id.rl_winning_prize:
                this.startActivity(new Intent(this,WinPrizeHistoryActivity.class));
                break;
            case R.id.rl_prepaid_card_recharge:
                showPrepaidCardRechargeDialog();
                break;
            case R.id.rl_prepaid_card_recharge_his:
                this.startActivity(new Intent(this,UseRechargeCardHistoryActivity.class));
                break;
            case R.id.bt_commission_exchange:
                String myStoreId=SharePrefUtil.getString(MallApplication.instance(), SharePrefUtil.KEY.function_myStoreId, null);
                Intent getCashIntent = new Intent(this,FenxiaoGetCashActivity.class);
                getCashIntent.putExtra("myStoreId", myStoreId);
                this.startActivity(getCashIntent);
                break;
            case R.id.bt_customization:
                startActivity(new Intent(this,PayStandActivity.class));
                break;
            case R.id.bt_customization_exchange:
                this.startActivity(new Intent(this,IntegralCashActivity.class));
                break;
        }

    }



    /**
     * 这是兼容的 AlertDialog
     */
    private void showBindDialog() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        AlertDialog.Builder   builder = new android.support.v7.app.AlertDialog.Builder(this);
        final EditText etRmark = new EditText(this);
        etRmark.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        etRmark.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        etRmark.setTextAppearance(this,R.style.personInfoEdit);
        etRmark.setHint("输入卡号");
        etRmark.setLines(3);
        builder.setTitle("绑定实体卡号");
        builder.setView(etRmark);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cardno = etRmark.getText().toString();
                if(TextUtils.isEmpty(cardno)){
                    ToastUtils.showToastShort(getBaseContext(),"卡号不能为空!");
                    return;
                }
                postBindCardNo(cardno);
            }
        });
        builder.show();

    }



    /**
     * 这是兼容的 AlertDialog
     */
    private void showPrepaidCardRechargeDialog() {

        AlertDialog.Builder   builder = new android.support.v7.app.AlertDialog.Builder(this);
        final EditText etRmark = new EditText(this);
        etRmark.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        etRmark.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        etRmark.setTextAppearance(this,R.style.personInfoEdit);
        etRmark.setHint("输入云豆卡号");
        etRmark.setLines(3);
        builder.setTitle("云豆卡充值");
        builder.setView(etRmark);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String cardno = etRmark.getText().toString();
                if(TextUtils.isEmpty(cardno)){
                    ToastUtils.showToastShort(getBaseContext(),"云豆卡号不能为空!");
                    return;
                }
                postPrepaidCardRechargeNo(cardno);
            }
        });
        builder.show();

    }


    private void postBindCardNo(String cardno){
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(MyVipCardActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).bindCardno(TAG, cardno, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                //mActivityMyVipCradBinding.rlBindEntityCard.setVisibility(View.GONE);
                Log.i(TAG,resp);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });
    }



    private void postPrepaidCardRechargeNo(String cardno){
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(MyVipCardActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).useRechargeCard(TAG, cardno, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                //mActivityMyVipCradBinding.rlBindEntityCard.setVisibility(View.GONE);
                Log.i(TAG,resp);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(MyVipCardActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });
    }

}
