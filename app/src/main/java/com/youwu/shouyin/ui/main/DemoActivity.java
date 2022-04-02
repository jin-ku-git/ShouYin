package com.youwu.shouyin.ui.main;

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
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xuexiang.xui.utils.KeyboardUtils;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppApplication;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityDemoBinding;
import com.tbruyelle.rxpermissions2.RxPermissions;
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
import com.youwu.shouyin.ui.order_goods.OrderGoodsActivity;
import com.youwu.shouyin.ui.set_up.SetUpActivity;
import com.youwu.shouyin.ui.vip.AddVipActivity;
import com.youwu.shouyin.ui.vip.SouSuoVipActivity;
import com.youwu.shouyin.util.ScanUtils;
import com.youwu.shouyin.utils_view.BigDecimalUtils;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.EditTextUtils;
import com.youwu.shouyin.utils_view.KeybordUtil;
import com.youwu.shouyin.utils_view.RxToast;
import com.youwu.shouyin.utils_view.StatusBarUtil;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;
import me.goldze.mvvmhabit.utils.ToastUtils;

/**
 * 首页
 * 2022/03/21
 */

public class DemoActivity extends BaseActivity<ActivityDemoBinding, DemoViewModel> implements ScanUtils.OnResultListener{

    //分类recyclerveiw的适配器
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //定义以CommunityEntityList实体类为对象的数据集合
    private ArrayList<String> CommunityEntityList = new ArrayList<>();

    //商品recyclerveiw的适配器
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //购物车recyclerveiw的适配器
    private ShoppingRecycleAdapter mShoppingRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();

    //搜索商品recyclerveiw的适配器
    private SouSuoListRecycleAdapter mSouSuoListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CommunityBean> SouSuoEntityList = new ArrayList<>();

    //优惠券recyclerveiw的适配器
    private CouponListRecycleAdapter mCouponListRecycleAdapter;
    //定义以CabinetEntityList实体类为对象的数据集合
    private ArrayList<CouponBean> CouponEntityList = new ArrayList<>();

    ArrayList<CommunityBean> SouSuoList = new ArrayList<CommunityBean>();

    Intent intent;

    private CouponBean couponBean;//选择的优惠券


    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public DemoViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(DemoViewModel.class);
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_demo;
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
                    case 2://添加会员
                        startActivity(AddVipActivity.class);
                        break;
                    case 3://交接班
                        startActivity(HandoverActivity.class);
                        break;
                    case 4://销售单据
                        startActivity(SalesDocumentActivity.class);
                        break;
                    case 5://申请订货
                        startActivity(OrderGoodsActivity.class);
                        break;
                    case 6://更多弹窗
                        showFunctionDialog();
                        break;

                    case 7://跳转到选择会员
                        intent = new Intent(DemoActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type",1);
                        startActivityForResult(intent,200);
                        break;
                    case 8://显示搜索
                        Animation topAnim = AnimationUtils.loadAnimation(
                                DemoActivity.this, R.anim.activity_down_in);
                        //切换特效
                        binding.sousuoLayout.startAnimation(topAnim);
                        break;
                    case 9://关闭搜索
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    DemoActivity.this, R.anim.activity_down_out);
                            //切换特效
                            binding.sousuoLayout.startAnimation(topAnimTow);
                        break;
                    case 10://清除购物车
                        ShoppingEntityList.clear();
                        for (int i=0;i<CabinetEntityList.size();i++){
                            CabinetEntityList.get(i).setCom_number(1);
                            CabinetEntityList.get(i).setCom_number_state(false);
                        }
                        initRecyclerViewTow();
                        //更新UI
                        updateView();
                        break;

                    case 11://优惠券弹窗

