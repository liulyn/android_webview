package com.cloud.tao.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * author:janecer on 2016/2/12 13:00
 * 旋转的ImageView，可用于正在加载提示
 */

public class LoaddingImageView extends ImageView {

    private RotateAnimation mRotateAnimation ;

    public LoaddingImageView(Context context) {
        this(context, null);
    }

    public LoaddingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRotateAnimation = new RotateAnimation(0,350 ,RotateAnimation.RELATIVE_TO_SELF,0.5f ,RotateAnimation.RELATIVE_TO_SELF ,0.5f) ;
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(-1) ;
        mRotateAnimation.setDuration(800);
        mRotateAnimation.setRepeatMode(RotateAnimation.RESTART) ;
    }

    @Override
    public void setVisibility(int visiable){
        super.setVisibility(visiable);
        if(visiable == VISIBLE){
            this.startAnimation(mRotateAnimation) ;
        } else {
            this.clearAnimation() ;
        }
    }
}
