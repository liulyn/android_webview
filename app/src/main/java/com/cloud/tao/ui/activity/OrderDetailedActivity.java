package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.GoodsDetail;
import com.cloud.tao.bean.etc.OrderDetail;
import com.cloud.tao.bean.etc.OrderDetailObj;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.bean.etc.TimeShow;
import com.cloud.tao.databinding.ActivityOrderDetailedBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.cloud.tao.util.DateUtil;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;


/**
 * Created by gezi-pc on 2016/10/20.
 */

public class OrderDetailedActivity  extends BaseNavBackActivity {

    public static final String ORDER_DETAILED_ID  = "order_detailed_id";
    private static final String TAG = OrderDetailedActivity.class.getSimpleName();
    ActivityOrderDetailedBinding mActivityOrderDetailedBinding;
    private OrderDetailObj orderDetailObj;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityOrderDetailedBinding =  DataBindingUtil.setContentView(this, R.layout.activity_order_detailed);
        setNavDefaultBack(mActivityOrderDetailedBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityOrderDetailedBinding.loadding.showLoadSuccess();
        initListener();
    }

    /**
     * 测试订单留言代码
     */
    private void initListener() {
        mActivityOrderDetailedBinding.btOrderComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),OrderRefundDetailActivity.class);
                intent.putExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_PARENT_ORDER_ID,orderDetailObj.sub_order[0].list.get(0).parent_order_id);
                intent.putExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_SUB_ORDER_ID,orderDetailObj.sub_order[0].list.get(0).sub_order_id);
                OrderDetailedActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    protected void initData() {
        String orderId  = getIntent().getStringExtra(ORDER_DETAILED_ID);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(OrderModel.class).getOrderDetail(TAG, orderId, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                try {
                    Log.e(TAG,resp);

                    JSONObject jsonObj=new JSONObject(resp);
                    orderDetailObj = GsonUtil.GsonToBean(jsonObj.optString("data"),OrderDetailObj.class);

                    fillData();
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }
                Log.e(TAG,resp);
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getBaseContext(),msg);
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
            }
        });

    }

    private void fillData() {
        final OrderDetail orderDetail = orderDetailObj.parent_order;
        mActivityOrderDetailedBinding.tvNumber.setText("订单号："+orderDetail.parent_order_id);
        mActivityOrderDetailedBinding.tvState.setText(orderDetail.state_name);
        mActivityOrderDetailedBinding.tvReceiver.setText(orderDetail.receiver_name);
        mActivityOrderDetailedBinding.tvReceiverPhone.setText(orderDetail.receiver_mobilephone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        mActivityOrderDetailedBinding.tvAddress.setText(orderDetail.receiver_province+orderDetail.receiver_city+orderDetail.receiver_district+orderDetail.receiver_address);
        mActivityOrderDetailedBinding.tvPayWay.setText(orderDetail.pay_way_name);
        mActivityOrderDetailedBinding.tvTotal.setText("￥"+orderDetail.actual_total_price);
        mActivityOrderDetailedBinding.tvFreight.setText("￥"+orderDetail.freight_money);

        if(orderDetailObj.time_show!=null&&orderDetailObj.time_show.length>0) {
            if (orderDetailObj.time_show.length >= 1) {
                TimeShow timeShow = orderDetailObj.time_show[0];
                mActivityOrderDetailedBinding.tvOrderPayTime.setVisibility(View.VISIBLE);
                if(timeShow.time.intValue()!=0) {
                    mActivityOrderDetailedBinding.tvOrderPayTime.setText(timeShow.time_name + "：" + DateUtil.getTimeString((timeShow.time * 1000L)));
                }else{
                    mActivityOrderDetailedBinding.tvOrderPayTime.setText(timeShow.time_name + "：暂无时间");
                }
            }
            if (orderDetailObj.time_show.length >=2) {
                TimeShow timeShow = orderDetailObj.time_show[1];
                mActivityOrderDetailedBinding.tvDeliveryTime.setVisibility(View.VISIBLE);
                if(timeShow.time.intValue()!=0) {
                    mActivityOrderDetailedBinding.tvDeliveryTime.setText(timeShow.time_name + "：" + DateUtil.getTimeString((timeShow.time * 1000L)));
                }else{
                    mActivityOrderDetailedBinding.tvDeliveryTime.setText(timeShow.time_name + "：暂无时间");
                }
            }
            if (orderDetailObj.time_show.length >=3) {
                TimeShow timeShow = orderDetailObj.time_show[2];
                mActivityOrderDetailedBinding.tvOrderOtherTime.setVisibility(View.VISIBLE);
                if(timeShow.time.intValue()!=0) {
                    mActivityOrderDetailedBinding.tvOrderOtherTime.setText(timeShow.time_name + "：" + DateUtil.getTimeString((timeShow.time * 1000L)));
                }else{
                    mActivityOrderDetailedBinding.tvOrderOtherTime.setText(timeShow.time_name + "：暂无时间");
                }
            }
        }
        mActivityOrderDetailedBinding.btContactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String store_mobilephone = orderDetail.store_mobilephone;
                if(!TextUtils.isEmpty(store_mobilephone)) {
                    //告诉系统我要打开拨号界面，并把要拨的号显示在拨号界面上，由用户决定是否要拨打。
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + store_mobilephone));
                    OrderDetailedActivity.this.startActivity(intent);
                }
            }
        });

        if(orderDetail.show_action_btn==1){
            mActivityOrderDetailedBinding.btHandler1.setText("取消订单");
            mActivityOrderDetailedBinding.btHandler1.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler1.setTag(orderDetail);

            mActivityOrderDetailedBinding.btHandler2.setText("付款");
            mActivityOrderDetailedBinding.btHandler2.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler2.setTag(orderDetail);
        }else if(orderDetail.show_action_btn==2){
            mActivityOrderDetailedBinding.btHandler1.setText("等待发货");
            mActivityOrderDetailedBinding.btHandler1.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler1.setBackgroundResource(R.color.item_line_cover);
            mActivityOrderDetailedBinding.btHandler1.setTextColor(getResources().getColor(R.color.text_gray_01));
            mActivityOrderDetailedBinding.btHandler1.setClickable(false);
            mActivityOrderDetailedBinding.btHandler1.setEnabled(false);
        }else if(orderDetail.show_action_btn==3){
            mActivityOrderDetailedBinding.btHandler2.setText("确认收货");
            mActivityOrderDetailedBinding.btHandler2.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler2.setTag(orderDetail);
        }else if(orderDetail.show_action_btn==4){
            mActivityOrderDetailedBinding.btHandler2.setText("交易成功");
            mActivityOrderDetailedBinding.btHandler2.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler2.setBackgroundResource(R.color.toolbar_color);
            mActivityOrderDetailedBinding.btHandler2.setClickable(false);
            mActivityOrderDetailedBinding.btHandler2.setEnabled(false);
        }else if(orderDetail.show_action_btn==5){
            mActivityOrderDetailedBinding.btHandler1.setText("等待全部发货");
            mActivityOrderDetailedBinding.btHandler1.setVisibility(View.VISIBLE);
            mActivityOrderDetailedBinding.btHandler1.setBackgroundResource(R.color.item_line_cover);
            mActivityOrderDetailedBinding.btHandler1.setTextColor(getResources().getColor(R.color.text_gray_01));
            mActivityOrderDetailedBinding.btHandler1.setClickable(false);
            mActivityOrderDetailedBinding.btHandler1.setEnabled(false);
        }


        mActivityOrderDetailedBinding.btHandler1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerOrderReq((Button) v);
            }
        });
        mActivityOrderDetailedBinding.btHandler2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlerOrderReq((Button) v);
            }
        });



        mActivityOrderDetailedBinding.lvGoodsList.setAdapter(new CommonAdapter<GoodsDetail>(this,orderDetailObj.sub_order[0].list ,R.layout.order_detail_list_item){

            @Override
            protected void convert(ViewHolder vh, final GoodsDetail item) {
               ImageView iv_ico = (ImageView) vh.getConvertView().findViewById(R.id.iv_goods);
                GlideUtil.getInstance().loadImage(OrderDetailedActivity.this,item.goods_picture,iv_ico);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_title)).setText(item.goods_name);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_desc)).setText(item.goods_attr_str);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_price)).setText("￥"+item.actual_total_price);
                ((TextView) vh.getConvertView().findViewById(R.id.tv_count)).setText("x"+item.goods_count);
                Button bt_refund = (Button) vh.getConvertView().findViewById(R.id.bt_refund);
                //ToastUtils.showToastShort(getBaseContext(),"订单状态："+item.refund_state);
                if(item.refund_state==0){
                //if(item.refund_state!=0){
                        if(new Date().getTime()<orderDetailObj.parent_order.allow_refund_time.longValue()*1000) {
                            bt_refund.setVisibility(View.VISIBLE);
                            bt_refund.setText("申请退货");
                            bt_refund.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getBaseContext(), ApplyRefundActivity.class);
                                    intent.putExtra(ApplyRefundActivity.APPLY_REFUND_ORDER_DETAIL_TAG, item);
                                    OrderDetailedActivity.this.startActivity(intent);
                                }
                            });
                        }
                    }else if(item.refund_state>0){
                        bt_refund.setVisibility(View.VISIBLE);
                        bt_refund.setText(item.refund_state_name);
                        bt_refund.setBackgroundResource(R.color.item_line_cover);
                        bt_refund.setTextColor(getResources().getColor(R.color.text_gray_01));
                        bt_refund.setClickable(false);
                        bt_refund.setEnabled(false);


                    }
                }

        });
    }

    private void handlerOrderReq(Button b) {
        Object obj = b.getTag();
        if(obj!=null){
            OrderDetail item = (OrderDetail)obj;
                if(item.show_action_btn==1){
                    switch (b.getId()){
                        case R.id.bt_handler1:
                            cancelOrderDialog(item);
                            break;
                        case R.id.bt_handler2:
                            payOrder(item);
                            break;
                    }

                }else if(item.show_action_btn==3){
                    confirmReceipt(item);
                }
        }
    }

    private void confirmReceipt(OrderDetail item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(OrderModel.class).updateOrderReceive(TAG, item.parent_order_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                OrderDetailedActivity.this.onStart();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });

    }

    private void payOrder(OrderDetail item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(OrderModel.class).orderPay(TAG, item.parent_order_id, item.pay_way, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                    if(null!=payH5){
                        readyToPay(payH5);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getBaseContext(),"获取数据异常");
                }

            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });


    }


    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(this,PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM,payH5.url);
        startActivity(intent);
        this.finish();
    }

    private void cancelOrder(OrderDetail item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(OrderModel.class).updateOrderCancel(TAG, item.parent_order_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                OrderDetailedActivity.this.onStart();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(OrderDetailedActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
            }
        });
    }

    private void cancelOrderDialog(final OrderDetail item) {
        alertDialog = VLDialog.showOkCancelDialog(this, this.getString(R.string.dialog_common_title), "确定要取消订单吗？", "确定", "取消", false,
                new VLDialog.DialogCallBack() {
                    @Override
                    public void ok(Object msg) {
                        alertDialog.cancel();
                        cancelOrder(item);
                    }
                    @Override
                    public void cancel() {
                    }
                });
    }
}
