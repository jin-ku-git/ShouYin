package com.youwu.shouyin.ui.handover;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.app.UserUtils;
import com.youwu.shouyin.databinding.ActivityRechargeRecordBinding;
import com.youwu.shouyin.databinding.ActivitySaleGoodsListBinding;
import com.youwu.shouyin.ui.handover.adapter.SaleGoodsListAdapter;
import com.youwu.shouyin.ui.handover.bean.GoodsBean;
import com.youwu.shouyin.ui.money.RechargeRecordViewModel;
import com.youwu.shouyin.ui.money.adapter.RechargeRecordAdapter;
import com.youwu.shouyin.ui.money.bean.RechargeBean;
import com.youwu.shouyin.utils_view.DividerItemDecorations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.goldze.mvvmhabit.base.BaseActivity;

/**
 * 销售商品列表
 * 2022/03/25
 */
public class SaleGoodsListActivity extends BaseActivity<ActivitySaleGoodsListBinding, SaleGoodsListViewModel> {


    //充值记录recyclerveiw的适配器
    private SaleGoodsListAdapter mSaleGoodsListAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<GoodsBean> SaleGoodsBeans = new ArrayList<>();
    int pageNo=1;

    @Override
    public void initParam() {
        super.initParam();

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_sale_goods_list;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public SaleGoodsListViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(SaleGoodsListViewModel.class);
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){

                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        viewModel.logo_time.set(UserUtils.getLogoTime());
        viewModel.logo_name.set(UserUtils.getLogoName());
        //刷新
        binding.scSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                SaleGoodsBeans.clear();
                pageNo=1;
                initczjl();
                refreshLayout.finishRefresh(true);
            }
        });
        //加载
        binding.scSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                pageNo++;
                initczjl();
                refreshLayout.finishLoadMore(true);//加载完成
            }
        });

    }


    /**
     * 模拟充值记录数据
     */
    private void initczjl() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        for (int i=0;i<20;i++){
            GoodsBean goodsBean=new GoodsBean();

            goodsBean.setGoods_class("早餐类"+i);
            goodsBean.setGoods_code("MX000"+i);
            goodsBean.setGoods_name("水煮鸡蛋"+i);
            goodsBean.setGoods_number(""+i);
            goodsBean.setGoods_prick("3"+i);
            SaleGoodsBeans.add(goodsBean);
        }
        initRecyclerView();
    }

    /**
     * 门店记录
     */
    private void initRecyclerView() {
        //创建adapter
        mSaleGoodsListAdapter = new SaleGoodsListAdapter(this, SaleGoodsBeans);
        //给RecyclerView设置adapter
        binding.czjlRecyclerview.setAdapter(mSaleGoodsListAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.czjlRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.czjlRecyclerview.getItemDecorationCount()==0) {
            binding.czjlRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }




}
