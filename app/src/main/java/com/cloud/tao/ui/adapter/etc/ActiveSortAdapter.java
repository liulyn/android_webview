package com.cloud.tao.ui.adapter.etc;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.ActiveSortInfo;
import java.util.List;

public class ActiveSortAdapter extends RecyclerView.Adapter<ActiveSortAdapter.ViewHolder> implements View.OnClickListener{

	private List<ActiveSortInfo> activeSortInfo= null;
	public int currentPosition;
	public int mPosition;
	private Context mContext;
	private OnRecyclerViewItemClickListener mOnItemClickListener = null;

	public ActiveSortAdapter(Context context,List<ActiveSortInfo> activeSortInfo) {
		this.mContext=context;
		this.activeSortInfo = activeSortInfo;
	}

	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.mOnItemClickListener = listener;
	}

	public void setCurrentPosition(int currentPosition){
		this.currentPosition=currentPosition;
		this.notifyDataSetChanged();
	}

	public interface OnRecyclerViewItemClickListener {
		void onItemClick(int position);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_etc_active_sort,viewGroup,false);
		ViewHolder viewHolder = new ViewHolder(view);
		viewHolder.mTextView.setOnClickListener(this);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		mPosition = position;
		viewHolder.mTextView.setText(activeSortInfo.get(position).category_name);
		viewHolder.mTextView.setTag(position);
		if (position == currentPosition) {
			viewHolder.mTextView.setBackgroundResource(R.drawable.etc_active_bg);
		} else {
			viewHolder.mTextView.setBackgroundColor(Color.parseColor("#f4f4f4"));
		}
	}

	@Override
	public int getItemCount() {
		return activeSortInfo==null?0:activeSortInfo.size();
	}

	@Override
	public void onClick(View v) {
		if (mOnItemClickListener != null) {
			mOnItemClickListener.onItemClick((int)v.getTag());
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView mTextView;
		public ViewHolder(View view){
			super(view);
			mTextView = (TextView) view.findViewById(R.id.tv_active_item);
		}
	}
}
