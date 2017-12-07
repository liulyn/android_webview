package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.AddressList;
import com.cloud.tao.bean.etc.CloudPayWay;
import com.cloud.tao.bean.etc.OrderGoods;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.bean.etc.RechargeCardOrder;
import com.cloud.tao.bean.etc.ShoppingCar;
import com.cloud.tao.bean.etc.ShoppingCarGoods;
import com.cloud.tao.bean.etc.SoSetAccount;
import com.cloud.tao.bean.etc.SubmitOrder;
import com.cloud.tao.bean.etc.TransportWay;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.databinding.ActivityOrderPayBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.cloud.tao.net.model.ordermodel.req.OrderRechargeCard;
import com.cloud.tao.net.model.ordermodel.req.ParentOrder;
import com.cloud.tao.net.model.ordermodel.req.SubOrder;
import com.cloud.tao.ui.widget.SelectRechargeWidget;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/10/24.
 */

public class OrderPayActivity extends BaseNavBackActivity {

    private static final String TAG = OrderPayActivity.class.getSimpleName();
    public static final int SELECT_ADDRESS_REQUEST_CODE = 0;
    ActivityOrderPayBinding mActivityOrderPayBinding;
    private DecimalFormat df = new DecimalFormat("#.##");
    private SubmitOrder submitOrder;
    private SelectSingleWidget selectPayWayWidget;
    private CloudPayWay selectPayWay;
    private SelectSingleWidget selectTransportWayWidget;
    private TransportWay selectTransportWay;
    private AddressList.Address defaultAddress;
    private ProgressDialog progressDialog;
    private double distribution;
    //private double scoreMoney;
    private boolean checkedIntegral = true;
    private SelectRechargeWidget selectRechargeWidget;
    private RechargeCardOrder selectRechargeCardOrder;
    private Double totalPrice;
    private ArrayList<Integer> delGoodsId;
    public static final String SHOPPING_CARD_STATE_TAG = "shopping_card_state_tag";
    public static final String GOODS_NOW_STATE_TAG = "goods_now_state_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityOrderPayBinding = DataBindingUtil.setContentView(this, R.layout.activity_order_pay);
        setNavDefaultBack(mActivityOrderPayBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityOrderPayBinding.loadding.showLoadSuccess();
        initListener();
    }

