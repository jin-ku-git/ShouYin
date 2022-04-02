package com.youwu.shouyin.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/28
 */

public class SalesDocumentViewModel extends BaseViewModel<DemoRepository> {
    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //结束时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");

    //反结帐状态的绑定
    public ObservableField<Boolean> check_state = new ObservableField<>();

    //订单编号的绑定
    public ObservableField<String> order_sn = new ObservableField<>("");
    //会员名称的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //收银员名称的绑定
    public ObservableField<String> cashier_name = new ObservableField<>("");
    //创建时间的绑定
    public ObservableField<String> create_time = new ObservableField<>("");
    //备注的绑定
    public ObservableField<String> remarks_content = new ObservableField<>("");

    //应收金额的绑定
    public ObservableField<String> cope_with_prick = new ObservableField<>("");
    //实收金额的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");
    //优惠折扣金额的绑定
    public ObservableField<String> discount_prick = new ObservableField<>("");
    //抹零的绑定
    public ObservableField<String> wipe_zero = new ObservableField<>("");
    //支付方式的绑定
    public ObservableField<String> pay_mode = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SalesDocumentViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });

    //打印小票的点击事件
    public BindingCommand PrintOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("打印小票");
        }
    });
    //复用订单的点击事件
    public BindingCommand CopyOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("复用订单");
        }
    });


    //开始时间的点击事件
    public BindingCommand StateOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //开始时间的点击事件
    public BindingCommand EndOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });

    //反结帐的点击事件
    public BindingCommand DeClosingOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
