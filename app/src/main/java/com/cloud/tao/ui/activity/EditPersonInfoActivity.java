package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.bigkoo.pickerview.TimePickerView;
import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.ClientInfo;
import com.cloud.tao.bean.etc.event.AccountInfoEven;
import com.cloud.tao.control.AccountInfo;
import com.cloud.tao.databinding.ActivityEditPersionInfoBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.net.model.membermodel.req.MemberReq;
import com.cloud.tao.ui.widget.SelectCityWidget;
import com.cloud.tao.ui.widget.bean.RegionInfo;
import com.cloud.tao.util.DateUtil;
import com.cloud.tao.util.GsonUtil;
import com.cloud.tao.util.SharePrefUtil;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


/**
 * 编辑个人信息
 *
 */
public class EditPersonInfoActivity extends BaseNavBackActivity implements View.OnClickListener{

    public static final String TAG = EditPersonInfoActivity.class.getSimpleName();

    ActivityEditPersionInfoBinding mActivityEditPersionInfoBinding;
    private ClientInfo.PersonInfo personInfo;


    MemberReq lastInfo;
    TimePickerView pvTime;
    private RegionInfo regionInfo;
    private SelectCityWidget selectCityWidget;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityEditPersionInfoBinding =  DataBindingUtil.setContentView(this,R.layout.activity_edit_persion_info);
        setNavDefaultBack(mActivityEditPersionInfoBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityEditPersionInfoBinding.loadding.showLoadSuccess();
        mActivityEditPersionInfoBinding.btPersonUpdate.setOnClickListener(this);
        mActivityEditPersionInfoBinding.rlBirthday.setOnClickListener(this);
        initDateWidget();
        initCityWidget();
        getClientInfo();
    }

    protected void initData() {
        lastInfo = (MemberReq) SharePrefUtil.getObjectFromShare(this,SharePrefUtil.KEY.function_member_info);
        mActivityEditPersionInfoBinding.etUsername.setText(personInfo.private_name);
        mActivityEditPersionInfoBinding.etPhoneNumber.setText(personInfo.private_mobilephone);
        if(personInfo.private_sex==1){
                mActivityEditPersionInfoBinding.rbMan.setChecked(true);
        }else if(personInfo.private_sex==2){
            mActivityEditPersionInfoBinding.rbWowan.setChecked(true);
        }
        /*else{
            mActivityEditPersionInfoBinding.rbMan.setChecked(false);
            mActivityEditPersionInfoBinding.rbWowan.setChecked(false);
        }*/
        if(!TextUtils.isEmpty(personInfo.private_province)&&!TextUtils.isEmpty(personInfo.private_city)){
            StringBuffer selectcity=new StringBuffer();
            if(!TextUtils.isEmpty(personInfo.private_province)){
                selectcity.append(personInfo.private_province);
            }
            if(!TextUtils.isEmpty(personInfo.private_city)){
                selectcity.append(personInfo.private_city);
            }
            if(!TextUtils.isEmpty(personInfo.private_district)){
                selectcity.append(personInfo.private_district);
            }
            mActivityEditPersionInfoBinding.tvSelectCity.setText(selectcity.toString());
            regionInfo = new RegionInfo();
            regionInfo.province = personInfo.private_province;
            regionInfo.city = personInfo.private_city;
            regionInfo.district = personInfo.private_district;
        }

        if(personInfo.private_birthday!=null){
            mActivityEditPersionInfoBinding.tvSelectBirthday.setText(DateUtil.formatDateToString(new Date(personInfo.private_birthday*1000),"yyyy-MM-dd"));
        }

        if(!TextUtils.isEmpty(personInfo.private_weixin)){
            mActivityEditPersionInfoBinding.etWeixin.setText(personInfo.private_weixin);
        }



    }

