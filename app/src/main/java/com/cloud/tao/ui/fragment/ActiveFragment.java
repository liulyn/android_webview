package com.cloud.tao.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cloud.tao.MallApplication;
import com.cloud.tao.bean.etc.ActiveSortInfo;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.appmodel.AppModel;
import com.cloud.tao.ui.adapter.etc.ActiveSortAdapter;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseShowFragment;
import com.cloud.tao.databinding.FragmentActiveBinding;
import com.cloud.tao.ui.fragment.active.ActiveCategoryFragment;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.ToastUtils;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

/**
 * sunny created at 2016/9/24
 * des: （云之道）分类主页
 */
public class ActiveFragment extends BaseShowFragment {

    private static final String TAG = "ActiveFragment";
    FragmentActiveBinding activeBinding ;
    private LinearLayoutManager mLayoutManager;
    private ActiveSortAdapter mAdapter;
    List<ActiveSortInfo> mActiveSortInfos;
    FragmentManager mFragmentManager;
    public int mPosition=0;
    int mLastPosition=0;


    @Override
    protected View initLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_active,container,false) ;

        getActiveGoods();
        mFragmentManager=getActivity().getSupportFragmentManager();
        return activeBinding.getRoot();
    }

    private void initCategoryFragment(int position){
        FragmentTransaction mTransaction=mFragmentManager.beginTransaction();
        Fragment targetFragment=mFragmentManager.findFragmentByTag(""+position);
        Fragment lastFragment=mFragmentManager.findFragmentByTag(""+mLastPosition);
        if(null==targetFragment){
            targetFragment= new ActiveCategoryFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("activeCategory", mActiveSortInfos.get(position).son);
            targetFragment.setArguments(bundle);
            if(null!=lastFragment){
                mTransaction.hide(lastFragment);
            }
            mTransaction.add(R.id.fragment_container,targetFragment,""+position).commit();
        }else{
            mTransaction.hide(lastFragment).show(targetFragment).commit();
        }
    }

    private void initParseData(){
        mLayoutManager = new LinearLayoutManager(getActivity());
        activeBinding.rvActiveSort.setLayoutManager(mLayoutManager);
        activeBinding.rvActiveSort.setHasFixedSize(true);
        mAdapter = new ActiveSortAdapter(getActivity(),mActiveSortInfos);
        activeBinding.rvActiveSort.setAdapter(mAdapter);

        initCategoryFragment(mPosition);
        mAdapter.setOnItemClickListener(new ActiveSortAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mLastPosition=mPosition;
                mPosition=position;
                mAdapter.setCurrentPosition(mPosition);
                initCategoryFragment(position);
            }
        });
    }

    private void getActiveGoods() {
        MallApplication.getInstance().getModel(AppModel.class).getActiveGoods(TAG,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code,String msg,String resp) {
                if(resp == null) return ;
                try {
                    JSONObject jsonObj=new JSONObject(resp);
                    mActiveSortInfos=GsonUtil.fromJsonList(jsonObj.optString("data"),ActiveSortInfo.class);
                    initParseData();
                } catch (JSONException e) {
                    ToastUtils.showToastShort(getActivity().getBaseContext(),"获取数据异常");
                }
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ToastUtils.showToastShort(getActivity().getBaseContext(),msg);
            }

            @Override
            public void onAfter(int id) {
            }
        });
    }

}
