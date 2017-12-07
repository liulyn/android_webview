package com.cloud.tao.ui.fragment.order;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.Order;
import com.cloud.tao.bean.etc.OrderChild;
import com.cloud.tao.bean.etc.OrderObj;
import com.cloud.tao.bean.etc.PayH5;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.cloud.tao.ui.activity.GoodsDetailsActivity;
import com.cloud.tao.ui.activity.OrderDetailedActivity;
import com.cloud.tao.ui.activity.PayWebViewActivity;
import com.cloud.tao.ui.widget.RefreshLayout;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gezi-pc on 2016/10/19.
 */

public class MyOrderDataBinding {

    private Activity activity;
    private ListView rootListView;
    private String  TAG;
    private ArrayList<Order> order_list = new ArrayList<>();
    private RefreshLayout myRefreshListView;
    private int p = 1;
    private String state;
    private CommonAdapter commonAdapter;
    private AlertDialog alertDialog;


    public MyOrderDataBinding(Activity activity,RefreshLayout refreshListView, ListView listView, String state, String tag){
        this.activity = activity;
        this.rootListView = listView;
        this.TAG = tag;
        this.myRefreshListView = refreshListView;
        this.state = state;
        initRefreshView();
    }



    public MyOrderDataBinding loadData() {
        MallApplication.getInstance().getModel(OrderModel.class).getMyOrder(TAG, p, state, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                OrderObj orderObj = GsonUtil.GsonToBean(jsonObj.optString("data"), OrderObj.class);
                if (orderObj.order_list != null && orderObj.order_list.size() > 0) {
                    if (p == 1) {
                        order_list.clear();
                        order_list.addAll(orderObj.order_list);
                    } else {

                        order_list.addAll(orderObj.order_list);

                    }
                }
                if(orderObj.order_list == null
                        || orderObj.order_list.isEmpty()){
                    myRefreshListView.setLast(true);
                }


                fillData();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {

                ToastUtils.showToastShort(activity,msg);
                Log.i(TAG,msg);
            }
        });


