package com.cloud.tao.ui.fragment.active;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseShowFragment;
import com.cloud.tao.bean.etc.ActiveCategoryInfo;
import com.cloud.tao.databinding.FragmentActiveCategoryBinding;
import com.cloud.tao.ui.activity.GoodsListActivity;
import com.cloud.tao.ui.adapter.etc.ActiveCategoryAdapter;
import java.util.ArrayList;

/**
 * sunny created at 2016/9/26
 * des: 分类菜单二级菜单内容
 */
public class ActiveCategoryFragment extends BaseShowFragment{

    FragmentActiveCategoryBinding activeCategoryBinding;
    ArrayList<ActiveCategoryInfo> activeCategoryInfos;
    ActiveCategoryAdapter activeCategoryAdapter;

    @Override
    protected View initLayoutView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activeCategoryBinding=DataBindingUtil.inflate(inflater, R.layout.fragment_active_category,container,false) ;
        return activeCategoryBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activeCategoryInfos=getArguments().getParcelableArrayList("activeCategory");
        activeCategoryAdapter=new ActiveCategoryAdapter(getActivity(),activeCategoryInfos);
        activeCategoryAdapter.setActiveCategoryListener(new ActiveCategoryAdapter.ActiveCategoryListener() {
            @Override
            public void onActiveItemClick(String activeId) {
                Intent intent=new Intent(getActivity(),GoodsListActivity.class);
                intent.putExtra("activeId",activeId);
                startActivity(intent);
            }
        });
        activeCategoryBinding.gvActiveCategory.setAdapter(activeCategoryAdapter);
    }
}
