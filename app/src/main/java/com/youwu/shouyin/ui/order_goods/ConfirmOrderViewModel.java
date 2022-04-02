package com.youwu.shouyin.ui.order_goods;

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
 * 2022/04/02
 */

public class ConfirmOrderViewModel extends BaseViewModel<DemoRepository> {



    //备注的绑定
    public ObservableField<String> remarks_content = new ObservableField<>("");

    //实收金额的绑定
    public ObservableField<String> paid_in_prick = new ObservableField<>("");
    //订货数的绑定
    public ObservableField<String> order_number = new ObservableField<>("");
    //商品种类的绑定
    public ObservableField<String> goods_type = new ObservableField<>("");



    //使用LiveData
    public SingleLiveEvent<Integer> IntegerEvent = new SingleLiveEvent<>();


    public ConfirmOrderViewModel(@NonNull Application application, DemoRepository repository) {
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

    //确认收货的点击事件
    public BindingCommand ReceivingOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("确认收货");
        }
    });





    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
