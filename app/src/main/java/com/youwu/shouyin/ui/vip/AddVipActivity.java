package com.youwu.shouyin.ui.vip;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityAddVipBinding;
import com.youwu.shouyin.databinding.ActivitySouSuoVipBinding;
import com.youwu.shouyin.ui.bean.VipBean;
import com.youwu.shouyin.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 添加会员
 * 2022/03/23
 */
public class AddVipActivity extends BaseActivity<ActivityAddVipBinding, AddVipViewModel> {

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_add_vip;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public AddVipViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(AddVipViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://创建会员信息
                        RxToast.normal("会员名称："+viewModel.vip_name.get()+",\n会员手机号："+viewModel.vip_tel.get()+
                                ",\n开卡日期："+viewModel.vip_start_time.get()+"，\n备注："+viewModel.vip_remarks.get());
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd");
        String time= formater.format(new Date());

        viewModel.vip_start_time.set(time);


    }
}
