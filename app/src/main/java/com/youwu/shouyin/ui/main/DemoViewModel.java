package com.youwu.shouyin.ui.main;

import android.app.Application;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.vip.SouSuoVipActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/21
 */

public class DemoViewModel extends BaseViewModel {


    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();

    //搜索页面的显示和隐藏
    public ObservableField<Boolean> sou_suo_bool = new ObservableField<>();
    //vip信息的显示和隐藏
    public ObservableField<Boolean> vip_bool = new ObservableField<>();

    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");
    //应付价格的绑定
    public ObservableField<String> paid_in = new ObservableField<>("");
    //优惠金额的绑定
    public ObservableField<String> discount_prick = new ObservableField<>("");


    public DemoViewModel(@NonNull Application application, DemoRepository repository) {
        super(application,repository);
    }
    //添加会员的点击事件
    public BindingCommand AddVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);
        }
    });
    //交接班的点击事件
    public BindingCommand HandoverOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(3);
        }
    });
    //销售单据的点击事件
    public BindingCommand SalesDocumentOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(4);
        }
    });
    //申请订货的点击事件
    public BindingCommand OrderGoodsOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(5);
        }
    });
    //更多的点击事件
    public BindingCommand moreOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(6);
        }
    });
    //选择VIP的点击事件
    public BindingCommand choiceVipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(7);
        }
    });
    //搜索的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(8);
            sou_suo_bool.set(true);
        }
    });
    //取消搜索的点击事件
    public BindingCommand cancelSouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(9);
            sou_suo_bool.set(false);
        }
    });

    //清除按钮的点击事件
    public BindingCommand clearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(10);
        }
    });
    //选择优惠券的点击事件
    public BindingCommand CouponOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(11);
        }
    });
    //点击vip信息的点击事件
    public BindingCommand VipOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(12);
        }
    });

    //收银的点击事件
    public BindingCommand CashierOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(13);
        }
    });

}
