package com.cloud.tao.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloud.tao.BuildConfig;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.bean.etc.MainActiveInfo;
import com.cloud.tao.bean.etc.MainGoodsModelInfo;
import com.cloud.tao.bean.etc.MainResultInfo;
import com.cloud.tao.bean.etc.OpenVipCardInfo;
import com.cloud.tao.bean.etc.VipCard;
import com.cloud.tao.bean.etc.event.UserCenterEven;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ItemMainGoodsHeaderBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.activity.FenxiaoCenterActivity;
import com.cloud.tao.ui.activity.GoodsDetailsActivity;
import com.cloud.tao.ui.activity.MyOrderListActivity;
import com.cloud.tao.ui.activity.MyVipCardActivity;
import com.cloud.tao.ui.activity.PayStandActivity;
import com.cloud.tao.ui.activity.PromotionLeaguerActivity;
import com.cloud.tao.ui.activity.ResetMobileActivity;
import com.cloud.tao.ui.adapter.etc.MainActiveAdapter;
import com.cloud.tao.ui.adapter.etc.MainGoodsActiveDataAdapter;
import com.cloud.tao.ui.widget.GridItemDecoration;
import com.cloud.tao.ui.widget.LoadMoreGridRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseShowFragment;
import com.cloud.tao.bean.etc.NavImgInfo;
import com.cloud.tao.databinding.FragmentMainBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.activity.SearchActivity;
import com.cloud.tao.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import me.nereo.multi_image_selector.utils.DimensionUtil;

/**
 * sunny created at 2016/9/24
 * des: 主页
 */
public class MainFragment extends BaseShowFragment{

