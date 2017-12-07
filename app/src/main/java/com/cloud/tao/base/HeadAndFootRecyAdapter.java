package com.cloud.tao.base;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by janecer on 2016/8/9 0009
 * des:
 */
public class HeadAndFootRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int BASE_HEADER_VIEW_TYPE = 10000 ;
    private final static int BASE_FOOTER_VIEW_TYPE = 20000 ;


    private SparseArrayCompat<View> headViews = new SparseArrayCompat<>() ;
    private SparseArrayCompat<View> footViews = new SparseArrayCompat<>() ;


    private RecyclerView.Adapter innerAdapter ;

    public HeadAndFootRecyAdapter(RecyclerView.Adapter adapter) {
        this.innerAdapter = adapter ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headViews.get(viewType) != null) {
            return BaseRecyViewHolder.createViewHolder(parent.getContext(),headViews.get(viewType));
        } if(footViews.get(viewType) != null) {
            return BaseRecyViewHolder.createViewHolder(parent.getContext(),footViews.get(viewType));
        }
        return innerAdapter.onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isHeaderView(position)) {
            return ;
        } if(isFooterView(position)) {
            return ;
        }
        innerAdapter.onBindViewHolder(holder,position - headViews.size());
    }

    @Override
    public int getItemCount() {
        return headViews.size() + footViews.size() + getRealItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeaderView(position)) {
            return headViews.keyAt(position) ;
        } if(isFooterView(position)){
            return footViews.keyAt(position) ;
        }
        return innerAdapter.getItemViewType(position - headViews.size());
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            innerAdapter.onAttachedToRecyclerView(recyclerView);
            final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager)
            {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
                {
                    @Override
                    public int getSpanSize(int position)
                    {
                        //return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                        int viewType = getItemViewType(position);
                        if (headViews.get(viewType) != null)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        } else if (footViews.get(viewType) != null)
                        {
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        }
                        if (spanSizeLookup != null){
                            return spanSizeLookup.getSpanSize(position);
                        }
                        return 1;

                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        innerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position))
        {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
        super.onViewAttachedToWindow(holder);
    }

    public boolean isHeaderView(int position) {
        return position <= headViews.size() - 1 ;
    }

    public boolean isFooterView(int position) {
        return position >= headViews.size() + getRealItemCount();
    }

    public int getRealItemCount() {
        return innerAdapter.getItemCount() ;
    }

    public void addHeaderViews(View view) {
        headViews.put(BASE_HEADER_VIEW_TYPE + headViews.size(),view);
    }

    public void addFooterViews(View view) {
        footViews.put(BASE_FOOTER_VIEW_TYPE + footViews.size(),view);
    }
}
