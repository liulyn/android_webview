package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.BankCard;

import java.util.List;


public class BankCardAdapter extends BaseAdapter {

	private List<BankCard> mDatas;
	private LayoutInflater mInflater;
	private Context mContext;



	public interface OnClickBankCardCallBack{
		void onClickAddToBankCard();
		void onClickDelBankCard(BankCard item);
	}

	private OnClickBankCardCallBack mOnClickBankCardCallBack;

	public void setOnClickBankCardCallBack(OnClickBankCardCallBack bankCardCallBack) {
		this.mOnClickBankCardCallBack = bankCardCallBack;
	}

	public BankCardAdapter(Context context, List<BankCard> datas) {
		this.mContext = context;
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mDatas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public int getItemViewType(int position) {
		if (mDatas.get(position).type == 0) {
			return 0;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		final BankCard bc = mDatas.get(arg0);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if(getItemViewType(arg0)==0){				
				convertView = mInflater.inflate(R.layout.activity_bank_card_item, arg2,false);
				holder.mBankname = (TextView) convertView.findViewById(R.id.tv_bank_name);
				holder.mBanknum = (TextView) convertView.findViewById(R.id.tv_bank_number);
				holder.mDel = (ImageView) convertView.findViewById(R.id.iv_del);
				holder.mItemview = convertView.findViewById(R.id.ll_item_view);
			}
			else if(getItemViewType(arg0)==1){
				convertView = mInflater.inflate(R.layout.activity_bank_card_add_item, arg2,false);
				holder.mItemview = convertView.findViewById(R.id.ll_item_view);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if(getItemViewType(arg0)==0){
			 	holder.mBankname.setText(bc.transfer_card_bank);
				holder.mBanknum.setText(bc.transfer_card_num.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*"));
				holder.mDel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(mOnClickBankCardCallBack!=null){
							mOnClickBankCardCallBack.onClickDelBankCard(bc);
						}
					}
				});
				if((arg0+1)%2==0){
					holder.mItemview.setBackgroundResource(R.drawable.shape_bank_card_bg2);
				}else{
					holder.mItemview.setBackgroundResource(R.drawable.shape_bank_card_bg1);
				}

		 }else{			 
			 holder.mItemview.setOnClickListener(new View.OnClickListener() {
				 @Override
				 public void onClick(View v) {
					 if(mOnClickBankCardCallBack!=null){
						 mOnClickBankCardCallBack.onClickAddToBankCard();
					 }
				 }
			 });
		 }
		
		return convertView;
	}

	final static class ViewHolder {
		TextView mBankname;
		TextView mBanknum;
		ImageView mDel;
		View mItemview;
	}

}
