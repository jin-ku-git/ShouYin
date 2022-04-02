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
 * 2022/03/23
 */

public class RechargeRecordViewModel extends BaseViewModel<DemoRepository> {
    //开始时间的绑定
    public ObservableField<String> start_time = new ObservableField<>("");
    //开始时间的绑定
    public ObservableField<String> end_time = new ObservableField<>("");


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public RechargeRecordViewModel(@NonNull Application application, DemoRepository repository) {
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
    //获取的点击事件
    public BindingCommand obtainOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
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





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
