package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.databinding.ActivityGoodsListBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
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
 * sunny created at 2016/10/17
 * des: 商品列表页
 */
public class GoodsListActivity extends BaseNavBackActivity {

    int pageIndex = 1 ;
    String mActiveId;
    GoodsAdapter mGoodsAdapter;
    ActivityGoodsListBinding mGoodsListBinding;
    private static final String TAG="GoodsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mGoodsListBinding= DataBindingUtil.setContentView(this, R.layout.activity_goods_list);
        setNavDefaultBack(mGoodsListBinding.tb);
        super.onCreate(savedInstanceState);
        initDatas(savedInstanceState);
        mGoodsAdapter = new GoodsAdapter(this,R.layout.item_etc_goods_item,null) ;
        mGoodsAdapter.setOnePageNum(Constants.PageSize);
        mGoodsAdapter.setOnGoodsListListener(new GoodsAdapter.OnGoodsListListener(){
            @Override
            public void onGoodsItemClick(String goodsId) {
                Intent intent=new Intent(GoodsListActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsId",goodsId);
                startActivity(intent);
            }
        });
        mGoodsListBinding.lrGoodsList.setAdapter(mGoodsAdapter);
        mGoodsListBinding.lrGoodsList.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGoodsList(true);
            }
        });
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
        mActiveId=getIntent().getExtras().getString("activeId");
    }

    @Override
    protected void loadData() {
        super.loadData();
        pageIndex = 1;
        getGoodsList(false);
    }

    /**
     * 获取列表
     * @param isLoadMore
     */
    public void getGoodsList(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getGoodsList(TAG,mActiveId,Constants.PageSize, pageIndex,
                new EntityCallBack<String>(new TypeToken<String>(){}) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        if(resp == null) return ;
                        try {
                            JSONObject jsonObj=new JSONObject(resp);
                            ArrayList<GoodsInfo> goodsList=GsonUtil.fromJsonList(jsonObj.optString("data"),GoodsInfo.class);
                            setData2List(goodsList, isLoadMore);
                        } catch (JSONException e) {
                            ToastUtils.showToastShort(GoodsListActivity.this,"获取数据异常");
                        }
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ToastUtils.showToastShort(getApplicationContext(),msg);
                    }
                });
    }

    private void setData2List(ArrayList<GoodsInfo> resp, boolean isLoadMore) {
        pageIndex ++ ;
        if(isLoadMore) {
            mGoodsAdapter.addAll(resp);
        } else {
            mGoodsAdapter.replace(resp);
        }
    }
}
