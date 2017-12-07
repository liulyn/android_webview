package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.MainActiveInfo;
import com.cloud.tao.bean.etc.VipActiveInfo;
import com.cloud.tao.callback.NoDoubleClickListener;

import java.util.List;

public class VipActiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<VipActiveInfo> mData;
    private ActiveMainListener mActiveMainListener;

    public VipActiveAdapter(Context context, List<VipActiveInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public interface ActiveMainListener{
        void onActiveItemClick(String activeType);
    }

    public void setActiveMainListener(ActiveMainListener activeMainListener){
        this.mActiveMainListener=activeMainListener;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etc_active_main, parent, false);
            viewHolder = new ViewHolder((ImageView) convertView.findViewById(R.id.iv_active_main_ico)
                    , (TextView) convertView.findViewById(R.id.iv_active_main_name));
           /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , parent.getWidth() / 4);
            convertView.setLayoutParams(new GridView.LayoutParams(params));*/
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new NoDoubleClickListener(){
                @Override
                public void noDoubleClick(View v) {
                    mActiveMainListener.onActiveItemClick(mData.get(position).getActiveType());
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.activeMainImgView.setImageResource(mData.get(position).getActiveIconId());
        viewHolder.activeMainTvView.setText(mData.get(position).getActiveTitle());
        return convertView;
    }

    public class ViewHolder {
        public ImageView activeMainImgView;
        public TextView activeMainTvView;

        public ViewHolder(ImageView imageView, TextView textView) {
            this.activeMainImgView = imageView;
            this.activeMainTvView = textView;
        }
    }
}
