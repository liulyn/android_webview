package com.cloud.tao.util;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.GoodsDetailAttrInfo;
import com.cloud.tao.bean.etc.GoodsDetailGoodsAttrInfo;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.bean.etc.ShoppingCarGoods;
import com.cloud.tao.callback.GoodsAttrCallBack;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.callback.OnTagSelectListener;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.ui.activity.OrderPayActivity;
import com.cloud.tao.ui.widget.FlowTagLayout;
import com.cloud.tao.ui.widget.GoodsAttrDialog;
import com.cloud.tao.ui.widget.bean.TagInfo;
import com.cloud.tao.util.adapter.TagAdapter;
import com.cloud.tao.util.glide.GlideUtil;
import com.cloud.tao.util.photoview.GlidePhotoView.PhotoViewHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * sunny created at 2016/10/20
 * dec: 商品属性选择 弹出框统一管理
 */
public class GoodsAttrWindow{

    private Activity mActivity;
    private View contentView;
    private TextView tvPrice, tvGoodsName;
    private EditText etNum;
    private LinearLayout mLlGoodsAttrTags;
    public List<List<GoodsDetailAttrInfo>> mAttrs; //按等级把属性分组
    public List<GoodsDetailGoodsAttrInfo> mGoodsAttrs; //商品属性信息
    public GoodsAttrDialog mBottomSheetDialog; //Dialog弹窗
    private int shopNum=1;
    private String shopPrice="0";
    private ImageButton btnCut, btnAdd;
    private Button btnBuyNow, btnAddShoppingCart;
    GoodsAttrCallBack callBack;
    GoodsInfo mGoodsInfo;
    String mGoodsAttrIdStr;
    String mGoodsAttrNameStr;
    Map mSelectAttrIds = new LinkedHashMap();
    Map mSelectAttrNames = new LinkedHashMap();
    private int mGoods_attr_id;

    public GoodsAttrWindow(GoodsInfo goodsInfo, Activity mActivity, GoodsAttrCallBack callBack) {
        this.mGoodsInfo = goodsInfo;
        mAttrs = mGoodsInfo.attr;
        mGoodsAttrs = mGoodsInfo.goods_attr;
        this.mActivity = mActivity;
        this.callBack = callBack;
    }

    public void showDialog() {
        mBottomSheetDialog = new GoodsAttrDialog(mActivity, R.style.GoodsAttrDialogStyle);
        mBottomSheetDialog.outDuration(100);
        mBottomSheetDialog.inDuration(100);
        int heightPixels = PhotoViewHelper.screenHeight(mActivity);
        mBottomSheetDialog.heightParam((int) (heightPixels * 0.6));
        //解析视图
        contentView = LayoutInflater.from(mActivity).inflate(R.layout.item_goods_attr_layout, null);
        mLlGoodsAttrTags = (LinearLayout) contentView.findViewById(R.id.ll_goods_attr_tags);
        //设置视图
        mBottomSheetDialog.setContentView(contentView);
        ImageView ivClose = (ImageView) contentView.findViewById(R.id.iv_close);
        ImageView ivShoppingPhoto = (ImageView) contentView.findViewById(R.id.iv_shopping_photo);

        tvPrice = (TextView) contentView.findViewById(R.id.tv_attr_goods_price);
        tvGoodsName = (TextView) contentView.findViewById(R.id.tv_attr_goods_name);
        etNum = (EditText) contentView.findViewById(R.id.et_shop_num);
        btnCut = (ImageButton) contentView.findViewById(R.id.btn_shop_cut);
        btnAdd = (ImageButton) contentView.findViewById(R.id.btn_shop_add);
        btnBuyNow = (Button) contentView.findViewById(R.id.btn_buy_now);
        btnAddShoppingCart = (Button) contentView.findViewById(R.id.btn_add_shopping_cart);
        setDefualtShopNum();
        tvPrice.setText("￥" + mGoodsInfo.goods_price);
        shopPrice=mGoodsInfo.goods_price;
        tvGoodsName.setText(mGoodsInfo.goods_name);
        if (null != mGoodsInfo.goods_picture_list && mGoodsInfo.goods_picture_list.size() > 0) {
            GlideUtil.getInstance().loadImage(mActivity, mGoodsInfo.goods_picture_list.get(0), ivShoppingPhoto);
        }
        initGoodsAttr();

        ivClose.setOnClickListener(mOnAttrClickListener);
        btnCut.setOnClickListener(mOnAttrClickListener);
        btnAdd.setOnClickListener(mOnAttrClickListener);
        btnBuyNow.setOnClickListener(mOnAttrClickListener);
        btnAddShoppingCart.setOnClickListener(mOnAttrClickListener);
        etNum.addTextChangedListener(new OnTextWatcherListener());
        mBottomSheetDialog.show();
    }

