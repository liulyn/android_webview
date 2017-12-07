package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.base.BaseShowFragment;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.databinding.ActivityGoodsListBinding;
import com.cloud.tao.databinding.FragmentActiveBinding;
import com.cloud.tao.databinding.ItemAllGoodsBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.activity.GoodsDetailsActivity;
import com.cloud.tao.ui.adapter.etc.GoodsAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * sunny created at 2016/9/24
 * des: （云之道）所有商品 主页
 */
public class AllGoodsActivity extends BaseNavBackActivity {

    int pageIndex = 1;
    String mActiveId;
    GoodsAdapter mGoodsAdapter;
    ItemAllGoodsBinding mGoodsListBinding;
    private static final String TAG = "AllGoodsActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mGoodsListBinding=DataBindingUtil.setContentView(this, R.layout.item_all_goods);
        setNavDefaultBack(mGoodsListBinding.tb);
        super.onCreate(savedInstanceState);
        mGoodsAdapter = new GoodsAdapter(AllGoodsActivity.this, R.layout.item_etc_goods_item, null);
        mGoodsAdapter.setOnePageNum(Constants.PageSize);
        mGoodsAdapter.setOnGoodsListListener(new GoodsAdapter.OnGoodsListListener() {
            @Override
            public void onGoodsItemClick(String goodsId) {
                Intent intent = new Intent(AllGoodsActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsId", goodsId);
                startActivity(intent);
            }
        });
        mGoodsListBinding.lrAllGoodsList.setAdapter(mGoodsAdapter);
        mGoodsListBinding.lrAllGoodsList.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGoodsList(true);
            }
        });
        loadData();
    }

    protected void loadData() {
        pageIndex = 1;
        getGoodsList(false);
    }

    /**
     * 获取列表
     *
     * @param isLoadMore
     */
    public void getGoodsList(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getAllGoodsList(TAG, null, Constants.PageSize, pageIndex,
                new EntityCallBack<String>(new TypeToken<String>() {
                }) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        if (resp == null) return;
                        try {
                            JSONObject jsonObj = new JSONObject(resp);
                            ArrayList<GoodsInfo> goodsList = GsonUtil.fromJsonList(jsonObj.optString("data"), GoodsInfo.class);
                            setData2List(goodsList, isLoadMore);
                        } catch (JSONException e) {
                            ToastUtils.showToastShort(AllGoodsActivity.this, "获取数据异常");
                        }
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ToastUtils.showToastShort(AllGoodsActivity.this, msg);
                    }
                });
    }

    private void setData2List(ArrayList<GoodsInfo> resp, boolean isLoadMore) {
        pageIndex++;
        if (isLoadMore) {
            mGoodsAdapter.addAll(resp);
        } else {
            mGoodsAdapter.replace(resp);
        }
    }
}
