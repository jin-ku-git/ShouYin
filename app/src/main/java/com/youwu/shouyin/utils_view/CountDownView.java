package com.youwu.shouyin.utils_view;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.youwu.shouyin.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 倒计时控件
 */
public class CountDownView extends RelativeLayout {

    private static final String TAG = "CountDownView";
    private Context context;
    private TextView tv_hours;
    private TextView tv_colon1;
    private TextView tv_minutes;
    private TextView tv_colon2;
    private TextView tv_seconds;

    private long mDay;// 天
    private long mHour;//小时,
    private long mMin;//分钟,
    private long mSecond;//秒

    int type=1;//1倒计时 2计时

    private Timer mTimer;

    private Date endDate = null;
    private long endTime;

    private CountDownView.CountDownEndListener countDownEndListener;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.count_down, this, true);
        this.tv_hours = (TextView) rootView.findViewById(R.id.tv_hours);
        this.tv_colon1 = (TextView) rootView.findViewById(R.id.tv_colon1);
        this.tv_minutes = (TextView) rootView.findViewById(R.id.tv_minutes);
        this.tv_colon2 = (TextView) rootView.findViewById(R.id.tv_colon2);
        this.tv_seconds = (TextView) rootView.findViewById(R.id.tv_seconds);
        mTimer = new Timer();
    }

    /**
     * 设置时分秒背景图
     * @param backgroundRes
     */
    public CountDownView setTimeBackGroundResource(int backgroundRes){
        tv_hours.setBackgroundResource(backgroundRes);
        tv_minutes.setBackgroundResource(backgroundRes);
        tv_seconds.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * 时
     * @param hour
     */
    public CountDownView setTvHourText(String hour){
        tv_hours.setText(hour);
        return this;
    }

    /**
     * 分
     * @param minute
     */
    public CountDownView setTvMinuteText(String minute){
        tv_minutes.setText(minute);
        return this;
    }

    /**
     * 秒
     * @param second
     */
    public CountDownView setTvSecondText(String second){
        tv_seconds.setText(second);
        return this;
    }

    /**
     * 设置时分秒及：的字体大小
     * @param textSize
     */
    public CountDownView setTimeTextSize(float textSize){
        tv_hours.setTextSize(textSize);
        tv_minutes.setTextSize(textSize);
        tv_seconds.setTextSize(textSize);
        return this;
    }

    /**
     * 设置时分秒字体颜色
     * @param colorHex
     */
    public CountDownView setTimeTextColor(String colorHex){
        int color = Color.parseColor(colorHex);
        tv_hours.setTextColor(color);
        tv_minutes.setTextColor(color);
        tv_seconds.setTextColor(color);
        return this;
    }

    /**
     * 设置时间分隔符字体大小
     * @param textSize
     */
    public CountDownView setColonTextSize(float textSize){
        tv_colon1.setTextSize(textSize);
        tv_colon2.setTextSize(textSize);
        return this;
    }

    /**
     * 设置时间分隔符字体颜色
     * @param colorHex
     */
    public CountDownView setColonTextColor(String colorHex){
        int color = Color.parseColor(colorHex);
        tv_colon1.setTextColor(color);
        tv_colon2.setTextColor(color);
        return this;
    }

    private Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                computeTime();
                setTvHourText(getTv(mHour))
                        .setTvMinuteText(getTv(mMin))
                        .setTvSecondText(getTv(mSecond));
                if (mSecond == 0 &&  mDay == 0 && mHour == 0 && mMin == 0 ) {
                    mTimer.cancel();
                    /**
                     * 倒计时结束的回调
                     */
                    if (mtime_end != null) {
                        Log.i("倒计时结束的回调","");
                        mtime_end.onTimeEndt();
                    }
                }
            }else if (msg.what==2){
                computeTime_zheng();
                setTvHourText(getTv(mHour))
                        .setTvMinuteText(getTv(mMin))
                        .setTvSecondText(getTv(mSecond));


            }
        }
    };

    /**
     * Cancel the countdown.
     */
    public synchronized final void cancel() {
        mTimer.cancel();
    }

    private String getTv(long l){
        if(l>=10){
            return l+"";
        }else{
            return "0"+l;//小于10,,前面补位一个"0"
        }
    }

    /**
     * 开启倒计时
     *  //time为Date类型：在指定时间执行一次。
     *        timer.schedule(task, time);
     *  //firstTime为Date类型,period为long，表示从firstTime时刻开始，每隔period毫秒执行一次。
     *       timer.schedule(task, firstTime,period);
     *  //delay 为long类型：从现在起过delay毫秒执行一次。
     *       timer.schedule(task, delay);
     *  //delay为long,period为long：从现在起过delay毫秒以后，每隔period毫秒执行一次。
     *       timer.schedule(task, delay,period);
     */

    private MyTimerTask mTimerTask;
    public CountDownView startRun(int type) {

        this.type=type;
        if (mTimer != null){
            if (mTimerTask != null){
                mTimerTask.cancel();  //将原任务从队列中移除

            }
            mTimer.cancel();
            mTimer = new Timer();

            mTimerTask = new MyTimerTask();  // 新建一个任务
            mTimer.schedule(mTimerTask,0,1000);
        }

        return this;
    }

    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = type;
            timeHandler.sendMessage(message);
        }

    }

    /**
     * 倒计时计算
     */
    private void computeTime() {
        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                    if(mDay < 0){
                        // 倒计时结束
                        mDay = 0;
                        mHour= 0;
                        mMin = 0;
                        mSecond = 0;

                    }
                }
            }
        }
    }

    //倒计时结束的回调
    public interface OnTimeEnd {
        void onTimeEndt();
    }

    public void setOnTimeEnd(OnTimeEnd listener) {
        mtime_end = listener;
    }

    private OnTimeEnd mtime_end;


    /**
     * 正计时计算
     */
    private void computeTime_zheng() {

        mSecond++;
        if (mSecond > 59) {
            mMin++;
            mSecond = 0;
            if (mMin > 59) {
                mMin = 0;
                mHour++;
                if (mHour > 23) {

                    mHour = 0;
                    mDay++;

                }
            }
        }

    }

    /**
     * 初始化倒计时结束时间
     */
    public CountDownView initEndTime(String endTimeStr){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置要读取的时间字符串格式

        try {
            endDate = format.parse(endTimeStr);
            Log.i(TAG,"endDate="+endDate);
            endTime = endDate.getTime();
            Log.i(TAG,"endtime="+endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        String times = format.format(new Date(time * 1000L));
//	    System.out.println("日期格式---->" + times);
        return times;
    }

    /**
     * 计算距倒计时结束的剩余时间，转换成天数、小时、分钟、秒
     */
    public CountDownView calcTime(){

        //s1.获取当前系统时间
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();

        //s2.计算剩余时间
        long allTime = (endTime - nowTime) / 1000;
        //s3.剩余时间转换成天数、小时、分钟、秒
        mDay = allTime / 3600 / 24;
        mHour = allTime / 3600 % 24;
        mMin = allTime / 60 % 60;
        mSecond = allTime % 60;

        //Log.i(TAG,"mDay="+mDay + "mHour=" + mHour + "mMin=" + mMin + "mSecond=" + mSecond);
        return this;
    }

    /**
     * 计算距倒计时结束的剩余时间，转换成天数、小时、分钟、秒
     */
    public CountDownView calcTime_two(){
        //s1.获取当前系统时间
        Date nowDate = new Date();
        long nowTime = nowDate.getTime();

        String sss=stampToDate(nowTime/1000);
        System.out.println("系统当前时间：" + sss);
        System.out.println("endTime：" + endTime);
        //s2.计算剩余时间
        long allTime = (nowTime - endTime) / 1000;
        //s3.剩余时间转换成天数、小时、分钟、秒
        mDay = allTime / 3600 / 24;
        mHour = allTime / 3600 % 24;
        mMin = allTime / 60 % 60;
        mSecond = allTime % 60;

        //Log.i(TAG,"mDay="+mDay + "mHour=" + mHour + "mMin=" + mMin + "mSecond=" + mSecond);
        return this;
    }


    public interface CountDownEndListener {
        void onCountDownEnd();
    }

    public CountDownView setCountDownEndListener(CountDownEndListener countDownEndListener) {
        this.countDownEndListener = countDownEndListener;
        return this;
    }

    /**
     * 获取天数
     * @return
     */
    public long getmDay() {
        return mDay;
    }
}