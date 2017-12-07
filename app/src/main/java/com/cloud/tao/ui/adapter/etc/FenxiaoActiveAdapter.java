package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.FenxiaoActiveInfo;
import com.cloud.tao.callback.NoDoubleClickListener;

import java.util.List;

/**
 * sunny created at 2016/10/24
 * des: 创业中心导航菜单的GridView的适配器
 */
public class FenxiaoActiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<FenxiaoActiveInfo> mData;
    private ActiveFenxiaoListener mActiveFenxiaoListener;

    public FenxiaoActiveAdapter(Context context, List<FenxiaoActiveInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public interface ActiveFenxiaoListener{
        void onActiveItemClick(String activeType);
    }

    public void setActiveMainListener(ActiveFenxiaoListener activeFenxiaoListener){
        this.mActiveFenxiaoListener=activeFenxiaoListener;
    }

    @Override
    public int getCount() {
        return mData==null?0:mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        FenxiaoActiveAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etc_active_fenxiao, parent, false);
            viewHolder = new FenxiaoActiveAdapter.ViewHolder((ImageView) convertView.findViewById(R.id.iv_active_fenxiao_ico)
                    , (TextView) convertView.findViewById(R.id.iv_active_fenxiao_name));
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new NoDoubleClickListener(){
                        @Override
                        public void noDoubleClick(View v) {
                            mActiveFenxiaoListener.onActiveItemClick(mData.get(position).getActiveType());
                        }
                    }
            );
        } else {
            viewHolder = (FenxiaoActiveAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.activeFenxiaoImgView.setImageResource(mData.get(position).getActiveIconId());
        viewHolder.activeFenxiaoTvView.setText(mData.get(position).getActiveTitle());
        return convertView;
    }

    public class ViewHolder {
        public ImageView activeFenxiaoImgView;
        public TextView activeFenxiaoTvView;

        public ViewHolder(ImageView imageView, TextView textView) {
            this.activeFenxiaoImgView = imageView;
            this.activeFenxiaoTvView = textView;
        }
    }
}
