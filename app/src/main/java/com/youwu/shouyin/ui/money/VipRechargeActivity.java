package com.youwu.shouyin.ui.money;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityVipRechargeBinding;
import com.youwu.shouyin.ui.main.DemoActivity;
import com.youwu.shouyin.ui.main.adapter.CouponListRecycleAdapter;
import com.youwu.shouyin.ui.main.bean.CouponBean;
import com.youwu.shouyin.ui.money.bean.VipRechargeBean;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.RxToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 会员充值
 * 2022/03/28
 */
public class VipRechargeActivity extends BaseActivity<ActivityVipRechargeBinding, VipRechargeViewModel> {

    //充值金额recyclerveiw的适配器
    private VipRechargeRecycleAdapter mVipRechargeRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<VipRechargeBean> rechargeList = new ArrayList<>();

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_vip_recharge;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public VipRechargeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(VipRechargeViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://自定义充值
                        Animation topAnim = AnimationUtils.loadAnimation(
                                VipRechargeActivity.this, R.anim.activity_down_in);
                        //切换特效
                        binding.customLayout.startAnimation(topAnim);
                        break;
                        case 2://现金充值
                            RxToast.normal("现金充值");
                        break;
                        case 3://返回充值列表
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    VipRechargeActivity.this, R.anim.activity_down_out);
                            //切换特效
                            binding.customLayout.startAnimation(topAnimTow);
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
//        viewModel.TOP_TITLE.set("会员充值");
        viewModel.custom_bool.set(false);


        initList();


    }

    /**
     * 测试数据
     */
    private void initList() {

        VipRechargeBean vipRechargeBean=new VipRechargeBean();
        vipRechargeBean.setRecharge_prick("50.00");
        vipRechargeBean.setRecharge_content("");
        rechargeList.add(vipRechargeBean);
        VipRechargeBean vipRechargeBean2=new VipRechargeBean();
        vipRechargeBean2.setRecharge_prick("100.00");
        vipRechargeBean2.setRecharge_content("");
        rechargeList.add(vipRechargeBean2);
        VipRechargeBean vipRechargeBean3=new VipRechargeBean();
        vipRechargeBean3.setRecharge_prick("200.00");
        vipRechargeBean3.setRecharge_content("赠送：5元无门槛通用券x2");
        rechargeList.add(vipRechargeBean3);
        VipRechargeBean vipRechargeBean4=new VipRechargeBean();
        vipRechargeBean4.setRecharge_prick("500.00");
        vipRechargeBean4.setRecharge_content("赠送：5元无门槛通用券x3");
        rechargeList.add(vipRechargeBean4);
        VipRechargeBean vipRechargeBean5=new VipRechargeBean();
        vipRechargeBean5.setRecharge_prick("1000.00");
        vipRechargeBean5.setRecharge_content("赠送：5元无门槛通用券x4");
        rechargeList.add(vipRechargeBean5);
        VipRechargeBean vipRechargeBean6=new VipRechargeBean();
        vipRechargeBean6.setRecharge_prick("2000.00");
        vipRechargeBean6.setRecharge_content("赠送：5元无门槛通用券x5");
        rechargeList.add(vipRechargeBean6);
        initRecyclerViewFive();
    }

    /**
     * 优惠券列表
     */
    private void initRecyclerViewFive() {
        //创建adapter
        mVipRechargeRecycleAdapter = new VipRechargeRecycleAdapter(this, rechargeList);
        //给RecyclerView设置adapter
        binding.prickRecyclerview.setAdapter(mVipRechargeRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.prickRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.prickRecyclerview.getItemDecorationCount()==0) {
            binding.prickRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mVipRechargeRecycleAdapter.setOnCouPonListener(new VipRechargeRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(VipRechargeBean data, int position) {

            }
        });

    }
}
