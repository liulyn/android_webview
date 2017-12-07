package com.cloud.tao.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cloud.tao.R;
import com.cloud.tao.framwork.Logger;

import me.nereo.multi_image_selector.utils.DimensionUtil;

/**
 * Created by janecer on 2016/3/17.
 * email:jxc@fancyf.cn
 * des:
 */
public class CommonUtils {

    /**
     * 校验手机号是否正确
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(17[0-9])|(14[6-7])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 验证密码
     */
    public static boolean checkPassword(String password){
        Pattern p = Pattern
                .compile("[0-9a-zA-Z_]{6,12}");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 手机号码中间截取替换
     */
    public static String subMobileReplace(String mobile){
        String mMobilePhone=mobile.substring(0,3) + "****" + mobile.substring(7,mobile.length());
        return mMobilePhone;
    }

    /**
     * 0-255
     * @param context
     * @param brightness
     */
    public static  void setWindowBrightness(Activity context, int brightness) {
        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    /**
     * 将手机号码 中间四位带星号
     * @param phone
     * @return
     */
    public static String convertPhoneEntry(String phone) {
        return phone.substring(0,3) +"****" + phone.substring(7,phone.length()) ;
    }

    /**
     * 设置dialog从window下面显示出来
     * @param parameDialog
     */
    public static void setDialogWindowAttr(Window parameDialog){
        parameDialog.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams lp = parameDialog.getAttributes();

        //lp.width= DimensionUtil.getWidth(context);

        parameDialog.setAttributes(lp);

        parameDialog.setWindowAnimations(R.style.dialogWindowAnim);
    }


    /**
     * 电话号码格式验证
     *
     * @param phone
     *            电话号码格式
     * @return 电话号码格式是否合法
     */
    public static boolean isPhoneNO(String phone) {
        Pattern p = Pattern.compile(
                "^((\\+?[0-9]{2,4}\\-[0-9]{3,4}\\-)|([0-9]{3,4}\\-))?([0-9]{7,8})(\\-[0-9]+)?$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }


    public static void initViewPage(final List<String> pics, final Context ctx, ViewPager viewPager, final LinearLayout ll, final View.OnClickListener onClickListener, int defaultPosition){

        final int size = pics.size() ;
        final List<ImageView> imageViews = new ArrayList<>() ;

        for(int i = 0 ; i < size ; i ++) {
            ImageView imageView = new ImageView(ctx) ;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT) ;
            imageView.setLayoutParams(lp);
            imageViews.add(imageView);

            if(size>1) {
                ImageView iv = new ImageView(ctx);
                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(DimensionUtil.dip2px(ctx, 8), DimensionUtil.dip2px(ctx, 8));
                if (defaultPosition % size == i) {
                    iv.setImageResource(R.drawable.checked_usb_open);
                } else {
                    llp.setMargins(DimensionUtil.dip2px(ctx, 6), 0, 0, 0);
                    iv.setImageResource(R.drawable.checked_usb_close);
                }
                iv.setLayoutParams(llp);
                ll.addView(iv);
            }
        }

        Logger.msg("initViewpager");

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return size;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }


            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                ImageView view = imageViews.get(position);
                (container).removeView(view);
                Logger.msg("destory ");
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ImageView view = imageViews.get(position) ;
                if(null != onClickListener) {
                    view.setOnClickListener(onClickListener);
                }
                try {
                    container.addView(view);
                } catch (Exception e) {}
                Glide.with(ctx).load(pics.get(position)).into(view) ;
                return  view;
            }

        };
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(defaultPosition);
    }

    public static boolean saveBitmapToSdcard(Context ctx,Bitmap bmp,String fileName){
        FileOutputStream fos = null;
        String path = null ;
        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath(),fileName);
            if(!file.getParentFile().exists()){
                file.mkdirs() ;
            }
            path = file.getAbsolutePath() ;
            fos = new FileOutputStream(file);
            Logger.msg("path : " + file.getPath());
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            MediaStore.Images.Media.insertImage(ctx.getContentResolver(),path, fileName, null);
            // 最后通知图库更新
            ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            return true ;
        } catch (FileNotFoundException e) {
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

    /**
     * 生成6位数字的字符串
     * @return
     */
    public static String getRandom(){
         return ((int)(Math.random() * 10000000)) +"" ;
    }

    /**
     * 实现文本复制功能
     * @param content
     */
    public static void copy(String content, Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setPrimaryClip(ClipData.newPlainText("",content));
    }

    /**
     * 实现粘贴功能
     * @param context
     * @return
     */
    public static String paste(Context context)
    {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(!cmb.hasPrimaryClip()){
            return null;
        }
        String resultString = "" ;
        int count = cmb.getPrimaryClip().getItemCount();
        for (int i = 0; i < count; ++i) {
            ClipData.Item item = cmb.getPrimaryClip().getItemAt(i);
            CharSequence str = item
                    .coerceToText(context);
            resultString += str;
        }
        return resultString ;
    }
}
