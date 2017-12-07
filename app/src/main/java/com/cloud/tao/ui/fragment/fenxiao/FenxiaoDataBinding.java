package com.cloud.tao.ui.fragment.fenxiao;

import android.content.Context;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoOrderDataInfo;
import com.cloud.tao.bean.etc.FenxiaoSubOrderInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.FenxiaoOrderAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * sunny created at 2016/10/26
 * des: 创业订单数据绑定通用
 */
public class FenxiaoDataBinding {

    private Context context;
    private LoadMoreRecyclerView rootListView;
    private String  TAG;
    private String mMyStoreId;
    int pageIndex = 1 ;
    String mState;
    FenxiaoOrderAdapter mFenxiaoOrderAdapter;

    public FenxiaoDataBinding(Context context, LoadMoreRecyclerView listView, String tag, String storeId, String state){
        this.context = context;
        this.rootListView = listView;
        this.TAG = tag;
        this.mMyStoreId=storeId;
        this.mState=state;
        mFenxiaoOrderAdapter = new FenxiaoOrderAdapter(context, R.layout.item_fenxiao_order_item,null) ;
        mFenxiaoOrderAdapter.setOnePageNum(Constants.PageSize);
        rootListView.setAdapter(mFenxiaoOrderAdapter);
        rootListView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(true);
            }
        });
        initData(false);
    }

    public void initData(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoOrderInfo(TAG,mMyStoreId,mState,pageIndex,new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    FenxiaoOrderDataInfo fenxiaoOrderDataInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), FenxiaoOrderDataInfo.class);
                    setData2List(fenxiaoOrderDataInfo.fenxiao_order_list,isLoadMore);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(context, "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(context, msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    private void setData2List(List<FenxiaoSubOrderInfo> resp, boolean isLoadMore) {
        pageIndex ++ ;
        if(isLoadMore) {
            mFenxiaoOrderAdapter.addAll(resp);
        } else {
            mFenxiaoOrderAdapter.replace(resp);
        }
    }
}
