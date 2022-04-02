package com.youwu.shouyin.ui.set_up;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;

import com.youwu.shouyin.data.DemoRepository;
import com.youwu.shouyin.ui.login.LoginActivity;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingAction;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.bus.event.SingleLiveEvent;

/**
 * 2022/03/23
 */

public class SetUpViewModel extends BaseViewModel<DemoRepository> {
    //版本号的绑定
    public ObservableField<String> Version_number = new ObservableField<>("");
    //当前账号的绑定
    public ObservableField<String> My_Account = new ObservableField<>("");

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();
    }

    public SetUpViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
        //从本地取得数据绑定到View层
        My_Account.set("我的当前账户："+model.getUserName());
    }


    //返回的点击事件
    public BindingCommand returnOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
           finish();
        }
    });

    //数据同步的点击事件
    public BindingCommand DataTBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("数据同步");

        }
    });
    //账号注销的点击事件
    public BindingCommand AccountClearOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {

            model.SignOutAccount();
            startActivity(LoginActivity.class);


        }
    });
    //选择会员的点击事件
    public BindingCommand AccountTBOnClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            RxToast.normal("账号同步");

        }
    });



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
