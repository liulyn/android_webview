package com.cloud.tao.ui.fragment.cashier;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.CashierDetail;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.cashier.CashierModel;
import com.cloud.tao.ui.widget.LoadTipLayout;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 收银明细
 */
public class CashierDetailTagFragment extends Fragment {


	private static final String TAG = CashierDetailTagFragment.class.getSimpleName();
	View rootview;
	private CommonAdapter commonAdapter;
	ListView cashierHistory;
	private LoadTipLayout loadding;
	private CashierDetail dashierDetail;
	private TextView tvReturnScore;
	private TextView tvAvailableScore;
	private TextView tvStockScore;
	private Button btSearch;
	private EditText etSearch;
	private String name;
	private int l = 1000;
	private int p = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_cashier_detail, container,false);
		return rootview;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		cashierHistory = (ListView) rootview.findViewById(R.id.lv_cashier_history);
		loadding = (LoadTipLayout)rootview.findViewById(R.id.loadding);
		loadding.showLoadSuccess();
		initView();
		initListener();
		loadData();
	}

	private void initListener() {
		btSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				name = etSearch.getText().toString();
				if(TextUtils.isEmpty(etSearch.getText())){
					name = null;
				}

				loadData();
			}
		});
	}
	private void initView() {
		tvStockScore=(TextView)rootview.findViewById(R.id.tv_stock_score);
		tvAvailableScore=(TextView)rootview.findViewById(R.id.tv_available_score);
		tvReturnScore=(TextView)rootview.findViewById(R.id.tv_return_score);
		btSearch=(Button)rootview.findViewById(R.id.bt_search);
		etSearch=(EditText)rootview.findViewById(R.id.et_search);
	}


	private void loadData() {
		loadding.showLoadSuccess();
		final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

		//errorCode   1:商家尚未开启收银平台功能 2:用户还不是会员，需要先开通会员卡
		MallApplication.getInstance().getModel(CashierModel.class).giveList(TAG,l,p,name,new EntityCallBack<String>(new TypeToken<String>(){}) {
			@Override
			public void onSuccess(int code, String msg, String resp) {
				ViewUtils.dismissDialog(getActivity(), progressDialog);
				Log.e(TAG,msg);
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(resp);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				dashierDetail = GsonUtil.GsonToBean(jsonObj.optString("data"), CashierDetail.class);
				Log.e(TAG,dashierDetail.toString());
				fillData();
			}

			@Override
			public void onFail(int code, Exception e, String msg) {
				ViewUtils.dismissDialog(getActivity(), progressDialog);
				ToastUtils.showToastShort(getActivity(),msg);
				Log.i(TAG,msg);
			}
		});

	}

			private void fillData() {

			tvStockScore.setText("库存云豆："+dashierDetail.score_inventory);
			tvAvailableScore.setText("可用云豆："+dashierDetail.score_to_exchange);
			tvReturnScore.setText("待返云豆："+dashierDetail.remain_score);
			commonAdapter = new CommonAdapter<CashierDetail.CashierDetailList>(getActivity(),dashierDetail.give_order,R.layout.fragment_cashier_detail_item) {
				@Override
				protected void convert(ViewHolder vh, CashierDetail.CashierDetailList item) {
					((TextView)vh.getConvertView().findViewById(R.id.tv_username)).setText(item.giver_name);
					((TextView)vh.getConvertView().findViewById(R.id.tv_to_username)).setText(item.receiver_name);
					((TextView)vh.getConvertView().findViewById(R.id.tv_buy_money)).setText(item.inventory_score+"");
					((TextView)vh.getConvertView().findViewById(R.id.tv_rmark)).setText(item.beizhu);
				}
			};
				cashierHistory.setAdapter(commonAdapter);
	}




}
