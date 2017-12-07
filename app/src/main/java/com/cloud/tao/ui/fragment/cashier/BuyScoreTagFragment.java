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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.BuyScoreInfo;
import com.cloud.tao.bean.etc.PayWay;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.cashier.CashierModel;
import com.cloud.tao.ui.widget.LoadTipLayout;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 购买云豆
 */
public class BuyScoreTagFragment extends Fragment {


	private static final String TAG = BuyScoreTagFragment.class.getSimpleName();
	View rootview;
	private SelectSingleWidget selectPayWayWidget;
	private PayWay selectPayWay;
	private LoadTipLayout loadding;
	private View rlPayWay;
	private TextView tvPayWay;
	private View vMasker;
	private BuyScoreInfo buyScoreInfo;
	private TextView tvScore;
	private TextView tvBalance;
	private EditText etToRmark;
	private EditText etBuyScore;
	private CheckBox cbIsDefault;
	private Button btBuyScore;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_buy_score, container,false);
		return rootview;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadding = (LoadTipLayout)rootview.findViewById(R.id.loadding);
		rlPayWay=rootview.findViewById(R.id.rl_pay_way);
		vMasker=rootview.findViewById(R.id.v_masker);
		tvPayWay=(TextView)rootview.findViewById(R.id.tv_pay_way);
		initView();
		initListener();
		loadData();
		initSelectPayWay();
	}

	private void initListener() {

		btBuyScore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(etBuyScore.getText())){
					ToastUtils.showToastShort(getActivity(),"购买的云豆不能为空！");
					return;
				}
				if(selectPayWay==null){
					ToastUtils.showToastShort(getActivity(),"请选择支付方式！");
					return;
				}


				int score = Integer.parseInt(etBuyScore.getText().toString());
				int score_buy_switch = cbIsDefault.isChecked()==true?1:0;
				double money = (score*1.0d)/(buyScoreInfo.inventory_score_add_ratio*1.0d);
				String rmark = etToRmark.getText().toString();
				loadding.showLoadding();
				MallApplication.getInstance().getModel(CashierModel.class).buyScoreConfirm(TAG, money, selectPayWay.id, score_buy_switch, rmark, new EntityCallBack<String>(new TypeToken<String>(){}) {
					@Override
					public void onSuccess(int code, String msg, String resp) {
						loadding.showLoadSuccess();

						ToastUtils.showToastShort(getActivity(),msg);
						loadData();
						Log.e(TAG,msg);
					}

					@Override
					public void onFail(int code, Exception e, String msg) {
						ToastUtils.showToastShort(getActivity(),msg);
						loadData();
						Log.e(TAG,msg);
					}
				});

			}
		});
	}

	private void initView() {
		tvScore = (TextView)rootview.findViewById(R.id.tv_score);
		tvBalance = (TextView)rootview.findViewById(R.id.tv_balance);
		etToRmark = (EditText)rootview.findViewById(R.id.et_to_rmark);
		etBuyScore = (EditText)rootview.findViewById(R.id.et_buy_score);
		cbIsDefault = (CheckBox)rootview.findViewById(R.id.cb_is_default);
		btBuyScore = (Button)rootview.findViewById(R.id.bt_buy_score);

	}

	private void loadData() {
		loadding.showLoadSuccess();
		final ProgressDialog progressDialog = VLDialog.showProgressDialog(getActivity(), getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

		//errorCode   1:商家尚未开启收银平台功能 2:用户还不是会员，需要先开通会员卡
		MallApplication.getInstance().getModel(CashierModel.class).buyScore(TAG,new EntityCallBack<String>(new TypeToken<String>(){}) {
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
				 buyScoreInfo = GsonUtil.GsonToBean(jsonObj.optString("data"), BuyScoreInfo.class);
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
		tvScore.setText("库存云豆："+buyScoreInfo.score_inventory);
		tvBalance.setText("￥"+buyScoreInfo.balance);
		etBuyScore.setHint("1元="+buyScoreInfo.inventory_score_add_ratio+"云豆  最低1元");
		cbIsDefault.setChecked(buyScoreInfo.score_buy_switch==1?true:false);

	}


	/**
	 * 初始化支付方式
	 */
	private void initSelectPayWay() {
		ArrayList<String> datas = new ArrayList<>();
		for(PayWay pay: PayWay.payways){
			datas.add(pay.name);
		}

		selectPayWayWidget = new SelectSingleWidget(getActivity(),PayWay.payways,datas,"选择支付方式",vMasker);
		selectPayWayWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<PayWay>() {
			@Override
			public void OnSelectSingleCallBack(PayWay result) {
				tvPayWay.setText(result.name);
				selectPayWay = result;
			}
		});

		//支付方式
		rlPayWay.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPayWayWidget.showWidget();
			}
		});
	}



}
