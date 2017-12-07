package com.cloud.tao.ui.fragment.fenxiao;

import android.content.Context;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailsInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.FenxiaoCommissionAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * sunny created at 2016/10/26
 * des: 佣金明细共用数据绑定 （已结算、待结算、冻结状态共用）
 */
public class CommissionDataBinding {

    private Context context;
    private LoadMoreRecyclerView rootListView;
    private String  TAG;
    private String mMyStoreId;
    int pageIndex = 1 ;
    int mState;
    FenxiaoCommissionAdapter mCommissionAdapter;

    public CommissionDataBinding(Context context, LoadMoreRecyclerView listView, String tag,String storeId,int state){
        this.context = context;
        this.rootListView = listView;
        this.TAG = tag;
        this.mMyStoreId=storeId;
        this.mState=state;
        mCommissionAdapter = new FenxiaoCommissionAdapter(context, R.layout.item_commission_details_item,null) ;
        mCommissionAdapter.setOnePageNum(Constants.PageSize);
        rootListView.setAdapter(mCommissionAdapter);
        rootListView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(true);
            }
        });
        initData(false);
    }

    public void initData(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoCommissionDetailsInfo(TAG,mMyStoreId,mState,pageIndex,new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    ArrayList<FenxiaoCommissionDetailsInfo> commissionDetailsInfo = GsonUtil.fromJsonList(jsonObj.optJSONObject("data").optString("reward"), FenxiaoCommissionDetailsInfo.class);
                    setData2List(commissionDetailsInfo,isLoadMore);
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

    private void setData2List(ArrayList<FenxiaoCommissionDetailsInfo> resp, boolean isLoadMore) {
        pageIndex ++ ;
        if(isLoadMore) {
            mCommissionAdapter.addAll(resp);
        } else {
            mCommissionAdapter.replace(resp);
        }
    }
}
