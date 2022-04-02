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
 * 2022/03/28
 */

public class VipRechargeViewModel extends BaseViewModel<DemoRepository> {



    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //自定义充值页面的显示和隐藏
    public ObservableField<Boolean> custom_bool = new ObservableField<>();
    //Top标题
    public ObservableField<String> TOP_TITLE = new ObservableField<>("会员充值");


    public VipRechargeViewModel(@NonNull Application application, DemoRepository repository) {
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
    //自定义充值的点击事件
    public BindingCommand customOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
            custom_bool.set(true);
            TOP_TITLE.set("自定义充值");
        }
    });

    //现金充值的点击事件
    public BindingCommand cashRechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //返回充值列表的点击事件
    public BindingCommand RechargeListOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
            custom_bool.set(false);
            TOP_TITLE.set("会员充值");
        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
