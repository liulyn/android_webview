package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.BankCardInfo;
import com.cloud.tao.bean.etc.CloudPayWay;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.PayStandDataInfo;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.bean.etc.RechargeSetInfo;
import com.cloud.tao.callback.OnTagSelectListener;
import com.cloud.tao.databinding.ActivityPayStandBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.FlowTagLayout;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.ui.widget.bean.TagInfo;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.TagAdapter;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/10/28
 * des: 充值
 */

public class PayStandActivity extends BaseNavBackActivity {

    private static final String TAG = "PayStandActivity";
    ActivityPayStandBinding  mActivityPayStandBinding;
    FlowTagLayout flowTagLayout;
    private SelectSingleWidget selectBankCardWidget;
    private SelectSingleWidget selectPayWayWidget;
    private ArrayList<BankCardInfo> mBankCards;
    private List<RechargeSetInfo> mRechargeSets;
    //PayWay selectPayWay;
    CloudPayWay selectPayWay;
    BankCardInfo selectBankCard;
    int mPosition=0;  //记录选择的Tag
    Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityPayStandBinding= DataBindingUtil.setContentView(this, R.layout.activity_pay_stand);
        flowTagLayout=mActivityPayStandBinding.ftlPayPriceTags;
        this.savedInstanceState = savedInstanceState;
        setNavDefaultBack(mActivityPayStandBinding.tb);
        mActivityPayStandBinding.loadding.showLoadSuccess();
        initSelectPayWay();
        initListener();
        super.onCreate(savedInstanceState);
    }

    private void initListener() {
        mActivityPayStandBinding.btAddRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRegMoney();
            }
        });

    }

    private void addRegMoney() {

        /*if(mRechargeSets==null||mRechargeSets.size()==0){
            ToastUtils.showToastShort(getBaseContext(),"请先添加银行卡！");
            return;
        }*/

        if(selectPayWay==null){
            ToastUtils.showToastShort(getBaseContext(),"支付方式不能为空！");
            return;
        }
        /*if(selectBankCard==null){
            ToastUtils.showToastShort(getBaseContext(),"银行卡号不能为空！");
            return;
        }*/

        int recharge_set_id = Integer.parseInt(mRechargeSets.get(mPosition).recharge_set_id);
        double money = Double.parseDouble(mRechargeSets.get(mPosition).amount);
        int  pay_way = selectPayWay.pay_way_value;
        //int card_id = Integer.parseInt(selectBankCard.client_transfer_card_id);
        //String card_no = selectBankCard.transfer_card_num;
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).regMoneyConfirm(TAG, recharge_set_id, money,0, "", pay_way, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(PayStandActivity.this, progressDialog);
                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                    if(null!=payH5){
                        readyToPay(payH5);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(PayStandActivity.this,"获取数据异常");
                }

            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PayStandActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });


    }

    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(this,PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM,payH5.url);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initTagData();
    }

    protected void initTagData() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).getPayPriceTagList(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(PayStandActivity.this, progressDialog);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayStandDataInfo payStandDataInfo= GsonUtil.GsonToBean(jsonObj.optString("data"), PayStandDataInfo.class);
                    if(null!=payStandDataInfo){
                        parsePriceTags(payStandDataInfo);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(PayStandActivity.this,"获取数据异常");
                }
                Log.e(TAG,msg);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PayStandActivity.this, progressDialog);
                ToastUtils.showToastShort(PayStandActivity.this, msg);
                Log.e(TAG,msg);
            }
        });
    }

    private void parsePriceTags(PayStandDataInfo payStandDataInfo){
        mRechargeSets=payStandDataInfo.recharge_set;
        /*mBankCards=payStandDataInfo.bank_card;
        if(mBankCards!=null&&mBankCards.size()>0){
            initBankCard();
        }*/
        List<TagInfo> mTagInfoList = new ArrayList<>();
        for(int i=0;i<mRechargeSets.size();i++){
            TagInfo tagInfo = new TagInfo(false, "￥"+mRechargeSets.get(i).amount);
            mTagInfoList.add(tagInfo);
        }
        if (mTagInfoList.size() > 0) {
            mTagInfoList.get(0).setSelect(true);
        }
        TagAdapter tagAdapter = new TagAdapter(PayStandActivity.this, mTagInfoList);
        flowTagLayout.setAdapter(tagAdapter);
        tagAdapter.notifyDataSetChanged();
        flowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
        flowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
            @Override
            public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                mPosition= selectedList.get(0);
                parseSelect(mPosition);
            }
        });
    }

    private void parseSelect(int position){
        mActivityPayStandBinding.tvPaySelectAmount.setText("￥"+mRechargeSets.get(position).amount);
        mActivityPayStandBinding.tvPaySelectPoundage.setText("￥"+mRechargeSets.get(position).poundage);
        mActivityPayStandBinding.tvPaySelectScore.setText(mRechargeSets.get(position).score);
        mActivityPayStandBinding.tvPaySelectAmountHint.setText("￥"+mRechargeSets.get(position).amount);
    }

    /**
     * 初始化支付方式
     */
    private void initSelectPayWay() {
        MallApplication.getInstance().getModel(MemberModel.class).getPayTypeList(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    ArrayList<CloudPayWay> mCloudPayWays=GsonUtil.fromJsonList(jsonObj.optJSONObject("data").optString("pay_way_list"),CloudPayWay.class);
                    ArrayList<String> datas = new ArrayList<>();
                    for(CloudPayWay pay: mCloudPayWays){
                        datas.add(pay.pay_way_name);
                    }
                    selectPayWayWidget=new SelectSingleWidget(PayStandActivity.this,mCloudPayWays,datas,"选择支付方式",mActivityPayStandBinding.vMasker);
                    selectPayWayWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CloudPayWay>() {
                        @Override
                        public void OnSelectSingleCallBack(CloudPayWay result) {
                /*if(result.id==64){ //银行卡支付
                    if(mBankCards!=null&&mBankCards.size()>0){
                        mActivityPayStandBinding.tvPayStandPayTypeHint.setText(result.name);
                        selectPayWay=result;
                    }else{
                        selectPayWay=null;
                        mActivityPayStandBinding.tvPayStandPayTypeHint.setText("请选择");
                        ToastUtils.showToastShort(PayStandActivity.this,"请先到个人中心，银行卡管理选项添加银行卡");
                    }
                }else{
                    selectPayWay=result;
                    mActivityPayStandBinding.tvPayStandPayTypeHint.setText(result.name);
                }*/
                            selectPayWay=result;
                            mActivityPayStandBinding.tvPayStandPayTypeHint.setText(result.pay_way_name);
                        }
                    });

                    //支付方式
                    mActivityPayStandBinding.rlPayStandPayType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectPayWayWidget.showWidget();
                        }
                    });
                } catch (JSONException e) {
                    ToastUtils.showToastShort(PayStandActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(PayStandActivity.this, msg);
            }
        });

    }

    /*private void initBankCard() {
        ArrayList<String> datas = new ArrayList<>();
        for(BankCardInfo bankCardInfo: mBankCards){
            datas.add(bankCardInfo.transfer_card_num);
        }
        selectBankCardWidget=new SelectSingleWidget(PayStandActivity.this,mBankCards,datas,"选择银行卡",mActivityPayStandBinding.vMasker);
        selectBankCardWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<BankCardInfo>() {
            @Override
            public void OnSelectSingleCallBack(BankCardInfo result) {
               mActivityPayStandBinding.tvPaySelectBankCard.setText(result.transfer_card_num);
                selectBankCard=result;
            }
        });

        mActivityPayStandBinding.rlPayStandSelectBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBankCardWidget.showWidget();
            }
        });
    }*/

}

