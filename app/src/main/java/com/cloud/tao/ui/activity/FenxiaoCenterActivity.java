package com.cloud.tao.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.CloudPayWay;
import com.cloud.tao.bean.etc.FenxiaoActiveInfo;
import com.cloud.tao.bean.etc.FenxiaoDataInfo;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.bean.etc.event.GetCashMoneyEven;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoCenterBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.ui.adapter.etc.FenxiaoActiveAdapter;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.eventbusutil.EventBusUtil;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/10/23
 * des: 创业中心
 */
public class FenxiaoCenterActivity extends BaseNavBackActivity {

    private static final String TAG = "FenxiaoCenterActivity";
    private final static int REQUEST_CODE_UPDATE_INFO = BASE_CODE + 1;
    Context mContext = FenxiaoCenterActivity.this;
    String mMyStoreId;
    ActivityFenxiaoCenterBinding mFenxiaoCenterBinding;
    private List<FenxiaoActiveInfo> mActiveData;
    private SelectSingleWidget selectPayWayWidget;
    private CloudPayWay selectPayWay;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoCenterBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_center);
        setNavDefaultBack(mFenxiaoCenterBinding.tb);
        checkIsFenxiaoAccount();
        EventBusUtil.register(this);
        super.onCreate(savedInstanceState);
    }

    private void checkIsFenxiaoAccount(){
        mFenxiaoCenterBinding.loadding.showLoadding();
        MallApplication.getInstance().getModel(AppModel.class).getIsFenxiaoAccount(TAG,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                mFenxiaoCenterBinding.loadding.showLoadSuccess();
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    mMyStoreId=jsonObj.optString("my_store_id");
                    if (!checkMyStoreId()) {
                        toFenxiaoApply();
                    } else {
                        getActiveFenxiao();
                        getFenxiaoCenterInfo();
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                if(code==1){
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this,"您未开通创业，请先申请创业");
                    toFenxiaoApply();
                }else{
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this,msg);
                }
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_UPDATE_INFO) {
                mFenxiaoCenterBinding.tvFenxiaoName.setText(data.getExtras().getString("nickName"));
            }
        }
    }

    /**
     * 初始化导航菜单
     */
    private void getActiveFenxiao() {
        mFenxiaoCenterBinding.loadding.showLoadSuccess();
        mFenxiaoCenterBinding.llFenxiaoCenterInfo.setVisibility(View.VISIBLE);
        mActiveData = new ArrayList<>();
        mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_commission, "创业佣金", FenxiaoActiveInfo.ACTIVE_FENXIAO_COMMISSION));
        mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_team, "我的团队", FenxiaoActiveInfo.ACTIVE_FENXIAO_TEAM));
        mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_order, "创业订单", FenxiaoActiveInfo.ACTIVE_FENXIAO_ORDER));
        //mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_info, "创业信息", FenxiaoActiveInfo.ACTIVE_FENXIAO_INFO));
        //mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_shop, "我的小店", FenxiaoActiveInfo.ACTIVE_FENXIAO_SHOP));
        mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_shop_home, "总店首页", FenxiaoActiveInfo.ACTIVE_FENXIAO_SHOP_HOME));
        mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_my_qr_code, "我的推广码", FenxiaoActiveInfo.ACTIVE_FENXIAO_MY_QR_CODE));
        //mActiveData.add(new FenxiaoActiveInfo(R.mipmap.ic_etc_fenxiao_public_qr_code, "公众号二维码", FenxiaoActiveInfo.ACTIVE_FENXIAO_PUBLIC_QR_CODE));
        FenxiaoActiveAdapter mFenxiaoActiveAdapter = new FenxiaoActiveAdapter(FenxiaoCenterActivity.this, mActiveData);
        mFenxiaoActiveAdapter.setActiveMainListener(new FenxiaoActiveAdapter.ActiveFenxiaoListener() {
            @Override
            public void onActiveItemClick(String activeType) {
                onActiveItem(activeType);
            }
        });
        mFenxiaoCenterBinding.gvFenxiaoActive.setAdapter(mFenxiaoActiveAdapter);
    }

    private void onActiveItem(String activeType) {
        if (activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_COMMISSION)) {
            Intent mIntent = new Intent(mContext, FenxiaoCommissionActivity.class);
            mIntent.putExtra("myStoreId", mMyStoreId);
            startActivityForResult(mIntent, REQUEST_CODE_UPDATE_INFO);
        } else if (activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_TEAM)){
            Intent mIntent = new Intent(mContext, FenxiaoMyTeamInfoActivity.class);
            mIntent.putExtra("myStoreId", mMyStoreId); //切换首页Tab
            startActivityForResult(mIntent, REQUEST_CODE_UPDATE_INFO);
        }else if(activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_ORDER)) {
            Intent mIntent = new Intent(mContext, FenxiaoOrderActivity.class);
            mIntent.putExtra("myStoreId", mMyStoreId);
            startActivityForResult(mIntent, REQUEST_CODE_UPDATE_INFO);
        }else if (activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_INFO)) {
            Intent mIntent = new Intent(mContext, FenxiaoUpdateInfoActivity.class);
            mIntent.putExtra("myStoreId", mMyStoreId); //切换首页Tab
            startActivityForResult(mIntent, REQUEST_CODE_UPDATE_INFO);
        } else if (activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_SHOP)) {
            Intent mIntent = new Intent(mContext, MainTabActivity.class);
            mIntent.putExtra("storeId", mMyStoreId); //我的小店ID
            mIntent.putExtra("isRefresh", true); //是否刷新首页
            mIntent.putExtra("isToMyStore", true); //是否获取小店信息
            FenxiaoCenterActivity.this.startActivity(mIntent);
            FenxiaoCenterActivity.this.finish();
        } else if (activeType.equals(FenxiaoActiveInfo.ACTIVE_FENXIAO_SHOP_HOME)) {
            Intent mIntent = new Intent(mContext, MainTabActivity.class);
            mIntent.putExtra("isRefresh", true);
            mIntent.putExtra("isToMyStore", false);
            FenxiaoCenterActivity.this.startActivity(mIntent);
            FenxiaoCenterActivity.this.finish();
        }else{
            Intent mIntent = new Intent(mContext, FenxiaoQRCodeActivity.class);
            mIntent.putExtra("myStoreId", mMyStoreId);
            startActivityForResult(mIntent, REQUEST_CODE_UPDATE_INFO);
        }


    }

    private void toFenxiaoApply() {
        startActivity(new Intent(FenxiaoCenterActivity.this, FenxiaoApplyActivity.class));
        this.finish();
    }

    private boolean checkMyStoreId() {
        boolean isCheck = true;
        if (TextUtils.isEmpty(mMyStoreId)) {
            isCheck = false;
        } else if ("0".equals(mMyStoreId)) {
            isCheck = false;
        }
        return isCheck;
    }

    private void getFenxiaoCenterInfo() {
        if (!checkMyStoreId()) {
            toFenxiaoApply();
        } else {
            progressDialog= VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
            MallApplication.getInstance().getModel(AppModel.class).getFenxiaoCenter(TAG, mMyStoreId, new EntityCallBack<String>(new TypeToken<String>() {
            }) {
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(FenxiaoCenterActivity.this, progressDialog);
                    if (resp == null) return;
                    try {
                        JSONObject jsonObj = new JSONObject(resp);
                        FenxiaoDataInfo mFenxiaoDataInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), FenxiaoDataInfo.class);
                        parseFenxiaoInfo(mFenxiaoDataInfo);
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(FenxiaoCenterActivity.this, "获取数据异常");
                    }
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(FenxiaoCenterActivity.this, progressDialog);
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this, msg);
                }

                @Override
                public void onAfter(int id) {
                }
            });
        }
    }

    public void parseFenxiaoInfo(FenxiaoDataInfo fenxiaoDataInfo) {
        if (fenxiaoDataInfo.fenxiao_info != null) {
            if (!TextUtils.isEmpty(fenxiaoDataInfo.fenxiao_info.private_headimgurl)) {
                GlideUtil.getInstance().loadImage(FenxiaoCenterActivity.this, fenxiaoDataInfo.fenxiao_info.private_headimgurl, mFenxiaoCenterBinding.ivFengxiaoIcon);
            }
            mFenxiaoCenterBinding.tvFenxiaoName.setText(fenxiaoDataInfo.fenxiao_info.nick_name);
            if (!TextUtils.isEmpty(fenxiaoDataInfo.recommend_code)) {
                mFenxiaoCenterBinding.tvFenxiaoRecommendCode.setText("推荐码：" + fenxiaoDataInfo.recommend_code);
            }
            mFenxiaoCenterBinding.tvFenxiaoCommissionTotal.setText(TextUtils.isEmpty(fenxiaoDataInfo.fenxiao_info.commission_total) ? "￥0" : "￥" + fenxiaoDataInfo.fenxiao_info.commission_total);
            if (!TextUtils.isEmpty(fenxiaoDataInfo.fenxiao_info.commission_confirmed) && Double.parseDouble(fenxiaoDataInfo.fenxiao_info.commission_confirmed) > 0) {
                mFenxiaoCenterBinding.tvFenxiaoCommissionConfirmed.setText("￥" + fenxiaoDataInfo.fenxiao_info.commission_confirmed);
                mFenxiaoCenterBinding.btFenxiaoExtractMoney.setVisibility(View.VISIBLE);
                mFenxiaoCenterBinding.btFenxiaoExtractMoney.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void noDoubleClick(View v) {
                        Intent intent = new Intent(FenxiaoCenterActivity.this,FenxiaoGetCashActivity.class);
                        intent.putExtra("myStoreId", mMyStoreId);
                        FenxiaoCenterActivity.this.startActivity(intent);
                    }
                });
            } else {
                mFenxiaoCenterBinding.tvFenxiaoCommissionConfirmed.setText("￥0");
            }
            if (Integer.valueOf(fenxiaoDataInfo.fenxiao_info.level_id) > 0) {
                mFenxiaoCenterBinding.tvFenxiaoCurrentLevel.setText(fenxiaoDataInfo.fenxiao_upgrade==null?"":fenxiaoDataInfo.fenxiao_upgrade.cur_level_info.level_name);
            } else {
                mFenxiaoCenterBinding.tvFenxiaoCurrentLevel.setText("消费者创业");
            }
            if ("1".equals(fenxiaoDataInfo.fenxiao_upgrade_switch)) {
                mFenxiaoCenterBinding.rlFenxiaoUpgrade.setVisibility(View.VISIBLE);
                mFenxiaoCenterBinding.tvFenxiaoNextLevel.setText("（可升级为" + fenxiaoDataInfo.fenxiao_upgrade.next_level_info.level_name + "）");
                mFenxiaoCenterBinding.tvFenxiaoNextLevelFrozenMoney.setText("可获得￥" + fenxiaoDataInfo.fenxiao_upgrade.frozen_money);
                mFenxiaoCenterBinding.tvFenxiaoNextLevelDiscount.setText("可享购买商品" + String.valueOf(Double.valueOf(fenxiaoDataInfo.fenxiao_upgrade.next_level_info.level_discount) * 0.1) + "折优惠");
            }


            initSelectPayWay();

        }

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
                    selectPayWayWidget = new SelectSingleWidget(FenxiaoCenterActivity.this,mCloudPayWays,datas,"选择支付方式",mFenxiaoCenterBinding.vMasker);
                    selectPayWayWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CloudPayWay>() {
                        @Override
                        public void OnSelectSingleCallBack(CloudPayWay result) {
                            selectPayWay = result;
                            updateLeven();
                        }
                    });

                    //支付方式
                    mFenxiaoCenterBinding.btUpdateLeven.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectPayWayWidget.showWidget();
                        }
                    });
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(FenxiaoCenterActivity.this, msg);
            }
        });
    }

    private void updateLeven() {
        progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(AppModel.class).updateFenxiaoLevel(TAG, mMyStoreId, selectPayWay.pay_way_value, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(FenxiaoCenterActivity.this, progressDialog);
                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                    if(null!=payH5){
                        readyToPay(payH5);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoCenterActivity.this,"获取数据异常");
                }

            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(FenxiaoCenterActivity.this, progressDialog);
                ToastUtils.showToastShort(FenxiaoCenterActivity.this, msg);
            }
        });
    }

    @Subscribe
    public void onEventMainThread(GetCashMoneyEven cashMoneyEven) {
        if(!cashMoneyEven.isHavenBalance){
            mFenxiaoCenterBinding.btFenxiaoExtractMoney.setVisibility(View.GONE);
        }
        mFenxiaoCenterBinding.tvFenxiaoCommissionConfirmed.setText("￥"+cashMoneyEven.cashMoney);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        //mMyStoreId = SharePrefUtil.getString(MallApplication.instance(), SharePrefUtil.KEY.function_myStoreId, null);
    }


    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(this,PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM,payH5.url);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        EventBusUtil.unRegister(this);
        if(progressDialog!=null){
            ViewUtils.dismissDialog(FenxiaoCenterActivity.this, progressDialog);
        }
        super.onDestroy();
    }
}
