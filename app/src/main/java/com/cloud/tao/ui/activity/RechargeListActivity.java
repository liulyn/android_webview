package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.CardType;
import com.cloud.tao.bean.etc.RechargeCard;
import com.cloud.tao.bean.etc.RechargeObject;
import com.cloud.tao.databinding.ActivityRechargeListBinding;
import com.cloud.tao.framwork.vl.VLDialog;
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

import java.util.List;

/**
 * Created by gezi-pc on 2016/10/13.
 */

public class RechargeListActivity extends BaseNavBackActivity {


    private static final String TAG = "RechargeListActivity";
    ActivityRechargeListBinding mActivityRechargeListBinding;
    private RechargeObject rechargeObject;
    private int[] titleBgs = new int[]{R.drawable.shape_recharge_taxi_bg,R.drawable.shape_recharge_oil_bg,R.drawable.shape_recharge_bank_bg
    ,R.drawable.shape_recharge_other1_bg,R.drawable.shape_recharge_other2_bg
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityRechargeListBinding =  DataBindingUtil.setContentView(this, R.layout.activity_recharge_list);
        setNavDefaultBack(mActivityRechargeListBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityRechargeListBinding.loadding.showLoadSuccess();
        mActivityRechargeListBinding.btAddRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeListActivity.this,AddRechargeActivity.class);
                if(rechargeObject!=null&&rechargeObject.card_set!=null) {
                    intent.putParcelableArrayListExtra(AddRechargeActivity.RECHARGE_TYPE_LIST, rechargeObject.card_set);
                }
                RechargeListActivity.this.startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();

    }

    protected void initData() {
        mActivityRechargeListBinding.loadding.showLoadSuccess();
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);


        MallApplication.getInstance().getModel(MemberModel.class).getRechargeList(TAG, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(RechargeListActivity.this, progressDialog);
                if(resp == null||"".equals(resp)) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    Log.i(TAG,jsonObj.optString("data"));
                    rechargeObject= GsonUtil.GsonToBean(jsonObj.optString("data"),RechargeObject.class);
                    fillData(rechargeObject);

                } catch (JSONException e) {
                    ToastUtils.showToastShort(RechargeListActivity.this,"获取数据异常");
                }
                Log.i(TAG,resp);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(RechargeListActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });


    }

    private int index = 0;

    private void fillData(RechargeObject rechargeObject) {

        mActivityRechargeListBinding.lvCard.setAdapter(new CommonAdapter<CardType>(this,rechargeObject.card_set,R.layout.activity_recharge_list_item) {
            @Override
            protected void convert(ViewHolder vh, CardType item) {
                ((TextView)vh.getConvertView().findViewById(R.id.tv_title)).setText(item.name);

                ((RelativeLayout)vh.getConvertView().findViewById(R.id.rl_title)).setBackgroundResource(titleBgs[index]);
                if(index==4){
                    index = 0;
                }else{
                    index++;
                }
                ListView cardNum = (ListView)vh.getConvertView().findViewById(R.id.lv_card_number);
                cardNum.setAdapter(new CommonAdapter<RechargeCard>(RechargeListActivity.this,item.data,R.layout.activity_recharge_list_item_item) {
                    @Override
                    protected void convert(ViewHolder vh, RechargeCard item) {
                        bindListViewData(this,vh,item);
                    }
                });
            }
        });
    }


    private void bindListViewData(final CommonAdapter<RechargeCard> commonAdapter,ViewHolder vh,final RechargeCard item){

        ((TextView)vh.getConvertView().findViewById(R.id.tv_card_number)).setText(item.label_num);
        ImageView iv = (ImageView) vh.getConvertView().findViewById(R.id.iv_del);

        iv.setTag(item);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Object cardObj = v.getTag();
                if(cardObj!=null){
                    RechargeCard card =(RechargeCard) cardObj;
                    final ProgressDialog progressDialog = VLDialog.showProgressDialog(RechargeListActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

                    MallApplication.getInstance().getModel(MemberModel.class).delRechargeCard(TAG, Integer.parseInt(card.client_label_id), new EntityCallBack<String>(new TypeToken<String>(){}) {
                        @Override
                        public void onSuccess(int code, String msg, String resp) {
                            ViewUtils.dismissDialog(RechargeListActivity.this, progressDialog);
                            List<RechargeCard> data = commonAdapter.getApps();
                            data.remove(item);
                            commonAdapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onFail(int code, Exception e, String msg) {
                            ViewUtils.dismissDialog(RechargeListActivity.this, progressDialog);
                            ToastUtils.showToastShort(getBaseContext(),msg);
                            Log.i(TAG,msg);
                        }
                    });
                }
            }
        });

    }
}

