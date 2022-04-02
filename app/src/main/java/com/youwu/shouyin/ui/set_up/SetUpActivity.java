package com.youwu.shouyin.ui.set_up;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyin.BR;
import com.youwu.shouyin.BuildConfig;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivitySetUpBinding;


import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 搜索会员
 * 2022/03/23
 */
public class SetUpActivity extends BaseActivity<ActivitySetUpBinding, SetUpViewModel> {
    //ActivityLoginBinding类是databinding框架自定生成的,对应activity_login.xml
    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_set_up;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SetUpViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SetUpViewModel.class);
    }

    @Override
    public void initViewObservable() {

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        viewModel.Version_number.set("版本："+BuildConfig.VERSION_NAME);


    }
}
