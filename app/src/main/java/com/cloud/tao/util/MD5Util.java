package com.cloud.tao.util;

import java.security.MessageDigest;

 /**
 * sunny created at 2016/9/10/06
 * des: MD5工具类
 */
public class MD5Util{

    /**
     * 字符串MD5加密
     * @return 转后结果
     * @param str 明文
     * @param isToUpperCase 是否转大写
     */
    public static String getMD5(String str,boolean isToUpperCase) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                else
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        } catch (Exception e) {
            md5StrBuff.setLength(0);
            throw new RuntimeException();
        }
        return isToUpperCase?md5StrBuff.toString().toUpperCase():md5StrBuff.toString();
    }

}
