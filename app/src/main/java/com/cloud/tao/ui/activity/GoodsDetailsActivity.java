package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.CountDownInfo;
import com.cloud.tao.bean.etc.GoodsActivityInfo;
import com.cloud.tao.bean.etc.GoodsDetailsInfo;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.callback.GoodsAttrCallBack;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.databinding.ActivityGoodsDetailsBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.MainTabActivity;
import com.cloud.tao.ui.widget.CountDownView;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.util.DateUtil;
import com.cloud.tao.util.GoodsAttrWindow;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.utils.DimensionUtil;

/**
 * sunny created at 2016/10/18
 * des: 商品详情
 */
public class GoodsDetailsActivity extends BaseNavBackActivity implements GoodsAttrCallBack {

    ActivityGoodsDetailsBinding mGoodsDetailsBinding;
    GoodsAttrWindow mGoodsAttrWindow;
    GoodsDetailsInfo mGoodsDetailsInfo;
    GoodsInfo mGoodsInfo;
    String mGoodsId;
    private int lastPagePosition = 0 ;
    private int currentVpSize ;
    private static final String TAG = "GoodsDetailsActivity";
    private static final int CODE_CIRCLE_TIMER = BASE_CODE + 1;
    private final int CIRCLE_TIME = 5000 ;
    public List<String> goods_picture_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoodsDetailsBinding=DataBindingUtil.setContentView(this, R.layout.activity_goods_details);
        setNavDefaultBack(mGoodsDetailsBinding.tb);
        mGoodsDetailsBinding.vpNav.addOnPageChangeListener(mPageChangeListener);
        mGoodsDetailsBinding.rlGoodsDetailsAttr.setOnClickListener(mOnInToAttrClickListener);
        mGoodsDetailsBinding.ivGoodsDetailsHome.setOnClickListener(mOnInToAttrClickListener);
        mGoodsDetailsBinding.btGoodsDetailsBuyNow.setOnClickListener(mOnInToAttrClickListener);
        mGoodsDetailsBinding.ivGoodsDetailsCard.setOnClickListener(mOnInToAttrClickListener);
        mGoodsDetailsBinding.btGoodsDetailsAddShoppingCart.setOnClickListener(mOnInToAttrClickListener);
        super.onCreate(savedInstanceState);
        getGoodsDetails();
    }

    private NoDoubleClickListener mOnInToAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()){
                case R.id.rl_goods_details_attr:
                    if(null!=mGoodsAttrWindow) {
                        mGoodsAttrWindow.showDialog();
                    }
                    break;
                case R.id.iv_goods_details_home:
                    GoodsDetailsActivity.this.startActivity(new Intent(GoodsDetailsActivity.this,MainTabActivity.class));
                    GoodsDetailsActivity.this.finish();
                    break;
                case R.id.bt_goods_details_buy_now:
                    if(null!=mGoodsInfo){
                        mGoodsAttrWindow.showDialog();
                        /*if(!TextUtils.isEmpty(mGoodsInfo.attribute_type)&&"1".equals(mGoodsInfo.attribute_type)){
                            mGoodsAttrWindow.showDialog();
                        }else{
                            ToastUtils.showToastShort(GoodsDetailsActivity.this,"无商品属性，添加商品到购物车并跳转至购物车页面");
                        }*/
                    }
                    break;
                case R.id.bt_goods_details_add_shopping_cart:
                    if(null!=mGoodsInfo){
                        mGoodsAttrWindow.showDialog();
                        /*if(!TextUtils.isEmpty(mGoodsInfo.attribute_type)&&"1".equals(mGoodsInfo.attribute_type)){
                            mGoodsAttrWindow.showDialog();
                        }else{
                            ToastUtils.showToastShort(GoodsDetailsActivity.this,"无商品属性，添加商品到购物车");
                        }*/
                    }
                    break;
                case R.id.iv_goods_details_card:
                    GoodsDetailsActivity.this.startActivity(new Intent(GoodsDetailsActivity.this,ShoppingCarActivity.class));
                    break;
                default:break;
            }
        }
    };

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        mGoodsId=getIntent().getExtras().getString("goodsId");
    }

    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case CODE_CIRCLE_TIMER:
                int position = mGoodsDetailsBinding.vpNav.getCurrentItem() + 1 ;
                if(position > currentVpSize - 1) {
                    position = 0 ;
                }
                mGoodsDetailsBinding.vpNav.setCurrentItem(position);
                break ;
        }
    }

   private void fillGoodsNavPager() {
       if(null!=mGoodsInfo.goods_picture_list&&mGoodsInfo.goods_picture_list.size()>0){
           List<String> urls = new ArrayList<String>() ;
           goods_picture_list=mGoodsInfo.goods_picture_list;
           currentVpSize=mGoodsInfo.goods_picture_list.size();
           for(int i = 0 ; i < currentVpSize ; i++) {
               urls.add(mGoodsInfo.goods_picture_list.get(i));
           }
           CommonUtils.initViewPage(urls,GoodsDetailsActivity.this,mGoodsDetailsBinding.vpNav,mGoodsDetailsBinding.llNav,null,lastPagePosition);
           mUiHandler.sendEmptyMessageDelayed(CODE_CIRCLE_TIMER, CIRCLE_TIME) ;
       }
    }

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    private void parseGoodsDetails(){
        mGoodsDetailsBinding.tvGoodsDetailsName.setText(!TextUtils.isEmpty(mGoodsInfo.goods_name)?mGoodsInfo.goods_name:"");
        mGoodsDetailsBinding.tvGoodsDetailsPrice.setText(!TextUtils.isEmpty(mGoodsInfo.goods_price)?"￥"+mGoodsInfo.goods_price:"");
        FloatingActionButton mFloatingActionButton= mGoodsDetailsBinding.fabUpSlide;
        mGoodsDetailsBinding.scGoodsDetails.setScrollListener(mFloatingActionButton);
        if(!TextUtils.isEmpty(mGoodsInfo.goods_description)){
            mGoodsDetailsBinding.wbGoodsDetailsDescribe.setVisibility(View.VISIBLE);
            WebSettings webSettings = mGoodsDetailsBinding.wbGoodsDetailsDescribe.getSettings();
            webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);
            //设置WebView属性，能够执行Javascript脚本
            webSettings.setJavaScriptEnabled(true);
            //设置可以访问文件
            webSettings.setAllowFileAccess(true);
            //设置支持缩放
            webSettings.setBuiltInZoomControls(true);
            //不显示缩放按钮
            webSettings.setDisplayZoomControls(false);
            //加载需要显示的网页
            mGoodsDetailsBinding.wbGoodsDetailsDescribe.loadDataWithBaseURL(null,getHtmlData(mGoodsInfo.goods_description),"text/html" ,"UTF-8" ,null);
            //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            mGoodsDetailsBinding.wbGoodsDetailsDescribe.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
        }else{
            mGoodsDetailsBinding.tvGoodsDetailsDescribe.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(mGoodsInfo.stock_switch)&&"1".equals(mGoodsInfo.stock_switch)&&!TextUtils.isEmpty(mGoodsInfo.goods_amount)){
            mGoodsDetailsBinding.tvGoodsDetailsGoodsMount.setVisibility(View.VISIBLE);
            if("0".equals(mGoodsInfo.goods_amount)){
                mGoodsDetailsBinding.llGoodsDetailsAttr.setVisibility(View.GONE);
                mGoodsDetailsBinding.btGoodsDetailsBuyNow.setEnabled(false);
                mGoodsDetailsBinding.btGoodsDetailsAddShoppingCart.setEnabled(false);
            }
            mGoodsDetailsBinding.tvGoodsDetailsGoodsMount.setText("库存："+mGoodsInfo.goods_amount);
        }

        if(!TextUtils.isEmpty(mGoodsDetailsInfo.is_display_amount)&&"1".equals(mGoodsDetailsInfo.is_display_amount)&&!TextUtils.isEmpty(mGoodsInfo.ordered_count)){
            mGoodsDetailsBinding.tvGoodsDetailsOrderedCount.setVisibility(View.VISIBLE);
            mGoodsDetailsBinding.tvGoodsDetailsOrderedCount.setText("销量："+mGoodsInfo.ordered_count+"件");
        }

        if("1".equals(mGoodsInfo.score_switch)){
            mGoodsDetailsBinding.tvGoodsDetailsOrderedScore.setVisibility(View.VISIBLE);
            mGoodsDetailsBinding.tvGoodsDetailsOrderedScore.setText("该商品赠送"+mGoodsInfo.goods_score+"云豆");
        }
        if("1".equals(mGoodsInfo.has_activity)&&mGoodsInfo.activity!=null){
            GoodsActivityInfo mGoodsActivityInfo=mGoodsInfo.activity.get(0);
            if("1".equals(mGoodsActivityInfo.ext_state)){
                mGoodsDetailsBinding.tvGoodsDetailsOrderedOriginalPrice.setVisibility(View.VISIBLE);
                mGoodsDetailsBinding.tvGoodsDetailsOrderedOriginalPrice.getPaint().setAntiAlias(true);
                mGoodsDetailsBinding.tvGoodsDetailsOrderedOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
                mGoodsDetailsBinding.tvGoodsDetailsOrderedOriginalPrice.setText("￥"+mGoodsInfo.original_price);
                mGoodsDetailsBinding.tvGoodsDetailsOrderedDiscountType.setVisibility(View.VISIBLE);
                mGoodsDetailsBinding.tvGoodsDetailsOrderedDiscountType.setText(mGoodsActivityInfo.name);
                mGoodsDetailsBinding.tvGoodsDetailsOrderedDiscountName.setText(mGoodsActivityInfo.type_name);
                if("1".equals(mGoodsActivityInfo.showtime)){
                    mGoodsDetailsBinding.llGoodsDetailsShowtime.setVisibility(View.VISIBLE);
                    CountDownInfo mCountDownInfo= DateUtil.getCountDown(mGoodsActivityInfo.wait_end_time);
                    mGoodsDetailsBinding.llGoodsDetailsOrderedShowtime.setCountDown(mCountDownInfo.day,mCountDownInfo.hour,mCountDownInfo.min,mCountDownInfo.sec);
                    mGoodsDetailsBinding.llGoodsDetailsOrderedShowtime.setOnTimeChangeListener(new CountDownView.OnTimeChangeListener() {
                        @Override
                        public String onSecChange(long sec) {
                            return sec+"";
                        }
                        @Override
                        public String onMinChange(long min) {
                            return null;
                        }
                        @Override
                        public String onHourChange(long hour) {
                            return null;
                        }
                        @Override
                        public String onDayChange(long day) {
                            return null;
                        }
                    });
                    mGoodsDetailsBinding.llGoodsDetailsOrderedShowtime.start();
                }
            }
        }
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            int size = mGoodsDetailsBinding.llNav.getChildCount() ;
            ((ImageView)mGoodsDetailsBinding.llNav.getChildAt(position%size)).setImageResource(R.drawable.checked_usb_open);
            ((ImageView)mGoodsDetailsBinding.llNav.getChildAt(lastPagePosition%size)).setImageResource(R.drawable.checked_usb_close);
            lastPagePosition = position;
            mUiHandler.removeMessages(CODE_CIRCLE_TIMER);
            mUiHandler.sendEmptyMessageDelayed(CODE_CIRCLE_TIMER,CIRCLE_TIME) ;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void getGoodsDetails() {
        MallApplication.getInstance().getModel(AppModel.class).getGoodsDetails(TAG,mGoodsId,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    mGoodsDetailsInfo=GsonUtil.GsonToBean(jsonObj.optString("data"),GoodsDetailsInfo.class);
                    if(null!=mGoodsDetailsInfo){
                        mGoodsInfo=mGoodsDetailsInfo.goods;
                        mGoodsInfo.is_display_amount=mGoodsDetailsInfo.is_display_amount;
                        fillGoodsNavPager();
                        parseGoodsDetails();
                        mGoodsAttrWindow=new GoodsAttrWindow(mGoodsInfo,GoodsDetailsActivity.this, GoodsDetailsActivity.this);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(GoodsDetailsActivity.this,"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(GoodsDetailsActivity.this.getBaseContext(),msg);
            }

            @Override
            public void onAfter(int id) {}
        });
    }

    /**
     * 立即购买window回调
     * @param completeMsg
     */
    @Override
    public void toBuNow(String completeMsg) {
        Log.e(TAG,completeMsg);
        //ToastUtils.showToastShort(GoodsDetailsActivity.this,completeMsg);
    }

    /**
     * 加入购物车window回调
     * @param completeMsg
     */
    @Override
    public void toAddShoppingCart(String completeMsg) {
        Log.e(TAG,completeMsg);
        //ToastUtils.showToastShort(GoodsDetailsActivity.this,completeMsg);
        Log.e(GoodsDetailsActivity.class.getSimpleName(),completeMsg);
    }
}
