package com.youwu.shouyin.ui.vip;

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
 * 2022/03/23
 */

public class AddVipViewModel extends BaseViewModel<DemoRepository> {
    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //开卡日期的绑定
    public ObservableField<String> vip_start_time = new ObservableField<>("");
    //备注的绑定
    public ObservableField<String> vip_remarks = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public AddVipViewModel(@NonNull Application application, DemoRepository repository) {
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
    //取消的点击事件
    public BindingCommand cancelOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });
    //创建会员信息的点击事件
    public BindingCommand AddVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
