package com.youwu.shouyin.ui.money;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.youwu.shouyin.BR;
import com.youwu.shouyin.R;
import com.youwu.shouyin.app.AppViewModelFactory;
import com.youwu.shouyin.databinding.ActivityCashierBinding;
import com.youwu.shouyin.jianpan.MyCustKeybords;
import com.youwu.shouyin.ui.main.adapter.ShoppingRecycleAdapter;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.money.adapter.GoodsListRecycleAdapter;
import com.youwu.shouyin.utils_view.BigDecimalUtils;
import com.youwu.shouyin.utils_view.DividerItemDecorations;
import com.youwu.shouyin.utils_view.RxToast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.KLog;

/**
 * 结算
 * 2022/03/25
 */
public class CashierActivity extends BaseActivity<ActivityCashierBinding, CashierViewModel> {



    //购物车recyclerveiw的适配器
    private GoodsListRecycleAdapter mGoodsListRecycleAdapter;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<CommunityBean> GoodsEntityList = new ArrayList<CommunityBean>();

    List<Integer> pay_mode=new ArrayList<>();

    int total_number;//共多少件商品
    String paid_in;//应付金额
    private Double totalMoney;
    String discount_price;//优惠金额

    String beforeText="";

    @Override
    public void initParam() {
        super.initParam();
        //获取列表传入的实体
        GoodsEntityList= (ArrayList<CommunityBean>) getIntent().getSerializableExtra("ShoppingEntityList");
        paid_in= getIntent().getStringExtra("paid_in");
        discount_price= getIntent().getStringExtra("discount_price");
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_cashier;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public CashierViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getApplication());
        return ViewModelProviders.of(this, factory).get(CashierViewModel.class);
    }


    @Override
    public void initData() {
        super.initData();
        hideBottomUIMenu();
        //默认不是组合支付
        viewModel.pay_state.set(false);
        //第一个支付价格
        viewModel.pay_one_prick.set(paid_in);
        //第二个价格
        viewModel.pay_Tow_prick.set("0");
        //折后金额
        viewModel.pay_discount_prick.set(paid_in);
        //默认折扣100%
        viewModel.pay_discount_rate.set("100");
        totalMoney=Double.parseDouble(paid_in);
        //默认用餐人数
        viewModel.pay_diners_number.set("1");
        //支付金额
        viewModel.paid_in.set(paid_in);
        //优惠金额
        viewModel.discount_price.set(discount_price);

        //默认余额支付
        pay_mode.add(1);
        viewModel.pay_one_text.set("余额");
        viewModel.YE_state.set(true);
        viewModel.XJ_state.set(false);
        viewModel.WX_state.set(false);
        viewModel.ZFB_state.set(false);


        for (int i=0;i<GoodsEntityList.size();i++){
            total_number+=GoodsEntityList.get(i).getCom_number();
        }
        viewModel.total_number.set("共"+total_number+"件");

        initBinding();


        /**
         * 组合支付的监听
         */
        binding.paysCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (!isChecked){
                    viewModel.pay_one_prick.set(paid_in);
                    viewModel.pay_Tow_prick.set("0");
                    viewModel.pay_Tow_state.set(false);
                }
                viewModel.pay_state.set(isChecked);
            }
        });

        binding.customKeyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if (viewModel.pay_state.get()){
                    RxToast.normal("支付方式1："+viewModel.pay_one_text.get()+"+支付金额："+viewModel.pay_one_prick.get()+",\n支付方式2："+viewModel.pay_Tow_text.get()+"+支付金额："+viewModel.pay_Tow_prick.get());
                }else {

                }


            }
        });
        initRecyclerViewThree();
    }

    @Override
    public void initViewObservable() {

        viewModel.IntegerEvent.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch (integer){
                    case 1://余额支付

                        pay_method(1);

                        break;
                    case 2://现金支付
                        pay_method(2);
                        break;
                    case 3://微信支付
                        pay_method(3);
                        break;
                    case 4://支付宝支付
                        pay_method(4);
                        break;

                }
            }
        });

    }

    /**
     * 多选支付方式
     * @param pay_type
     */
    private void pay_method(int pay_type) {
        if (viewModel.pay_state.get()){//判断是否为组合支付
            if (pay_mode.size()==2){
                if (pay_mode.get(0)==pay_type||pay_mode.get(1)==pay_type){//判断是否已原则
                    RxToast.normal("包含");
                    return;
                }
            }
            if (pay_mode.size()==2){
                //支付宝和微信不能同时选择
                if (pay_type==3){//点击微信判断是否含有支付宝 有则删除
                    for (int j = 0; j < pay_mode.size(); j++) {
                        if (pay_mode.get(j) == 4) {//删除支付宝
                            initRemove(pay_mode.get(j));
                            pay_mode.remove(j);
                        }
                    }
                }else if (pay_type==4){//点击支付宝判断是否含有微信 有则删除
                    for (int j = 0; j < pay_mode.size(); j++) {
                        if (pay_mode.get(j) == 3) {//删除微信
                            initRemove(pay_mode.get(j));
                            pay_mode.remove(j);
                        }
                    }
                }
                //如果还有两种支付方式去掉第一个支付方式
                if (pay_mode.size()==2){
                    initRemove(pay_mode.get(0));
                    pay_mode.remove(0);
                }
            }
            //添加点击的支付方式
                pay_mode.add(pay_type);
                switch (pay_type){
                    case 1:
                        viewModel.YE_state.set(true);
                        break;
                    case 2:
                        viewModel.XJ_state.set(true);
                        break;
                    case 3:
                        viewModel.WX_state.set(true);
                        break;
                    case 4:
                        viewModel.ZFB_state.set(true);
                        break;
                }

            //更新ui
            initpayText(pay_mode.get(0),pay_mode.get(1));
        }else {

            initPayMode(pay_type);
            switch (pay_type){
                case 1:
                    viewModel.YE_state.set(true);
                    viewModel.pay_one_text.set("余额");
                    break;
                case 2:
                    viewModel.XJ_state.set(true);
                    viewModel.pay_one_text.set("现金");
                    break;
                case 3:
                    viewModel.WX_state.set(true);
                    viewModel.pay_one_text.set("微信");
                    break;
                case 4:
                    viewModel.ZFB_state.set(true);
                    viewModel.pay_one_text.set("支付宝");
                    break;
            }

        }
    }

    //支付方式赋值
    private void initpayText(Integer integer, Integer integer1) {
        viewModel.pay_Tow_state.set(true);
        switch (integer){
            case 1:
                viewModel.pay_one_text.set("余额");
                break;
            case 2:
                viewModel.pay_one_text.set("现金");
                break;
            case 3:
                viewModel.pay_one_text.set("微信");
                break;
            case 4:
                viewModel.pay_one_text.set("支付宝");
                break;
        }
        switch (integer1){
            case 1:
                viewModel.pay_Tow_text.set("余额");
                break;
            case 2:
                viewModel.pay_Tow_text.set("现金");
                break;
            case 3:
                viewModel.pay_Tow_text.set("微信");
                break;
            case 4:
                viewModel.pay_Tow_text.set("支付宝");
                break;
        }
    }

    private void initRemove(int j) {
        switch (j){
            case 1://余额
                viewModel.YE_state.set(false);
                break;
            case 2://现金
                viewModel.XJ_state.set(false);
                break;
            case 3://微信
                viewModel.WX_state.set(false);
                break;
            case 4://支付宝
                viewModel.ZFB_state.set(false);
                break;
        }
    }

    /**
     * 支付方式
     */
    private void initPayMode(int pay) {
        //初始化
        itAllFalse();
        if (pay_mode.size()!=0){//全部删除
            pay_mode.clear();
        }
        //重新添加
        pay_mode.add(pay);
    }



    /**
     * 键盘绑定
     */
    private void initBinding() {
        disableShowSoftInput(binding.payDiscountRate);
        disableShowSoftInput(binding.payDiscountPrick);
        disableShowSoftInput(binding.payDinersNumber);
        disableShowSoftInput(binding.payOneEdit);
        disableShowSoftInput(binding.payTowEdit);
        //折扣率
        binding.payDiscountRate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payDiscountRate);

                }
            }
        });
        //折后金额
        binding.payDiscountPrick.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payDiscountPrick);
                }
            }
        });
        //用餐人数
        binding.payDinersNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payDinersNumber);
                }
            }
        });
        //第一个价格
        binding.payOneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payOneEdit);
                }
            }
        });
        //第二价格
        binding.payTowEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    binding.customKeyboard.bindEditText(binding.payTowEdit);

                }
            }
        });
        //折后率监听
        binding.payDiscountRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    if (binding.payDiscountRate.hasFocus()) {
                        viewModel.pay_discount_rate.set(binding.payDiscountRate.getText().toString());
                        cul();
                    }
                }else if (TextUtils.isEmpty(s.toString())){//如果数据为空 则折扣率改为0
                    viewModel.pay_discount_rate.set("0");
                    cul();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                String editStr = s.toString().trim();
                int posDot = editStr.indexOf(".");
                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    //首位是点，去掉点
                    s.delete(posDot, posDot + 1);
                }
            }
        });

        //折后价格
        binding.payDiscountPrick.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    if (binding.payDiscountPrick.hasFocus()) {
                        paid_in=binding.payDiscountPrick.getText().toString();
                        cul();
                    }
                }else if (TextUtils.isEmpty(s.toString())){//如果数据为空 则折扣总额改为0

                    viewModel.pay_discount_prick.set("0");
                    paid_in="0";
                    cul();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String editStr = s.toString().trim();
                int posDot = editStr.indexOf(".");
                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    //首位是点，去掉点
                    s.delete(posDot, posDot + 1);
                }

            }
        });

        //第一个价格监听
        binding.payOneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                beforeText=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {
                    try {
                        Double subtract = BigDecimalUtils.subtract(paid_in + "", binding.payOneEdit.getText().toString());
                        if (subtract < 0) {
                            binding.payOneEdit.setText(paid_in + "");
                            return;
                        }
                        if (binding.payOneEdit.hasFocus()) {
                            cul();
                        }
                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                String editStr = s.toString().trim();
                int posDot = editStr.indexOf(".");
                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    //首位是点，去掉点
                    s.delete(posDot, posDot + 1);
                }
            }
        });

        //第二个价格监听
        binding.payTowEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
                beforeText=s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                //判断数据不为空，数据有改变，数据首位不为点
                if (!TextUtils.isEmpty(s.toString())
                        && !s.toString().equals(beforeText)
                        && !s.toString().equals(".")) {

                    try {

                        Double subtract = BigDecimalUtils.subtract(paid_in + "", binding.payTowEdit.getText().toString());
                        if (subtract < 0 && pay_mode.size() > 1) {
                            binding.payTowEdit.setText(paid_in + "");
                            return;
                        }
                        if (binding.payTowEdit.hasFocus()) {
                            cul();
                        }
                    } catch (Exception e) {

                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                String editStr = s.toString().trim();
                int posDot = editStr.indexOf(".");
                if (posDot < 0) {
                    return;
                }else if (posDot == 0) {
                    //首位是点，去掉点
                    s.delete(posDot, posDot + 1);
                }
            }
        });
    }

    private void cul() {
        if (binding.payDiscountRate.hasFocus()) {
            KLog.d("payDiscountRate11");

            //正在打折时修改
            paid_in=BigDecimalUtils.formatRoundUp((BigDecimalUtils.multiply((totalMoney/100) + "", viewModel.pay_discount_rate.get())),2)+"";
            viewModel.pay_discount_prick.set(paid_in);

            viewModel.pay_one_prick.set(paid_in);
            viewModel.pay_Tow_prick.set("0");

        } else if (binding.payDiscountPrick.hasFocus()) {


            viewModel.pay_discount_rate.set(BigDecimalUtils.formatRoundUp((BigDecimalUtils.divide(paid_in, totalMoney+"")*100),2)+"");
            KLog.d("payDiscountPrick22");
            viewModel.pay_discount_prick.set(paid_in);
            viewModel.pay_one_prick.set(paid_in);
            viewModel.pay_Tow_prick.set("0");

        }else if (binding.payOneEdit.hasFocus()) {
            Double subtract = BigDecimalUtils.subtract(paid_in + "", binding.payOneEdit.getText().toString());
            if (pay_mode.size() > 1) {
                if ("".equals(binding.payOneEdit.getText().toString())){//当第一个价格等于空时第二个价格等于实付价格
                    binding.payTowEdit.setText(paid_in);
                }else {
                    if (subtract <= 0) {//当支付价格减去第一个价格等于或小于0时  把实付价格付给第一个价格 第二个价格等于0
                        binding.payTowEdit.setText("0");
                    } else {
                        binding.payTowEdit.setText(BigDecimalUtils.formatRoundUp(subtract, 2) + "");
                    }
                }
            }
        } else if (binding.payTowEdit.hasFocus()) {
            Double subtract = BigDecimalUtils.subtract(paid_in + "", binding.payTowEdit.getText().toString());
            if ("".equals(binding.payTowEdit.getText().toString())){//当第二个价格等于空时第一个价格等于实付价格
                binding.payOneEdit.setText(paid_in);
            }else {
                if (pay_mode.size() > 1) {
                    if (subtract <= 0) {//当支付价格减去第二个价格等于或小于0时  把实付价格付给第一个价格 第一个价格等于0
                        binding.payOneEdit.setText("0");
                    } else {
                        binding.payOneEdit.setText(BigDecimalUtils.formatRoundUp(subtract, 2) + "");
                    }
                }
            }


        }
    }

    private void itAllFalse() {
        viewModel.YE_state.set(false);
        viewModel.XJ_state.set(false);
        viewModel.WX_state.set(false);
        viewModel.ZFB_state.set(false);
    }


    /**
     * 购物车列表
     */
    private void initRecyclerViewThree() {

        //创建adapter
        mGoodsListRecycleAdapter = new GoodsListRecycleAdapter(this, GoodsEntityList);
        //给RecyclerView设置adapter
        binding.goodsRecyclerview.setAdapter(mGoodsListRecycleAdapter);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局

        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        binding.goodsRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //设置item的分割线
        if (binding.goodsRecyclerview.getItemDecorationCount()==0) {
            binding.goodsRecyclerview.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

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
