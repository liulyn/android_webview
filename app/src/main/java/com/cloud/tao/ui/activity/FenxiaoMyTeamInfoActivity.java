package com.cloud.tao.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.FenxiaoMyTeamAllInfo;
import com.cloud.tao.bean.etc.FenxiaoMyTeamInfo;
import com.cloud.tao.bean.etc.FenxiaoMyTeamRecommendInfo;
import com.cloud.tao.databinding.ActivityFenxiaoMyTeamBinding;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.FenxiaoMyTeamParentAdapter;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;
import com.cloud.tao.util.Constants;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.glide.GlideUtil;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * sunny created at 2016/10/25
 * des: 修改创业团队
 */
public class FenxiaoMyTeamInfoActivity extends BaseNavBackActivity {

    private static final String TAG = "FenxiaoMyTeamInfoActivity";
    ActivityFenxiaoMyTeamBinding mFenxiaoMyTeamBinding;
    FenxiaoMyTeamParentAdapter mFenxiaoMyTeamParentAdapter;
    String mMyStoreId;
    int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFenxiaoMyTeamBinding = DataBindingUtil.setContentView(this, R.layout.activity_fenxiao_my_team);
        setNavDefaultBack(mFenxiaoMyTeamBinding.tb);
        mMyStoreId = getIntent().getExtras().getString("myStoreId");
        mFenxiaoMyTeamParentAdapter = new FenxiaoMyTeamParentAdapter(this, R.layout.item_fenxiao_my_parent_team, null);
        mFenxiaoMyTeamParentAdapter.setOnePageNum(Constants.PageSize);
        mFenxiaoMyTeamBinding.lvFenxiaoMyTeamList.setAdapter(mFenxiaoMyTeamParentAdapter);
        mFenxiaoMyTeamBinding.lvFenxiaoMyTeamList.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                getFenxiaoMyTeamInfo(true);
            }
        });
        getFenxiaoMyTeamInfo(false);
        super.onCreate(savedInstanceState);
    }


    private void getFenxiaoMyTeamInfo(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoMyTeamInfo(TAG, mMyStoreId, pageIndex, new EntityCallBack<String>(new TypeToken<String>() {
        }) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    FenxiaoMyTeamAllInfo mFenxiaoMyTeamAllInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), FenxiaoMyTeamAllInfo.class);
                    List<FenxiaoMyTeamInfo> fenxiaoMyTeamInfos = mFenxiaoMyTeamAllInfo.fenxiao_team_list;
                    mFenxiaoMyTeamBinding.tvFenxiaoTeamNum.setText(mFenxiaoMyTeamAllInfo.total_num + "个成员");
                    FenxiaoMyTeamRecommendInfo mFecommendInfo = mFenxiaoMyTeamAllInfo.recommend_info;
                    if (mFecommendInfo != null) {
                        mFenxiaoMyTeamBinding.llFenxiaoTeamRecommend.setVisibility(View.VISIBLE);
                        if (!TextUtils.isEmpty(mFecommendInfo.private_headimgurl)) {
                            GlideUtil.getInstance().loadImage(FenxiaoMyTeamInfoActivity.this, mFecommendInfo.private_headimgurl, mFenxiaoMyTeamBinding.tvMyTeamIcon);
                        } else {
                            GlideUtil.getInstance().loadImage(FenxiaoMyTeamInfoActivity.this, R.mipmap.ic_fenxiao_team_default, mFenxiaoMyTeamBinding.tvMyTeamIcon);
                        }
                        mFenxiaoMyTeamBinding.tvMyTeamRecommendPhone.setText(mFecommendInfo.login_mobilephone);
                        mFenxiaoMyTeamBinding.tvMyTeamRecommendRank.setText(mFecommendInfo.rank_name);
                    }
                    mFenxiaoMyTeamBinding.tvFenxiaoCommissionTotal.setText("一共累计佣金：￥"+mFenxiaoMyTeamAllInfo.commission_total);
                    setData2List(fenxiaoMyTeamInfos, isLoadMore);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(FenxiaoMyTeamInfoActivity.this, "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(FenxiaoMyTeamInfoActivity.this, msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    /*private void parseData(List<FenxiaoMyTeamInfo> fenxiaoMyTeamInfos){
        if(null!=fenxiaoMyTeamInfos){
            mFenxiaoMyTeamBinding.tvFenxiaoMyTeamCount.setText(""+fenxiaoMyTeamInfos.size());
        }
        mFenxiaoMyTeamParentAdapter.addAll(fenxiaoMyTeamInfos);
    }*/

    private void setData2List(List<FenxiaoMyTeamInfo> resp, boolean isLoadMore) {
        pageIndex++;
        if (isLoadMore) {
            mFenxiaoMyTeamParentAdapter.addAll(resp);
        } else {
            mFenxiaoMyTeamParentAdapter.replace(resp);
        }
    }

    @Override
    protected void initDatas(Bundle savedInstanceState) {
        super.initDatas(savedInstanceState);
    }
}