    private void initListener() {

        //选择收货人
        mActivityOrderPayBinding.rlAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(getBaseContext(), "选择收货人");
                Intent intent = new Intent(OrderPayActivity.this, ManagerAddressActivity.class);
                ManagerAddressActivity.SELECT_ADDRESS_REQUEST_CODE = 1;
                OrderPayActivity.this.startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE);
                checkSubmit();
            }
        });

        //更改收货人
        mActivityOrderPayBinding.rlAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToastShort(getBaseContext(), "更改收货人");
                Intent intent = new Intent(OrderPayActivity.this, ManagerAddressActivity.class);
                ManagerAddressActivity.SELECT_ADDRESS_REQUEST_CODE = 1;
                OrderPayActivity.this.startActivityForResult(intent, SELECT_ADDRESS_REQUEST_CODE);
            }
        });

        //提交订单
        mActivityOrderPayBinding.btSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submitOrder != null) {
                    ToastUtils.showToastShort(getBaseContext(), "提交订单");
                    submitOrder();
                }
            }
        });

    }

    /**
     * 提交订单
     */
    private void submitOrder() {
        try {
            String parentOrderJson = null;
            String subOrderJson = null;
            ParentOrder parent = new ParentOrder();

            parent.discount_type = submitOrder.fenxiao_discount.fenxiao_discount_switch == 0 ? 0 : 4;
            parent.discount_money = submitOrder.fenxiao_discount.fenxiao_discount_switch == 0 ? 0 : submitOrder.discount_money;

            parent.score_buy_switch = checkedIntegral == true ? 1 : 0;
            //ToastUtils.showToastShort(this,checkedIntegral+";score_buy_switch="+parent.score_buy_switch);
            if (submitOrder.client_score != null && submitOrder.client_score.score_to_money != null) {
                parent.score_buy_money = checkedIntegral == true ? submitOrder.client_score.score_to_money : 0.0;
            } else {
                parent.score_buy_money = 0.0;
            }
   /* if (submitOrder.client_score != null && submitOrder.client_score.score_to_exchange != null) {
        parent.score_buy_score = checkedIntegral == true ? submitOrder.client_score.score_to_exchange : 0;
    } else {
        parent.score_buy_score = 0;
    }*/
            if (submitOrder.postage == null) {
                parent.post = 0.0;
                parent.freight_type = 0;
            } else {
                if (selectTransportWay != null) {
                    parent.post = selectTransportWay.price;
                    if (selectTransportWay.type.equals("EXPRESS")) {
                        parent.freight_type = 1;
                    } else if (selectTransportWay.type.equals("EMS")) {
                        parent.freight_type = 2;
                    } else if (selectTransportWay.type.equals("POST")) {
                        parent.freight_type = 4;
                    }
                } else {
                    parent.post = 0.0;
                    parent.freight_type = 0;
                }
            }
            parent.actual_total_price = totalPrice;
            parent.pay_way = selectPayWay.pay_way_value;
            parent.receiver_name = defaultAddress.consignee;
            parent.receiver_mobilephone = defaultAddress.mobilephone;
            parent.receiver_province = defaultAddress.province;
            parent.receiver_city = defaultAddress.city;
            parent.receiver_district = defaultAddress.area;
            parent.receiver_address = defaultAddress.street;
            parent.buyer_leave_message = rmark == null ? null : rmark;
            if (selectRechargeCardOrder != null) {
                String name = selectRechargeCardOrder.label_name;
                String num = selectRechargeCardOrder.data.get(0).label_num;
                OrderRechargeCard orc = new OrderRechargeCard(name, num);
                parent.label_beizhu.add(orc);
            }

            ArrayList<SubOrder> subs = new ArrayList<SubOrder>();

            for (SoSetAccount so : submitOrder.setAccount) {
                SubOrder sub = new SubOrder();
                sub.goods_id = so.goods_id;
                sub.goods_count = so.goods_count;
                sub.goods_price = so.goods_price;
                sub.actual_total_price = so.actual_total_price;
                sub.goods_attr_id = so.goods_attr_id;
                sub.goods_attr_str = so.goods_attr_str;
                subs.add(sub);
            }

            Gson gson = new Gson();
            parentOrderJson = gson.toJson(parent);
            Log.e(TAG, parentOrderJson);
            subOrderJson = gson.toJson(subs);
            progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
            MallApplication.getInstance().getModel(OrderModel.class).addOrder(TAG, submitOrder.order_token, submitOrder.store_id, submitOrder.u_client_id, parentOrderJson, subOrderJson, new EntityCallBack<String>(new TypeToken<String>() {
            }) {
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    Log.e(TAG, "code=" + code + ";msg=" + msg + ";resp=" + resp);
                    ViewUtils.dismissDialog(OrderPayActivity.this, progressDialog);
                    for (int goodsId : delGoodsId) {
                        AccountInfo.getInstance().removeGoodsByGoodsId(goodsId);
                    }

                    Log.e(TAG, resp);
                    if (resp == null) return;
                    try {
                        JSONObject jsonObj = new JSONObject(resp);
                        PayH5 payH5 = GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                        if (null != payH5) {
                            readyToPay(payH5);
                        }
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(getBaseContext(), "获取数据异常");
                    }

                    //OrderPayActivity.this.startActivity(new Intent(getBaseContext(),MyOrderListActivity.class));
                    OrderPayActivity.this.finish();
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(OrderPayActivity.this, progressDialog);
                    Log.e(TAG, "code=" + code + ";msg=" + msg + ";e=" + e);
                    ToastUtils.showToastShort(getBaseContext(), msg);

                }
            });
        } catch (Exception e) {
            ToastUtils.showToastShort(getBaseContext(), "获取数据,解析异常");
        }

    }

    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(this, PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM, payH5.url);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        getSubmitOrder();
        initSelectPayWay();
    }


    private void getSubmitOrder() {

        try {
            String buy_state = getIntent().getStringExtra(SHOPPING_CARD_STATE_TAG);
            ShoppingCar shoppingCar = AccountInfo.getInstance().getShoppingCar();

            ArrayList<OrderGoods> goodss = new ArrayList<OrderGoods>();
            delGoodsId = new ArrayList<Integer>();

            if ("buy_now".equals(buy_state)) {

                ShoppingCarGoods item = (ShoppingCarGoods) getIntent().getSerializableExtra(GOODS_NOW_STATE_TAG);
                OrderGoods og = new OrderGoods();
                og.goods_id = item.goods_id;
                og.goods_name = item.goods_name;
                og.goods_price = item.goods_price;
                og.goods_count = item.goods_count;
                og.goods_attr_id = item.goods_attr_id;
                og.attr_id_list = item.attr_id_list;
                og.goods_attr_name = item.goods_attr_name;
                goodss.add(og);
            } else {

                for (ShoppingCarGoods item : shoppingCar.goods) {
                    //if(item.buy_state == ShoppingCarGoods.BuyState.SHOPPING_SELECTED&&"shopping_car".equals(buy_state)){
                    if (item.buy_state == ShoppingCarGoods.BuyState.SHOPPING_SELECTED) {
                        OrderGoods og = new OrderGoods();
                        og.goods_id = item.goods_id;
                        og.goods_name = item.goods_name;
                        og.goods_price = item.goods_price;
                        og.goods_count = item.goods_count;
                        og.goods_attr_id = item.goods_attr_id;
                        og.attr_id_list = item.attr_id_list;
                        og.goods_attr_name = item.goods_attr_name;
                        goodss.add(og);
                        delGoodsId.add(item.goods_id);
                    }
//            }else if(item.buy_state == ShoppingCarGoods.BuyState.BUY_NOW&&"buy_now".equals(buy_state)){
//                OrderGoods og = new OrderGoods();
//                og.goods_id = item.goods_id;
//                og.goods_name = item.goods_name;
//                og.goods_price = item.goods_price;
//                og.goods_count = item.goods_count;
//                og.goods_attr_id = item.goods_attr_id;
//                og.attr_id_list = item.attr_id_list;
//                og.goods_attr_name = item.goods_attr_name;
//                goodss.add(og);
//                delGoodsId.add(item.goods_id);
//            }
                }
            }

            Gson gson = new Gson();
            String orderJson = gson.toJson(goodss);
            Log.e(TAG, orderJson);
           progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

            MallApplication.getInstance().getModel(OrderModel.class).toSubmitOrder(TAG, orderJson, new EntityCallBack<String>(new TypeToken<String>() {
            }) {
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(OrderPayActivity.this, progressDialog);
                    Log.e(TAG, msg);
                    Log.e(TAG, resp);
                    try {
                        JSONObject jsonObj = new JSONObject(resp);
                        submitOrder = GsonUtil.GsonToBean(jsonObj.optString("data"), SubmitOrder.class);
                        fillData();
                    } catch (JSONException e) {
                        ToastUtils.showToastShort(getBaseContext(), "获取数据异常");
                    }
                    Log.e(TAG, msg);

                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(OrderPayActivity.this, progressDialog);
                    ToastUtils.showToastShort(getBaseContext(), msg);
                    Log.e(TAG, msg);
                    if (code == 3 || code == 4) {
                        OrderPayActivity.this.finish();
                    }
                }
            });
        } catch (Exception e) {
            ToastUtils.showToastShort(getBaseContext(), "获取数据,解析异常");
        }
    }

    private void fillData() {
        if (submitOrder == null) {
            return;
        }
        if (submitOrder.address != null) {
            defaultAddress = submitOrder.address;
            mActivityOrderPayBinding.rlAlreadyAccount.setVisibility(View.VISIBLE);
            mActivityOrderPayBinding.rlAddAddress.setVisibility(View.GONE);
            bindAddress();
        } else {
            mActivityOrderPayBinding.rlAddAddress.setVisibility(View.VISIBLE);
            mActivityOrderPayBinding.rlAlreadyAccount.setVisibility(View.GONE);

        }

        mActivityOrderPayBinding.tvScoreConsume.setText("待返云豆"+submitOrder.actual_total_score+"分");

//        if (submitOrder.client_score == null || submitOrder.client_score.set == 3 || submitOrder.client_score.score_to_exchange == null || submitOrder.client_score.score_to_exchange.intValue() == 0) {
//            mActivityOrderPayBinding.rlIntegral.setVisibility(View.GONE);
//            checkedIntegral = false;
//        } else {
//            checkedIntegral = true;
//            mActivityOrderPayBinding.rlIntegral.setVisibility(View.VISIBLE);
//            String integral = "可用" + submitOrder.client_score.score_to_exchange + "云豆抵￥" + df.format(submitOrder.client_score.score_to_money);
//            scoreMoney = Double.parseDouble(df.format(submitOrder.client_score.score_to_money));
//            mActivityOrderPayBinding.tvIntegral.setText(integral);
//            mActivityOrderPayBinding.rbIntegral.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    checkedIntegral = !checkedIntegral;
//                    mActivityOrderPayBinding.rbIntegral.setChecked(checkedIntegral);
//                    countPriceTotal();
//                }
//            });
//        }

        if (submitOrder.fenxiao_discount.fenxiao_discount_switch == 1) {
            mActivityOrderPayBinding.rlDistribution.setVisibility(View.VISIBLE);
            distribution = Double.parseDouble(df.format(submitOrder.actual_total_price * ((100 - submitOrder.fenxiao_discount.level_discount) / 100)));
            mActivityOrderPayBinding.tvDistribution.setText("优惠-" + distribution);
        } else {
            mActivityOrderPayBinding.rlDistribution.setVisibility(View.GONE);
        }


        //添加充值卡
        if (submitOrder.is_label == 1) {
            mActivityOrderPayBinding.rlRechargeCard.setVisibility(View.VISIBLE);

        } else {
            mActivityOrderPayBinding.rlRechargeCard.setVisibility(View.GONE);
        }

        initSelectTransportWay();


        //添加备注
        mActivityOrderPayBinding.rlAddComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        mActivityOrderPayBinding.lvGoodsList.setAdapter(new CommonAdapter<SoSetAccount>(this, submitOrder.setAccount, R.layout.my_order_list_item_item) {

            @Override
            protected void convert(ViewHolder vh, SoSetAccount item) {
                ((TextView) vh.getConvertView().findViewById(R.id.tv_title)).setText(item.goods_name);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_desc)).setText(item.goods_attr_str);
                TextView count = (TextView) vh.getConvertView().findViewById(R.id.tv_number);
                count.setTextColor(getResources().getColor(R.color.gray_text));
                count.setText("x" + item.goods_count);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_price)).setText("￥" + item.actual_total_price);
                ImageView iv_ico = (ImageView) vh.getConvertView().findViewById(R.id.iv_goods);
                GlideUtil.getInstance().loadImage(OrderPayActivity.this, item.goods_picture_list.get(0), iv_ico);
            }
        });

        initSelectRechargeCard();
        countPriceTotal();
        checkSubmit();
    }

    private void bindAddress() {
        mActivityOrderPayBinding.rlAddAddress.setVisibility(View.GONE);
        mActivityOrderPayBinding.rlAlreadyAccount.setVisibility(View.VISIBLE);
        mActivityOrderPayBinding.tvUsername.setText(defaultAddress.consignee);
        mActivityOrderPayBinding.tvPhoneNumber.setText(defaultAddress.mobilephone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        mActivityOrderPayBinding.tvAddress.setText(defaultAddress.province + " " + defaultAddress.city + " " + (defaultAddress.area == null ? "" : defaultAddress.area));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ManagerAddressActivity.SELECT_ADDRESS_REQUEST_CODE = 0;

        if (resultCode == OrderPayActivity.SELECT_ADDRESS_REQUEST_CODE && data != null) {
            defaultAddress = (AddressList.Address) data.getSerializableExtra(ManagerAddressActivity.SELECT_ADDRESS_RESULT);
            if (defaultAddress != null) {
                bindAddress();
            }
        }
    }


    /**
     * 初始化支付方式
     */
    private void initSelectPayWay() {
        MallApplication.getInstance().getModel(MemberModel.class).getPayTypeList(TAG, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    ArrayList<CloudPayWay> mCloudPayWays=GsonUtil.fromJsonList(jsonObj.optJSONObject("data").optString("pay_way_list"),CloudPayWay.class);
                    ArrayList<String> datas = new ArrayList<>();
                    for(CloudPayWay pay: mCloudPayWays){
                        datas.add(pay.pay_way_name);
                    }
                    selectPayWayWidget = new SelectSingleWidget(OrderPayActivity.this, mCloudPayWays, datas, "选择支付方式", mActivityOrderPayBinding.vMasker);
                    selectPayWayWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CloudPayWay>() {
                        @Override
                        public void OnSelectSingleCallBack(CloudPayWay result) {
                            mActivityOrderPayBinding.tvPayWay.setText(result.pay_way_name);
                            selectPayWay = result;
                            checkSubmit();
                        }
                    });

                    //支付方式
                    mActivityOrderPayBinding.rlPayWay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectPayWayWidget.showWidget();
                        }
                    });
                } catch (JSONException e) {
                    ToastUtils.showToastShort(OrderPayActivity.this,"获取数据异常");
                }
            }
            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(OrderPayActivity.this, msg);
            }
        });

        /*ArrayList<String> datas = new ArrayList<>();
        for (PayWay pay : PayWay.payways) {
            datas.add(pay.name);
        }*/
    }

    /**
     * 初始化充值卡
     */
    private void initSelectRechargeCard() {

        if (submitOrder.label == null || submitOrder.label.size() == 0) {
            mActivityOrderPayBinding.rlRechargeCard.setVisibility(View.GONE);
            return;
        }

        selectRechargeWidget = new SelectRechargeWidget(this, "选择充值卡", submitOrder.label, mActivityOrderPayBinding.vMasker);
        selectRechargeWidget.SetOnSelectOptionSingleCallBack(new SelectRechargeWidget.OnSelectOptionSingleCallBack() {
            @Override
            public void OnSelectSingleCallBack(RechargeCardOrder result) {
                mActivityOrderPayBinding.tvRechargeCard.setText(result.data.get(0).label_num.toString());
                selectRechargeCardOrder = result;
            }
        });

        //充值卡
        mActivityOrderPayBinding.rlRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRechargeWidget.showWidget();
            }
        });
    }


    /**
     * 初始化运费
     */
    private void initSelectTransportWay() {


        if (submitOrder.postage == null) {
            return;
        }
        ArrayList<String> datas = new ArrayList<>();
        datas.add("快递   (" + submitOrder.postage.express + "元)");
        datas.add("EMS   (" + submitOrder.postage.ems + "元)");
        datas.add("平邮   (" + submitOrder.postage.post + "元)");
        ArrayList<TransportWay> ways = new ArrayList<>();
        ways.add(new TransportWay("快递", Double.parseDouble(submitOrder.postage.express), "EXPRESS"));
        ways.add(new TransportWay("EMS", Double.parseDouble(submitOrder.postage.ems), "EMS"));
        ways.add(new TransportWay("平邮", Double.parseDouble(submitOrder.postage.post), "POST"));


        selectTransportWayWidget = new SelectSingleWidget(this, ways, datas, "选择配送方式", mActivityOrderPayBinding.vMasker);
        selectTransportWayWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<TransportWay>() {
            @Override
            public void OnSelectSingleCallBack(TransportWay result) {
                mActivityOrderPayBinding.tvFreight.setText(result.name + "(" + result.price + "元)");
                selectTransportWay = result;
                countPriceTotal();
            }
        });

        //运送方式
        mActivityOrderPayBinding.rlTransportWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTransportWayWidget.showWidget();
            }
        });
    }


    /**
     * 计算总价
     */
    private void countPriceTotal() {

        totalPrice = submitOrder.actual_total_price;
//        if (submitOrder.client_score.set != 3 && submitOrder.client_score.score_to_exchange != null && submitOrder.client_score.score_to_exchange.intValue() != 0 && checkedIntegral == true) {
//            totalPrice = totalPrice - scoreMoney;
//        }
        if (selectTransportWay != null) {
            totalPrice = totalPrice + selectTransportWay.price;
        }
        //创业优惠
        //totalPrice = totalPrice - distribution;
        totalPrice = totalPrice < 0.0 ? 0.0 : Double.parseDouble(df.format(totalPrice));
        mActivityOrderPayBinding.tvTotalPrice.setText("￥" + totalPrice.toString());
    }

    /**
     * 验证是否可以提交
     */
    private void checkSubmit() {
        if (defaultAddress != null && selectPayWay != null) {
            mActivityOrderPayBinding.btSubmitOrder.setBackgroundResource(R.drawable.my_order_pay_selector);
            mActivityOrderPayBinding.btSubmitOrder.setEnabled(true);
            mActivityOrderPayBinding.btSubmitOrder.setClickable(true);
        } else {
            mActivityOrderPayBinding.btSubmitOrder.setBackgroundResource(R.color.gray_text);
            mActivityOrderPayBinding.btSubmitOrder.setEnabled(false);
            mActivityOrderPayBinding.btSubmitOrder.setClickable(false);
        }
    }

    private String rmark;

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        final EditText etRmark = new EditText(this);
        etRmark.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        etRmark.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        etRmark.setTextAppearance(this, R.style.personInfoEdit);
        etRmark.setHint("想对商家说点什么~~~");
        if (rmark != null && !"".equals(rmark)) {
            etRmark.setText(rmark);
        }
        etRmark.setLines(3);
        builder.setTitle("订单备注");
        builder.setView(etRmark);
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rmark = etRmark.getText().toString();
                mActivityOrderPayBinding.tvToStore.setText(rmark);

            }
        });
        builder.show();

    }

    @Override
    protected void onDestroy() {
        ViewUtils.dismissDialog(OrderPayActivity.this, progressDialog);
        super.onDestroy();
    }

}
