package com.cloud.tao.ui.fragment.fenxiao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoCommissionDetailsTixianInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.FenxiaoCommissionTixianAdapter;
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
 * des: 佣金结算
 */
public class CommissionTixianFragment extends Fragment {

    private String myStoreId;
    private static final String TAG = CommissionTixianFragment.class.getSimpleName();
    View rootView;
    FenxiaoCommissionTixianAdapter mCommissionTixianAdapter;
    LoadMoreRecyclerView mLvcommissionList;
    int pageIndex=1;

    @Override
    public void setArguments(Bundle args) {
        myStoreId=args.getString("myStoreId");
        super.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.item_fenxiao_load_more, container,false);
        mLvcommissionList= (LoadMoreRecyclerView) rootView.findViewById(R.id.lr_fenxiao_load_list);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommissionTixianAdapter = new FenxiaoCommissionTixianAdapter(getActivity(), R.layout.item_commission_details_tixian_item,null) ;
        mCommissionTixianAdapter.setOnePageNum(Constants.PageSize);
        mLvcommissionList.setAdapter(mCommissionTixianAdapter);
        mLvcommissionList.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                initData(true);
            }
        });
        initData(false);
    }

    public void initData(final boolean isLoadMore) {
        MallApplication.getInstance().getModel(AppModel.class).getFenxiaoCommissionDetailsTiXianInfo(TAG,myStoreId,pageIndex,new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                if (resp == null) return;
                try {
                    JSONObject jsonObj = new JSONObject(resp);
                    ArrayList<FenxiaoCommissionDetailsTixianInfo> commissionDetailsInfos= GsonUtil.fromJsonList(jsonObj.optJSONObject("data").optString("tixian"), FenxiaoCommissionDetailsTixianInfo.class);
                    setData2List(commissionDetailsInfos,isLoadMore);
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getActivity(), "获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getActivity(), msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

    private void setData2List(ArrayList<FenxiaoCommissionDetailsTixianInfo> resp, boolean isLoadMore) {
        pageIndex ++ ;
        if(isLoadMore) {
            mCommissionTixianAdapter.addAll(resp);
        } else {
            mCommissionTixianAdapter.replace(resp);
        }
    }
}
