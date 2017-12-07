package com.cloud.tao.ui.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cloud.tao.R;

/**
 * 自定义倒计时View
 */
public class CountDownView extends LinearLayout {
    private TextView tv_day;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_sec;
    private MyCount mc;
    public Boolean isStarted = false;

    /**
     * 定义一个倒计时的监听接口，来监听时间变化
     */
    public interface OnTimeChangeListener {
        String onDayChange(long day);

        String onHourChange(long hour);

        String onMinChange(long min);

        String onSecChange(long sec);
    }

    private OnTimeChangeListener onTimeChangeListener;

    /**
     * 设置实现了监听接口的类对象
     * @param onTimeChangeListener
     */
    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    /**
     * 下边为三个构造方法，主要用到了第二个，因为系统实例化时默认是调用第二个构造的
     */
    public CountDownView(Context context) {
        super(context);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_countdown_layout, this);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_hour = (TextView) findViewById(R.id.tv_hour);
        tv_min = (TextView) findViewById(R.id.tv_min);
        tv_sec = (TextView) findViewById(R.id.tv_sec);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 为view设置初始时间
     * @param millisInFuture
     * @param countDownInterval
     */
    public void setCountDown(long millisInFuture, long countDownInterval) {
        if (mc == null) {
            mc = new MyCount(millisInFuture, countDownInterval);
        }
    }

    /**
     * 为view设置初始时间，对时、分、秒分别设置，主要用这个来设置时间
     * @param hour
     * @param min
     * @param sec
     */
    public void setCountDown(long day, long hour, long min, long sec) {
        if (mc == null) {
            tv_day.setText(day + "");
            tv_hour.setText(hour + "");
            tv_min.setText(min + "");
            tv_sec.setText(sec + "");
            mc = new MyCount(day * 60 * 60 * 24 * 1000 + hour * 60 * 60 * 1000 + min * 60 * 1000 + sec * 1000, 1000);
        }
    }

    /**
     * 调用这个方法来开始倒计时
     */
    public void start() {
        if (!isStarted) {
            isStarted = true;
            mc.start();
        }
    }

    public void cancel() {
        if (isStarted) {
            mc.cancel();
            isStarted = false;
        }
    }

    /* 定义一个倒计时的内部类 */
    class MyCount extends CountDownTimer {
        private String sec = null;
        private String min = null;
        private String hour = null;
        private String day = null;

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            //倒计时结束
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (onTimeChangeListener != null && (sec = onTimeChangeListener.onSecChange(millisUntilFinished / 1000 % 60)) != null) {
                tv_sec.setText(sec);
            } else {
                tv_sec.setText(millisUntilFinished / 1000 % 60 + "");
            }
            //如果秒数=59则说明分钟数有变化，则对分进行设置
            if (millisUntilFinished / 1000 % 60 == 59) {
                if (onTimeChangeListener != null && (min = onTimeChangeListener.onMinChange(millisUntilFinished / 1000 % (60 * 60) / 60)) != null) {
                    tv_min.setText(min);
                } else {
                    tv_min.setText(millisUntilFinished / 1000 / 60 % 60 + "");
                }
            }
            //如果分数=59并且秒数=59则说明小时数有变化，对小时数进行设置
            if (millisUntilFinished / 1000 % (60 * 60) / 60 == 59 && millisUntilFinished / 1000 % 60 == 59) {
                if (onTimeChangeListener != null && (hour = onTimeChangeListener.onHourChange(millisUntilFinished / 1000 / (60 * 60))) != null) {
                    tv_hour.setText(hour);
                } else {
                    tv_hour.setText(millisUntilFinished / 1000 / 60 / 60 % 24 + "");
                }
            }
            //如果时=24则说明天数有变化，则对天进行设置
            if (millisUntilFinished / 1000 % (60 * 60 * 24) / 24 == 24 && millisUntilFinished / 1000 % (60 * 60) / 60 == 59) {
                if (onTimeChangeListener != null && (day = onTimeChangeListener.onDayChange(millisUntilFinished / 1000 / (60 * 60 * 24))) != null) {
                    tv_day.setText(day);
                } else {
                    tv_day.setText(millisUntilFinished / 1000 / 60 / 60 / 24 + "");
                }
            }
        }
    }
}