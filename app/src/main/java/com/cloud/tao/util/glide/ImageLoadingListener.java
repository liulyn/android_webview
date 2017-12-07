package com.cloud.tao.util.glide;

import android.graphics.Bitmap;

/**
 * 监听图片加载过程
 */
public interface ImageLoadingListener {

    public void onLoadStart();

    public void onLoadFailure();

    public void onLoadSuccess(Bitmap bitmap);
}
