package com.cloud.tao.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.GoodsInfo;
import com.cloud.tao.callback.NoDoubleClickListener;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.ui.adapter.etc.GoodsAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.ui.widget.SearchEditTextView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SoftInputUtils;
import com.google.gson.reflect.TypeToken;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.databinding.ActivitySearchBinding;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.util.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * sunny created at 2016/10/21
 * des: 商品搜索
 */
public class SearchActivity extends BaseNavBackActivity{

    private static final String TAG = "SearchActivity";
    int pageIndex = 1 ;
    GoodsAdapter mGoodsAdapter;
    ActivitySearchBinding mSearchBinding;
    String mSearchContent;

    @Override
    protected void initViews() {
        super.initViews();
        mSearchBinding=DataBindingUtil.setContentView(this, R.layout.activity_search);
        mGoodsAdapter = new GoodsAdapter(this,R.layout.item_etc_goods_item,null) ;
        mGoodsAdapter.setOnePageNum(Constants.PageSize);
        mGoodsAdapter.setOnGoodsListListener(new GoodsAdapter.OnGoodsListListener(){
            @Override
            public void onGoodsItemClick(String goodsId) {
                Intent intent=new Intent(SearchActivity.this, GoodsDetailsActivity.class);
                intent.putExtra("goodsId",goodsId);
                startActivity(intent);
            }
        });
        mSearchBinding.lrGoodsList.setAdapter(mGoodsAdapter);
        mSearchBinding.lrGoodsList.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getGoodsList(true);
            }
        });
        mSearchBinding.etSearch.setOnSearchClickListener(new SearchEditTextView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                toSearch();
            }
        });
        mSearchBinding.ivSearchCancel.setOnClickListener(mOnInToAttrClickListener);
        mSearchBinding.ivSearch.setOnClickListener(mOnInToAttrClickListener);

        mGoodsAdapter.setFooterVisibility(false);
    }

    private void toSearch(){
        mSearchContent=mSearchBinding.etSearch.getText().toString().trim();
        if(TextUtils.isEmpty(mSearchContent)) return;
        if(!mGoodsAdapter.isLoading()){
            mGoodsAdapter.reset();
            SoftInputUtils.closeSoftInput(SearchActivity.this,mSearchBinding.etSearch);
            pageIndex=1;
            getGoodsList(false);
        }
    }

    private NoDoubleClickListener mOnInToAttrClickListener = new NoDoubleClickListener() {
        @Override
        public void noDoubleClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_search_cancel:
                        SearchActivity.this.finish();
                        break;
                    case R.id.iv_search:
                        toSearch();
                        break;
                    default:break;
                }
        }
    };

    /**
     * 获取列表
     * @param isLoadMore
     */
    public void getGoodsList(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getSearchGoodsList(TAG,mSearchContent,Constants.PageSize, pageIndex,
                new EntityCallBack<String>(new TypeToken<String>(){}) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        if(resp == null) return ;
                        try {
                            JSONObject jsonObj=new JSONObject(resp).optJSONObject("data");
                            int num=jsonObj.optInt("where_num");
                            if(num==0){
                                setData2List(null, isLoadMore);
                                return;
                            }
                            ArrayList<GoodsInfo> goodsList= GsonUtil.fromJsonList(jsonObj.optString("data"),GoodsInfo.class);
                            setData2List(goodsList, isLoadMore);
                        } catch (JSONException e) {
                            ToastUtils.showToastShort(SearchActivity.this,"获取数据异常");
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

    @Override
    protected void onStart() {
        super.onStart();
        mSearchBinding.etSearch.setFocusable(true);
        mSearchBinding.etSearch.setFocusableInTouchMode(true);
        mSearchBinding.etSearch.requestFocus();
        SoftInputUtils.openSoftInput(this, mSearchBinding.etSearch);
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
    }

}
