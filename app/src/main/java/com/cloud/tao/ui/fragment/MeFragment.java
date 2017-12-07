package com.cloud.tao.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.GameInfo;
import com.cloud.tao.bean.ZiXunInfo;
import com.cloud.tao.bean.etc.MeActiveInfo;
import com.cloud.tao.bean.etc.OpenVipCardInfo;
import com.cloud.tao.bean.etc.VipCard;
import com.cloud.tao.bean.etc.event.AccountInfoEven;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.net.model.membermodel.req.MemberReq;
import com.cloud.tao.ui.activity.BankCarkManageActivity;
import com.cloud.tao.ui.activity.EditPersonInfoActivity;
import com.cloud.tao.ui.activity.FenxiaoCenterActivity;
import com.cloud.tao.ui.activity.ManagerAddressActivity;
import com.cloud.tao.ui.activity.MyOrderListActivity;
import com.cloud.tao.ui.activity.MyVipCardActivity;
import com.cloud.tao.ui.activity.PromotionLeaguerActivity;
import com.cloud.tao.ui.activity.RechargeListActivity;
import com.cloud.tao.ui.activity.RegMoneyHistoryActivity;
import com.cloud.tao.ui.activity.ResetMobileActivity;
import com.cloud.tao.ui.activity.ShoppingCarActivity;
import com.cloud.tao.ui.activity.UserSettingActivity;
import com.cloud.tao.ui.adapter.etc.MeActiveAdapter;
import com.cloud.tao.ui.widget.BadgeView;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseShowFragment;
import com.cloud.tao.databinding.FragmentMeBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.glide.GlideUtil;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * author:janecer on 2016/8/23 19:42
 */

public class MeFragment extends BaseShowFragment implements View.OnClickListener {

    private static final int REQUEST_CODE_USERINFO = BASE_CODE + 1;
    private static final String TAG = "MeFragment";

    FragmentMeBinding meBinding;
    private int pageIndex = 0;
    private List<MeActiveInfo> mActiveData;

    private CommonAdapter<ZiXunInfo> commonAdapter;
    private CommonAdapter<GameInfo> myGameAdapter;
    private VipCard vipCard;

    BadgeView bvCoin, bvGift;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected View initLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        meBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
        meBinding.rlUserinfo.setOnClickListener(this);
        meBinding.loadding.showLoadSuccess();
        meBinding.rlMyVipCard.setOnClickListener(this);
        // meBinding.rlManagerRechargeCard.setOnClickListener(this);
        //meBinding.rlManagerBankCard.setOnClickListener(this);
        //meBinding.rlManagerRechargeableRecord.setOnClickListener(this);
        meBinding.rlManagerAddress.setOnClickListener(this);
        meBinding.llMyOrder.setOnClickListener(this);
        meBinding.rlUserSetting.setOnClickListener(this);
        meBinding.rlShoppingCar.setOnClickListener(this);
        meBinding.rlDistributionCentre.setOnClickListener(this);
        initData();

