package com.cloud.tao.ui.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.cloud.tao.base.BaseLoadMoreViewAdapter;


/**
 * 网格布局(两列)加载更多 配合HMSwipeRefreshView
 */
public class LoadMoreGridRecyclerView extends RecyclerView {

    private SwipeRefreshLayout mRefreshView;

    private OnLoadMoreListener mOnLoadMoreListener;


    public LoadMoreGridRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        android.support.v7.widget.GridLayoutManager manager = new android.support.v7.widget.GridLayoutManager(context,2);
        manager.setOrientation(android.support.v7.widget.GridLayoutManager.VERTICAL);
        setLayoutManager(manager);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mRefreshView != null) {
                    mRefreshView.setEnabled(((android.support.v7.widget.GridLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                android.support.v7.widget.GridLayoutManager manager = (android.support.v7.widget.GridLayoutManager) getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    Lmsg.e("onScroollStateChanged");
                    Adapter adapter = getAdapter();
                    BaseLoadMoreViewAdapter loadMoreViewAdapter = null;

                    if (adapter == null || !(adapter instanceof BaseLoadMoreViewAdapter))//不是加载更多adapter不处理
                        return;
                    else
                        loadMoreViewAdapter = (BaseLoadMoreViewAdapter) adapter;

                    int last = manager.findLastCompletelyVisibleItemPosition();
                    int total = manager.getItemCount();

                    if (last == total - 1 && (mRefreshView == null || !mRefreshView
                            .isRefreshing()) && !loadMoreViewAdapter.isLoading()
                            && mOnLoadMoreListener != null && loadMoreViewAdapter.isHaveMoreData()) {
                        loadMoreViewAdapter.setIsLoading(true);
//                        Lmsg.e("onLoadMore");
                        mOnLoadMoreListener.onLoadMore();
                    }
                }
            }
        });
    }

    public void setRefreshView(
            SwipeRefreshView mRefreshMoreView) {
        this.mRefreshView = mRefreshMoreView;
    }

    /**
     * 加载更多时间监听
     *
     * @author zeda
     */
    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

}
