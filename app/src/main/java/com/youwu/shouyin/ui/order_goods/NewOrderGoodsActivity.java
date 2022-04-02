package com.youwu.shouyin.ui.order_goods;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.utils.KeyboardUtils;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppApplication;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityDemoBinding;
import com.youwu.shouyin.databinding.ActivityNewOrderGoodsBinding;
import com.youwu.shouyin.jianpan.MyCustKeybords;
import com.youwu.shouyin.ui.bean.VipBean;
import com.youwu.shouyin.ui.handover.HandoverActivity;
import com.youwu.shouyin.ui.main.adapter.CommunityListRecycleAdapter;
import com.youwu.shouyin.ui.main.adapter.CommunityRecycleAdapter;
import com.youwu.shouyin.ui.main.adapter.CouponListRecycleAdapter;
import com.youwu.shouyin.ui.main.adapter.ShoppingRecycleAdapter;
import com.youwu.shouyin.ui.main.adapter.SouSuoListRecycleAdapter;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.main.bean.CouponBean;
import com.youwu.shouyin.ui.money.CashierActivity;
import com.youwu.shouyin.ui.money.RechargeRecordActivity;
import com.youwu.shouyin.ui.money.SalesDocumentActivity;
import com.youwu.shouyin.ui.order_goods.adapter.NewOrderShoppingRecycleAdapter;
import com.youwu.shouyin.ui.order_goods.adapter.SouSuoNewOrderRecycleAdapter;
import com.youwu.shouyin.ui.set_up.SetUpActivity;
import com.youwu.shouyin.ui.vip.AddVipActivity;
import com.youwu.shouyin.ui.vip.SouSuoVipActivity;
import com.youwu.shouyin.util.ScanUtils;
import com.youwu.shouyin.utils_view.BigDecimalUtils;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.RxToast;
import com.youwu.shouyin.utils_view.StatusBarUtil;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 新建订货
 * 2022/04/01
 */

public class NewOrderGoodsActivity extends BaseActivity<ActivityNewOrderGoodsBinding, NewOrderGoodsViewModel> {

    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //定义以CommunityEntityList实体类为对象的数据集合
    private ArrayList<String> CommunityEntityList = new ArrayList<>();

    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //购物车recyclerveiw的适配器
    private NewOrderShoppingRecycleAdapter mNewOrderShoppingRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();

    //搜索商品recyclerveiw的适配器
    private SouSuoNewOrderRecycleAdapter mSouSuoNewOrderRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> SouSuoEntityList = new ArrayList<>();

    ArrayList<CommunityBean> SouSuoList = new ArrayList<CommunityBean>();

    Intent intent;



    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public NewOrderGoodsViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(NewOrderGoodsViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_new_order_goods;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://显示搜索
                        Animation topAnim = AnimationUtils.loadAnimation(
                                NewOrderGoodsActivity.this, R.anim.activity_down_in);
                        //切换特效
                        binding.sousuoLayout.startAnimation(topAnim);
                        break;
                    case 2://关闭搜索
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    NewOrderGoodsActivity.this, R.anim.activity_down_out);
                            //切换特效
                            binding.sousuoLayout.startAnimation(topAnimTow);
                        break;
                    case 3://清除购物车
                        ShoppingEntityList.clear();
                        for (int i=0;i<CabinetEntityList.size();i++){
                            CabinetEntityList.get(i).setCom_number(1);
                            CabinetEntityList.get(i).setCom_number_state(false);
                        }
                        initRecyclerViewTow();
                        //更新UI
                        updateView();
                        break;

                    case 4://跳转到收银
                        if (ShoppingEntityList.size()==0){
                            RxToast.normal("请选择商品!");
                        }else {

                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("ShoppingEntityList", ShoppingEntityList);//订货列表
                            mBundle.putString("goods_number", viewModel.goods_number.get());//订货数量
                            mBundle.putString("goods_type", ShoppingEntityList.size()+"");//订货种类
                            startActivity(ConfirmOrderActivity.class,mBundle);
                        }