                        showCouponDialog();
                        break;
                    case 12://点击vip信息
                             intent = new Intent(DemoActivity.this, SouSuoVipActivity.class);
                            intent.putExtra("type",2);
                            startActivityForResult(intent,200);
                        break;
                    case 13://跳转到收银
                        if (ShoppingEntityList.size()==0){
                            RxToast.normal("请选择商品!");
                        }else {
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("ShoppingEntityList", ShoppingEntityList);
                            mBundle.putString("paid_in", viewModel.paid_in.get());
                            mBundle.putString("discount_price", viewModel.discount_prick.get());
                            startActivity(CashierActivity.class,mBundle);
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

        // 可以调用该方法，设置不允许滑动退出
        setSwipeBackEnable(false);
        viewModel.sou_suo_bool.set(false);//默认不显示搜索
        viewModel.vip_bool.set(false);//默认没选择会员
        /**
         * 模拟数据
         */
        initCommunit();

        /**
         * 优惠券模拟数据
         */
        initCouPonList();


        //添加editText的监听事件
        binding.souSuoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {

                KLog.d("SouSuoList------》"+SouSuoList.size());
                SouSuoEntityList.clear();
                KLog.d("SouSuoList------2222》"+SouSuoList.size());
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
     * 接收传递的数据
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200://从选择会员页面传的数据
                    VipBean   vipBean = (VipBean) data.getSerializableExtra("vipBean");
                    if (vipBean.getType_state()==1){
                        viewModel.vip_bool.set(true);
                        viewModel.vip_name.set(vipBean.getName());
                        viewModel.vip_money.set(vipBean.getMoney());
                    }else {
                        viewModel.vip_bool.set(false);
                    }

                    break;
            }
        }
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
            communityBean.setGoods_number("12345678"+j);
            communityBean.setCom_discount_price("0");
            communityBean.setId(j);
            communityBean.setCom_number(1);
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
                data.setCom_number_state(true);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,position);
                /**
                 * 添加
                 */
                addShopping(data);

            }
        });
        mCabinetListRecycleAdapter.setOnPopupListener(new CommunityListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data,int position) {
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
        mShoppingRecycleAdapter = new ShoppingRecycleAdapter(this, ShoppingEntityList);
        //给RecyclerView设置adapter
        binding.shoppingCartRecycler.setAdapter(mShoppingRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.shoppingCartRecycler.getItemDecorationCount()==0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        /**
         * 加减
         */
        mShoppingRecycleAdapter.setOnChangeListener(new ShoppingRecycleAdapter.OnChangeListener() {
            @Override
            public void onChange(CommunityBean data,int position) {
                updateShopping(data,position);

                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,data.getPosition());
            }
        });
        /**
         * 删除
         */
        mShoppingRecycleAdapter.setOnDeleteListener(new ShoppingRecycleAdapter.OnDeleteListener() {
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
        mSouSuoListRecycleAdapter = new SouSuoListRecycleAdapter(this, SouSuoEntityList);
        //给RecyclerView设置adapter
        binding.sousuoRecycler.setAdapter(mSouSuoListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.sousuoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.sousuoRecycler.getItemDecorationCount()==0) {
            binding.sousuoRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSouSuoListRecycleAdapter.setOnAddShopping(new SouSuoListRecycleAdapter.OnAddShopping() {
            @Override
            public void onAddShopping(CommunityBean data, int position) {
                data.setCom_number_state(true);
                /**
                 * 更新商品UI
                 */
                updateGoodsView(data,position);
                /**
                 * 添加
                 */
                addShopping(data);
            }
        });
        mSouSuoListRecycleAdapter.setOnPopupListener(new SouSuoListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data,int position) {
                data.setCom_number(1);
                /**
                 * 商品详情弹窗
                 */
                showDialog(data,position);
            }
        });

    }



    /**
     * 添加到购物车  +1
     * @param data
     */
    private void addShopping(CommunityBean data) {
        int hasInShopCar = -1;
        for (int i=0;i<ShoppingEntityList.size();i++){
            if (data.getId()==ShoppingEntityList.get(i).getId()){
                //如果是修改直接中断循环
                hasInShopCar = i;
                break;
            }
        }
        if (hasInShopCar == -1) {
            data.setCom_number(1);
            ShoppingEntityList.add(data);
        }else {
            ShoppingEntityList.get(hasInShopCar).setCom_number(ShoppingEntityList.get(hasInShopCar).getCom_number()+1);
        }
        //更新UI
        updateView();
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

            binding.shoppingNum.setText(num+"");
        }
        initRecyclerViewThree();
        //计算价格
        countMoney();

    }

    /**
     * 计算价格
     */
    private void countMoney() {
        Double total_price=0.00;//总价
        Double discount_price=0.00;//优惠价格

        if (couponBean!=null){//判断优惠券不等于空

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long end = 0L;
            Long start = 0L;
            try {
                end = sf.parse((couponBean.getEndTime()+" 23:59:59")).getTime();// 日期转换为时间戳
                start = sf.parse((couponBean.getStartTime()+" 00:00:00")).getTime();// 日期转换为时间戳
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KLog.d("结束时间："+end);
            KLog.d("开始时间："+start);
            KLog.d("当前时间："+System.currentTimeMillis());

            if (System.currentTimeMillis() > end) {
                RxToast.normal("优惠券已经过期！");
                couponBean = null;
                for (int i=0;i<ShoppingEntityList.size();i++){
                    if (ShoppingEntityList.get(i).getCom_discount_price()!=null){
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();
                        discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                    }
                }

            } else if (System.currentTimeMillis() < start) {
                RxToast.normal("优惠券活动还未开始！");
                couponBean = null;
                for (int i=0;i<ShoppingEntityList.size();i++){
                    if (ShoppingEntityList.get(i).getCom_discount_price()!=null){
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();
                        discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                    }
                }
            }else {
                for (int i=0;i<ShoppingEntityList.size();i++){
                    if (ShoppingEntityList.get(i).getCom_discount_price()!=null){
                        //（商品价格-商品优惠价格）*购买的数量
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();

                        discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                    }
                }
                //减去优惠券价格
                if (total_price-couponBean.getCou_money()<0.0){
                    //加上total_price价格
                    discount_price=discount_price+total_price;
                    total_price=0.0;
                }else {
                    total_price=total_price-couponBean.getCou_money();
                    //加上优惠券价格
                    discount_price=discount_price+couponBean.getCou_money();
                }
            }
        }else {
            for (int i=0;i<ShoppingEntityList.size();i++){
                if (ShoppingEntityList.get(i).getCom_discount_price()!=null){
                    total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();
                    discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                }
            }
        }


        viewModel.discount_prick.set(discount_price+"");
        viewModel.paid_in.set(""+BigDecimalUtils.formatRoundUp(total_price,2));
    }

    /**
     * 加减操作
     * @param data
     */
    private void updateShopping(CommunityBean data,int position) {
        ShoppingEntityList.set(position,data);
        //更新UI
        updateView();
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_good_details, null);
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
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView goods_name = (TextView) dialogView.findViewById(R.id.goods_name);
        TextView goods_number = (TextView) dialogView.findViewById(R.id.goods_number);
        final TextView goods_price = (TextView) dialogView.findViewById(R.id.goods_price);
        ImageView iv_edit_subtract = (ImageView) dialogView.findViewById(R.id.iv_edit_subtract);//减
        ImageView iv_edit_add = (ImageView) dialogView.findViewById(R.id.iv_edit_add);//加
        TextView add_shopping = (TextView) dialogView.findViewById(R.id.add_shopping);//加入购物车
        final EditText goods_quantity = (EditText) dialogView.findViewById(R.id.goods_quantity);//商品数量
        final LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        final MyCustKeybords custom_keyboard = (MyCustKeybords) dialogView.findViewById(R.id.custom_keyboard);//键盘


        goods_name.setText(data.getCom_name());
        goods_number.setText("商品编号："+data.getGoods_number());
        goods_price.setText(""+data.getCom_price());
        goods_quantity.setText(data.getCom_number()+"");
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //加
        iv_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number(data.getCom_number()+1);
                goods_quantity.setText(data.getCom_number()+"");
                goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
            }
        });
        //减
        iv_edit_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCom_number()==1){
                    RxToast.normal("无法继续减少");
                }else {
                    data.setCom_number(data.getCom_number()-1);
                    goods_quantity.setText(data.getCom_number()+"");
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }

            }
        });
        //关闭键盘
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(goods_quantity);
            }
        });

        //添加editText的监听事件
        goods_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())||s.toString()==null){
                    RxToast.normal("最少购买一份");
                    goods_quantity.setText("1");
                    data.setCom_number(1);
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }else {
                    data.setCom_number(Integer.parseInt(s.toString()));
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }

            }
        });
        //加入购物车
        add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number_state(true);
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
        });

