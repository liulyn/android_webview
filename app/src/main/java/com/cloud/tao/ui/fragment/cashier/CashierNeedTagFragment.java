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
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.GiveInventoryScore;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.cashier.CashierModel;
import com.cloud.tao.ui.widget.LoadTipLayout;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我要收银
 */
public class CashierNeedTagFragment extends Fragment {


	private static final String TAG = CashierNeedTagFragment.class.getSimpleName();
	View rootview;
	private LoadTipLayout loadding;
	private GiveInventoryScore giveInventoryScore;
	private TextView tv_core;
	private EditText etBuyId;
	private EditText etToRmark;
	private Button btCashier;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_cashier_need, container,false);
		return rootview;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadding = (LoadTipLayout)rootview.findViewById(R.id.loadding);
		loadding.showLoadSuccess();
		initView();
		initListener();
		loadData();
	}

	private void initListener() {
		btCashier.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(TextUtils.isEmpty(etBuyId.getText())){
					ToastUtils.showToastShort(getActivity(),"对方ID号不能为空！");
					return;
				}
				int buyer_id = Integer.parseInt(etBuyId.getText().toString());
				String comment = etToRmark.getText().toString();
				MallApplication.getInstance().getModel(CashierModel.class).giveInventoryScoreConfirm(TAG, buyer_id, comment, new EntityCallBack<String>(new TypeToken<String>(){}) {
					@Override
					public void onSuccess(int code, String msg, String resp) {
						ToastUtils.showToastShort(getActivity(),msg);
						loadData();
						Log.e(TAG,msg);
					}

					@Override
					public void onFail(int code, Exception e, String msg) {
						ToastUtils.showToastShort(getActivity(),msg);
						Log.e(TAG,msg);
					}
				});

			}
		});
	}

	private void initView() {
		tv_core = (TextView)rootview.findViewById(R.id.tv_score);
		etToRmark=(EditText)rootview.findViewById(R.id.et_to_rmark);
		etBuyId=(EditText)rootview.findViewById(R.id.et_buy_id);
		btCashier=(Button)rootview.findViewById(R.id.bt_cashier);
	}

	private void loadData() {
		loadding.showLoadSuccess();
		final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

		MallApplication.getInstance().getModel(CashierModel.class).giveInventoryScore(TAG,new EntityCallBack<String>(new TypeToken<String>(){}) {
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
				giveInventoryScore = GsonUtil.GsonToBean(jsonObj.optString("data"), GiveInventoryScore.class);
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
		tv_core.setText("库存云豆："+giveInventoryScore.score_inventory);
	}

}
