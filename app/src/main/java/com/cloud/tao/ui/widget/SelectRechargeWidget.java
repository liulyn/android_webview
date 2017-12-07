package com.cloud.tao.ui.widget;

import android.app.Activity;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.cloud.tao.bean.etc.RechargeCard;
import com.cloud.tao.bean.etc.RechargeCardOrder;

import java.util.ArrayList;


/**
 * Created by gezi-pc on 2016/10/26.
 */

public  class SelectRechargeWidget {

    private static final String TAG = SelectRechargeWidget.class.getSimpleName();
    private Activity activity;
    private ArrayList<String> options1Items = new ArrayList<String>();
    ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    OptionsPickerView pvOptions;
    View vMasker;
    private String title;
    private ArrayList<RechargeCardOrder> rechargeCardOrders;

    public interface OnSelectOptionSingleCallBack{
        void OnSelectSingleCallBack(RechargeCardOrder result);

    }




    private OnSelectOptionSingleCallBack mOnSelectOptionSingleCallBack;

    public SelectRechargeWidget(Activity activity, String title, ArrayList<RechargeCardOrder> label, View masker) {
        this.activity = activity;
        this.vMasker = masker;
        this.title = title;
        this.rechargeCardOrders = label;
        initWidget();
    }


    private void initWidget(){
        //选项选择器
        pvOptions = new OptionsPickerView(activity);

        //选项1
        for(int i=0;i<rechargeCardOrders.size();i++){
            RechargeCardOrder rco = rechargeCardOrders.get(i);
            options1Items.add(rco.label_name);
            //选项2
            ArrayList<String> options2Items_01=new ArrayList<>();
            for(RechargeCard card: rco.data){
                options2Items_01.add(card.label_num);
            }
            options2Items.add(options2Items_01);

        }


        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, true);



        pvOptions.setTitle(title);
        pvOptions.setCyclic(false, false, true);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {

                if(mOnSelectOptionSingleCallBack!=null){
                    RechargeCardOrder rco = new RechargeCardOrder();
                    RechargeCardOrder rcoItem = rechargeCardOrders.get(options1);
                    rco.label_id = rcoItem.label_id;
                    rco.label_name = rcoItem.label_name;
                    rco.data = new ArrayList<RechargeCard>();
                    rco.data.add(rcoItem.data.get(option2));
                    mOnSelectOptionSingleCallBack.OnSelectSingleCallBack(rco);
                }
                vMasker.setVisibility(View.GONE);
            }
        });


    }

    public void SetOnSelectOptionSingleCallBack(OnSelectOptionSingleCallBack onSelectOptionSingleCallBack) {
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