                        break;

                }
            }
        });

    }

    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();

        viewModel.sou_suo_bool.set(false);//默认不显示搜索

        /**
         * 模拟数据
         */
        initCommunit();



        //添加editText的监听事件
        binding.souSuoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {


                SouSuoEntityList.clear();

                for (int i=0;i<SouSuoList.size();i++){
                    if (SouSuoList.get(i).getCom_name().indexOf(s.toString()) != -1){
                        SouSuoList.get(i).setSou_suo_text(s.toString());
                        SouSuoEntityList.add(SouSuoList.get(i));
                    }
                }
                KLog.d("SouSuoEntityList------3333》"+SouSuoEntityList.size());
                initRecyclerViewFour();

            }
        });
    }


    /**
     * 模拟数据
     */
    private void initCommunit() {
        for (int i=0;i<10;i++){
            CommunityEntityList.add("营养早餐"+i);
        }
        initRecyclerView();

        for (int j=0;j<20;j++){
            CommunityBean communityBean=new CommunityBean();
            communityBean.setCom_name("云南辣椒炒肉"+j);
            communityBean.setCom_price("7."+j);
            communityBean.setGoods_number("1234567"+j);
            communityBean.setGoods_company("把"+j);
            communityBean.setGoods_peibi("1"+j);
            communityBean.setGoods_purchase_price("1"+j);//进货价
            communityBean.setId(j);
            communityBean.setCom_number(j);
            communityBean.setPosition(j);
            communityBean.setCom_number_state(false);
            CabinetEntityList.add(communityBean);
            SouSuoEntityList.add(communityBean);
            SouSuoList.add(communityBean);
        }
        initRecyclerViewTow();
        initRecyclerViewFour();
    }


    /**
     * 分类
     */
    private void initRecyclerView() {
        //一级分类
        //创建adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(this, CommunityEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommunity.setAdapter(mCommunityRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommunity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommunity.getItemDecorationCount()==0) {
            binding.recyclerviewCommunity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }

    /**
     * 商品列表
     */
    private void initRecyclerViewTow() {
        //创建adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(this, CabinetEntityList);
        //给RecyclerView设置adapter
        binding.recyclerviewCommodity.setAdapter(mCabinetListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.recyclerviewCommodity.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.recyclerviewCommodity.getItemDecorationCount()==0) {
            binding.recyclerviewCommodity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number(1);
                /**
                 * 商品详情弹窗
                 */
                showDialog(data,position);
            }
        });


    }
    /**
     * 更新商品UI
     */
    private void updateGoodsView(CommunityBean data, int position) {

        mCabinetListRecycleAdapter.notifyItemChanged(position,mCabinetListRecycleAdapter.getItemId(R.id.shopping_num));
    }


    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        binding.shoppingCartRecycler.setNestedScrollingEnabled(false);
        //创建adapter
        mNewOrderShoppingRecycleAdapter = new NewOrderShoppingRecycleAdapter(this, ShoppingEntityList);
        //给RecyclerView设置adapter
        binding.shoppingCartRecycler.setAdapter(mNewOrderShoppingRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.shoppingCartRecycler.getItemDecorationCount()==0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        /**
         * 删除
         */
        mNewOrderShoppingRecycleAdapter.setOnDeleteListener(new NewOrderShoppingRecycleAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {
                DeleteShopping(data,position);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,data.getPosition());

            }
        });
    }

    /**
     * 搜索商品列表
     */
    private void initRecyclerViewFour() {
        //创建adapter
        mSouSuoNewOrderRecycleAdapter = new SouSuoNewOrderRecycleAdapter(this, SouSuoEntityList);
        //给RecyclerView设置adapter
        binding.sousuoRecycler.setAdapter(mSouSuoNewOrderRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.sousuoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.sousuoRecycler.getItemDecorationCount()==0) {
            binding.sousuoRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSouSuoNewOrderRecycleAdapter.setOnItemClickListener(new SouSuoNewOrderRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number(1);
                /**
                 * 商品详情弹窗
                 */
                showDialog(data,position);
            }
        });

    }


    /**
     * 添加到购物车
     * @param data
     */
    private void addUpShopping(CommunityBean data) {
        int hasInShopCar = -1;
        for (int i=0;i<ShoppingEntityList.size();i++){
            if (data.getId()==ShoppingEntityList.get(i).getId()){
                //如果是修改直接中断循环
                hasInShopCar = i;
                break;
            }
        }
        if (hasInShopCar == -1) {
            ShoppingEntityList.add(data);
        }else {
            ShoppingEntityList.get(hasInShopCar).setCom_number(data.getCom_number());
        }
        //更新UI
        updateView();
    }

    /**
     * 更新UI
     */
    private void updateView() {
        if (ShoppingEntityList.size()==0){
            binding.shoppingNum.setVisibility(View.GONE);
            binding.nullView.setVisibility(View.VISIBLE);
        }else {
            binding.nullView.setVisibility(View.GONE);
            binding.shoppingNum.setVisibility(View.VISIBLE);

            int num=0;
            for (int i=0;i<ShoppingEntityList.size();i++){
                num+=ShoppingEntityList.get(i).getCom_number();
            }
            viewModel.goods_number.set(num+"");
        }
        initRecyclerViewThree();


    }



    /**
     * 删除操作
     * @param data
     */
    private void DeleteShopping(CommunityBean data,int position) {
        data.setCom_number_state(false);
        data.setCom_number(1);
        /**
         * 更新商品UI
         */
        updateGoodsView(data,position);
        ShoppingEntityList.remove(position);
        //更新UI
        updateView();
    }



    /**
     * 商品详情弹窗
     */
    private void showDialog(final CommunityBean data, final int position) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_order_good_details, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //将界面填充到AlertDiaLog容器
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView goods_name = (TextView) dialogView.findViewById(R.id.goods_name);
        final TextView goods_price = (TextView) dialogView.findViewById(R.id.goods_price);


        final EditText goods_number = (EditText) dialogView.findViewById(R.id.new_goods_number);//订货数量

        final TextView new_peibi_number = (TextView) dialogView.findViewById(R.id.new_peibi_number);//配比
        final TextView ratio_number = (TextView) dialogView.findViewById(R.id.new_ratio_number);//配比数量

        final MyCustKeybords custom_keyboard = (MyCustKeybords) dialogView.findViewById(R.id.custom_keyboard);//键盘


        goods_name.setText(data.getCom_name());
        goods_number.setText(data.getCom_number()+"");
        goods_price.setText(""+data.getCom_price());
        new_peibi_number.setText(data.getGoods_peibi());

        String number = data.getCom_number()+"";
        Double multiply = BigDecimalUtils.divide(number, data.getGoods_peibi() + "");
        ratio_number.setText(BigDecimalUtils.formatZero(multiply, 1));

        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        goods_number.setInputType(InputType.TYPE_NULL);
        custom_keyboard.bindEditText(goods_number);
