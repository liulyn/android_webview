package com.cloud.tao.util.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.File;

import com.cloud.tao.R;

/**
 * Created by janecer on 2016/6/15 0015
 * des:
 */
public class GlideUtil implements IGlideLoader {


    //设置加载时的图片
    private static final int PLACE_HOLDER_IMAGE = R.mipmap.dangkr_no_picture_small;
    //设置加载错误时的图片
    private static final int ERROR_IMAGE = R.mipmap.dangkr_no_picture_small;

    private static GlideUtil glideUtil;



    public GlideUtil(){

    }

    public static GlideUtil getInstance(){
        if (glideUtil ==null){
            synchronized (GlideUtil.class){
                if (glideUtil == null) {
                    glideUtil =new GlideUtil();
                }
            }
        }
        return glideUtil;
    }

    /**
     * 加载普通网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .override(width,height)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Activity activity, String url, ImageView imageView, int width, int height) {
        Glide.with(activity)
                .load(url)
                .override(width,height)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment frament, String url, ImageView imageView, int width, int height) {
        Glide.with(frament)
                .load(url)
                .override(width,height)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    /**
     * 如果需要设置请求优先级使用这个，不设置默认是Priority.NORMAL
     * @param context
     * @param url
     * @param imageView
     * @param priority
     */
    @Override
    public  void loadImage(Context context, String url, ImageView imageView,Priority priority) {
        Glide.with(context)
                .load(url)
                .priority(priority)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Activity activity, String url, ImageView imageView, Priority priority) {
        Glide.with(activity)
                .load(url)
                .priority(priority)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment fragment, String url, ImageView imageView, Priority priority) {
        Glide.with(fragment)
                .load(url)
                .priority(priority)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    /**
     * 加载网络图片,圆
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public  void loadCircleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(context))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadCircleImage(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(activity))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadCircleImage(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideCircleTransform(fragment.getContext()))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    /**
     * 加载网络图片,添加圆角
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public  void loadRoundImage(Context context, String url, ImageView imageView,int dp) {
        Glide.with(context)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(context,dp))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadRoundImage(Activity activity, String url, ImageView imageView, int dp) {
        Glide.with(activity)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(activity,dp))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }

    @Override
    public void loadRoundImage(Fragment fragment, String url, ImageView imageView, int dp) {
        Glide.with(fragment)
                .load(url)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(fragment.getContext(),dp))
                //设置填充满imageview，可能有部分被裁剪掉，还有一种方式是fitCenter，将图片完整显示
                .into(imageView);
    }


    /**
     * 监控加载过程
     * @param context
     * @param url
     * @param loadingImageListener
     */
    @Override
    public void loadImage(Context context, String url, ImageLoadingListener loadingImageListener) {
        MySimpleTarget mySimpleTarget=new MySimpleTarget(loadingImageListener);
        Glide.with(context)
                .load(url)
                .asBitmap()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mySimpleTarget);
    }

    @Override
    public void loadImage(Activity activity, String url, ImageLoadingListener loadingImageListener) {
        MySimpleTarget mySimpleTarget=new MySimpleTarget(loadingImageListener);
        Glide.with(activity)
                .load(url)
                .asBitmap()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mySimpleTarget);
    }

    @Override
    public void loadImage(Fragment fragment, String url, ImageLoadingListener loadingImageListener) {
        MySimpleTarget mySimpleTarget=new MySimpleTarget(loadingImageListener);
        Glide.with(fragment)
                .load(url)
                .asBitmap()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mySimpleTarget);
    }


    /**
     * 从资源文件中加载图片
     *
     * @param context
     * @param sourceId
     * @param imageView
     */
    @Override
    public  void loadImage(Context context, int sourceId, ImageView imageView) {
        Glide.with(context)
                .load(sourceId)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Activity activity, int sourceId, ImageView imageView) {
        Glide.with(activity)
                .load(sourceId)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment fragment, int sourceId, ImageView imageView) {
        Glide.with(fragment)
                .load(sourceId)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 从文件中加载图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    @Override
    public  void loadImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    @Override
    public void loadImage(Activity activity, File file, ImageView imageView) {
        Glide.with(activity)
                .load(file)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment fragment, File file, ImageView imageView) {
        Glide.with(fragment)
                .load(file)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    /**
     * 从Uri中加载图片
     *
     * @param context
     * @param uri
     * @param imageView
     */
    @Override
    public  void loadImage(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Activity activity, Uri uri, ImageView imageView) {
        Glide.with(activity)
                .load(uri)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public void loadImage(Fragment fragment, Uri uri, ImageView imageView) {
        Glide.with(fragment)
                .load(uri)
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    /**
     * 从网络中加载Gif
     *
     * @param context
     * @param url
     * @param imageView
     */
    @Override
    public  void loadGif(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Activity activity, String url, ImageView imageView) {
        Glide.with(activity)
                .load(url)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Fragment fragment, String url, ImageView imageView) {
        Glide.with(fragment)
                .load(url)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }


    /**
     * 从资源文件中加载Gif
     *
     * @param context
     * @param sourceId
     * @param imageView
     */
    @Override
    public  void loadGif(Context context, int sourceId, ImageView imageView) {
        Glide.with(context)
                .load(sourceId)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Activity activity, int sourceId, ImageView imageView) {
        Glide.with(activity)
                .load(sourceId)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Fragment fragment, int sourceId, ImageView imageView) {
        Glide.with(fragment)
                .load(sourceId)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 从文件中加载Gif
     *
     * @param context
     * @param file
     * @param imageView
     */
    @Override
    public  void loadGif(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);

    }

    @Override
    public void loadGif(Activity activity, File file, ImageView imageView) {
        Glide.with(activity)
                .load(file)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Fragment fragment, File file, ImageView imageView) {
        Glide.with(fragment)
                .load(file)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    /**
     * 从Uri中加载Gif
     *
     * @param context
     * @param uri
     * @param imageView
     */
    @Override
    public void loadGif(Context context, Uri uri, ImageView imageView) {
        Glide.with(context)
                .load(uri)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Activity activity, Uri uri, ImageView imageView) {
        Glide.with(activity)
                .load(uri)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    @Override
    public void loadGif(Fragment fragment, Uri uri, ImageView imageView) {
        Glide.with(fragment)
                .load(uri)
                .asGif()
                .placeholder(PLACE_HOLDER_IMAGE)
                .error(ERROR_IMAGE)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public class MySimpleTarget extends SimpleTarget<Bitmap> {

        private ImageLoadingListener imageListener;

        public MySimpleTarget(ImageLoadingListener imageListener) {
            this.imageListener = imageListener;
        }

        public MySimpleTarget(int width, int height, ImageLoadingListener imageListener) {
            super(width, height);
            this.imageListener = imageListener;
        }


        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            if (null!=imageListener){
                imageListener.onLoadSuccess(resource);
            }
        }

        @Override
        public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            imageListener.onLoadFailure();
        }

        @Override
        public void onLoadStarted(Drawable placeholder) {
            super.onLoadStarted(placeholder);
            imageListener.onLoadStart();
        }
    }
}
