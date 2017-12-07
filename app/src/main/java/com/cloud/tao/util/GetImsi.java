package com.cloud.tao.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;

public class GetImsi {
	/**
	 * 无卡或者获取失败返回000000
	 * @param context
	 * @return
	 */
	@SuppressLint("UseValueOf")
	public static String getImsiAll (Context context) {
		String imsi = "";
		try {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			//普通方法获取
			imsi = tm.getSubscriberId();
			if (!TextUtils.isEmpty(imsi)){
               return imsi;
            }
            imsi = tm.getSimOperator();
			if (!TextUtils.isEmpty(imsi)) {
			   return imsi;
            }
            Class<?>[] resources = new Class<?>[] {int.class};
            Integer resourcesId = new Integer(1);
            try {
                //反射展讯
                Method addMethod = tm.getClass().getDeclaredMethod("getSubscriberIdGemini", resources);
                addMethod.setAccessible(true);
                imsi = (String) addMethod.invoke(tm, resourcesId);
            } catch (Exception e) {
                imsi = null;
            }
			if (!TextUtils.isEmpty(imsi)) {
				return imsi;
			}
            try {
                //反射mtk
                Class<?> c = Class
                        .forName("com.android.internal.telephony.PhoneFactory");
                Method m = c.getMethod("getServiceName", String.class, int.class);
                String spreadTmService =(String) m.invoke(c, Context.TELEPHONY_SERVICE, 1) ;
                TelephonyManager tm1 = (TelephonyManager)context.getSystemService(spreadTmService);
                imsi = tm1.getSubscriberId();
            } catch (Exception e) {
                imsi = null;
            }
			if (!TextUtils.isEmpty(imsi)) {
				return imsi;
			}
            try {
                //反射高通    这个没测过
                Method addMethod2 = tm.getClass().getDeclaredMethod("getSimSerialNumber", resources);
                addMethod2.setAccessible(true);
                imsi = (String) addMethod2.invoke(tm, resourcesId);
            } catch (Exception e) {
                imsi = null;
            }
			if (!TextUtils.isEmpty(imsi)) {
				return imsi;
			}
            return "000000";
		} catch (Exception e) {
			return "000000";
		}
	}

    public static String getImsiNormal(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imsi=  tm.getSubscriberId();
        return null==imsi?"":imsi;
    }

    public static String getImei(Context context){
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }
}
