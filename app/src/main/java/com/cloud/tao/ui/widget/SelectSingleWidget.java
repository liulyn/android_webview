package com.cloud.tao.ui.widget;

import android.app.Activity;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;


/**
 * Created by gezi-pc on 2016/10/16.
 */

public  class SelectSingleWidget<T> {

    private static final String TAG = "SelectSingleWidget";
    private Activity activity;
    private ArrayList<T> options1Items;
    OptionsPickerView pvOptions;
    View vMasker;
    private String title;
    private ArrayList<String> mDataItems;

    public interface OnSelectOptionSingleCallBack<T>{
        void OnSelectSingleCallBack(T result);
    }




    private OnSelectOptionSingleCallBack<T> mOnSelectOptionSingleCallBack;

    public SelectSingleWidget(Activity activity, ArrayList<T> resultData,ArrayList<String> showDatas,String title, View masker) {
        this.activity = activity;
        this.vMasker = masker;
        this.title = title;
        this.options1Items = resultData;
        this.mDataItems = showDatas;
        initWidget();
    }


    private void initWidget(){
        //选项选择器
        pvOptions = new OptionsPickerView(activity);
        //三级联动效果
        pvOptions.setPicker(mDataItems);
        pvOptions.setTitle(title);
        pvOptions.setCyclic(false, false, false);

        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                if(mOnSelectOptionSingleCallBack!=null){
                    mOnSelectOptionSingleCallBack.OnSelectSingleCallBack(options1Items.get(options1));
                }
                vMasker.setVisibility(View.GONE);
            }
        });


    }

    public void SetOnSelectOptionSingleCallBack(OnSelectOptionSingleCallBack<T> onSelectOptionSingleCallBack) {
        this.mOnSelectOptionSingleCallBack = onSelectOptionSingleCallBack;
    }

    public void showWidget(){
        pvOptions.show();
    }

    public boolean isShowing() {
        return pvOptions.isShowing();
    }

    public void dismiss() {
        pvOptions.dismiss();
    }



}


