package com.cloud.tao.ui.fragment.fenxiao;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.tao.R;
import com.cloud.tao.ui.widget.LoadMoreRecyclerView;

/**
 * sunny created at 2016/10/26
 * des: 创业订单已完成
 */
public class FenxiaoOrderSuccessFragment extends Fragment {

    private String myStoreId;
    private static final String TAG = FenxiaoOrderSuccessFragment.class.getSimpleName();
    FenxiaoDataBinding dataBinding;
    View rootView;
    LoadMoreRecyclerView mLvcommissionList;

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
        dataBinding=new FenxiaoDataBinding(getActivity(),mLvcommissionList,TAG,myStoreId,"success");
    }
}
