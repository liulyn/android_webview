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
import com.cloud.tao.bean.etc.CloudPayWay;
import com.cloud.tao.bean.etc.OpenVipCard;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.bean.etc.UpdateVipCardInfo;
import com.cloud.tao.databinding.ActivityPromotionLeaguerBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 升级会员
 * Created by gezi-pc on 2016/9/30.
 */

public class PromotionLeaguerActivity extends BaseNavBackActivity {

    public final static String IN_PROMOTION_LEAGUER_WAY = "in_promotion_leaguer_way";
    private static final String TAG = PromotionLeaguerActivity.class.getSimpleName();
    ActivityPromotionLeaguerBinding mActivityPromotionLeaguerBinding;
    private SelectSingleWidget selectSingleWidget;
    private CloudPayWay selectPayWay;
    private UpdateVipCardInfo updateVipCardInfo;
    private int client_type;
    private ArrayList<UpdateVipCardInfo> updateVipCardInfos;
    private SelectSingleWidget selectLevelWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityPromotionLeaguerBinding = DataBindingUtil.setContentView(this, R.layout.activity_promotion_leaguer);
        setNavDefaultBack(mActivityPromotionLeaguerBinding.tb);
        super.onCreate(savedInstanceState);

        //mActivityPromotionLeaguerBinding.rlPromotionLeaguerType.setOnClickListener(this);
        mActivityPromotionLeaguerBinding.loadding.showLoadSuccess();

        if(getIntent().getIntExtra(IN_PROMOTION_LEAGUER_WAY,1)==2) {
            mActivityPromotionLeaguerBinding.tb.setTitleText("升级会员");
            mActivityPromotionLeaguerBinding.tvMementLevel.setText("请选择会员类型");
            client_type =2;
            updateVipData();
        }else{
            mActivityPromotionLeaguerBinding.tb.setTitleText("开通会员");
            mActivityPromotionLeaguerBinding.tvMementLevel.setText("请选择会员等级");
            client_type =1;
            openVipCard();
        }

        mActivityPromotionLeaguerBinding.btUpdateLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVipLevel();
            }
        });
        //initSelectPayWay();


    }

    private void updateVipLevel() {

            if(selectPayWay==null){
                ToastUtils.showToastShort(getBaseContext(),"支付方式不能空");
                return;
            }

            if(updateVipCardInfo==null){
                ToastUtils.showToastShort(getBaseContext(),"会员类型不能空");
                return;
        }
        int rank_id = updateVipCardInfo.rank_id;
        int pay_way = selectPayWay.pay_way_value;
        double pay_num = updateVipCardInfo.money;
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).postVipCardPay(TAG, rank_id, client_type, pay_way, pay_num, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                    if(null!=payH5){
                        readyToPay(payH5);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
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
        PromotionLeaguerActivity.this.finish();
    }


    /**
     * 开通会员
     */
    private void openVipCard() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(PromotionLeaguerActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).openVipCardInfo(TAG, new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);
                Log.e(TAG,resp);
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);

                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    OpenVipCard openVipCard= GsonUtil.GsonToBean(jsonObj.optString("data"), OpenVipCard.class);
                    updateVipCardInfos = openVipCard.rank_list;
                    initSelectMementLevel();
                    initSelectOpterPayWay(openVipCard.pay_way_list);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }


            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);
                ToastUtils.showToastShort(PromotionLeaguerActivity.this,msg);
                Log.i(TAG,msg);
            }
        });
    }



    private void updateVipData() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).getUpdateVipCardInfo(TAG, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);

                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    //updateVipCardInfos= GsonUtil.fromJsonList(jsonObj.optString("data"), UpdateVipCardInfo.class);
                    OpenVipCard openVipCard= GsonUtil.GsonToBean(jsonObj.optString("data"), OpenVipCard.class);
                    if(openVipCard!=null&&openVipCard.rank_list!=null&&openVipCard.rank_list.size()>0) {
                        updateVipCardInfos = openVipCard.rank_list;
                        initSelectMementLevel();

                        initSelectOpterPayWay(openVipCard.pay_way_list);
                    }else{
                        ToastUtils.showToastShort(getBaseContext(),"暂无无会员类型升级！");
                        PromotionLeaguerActivity.this.finish();
                    }

                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(PromotionLeaguerActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });
    }

    private void initSelectOpterPayWay(ArrayList<CloudPayWay> cloudPayWays){
        ArrayList<CloudPayWay> mCloudPayWays=cloudPayWays;
        ArrayList<String> datas = new ArrayList<>();
        for(CloudPayWay pay: mCloudPayWays){
            datas.add(pay.pay_way_name);
        }
        selectSingleWidget = new SelectSingleWidget(PromotionLeaguerActivity.this,mCloudPayWays,datas,"选择支付方式",mActivityPromotionLeaguerBinding.vMasker);
        selectSingleWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CloudPayWay>() {
            @Override
            public void OnSelectSingleCallBack(CloudPayWay result) {
                mActivityPromotionLeaguerBinding.tvPayWay.setText(result.pay_way_name);
                selectPayWay = result;
            }
        });

        //点击弹出选项选择器
        mActivityPromotionLeaguerBinding.rlPromotionLeaguerPayWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectSingleWidget.showWidget();
            }
        });
    }

    /*private void initSelectPayWay() {
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
                    selectSingleWidget = new SelectSingleWidget(PromotionLeaguerActivity.this,mCloudPayWays,datas,"选择支付方式",mActivityPromotionLeaguerBinding.vMasker);
                    selectSingleWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CloudPayWay>() {
                        @Override
                        public void OnSelectSingleCallBack(CloudPayWay result) {
                            mActivityPromotionLeaguerBinding.tvPayWay.setText(result.pay_way_name);
                            selectPayWay = result;
                        }
                    });

                    //点击弹出选项选择器
                    mActivityPromotionLeaguerBinding.rlPromotionLeaguerPayWay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectSingleWidget.showWidget();
                        }
                    });
                } catch (JSONException e) {
                    ToastUtils.showToastShort(PromotionLeaguerActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(PromotionLeaguerActivity.this, msg);
            }
        });
    }*/

    private void initSelectMementLevel() {
        if(updateVipCardInfos==null||updateVipCardInfos.size()==0){
            mActivityPromotionLeaguerBinding.tvMementLevel.setText("已是最高会员，无需升级！");
            ToastUtils.showToastShort(getBaseContext(),"已是最高会员，无需升级！");
            return;
        }

        ArrayList<String> datas = new ArrayList<>();
        for(UpdateVipCardInfo info: updateVipCardInfos){
            datas.add(info.name);
        }

        selectLevelWidget = new SelectSingleWidget(this,updateVipCardInfos,datas,"选择会员类型",mActivityPromotionLeaguerBinding.vMasker);
        selectLevelWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<UpdateVipCardInfo>() {
            @Override
            public void OnSelectSingleCallBack(UpdateVipCardInfo result) {
                mActivityPromotionLeaguerBinding.tvPromotionCrash.setText("￥"+result.money);
                mActivityPromotionLeaguerBinding.tvMementLevel.setText(result.name);
                updateVipCardInfo = result;
            }
        });

        //点击弹出选项选择器
        mActivityPromotionLeaguerBinding.rlPromotionLeaguerType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLevelWidget.showWidget();
            }
        });
    }






}
