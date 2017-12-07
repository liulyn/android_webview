package me.nereo.multi_image_selector.utils;

import android.content.Context;

public class DimensionUtil {
	/**
	 * 获取屏幕的宽度
	 * @param ctx
	 * @return
	 */
	public static int getWidth(Context ctx){
		   return ctx.getResources().getDisplayMetrics().widthPixels;
	}
	
	/**
	 * 获取屏幕的高度
	 * @param ctx
	 * @return
	 */
	public static int getHeight(Context ctx){
		 return ctx.getResources().getDisplayMetrics().heightPixels;
	}
	
	/**
	 * @param ctx
	 * @return
	 */
	public static float density(Context ctx){
		return ctx.getResources().getDisplayMetrics().density;
	}

	/**
	 * dip转换成px
	 * @param ctx
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context ctx, int dpValue) {
		float scale = ctx.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转换成dip
	 * @param ctx
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context ctx, int pxValue) {
		float scale = ctx.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
