package com.cloud.tao.framwork.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import me.nereo.multi_image_selector.utils.DimensionUtil;


/**
 * Created by janecer on 2016/03/20.
 */
public class BaseDialog extends Dialog {

    public static int default_width = 160; //默认宽度
    public static int default_height = 120;//默认高度

    public BaseDialog(Context context, View layout, int style) {
        this(context, DimensionUtil.dip2px(context,default_width), DimensionUtil.dip2px(context,default_height), layout, style);
    }

    public BaseDialog(Context context, int width, int height, View layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height ;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

}
