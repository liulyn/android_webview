package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.RegMoneyList;
import com.cloud.tao.databinding.ActivityReqMoneyHistoryBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.DetailDialog;
import com.cloud.tao.ui.widget.RefreshLayout;
import com.cloud.tao.util.DateUtil;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gezi-pc on 2016/10/29.
 * 购买记录
 */

public class RegMoneyHistoryActivity  extends BaseNavBackActivity {

    private static final String TAG = RegMoneyHistoryActivity.class.getSimpleName();
    ActivityReqMoneyHistoryBinding mActivityReqMoneyHistoryBinding;
    private int l=30,p=1;
    private ArrayList<RegMoneyList.RegMoney> regMoneys = new ArrayList<RegMoneyList.RegMoney>();
    private CommonAdapter commonAdapter;
    private RefreshLayout myRefreshListView;
    private DetailDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityReqMoneyHistoryBinding= DataBindingUtil.setContentView(this, R.layout.activity_req_money_history);
        mActivityReqMoneyHistoryBinding.loadding.showLoadSuccess();
        setNavDefaultBack(mActivityReqMoneyHistoryBinding.tb);
        super.onCreate(savedInstanceState);
        myRefreshListView = mActivityReqMoneyHistoryBinding.swipeLayout;
        loadData();
        initRefreshView();
    }

    protected void loadData() {
        MallApplication.getInstance().getModel(MemberModel.class).regMoneyList(TAG, l, p, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                Log.e(TAG,msg+";"+resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    RegMoneyList regMoneyList= GsonUtil.GsonToBean(jsonObj.optString("data"), RegMoneyList.class);


                    if (regMoneyList.reg_money_list != null &&regMoneyList.reg_money_list.size() > 0) {
                        if (p == 1) {
                            regMoneys.clear();
                            regMoneys.addAll(regMoneyList.reg_money_list);
                        }else{
                            regMoneys.addAll(regMoneyList.reg_money_list);
                        }
                    }

                    if(regMoneyList.reg_money_list == null
                            || regMoneyList.reg_money_list.isEmpty()
                            ||regMoneyList.reg_money_list.size()!=l){
                        myRefreshListView.setLast(true);
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
            commonAdapter = new CommonAdapter<RegMoneyList.RegMoney>(this,regMoneys,R.layout.activity_req_money_histor_item) {
                @Override
                protected void convert(ViewHolder vh, final RegMoneyList.RegMoney item) {
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_date)).setText(DateUtil.formatDateToString(new Date(item.create_time*1000l),"yyyy-MM-dd"));
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_money)).setText(item.total_money+"");
                    ((TextView)vh.getConvertView().findViewById(R.id.tv_state)).setText(item.state_info);
                    TextView operator = (TextView) vh.getConvertView().findViewById(R.id.tv_handler);
                    operator.setText(item.operator_info);
                    operator.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog = new DetailDialog(RegMoneyHistoryActivity.this,"明细详情",item);
                            dialog.show();
                        }
                    });
                }
            };
            mActivityReqMoneyHistoryBinding.lvHistory.setAdapter(commonAdapter);
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
                            regMoneys.clear();
                            RegMoneyHistoryActivity.this.p = 1;
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
                            if (commonAdapter != null) {
                                RegMoneyHistoryActivity.this.p++;
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
