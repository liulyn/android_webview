package com.cloud.tao.util.glide;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Priority;

import java.io.File;

/**
 * 加载图片的抽象类
 */
public interface IGlideLoader {


    /**
     * 加载普通网络图片
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadImage(Context context, String url, ImageView imageView);
    public void loadImage(Activity activity, String url, ImageView imageView);
    public void loadImage(Fragment fragment, String url, ImageView imageView);

    /**
     * 加载指定大小的图片
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public void loadImage(Context context, String url, ImageView imageView, int width, int height);
    public void loadImage(Activity activity, String url, ImageView imageView, int width, int height);
    public void loadImage(Fragment frament, String url, ImageView imageView, int width, int height);
    /**
     * 如果需要设置请求优先级使用这个，不设置默认是Priority.NORMAL
     *
     * @param context
     * @param url
     * @param imageView
     * @param priority
     */
    public void loadImage(Context context, String url, ImageView imageView, Priority priority);
    public void loadImage(Activity activity, String url, ImageView imageView, Priority priority);
    public void loadImage(Fragment fragment, String url, ImageView imageView, Priority priority);

    /**
     * 加载网络图片,圆
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadCircleImage(Context context, String url, ImageView imageView);
    public void loadCircleImage(Activity activity, String url, ImageView imageView);
    public void loadCircleImage(Fragment fragment, String url, ImageView imageView);
    /**
     * 加载网络图片,添加圆角
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadRoundImage(Context context, String url, ImageView imageView, int dp);
    public void loadRoundImage(Activity activity, String url, ImageView imageView, int dp);
    public void loadRoundImage(Fragment fragment, String url, ImageView imageView, int dp);
    /**
     * 加载图片时传入监听器
     *
     * @param context
     * @param url
     * @param loadingImageListener
     */
    public void loadImage(Context context, String url, ImageLoadingListener loadingImageListener);
    public void loadImage(Activity activity, String url, ImageLoadingListener loadingImageListener);
    public void loadImage(Fragment fragment, String url, ImageLoadingListener loadingImageListener);

    /**
     * 从资源文件中加载图片
     *
     * @param context
     * @param sourceId
     * @param imageView
     */
    public void loadImage(Context context, int sourceId, ImageView imageView);
    public void loadImage(Activity activity, int sourceId, ImageView imageView);
    public void loadImage(Fragment fragment, int sourceId, ImageView imageView);

    /**
     * 从文件中加载图片
     *
     * @param context
     * @param file
     * @param imageView
     */
    public void loadImage(Context context, File file, ImageView imageView);
    public void loadImage(Activity activity, File file, ImageView imageView);
    public void loadImage(Fragment fragment, File file, ImageView imageView);

    /**
     * 从Uri中加载图片
     *
     * @param context
     * @param uri
     * @param imageView
     */
    public void loadImage(Context context, Uri uri, ImageView imageView);
    public void loadImage(Activity activity, Uri uri, ImageView imageView);
    public void loadImage(Fragment fragment, Uri uri, ImageView imageView);
    /**
     * 从网络中加载Gif
     *
     * @param context
     * @param url
     * @param imageView
     */
    public void loadGif(Context context, String url, ImageView imageView);
    public void loadGif(Activity activity, String url, ImageView imageView);
    public void loadGif(Fragment fragment, String url, ImageView imageView);
    /**
     * 从资源文件中加载Gif
     *
     * @param context
     * @param sourceId
     * @param imageView
     */
    public void loadGif(Context context, int sourceId, ImageView imageView);
    public void loadGif(Activity activity, int sourceId, ImageView imageView);
    public void loadGif(Fragment fragment, int sourceId, ImageView imageView);

    /**
     * 从文件中加载Gif
     *
     * @param context
     * @param file
     * @param imageView
     */
    public void loadGif(Context context, File file, ImageView imageView);
    public void loadGif(Activity activity, File file, ImageView imageView);
    public void loadGif(Fragment fragment, File file, ImageView imageView);

    /**
     * 从Uri中加载Gif
     *
     * @param context
     * @param uri
     * @param imageView
     */
    public void loadGif(Context context, Uri uri, ImageView imageView);
    public void loadGif(Activity activity, Uri uri, ImageView imageView);
    public void loadGif(Fragment fragment, Uri uri, ImageView imageView);
}