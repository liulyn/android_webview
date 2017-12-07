package com.cloud.tao.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cloud.tao.callback.IOnFragmentListener;
import com.cloud.tao.framwork.base.BaseFragment;


/**
 * Created by janecer on 2016/5/24 0024
 * des:提供与activity相互通信的接口onRefreshComplete，onFragmentListener
 */
public abstract class BaseShowFragment extends BaseFragment {

    protected IOnFragmentListener onFragmentListener ;

    protected abstract View initLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) ;

    /**
     * 所在的父activity与该fragment通信使用
     * @param msg
     */
    public void onRefreshComplete(Message msg) {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(mContentView == null) {
            mContentView = initLayoutView(inflater,container,savedInstanceState) ;
        } else {
            if(mContentView.getParent() != null) {
                ((ViewGroup)mContentView.getParent()).removeAllViews();
            }
        }
        return mContentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if(activity instanceof IOnFragmentListener){
            this.onFragmentListener = (IOnFragmentListener) activity;
        }
    }



}