        return this;

    }

    private void fillData() {
        if(commonAdapter==null) {
            commonAdapter = new CommonAdapter<Order>(activity, order_list, R.layout.my_order_list_item) {
                @Override
                protected void convert(ViewHolder vh, final Order item) {
                    String order_number = MessageFormat.format(activity.getResources().getString(R.string.order_list_order_name).toString(), item.parent_order_id);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_number)).setText(order_number);
                    ((TextView) vh.getConvertView().findViewById(R.id.tv_status)).setText(item.state_name);

                    Button handler1 = (Button) vh.getConvertView().findViewById(R.id.bt_handler1);
                    Button handler2 = (Button) vh.getConvertView().findViewById(R.id.bt_handler2);
                    Button handler3 = (Button) vh.getConvertView().findViewById(R.id.bt_handler3);


                    handler3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String orderId = item.parent_order_id;
                            Intent intent = new Intent(activity, OrderDetailedActivity.class);
                            intent.putExtra(OrderDetailedActivity.ORDER_DETAILED_ID, orderId);
                            activity.startActivity(intent);
                        }
                    });
                    handler3.setVisibility(View.VISIBLE);


                    if (item.show_action_btn == 1) {
                        handler1.setText("取消订单");
                        handler1.setVisibility(View.VISIBLE);
                        handler1.setTag(item);
                        handler1.setBackgroundResource(R.drawable.my_order_cancel_selector);
                        handler1.setTextColor(Color.parseColor("#F84241"));

                        handler2.setText("付款");
                        handler2.setVisibility(View.VISIBLE);
                        handler2.setTag(item);
                    } else if (item.show_action_btn == 2) {
                        handler1.setText("等待发货");
                        handler1.setVisibility(View.VISIBLE);
                        handler1.setBackgroundResource(R.color.item_line_cover);
                        handler1.setTextColor(activity.getResources().getColor(R.color.text_gray_01));
                        handler1.setClickable(false);
                        handler1.setEnabled(false);
                    } else if (item.show_action_btn == 3) {
                        handler2.setText("确认收货");
                        handler2.setVisibility(View.VISIBLE);
                        handler2.setTag(item);
                    } else if (item.show_action_btn == 4) {

                        handler1.setVisibility(View.VISIBLE);
                        handler1.setText("重新购买");
                        handler1.setTextColor(Color.parseColor("#F84241"));
                        handler1.setBackgroundResource(R.drawable.my_order_cancel_selector);
                        handler1.setTag(item);

                    } else if (item.show_action_btn == 5) {
                        handler1.setText("等待全部发货");
                        handler1.setVisibility(View.VISIBLE);
                        handler1.setBackgroundResource(R.color.item_line_cover);
                        handler1.setTextColor(activity.getResources().getColor(R.color.text_gray_01));
                        handler1.setClickable(false);
                        handler1.setEnabled(false);
                    }


                    handler1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlerOrderReq((Button) v);
                        }
                    });
                    handler2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            handlerOrderReq((Button) v);
                        }
                    });


                    ListView goods = (ListView) vh.getConvertView().findViewById(R.id.lv_goods);

                    List<OrderChild> items = Arrays.asList(item.son);
                    goods.setAdapter(new CommonAdapter<OrderChild>(activity, items, R.layout.my_order_list_item_item) {
                        @Override
                        protected void convert(ViewHolder vh1, OrderChild childItem) {
                            ((TextView) vh1.getConvertView().findViewById(R.id.tv_number)).setText("x" + childItem.goods_count);
                            ((TextView) vh1.getConvertView().findViewById(R.id.tv_title)).setText(childItem.goods_name);
                            ((TextView) vh1.getConvertView().findViewById(R.id.tv_price)).setText(childItem.goods_price);
                            ((TextView) vh1.getConvertView().findViewById(R.id.tv_desc)).setText(childItem.goods_attr_str);
                            ImageView iv_ico = (ImageView) vh1.getConvertView().findViewById(R.id.iv_goods);
                            GlideUtil.getInstance().loadImage(activity, childItem.goods_picture, iv_ico);
                            //Log.e(TAG,childItem.toString());
                        }
                    });

                }
            };

            rootListView.setAdapter(commonAdapter);
        }else{
            commonAdapter.notifyDataSetChanged();
        }


    }




    private void handlerOrderReq(Button b) {
        Object obj = b.getTag();
        if(obj!=null){
            Order item = (Order)obj;
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
            }else if(item.show_action_btn == 4){
                goodsDetail(item);
            }
        }
    }

    /**
     * 重新购买
     * @param item
     */
    private void goodsDetail(Order item) {
        Intent intent = new Intent(activity, GoodsDetailsActivity.class);
        intent.putExtra("goodsId",item.son[0].goods_id);
        activity.startActivity(intent);
    }

    /**
     * 确认收货
     * @param item
     */
    private void confirmReceipt(final Order item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(activity, activity.getString(R.string.dialog_common_title), activity.getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(OrderModel.class).updateOrderReceive(TAG, item.parent_order_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);
                commonAdapter = null;
                // 更新数据
                MyOrderDataBinding.this.p = 1;
                loadData();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);
                Log.i(TAG,msg);
            }
        });

    }

    /**
     * 付款
     * @param item
     */
    private void payOrder(final Order item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(activity, activity.getString(R.string.dialog_common_title), activity.getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(OrderModel.class).orderPay(TAG, item.parent_order_id, item.pay_way, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);
                order_list.remove(item);
                commonAdapter.notifyDataSetChanged();
                Log.e(TAG,resp);
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    PayH5 payH5= GsonUtil.GsonToBean(jsonObj.optString("data"), PayH5.class);
                    if(null!=payH5){
                        readyToPay(payH5);
                    }
                } catch (JSONException e) {
                    ToastUtils.showToastShort(activity,"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);
                Log.i(TAG,msg);
            }
        });


    }

    private void readyToPay(PayH5 payH5) {
//        Uri  uri = Uri.parse(payH5.url);
//        Intent  intent = new  Intent(Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        Intent intent = new Intent(activity,PayWebViewActivity.class);
        intent.putExtra(PayWebViewActivity.LOAD_PAY_URL_PARAM,payH5.url);
        activity.startActivity(intent);
    }

    /**
     * 取消订单
     * @param item
     */
    private void cancelOrder(final Order item) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(activity, activity.getString(R.string.dialog_common_title), activity.getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(OrderModel.class).updateOrderCancel(TAG, item.parent_order_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);

                commonAdapter = null;
                // 更新数据
                MyOrderDataBinding.this.p = 1;
                loadData();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(activity, progressDialog);
                ToastUtils.showToastShort(activity,msg);
                Log.i(TAG,msg);
            }
        });
    }



    private void initRefreshView() {

        // 设置下拉刷新监听器
        myRefreshListView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                //Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(commonAdapter!=null) {
                            // 更新数据
                            MyOrderDataBinding.this.p = 1;
                            loadData();
                        }

                        // 更新完后调用该方法结束刷新
                        myRefreshListView.setRefreshing(false);

                    }
                }, 500);
            }
        });

        // 加载监听器
        myRefreshListView.setOnLoadListener(new RefreshLayout.OnLoadListener() {

            @Override
            public void onLoad() {

                //Toast.makeText(activity, "load", Toast.LENGTH_SHORT).show();

                myRefreshListView.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if(commonAdapter!=null) {
                            MyOrderDataBinding.this.p++;
                            loadData();
                        }
                        // 加载完后调用该方法
                        myRefreshListView.setLoading(false);
                    }
                }, 500);

            }
        });
    }

    private void cancelOrderDialog(final Order item) {
        alertDialog = VLDialog.showOkCancelDialog(activity, activity.getString(R.string.dialog_common_title), "确定要取消订单吗？", "确定", "取消", false,
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