    private void getClientInfo(){

        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);
        MallApplication.getInstance().getModel(MemberModel.class).getClientInfo(TAG,new EntityCallBack<String>(new TypeToken<String>(){}){
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(EditPersonInfoActivity.this, progressDialog);
                Log.i(TAG,msg);
                Log.i(TAG,resp);
                JSONObject jsonObj= null;
                try {
                    jsonObj = new JSONObject(resp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ClientInfo clientInfo= GsonUtil.GsonToBean(jsonObj.optString("data"),ClientInfo.class);
                personInfo = clientInfo.client_info;
                initData();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(EditPersonInfoActivity.this, progressDialog);
                ToastUtils.showToastLong(EditPersonInfoActivity.this,msg);
                Log.i(TAG,msg);
            }
        });

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_person_update:
                submitMemberInfo();
                break;
        }

    }

    private void submitMemberInfo() {
        if(checkNotEmpty()) {
            final String name = mActivityEditPersionInfoBinding.etUsername.getText().toString();
            final String phoneNumber = mActivityEditPersionInfoBinding.etPhoneNumber.getText().toString();
            final short sex = (short)(mActivityEditPersionInfoBinding.rbMan.isChecked()==true?1:2);
            final Long birthday = DateUtil.getODay(mActivityEditPersionInfoBinding.tvSelectBirthday.getText().toString()).getTime()/1000;
            final String weixin = mActivityEditPersionInfoBinding.etWeixin.getText().toString();
            final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

            MallApplication.getInstance().getModel(MemberModel.class).editMember(TAG,name,phoneNumber,sex,regionInfo.province,regionInfo.city,regionInfo.district,birthday,weixin,new EntityCallBack<String>(new TypeToken<String>(){}){
                @Override
                public void onSuccess(int code, String msg, String resp) {
                    ViewUtils.dismissDialog(EditPersonInfoActivity.this, progressDialog);
                    Log.i(TAG,msg);
                    if(code==0){
                        AccountInfo.getInstance().saveMemberInfo(name,sex,phoneNumber,regionInfo.province,regionInfo.city,regionInfo.district,birthday,weixin,lastInfo.private_headimgurl);
                        EventBus.getDefault().post(new AccountInfoEven(name));
                        EditPersonInfoActivity.this.finish();
                    }
                }

                @Override
                public void onFail(int code, Exception e, String msg) {
                    ViewUtils.dismissDialog(EditPersonInfoActivity.this, progressDialog);
                    ToastUtils.showToastLong(EditPersonInfoActivity.this,msg);
                    Log.i(TAG,msg);
                }
            });

        }
    }

    private boolean checkNotEmpty(){
        if(TextUtils.isEmpty(mActivityEditPersionInfoBinding.etUsername.getText())){
            ToastUtils.showToastLong(this,"姓名不能为空！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityEditPersionInfoBinding.etPhoneNumber.getText())){
            ToastUtils.showToastLong(this,"手机号码不能为空！");
            return false;
        }

        if(mActivityEditPersionInfoBinding.rbMan.isChecked()==false&& mActivityEditPersionInfoBinding.rbWowan.isChecked()==false){
            ToastUtils.showToastLong(this,"请选择性别！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityEditPersionInfoBinding.tvSelectBirthday.getText())){
            ToastUtils.showToastLong(this,"生日不能为空！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityEditPersionInfoBinding.tvSelectCity.getText())||regionInfo==null){
            ToastUtils.showToastLong(this,"城市不能为空！");
            return false;
        }

        if(TextUtils.isEmpty(mActivityEditPersionInfoBinding.etWeixin.getText())){
            ToastUtils.showToastLong(this,"微信号不能为空！");
            return false;
        }

        return true;
    }


    /**
     * 初始化选择城市控件
     */
    private void initCityWidget(){


        selectCityWidget = new SelectCityWidget(this,mActivityEditPersionInfoBinding.vMasker);
        selectCityWidget.setOnSelectOptionsCallBack(new SelectCityWidget.OnSelectOptionsCallBack() {
            @Override
            public void OnSelectCityCallBack(RegionInfo info) {
                regionInfo = info;
                StringBuffer selectcity=new StringBuffer();
                if(!TextUtils.isEmpty(info.province)){
                    selectcity.append(info.province);
                    regionInfo.province = info.province;
                }else{
                    regionInfo.province="";
                }
                if(!TextUtils.isEmpty(info.city)){
                    selectcity.append(info.city);
                }else{
                    regionInfo.city="";
                }
                if(!TextUtils.isEmpty(info.district)){
                    selectcity.append(info.district);
                    regionInfo.district=info.district;
                }else{
                    regionInfo.district="";
                }
                mActivityEditPersionInfoBinding.tvSelectCity.setText(selectcity.toString());
            }
        });

        //点击弹出选项选择器
        mActivityEditPersionInfoBinding.rlCity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectCityWidget.showCityWidget();
            }
        });
    }


    /**
     * 初始化选择日期控件
     */
    private void initDateWidget(){
//时间选择器
        pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        //控制时间范围
        Calendar calendar = Calendar.getInstance();
        pvTime.setRange(calendar.get(Calendar.YEAR) - 100, calendar.get(Calendar.YEAR));//要在setTime 之前才有效果哦
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                mActivityEditPersionInfoBinding.tvSelectBirthday.setText(DateUtil.formatDateToString(date,"yyyy-MM-dd"));
            }
        });
        //弹出时间选择器
        mActivityEditPersionInfoBinding.rlBirthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });

    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(selectCityWidget.isShowing()||pvTime.isShowing()){
                selectCityWidget.dismiss();
                pvTime.dismiss();
                return true;
            }
            if(pvTime.isShowing()){
                pvTime.dismiss();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


}