//        disableShowSoftInput(goods_quantity);
//        goods_quantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    custom_keyboard.bindEditText(goods_quantity);
//
//                }
//            }
//        });

//        custom_keyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
//            @Override
//            public void onConfirm() {
//                    RxToast.normal("结账");
//
//            }
//        });

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

    /**
     * 功能菜单弹窗
     */
    private void showFunctionDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_function, null);
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
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final Button button_1 = (Button) dialogView.findViewById(R.id.button_1);
        final Button button_2 = (Button) dialogView.findViewById(R.id.button_2);
        final Button button_3 = (Button) dialogView.findViewById(R.id.button_3);
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //门店充值记录
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RechargeRecordActivity.class);

            }
        });
        //设置
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到设置
                startActivity(SetUpActivity.class);
//                RxToast.normal("设置");
            }
        });
        //敬请期待
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal("敬请期待");
            }
        });

    }

    private RecyclerView coupon_recycler;

    /**
     * 优惠券弹窗
     */
    private void showCouponDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_coupon, null);
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
        dialog.show();

        //初始化控件
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//返回
        TextView use_coupon = (TextView) dialogView.findViewById(R.id.use_coupon);//确认使用优惠券
        coupon_recycler = (RecyclerView) dialogView.findViewById(R.id.coupon_recycler);

        initRecyclerViewFive();
        //返回
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //确认使用优惠券
        use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countMoney();

                dialog.dismiss();
