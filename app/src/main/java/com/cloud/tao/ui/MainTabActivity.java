package com.cloud.tao.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.event.UserCenterEven;
import com.cloud.tao.bean.etc.upgrade.UpgradeBean;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.activity.FenxiaoCenterActivity;
import com.cloud.tao.ui.activity.upgrade.UpgradeActivity;
import com.cloud.tao.ui.fragment.MainFragment;
import com.cloud.tao.R;
import com.cloud.tao.callback.IOnFragmentListener;
import com.cloud.tao.callback.IPullAction;
import com.cloud.tao.framwork.base.BaseFragmentActivity;
import com.cloud.tao.ui.fragment.ActiveFragment;
import com.cloud.tao.ui.fragment.MeFragment;
import com.cloud.tao.ui.widget.CustomRadioButton;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.bean.etc.event.NetWorkActionState;
import com.cloud.tao.util.NetworkStatUtils;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.eventbusutil.EventBusUtil;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by created on 2016/9/25
 * des: 主页
 */
public class MainTabActivity extends BaseFragmentActivity implements IOnFragmentListener {

    private static final String TAG = "MainTabActivity";
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //定义数组来存放Fragment界面
    private Class mFragmentArray[] = {MainFragment.class,ActiveFragment.class,MeFragment.class}; //,AllGoodsActivity.class
    private String mTabTags[] = {"tagMain", "tagActive", "tagMe" }; //,"tagAllGoods"
    private static final int INDEX_MAIN=0,INDEX_ACTIVE=1,INDEX_ME=3; //,INDEX_ALL_GOODS=1
    private int showIndex;
    private RadioGroup mRgTabs ;
    private TextView rbFenxiao;
    MallApplication application;

    private BroadcastReceiver networkBroadcast= new BroadcastReceiver() { //注册网络监听
        @Override
        public void onReceive(Context context, Intent intent) {
            if(NetworkStatUtils.isNetworkAvailable(context)){
                checkUpgrade();
            }else{
                EventBus.getDefault().post(new NetWorkActionState(true));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_tab_main);
        rbFenxiao= (TextView) findViewById(R.id.rb_fenxiao);
        rbFenxiao.setOnClickListener(mOnRbFenxiaoClickListener);
        registerNetworkReceiver();
        EventBusUtil.register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews() {
        setSwipeBackEnable(false);

        Intent intent = getIntent() ;
        intShowIndex(intent);
        application=(MallApplication)getApplication();

        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        //设置选项卡不可见
        mTabHost.getTabWidget().setVisibility(View.GONE);
        initTabViews() ;
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        intShowIndex(intent);
        ((CustomRadioButton)mRgTabs.getChildAt(showIndex)).setChecked(true);

        boolean isRefresh=intent.getBooleanExtra("isRefresh",false);
        if(null!=intent&&isRefresh){
            boolean isToMyStore=intent.getBooleanExtra("isToMyStore",false);
            MainFragment mMainFragment=(MainFragment)getSupportFragmentManager().findFragmentByTag(mTabTags[0]);
            Message mg=new Message();
            if(isToMyStore){
                String mStoreId=intent.getStringExtra("storeId");
                mg.what=MainFragment.CODE_REFRESH_DATA_MY;
                mg.obj=mStoreId;
            }else{
                mg.what=MainFragment.CODE_REFRESH_DATA;
            }
            mMainFragment.mUiHandler.sendMessage(mg);
        }
    }

    private void intShowIndex(Intent intent){
        if(null != intent) {
            showIndex = intent.getIntExtra("showIndex" ,INDEX_MAIN) ;
        }
        if(showIndex < 0 || showIndex >= mFragmentArray.length) {
            showIndex = INDEX_MAIN ;
        }
    }

    private void initTabViews() {
        //得到fragment的个数
        int count = mFragmentArray.length;
        for(int i = 0; i < count; i++){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTabTags[i]).setIndicator(i+"") ;
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
        }

        mRgTabs = ((RadioGroup)findViewById(R.id.rg_tab)) ;
        mRgTabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                switch (id) {
                    case R.id.rb_sort:
                        showIndex = INDEX_ACTIVE ;
                        mTabHost.setCurrentTabByTag(mTabTags[1]);
                        break;
                   /* case R.id.rb_all_goods:
                        showIndex = INDEX_ALL_GOODS ;
                        mTabHost.setCurrentTabByTag(mTabTags[1]);
                        break;*/
                    case R.id.rb_main:
                        showIndex = INDEX_MAIN;
                        mTabHost.setCurrentTabByTag(mTabTags[0]);
                        break;
                    case R.id.rb_personal:
                        showIndex = INDEX_ME ;
                        mTabHost.setCurrentTabByTag(mTabTags[2]);
                        break;
                }
            }
        });

        ((CustomRadioButton)mRgTabs.getChildAt(showIndex)).setChecked(true);
    }

    /**
     * 检查版本更新
     */
    private void checkUpgrade() {
        final int versionCode = application.appVersionCode();
        MallApplication.instance().getModel(AppModel.class).getAppVersionInfo(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (TextUtils.isEmpty(resp)) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    UpgradeBean upgradeBean = GsonUtil.GsonToBean(jsonObj.optString("data"), UpgradeBean.class);
                    if (Integer.valueOf(upgradeBean.version) > versionCode) {
                        Intent intent = new Intent(MainTabActivity.this, UpgradeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("upgradeBean", upgradeBean);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                Log.i(TAG, "onFail: 网络错误");
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
            }
        });
    }

    private NoDoubleClickListener mOnRbFenxiaoClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.rb_fenxiao:
                    startActivity(new Intent(MainTabActivity.this,FenxiaoCenterActivity.class));
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private long lastClickBackTime ;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() - lastClickBackTime < 2 * 1000) {
            finish();
        } else {
            lastClickBackTime = System.currentTimeMillis() ;
            ToastUtils.showToastShort(getApplicationContext(),getString(R.string.tip_exit_app));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void deliverMsg2Activity(IPullAction action, Message msg) {
        //根据 message.what 判断是tabFragment哪一个fragment传来的消息
        /*if(action == IPullAction.LOGIN) {
            showIndex =  msg.what  ;
            intShowIndex(null);
            ((CustomRadioButton)mRgTabs.getChildAt(showIndex)).setChecked(true);
            ActionJumpUtil.jumpToCheckLoginState(MainTabActivity.this,REQUEST_LOGIN);
        }*/
    }

    @Override
    protected void onDestroy() {
        unRegisterNetworkReceiver();
        EventBusUtil.unRegister(this);
        super.onDestroy();
    }

    private void registerNetworkReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkBroadcast, filter);
    }

    private void unRegisterNetworkReceiver() {
        this.unregisterReceiver(networkBroadcast);
    }


    @Subscribe
    public void onEventMainThread(UserCenterEven mUserCenterEven) {
        showIndex = INDEX_ME ;
        mTabHost.setCurrentTabByTag(mTabTags[2]);
        ((CustomRadioButton)mRgTabs.getChildAt(showIndex)).setChecked(true);
    }
}
