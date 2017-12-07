package com.cloud.tao.ui.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import com.cloud.tao.MallApplication;
import com.cloud.tao.R;
import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.bean.etc.CardType;
import com.cloud.tao.databinding.ActivityAddRechargeBinding;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.net.callBack.EntityCallBack;
import com.cloud.tao.net.model.membermodel.MemberModel;
import com.cloud.tao.ui.widget.SelectSingleWidget;
import com.cloud.tao.util.ToastUtils;
import com.cloud.tao.util.ViewUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by gezi-pc on 2016/9/30.
 */

public class AddRechargeActivity extends BaseNavBackActivity {


    private static final String TAG = "AddRechargeActivity";
    public  static final String RECHARGE_TYPE_LIST = "recharge_type_list";
    ActivityAddRechargeBinding mActivityAddRechargeBinding;
    private ArrayList<CardType> cardTypes;
    private SelectSingleWidget selectSingleWidget;
    private CardType selectCardType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivityAddRechargeBinding =  DataBindingUtil.setContentView(this, R.layout.activity_add_recharge);
        setNavDefaultBack(mActivityAddRechargeBinding.tb);
        super.onCreate(savedInstanceState);
        mActivityAddRechargeBinding.loadding.showLoadSuccess();
        mActivityAddRechargeBinding.btAddRechargeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRechargeCard();
            }
        });
        cardTypes = getIntent().getParcelableArrayListExtra(RECHARGE_TYPE_LIST);

        initSelectCardType();

    }

    private void initSelectCardType() {
        ArrayList<String> datas = new ArrayList<>();
        for(CardType type:cardTypes){
            datas.add(type.name);
        }

        selectSingleWidget = new SelectSingleWidget(this,cardTypes,datas,"选择充值卡类型",mActivityAddRechargeBinding.vMasker);
        selectSingleWidget.SetOnSelectOptionSingleCallBack(new SelectSingleWidget.OnSelectOptionSingleCallBack<CardType>() {
            @Override
            public void OnSelectSingleCallBack(CardType result) {
                mActivityAddRechargeBinding.tvCardName.setText(result.name);
                selectCardType = result;
            }

        });

        //点击弹出选项选择器
        mActivityAddRechargeBinding.llCardType.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectSingleWidget.showWidget();
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(selectSingleWidget.isShowing()){
                selectSingleWidget.dismiss();
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    private void addRechargeCard(){
        String cardNumber  = mActivityAddRechargeBinding.etInputCard.getText().toString();
        if(selectCardType==null){
            ToastUtils.showToastShort(AddRechargeActivity.this,"请选择充值卡类型！");
            return;
        }
        if(TextUtils.isEmpty(cardNumber)){
            ToastUtils.showToastShort(AddRechargeActivity.this,"卡号不能为空！");
            return;
        }




        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this, getString(R.string.dialog_common_title), getString(R.string.dialog_common_loading), true);

        MallApplication.getInstance().getModel(MemberModel.class).addRechargeCard(TAG, Integer.parseInt(selectCardType.label_id), cardNumber, new EntityCallBack<String>(new TypeToken<String>(){}) {
            @Override
            public void onSuccess(int code, String msg, String resp) {
                ViewUtils.dismissDialog(AddRechargeActivity.this, progressDialog);
                AddRechargeActivity.this.finish();
            }

            @Override
            public void onFail(int code, Exception e, String msg) {
                ViewUtils.dismissDialog(AddRechargeActivity.this, progressDialog);
                ToastUtils.showToastShort(AddRechargeActivity.this,msg);
            }
        });

    }





}

