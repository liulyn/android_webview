package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloud.tao.R;
import com.cloud.tao.bean.etc.MeActiveInfo;

import java.util.List;

/**
 * sunny created at 2016/9/26
 * des: （云之道）个人中心导航菜单的GridView的适配器
 */
public class MeActiveAdapter extends BaseAdapter {

    private Context mContext;
    private List<MeActiveInfo> mData;
    private ActiveMeListener mActiveMeListener;

    public MeActiveAdapter(Context context, List<MeActiveInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public interface ActiveMeListener{
        void onActiveItemClick(String activeType);
    }

    public void setActiveMainListener(ActiveMeListener activeMeListener){
        this.mActiveMeListener=activeMeListener;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etc_navigation_me, parent, false);
            viewHolder = new ViewHolder((ImageView) convertView.findViewById(R.id.iv_active_main_ico)
                    , (TextView) convertView.findViewById(R.id.iv_active_main_name),convertView.findViewById(R.id.ll_main));
           /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , parent.getWidth() / 4);
            convertView.setLayoutParams(new GridView.LayoutParams(params));*/
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActiveMeListener.onActiveItemClick(mData.get(position).getActiveType());
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.activeMeImgView.setImageResource(mData.get(position).getActiveIconId());
        viewHolder.activeMeTvView.setText(mData.get(position).getActiveTitle());
        //viewHolder.activeMeLl.setBackgroundColor(mData.get(position).getBgColor());
        return convertView;
    }

    public class ViewHolder {
        public ImageView activeMeImgView;
        public TextView activeMeTvView;
        public View activeMeLl;


        public ViewHolder(ImageView imageView, TextView textView,View view) {
            this.activeMeImgView = imageView;
            this.activeMeTvView = textView;
            this.activeMeLl = view;
        }
    }
}
