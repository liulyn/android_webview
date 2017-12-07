package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityFenxiaoCommissionDetailsBinding;
import com.cloud.tao.databinding.ActivityFenxiaoOrdersBinding;
import com.cloud.tao.ui.fragment.fenxiao.FenxiaoOrderAllFragment;
import com.cloud.tao.ui.fragment.fenxiao.FenxiaoOrderSendFragment;
import com.cloud.tao.ui.fragment.fenxiao.FenxiaoOrderSuccessFragment;
import com.cloud.tao.ui.fragment.fenxiao.FenxiaoOrderWaitPayFragment;
import com.cloud.tao.ui.fragment.fenxiao.FenxiaoOrderWaitSendFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/10/25
 * des: 创业订单
 */
public class FenxiaoOrderActivity extends BaseNavBackActivity {

    String mMyStoreId;
    ActivityFenxiaoOrdersBinding mFenxiaoOrdersBinding;
    public static  final String VIEWPAGER_INIT_PAGE_INDEX="viewpager_init_page_index";
    private ViewPager mViewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mDatas;
    private TextView mTvFenxiaoOrderAll;
    private TextView tvFenxiaoOrderWaitPay;
    private TextView tvFenxiaoOrderWaitSend;
    private TextView tvFenxiaoOrderSended;
    private TextView mTvFenxiaoOrderSuccess;
    private View mTabline;
    private int mScrren1_4;
    private int mCurrentPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoOrdersBinding=DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_orders);
        setNavDefaultBack(mFenxiaoOrdersBinding.tb);
        mMyStoreId = getIntent().getExtras().getString("myStoreId");
        super.onCreate(savedInstanceState);
        initTabLine();
        initView();
        setEvent();
        mCurrentPageIndex = getIntent().getIntExtra(VIEWPAGER_INIT_PAGE_INDEX,0);
        mViewPager.setCurrentItem(mCurrentPageIndex);
    }

    private NoDoubleClickListener mOnClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_fenxiao_order_all:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.ll_fenxiao_order_wait_pay:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.ll_fenxiao_order_wait_send:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.ll_fenxiao_order_sended:
                    mViewPager.setCurrentItem(3);
                    break;
                case R.id.ll_fenxiao_order_success:
                    mViewPager.setCurrentItem(4);
                    break;
            }
        }
    };

    private void setEvent() {
        mFenxiaoOrdersBinding.llFenxiaoOrderAll.setOnClickListener(mOnClickListener);
        mFenxiaoOrdersBinding.llFenxiaoOrderWaitPay.setOnClickListener(mOnClickListener);
        mFenxiaoOrdersBinding.llFenxiaoOrderWaitSend.setOnClickListener(mOnClickListener);
        mFenxiaoOrdersBinding.llFenxiaoOrderSended.setOnClickListener(mOnClickListener);
        mFenxiaoOrdersBinding.llFenxiaoOrderSuccess.setOnClickListener(mOnClickListener);
    }

    private void initTabLine() {
        mTabline = findViewById(R.id.id_iv_tabline);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        mScrren1_4 = dm.widthPixels / 5;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScrren1_4;
        mTabline.setLayoutParams(lp);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_fnexiao_order);
        mTvFenxiaoOrderAll = (TextView) findViewById(R.id.tv_fenxiao_order_all);
        tvFenxiaoOrderWaitPay = (TextView) findViewById(R.id.tv_fenxiao_order_wait_pay);
        tvFenxiaoOrderWaitSend = (TextView) findViewById(R.id.tv_fenxiao_order_wait_send);
        tvFenxiaoOrderSended = (TextView) findViewById(R.id.tv_fenxiao_order_sended);
        mTvFenxiaoOrderSuccess= (TextView) findViewById(R.id.tv_fenxiao_order_success);
        mDatas = new ArrayList<>();
        Bundle mBundle=new Bundle();
        mBundle.putString("myStoreId",mMyStoreId);
        FenxiaoOrderAllFragment tab1 = new FenxiaoOrderAllFragment();
        tab1.setArguments(mBundle);
        FenxiaoOrderWaitPayFragment tab2 = new FenxiaoOrderWaitPayFragment();
        tab2.setArguments(mBundle);
        FenxiaoOrderWaitSendFragment tab3 = new FenxiaoOrderWaitSendFragment();
        tab3.setArguments(mBundle);
        FenxiaoOrderSendFragment tab4 = new FenxiaoOrderSendFragment();
        tab4.setArguments(mBundle);
        FenxiaoOrderSuccessFragment tab5 = new FenxiaoOrderSuccessFragment();
        tab5.setArguments(mBundle);
        mDatas.add(tab1);
        mDatas.add(tab2);
        mDatas.add(tab3);
        mDatas.add(tab4);
        mDatas.add(tab5);
        pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mDatas.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return mDatas.get(arg0);
            }
        };
        mViewPager.setAdapter(pagerAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                switch (position) {
                    case 0:
                        mTvFenxiaoOrderAll.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 1:
                        tvFenxiaoOrderWaitPay.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 2:
                        tvFenxiaoOrderWaitSend.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 3:
                        tvFenxiaoOrderSended.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 4:
                        mTvFenxiaoOrderSuccess.setTextColor(Color.parseColor("#F96363"));
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx) {

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabline
                        .getLayoutParams();
                if (mCurrentPageIndex == 0 && position == 0) {// 0->1
                    lp.leftMargin = (int) (positionOffset * mScrren1_4 + mCurrentPageIndex
                            * mScrren1_4);
                } else if (mCurrentPageIndex == 1 && position == 0) {// 1->0

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + (positionOffset - 1)
                            * mScrren1_4);
                } else if (mCurrentPageIndex == 1 && position == 1) {// 1->2
                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + positionOffset
                            * mScrren1_4);
                } else if (mCurrentPageIndex == 2 && position == 1) {// 2->1

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + (positionOffset - 1)
                            * mScrren1_4);
                }

                //后面加的代码

                else if (mCurrentPageIndex == 2 && position == 2) {// 2->3

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + positionOffset
                            * mScrren1_4);
                }else if (mCurrentPageIndex == 3 && position == 3) {// 2->3

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + positionOffset
                            * mScrren1_4);
                }else if (mCurrentPageIndex == 3 && position == 2) {// 3->2

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + (positionOffset - 1)
                            * mScrren1_4);
                }

                //后面加的代码

                else if (mCurrentPageIndex == 3 && position == 3) {// 2->3

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + positionOffset
                            * mScrren1_4);
                }else if (mCurrentPageIndex == 4 && position == 4) {// 2->3

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + positionOffset
                            * mScrren1_4);
                }else if (mCurrentPageIndex == 4 && position == 3) {// 3->2

                    lp.leftMargin = (int) (mCurrentPageIndex * mScrren1_4 + (positionOffset - 1)
                            * mScrren1_4);
                }

                mTabline.setLayoutParams(lp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setOffscreenPageLimit(5);
    }

    protected void resetTextView() {
        mTvFenxiaoOrderAll.setTextColor(Color.BLACK);
        tvFenxiaoOrderWaitPay.setTextColor(Color.BLACK);
        tvFenxiaoOrderWaitSend.setTextColor(Color.BLACK);
        tvFenxiaoOrderSended.setTextColor(Color.BLACK);
        mTvFenxiaoOrderSuccess.setTextColor(Color.BLACK);
    }

}


