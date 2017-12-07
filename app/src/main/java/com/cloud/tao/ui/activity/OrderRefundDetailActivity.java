package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.AccountInfo;
import com.cloud.tao.bean.etc.ChatMessage;
import com.cloud.tao.bean.etc.OrderDetailMessage;
import com.cloud.tao.bean.etc.OrderMessage;
import com.cloud.tao.databinding.ActivityOrderRefundDetailBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.ordermodel.OrderModel;
import com.cloud.tao.ui.adapter.etc.ChatMessageAdapter;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gezi-pc on 2016/10/23.
 */

public class OrderRefundDetailActivity extends BaseNavBackActivity {

    private static final String TAG = OrderRefundDetailActivity.class.getSimpleName();
    ActivityOrderRefundDetailBinding mActivityOrderRefundDetailBinding;
    public static final String ORDER_REFUND_DETAIL_PARENT_ORDER_ID ="order_refund_detail_parent_order_id";
    public static final String ORDER_REFUND_DETAIL_SUB_ORDER_ID ="order_refund_detail_sub_order_id";
    private String parent_order_id ,sub_order_id;
    private ListView mMsgs;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> mDatas;
    private OrderDetailMessage orderDetailMessage;
    private String username,headImageUrl;


    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 等待子线程,数据的返回
            ChatMessage fromMessage = (ChatMessage) msg.obj;
            mDatas.add(fromMessage);
            adapter.notifyDataSetChanged();
            mMsgs.setSelection(adapter.getCount()-1);
        };
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityOrderRefundDetailBinding =  DataBindingUtil.setContentView(this, R.layout.activity_order_refund_detail);
        setNavDefaultBack(mActivityOrderRefundDetailBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityOrderRefundDetailBinding.loadding.showLoadSuccess();
        mMsgs = mActivityOrderRefundDetailBinding.idListviewMsg;
        initListener();
        initDatas(savedInstanceState);

    }

    private void initListener() {

        mDatas = new ArrayList<ChatMessage>();
        adapter = new ChatMessageAdapter(OrderRefundDetailActivity.this,mDatas);
        mMsgs.setAdapter(adapter);

        mActivityOrderRefundDetailBinding.idSendMsg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String toMsg = mActivityOrderRefundDetailBinding.idInputMsg.getText().toString();
                if (TextUtils.isEmpty(toMsg)) {
                    ToastUtils.showToastShort(OrderRefundDetailActivity.this, "评论内容不能为空!");
                    return;
                }
                final ProgressDialog progressDialog = VLDialog.showProgressDialog(OrderRefundDetailActivity.this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
                MallApplication.getInstance().getModel(OrderModel.class).addOrderMessage(TAG, parent_order_id, sub_order_id,toMsg,new EntityCallBack<String>(new TypeToken<String>(){}){
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        ViewUtils.dismissDialog(OrderRefundDetailActivity.this, progressDialog);
                        ChatMessage cm = new ChatMessage();
                        cm.setDate(new Date());
                        cm.setMsg(toMsg);
                        cm.setName(username);
                        cm.setType(ChatMessage.Type.OUTCOMEING);
                        cm.setHeadImageUrl(headImageUrl);
                        mDatas.add(cm);
                        adapter.notifyDataSetChanged();
                        mActivityOrderRefundDetailBinding.idInputMsg.setText("");
                        Log.e(TAG,msg);
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ViewUtils.dismissDialog(OrderRefundDetailActivity.this, progressDialog);
                        ToastUtils.showToastShort(getBaseContext(),msg);
                        Log.e(TAG,msg);
                    }
                });


            }
        });

    }





    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        AccountInfo info = (AccountInfo) SharePrefUtil.getObjectFromShare(this,SharePrefUtil.KEY.function_user_info);
        if(info.private_name!=null&&!"".equals(info.private_name)) {
            username = info.private_name;
        }
        if(info.private_headimgurl!=null&&!"".equals(info.private_headimgurl)) {
            headImageUrl = info.private_headimgurl;
        }

        parent_order_id = getIntent().getStringExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_PARENT_ORDER_ID);
        sub_order_id = getIntent().getStringExtra(OrderRefundDetailActivity.ORDER_REFUND_DETAIL_SUB_ORDER_ID);
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(OrderModel.class).getOrderRefundDetail(TAG, parent_order_id, sub_order_id, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp){
                ViewUtils.dismissDialog(OrderRefundDetailActivity.this, progressDialog);
                JSONObject jsonObj= null;
                try {
                    jsonObj = new JSONObject(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderDetailMessage = GsonUtil.GsonToBean(jsonObj.optString("data"),OrderDetailMessage.class);
                fillData();
                Log.e(TAG,resp);

            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(OrderRefundDetailActivity.this, progressDialog);
                ToastUtils.showToastShort(getBaseContext(),msg);
                Log.e(TAG,msg);
            }
        });

    }

    private void fillData() {
        mActivityOrderRefundDetailBinding.tvStoreName.setText(orderDetailMessage.parent_order.store_name);
        mActivityOrderRefundDetailBinding.tvRufundState.setText(orderDetailMessage.sub_order.refund_state_name);
        mActivityOrderRefundDetailBinding.tvCount.setText("x"+orderDetailMessage.sub_order.goods_count);
        mActivityOrderRefundDetailBinding.tvDesc.setText(orderDetailMessage.sub_order.goods_attr_str);
        ImageView ivGoods = mActivityOrderRefundDetailBinding.ivGoods;
        GlideUtil.getInstance().loadImage(OrderRefundDetailActivity.this,orderDetailMessage.sub_order.goods_picture,ivGoods);
        mActivityOrderRefundDetailBinding.tvTitle.setText(orderDetailMessage.sub_order.goods_name);
        mActivityOrderRefundDetailBinding.tvGoodsPrice.setText("交易金额：￥"+orderDetailMessage.sub_order.goods_price);
        mActivityOrderRefundDetailBinding.tvRefundPrice.setText("退款金额：￥"+orderDetailMessage.sub_order.actual_total_price);

        for(OrderMessage om:orderDetailMessage.message){
            ChatMessage cm = new ChatMessage();
            cm.setDate(new Date(om.create_time));
            cm.setMsg(om.message);
            cm.setName(om.type_name);
            cm.setHeadImageUrl(headImageUrl);
            if(om.type==0) {
                cm.setType(ChatMessage.Type.INCOMING);
            }else if(om.type==1){
                cm.setType(ChatMessage.Type.OUTCOMEING);
            }
            mDatas.add(cm);
        }


    }
}
