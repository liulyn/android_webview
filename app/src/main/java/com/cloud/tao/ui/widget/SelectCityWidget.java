package com.cloud.tao.ui.widget;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.cloud.tao.R;
import com.cloud.tao.bean.etc.Region;
import com.cloud.tao.ui.widget.bean.PickerViewData;
import com.cloud.tao.ui.widget.bean.ProvinceBean;
import com.cloud.tao.ui.widget.bean.RegionInfo;
import com.cloud.tao.util.GsonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by gezi-pc on 2016/10/15.
 */

public  class SelectCityWidget {

    private static final String TAG = "SelectCityWidget";
    private Activity activity;
    private static Region region;
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items = new ArrayList<>();
    OptionsPickerView pvOptions;
    View vMasker;

    public interface OnSelectOptionsCallBack{
        void OnSelectCityCallBack(RegionInfo info);

    }




    private OnSelectOptionsCallBack mOnSelectOptionsCallBack;

    public SelectCityWidget(Activity activity,View masker) {
        this.activity = activity;
        this.vMasker = masker;
        if(region==null) {
            region = GsonUtil.GsonToBean(readRawCityFile(), Region.class);
        }
        initCityWidget();

    }


    private void initCityWidget(){
        //选项选择器
        pvOptions = new OptionsPickerView(activity);
        //选项1
        for(int i=0;i<region.list.size();i++){
            Region.Province province = region.list.get(i);
            options1Items.add(new ProvinceBean(i,province.name,"",""));
            //选项2
            ArrayList<String> options2Items_01=new ArrayList<>();
            ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
            for(Region.Province.City city: province.city){
                options2Items_01.add(city.name);
                ArrayList<IPickerViewData> options3Items_01_01=new ArrayList<>();
                //选项3
                for(final String item :city.area){
                    options3Items_01_01.add(new PickerViewData(item));
                }
                options3Items_01.add(options3Items_01_01);
            }
            options3Items.add(options3Items_01);
            options2Items.add(options2Items_01);

        }


        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);


        //设置选择的三级单位
        //pvOptions.setLabels("选择省", "选择市", "选择区");
        pvOptions.setTitle("选择所在城市");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        //pvOptions.setSelectOptions(1, 1, 1);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                RegionInfo info = new RegionInfo();
                info.province = options1Items.get(options1).getPickerViewText();
                info.city = options2Items.get(options1).get(option2);
                info.district = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                if(info.province.equals(info.city)){
                    info.city = info.district;
                    info.district = null;
                }
                if(mOnSelectOptionsCallBack!=null){
                    mOnSelectOptionsCallBack.OnSelectCityCallBack(info);
                }
                vMasker.setVisibility(View.GONE);
            }
        });
    }

    public void setOnSelectOptionsCallBack(OnSelectOptionsCallBack onSelectOptionsCallBack) {
        this.mOnSelectOptionsCallBack = onSelectOptionsCallBack;
    }

    public void showCityWidget(){
        pvOptions.show();
    }

    public boolean isShowing() {
        return pvOptions.isShowing();
    }

    public void dismiss() {
        pvOptions.dismiss();
    }




    private String readRawCityFile()
    {
        String content = null;
        Resources resources=activity.getResources();
        InputStream is=null;
        try{
            is=resources.openRawResource(R.raw.city_json);
            byte buffer[]=new byte[is.available()];
            is.read(buffer);
            content=new String(buffer);
        }
        catch(IOException e)
        {
            Log.e(TAG, "write file",e);
        }
        finally
        {
            if(is!=null)
            {
                try{
                    is.close();
                }catch(IOException e)
                {
                    Log.e(TAG, "close file",e);
                }
            }
        }
        return content;
    }




}


