package com.cloud.tao.ui.adapter.etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.MainGoodsModelInfo;
import com.cloud.tao.ui.widget.WrapContentListView;
import java.util.ArrayList;
import java.util.List;

/**
 * sunny created at 2016/9/30
 * des: 首页商品类别模块 填充
 */
public class MainGoodsModelAdapter extends BaseAdapter implements MainGoodsAdapter.OnGoodsItemClickListener {
    private List<MainGoodsModelInfo> mMinGoodsModelInfos;
    private List<MainGoodsAdapter> pAdapterList=new ArrayList<>();
    private Context mContext;

    private OnMainGoodsModelListener mOnMainGoodsModelListener;

    public interface OnMainGoodsModelListener{
        void onMainGoodsItemClick(String goodsId);
    }

    public void setOnMainGoodsModelListener(OnMainGoodsModelListener onMainGoodsModelListener){
        this.mOnMainGoodsModelListener=onMainGoodsModelListener;
    }

    public MainGoodsModelAdapter(Context context, List<MainGoodsModelInfo> mMinGoodsModelInfos) {
        this.mContext = context;
        this.mMinGoodsModelInfos = mMinGoodsModelInfos;

        for (int i = 0; i < mMinGoodsModelInfos.size();i++) {
            boolean isShowOrderCount=mMinGoodsModelInfos.get(i).is_show_ordered_count==0?false:true;
           MainGoodsAdapter pAdapter = new MainGoodsAdapter(context, mMinGoodsModelInfos.get(i).goods,isShowOrderCount);
           pAdapterList.add(pAdapter);
        }
    }

    @Override
    public int getCount() {
        return mMinGoodsModelInfos == null ? 0 : mMinGoodsModelInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mMinGoodsModelInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        MainGoodsModelInfo mMainGoodsModelInfo;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_etc_main_goods, null);
            viewHolder = new ViewHolder((TextView) convertView.findViewById(R.id.tv_main_goods_title)
                    , (WrapContentListView) convertView.findViewById(R.id.lv_main_goods));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mMainGoodsModelInfo = mMinGoodsModelInfos.get(position);
        viewHolder.tvTitle.setText(mMainGoodsModelInfo.title);
        MainGoodsAdapter mainGoodsAdapter=pAdapterList.get(position);
        mainGoodsAdapter.setmOnGoodsItemClickListener(MainGoodsModelAdapter.this);
        viewHolder.lvMainGoodsModel.setAdapter(mainGoodsAdapter);

        return convertView;
    }

    /**
     * 商品选项单击回调
     * @param goodsId
     */
    @Override
    public void onGoodsItemClick(String goodsId) {
        mOnMainGoodsModelListener.onMainGoodsItemClick(goodsId);
    }

    public class ViewHolder {
        TextView tvTitle;
        WrapContentListView lvMainGoodsModel;

        public ViewHolder(TextView tvTitle, WrapContentListView lvMainGoodsModel) {
            this.tvTitle = tvTitle;
            this.lvMainGoodsModel = lvMainGoodsModel;
        }
    }
}
