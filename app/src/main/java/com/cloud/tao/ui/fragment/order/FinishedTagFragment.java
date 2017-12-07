package com.cloud.tao.ui.fragment.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cloud.tao.R;
import com.cloud.tao.ui.widget.RefreshLayout;

public class FinishedTagFragment extends Fragment {


	private static final String TAG = FinishedTagFragment.class.getSimpleName();
	View rootview;
	ListView orders;
	private MyOrderDataBinding mob;
	private RefreshLayout myRefreshListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_all_order, container,false);
		return rootview;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		orders = (ListView) rootview.findViewById(R.id.lv_order);

		// 获取RefreshLayout实例
		myRefreshListView = (RefreshLayout)rootview.findViewById(R.id.swipe_layout);
		// 设置下拉刷新时的颜色值,颜色值需要定义在xml中
		myRefreshListView.setColorSchemeResources(R.color.red_bg,R.color.red_bg,R.color.red_bg,R.color.red_bg);
		mob =new MyOrderDataBinding(getActivity(),myRefreshListView,orders,"success",TAG).loadData();

	}


}
