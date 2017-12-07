package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.ScoreApplyHistory;
import com.cloud.tao.databinding.ActivityIntegralCashHistoryBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.req.BasePageReq;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.RefreshLayout;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/3.
 */

public class IntegralCashHistoryActivity extends BaseNavBackActivity {


    private static final String TAG = IntegralCashHistoryActivity.class.getSimpleName();
    ActivityIntegralCashHistoryBinding mActivityIntegralCashHistoryBinding;
    ListView lvHistory;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    private RefreshLayout myRefreshListView;
    private int p = 1;
    private int l = Constants.PageSize;
    private ArrayList<ScoreApplyHistory> historys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityIntegralCashHistoryBinding =  DataBindingUtil.setContentView(this, R.layout.activity_integral_cash_history);
        setNavDefaultBack(mActivityIntegralCashHistoryBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityIntegralCashHistoryBinding.loadding.showLoadSuccess();
        lvHistory = mActivityIntegralCashHistoryBinding.lvIntegralCashHistory;
        initListener();
        loadData();
    }

    private void initListener() {

        // 获取RefreshLayout实例
        myRefreshListView = (RefreshLayout)this.findViewById(R.id.swipe_layout);
        // 设置下拉刷新时的颜色值,颜色值需要定义在xml中
        myRefreshListView.setColorSchemeResources(R.color.red_bg,R.color.red_bg,R.color.red_bg,R.color.red_bg);

        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                //Toast.makeText(context, "refresh", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(commonAdapter!=null) {
                            // 更新数据
                            IntegralCashHistoryActivity.this.p = 1;
                            loadData();
                            commonAdapter.notifyDataSetInvalidated();
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

                //Toast.makeText(context, "load", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        if(commonAdapter!=null) {
                            IntegralCashHistoryActivity.this.p++;
                            loadData();
                            commonAdapter.notifyDataSetInvalidated();
                        }

                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 500);

            }
        });


    }


    protected void loadData() {


        BasePageReq req = new BasePageReq();
        req.p = p;
        req.l = l;
        MallApplication.getInstance().getModel(MemberModel.class).getScoreApplyHistoryList(TAG, req,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {

                Log.e(TAG,resp.toString());
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    ArrayList<ScoreApplyHistory> tempHistorys = GsonUtil.fromJsonList(jsonObj.optString("data"),ScoreApplyHistory.class);
                    if (tempHistorys != null && tempHistorys.size() > 0) {
                        if (p == 1) {
                            historys.clear();
                            historys.addAll(tempHistorys);
                        }else{
                            historys.addAll(tempHistorys);
                        }
                    }

                    if(historys == null
                            || historys.isEmpty()
                            ||historys.size()!=l){
                        myRefreshListView.setLast(true);
                    }

                    initData();


                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }

            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.i(TAG,msg);
            }
        });
    }

    private CommonAdapter commonAdapter;

    private void initData() {
        if(commonAdapter==null) {
            commonAdapter = new CommonAdapter<ScoreApplyHistory>(this, historys, R.layout.activity_integral_cash_history_item) {
                @Override
                protected void convert(ViewHolder vh, ScoreApplyHistory item) {
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_username)).setText(item.receiver);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_number)).setText(item.pay_way + "：" + item.account);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_cash)).setText("提现金额：￥" + item.apply_amount);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_integral)).setText("提现云豆：" + item.apply_score);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_date)).setText(item.apply_start_time.substring(0, item.apply_start_time.indexOf(" ")).toString());
                    TextView tvStatus = (TextView) vh.getConvertView().findViewById(R.id.tv_status);
                    tvStatus.setText(item.state);

                }
            };
            lvHistory.setAdapter(commonAdapter);
        }else{
            commonAdapter.notifyDataSetChanged();
        }



    }


}