        getActiveMain();
        return meBinding.getRoot();
    }

    private void initData() {
        MemberReq info = (MemberReq) SharePrefUtil.getObjectFromShare(getActivity(),SharePrefUtil.KEY.function_member_info);
        if (!TextUtils.isEmpty(info.private_name)) {
            meBinding.tvUsername.setText(info.private_name);
        }
        if (!TextUtils.isEmpty(info.private_headimgurl)) {
            GlideUtil.getInstance().loadCircleImage(getActivity(), info.private_headimgurl, meBinding.ivIcon);
        }

    }


    /**
     * 初始化导航菜单
     */
    private void getActiveMain() {
        mActiveData = new ArrayList<>();
        mActiveData.add(new MeActiveInfo(R.mipmap.user_for_pay, "待付款", MeActiveInfo.ACTIVE_STAY_PAY));
        mActiveData.add(new MeActiveInfo(R.mipmap.user_for_good, "待收货", MeActiveInfo.ACTIVE_GET_GOODS));
        mActiveData.add(new MeActiveInfo(R.mipmap.user_complete, "已完成", MeActiveInfo.ACTIVE_ALREADY_FINISHED));


        MeActiveAdapter ma = new MeActiveAdapter(getActivity(), mActiveData);
        ma.setActiveMainListener(new MeActiveAdapter.ActiveMeListener() {
            @Override
            public void onActiveItemClick(String activeType) {
                int currentPageIndex = 0;
                if (activeType.equals(MeActiveInfo.ACTIVE_STAY_PAY)) {
                    currentPageIndex = 1;
                } else if (activeType.equals(MeActiveInfo.ACTIVE_GET_GOODS)) {
                    currentPageIndex = 2;
                } else if (activeType.equals(MeActiveInfo.ACTIVE_ALREADY_FINISHED)) {
                    currentPageIndex = 3;
                }

                Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
                intent.putExtra(MyOrderListActivity.VIEWPAGER_INIT_PAGE_INDEX, currentPageIndex);
                getActivity().startActivity(intent);

            }

        });
        meBinding.gvMeNvg.setAdapter(ma);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if(Activity.RESULT_OK == resultCode) {
            Logger.msg(TAG,"onActivityResult ");
            if(requestCode == REQUEST_CODE_USERINFO) {
               // fillUserInfo();
            }
          return ;
        }
        if(Activity.RESULT_FIRST_USER == resultCode && requestCode == REQUEST_CODE_USERINFO){
            Message msg = Message.obtain() ;
            msg.what = 0 ;
            MeFragment.this.onFragmentListener.deliverMsg2Activity(IPullAction.LOGIN,msg);
            return ;
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_userinfo:
                this.startActivity(new Intent(getActivity(), EditPersonInfoActivity.class));
                break;
            case R.id.rl_my_vip_card:
                checkUserVip();
                break;
            /*case R.id.rl_manager_recharge_card:
                this.startActivity(new Intent(getActivity(), RechargeListActivity.class));
                break;
            case R.id.rl_manager_bank_card:
                this.startActivity(new Intent(getActivity(), BankCarkManageActivity.class));
                break;*/
            /*case R.id.rl_manager_rechargeable_record:
                this.startActivity(new Intent(getActivity(), RegMoneyHistoryActivity.class));
                break;*/
            case R.id.rl_manager_address:
                ManagerAddressActivity.SELECT_ADDRESS_REQUEST_CODE = 0;
                this.startActivity(new Intent(getActivity(), ManagerAddressActivity.class));
                break;
            case R.id.rl_user_setting:
                this.startActivity(new Intent(getActivity(), UserSettingActivity.class));
                break;
            case R.id.ll_my_order:
                this.startActivity(new Intent(getActivity(), MyOrderListActivity.class));
                break;
            case R.id.rl_shopping_car:
                this.startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
                break;
            case R.id.rl_distribution_centre:
                this.startActivity(new Intent(getActivity(), FenxiaoCenterActivity.class));
                break;
        }
    }


    private void checkUserVip() {

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getVipCardCenter(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    vipCard = GsonUtil.GsonToBean(jsonObj.optString("data"), VipCard.class);
                    Log.i(TAG, vipCard.toString());
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getActivity(), "获取数据异常");
                }

                if (code == 0) {
                    Intent intent = new Intent(getActivity(), MyVipCardActivity.class);
                    intent.putExtra(MyVipCardActivity.MY_VIP_BASE_IFNO, vipCard);
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                if (code == 1) {
                    getActivity().startActivity(new Intent(getActivity(), ResetMobileActivity.class));
                } else if (code == 2) {
                    checkIsOpenVip();
                }
                Log.i(TAG, msg);
            }
        });


    }

    private void checkIsOpenVip() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).openVipCardInfo(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                Log.e(TAG, resp);
                if(code==1){
                    Intent intent = new Intent(getActivity(), MyVipCardActivity.class);
                    intent.putExtra(MyVipCardActivity.MY_VIP_BASE_IFNO, vipCard);
                    getActivity().startActivity(intent);
                }else if(code==2){
                    getActivity().startActivity(new Intent(getActivity(), ResetMobileActivity.class));
                }


                OpenVipCardInfo openVipCardInfo = null;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    openVipCardInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), OpenVipCardInfo.class);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getActivity(), "获取数据异常");
                }
                if (openVipCardInfo == null || openVipCardInfo.rank_list == null) {
                    openVipCardNopay();
                } else {
                    Intent intent = new Intent(getActivity(), PromotionLeaguerActivity.class);
                    intent.putExtra(PromotionLeaguerActivity.IN_PROMOTION_LEAGUER_WAY, 1);
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                ToastUtils.showToastShort(getActivity(), msg);
                Log.i(TAG, msg);
                if(code==1){
                    Intent intent = new Intent(getActivity(), MyVipCardActivity.class);
                    intent.putExtra(MyVipCardActivity.MY_VIP_BASE_IFNO, vipCard);
                    getActivity().startActivity(intent);
                }else if(code==2){
                    getActivity().startActivity(new Intent(getActivity(), ResetMobileActivity.class));
                }
            }
        });
    }


    private void openVipCardNopay() {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).openVipCardNopay(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                Log.e(TAG, resp);
                Intent intent = new Intent(getActivity(), MyVipCardActivity.class);
                intent.putExtra(MyVipCardActivity.MY_VIP_BASE_IFNO, vipCard);
                getActivity().startActivity(intent);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(getActivity(), progressDialog);
                ToastUtils.showToastShort(getActivity(), msg);
                Log.i(TAG, msg);
            }
        });
    }

    private void fillUserInfo() {
       /* LoginInfo loginInfo = AccountInfo.getInstance().getLoginInfo() ;
        meBinding.tvUsername.setText(loginInfo.nickname);
        GlideUtil.getInstance().loadImage(this,loginInfo.head_img,meBinding.ivIcon);
        Logger.msg(TAG,"fill headUrl : " + loginInfo.head_img);
        if(TextUtils.isEmpty(loginInfo.mobile)|| !CommonUtils.isMobile(loginInfo.mobile)){
           meBinding.tvUsername.setText(R.string.login_username);
        } else {
            meBinding.tvUsername.setText(CommonUtils.convertPhoneEntry(loginInfo.mobile));
        }*/

    }

    @Subscribe
    public void onEventMainThread(AccountInfoEven accountInfoEven) {
        meBinding.tvUsername.setText(accountInfoEven.nickName);
    }

}
