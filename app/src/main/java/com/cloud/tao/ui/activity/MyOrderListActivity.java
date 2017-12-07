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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivityMyOrderListBinding;
import com.cloud.tao.ui.fragment.order.AllTagFragment;
import com.cloud.tao.ui.fragment.order.FinishedTagFragment;
import com.cloud.tao.ui.fragment.order.StayGoodsTagFragment;
import com.cloud.tao.ui.fragment.order.StayPayTagFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的订单列表
 * Created by gezi-pc on 2016/10/13.
 */

public class MyOrderListActivity extends BaseNavBackActivity implements View.OnClickListener{


    private static final String TAG = "MyOrderListActivity";
    ActivityMyOrderListBinding mActivityMyOrderListBinding;
    public static  final String VIEWPAGER_INIT_PAGE_INDEX="viewpager_init_page_index";

    private ViewPager mViewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mDatas;
    private TextView mAllTextView;
    private TextView mStayPayTextView;
    private TextView mStayGoodsTextView;
    private TextView mFinishedTextView;

    private View mTabline;
    private int mScrren1_4;

    private int mCurrentPageIndex = 0;

    private LinearLayout ll_all;
    private LinearLayout ll_stay_pay;
    private LinearLayout ll_stay_goods;
    private LinearLayout ll_finished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityMyOrderListBinding =  DataBindingUtil.setContentView(this, R.layout.activity_my_order_list);
        setNavDefaultBack(mActivityMyOrderListBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityMyOrderListBinding.loadding.showLoadSuccess();
        initTabLine();
        initView();
        setEvent();

        mCurrentPageIndex = getIntent().getIntExtra(VIEWPAGER_INIT_PAGE_INDEX,0);
        mViewPager.setCurrentItem(mCurrentPageIndex);

    }



    private void setEvent() {

        mActivityMyOrderListBinding.llAll.setOnClickListener(this);
        mActivityMyOrderListBinding.llStayPay.setOnClickListener(this);
        mActivityMyOrderListBinding.llStayGoods.setOnClickListener(this);
        mActivityMyOrderListBinding.llFinished.setOnClickListener(this);
    }

    private void initTabLine() {
        mTabline = findViewById(R.id.id_iv_tabline);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        mScrren1_4 = dm.widthPixels / 4;
        LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScrren1_4;
        mTabline.setLayoutParams(lp);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mAllTextView = (TextView) findViewById(R.id.tv_all);
        mStayPayTextView = (TextView) findViewById(R.id.tv_stay_pay);
        mStayGoodsTextView = (TextView) findViewById(R.id.tv_stay_goods);
        mFinishedTextView = (TextView) findViewById(R.id.tv_finished);

        ll_all = (LinearLayout) findViewById(R.id.ll_all);
        ll_stay_pay = (LinearLayout) findViewById(R.id.ll_stay_pay);
        ll_stay_goods = (LinearLayout) findViewById(R.id.ll_stay_goods);
        ll_finished = (LinearLayout) findViewById(R.id.ll_finished);
        mDatas = new ArrayList<Fragment>();
        AllTagFragment tab1 = new AllTagFragment();
        StayPayTagFragment tab2 = new StayPayTagFragment();
        StayGoodsTagFragment tab3 = new StayGoodsTagFragment();
        FinishedTagFragment tab4 = new FinishedTagFragment();
        mDatas.add(tab1);
        mDatas.add(tab2);
        mDatas.add(tab3);
        mDatas.add(tab4);
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
                        mAllTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 1:
                        mStayPayTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 2:
                        mStayGoodsTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 3:
                        mFinishedTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                }
                mCurrentPageIndex = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPx) {

                LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
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
                mTabline.setLayoutParams(lp);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mViewPager.setOffscreenPageLimit(4);
    }

    protected void resetTextView() {
        mAllTextView.setTextColor(Color.BLACK);
        mStayPayTextView.setTextColor(Color.BLACK);
        mStayGoodsTextView.setTextColor(Color.BLACK);
        mFinishedTextView.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_all:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.ll_stay_pay:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.ll_stay_goods:
                mViewPager.setCurrentItem(2);
                break;
            case R.id.ll_finished:
                mViewPager.setCurrentItem(3);
                break;
        }

    }


}
