package com.cloud.tao.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.framwork.Logger;


/**
 * author:janecer on 2016/2/12 12:45
 *
 * 提供三种状态
 * 正在加载的显示状态
 * 加载异常的显示状态
 * 加载成功的状态（隐藏此View）
 *
 * 默认显示正在加载状态
 */

public class LoadTipLayout extends RelativeLayout {

    /* 正在加载的显示状态 */
    private LinearLayout mLLLoadingLayout ;
    private LoaddingImageView mIvLoadding ;
    private TextView mTvLoadding ;

    /* 加载异常的显示状态 */
    private LinearLayout mLLLoadExceptionLayout;
    private ImageView mIvLoadException;
    private TextView mTvLoadExceptionTip;
    private TextView mTvReloadClickTip;

    /* 自定义属性值的获取 临时存放的变量申明 */
    private String mLoadingStr ;
    private int mNormalTextColor ;
    private int mClickTextColor ;
    private String mLoadExceptionStr ;
    private String mLoadExceptionClickStr ;
    private int mIconLoaddingSrc, mIconLoadExceptionSrc;

    public LoadTipLayout(Context context) {
        this(context, null);
    }

    public LoadTipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public LoadTipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setBackgroundColor(getResources().getColor(android.R.color.white));

        TypedArray a = context.obtainStyledAttributes(attrs , R.styleable.LoadTipLayout ) ;
        mLoadingStr = a.getString(R.styleable.LoadTipLayout_tip_loadding_text) ;
        mLoadExceptionStr = a.getString(R.styleable.LoadTipLayout_tip_exception_text) ;
        mLoadExceptionClickStr = a.getString(R.styleable.LoadTipLayout_tip_exception_click_text) ;
        mNormalTextColor = a.getColor(R.styleable.LoadTipLayout_tip_text_color, context.getResources().getColor(android.R.color.darker_gray)) ;
        mClickTextColor =  a.getColor(R.styleable.LoadTipLayout_tip_exception_click_color, context.getResources().getColor(android.R.color.holo_blue_dark)) ;
        mIconLoaddingSrc = a.getResourceId(R.styleable.LoadTipLayout_tip_loadding_src, R.drawable.shadow) ;
        mIconLoadExceptionSrc = a.getResourceId(R.styleable.LoadTipLayout_tip_exception_src ,R.mipmap.ic_launcher) ;
        a.recycle();

        initLoadingLayout() ;

        initLoadExceptionLayout() ;

        initViewValues() ;

        showLoadding();
    }

    /**
     * 将资源中获取到的值设置到相关控件中
     */
    private void initViewValues() {

        Logger.msg(mLoadingStr + "   " +mLoadExceptionStr + "  " + mLoadExceptionClickStr );
        mTvLoadding.setTextColor(mNormalTextColor);
        mTvLoadExceptionTip.setTextColor(mNormalTextColor);
        mTvReloadClickTip.setTextColor(mClickTextColor);

        mIvLoadding.setImageResource(mIconLoaddingSrc);
        mTvLoadding.setText(mLoadingStr);

        mIvLoadException.setImageResource(mIconLoadExceptionSrc);
        mTvLoadExceptionTip.setText(mLoadExceptionStr);
        mTvReloadClickTip.setText(mLoadExceptionClickStr);
    }



    /**
     * 初始化加载异常布局
     */
    private void initLoadExceptionLayout() {
        mLLLoadExceptionLayout = new LinearLayout(getContext()) ;
        mLLLoadExceptionLayout.setOrientation(LinearLayout.VERTICAL);

        mTvLoadExceptionTip = new TextView(getContext()) ;
        mTvReloadClickTip = new TextView(getContext()) ;
        mIvLoadException = new ImageView(getContext()) ;
        mIvLoadException.setScaleType(ImageView.ScaleType.CENTER);

        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT) ;
        ll.gravity = Gravity.CENTER ;
        mLLLoadExceptionLayout.addView(mIvLoadException, ll);
        mLLLoadExceptionLayout.addView(mTvLoadExceptionTip, ll);
        mLLLoadExceptionLayout.addView(mTvReloadClickTip, ll);

        LayoutParams ll_exception =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        ll_exception.addRule(CENTER_IN_PARENT);
        this.addView(mLLLoadExceptionLayout, ll_exception);
    }

    /**
     * 初始化正在加载布局
     */
    private void initLoadingLayout() {
        mLLLoadingLayout = new LinearLayout(getContext()) ;
        mLLLoadingLayout.setOrientation(LinearLayout.VERTICAL);
        mTvLoadding = new TextView(getContext()) ;
        mIvLoadding = new LoaddingImageView(getContext()) ;
        mIvLoadding.setScaleType(ImageView.ScaleType.CENTER);
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT) ;
        ll.gravity = Gravity.CENTER ;
        mLLLoadingLayout.addView(mIvLoadding, ll);
        mLLLoadingLayout.addView(mTvLoadding, ll);

        LayoutParams ll_loadding =  new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) ;
        ll_loadding.addRule(CENTER_IN_PARENT);
        this.addView(mLLLoadingLayout, ll_loadding);
    }

    /**
     * 显示正在加载View
     */
    public void showLoadding(){
        this.setVisibility(View.VISIBLE);
        mLLLoadingLayout.setVisibility(View.VISIBLE);
        mIvLoadding.setVisibility(View.VISIBLE);
        mLLLoadExceptionLayout.setVisibility(View.GONE);
    }

    /**
     * 显示加载异常View
     */
    public void showLoadException () {
        mLLLoadingLayout.setVisibility(View.GONE);
        mIvLoadding.setVisibility(View.GONE);
        mLLLoadExceptionLayout.setVisibility(View.VISIBLE);
    }

    public void showLoadException(String exceptionStr) {
        mTvLoadExceptionTip.setText(exceptionStr);
        showLoadException();
    }

    public void showLoadSuccess(){
        this.setVisibility(View.GONE);
        mLLLoadingLayout.setVisibility(View.GONE);
        mIvLoadding.setVisibility(View.GONE);
        mLLLoadExceptionLayout.setVisibility(View.GONE);
    }

    /**
     * 为加载异常设置点击回调监听
     * @param onClickListner
     */
    public void setOnReloadClickListner(OnClickListener onClickListner){
        this.mIvLoadException.setOnClickListener(onClickListner);
    }

}
