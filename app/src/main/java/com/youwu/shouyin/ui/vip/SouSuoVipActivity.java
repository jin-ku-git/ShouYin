package com.youwu.shouyin.ui.vip;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityLoginBinding;
import com.youwu.shouyin.databinding.ActivitySouSuoVipBinding;
import com.youwu.shouyin.ui.bean.VipBean;
import com.youwu.shouyin.ui.login.LoginViewModel;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.money.VipRechargeActivity;
import com.youwu.shouyin.utils_view.RxToast;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 搜索会员
 * 2022/03/23
 */
public class SouSuoVipActivity extends BaseActivity<ActivitySouSuoVipBinding, SouSuoVipViewModel> {

    private int type;
    Intent intent;
    @Override
    public void initParam() {
        super.initParam();
        type = getIntent().getIntExtra("type",0);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sou_suo_vip;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SouSuoVipViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SouSuoVipViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://选择会员
                        if (type==1){
                            VipBean vipBean=new VipBean();
                            vipBean.setName(viewModel.vip_name.get());
                            vipBean.setMoney(viewModel.vip_money.get());
                            vipBean.setTel(viewModel.vip_tel.get());
                            vipBean.setType_state(1);
                             intent = new Intent();
                            intent.putExtra("vipBean", vipBean);
                            setResult(RESULT_OK, intent);
                            finish();
                        }else {
                            VipBean vipBean=new VipBean();
                            vipBean.setType_state(2);
                            intent = new Intent();
                            intent.putExtra("vipBean", vipBean);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                        break;
                    case 2://充值
                            startActivity(VipRechargeActivity.class);
                        break;
                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        viewModel.type_state.set(type);
        viewModel.vip_name.set("清风拂杨柳");
        viewModel.vip_tel.set("15811112223");
        viewModel.vip_money.set("99");

    }
}
