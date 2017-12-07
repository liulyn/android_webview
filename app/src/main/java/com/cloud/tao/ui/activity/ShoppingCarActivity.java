package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.ShoppingCar;
import com.cloud.tao.bean.etc.ShoppingCarGoods;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.databinding.ActivityShppingCarBinding;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.cloud.tao.util.glide.GlideUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gezi-pc on 2016/10/23.
 */

public class ShoppingCarActivity  extends BaseNavBackActivity {

    private static final String TAG = ShoppingCarActivity.class.getSimpleName();
    ActivityShppingCarBinding mActivityShppingCarBinding;
    private static ShoppingCar shoppingCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityShppingCarBinding =  DataBindingUtil.setContentView(this, R.layout.activity_shpping_car);
        setNavDefaultBack(mActivityShppingCarBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityShppingCarBinding.loadding.showLoadSuccess();
        initListener();
        initDatas(savedInstanceState);
    }

    private boolean allCheck  = false;

    private void initListener() {
        mActivityShppingCarBinding.btGoPay.setOnClickListener(new NoDoubleClickListener(){
            @Override
            public void noDoubleClick(View v) {
                submitOrder();
            }
        });
        mActivityShppingCarBinding.rbTopGoodsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check =!allCheck;
                if(shoppingCar!=null){
                    for(ShoppingCarGoods goods:shoppingCar.goods){
                        goods.buy_state = check==true? ShoppingCarGoods.BuyState.SHOPPING_SELECTED: ShoppingCarGoods.BuyState.ADD_SHOPPING;
                    }
                    adapter.notifyDataSetChanged();
                }
                mActivityShppingCarBinding.rbTopGoodsAll.setChecked(check);
                mActivityShppingCarBinding.rbGoodsAll.setChecked(check);
                allCheck = !allCheck;
                countTotal();
            }
        });
        mActivityShppingCarBinding.rbGoodsAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check =!allCheck;
                if(shoppingCar!=null){
                    for(ShoppingCarGoods goods:shoppingCar.goods){
                        goods.buy_state = check==true? ShoppingCarGoods.BuyState.SHOPPING_SELECTED: ShoppingCarGoods.BuyState.ADD_SHOPPING;
                    }
                    adapter.notifyDataSetChanged();
                }
                mActivityShppingCarBinding.rbTopGoodsAll.setChecked(check);
                mActivityShppingCarBinding.rbGoodsAll.setChecked(check);
                allCheck = !allCheck;
                countTotal();
            }
        });
        mActivityShppingCarBinding.tvDelGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shoppingCar!=null){
                    List<ShoppingCarGoods> tempGoodss = new ArrayList<ShoppingCarGoods>();
                    tempGoodss.addAll(shoppingCar.goods);
                    for(ShoppingCarGoods goods:tempGoodss){
                        if(goods.buy_state== ShoppingCarGoods.BuyState.SHOPPING_SELECTED){
                            AccountInfo.getInstance().removeGoodsByGoodsId(goods.goods_id);
                            shoppingCar.goods.remove(goods);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    countTotal();
                }
            }
        });

    }

    /**
     * 去提交订单
     */
    private void submitOrder() {
        Intent intent = new Intent(getBaseContext(),OrderPayActivity.class);
        intent.putExtra(OrderPayActivity.SHOPPING_CARD_STATE_TAG,"shopping_car");
        ShoppingCarActivity.this.startActivity(intent);
        AccountInfo.getInstance().updateShoppingCar(shoppingCar);
        this.finish();

    }

    private CommonAdapter adapter;



    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        shoppingCar = AccountInfo.getInstance().getShoppingCar();
        if(shoppingCar==null){
            countTotal();
            return;
        }
        ArrayList<ShoppingCarGoods> tempGoods = new ArrayList<>();
        tempGoods.addAll(shoppingCar.goods);
        for(ShoppingCarGoods goods:tempGoods){
            if(goods.buy_state== ShoppingCarGoods.BuyState.BUY_NOW) {
                shoppingCar.goods.remove(goods);
            }
        }
        if(shoppingCar!=null) {
            adapter = new CommonAdapter<ShoppingCarGoods>(this, shoppingCar.goods, R.layout.shopping_car_item) {
                @Override
                protected void convert(ViewHolder vh,final ShoppingCarGoods item) {
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_title)).setText(item.goods_name);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_desc)).setText(item.goods_attr_name);//.setText(item.goods_description);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_price)).setText("￥"+item.goods_price.toString());
//                    ((TextView) vh.getConvertView().findViewById(R.id.tv_count)).setText("x"+item.goods_count);
                    final TextView et_shop_num = (TextView)vh.getConvertView().findViewById(R.id.et_shop_num);
                    et_shop_num.setText(""+item.goods_count);

                    Button btn_shop_cut =(Button)vh.getConvertView().findViewById(R.id.btn_shop_cut);
                    Button btn_shop_add =(Button)vh.getConvertView().findViewById(R.id.btn_shop_add);
                    btn_shop_cut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(item.goods_count>1) {
                                item.goods_count--;
                                et_shop_num.setText(""+item.goods_count);
                                for(ShoppingCarGoods citem : shoppingCar.goods){
                                    if(citem==item){
                                        citem.goods_count=item.goods_count;
                                    }
                                }
                                countTotal();
                            }
                        }
                    });
                    btn_shop_add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            item.goods_count++;
                            et_shop_num.setText(""+item.goods_count);
                            for(ShoppingCarGoods citem : shoppingCar.goods){
                                if(citem==item){
                                    citem.goods_count=item.goods_count;
                                }
                            }
                            countTotal();
                        }
                    });

                    ImageView iv_ico = (ImageView) vh.getConvertView().findViewById(R.id.iv_goods);
                    GlideUtil.getInstance().loadImage(ShoppingCarActivity.this,item.goods_picture,iv_ico);
                    RadioButton rb = (RadioButton)vh.getConvertView().findViewById(R.id.rb_goods_sel);
                    boolean ischeck  = item.buy_state== ShoppingCarGoods.BuyState.SHOPPING_SELECTED?true:false;
                    rb.setChecked(ischeck);
                    rb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean check = item.buy_state== ShoppingCarGoods.BuyState.SHOPPING_SELECTED?true:false;
                            if(!check) {
                                item.buy_state = ShoppingCarGoods.BuyState.SHOPPING_SELECTED;
                            }else{
                                item.buy_state = ShoppingCarGoods.BuyState.ADD_SHOPPING;
                            }
                            checkAllIsCheck(!check);
                            notifyDataSetChanged();
                            countTotal();
                        }
                    });
                }
            };
            mActivityShppingCarBinding.lvGoods.setAdapter(adapter);
        }
        countTotal();
    }

    private void checkAllIsCheck(boolean isCheck){
        boolean check = true;
        for(ShoppingCarGoods goods:shoppingCar.goods){
            boolean tempCheck = goods.buy_state== ShoppingCarGoods.BuyState.SHOPPING_SELECTED?true:false;
            if(isCheck!=tempCheck){
                check = false;
                break;
            }
        }
        if(check){
            mActivityShppingCarBinding.rbTopGoodsAll.setChecked(isCheck);
            mActivityShppingCarBinding.rbGoodsAll.setChecked(isCheck);
            allCheck = isCheck;
        }
    }

    /**
     * 统计总价
     */
    private void countTotal(){
        int count = 0;
        Double totalPrice = 0.00;

        if(shoppingCar!=null) {
            for (ShoppingCarGoods item : shoppingCar.goods) {
                if (item.buy_state== ShoppingCarGoods.BuyState.SHOPPING_SELECTED) {
                    count++;
                    totalPrice += item.goods_count * item.goods_price;
                }
            }
        }

        if(count==0) {
            mActivityShppingCarBinding.btGoPay.setText("去结算("+count+")");
            mActivityShppingCarBinding.btGoPay.setBackgroundResource(R.color.gray_text);
            mActivityShppingCarBinding.btGoPay.setEnabled(false);
            mActivityShppingCarBinding.btGoPay.setClickable(false);
            mActivityShppingCarBinding.tvTotalPrice.setText("￥"+totalPrice);
        }else{
            mActivityShppingCarBinding.btGoPay.setText("去结算("+count+")");
            mActivityShppingCarBinding.btGoPay.setBackgroundResource(R.drawable.my_order_pay_selector);
            mActivityShppingCarBinding.btGoPay.setEnabled(true);
            mActivityShppingCarBinding.btGoPay.setClickable(true);
            mActivityShppingCarBinding.tvTotalPrice.setText("￥"+totalPrice);
        }
    }
}
