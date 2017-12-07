package com.cloud.tao.util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.cloud.tao.framwork.base.BaseWebViewActivity;

/**
 * author:janecer on 2016/8/28 11:11
 */


public class ActionJumpUtil {

    /**
     * 跳转到webview
     * @param activity
     * @param url
     */
    public static void jumpToCommentWebView(Activity activity, String url) {
        Intent intent = new Intent(activity, BaseWebViewActivity.class) ;
        intent.putExtra(BaseWebViewActivity.EXTRA_URL,url) ;
        activity.startActivity(intent);
    }

    public static boolean jumpToCheckLoginState(Activity activity,int requestCode) {
        return jumpToCheckLoginState(activity,null,requestCode) ;
    }

    /**
     * 检查登陆状态 根据状态是否跳转到登陆页面
     * @param activity
     * @param fragment
     * @param requestCode
     * @return
     */
    public static boolean jumpToCheckLoginState(Activity activity, Fragment fragment , int requestCode) {
       /* if(AccountInfo.getInstance().isUserLogin() ){
            return true ;
        }
        if(fragment != null) {
            fragment.startActivityForResult(new Intent(activity, LoginActivity.class),requestCode);
        } else {
            activity.startActivityForResult(new Intent(activity,LoginActivity.class),requestCode);
        }*/
        return false ;
    }

    /**
     * 裁剪选择的相片
     * @param activity
     * @param requetCode
     * @param selectPicUri
     * @param imageCropUri
     */
    public static void jumpToCropSelecPic(Activity activity,int requetCode,Uri selectPicUri,Uri imageCropUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(selectPicUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 700);
        intent.putExtra("outputY", 700);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, requetCode);
    }

    /**
     * 打开相机拍照
     */
    public static void JumpToTakeCameraOnly(Activity activity,int requestCode,Uri imageUri) {
        Intent intent = null;
        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent,requestCode);
    }

    /**
     * 进入
     * @param activity
     * @param fragment
     * @param REQUEST_LOGIN
     * @param gameId
     */
    public static void jumpToPlayGame(Activity activity,Fragment fragment,int REQUEST_LOGIN,String gameId) {
        if(!ActionJumpUtil.jumpToCheckLoginState(activity,fragment,REQUEST_LOGIN)){
            return;
        }
//        String url = MallApplication.getInstance().getModel(AppModel.class).getGameUrl(gameId,AccountInfo.getInstance().getLoginInfo().token) ;
//        Intent intent = new Intent(activity, GameWebViewActivity.class) ;
//        intent.putExtra(BaseWebViewActivity.EXTRA_URL,url) ;
//        activity.startActivity(intent);
    }
}
