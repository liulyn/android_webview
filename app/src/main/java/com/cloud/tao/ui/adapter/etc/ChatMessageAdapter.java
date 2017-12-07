package com.cloud.tao.ui.adapter.etc;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.ChatMessage;
import com.cloud.tao.util.glide.GlideUtil;


public class ChatMessageAdapter extends BaseAdapter {

	private List<ChatMessage> mDatas;
	private LayoutInflater mInflater;
	private Context mContext;

	private int mMinItemWidth;
	private int mMaxItemWidth;
	

	public ChatMessageAdapter(Context context, List<ChatMessage> datas) {
		this.mContext = context;
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
		WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics );
		mMaxItemWidth = (int) (outMetrics.widthPixels*0.7f);
		mMinItemWidth = (int) (outMetrics.widthPixels*0.15f);
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
		if (mDatas.get(position).getType() == ChatMessage.Type.INCOMING) {
			return 0;
		}
		if(mDatas.get(position).getType() == ChatMessage.Type.OUTVOICE){
			return 2;
		}
		return 1;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ChatMessage cm = mDatas.get(arg0);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			if(getItemViewType(arg0)==0){				
				convertView = mInflater.inflate(R.layout.item_form_msg, arg2,false);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_form_msg_date);
				holder.mMsg = (TextView) convertView.findViewById(R.id.id_from_msg_info);
				holder.mHead = (ImageView) convertView.findViewById(R.id.id_head);
			}
			else if(getItemViewType(arg0)==1){
				convertView = mInflater.inflate(R.layout.item_to_msg, arg2,false);
				holder.mDate = (TextView) convertView.findViewById(R.id.id_to_msg_date);
				holder.mMsg = (TextView) convertView.findViewById(R.id.id_to_msg_info);
				holder.mHead = (ImageView) convertView.findViewById(R.id.id_head);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		holder.mDate.setText(df.format(cm.getDate()));
		 if(getItemViewType(arg0)==2){			 
			 holder.mMsg.setText(Math.round(cm.getVoiceSeconds())+"\"");
				ViewGroup.LayoutParams lp = holder.length.getLayoutParams();
				lp.width = (int) (mMinItemWidth + (mMaxItemWidth/60*cm.getVoiceSeconds()));
		 }else{			 
			 holder.mMsg.setText(cm.getMsg());
			 GlideUtil.getInstance().loadImage(mContext, cm.getHeadImageUrl(), holder.mHead);
		 }
		
		return convertView;
	}

	final static class ViewHolder {
		TextView mDate;
		TextView mMsg;
		ImageView mHead;
		View length;
	}

}
