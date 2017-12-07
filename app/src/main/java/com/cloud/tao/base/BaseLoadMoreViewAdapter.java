package com.cloud.tao.base;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.cloud.tao.callback.OnHolderViewClickListener;
import com.cloud.tao.callback.OnHolderViewLongClickListener;
import com.cloud.tao.R;

/**
 * 基础的加载更多adapter
 */
public abstract class BaseLoadMoreViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private View footerView;
    public static final int HEAD_VIEW_TYPE = 1000;
    public static final int FOOTER_VIEW_TYPE = -2000;

    private boolean isHaveMoreData = true;// 是否有更多数据(默认为有);

    private boolean isShowFooterView = true ;
    private View lineView;
    private ProgressBar progressBar;
    private TextView tipContent;

    private boolean isLoading = false;// 是否正在加载

    private int onePageNum = 20;//一页显示的数量

    protected List<T> list;
    protected Context mContext;
    protected OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;
    private int mItemLayoutRes;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    private String tipByNotHaveMore = "只有这么多啦";
    private String tipByLoading = "正在加载...";
    private String tipByNotData = "暂无数据";
    private String tipByLoadFailure = "加载失败";

    public BaseLoadMoreViewAdapter(Context context, int itemLayout, List<T> list) {
        mContext = context;
        this.list = list;
        mItemLayoutRes = itemLayout;
        if (this.list == null) {
            this.list = new ArrayList<>();
        }
    }

    @Override
    public BaseRecyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderViews.get(viewType) != null)
        {
            return new BaseRecyViewHolder(parent.getContext(),mHeaderViews.get(viewType));
        }else if (isFooter(viewType)) {
            createFooterView(parent);

            return new BaseRecyViewHolder(parent.getContext(),footerView);
        } else {
            View view = LayoutInflater.from(mContext).inflate(mItemLayoutRes, parent, false);
            changeViewSize(view);
            BaseRecyViewHolder holder = new BaseRecyViewHolder(parent.getContext(),view);

            view.setOnClickListener(new OnHolderViewClickListener(holder) {
                @Override
                public void onClick(BaseRecyViewHolder holder, View v) {
                    final int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition < 0 || adapterPosition >= getListSize()) return;
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickDeal(holder, get(adapterPosition), adapterPosition);
                    }
                    onClickHook(holder, adapterPosition);
                }
            });

            view.setOnLongClickListener(new OnHolderViewLongClickListener(holder) {
                @Override
                public boolean onLongClick(BaseRecyViewHolder holder, View v) {
                    final int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition < 0 || adapterPosition >= getListSize()) return true;
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(holder, get(adapterPosition), adapterPosition);
                    }
                    return true;
                }
            });
            return holder;
        }
    }

    protected void changeViewSize(View rootView) {

    }

    protected void onClickHook(BaseRecyViewHolder holder, int adapterPosition) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderViewPos(position))
        {
            return;
        }
        if (isFooterViewPos(position))
        {
            return;
        }
        onBindViewHolder((BaseRecyViewHolder)holder, get(position - getHeadersCount()), (position - getHeadersCount()));
    }

    public abstract void onBindViewHolder(BaseRecyViewHolder holder, T data, int position);

    @Override
    public int getItemCount() {
        return isShowFooterView ? getListSize()+1+getHeadersCount():getListSize()+getHeadersCount();
    }

    private boolean isFooterViewPos(int position)
    {
        return position >= getHeadersCount() + getListSize();
    }

    @Override
    public int getItemViewType(int position) {
        /*if (isHeaderViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        }else if (position < getListSize()) {
            return super.getItemViewType(position);
        } else {
            return FOOTER_VIEW_TYPE;
        }*/

        if (isHeaderViewPos(position))
        {
            return mHeaderViews.keyAt(position);
        }else if (isFooterViewPos(position)){
            return FOOTER_VIEW_TYPE;
        }else{
            return super.getItemViewType(position - getHeadersCount());
        }
    }

    private void createFooterView(ViewGroup viewGroup) {
        footerView = LayoutInflater.from(mContext).inflate(R.layout.pull_to_load_footer, viewGroup, false);
        lineView = footerView.findViewById(R.id.line_layout);
        progressBar = (ProgressBar) footerView
                .findViewById(R.id.pull_to_load_footer_progressbar);
        tipContent = (TextView) footerView
                .findViewById(R.id.pull_to_load_footer_hint_textview);
    }


    public void setFooterVisibility(boolean shouldShow) {
        isShowFooterView = shouldShow ;
        if(null != footerView) {
            footerView.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        }
    }

    public View getFooter() {
        return footerView;
    }


    private boolean isFooter(int viewType) {
        return isShowFooterView && viewType == FOOTER_VIEW_TYPE;
    }


    /**
     * 加载成功
     *
     * @param count 新增加数据的数量
     */
    public void onLoadSuccess(int count) {
        isLoading = false;
        if (count < onePageNum) {
            this.isHaveMoreData = false;
            String msg = tipByNotHaveMore;
            if (getListSize() == 0) {
                msg = tipByNotData;
            }
            if (footerView != null) {
                tipContent.setText(msg);
                progressBar.setVisibility(View.GONE);
            }
        } else {
            this.isHaveMoreData = true;
            if (footerView != null) {
                tipContent.setText(tipByLoading);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 加载失败
     */
    public void onLoadFailure(String msg) {
        isLoading = false;
        this.isHaveMoreData = true;
        if (TextUtils.isEmpty(msg))
            msg = tipByLoadFailure;
        if (footerView != null) {
            progressBar.setVisibility(View.GONE);
            tipContent.setText(msg);
        }
    }

    /**
     * 是否有更多数据(默认为有)
     */
    public boolean isHaveMoreData() {
        return isHaveMoreData;
    }


    public void setIsLoading(boolean isLoading) {
        this.isLoading = isLoading;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setOnePageNum(int onePageNum) {
        this.onePageNum = onePageNum;
    }

    public String getTipByNotHaveMore() {
        return tipByNotHaveMore;
    }

    public void setTipByNotHaveMore(String tipByNotHaveMore) {
        this.tipByNotHaveMore = tipByNotHaveMore;
    }

    public String getTipByLoading() {
        return tipByLoading;
    }

    public void setTipByLoading(String tipByLoading) {
        this.tipByLoading = tipByLoading;
    }

    public String getTipByNotData() {
        return tipByNotData;
    }

    public void setTipByNotData(String tipByNotData) {
        this.tipByNotData = tipByNotData;
    }

    public String getTipByLoadFailure() {
        return tipByLoadFailure;
    }

    public void setTipByLoadFailure(String tipByLoadFailure) {
        this.tipByLoadFailure = tipByLoadFailure;
    }

    /**
     * 显示底部加载View和listView分割线
     */
    public void showLineView() {
        if (lineView != null)
            lineView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏底部加载View和listView分割线
     */
    public void hiddenLineView() {
        if (lineView != null)
            lineView.setVisibility(View.GONE);
    }

    public T get(int position) {
        if (position >= getListSize())
            return null;
        return list.get(position);
    }

    public List<T> getList() {
        return list;
    }

    public int getListSize() {
        return list.size();
    }

    public void add(T data) {
        int position = getListSize();
        list.add(data);
        if (position == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemInserted(getListSize() - 1);
        }
    }

    public void addAll(List<T> addDatas) {
        if (addDatas == null) {
            onLoadSuccess(0);
            return;
        }
        int position = getListSize()+getHeadersCount();
        list.addAll(addDatas);

        if (position == 0) {
            notifyDataSetChanged();
        } else {
            notifyItemRangeInserted(position, addDatas.size());
        }
        onLoadSuccess(addDatas.size());
    }

    public void addAll(int position, List<T> addList) {
        if (addList == null) {
            return;
        }
        list.addAll(position, addList);
        notifyDataSetChanged();
    }

    public void remove(T t) {
        int position = list.indexOf(t);
        if (position != -1) {
            list.remove(t);
            notifyItemRemoved(position);
        }
    }

    public void reset() {
        list.clear();
        notifyDataSetChanged();
        setFooterVisibility(true);
        if (footerView != null) {
            tipContent.setText(tipByLoading);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void replace(List<T> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<>();
        }
        list.clear();
        list.addAll(newDatas);
        notifyDataSetChanged();
        onLoadSuccess(newDatas.size());
    }

    /**
     *
     * @param newDatas
     * @param isLoadMore  true【将newDetas添加到集合尾部】,false【将newDatas替换集合中的数据】
     */
    public void addDatas(List<T> newDatas,boolean isLoadMore){
        if(isLoadMore){
            addAll(newDatas);
        } else {
            replace(newDatas);
        }
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 防止快速多次点击（如果不需要，请实现onItemClickDeal方法）
     */
    public static abstract class OnItemClickListener<T> {
        private long lastClickTime = 0;

        public void onItemClickDeal(BaseRecyViewHolder holder, T data, int position) {
            long time = System.currentTimeMillis();
            if (time - lastClickTime < 200)
                return;
            lastClickTime = time;
            onItemClick(holder, data, position);
        }

        public abstract void onItemClick(BaseRecyViewHolder holder, T data, int position);
    }

    public interface OnItemLongClickListener<T> {
        void onItemLongClick(BaseRecyViewHolder holder, T data, int position);
    }

    private boolean isHeaderViewPos(int position)
    {
        return position < getHeadersCount();
    }

    public void addHeaderView(View view)
    {
        mHeaderViews.put(HEAD_VIEW_TYPE, view);
    }

    public int getHeadersCount()
    {
        return mHeaderViews.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position)==HEAD_VIEW_TYPE||getItemViewType(position)==FOOTER_VIEW_TYPE){
                        return gridManager.getSpanCount();
                    }else{
                        return 1;
                    }
                }
            });
        }
    }

}