//                RxToast.normal("使用优惠券");
            }
        });

    }

    /**
     * 优惠券模拟数据
     */
    private void initCouPonList() {
        if (CouponEntityList.size()!=0){
            CouponEntityList.clear();
        }
        for (int i=0;i<10;i++){
            CouponBean couponBean=new CouponBean();
            couponBean.setName("全品类商品无门槛"+i);
            couponBean.setCou_money(Double.parseDouble("1"+i));
            couponBean.setStartTime("2022-03-24");
            couponBean.setEndTime("2022-03-25");
            CouponEntityList.add(couponBean);
        }

    }

    /**
     * 优惠券列表
     */
    private void initRecyclerViewFive() {
        //创建adapter
        mCouponListRecycleAdapter = new CouponListRecycleAdapter(this, CouponEntityList);
        //给RecyclerView设置adapter
        coupon_recycler.setAdapter(mCouponListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        coupon_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (coupon_recycler.getItemDecorationCount()==0) {
            coupon_recycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
        mCouponListRecycleAdapter.setOnCouPonListener(new CouponListRecycleAdapter.OnCouPonListener() {
            @Override
            public void onCouPon(CouponBean data, int position) {
                couponBean=data;

            }
        });
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (ScanUtils.getInstance().isInputFromScanner(DemoActivity.this, event)) {

            //暂时取消扫码
            ScanUtils.getInstance().setOnResultListener(DemoActivity.this);
            ScanUtils.getInstance().analysisKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }


    //外接扫码器的回调
    @Override
    public void onResult(final String resultStr) {
        KLog.e("scanToWork2222", "onResult:" + resultStr);
        if (TextUtils.isEmpty(resultStr)) {
            return;
        }
        long last = AppApplication.spUtils.getLong("lastpay");
        if (System.currentTimeMillis() - last < 2000) {
            return;
        }
        AppApplication.spUtils.put("lastpay", System.currentTimeMillis());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                scanToWork(resultStr);
            }
        });
    }
    private void scanToWork(String resultStr) {

        RxToast.normal("扫描信息："+resultStr);

    }

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("再按一次退出程序");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
