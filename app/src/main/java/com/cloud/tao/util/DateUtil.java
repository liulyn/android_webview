package com.cloud.tao.util;

import android.text.TextUtils;

import com.cloud.tao.bean.etc.CountDownInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类。
 *
 */
public class DateUtil {

	/** 常规日期格式，24小时制格式  **/
	public static final String NORMAL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 取得表示当天的时间对象
	 * 
	 * @return
	 */
	public static Date getCurrentDay() {
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTime();
	}

	public static long getDayOfHour(long time) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		ca.set(Calendar.MILLISECOND, 0);
		return ca.getTimeInMillis();
	}

	public static long addDayOfHour(long time, int hour) {
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		ca.add(Calendar.HOUR_OF_DAY, hour);
		return ca.getTimeInMillis();
	}

	/**
	 * 解析简单格式的日期yyyy-MM-dd HH:mm字符串
	 * @param simpleDateStr
	 * @return
	 */
	public static Date parseSimpleForMinute(String simpleDateStr) {
		if (TextUtils.isEmpty(simpleDateStr))
			return null;
		try {
			DateFormat simpleDateFormatForMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return simpleDateFormatForMinute.parse(simpleDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parseDate(String dateStr, String format) {
		if (TextUtils.isEmpty(dateStr))
			return null;
		try {
			DateFormat simpleDateFormatForMinute = new SimpleDateFormat(format);
			return simpleDateFormatForMinute.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd HH:mm"字符串
	 * @param time
	 * @return
	 */
	public static String getTimeStringForMinute(Long time) {
		DateFormat simpleDateFormatForMinute = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return simpleDateFormatForMinute.format(new Date(time));
	}

	/**
	 * 解析简单格式yyyy-MM-dd HH:mm:ss的日期字符串
	 * @param simpleDateStr
	 * @return
	 */
	public static Date parseSimple(String simpleDateStr) {
		DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (TextUtils.isEmpty(simpleDateStr))
			return null;
		try {
			return simpleDateFormat.parse(simpleDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd HH:mm:ss"字符串
	 * @param time
	 * @return
	 */
	public static String getTimeString(Long time) {
		final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return simpleDateFormat.format(new Date(time));
	}

	/**
	 *
	 * @param time
	 * @param format  根据具体格式 format
     * @return
     */
	public static String getTimeString(Long time,String format) {
		final DateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(new Date(time));
	}

    /**
     * 返回某个时间的"yyyy-MM-ddHH:mm:ss"字符串
     * @return
     */
    public static String getLogTimeString() {
        final DateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date(new Date().getTime() ));
    }

	/**
	 * 返回日期的微秒数
	 * @param date
	 * @return
	 */
	public static Long getTimeLong(Date date) {
		return date.getTime();
	}

	/**
	 * 获取当前的时间
	 *
	 * @return
	 */
	public static Date getCurrentTime() {
		return new Date();
	}

	/**
	 * 获取当前的时间
	 *
	 * @return
	 */
	public static String formatCurrentTime(String patten) {
		return formatDateToString(new Date(), patten);
	}

	/**
	 * 将时间按格式转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @param patten
	 * @return
	 */
	public static String formatDateToString(Date date, String patten) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(patten);
		return sd.format(date);
	}


	/**
	 * 将时间按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateNormal(Date date) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(NORMAL_DATE_FORMAT);
		return sd.format(date);
	}

	/**
	 * 将时间按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为字符串，日期为空时转换为空字符串
	 *
	 * @param date
	 * @return
	 */
	public static String formatDateNormal(Long date) {
		if (null == date)
			return "";
		SimpleDateFormat sd = new SimpleDateFormat(NORMAL_DATE_FORMAT);
		return sd.format(new Date(date));
	}

	/**
	 * 将格式long转换为字符串("yyyy-MM-dd")，日期为空时转换为空字符串
	 * @return
	 */
	public static String formatDateNormalStr(String dateStr,String patten) {
		if (TextUtils.isEmpty(dateStr))
			return "-";
		if ("0".equals(dateStr))
			return "-";
		Date mDate=new Date((Long.parseLong(dateStr)*1000)); //转换毫秒
		return formatDateToString(mDate,patten);
	}

	/**
	 * 功能描述：将字符串按格式转换为时间，字符串为空时转换为null
	 *
	 * @param dateStr 时间字符串
	 * @param patten 格式
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-5-2
	 */
	public static Date formatStrToDate(String dateStr, String patten) {
		if (null == dateStr || "".equals(dateStr))
			return null;
		SimpleDateFormat sd = new SimpleDateFormat(patten);
		try {
			Date date = sd.parse(dateStr);
			return date;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串按24小时制格式("yyyy-MM-dd HH:mm:ss")转换为时间
	 * @param dateStr 时间字符串
	 * @return 转换成的时间，字符串为空时转换为null
	 */
	public static Date formatStrToNormalDate(String dateStr) {
		return formatStrToDate(dateStr, NORMAL_DATE_FORMAT);
	}

	/**
	 * 获得按时间字符time(格式 "HH:mm:ss")转换的日期date
	 * @param date
	 * @param timeString 格式 "HH:mm:ss"
	 * @return 非法返回null
	 */
	public static Date getDateByTimeString(Date date, String timeString) {
		timeString = formatDateToString(date, String.format("yyyy-MM-dd %s", timeString));
		return formatStrToDate(timeString, NORMAL_DATE_FORMAT);
	}

	/**
	 * 功能描述：获取对应日期的开始时间
	 * @param date
	 * @return
	 */
	public static Date getDayStart(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 00:00:00");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 功能描述：获取对应日期的结束时间
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 23:59:59");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getDay(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd 00:00:00");
		return formatStrToDate(dateStartString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date getODay(Date date) {
		String dateStartString = formatDateToString(date, "yyyy-MM-dd");
		return formatStrToDate(dateStartString, "yyyy-MM-dd");
	}

	public static Date getODay(String dateStr) {
		return formatStrToDate(dateStr, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：获取昨天
	 *
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getYesterday() {
		return getDayBefore(1);
	}

	/**
	 * 功能描述：获取前day天的日期
	 *
	 * @param day
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getDayBefore(Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
		return calendar.getTime();
	}

	public static Date getDayBefore(Date date, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - day);
		return calendar.getTime();
	}

	public static Date getDayAfter(Date date, Integer day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + day);
		return calendar.getTime();
	}

	public static Date getMonthBefore(Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	public static Date getMonthBefore(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 跨年有问题??
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - month);
		calendar.add(Calendar.MONTH, -month);
		return calendar.getTime();
	}

	public static Date getMonthAfter(Date date, Integer month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// 跨年有问题??
		//calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
		calendar.add(Calendar.MONTH, month);
		return calendar.getTime();
	}

	/***
	 *
	 * 功能描述：获得当月，如2012-08
	 *
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-17
	 */
	public static String getMonth() {
		String dateStartString = formatDateToString(new Date(), "yyyy-MM");
		return dateStartString;
	}

	public static Date getLastMonth() {
		return getMonthBefore(1);
	}

	public static boolean isMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	public static boolean isMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	/**
	 * 功能描述：获取对应日期的月头
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 功能描述：获取对应日期的月尾
	 *
	 * @param date
	 * @return
	 * @version 1.0.0
	 * @since 1.0.0 create on: 2012-8-15
	 */
	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	public static int getMonthDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Calendar calInstance = Calendar.getInstance();
		calInstance.setTime(bigDate);
		int year1 = calInstance.get(Calendar.YEAR);
		int month1 = calInstance.get(Calendar.MONTH);
		calInstance.setTime(smallDate);
		int year2 = calInstance.get(Calendar.YEAR);
		int month2 = calInstance.get(Calendar.MONTH);
		return (year1 - year2) * 12 + month1 - month2;
	}

	public static int getDayDiff(Date bigDate, Date smallDate) {
		if (bigDate.compareTo(smallDate) <= 0) {
			return -1;
		}
		Long diff = (bigDate.getTime() - smallDate.getTime()) / 86400000;
		return diff.intValue();
	}

	/**
	 * 返回某个时间的"yyyy-MM-dd 00:00:00"字符串
	 * @param time
	 * @return
	 */
	public static String getTimeZeroString(Long time) {
		DateFormat simpleDateTimeZeroFormat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return simpleDateTimeZeroFormat.format(new Date(time));
	}

	public static int compareDate(Date date1, Date date2) {
		if (date1.getTime() > date2.getTime()) {
			return 1;
		} else if (date1.getTime() < date2.getTime()) {
			return -1;
		} else {
			return 0;
		}
	}

    /**
     * 根据时间戳判断是否是今天
     * @param timestamp
     * @return true表示当前时间戳时间今天，false表示不是今天
     */
    public static boolean isToday(long timestamp){
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();	//今天
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        long result=timestamp-today.getTimeInMillis();
        if(result>0&&result<24*60*60*1000){
            return true;
        }
        return false;
    }

    /**
     * 比较时间是否大于当前时间
     * @param compareStr
     * @return compareStr是否大于当前时间
     */
    public static boolean compareToCurrentTime(String compareStr){
        if(TextUtils.isEmpty(compareStr)){
            return true;
        }
        boolean result;
        Date compareDate=new Date((Long.parseLong(compareStr)*1000)); //转换毫秒
        Date currentTimeString=getCurrentTime();

        java.util.Calendar c1=java.util.Calendar.getInstance();
        java.util.Calendar c2=java.util.Calendar.getInstance();
        c1.setTime(compareDate);
        c2.setTime(currentTimeString);
        int compareResult=c1.compareTo(c2);
        if(compareResult<=0){
            result=false;
        }else{
            result=true;
        }
        return result;

    }

	public static CountDownInfo getCountDown(long time){
		long mTime=(time*1000);//转换毫秒
		CountDownInfo countDownInfo=new CountDownInfo();
		countDownInfo.setDay(mTime);
		countDownInfo.setHour(mTime);
		countDownInfo.setMin(mTime);
		countDownInfo.setSec(mTime);
		return countDownInfo;
	}

}
