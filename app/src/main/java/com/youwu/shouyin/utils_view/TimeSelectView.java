package com.youwu.shouyin.utils_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.TimeUtils;
import com.youwu.shouyin.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeSelectView extends FrameLayout {

    private TextView startTime;
    private TextView endTime;


    TimePickerView build;

    String startDate_tiem;
    String endDate_tiem;


    public TimeSelectView(@NonNull Context context) {
        this(context, null);
    }

    public TimeSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addView(LayoutInflater.from(getContext()).inflate(R.layout.time_choose, null));
        initView();
    }

    public void initView() {
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        startTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime((TextView) view,1);
            }
        });
        endTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTime((TextView) view,2);
            }
        });
//        ClickUtils.applyGlobalDebouncing(new View[]{startTime, endTime}, new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showTime((TextView) view);
//
//            }
//        });
        Calendar start = Calendar.getInstance();
        // 时
        start.set(Calendar.HOUR_OF_DAY, 4);
        // 分
        start.set(Calendar.MINUTE, 0);
        startDate_tiem=getTimes(start.getTime());
        startTime.setText(getTime(start.getTime()));
        Calendar end = Calendar.getInstance();
        // 时
        end.set(Calendar.HOUR_OF_DAY, 9);
        // 分
        end.set(Calendar.MINUTE, 0);
        endDate_tiem=getTimes(end.getTime());
        endTime.setText(getTime(end.getTime()));

    }

    public void showTime(final TextView textView, final int type) {
        Calendar selectedDate = Calendar.getInstance();
        if (type==1){
            // 时
            selectedDate.set(Calendar.HOUR_OF_DAY, 4);
            // 分
            selectedDate.set(Calendar.MINUTE, 0);
        }else {
            // 时
            selectedDate.set(Calendar.HOUR_OF_DAY, 9);
            // 分
            selectedDate.set(Calendar.MINUTE, 0);
        }


        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(endDate.YEAR, 10);
         build = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                textView.setText(getTime(date));
                if (type==1){
                    startDate_tiem=getTimes(date);
                }else {
                    endDate_tiem=getTimes(date);
                }

            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                build.returnData();
                                build.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                build.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})//分别对应年月日时分秒，默认全部显示
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(16)//标题文字大小
                 .setLineSpacingMultiplier(2.0f)//设置间距倍数
                 .setItemVisibleCount(7)//设置最大可见数目
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFfafafa)//滚轮背景颜色 Night mode
//                .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(true)//是否显示为对话框样式
                .build();
        build.show();

    }

    private String getTime(Date date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH时mm分");
        return simpleDateFormat.format(date);
    }

    private String getTimes(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public String getTimeRange() {
        Date date1 = TimeUtils.string2Date(startTime.getText().toString(), "yyyy年MM月dd");
        Date date2 = TimeUtils.string2Date(endTime.getText().toString(), "yyyy年MM月dd");
        String start = TimeUtils.date2String(date1, "yyyy/MM/dd HH:mm:ss");
        String end = TimeUtils.date2String(date2, "yyyy/MM/dd");
        return start + "~" + end + " 23:59:59";
    }

    public String getStart() {
        Date date1 = TimeUtils.string2Date(startTime.getText().toString(), "yyyy年MM月dd");
        return date1.getTime() / 1000 + "";
    }

    public String getEnd() {
        Date date2 = TimeUtils.string2Date(endTime.getText().toString(), "yyyy年MM月dd日");
        return date2.getTime() / 1000 + 86399 + "";
    }

    public String getStart_second() {

        return startDate_tiem;
    }

    public String getEnd_second() {

        return endDate_tiem;
    }


}
