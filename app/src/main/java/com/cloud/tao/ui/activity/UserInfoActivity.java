package com.cloud.tao.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;

import com.cloud.tao.base.BaseNavBackActivity;
import com.cloud.tao.framwork.base.BaseDialog;
import com.cloud.tao.framwork.vl.VLDialog;
import com.cloud.tao.util.CommonUtils;
import com.cloud.tao.R;
import com.cloud.tao.databinding.ActivityUserInfoBinding;
import com.cloud.tao.databinding.DlgSelectHeaderBinding;
import com.cloud.tao.util.ActionJumpUtil;
import com.cloud.tao.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.utils.DimensionUtil;

public class UserInfoActivity extends BaseNavBackActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoActivity";

    private final static int REQUEST_IMAGE = BASE_CODE + 1 ;
    private final static int REQUEST_CODE_UPDATE_NICKNAME = REQUEST_IMAGE + 1;
    private final static int REQUEST_CODE_BIND_UNBIND_MOBILE = REQUEST_CODE_UPDATE_NICKNAME + 1;
    private final static int REQUEST_CODE_UPDATE_PWD = REQUEST_CODE_BIND_UNBIND_MOBILE + 1;


    private final static int REQUEST_CODE_CAMERA = REQUEST_CODE_UPDATE_PWD + 1;
    private final static int REQUEST_CAMERA_CROP = REQUEST_CODE_CAMERA + 1 ;


    ActivityUserInfoBinding mUserInfoBinding ;
    private BaseDialog picDialog ;
    private ArrayList<String> mSelectPath = new ArrayList<>() ;

    final String KEY_PIC = "key_pic",KEY_CROP = "key_pic" ;
    Uri imageUri,imageCropUri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUserInfoBinding = DataBindingUtil.setContentView(this,R.layout.activity_user_info);
        super.onCreate(savedInstanceState);
        setNavDefaultBack(mUserInfoBinding.tb);

        mUserInfoBinding.rlHeader.setOnClickListener(this);
        mUserInfoBinding.rlNickname.setOnClickListener(this);
        mUserInfoBinding.rlBind.setOnClickListener(this);
        mUserInfoBinding.rlUpdatePwd.setOnClickListener(this);
        mUserInfoBinding.rlClear.setOnClickListener(this);
        mUserInfoBinding.tvLoginOut.setOnClickListener(this);


        initCameraPic();
    }


    @Override
    protected void initDatas(Bundle savedInstanceState) {
        refreshViews();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_PIC,imageUri.getPath());
        outState.putString(KEY_CROP,imageCropUri.getPath());
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        File file = new File(savedInstanceState.getString(KEY_PIC));
        imageUri = Uri.fromFile(file);
        File cropFile = new File(savedInstanceState.getString(KEY_CROP));
        imageCropUri = Uri.fromFile(cropFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE:
                    mSelectPath = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                    ActionJumpUtil.jumpToCropSelecPic(this,REQUEST_CAMERA_CROP,Uri.fromFile(new File(mSelectPath.get(0))),imageCropUri);
                    break ;
                case REQUEST_CODE_UPDATE_NICKNAME:
                    setResult(Activity.RESULT_OK);
                    refreshViews();
                    break ;
                case REQUEST_CODE_BIND_UNBIND_MOBILE:
                    setResult(Activity.RESULT_OK);
                    refreshViews();
                    break ;
                case REQUEST_CODE_CAMERA :
                    ActionJumpUtil.jumpToCropSelecPic(this,REQUEST_CAMERA_CROP,imageUri,imageCropUri);
                    break ;
                case REQUEST_CAMERA_CROP:
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        uploadPic(imageCropUri.getPath());
                        return ;
                    }
                    ToastUtils.showToastShort(getApplicationContext(),getString(R.string.upload_ico_fial_tip));
                    break ;
            }
        } ;
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    protected void onBack() {
        super.onBack();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_header ://更换头像
                showPicDialog();
                break ;
            case R.id.rl_nickname ://修改昵称
                startActivityForResult(new Intent(this,UpdateNickNameActivity.class),REQUEST_CODE_UPDATE_NICKNAME);
                break ;
            case R.id.rl_bind://绑定手机号
                startActivityForResult(new Intent(this,BindUnBindPhoneActivity.class),REQUEST_CODE_BIND_UNBIND_MOBILE) ;
                break;
            case R.id.rl_clear ://清理缓存

                break ;
            case R.id.tv_login_out://退出登录
                //LoginProxy.getInstance().loginOut();
                setResult(Activity.RESULT_FIRST_USER);
                onBack();
                break;
            /*↓  选择头像 点击事件处理*/
            case R.id.tv_camera:
                picDialog.dismiss();
                //takeCameraOnly();
                ActionJumpUtil.JumpToTakeCameraOnly(this,REQUEST_CODE_CAMERA,imageUri);
                break ;
            case R.id.tv_pic:
                picDialog.dismiss();
                MultiImageSelector selector = MultiImageSelector.create(getApplicationContext());
                selector.showCamera(true);
                selector.single();
                selector.origin(mSelectPath);
                selector.start(this, REQUEST_IMAGE);
                break ;
            case R.id.tv_cancel:
                picDialog.dismiss();
                break ;
            /*↑  选择头像 点击事件处理*/
        }
    }

    public void uploadPic(String path) {
        final ProgressDialog progressDialog = VLDialog.showProgressDialog(this,getString(android.R.string.dialog_alert_title),getString(R.string.dialog_common_doing),false) ;
        /*MallApplication.getInstance().getModel(LoginModel.class).updateHeader(TAG, AccountInfo.getInstance().getLoginInfo().token, path,
                new EntityCallBack<String>(new TypeToken<String>(){}) {
                    @Override
                    public void onSuccess(int code, String msg, String resp) {
                        Logger.msg(TAG," imagUrl : " + resp);
                        ToastUtils.showToastShort(getApplicationContext(),msg);
                        //AccountInfo.getInstance().getLoginInfo().head_img = resp ;
                        setHeadIcon();
                        setResult(Activity.RESULT_OK);
                    }

                    @Override
                    public void onFail(int code, Exception e, String msg) {
                        ToastUtils.showToastShort(getApplicationContext(),msg);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Logger.msg(TAG,"progress : " + progress +"   total : " + total);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        ViewUtils.dismissDialog(UserInfoActivity.this,progressDialog);
                    }
                });*/
    }

    /**
     * 头像上传成功后 设置头像地址
     */
    private void setHeadIcon() {
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri));
//            mUserInfoBinding.ivHeader.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        /*if(TextUtils.isEmpty(AccountInfo.getInstance().getLoginInfo().head_img)) {
            return ;
        }*/
        //GlideUtil.getInstance().loadImage(this,AccountInfo.getInstance().getLoginInfo().head_img,mUserInfoBinding.ivHeader);
    }


    private void initCameraPic() {
        String path = Environment.getExternalStorageDirectory().getPath();
        File file = new File(path + "/temp.jpg");
        imageUri = Uri.fromFile(file);
        File cropFile = new File(path + "/temp_crop.jpg");
        imageCropUri = Uri.fromFile(cropFile);
    }


    private void refreshViews() {
       /* mUserInfoBinding.tvNickname.setText(AccountInfo.getInstance().getLoginInfo().nickname);
        setHeadIcon();
        if(!TextUtils.isEmpty(AccountInfo.getInstance().getLoginInfo().mobile)){
            mUserInfoBinding.tvPhone.setText(CommonUtils.convertPhoneEntry(AccountInfo.getInstance().getLoginInfo().mobile));
        } else {
            mUserInfoBinding.tvPhone.setText("");
        }*/
    }


    public void showPicDialog() {
        if(picDialog == null) {
            DlgSelectHeaderBinding binding = DataBindingUtil.inflate(getLayoutInflater(),R.layout.dlg_select_header,null,false) ;
            picDialog = new BaseDialog(this, (DimensionUtil.getWidth(getApplicationContext())/5)* 4, WindowManager.LayoutParams.WRAP_CONTENT,binding.getRoot(),R.style.customDialog) ;
            CommonUtils.setDialogWindowAttr(picDialog.getWindow());
            binding.tvCamera.setOnClickListener(this);
            binding.tvPic.setOnClickListener(this);
            binding.tvCancel.setOnClickListener(this);
        }
        picDialog.show();
    }
}