    private NoDoubleClickListener mOnAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.iv_close:
                    mBottomSheetDialog.dismiss();
                    break;
                case R.id.btn_buy_now:
                    setDefualtShopNum();
                    //AccountInfo.getInstance().saveShoppingCarGoods(Integer.parseInt(mGoodsInfo.goods_id),mGoodsInfo.goods_name,Double.parseDouble(shopPrice),shopNum,mGoods_attr_id,mGoodsAttrIdStr,mGoodsAttrNameStr,mGoodsInfo.goods_picture_list.get(0),mGoodsInfo.goods_description,ShoppingCarGoods.BuyState.BUY_NOW);
                    ShoppingCarGoods goods = new ShoppingCarGoods();
                    goods.goods_id = Integer.parseInt(mGoodsInfo.goods_id);
                    goods.goods_name = mGoodsInfo.goods_name;
                    goods.goods_price = Double.parseDouble(shopPrice);
                    goods.goods_count = shopNum;
                    goods.goods_attr_id = mGoods_attr_id;
                    goods.attr_id_list = mGoodsAttrIdStr;
                    goods.goods_attr_name = mGoodsAttrNameStr;
                    goods.goods_description = mGoodsInfo.goods_description;
                    goods.goods_picture = mGoodsInfo.goods_picture_list.get(0);
                    goods.buy_state = ShoppingCarGoods.BuyState.BUY_NOW;
                    callBack.toBuNow("加入购物车并跳转至购物购物车页面" + ",---->商品对象：<" + mGoodsInfo + ">,商品属性id：" + mGoodsAttrIdStr + ",商品属性名称: " + mGoodsAttrNameStr + "，商品数量：" + shopNum+"，单价："+shopPrice);
                    Intent intent = new Intent(mActivity,OrderPayActivity.class);
                    intent.putExtra(OrderPayActivity.SHOPPING_CARD_STATE_TAG,"buy_now");
                    intent.putExtra(OrderPayActivity.GOODS_NOW_STATE_TAG,goods);
                    mActivity.startActivity(intent);
                    mBottomSheetDialog.dismiss();
                    mActivity.finish();
                    break;
                case R.id.btn_add_shopping_cart:
                    //ToastUtils.showToastShort(mActivity,"mGoodsInfo.goods_id="+mGoodsInfo.goods_id+";mGoods_attr_id"+mGoods_attr_id);
                    AccountInfo.getInstance().saveShoppingCarGoods(Integer.parseInt(mGoodsInfo.goods_id),mGoodsInfo.goods_name,Double.parseDouble(shopPrice),shopNum,mGoods_attr_id,mGoodsAttrIdStr,mGoodsAttrNameStr,mGoodsInfo.goods_picture_list.get(0),mGoodsInfo.goods_description, ShoppingCarGoods.BuyState.ADD_SHOPPING);
                    ToastUtils.showToastShort(mActivity,"已将商品加入购物车");
                    callBack.toAddShoppingCart("加入购物车" + ",---->商品对象：<" + mGoodsInfo + ">,商品属性id：" + mGoodsAttrIdStr + ",商品属性名称: " + mGoodsAttrNameStr + "，商品数量：" + shopNum+"，单价："+shopPrice);
                    //mActivity.finish();
                    mBottomSheetDialog.dismiss();
                    break;
                case R.id.btn_shop_cut://减少
                    if (shopNum > 1) {
                        shopNum--;
                    }
                    etNum.setText(shopNum + "");
                    break;
                case R.id.btn_shop_add://增加
                    if ("1".equals(mGoodsInfo.stock_switch)) {
                        if (shopNum < Integer.valueOf(mGoodsInfo.goods_amount)) {
                            shopNum++;
                        } else {
                            ToastUtils.showToastShort(mActivity, "库存里只有" + mGoodsInfo.goods_amount + "个");
                        }
                    } else {
                        shopNum++;
                    }
                    etNum.setText(shopNum + "");
                    break;
                default:
                    break;
            }
        }
    };

    //文本输入监听
    private class OnTextWatcherListener implements TextWatcher {
        public OnTextWatcherListener() {}
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String inputNumber = etNum.getText().toString();
            if (!TextUtils.isEmpty(inputNumber)) {
                checkIsEmpty(Integer.valueOf(inputNumber));
            }
        }

        private void checkIsEmpty(int inputNumber) {
            if(inputNumber>0){
                if ("1".equals(mGoodsInfo.stock_switch)) {
                    if (inputNumber > Integer.valueOf(mGoodsInfo.goods_amount)) {
                        shopNum = Integer.valueOf(mGoodsInfo.goods_amount);
                        ToastUtils.showToastShort(mActivity, "库存里只有" + mGoodsInfo.goods_amount + "件");
                    }
                }else{
                    shopNum = inputNumber;
                }
            }else{
                etNum.setText("1");
                shopNum=1;
            }

        }
    }

    private void initGoodsAttr() {
        if ("1".equals(mGoodsInfo.attribute_type)) {
            mLlGoodsAttrTags.setVisibility(View.VISIBLE);
            for (int i = 0; i < mAttrs.size(); i++) {
                List<TagInfo> mTagInfoList = new ArrayList<>();
                View tagView = LayoutInflater.from(mActivity).inflate(R.layout.item_goods_attr_tag, null);
                TextView tvTagTitle = (TextView) tagView.findViewById(R.id.ftl_goods_attr_title);
                FlowTagLayout flowTagLayout = (FlowTagLayout) tagView.findViewById(R.id.ftl_goods_attr_title_tab);
                tvTagTitle.setText("属性" + (i + 1) + "：");
                for (int j = 0; j < mAttrs.get(i).size(); j++) {
                    TagInfo tagInfo = new TagInfo(false, mAttrs.get(i).get(j).attr_name);
                    tagInfo.setSelect(false);
                    mTagInfoList.add(tagInfo);
                }
                if (mTagInfoList.size() > 0) {
                    mTagInfoList.get(0).setSelect(true);
                }
                TagAdapter tagAdapter = new TagAdapter(mActivity, mTagInfoList);
                flowTagLayout.setAdapter(tagAdapter);
                tagAdapter.notifyDataSetChanged();
                flowTagLayout.setTagCheckedMode(FlowTagLayout.FLOW_TAG_CHECKED_SINGLE);
                final int finalI = i;
                flowTagLayout.setOnTagSelectListener(new OnTagSelectListener() {
                    @Override
                    public void onItemSelect(FlowTagLayout parent, List<Integer> selectedList) {
                        int mPosition = selectedList.get(0);
                        GoodsDetailAttrInfo GoodsDetailAttrInfo = mAttrs.get(finalI).get(mPosition);
                        addSelectGoodsAttr((String.valueOf(finalI)), GoodsDetailAttrInfo);
                    }
                });
                mLlGoodsAttrTags.addView(tagView);
            }
        }
    }

    //逻辑有点乱 后面再优化，不影响计算选择的属性和价格
    private void addSelectGoodsAttr(String key, GoodsDetailAttrInfo GoodsDetailAttrInfo) {
        mSelectAttrIds.put(key, GoodsDetailAttrInfo.attr_id);
        mSelectAttrNames.put(key, GoodsDetailAttrInfo.attr_name);

        Iterator iteratorId = mSelectAttrIds.entrySet().iterator();
        StringBuffer sbId = new StringBuffer();
        while (iteratorId.hasNext()) {
            Map.Entry e = (Map.Entry) iteratorId.next();
            sbId.append(e.getValue());
            sbId.append(",");
        }
        String appendAttrIdStr = sbId.toString();
        mGoodsAttrIdStr = appendAttrIdStr.substring(0, appendAttrIdStr.length() - 1);

        Iterator iteratorName = mSelectAttrNames.entrySet().iterator();
        StringBuffer sbName = new StringBuffer();
        while (iteratorName.hasNext()) {
            Map.Entry e = (Map.Entry) iteratorName.next();
            sbName.append(e.getValue());
            sbName.append(",");
        }
        String appendAttrNameStr = sbName.toString();
        mGoodsAttrNameStr = appendAttrNameStr.substring(0, appendAttrNameStr.length() - 1);
        List<GoodsDetailGoodsAttrInfo> goodsAttrs = mGoodsInfo.goods_attr;
        for (int k = 0; k < goodsAttrs.size(); k++) {
            StringBuffer sbAttrsList = new StringBuffer();
            for (int j = 0; j < goodsAttrs.get(k).attr_id_list.size(); j++) {
                if (j > 0) {
                    sbAttrsList.append(",");
                }
                sbAttrsList.append(goodsAttrs.get(k).attr_id_list.get(j));
            }
            if (mGoodsAttrIdStr.equals(sbAttrsList.toString())) {
                tvPrice.setText("￥" + goodsAttrs.get(k).goods_price);
                shopPrice=goodsAttrs.get(k).goods_price;
                mGoods_attr_id = Integer.parseInt(goodsAttrs.get(k).goods_attr_id);
            }
        }
    }

    private void setDefualtShopNum() {
        if (TextUtils.isEmpty(etNum.getText().toString())) {
            etNum.setText("1");
            etNum.setSelection(1);
            shopNum = 1;
        }
    }
}
