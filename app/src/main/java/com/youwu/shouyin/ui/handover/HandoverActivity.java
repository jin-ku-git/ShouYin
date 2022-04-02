package com.youwu.shouyin.ui.handover;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.app.UserUtils;
import com.youwu.shouyin.databinding.ActivityAddVipBinding;
import com.youwu.shouyin.databinding.ActivityHandoverBinding;
import com.youwu.shouyin.ui.money.RechargeRecordActivity;
import com.youwu.shouyin.ui.set_up.SetUpActivity;
import com.youwu.shouyin.ui.vip.AddVipViewModel;
import com.youwu.shouyin.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 交接班
 * 2022/03/23
 */
public class HandoverActivity extends BaseActivity<ActivityHandoverBinding, HandoverViewModel> {


    TextView start_time;//日志报表开始时间
    TextView end_time;//日志报表结束时间

    private int time_state;//1 开始 2 结束

    private TimePickerView pvCustomTime;//时间选择器

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_handover;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HandoverViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(HandoverViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://盘点现金
                        showInventoryDialog();
                        break;
                        case 2://销售商品列表
                            startActivity(SaleGoodsListActivity.class);
                        break;
                        case 3://日结
                            showJournalDialog();
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        //默认勾选
        binding.check.setChecked(true);
        viewModel.today_money.set("99.9");

        viewModel.logo_time.set(UserUtils.getLogoTime());
        viewModel.logo_name.set(UserUtils.getLogoName());

        initCustomTimePicker();


    }

    /**
     * 日结弹窗
     */
    private void showJournalDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_journal, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView handover_text = (TextView) dialogView.findViewById(R.id.handover_text);//交接班并退出
         start_time = (TextView) dialogView.findViewById(R.id.start_time);
         end_time = (TextView) dialogView.findViewById(R.id.end_time);
        SmoothCheckBox journal_check = (SmoothCheckBox) dialogView.findViewById(R.id.journal_check);
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String time= formater.format(new Date());

        start_time.setText(UserUtils.getLogoTime());
        end_time.setText(time);

        journal_check.setChecked(true);

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //交接班并退出
        handover_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //开始时间
        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_state=1;
                pvCustomTime.show(); //弹出自定义时间选择器
            }
        });
        //结束时间
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_state=2;
                pvCustomTime.show(); //弹出自定义时间选择器
            }
        });


    }


    /**
     * 盘点现金数量弹窗
     */
    private void showInventoryDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_inventory, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView handover_text = (TextView) dialogView.findViewById(R.id.handover_text);//交接班并退出
        final EditText value_100_num = (EditText) dialogView.findViewById(R.id.value_100_num);
        final EditText value_50_num = (EditText) dialogView.findViewById(R.id.value_50_num);
        final EditText value_20_num = (EditText) dialogView.findViewById(R.id.value_20_num);
        final EditText value_10_num = (EditText) dialogView.findViewById(R.id.value_10_num);
        final EditText value_5_num = (EditText) dialogView.findViewById(R.id.value_5_num);
        final EditText value_1_num = (EditText) dialogView.findViewById(R.id.value_1_num);
        final EditText value_0_5_num = (EditText) dialogView.findViewById(R.id.value_0_5_num);
        final EditText value_0_1_num = (EditText) dialogView.findViewById(R.id.value_0_1_num);

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //交接班并退出
        handover_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(value_100_num.getText().toString())){
                    RxToast.normal("面值100的不能为空");
                }else if("".equals(value_50_num.getText().toString())) {
                    RxToast.normal("面值50的不能为空");
                }else if("".equals(value_20_num.getText().toString())) {
                    RxToast.normal("面值20的不能为空");
                }else if("".equals(value_10_num.getText().toString())) {
                    RxToast.normal("面值10的不能为空");
                }else if("".equals(value_5_num.getText().toString())) {
                    RxToast.normal("面值5的不能为空");
                }else if("".equals(value_1_num.getText().toString())) {
                    RxToast.normal("面值1的不能为空");
                }else if("".equals(value_0_5_num.getText().toString())) {
                    RxToast.normal("面值0.5的不能为空");
                }else if("".equals(value_0_1_num.getText().toString())) {
                    RxToast.normal("面值0.1的不能为空");
                }else {
                    RxToast.normal("提交数据：面值100: "+value_100_num.getText().toString()+"张,面值50: "+value_50_num.getText().toString()+"张,\n"+
                            "面值20: "+value_20_num.getText().toString()+"张,面值10: "+value_10_num.getText().toString()+"张,\n"+
                            "面值5: "+value_5_num.getText().toString()+"张,面值1: "+value_1_num.getText().toString()+"张,\n"+
                            "面值0.5: "+value_0_5_num.getText().toString()+"张,面值0.1: "+value_0_1_num.getText().toString()+"张");
                    dialog.dismiss();
                }
            }
        });


    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        return format.format(date);
    }


    private void initCustomTimePicker() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2009, 0, 1);
        Calendar endDate = Calendar.getInstance();
//        endDate.set(2299, 11, 31);
        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
//                birthday.setText(getTime(date));
                if (time_state==1){
                    start_time.setText(getTime(date));
                }else {
                    end_time.setText(getTime(date));
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
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.dismiss();
                            }
                        });
                    }
                })
                .setType(new boolean[]{true, true, true, false, false, false})//分别对应年月日时分秒，默认全部显示
                .setContentTextSize(28)//滚轮文字大小
                .setTitleSize(26)//标题文字大小
                .setLineSpacingMultiplier(2.0f)//设置间距倍数
                .setItemVisibleCount(7)//设置最大可见数目
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFfafafa)//滚轮背景颜色 Night mode
//                .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isDialog(true)//是否显示为对话框样式
                .build();
    }
}
