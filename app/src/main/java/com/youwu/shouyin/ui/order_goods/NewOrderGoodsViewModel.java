package com.youwu.shouyin.ui.order_goods;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/21
 */

public class NewOrderGoodsViewModel extends BaseViewModel {


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //搜索页面的显示和隐藏
    public ObservableField<Boolean> sou_suo_bool = new ObservableField<>();
    //应付价格的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //订货数量的绑定
    public ObservableField<String> goods_number = new ObservableField<>("");

    public NewOrderGoodsViewModel(@NonNull Application application, DemoRepository repository) {
        super(application,repository);
    }

    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            finish();
        }
    });


    //搜索的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
            sou_suo_bool.set(true);
        }
    });
    //取消搜索的点击事件
    public BindingCommand cancelSouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
            sou_suo_bool.set(false);
        }
    });

    //清除按钮的点击事件
    public BindingCommand clearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });


    //下一步的点击事件
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });

}