//        disableShowSoftInput(goods_number);

        goods_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    custom_keyboard.bindEditText(goods_number);

                }
            }
        });

        goods_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String number = goods_number.getText().toString();
                Double multiply = BigDecimalUtils.divide(number, data.getGoods_peibi() + "");
                ratio_number.setText(BigDecimalUtils.formatZero(multiply, 1));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //结账
        custom_keyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if ("".equals(goods_number.getText().toString())||goods_number.getText().toString()==null){
                    RxToast.normal("订货数量不得为空");
                }else {
                    data.setCom_number_state(true);
                    data.setCom_number(Integer.parseInt(goods_number.getText().toString()));

                    data.setDistribution_number(ratio_number.getText().toString());
                    String total_price=(Double.parseDouble(goods_number.getText().toString())*Double.parseDouble(data.getGoods_purchase_price()))+"";
                    data.setTotal_price(total_price);

                    /**
                     * 添加到购物车
                     */
                    addUpShopping(data);
                    /**
                     * 更新商品UI
                     */
                    updateGoodsView(data,position);
                    dialog.cancel();
                }

            }
        });

    }

    /**
     * 禁止Edittext弹出软件盘，光标依旧正常显示。
     */
    public void disableShowSoftInput(EditText editText) {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }

        try {
            method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (Exception e) {
        }
    }



}
