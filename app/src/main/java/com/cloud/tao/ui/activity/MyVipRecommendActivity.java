package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.MyVipRecommendInfo;
import com.cloud.tao.bean.etc.MyVipRecommendItemInfo;
import com.cloud.tao.databinding.ActivityVipRecommendBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.MyVipRecommendParentAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * sunny created at 2017/6/24
 * des:推荐云豆列表
 */
public class MyVipRecommendActivity extends BaseNavBackActivity {
    private static final String TAG = "MyVipRecommendActivity";
    private ActivityVipRecommendBinding mActivityVipRecommendBinding=null;
    MyVipRecommendParentAdapter mMyVipRecommendParentAdapter;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityVipRecommendBinding =  DataBindingUtil.setContentView(this, R.layout.activity_vip_recommend);
        setNavDefaultBack(mActivityVipRecommendBinding.tb);
        mMyVipRecommendParentAdapter = new MyVipRecommendParentAdapter(this, R.layout.item_vip_parent_recommend, null);
        mMyVipRecommendParentAdapter.setOnePageNum(Constants.PageSize);
        mActivityVipRecommendBinding.lvVipRecommend.setAdapter(mMyVipRecommendParentAdapter);
        mActivityVipRecommendBinding.lvVipRecommend.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getDataMain(true);
            }
        });
        getDataMain(false);
        super.onCreate(savedInstanceState);
    }

    private void getDataMain(final boolean isLoadMore){
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoScoreRecommend(TAG, pageIndex, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    MyVipRecommendInfo mMyVipRecommendInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), MyVipRecommendInfo.class);
                    mActivityVipRecommendBinding.tvVipRecommendTotal.setText("￥"+mMyVipRecommendInfo.reward);
                    List<MyVipRecommendItemInfo> mVipRecommendItemInfo = mMyVipRecommendInfo.yd_bonus;
                    setData2List(mVipRecommendItemInfo, isLoadMore);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(MyVipRecommendActivity.this, "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(MyVipRecommendActivity.this, msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    private void setData2List(List<MyVipRecommendItemInfo> resp, boolean isLoadMore) {
        pageIndex++;
        if (isLoadMore) {
            mMyVipRecommendParentAdapter.addAll(resp);
        } else {
            mMyVipRecommendParentAdapter.replace(resp);
        }
    }
}
