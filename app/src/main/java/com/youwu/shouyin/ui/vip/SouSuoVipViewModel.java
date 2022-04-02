package com.youwu.shouyin.ui.vip;

import android.app.Application;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.main.DemoActivity;
import com.youwu.shouyin.utils_view.RxToast;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.binding.command.BindingConsumer;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;
import me.goldze.mvvmhabit.utils.RxUtils;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 2022/03/23
 */

public class SouSuoVipViewModel extends BaseViewModel<DemoRepository> {
    //用户名手机号的绑定
    public ObservableField<String> vip_tel = new ObservableField<>("");
    //用户名的绑定
    public ObservableField<String> vip_name = new ObservableField<>("");
    //用户名余额的绑定
    public ObservableField<String> vip_money = new ObservableField<>("");

    //用户名余额的绑定
    public ObservableField<Integer> type_state = new ObservableField<>();

    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public SouSuoVipViewModel(@NonNull Application application, DemoRepository repository) {
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

    //搜索VIP的点击事件
    public BindingCommand SouSuoOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("搜索手机号:"+vip_tel.get());

        }
    });
    //推送优惠券的点击事件
    public BindingCommand PushDisOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("推送优惠");

        }
    });
    //充值的点击事件
    public BindingCommand RechargeOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(2);

        }
    });
    //选择会员的点击事件
    public BindingCommand ChoiceOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            IntegerEvent.setValue(1);
//            RxToast.normal("选择会员");

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
