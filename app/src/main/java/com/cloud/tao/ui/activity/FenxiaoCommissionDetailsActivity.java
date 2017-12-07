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
import com.cloud.tao.ui.fragment.fenxiao.CommissionConfirmedFragment;
import com.cloud.tao.ui.fragment.fenxiao.CommissionCongelationFragment;
import com.cloud.tao.ui.fragment.fenxiao.CommissionTixianFragment;
import com.cloud.tao.ui.fragment.fenxiao.CommissionUnconfirmedFragment;
import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/10/25
 * des: 创业佣金明细
 */
public class FenxiaoCommissionDetailsActivity extends BaseNavBackActivity {

    String mMyStoreId;
    ActivityFenxiaoCommissionDetailsBinding mFenxiaoCommissionDetailsBinding;
    public static  final String VIEWPAGER_INIT_PAGE_INDEX="viewpager_init_page_index";
    private ViewPager mViewPager;
    private FragmentPagerAdapter pagerAdapter;
    private List<Fragment> mDatas;
    private TextView mConfirmedTextView;
    private TextView mUnconfirmedTextView;
    private TextView mCongelationTextView;
    private TextView mTixianTextView;
    private View mTabline;
    private int mScrren1_4;
    private int mCurrentPageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoCommissionDetailsBinding=DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_commission_details);
        setNavDefaultBack(mFenxiaoCommissionDetailsBinding.tb);
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
                case R.id.ll_confirmed:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.ll_unconfirmed:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.ll_congelation:
                    mViewPager.setCurrentItem(2);
                    break;
                case R.id.ll_tixian:
                    mViewPager.setCurrentItem(3);
                    break;
            }
        }
    };

    private void setEvent() {
        mFenxiaoCommissionDetailsBinding.llConfirmed.setOnClickListener(mOnClickListener);
        mFenxiaoCommissionDetailsBinding.llUnconfirmed.setOnClickListener(mOnClickListener);
        mFenxiaoCommissionDetailsBinding.llCongelation.setOnClickListener(mOnClickListener);
        mFenxiaoCommissionDetailsBinding.llTixian.setOnClickListener(mOnClickListener);
    }

    private void initTabLine() {
        mTabline = findViewById(R.id.id_iv_tabline);
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        mScrren1_4 = dm.widthPixels / 4;
        ViewGroup.LayoutParams lp = mTabline.getLayoutParams();
        lp.width = mScrren1_4;
        mTabline.setLayoutParams(lp);
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.vp_commission);
        mConfirmedTextView = (TextView) findViewById(R.id.tv_confirmed);
        mUnconfirmedTextView = (TextView) findViewById(R.id.tv_unconfirmed);
        mCongelationTextView = (TextView) findViewById(R.id.tv_congelation);
        mTixianTextView = (TextView) findViewById(R.id.tv_tixian);

        mDatas = new ArrayList<>();
        Bundle mBundle=new Bundle();
        mBundle.putString("myStoreId",mMyStoreId);
        CommissionConfirmedFragment tab1 = new CommissionConfirmedFragment();
        tab1.setArguments(mBundle);
        CommissionUnconfirmedFragment tab2 = new CommissionUnconfirmedFragment();
        tab2.setArguments(mBundle);
        CommissionCongelationFragment tab3 = new CommissionCongelationFragment();
        tab3.setArguments(mBundle);
        CommissionTixianFragment tab4 = new CommissionTixianFragment();
        tab4.setArguments(mBundle);
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
                        mConfirmedTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 1:
                        mUnconfirmedTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 2:
                        mCongelationTextView.setTextColor(Color.parseColor("#F96363"));
                        break;
                    case 3:
                        mTixianTextView.setTextColor(Color.parseColor("#F96363"));
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
        mConfirmedTextView.setTextColor(Color.BLACK);
        mUnconfirmedTextView.setTextColor(Color.BLACK);
        mCongelationTextView.setTextColor(Color.BLACK);
        mTixianTextView.setTextColor(Color.BLACK);
    }

}


