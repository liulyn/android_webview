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
import com.cloud.tao.bean.etc.BuyDetail;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.cashier.CashierModel;
import com.cloud.tao.ui.widget.BuyDetailDialog;
import com.cloud.tao.ui.widget.LoadTipLayout;
import com.cloud.tao.util.DateUtil;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.cloud.tao.util.adapter.CommonAdapter;
import com.cloud.tao.util.adapter.ViewHolder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * 购买明细
 */
public class BuyDetailTagFragment extends Fragment {


	private static final String TAG = BuyDetailTagFragment.class.getSimpleName();
	View rootview;
	ListView historyDetails;
	private CommonAdapter commonAdapter;
	private LoadTipLayout loadding;
	private int l = 1000;
	private int p = 1;
	private String start;
	private String end ;
	private BuyDetail buyDetail;
	private TextView tvReturnScore;
	private TextView tvAvailableScore;
	private TextView tvStockScore;
	private Button btSearch;
	private EditText etStartDate;
	private EditText etEndDate;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_buy_detail, container,false);
		return rootview;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		historyDetails = (ListView) rootview.findViewById(R.id.lv_buy_score_history);
		loadding = (LoadTipLayout)rootview.findViewById(R.id.loadding);
		initView();
		initListener();
		loadData();
	}

	private void initListener() {
		btSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				start = etStartDate.getText().toString();
				if(TextUtils.isEmpty(start)){
					start = null;
				}
				end = etEndDate.getText().toString();
				if(TextUtils.isEmpty(end)){
					end = null;
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
		etStartDate=(EditText)rootview.findViewById(R.id.et_start_date);
		etEndDate=(EditText)rootview.findViewById(R.id.et_end_date);
	}

	private void loadData() {

		loadding.showLoadSuccess();
		final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

		MallApplication.getInstance().getModel(CashierModel.class).inventoryList(TAG,l,p,start,end,new EntityCallBack<String>(new TypeToken<String>(){}) {
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
				buyDetail = GsonUtil.GsonToBean(jsonObj.optString("data"), BuyDetail.class);
				Log.e(TAG,buyDetail.toString());
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
			tvStockScore.setText("库存云豆："+buyDetail.score_inventory);
			tvAvailableScore.setText("可用云豆："+buyDetail.score_to_exchange);
			tvReturnScore.setText("待返云豆："+buyDetail.remain_score);

			commonAdapter = new CommonAdapter<BuyDetail.BuyDetailList>(getActivity(),buyDetail.inventory,R.layout.fragment_buy_detail_item) {
				@Override
				protected void convert(ViewHolder vh,final BuyDetail.BuyDetailList item) {
					((TextView)vh.getConvertView().findViewById(R.id.tv_date)).setText(DateUtil.formatDateToString(new Date(item.order_create_time),"yyyy-MM-dd"));
					((TextView)vh.getConvertView().findViewById(R.id.tv_username)).setText(item.buyer_login_name);
					((TextView)vh.getConvertView().findViewById(R.id.tv_state)).setText(item.State);
					((TextView)vh.getConvertView().findViewById(R.id.tv_buy_money)).setText("￥"+item.total_price);
					Button showDetail = (Button)vh.getConvertView().findViewById(R.id.bt_show_detail);
					showDetail.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							BuyDetailDialog dialog = new BuyDetailDialog(getActivity(),"明细详情",item);
							dialog.show();
						}
					});

				}
			};
		historyDetails.setAdapter(commonAdapter);
	}


}
