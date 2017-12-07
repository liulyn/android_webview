package com.cloud.tao.util;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloud.tao.ui.widget.BadgeView;

import me.nereo.multi_image_selector.utils.DimensionUtil;

/**
 * view属性快速操作类
 * Created by luokj on 2015/11/24.
 */
public class ViewUtils {


    public static void dismissBageView(BadgeView bv) {
        if(bv != null && bv.isShown()){
            bv.hide();
        }
    }
    /**
     * 设置view高度
     *
     * @param view
     * @param height
     */
    public static void setHeight(View view, int height) {
        view.getLayoutParams().height = height;
        view.requestLayout();
    }

    /**
     * 设置view高度
     *
     * @param view
     * @param dp
     */
    public static void setDpHeight(View view, int dp) {
        if (dp == ViewGroup.LayoutParams.WRAP_CONTENT) {
            view.getLayoutParams().height = dp;
        } else {
            view.getLayoutParams().height = (int) DimensionUtil.dip2px(view.getContext(),dp);
        }
        view.requestLayout();
    }

    /**
     * 设置text
     *
     * @param view
     * @param content
     */
    public static void setText(TextView view, String content) {
        if (!TextUtils.isEmpty(content) && view != null)
            view.setText(content);
    }

    /**
     * 关闭 对话框
     *
     * @param activity
     * @param pd
     */
    public static void dismissDialog(Activity activity, Dialog pd) {
        if (pd == null)
            return;
        if (activity != null && activity.isFinishing())
            return;
        pd.dismiss();
    }

    /**
     * 设置控件的状态
     *
     * @param view       控件
     * @param isEnabled  是否开启
     * @param isSelected 是否选中
     */
    public static void setViewStatus(View view, boolean isEnabled, boolean isSelected) {
        view.setEnabled(isEnabled);
        view.setSelected(isSelected);
    }


    public static void setMarginTop(View mView, int marginTop) {
        if (mView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) mView.getLayoutParams()).topMargin = marginTop;
            mView.requestLayout();
        }
    }

    public static void setPaddingTop(View view, int paddingTop) {
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        int paddingBottom = view.getPaddingBottom();
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }
}
