package com.cloud.tao.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * SharePreferences操作工具
 */
public class SharePrefUtil {
	private static String tag = SharePrefUtil.class.getSimpleName();
	private final static String SP_NAME = "config";
	private static SharedPreferences sp;

	public interface KEY {
        String function_is_first_in_app="function_is_first_in_app";//第一次进入app，登录后 提示进入激活璀璨卡界面
		String function_token = "function_token" ; //请求token
		String function_msg_code = "function_phone_code" ;
		String function_login_time="login_time";
		String function_login_token="login_token";
		String function_login_u_client_id="login_u_client_id";
		String function_session_id="session_id";
		String function_mobile_phone="function_mobilephone"; //本次登录的手机号
		String function_time = "function_time" ; //本次登录的时间
		String function_sessionId= "function_sessionId" ; //本次登录的seesionId
		String function_myStoreId= "function_myStoreId" ; //我的商店id
		String function_user_info= "function_user_info" ; //本次登录用户的信息
		String function_member_info= "function_member_info" ; //本次登录会员的信息
		String function_shopping_car_goods= "function_shopping_car_goods" ; //购物车的商品

    }

	/**
	 * 保存布尔
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME,0);
		}

		sp.edit().putBoolean(key, value).commit();
	}

	/**
	 * 保存字符
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().putString(key, value).commit();
	}
	
	public static void clear(Context context){
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		sp.edit().clear().commit();
	}

	/**
	 * 保存long
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveLong(Context context, String key, long value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		sp.edit().putLong(key, value).commit();
		
	}

	/**
	 * 保存int
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		sp.edit().putInt(key, value).commit();
	}

	/**
	 * 保存float
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveFloat(Context context, String key, float value) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		sp.edit().putFloat(key, value).commit();
	}

	/**
	 * 获取字符
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getString(key, defValue);
	}

	/**
	 * 获取int
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
	    }
		return sp.getInt(key, defValue);
	}

	/**
	 * 获取long
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getLong(key, defValue);
	}

	/**
	 * 获取float
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static float getFloat(Context context, String key, float defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getFloat(key, defValue);
	}

	/**
	 * 获取布尔
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		return sp.getBoolean(key, defValue);
	}


	/**
	 * 将对象保存到SharedPreferences中
	 * @param context
	 * @param object
	 * @param key
     * @return
     */
	public static boolean setObjectToShare(Context context,String key,Object object) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		if (object == null) {
			SharedPreferences.Editor editor = sp.edit().remove(key);
			return editor.commit();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		// 将对象放到OutputStream中
		// 将对象转换成byte数组，并将其进行base64编码
		String objectStr = new String(Base64.encode(baos.toByteArray(),
				Base64.DEFAULT));
		try {
			baos.close();
			oos.close();
		} catch (IOException e) {
		// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SharedPreferences.Editor editor = sp.edit();
		// 将编码后的字符串写到base64.xml文件中
		editor.putString(key, objectStr);
		return editor.commit();
	}


	/**
	 * 从SharedPreferences中取出对象
	 * @param context
	 * @param key
     * @return
     */
	public static Object getObjectFromShare(Context context, String key) {
		if (sp == null){
			sp = context.getSharedPreferences(SP_NAME, 0);
		}
		try {
			String wordBase64 = sp.getString(key, "");
			// 将base64格式字符串还原成byte数组
			if (wordBase64 == null || wordBase64.equals("")) { // 不可少，否则在下面会报java.io.StreamCorruptedException
				return null;
			}
			byte[] objBytes = Base64.decode(wordBase64.getBytes(),
					Base64.DEFAULT);
			ByteArrayInputStream bais = new ByteArrayInputStream(objBytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			// 将byte数组转换成product对象
			Object obj = ois.readObject();
			bais.close();
			ois.close();
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
