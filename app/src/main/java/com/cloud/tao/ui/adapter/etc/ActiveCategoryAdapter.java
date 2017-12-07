package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.ActiveCategoryInfo;
import com.cloud.tao.util.glide.GlideUtil;
import java.util.ArrayList;

/**
 * sunny created at 2016/9/26
 * des: 二级菜单GridView的适配器
 */
public class ActiveCategoryAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ActiveCategoryInfo> mData;
    private ActiveCategoryListener activeCategoryListener;

    public ActiveCategoryAdapter(Context context, ArrayList<ActiveCategoryInfo> mData) {
        this.mContext = context;
        this.mData = mData;
    }

    public interface ActiveCategoryListener{
        void onActiveItemClick(String activeId);
    }

    public void setActiveCategoryListener(ActiveCategoryListener activeCategoryListener){
        this.activeCategoryListener=activeCategoryListener;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_active_category_item, parent, false);
            viewHolder = new ViewHolder((ImageView) convertView.findViewById(R.id.iv_active_category_ico)
                    , (TextView) convertView.findViewById(R.id.iv_active_category_name));
            convertView.setTag(viewHolder);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activeId=mData.get(position).category_id;
                    if(TextUtils.isEmpty(activeId)){
                        activeId=mData.get(position).parent_id;
                    }
                    activeCategoryListener.onActiveItemClick(activeId);
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GlideUtil.getInstance().loadImage(mContext,mData.get(position).category_pic_url, viewHolder.ivActiveCategory);
        viewHolder.tvActiveCategory.setText(mData.get(position).category_name);
        return convertView;
    }

    public class ViewHolder {
        public ImageView ivActiveCategory;
        public TextView tvActiveCategory;

        public ViewHolder(ImageView imageView, TextView textView) {
            this.ivActiveCategory = imageView;
            this.tvActiveCategory = textView;
        }
    }
}