    private static final String TAG = "MainFragment";
    private static final int CODE_CIRCLE_TIMER = BASE_CODE + 1;
    public static final int CODE_REFRESH_DATA_MY = BASE_CODE + 2;
    public static final int CODE_REFRESH_DATA = BASE_CODE + 3;
    private final int CIRCLE_TIME = 5000 ;
    FragmentMainBinding mainBinding ;
    ItemMainGoodsHeaderBinding mainHeaderBinding;
    boolean isStoreHome=true; //是否总店首页
    private List<MainActiveInfo> mActiveData;
   // private MainGoodsModelAdapter mMainGoodsModelAdapter;
   int pageIndex = 1;
    private MainGoodsActiveDataAdapter mMainGoodsActiveDataAdapter;
    private List<NavImgInfo> mNavImgInfos;
    private List<MainGoodsModelInfo> mGoodsTitles;
    private int lastPagePosition = 0 ;
    private int currentVpSize ;
    private Activity mContext;
    private Dialog loadingDialog;
    private VipCard vipCard;

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            int size = mainHeaderBinding.llNav.getChildCount() ;
            ((ImageView)mainHeaderBinding.llNav.getChildAt(position%size)).setImageResource(R.drawable.checked_usb_open);
            ((ImageView)mainHeaderBinding.llNav.getChildAt(lastPagePosition%size)).setImageResource(R.drawable.checked_usb_close);
            lastPagePosition = position ;
            mUiHandler.removeMessages(CODE_CIRCLE_TIMER);
            mUiHandler.sendEmptyMessageDelayed(CODE_CIRCLE_TIMER,CIRCLE_TIME) ;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case CODE_CIRCLE_TIMER:
                int position = mainHeaderBinding.vpNav.getCurrentItem() + 1 ;
                if(position > currentVpSize - 1) {
                    position = 0 ;
                }
                mainHeaderBinding.vpNav.setCurrentItem(position);
                break ;
            case CODE_REFRESH_DATA_MY:
                String mMyStoreId=msg.obj.toString();
                isStoreHome=false;
                getNavGoods(true,mMyStoreId);
                break;
            case CODE_REFRESH_DATA:
                isStoreHome=true;
                getNavGoods(true, BuildConfig.STORE_ID);
                break;
            default:break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            /*if(requestCode == REQUEST_LOGIN) { //进入时，登录成功后的回调
                 ActionJumpUtil.jumpToPlayGame(mContext,MainFragment.this,REQUEST_LOGIN,gameId);
            }*/
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getActivity();
    }

    //进入搜索界面
    private View.OnClickListener onSreachClickListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(mContext, SearchActivity.class));
        }
    };

    private NoDoubleClickListener mOnDoubleClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                /*case R.id.tb: //进入搜索界面
                    break;*/
            }
        }
    };

    @Override
    public void onDestroy() {
        if(loadingDialog!=null){
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
        }
        super.onDestroy();
    }

    @Override
    protected View initLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container,false) ;
        mainHeaderBinding= DataBindingUtil.inflate(inflater, R.layout.item_main_goods_header,container,false) ;

        mNavImgInfos=new ArrayList<>();
        mGoodsTitles=new ArrayList<>();

        mainHeaderBinding.vpNav.addOnPageChangeListener(mPageChangeListener);
        mainBinding.tb.setOnRightNavClickListener(onSreachClickListner);
       /* mainBinding.llMainAllGoods.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void noDoubleClick(View v) {
                startActivity(new Intent(mContext,AllGoodsActivity.class));
            }
        });*/

        getNavGoods(false,null);//获取首页轮播和商品条目
        getActiveMain();
        getGoodsModel();
        return  mainBinding.getRoot() ;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * 初始化导航菜单
     */
    private void getActiveMain(){
        mActiveData= new ArrayList<>();
        mActiveData.add(new MainActiveInfo(R.mipmap.ic_etc_main_checkstand, "云豆购买", MainActiveInfo.ACTIVE_PAYSTAND));
        mActiveData.add(new MainActiveInfo(R.mipmap.ic_etc_main_distribution, "创业中心", MainActiveInfo.ACTIVE_DISTRIBUTION));
        mActiveData.add(new MainActiveInfo(R.mipmap.ic_etc_main_order, "我的订单", MainActiveInfo.ACTIVE_ORDER));
        mActiveData.add(new MainActiveInfo(R.mipmap.ic_etc_main_user, "用户中心", MainActiveInfo.ACTIVE_USER));
        MainActiveAdapter mMainActiveAdapter =new MainActiveAdapter(mContext,mActiveData);
        mMainActiveAdapter.setActiveMainListener(new MainActiveAdapter.ActiveMainListener() {
            @Override
            public void onActiveItemClick(String activeType) {
                if(activeType.equals(MainActiveInfo.ACTIVE_USER)){
                    //EventBus.getDefault().post(new UserCenterEven());
                    checkUserVip();
                }else if(activeType.equals(MainActiveInfo.ACTIVE_DISTRIBUTION)){
                    startActivity(new Intent(mContext,FenxiaoCenterActivity.class));
                }else if(activeType.equals(MainActiveInfo.ACTIVE_ORDER)){
                    startActivity(new Intent(mContext,MyOrderListActivity.class));
                }else if(activeType.equals(MainActiveInfo.ACTIVE_PAYSTAND)){
                   // startActivity(new Intent(mContext,CashierTagActivity.class));
                    startActivity(new Intent(mContext,PayStandActivity.class));
                }
            }
        });
        mainHeaderBinding.gvMainActive.setAdapter(mMainActiveAdapter);
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

    private void getGoodsModel(){
        /*if(!isRefresh){
            mMainGoodsModelAdapter=new MainGoodsModelAdapter(mContext,mGoodsTitles);
            mMainGoodsModelAdapter.setOnMainGoodsModelListener(new MainGoodsModelAdapter.OnMainGoodsModelListener(){
                @Override
                public void onMainGoodsItemClick(String goodsId) {
                    Intent intent=new Intent(mContext, GoodsDetailsActivity.class);
                    intent.putExtra("goodsId",goodsId);
                    startActivity(intent);
                }
            });
            mainBinding.lvMainGoodsModel.setAdapter(mMainGoodsModelAdapter);
        }else{
            mMainGoodsModelAdapter.notifyDataSetChanged();
        }*/

        mMainGoodsActiveDataAdapter = new MainGoodsActiveDataAdapter(mContext, R.layout.item_main_goods_active_item,null) ;
        mainBinding.lvMainGoodsModel.addItemDecoration(new GridItemDecoration(mContext));
        mMainGoodsActiveDataAdapter.setOnePageNum(Constants.PageSize);
        mMainGoodsActiveDataAdapter.addHeaderView(mainHeaderBinding.getRoot());
        mMainGoodsActiveDataAdapter.setIMainGoodsDataAdapterFace(new MainGoodsActiveDataAdapter.IMainGoodsDataAdapterFace(){
            @Override
            public void ItemClick(String goodsId) {
                Intent intent=new Intent(mContext, GoodsDetailsActivity.class);
                intent.putExtra("goodsId",goodsId);
                startActivity(intent);
            }
        });
        mainBinding.lvMainGoodsModel.setAdapter(mMainGoodsActiveDataAdapter);
        mainBinding.lvMainGoodsModel.setOnLoadMoreListener(new LoadMoreGridRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getAllGoodsData(true);
            }
        });
        loadInitAllGoodsData();
        mainBinding.swMainGoodsModel.setColorSchemeResources(R.color.nag_bg);
        mainBinding.swMainGoodsModel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadInitAllGoodsData();
            }
        });
    }

    protected void loadInitAllGoodsData() {
        pageIndex = 1;
        getAllGoodsData(false);
    }

    public void getAllGoodsData(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getAllGoodsList(TAG, null, Constants.PageSize, pageIndex,
                new EntityCallBack<String>(new TypeToken<String>() {
                }) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        if (resp == null) return;
                        try {
                            mainBinding.swMainGoodsModel.setRefreshing(false);
                            JSONObject jsonObj = new JSONObject(resp).optJSONObject("data");
                            int num=jsonObj.optInt("num");
                            if(num==0){
                                setData2List(null, isLoadMore);
                                return;
                            }
                            ArrayList<GoodsInfo> goodsList = GsonUtil.fromJsonList(jsonObj.optString("goods_list"), GoodsInfo.class);
                            setData2List(goodsList, isLoadMore);
                        } catch (JSONException e) {
                            ToastUtils.showToastShort(mContext, "获取数据异常");
                        }
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ToastUtils.showToastShort(mContext, msg);
                    }
                });
    }

    private void setData2List(ArrayList<GoodsInfo> resp, boolean isLoadMore) {
        pageIndex ++ ;
        if(isLoadMore) {
            mMainGoodsActiveDataAdapter.addAll(resp);
        } else {
            mMainGoodsActiveDataAdapter.replace(resp);
        }
    }

    private void getNavGoods(final boolean isRefresh, String storeId) {
        loadingDialog = ToastUtils.getLoadingDialog(mContext, getString(R.string.dialog_common_loading), true);
        loadingDialog.show();
        MallApplication.getInstance().getModel(AppModel.class).getNavImgAndGoods(TAG,isRefresh,storeId,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                loadingDialog.dismiss();
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    MainResultInfo mainResultInfo=GsonUtil.GsonToBean(jsonObj.optString("data"),MainResultInfo.class);
                    String myStoreId=jsonObj.optString("my_store_id");
                    SharePrefUtil.saveString(MallApplication.instance(),SharePrefUtil.KEY.function_myStoreId,myStoreId);
                    parseDataInfo(mainResultInfo,isRefresh);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(mContext,"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                loadingDialog.dismiss();
                ToastUtils.showToastShort(mContext,msg);
            }

            @Override
            public void onAfter(int id) {
                loadingDialog.dismiss();
            }
        });

    }

    private void parseDataInfo(MainResultInfo mainResultInfo,boolean isRefresh){
        if(isRefresh){
            if(isStoreHome){
                mainBinding.tb.setTitleText("主页");
            }else{
                mainBinding.tb.setTitleText("主页（我的小店）");
            }
        }
        List<NavImgInfo> navImgInfos=mainResultInfo.slider;
        mNavImgInfos.clear();
        mNavImgInfos.addAll(navImgInfos);
        fillViewpager(isRefresh);

        /*List<MainGoodsModelInfo> goodsTitles=mainResultInfo.title;
        mGoodsTitles.clear();
        mGoodsTitles.addAll(goodsTitles);*/
    }

    private void fillViewpager(boolean isRefresh) {
        List<String> urls = new ArrayList<String>() ;
        currentVpSize = mNavImgInfos.size() ;
        for(int i = 0 ; i < currentVpSize ; i++) {
            urls.add(mNavImgInfos.get(i).image);
        }
        if(isRefresh){
            lastPagePosition=0;
            mainHeaderBinding.vpNav.removeAllViews();
            mainHeaderBinding.llNav.removeAllViews();
        }
        CommonUtils.initViewPage(urls,getContext(),mainHeaderBinding.vpNav,mainHeaderBinding.llNav,null,lastPagePosition);
        mUiHandler.sendEmptyMessageDelayed(CODE_CIRCLE_TIMER, CIRCLE_TIME) ;
    }


}
