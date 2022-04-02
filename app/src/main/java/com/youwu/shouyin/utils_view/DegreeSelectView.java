package com.youwu.shouyin.utils_view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.TimeUtils;
import com.youwu.shouyin.R;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DegreeSelectView extends FrameLayout {

    private TextView startTime;
    private TextView endTime;
    private OnChangeListener onChangeListener;

    TimePickerView build;

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    public DegreeSelectView(@NonNull Context context) {
        this(context, null);
    }

    public DegreeSelectView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DegreeSelectView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        endTime.setText("60");

        startTime.setText("40");
    }
    List<String> content = new ArrayList<>();

    int default_type;//默认数值
    public void showTime(final TextView textView, int type) {

        if (type==1){
            default_type=39;
        }else {
            default_type=59;
        }

        for (int i=1;i<100;i++){
            content.add(i+"");
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String strBankName = content.get(options1);
                textView.setText(strBankName);//将选中的数据返回设置在TextView 上。

            }
        })
                .setDividerColor(getResources().getColor(R.color.md_grey_400))
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)//设置文字大小
                .setOutSideCancelable(true)// default is true
                .isDialog(true)//是否显示为对话框样式
                .isRestoreItem(true)//保持上一个选项
                .setSelectOptions(default_type)
                .build();


        pvOptions.setPicker(content);//条件选择器

        pvOptions.show();
    }

    private String getTime(Date date) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH时mm分");
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
        return startTime.getText().toString();
    }

    public String getEnd() {

        return endTime.getText().toString();
    }


    public interface OnChangeListener {
        void onChange();

    }


}
