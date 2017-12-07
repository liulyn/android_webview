package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.RechargeCardList;
import com.cloud.tao.databinding.ActivityUseRechargeCardHistoryBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.RefreshLayout;
import com.cloud.tao.ui.widget.WinPrizeDialog;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 云豆卡充值云豆历史记录
 * Created by gezi-pc on 2016/11/25.
 */

public class UseRechargeCardHistoryActivity extends BaseNavBackActivity {

    private static final String TAG = RegMoneyHistoryActivity.class.getSimpleName();
    ActivityUseRechargeCardHistoryBinding mActivityUseRechargeCardHistoryBinding;
    private int l=10,p=1;
    private ArrayList<RechargeCardList.RechargeCard> records = new ArrayList<RechargeCardList.RechargeCard>();
    private CommonAdapter commonAdapter;
    private RefreshLayout myRefreshListView;
    private boolean isLastPage = false;
    private WinPrizeDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityUseRechargeCardHistoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_use_recharge_card_history);
        mActivityUseRechargeCardHistoryBinding.loadding.showLoadSuccess();
        setNavDefaultBack(mActivityUseRechargeCardHistoryBinding.tb);
        super.onCreate(savedInstanceState);
        myRefreshListView = mActivityUseRechargeCardHistoryBinding.swipeLayout;
        loadData();
        initRefreshView();
    }

    protected void loadData() {
        MallApplication.getInstance().getModel(MemberModel.class).rechargeCardList(TAG, l, p, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                Log.e(TAG,msg+";"+resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    RechargeCardList recordList= GsonUtil.GsonToBean(jsonObj.optString("data"), RechargeCardList.class);


                    if (recordList.recharge_card_list != null &&recordList.recharge_card_list.size() > 0) {
                        if (p == 1) {
                            records = recordList.recharge_card_list;
                        }else{
                            records.addAll(recordList.recharge_card_list);
                        }
                    }else{
                        isLastPage = true;
                    }


                    fillData();
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getBaseContext(), msg);
                Log.e(TAG,msg);
            }
        });

    }


    private void fillData() {
        if(commonAdapter==null){
            commonAdapter = new CommonAdapter<RechargeCardList.RechargeCard>(this,records,R.layout.activity_use_recharge_card_history_item) {
                @Override
                protected void convert(ViewHolder vh, final RechargeCardList.RechargeCard item) {
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_card_no)).setText(item.cardno);
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_money)).setText(item.money);
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_date)).setText(item.used_time);
                }
            };
            mActivityUseRechargeCardHistoryBinding.lvHistory.setAdapter(commonAdapter);
        }else{
            commonAdapter.notifyDataSetChanged();
        }


    }

    private void initRefreshView() {


        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                //Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(commonAdapter!=null) {
                            // 更新数据
                            records.clear();
                            UseRechargeCardHistoryActivity.this.p = 1;
                            loadData();
                        }

                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);

                    }
                }, 500);
            }
        });

        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                //Toast.makeText(activity, "load", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (commonAdapter != null&&isLastPage==false) {
                            UseRechargeCardHistoryActivity.this.p++;
                            loadData();

                        }
                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 500);

            }
        });

    }





}