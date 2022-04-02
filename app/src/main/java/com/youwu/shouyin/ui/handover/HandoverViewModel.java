package com.youwu.shouyin.ui.handover;

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

public class HandoverViewModel extends BaseViewModel<DemoRepository> {

    //收银员的绑定
    public ObservableField<String> logo_name = new ObservableField<>("");

    //登录时间的绑定
    public ObservableField<String> logo_time = new ObservableField<>("");

    //今日充值金额的绑定
    public ObservableField<String> today_money = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public HandoverViewModel(@NonNull Application application, DemoRepository repository) {
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
    //销售商品列表的点击事件
    public BindingCommand TowOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //盘点现金的点击事件
    public BindingCommand OneOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });
    //盘点现金的点击事件
    public BindingCommand ThreeOnClick = new BindingCommand(new BindingAction() {
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
