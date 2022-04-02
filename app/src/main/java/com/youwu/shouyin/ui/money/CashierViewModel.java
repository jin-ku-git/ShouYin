package com.youwu.shouyin.ui.money;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/23
 */

public class CashierViewModel extends BaseViewModel<DemoRepository> {
    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //开始时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");

    //总共多少件商品的绑定
    public ObservableField<String> total_number = new ObservableField<>("");
    //余额状态的绑定
    public ObservableField<Boolean> YE_state = new ObservableField<>();
    //现金状态的绑定
    public ObservableField<Boolean> XJ_state = new ObservableField<>();
    //微信状态的绑定
    public ObservableField<Boolean> WX_state = new ObservableField<>();
    //支付宝状态的绑定
    public ObservableField<Boolean> ZFB_state = new ObservableField<>();

    //是否组合支付状态的绑定
    public ObservableField<Boolean> pay_state = new ObservableField<>();

    //第二个价格是否显示的绑定
    public ObservableField<Boolean> pay_Tow_state = new ObservableField<>();

    //第一个支付方式的绑定
    public ObservableField<String> pay_one_text = new ObservableField<>("");
    //第一个支付方式金额的绑定
    public ObservableField<String> pay_one_prick = new ObservableField<>("");
    //第二个支付方式的绑定
    public ObservableField<String> pay_Tow_text = new ObservableField<>("");
    //第二个支付方式金额的绑定
    public ObservableField<String> pay_Tow_prick = new ObservableField<>("");

    //折扣率的绑定
    public ObservableField<String> pay_discount_rate = new ObservableField<>("");
    //折后金额的绑定
    public ObservableField<String> pay_discount_prick = new ObservableField<>("");
    //用餐人数的绑定
    public ObservableField<String> pay_diners_number = new ObservableField<>("");

    //应付金额的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //优惠金额的绑定
    public ObservableField<String> discount_price = new ObservableField<>("");


    //备注的绑定
    public ObservableField<String> remarks = new ObservableField<>("");
    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public CashierViewModel(@NonNull Application application, DemoRepository repository) {
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
    //余额的点击事件
    public BindingCommand YEOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //现金的点击事件
    public BindingCommand XJOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //微信的点击事件
    public BindingCommand WXOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //支付宝的点击事件
    public BindingCommand ZFBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });






    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
