package com.cloud.tao.bean.etc;

/**
 * sunny created at 2016/11/10
 * des: 活动倒计时
 */
public class CountDownInfo {
    public long day;
    public long hour;
    public long min;
    public long sec;
    public long getDay() {
        return day;
    }
    public long getHour() {
        return hour;
    }
    public long getMin() {
        return min;
    }
    public long getSec() {
        return sec;
    }
    public void setDay(long time) {
        long mDay=time/1000/60/60/24;
        this.day=mDay<=0?0:mDay;
    }
    public void setHour(long time) {
        long mHour=time/1000/60/60%24;
        this.hour=mHour<=0?0:mHour;
    }
    public void setMin(long time) {
        long mMin=time/1000/60%60;
        this.min=mMin<=0?0:mMin;
    }
    public void setSec(long time) {
        long mSec=time/1000%60 ;
        this.sec=mSec<=0?0:mSec;
    }
}
