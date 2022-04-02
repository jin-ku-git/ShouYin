package com.youwu.shouyin.ui.money;

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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppApplication;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityRechargeRecordBinding;
import com.youwu.shouyin.databinding.ActivitySalesDocumentBinding;
import com.youwu.shouyin.ui.money.adapter.RechargeRecordAdapter;
import com.youwu.shouyin.ui.money.adapter.SaleBillAdapter;
import com.youwu.shouyin.ui.money.adapter.SalesAdapter;
import com.youwu.shouyin.ui.money.bean.RechargeBean;
import com.youwu.shouyin.ui.money.bean.SaleBillBean;
import com.youwu.shouyin.ui.set_up.SetUpActivity;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 销售单据
 * 2022/03/28
 */
public class SalesDocumentActivity extends BaseActivity<ActivitySalesDocumentBinding, SalesDocumentViewModel> {


    //订单记录recyclerveiw的适配器
    private SaleBillAdapter mSaleBillAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<SaleBillBean> mSaleBillBeans = new ArrayList<>();

    //单据详情recyclerveiw的适配器
    private SalesAdapter mSalesAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private List<SaleBillBean.SaleBean> mSaleBeans = new ArrayList<>();
    int pageNo=1;

    private TimePickerView pvCustomTime;//时间选择器

    private int time_state;//1 开始 2 结束
    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sales_document;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SalesDocumentViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SalesDocumentViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1:
                        time_state=1;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 2:
                        time_state=2;
                        pvCustomTime.show(); //弹出自定义时间选择器
                        break;
                    case 3://反结帐
                        shownDialog();
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
       String time= formater.format(new Date());

       viewModel.start_time.set(time);
       viewModel.end_time.set(time);

        initCustomTimePicker();
        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mSaleBillBeans.clear();
                pageNo=1;
                initczjl();
                refreshLayout.finishRefresh(true);
            }
        });
        //加载
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                initczjl();
                refreshLayout.finishLoadMore(true);//加载完成
            }
        });
        initczjl();


    }

    /**
     * 绑定
     */
    private void initBinding(SaleBillBean saleBillBean) {

        viewModel.cashier_name.set(saleBillBean.getCashier_name());//收银员
        viewModel.create_time.set(saleBillBean.getCreate_time());//创建时间
        viewModel.discount_prick.set(saleBillBean.getDiscount_prick());//优惠金额
        viewModel.cope_with_prick.set(saleBillBean.getReceivable_price());//应收金额
        viewModel.order_sn.set(saleBillBean.getOrder_sn());//订单编号
        viewModel.paid_in_prick.set(saleBillBean.getPaid_in_prick());//订单编号
        viewModel.remarks_content.set(saleBillBean.getRemarks_content());//备注
        viewModel.vip_name.set(saleBillBean.getVip_name());//会员名称
        viewModel.wipe_zero.set(saleBillBean.getWipe_zero());//抹零
        viewModel.pay_mode.set(saleBillBean.getPay_mode());//支付方式
    }


    /**
     * 模拟充值记录数据
     */
    private void initczjl() {

        for (int i=0;i<20;i++){
            List<SaleBillBean.SaleBean> mSaleBeans = new ArrayList<>();
            SaleBillBean saleBillBean=new SaleBillBean();
            saleBillBean.setOrder_sn("123456789"+i);//订单编号
            saleBillBean.setPaid_in_prick("15"+i);//实收价格
            saleBillBean.setCreate_time("2020.03.28 15:17:0"+i);//创建时间
            saleBillBean.setCashier_name("张三"+i);//收银员
            saleBillBean.setDiscount_prick("￥1"+i);//优惠金额
            saleBillBean.setVip_name("李四"+i);//vip名称
            saleBillBean.setWipe_zero("￥0."+i);//抹零
            saleBillBean.setReceivable_price("16"+i);//应收金额
            saleBillBean.setRemarks_content("无"+i);//备注
            if (i%2==0){
                saleBillBean.setPay_mode("支付宝"+i);//支付方式
            }else {
                saleBillBean.setPay_mode("微信"+i);//支付方式
            }

            saleBillBean.setGoods_list(mSaleBeans);

            for (int j=0;j<5;j++){
                SaleBillBean.SaleBean saleBean=new SaleBillBean.SaleBean();
                saleBean.setGoods_name(i+"白菜");
                saleBean.setGoods_number(i+"1"+j);
                saleBean.setGoods_price(i+"2"+j);
                saleBean.setGoods_discount(i+"3"+j);
                saleBean.setTotal_price("4"+j);
                mSaleBeans.add(saleBean);
            }
            mSaleBillBeans.add(saleBillBean);
        }
        initBinding(mSaleBillBeans.get(0));
        initRecyclerView();

        mSaleBeans=mSaleBillBeans.get(0).getGoods_list();
        initRecyclerViewTow();
    }

    /**
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mSaleBillAdapter = new SaleBillAdapter(this, mSaleBillBeans);
        //给RecyclerView设置adapter
        binding.czjlRecyclerview.setAdapter(mSaleBillAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.czjlRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.czjlRecyclerview.getItemDecorationCount()==0) {
            binding.czjlRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSaleBillAdapter.setOnClickListener(new SaleBillAdapter.OnClickListener() {
            @Override
            public void onClick(SaleBillBean lists, int position) {
                mSaleBeans=lists.getGoods_list();
                initBinding(lists);
                initRecyclerViewTow();
            }
        });

    }

    /**
     * 订单详情记录
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mSalesAdapter = new SalesAdapter(this, mSaleBeans);
        //给RecyclerView设置adapter
        binding.xqRecyclerview.setAdapter(mSalesAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.xqRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.xqRecyclerview.getItemDecorationCount()==0) {
            binding.xqRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
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
                    viewModel.start_time.set(getTime(date));
                }else {
                    viewModel.end_time.set(getTime(date));
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



    /**
     * 反结帐弹窗
     */
    private void shownDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_de_closing, null);
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
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        TextView fan_price = (TextView) dialogView.findViewById(R.id.fan_price);
        TextView de_closing = (TextView) dialogView.findViewById(R.id.de_closing);
        fan_price.setText(viewModel.paid_in_prick.get());
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //反结帐
        de_closing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal("反结帐");
            }
        });


    }

}
