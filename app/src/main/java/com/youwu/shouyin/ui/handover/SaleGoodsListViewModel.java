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
 * 2022/03/25
 */

public class SaleGoodsListViewModel extends BaseViewModel<DemoRepository> {


    //收银员的绑定
    public ObservableField<String> logo_name = new ObservableField<>("");

    //登录时间的绑定
    public ObservableField<String> logo_time = new ObservableField<>("");

    //菜品名称的绑定
    public ObservableField<String> goods_name = new ObservableField<>("");



    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SaleGoodsListViewModel(@NonNull Application application, DemoRepository repository) {
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



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
