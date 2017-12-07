package com.cloud.tao.ui.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cloud.tao.R;

import me.nereo.multi_image_selector.utils.DimensionUtil;

/**
 * Created by janecer on 2016/9/17 0017
 * des:
 */
// TODO: 2016/9/17 0017 将属性值 抽取到xml中设置
public class StepView extends FrameLayout {

    private LinearLayout contentView ;
    private int total = 7 ; //总步数
    private int completeStep = 0 ;//当前完成步数
    private int resDefaultId = R.drawable.default_icon , resCompleteId = R.mipmap.ic_sign_pointer ;

    public StepView(Context context) {
        super(context);
        this.initView(context);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        //contentView = LayoutInflater.from(context).inflate(R.layout.view_step,null)
        contentView = new LinearLayout(context) ;
        contentView.setOrientation(LinearLayout.HORIZONTAL);

        //initStepViews() ;// TODO: 2016/9/21 0021 自定义属性时，可在初始化此处之前,初始化相关属性

        this.addView(contentView);
    }

    private void initStepViews() {
        if(contentView.getChildCount() > 0) {
            contentView.removeAllViews();
        }
        ImageView ivStep ;
        View vLine ;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(DimensionUtil.dip2px(getContext(),10), DimensionUtil.dip2px(getContext(),10)) ;
        lp.gravity = Gravity.CENTER_VERTICAL ;
        LinearLayout.LayoutParams lpLine = new LinearLayout.LayoutParams(DimensionUtil.dip2px(getContext(),26), DimensionUtil.dip2px(getContext(),1)) ;
        lpLine.gravity = Gravity.CENTER_VERTICAL ;

        for(int i = 0 ; i < total ; i++) {
            ivStep = new ImageView(getContext()) ;
            ivStep.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            contentView.addView(ivStep,lp);

            if(i < total - 1) { //最后哪一个ImageView 后面不需要再拼接一根横线
                vLine = new View(getContext());
                vLine.setBackgroundColor(getResources().getColor(R.color.gray_line));
                contentView.addView(vLine, lpLine);
            }
        }
    }


    public void showDataView() {

        initStepViews();

        ImageView point = null ;
        for(int i = 0 ; i < total ; i++) {
            point = (ImageView) contentView.getChildAt(i * 2);
            point.setImageResource(completeStep - 1 >= i ? resCompleteId : resDefaultId);
        }
    }

    public StepView setDefaultIcon(@IdRes int resouseId) {
        this.resDefaultId = resouseId ;
        return this ;
    }

    public StepView setCompleteIcon(@IdRes int resouseId) {
        this.resCompleteId = resouseId ;
        return this ;
    }

    public StepView setComplete(int step) {
        this.completeStep = step ;
        return this ;
    }

    public StepView setTotal(int total) {
        this.total = total ;
        return this ;
    }
}
