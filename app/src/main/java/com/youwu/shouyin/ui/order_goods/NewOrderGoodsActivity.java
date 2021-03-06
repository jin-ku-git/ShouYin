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
 * ????????????
 * 2022/04/01
 */

public class NewOrderGoodsActivity extends BaseActivity<ActivityNewOrderGoodsBinding, NewOrderGoodsViewModel> {

    //??????recyclerveiw????????????
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //?????????CommunityEntityList?????????????????????????????????
    private ArrayList<String> CommunityEntityList = new ArrayList<>();

    //??????recyclerveiw????????????
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //?????????CabinetEntityList?????????????????????????????????
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //?????????recyclerveiw????????????
    private NewOrderShoppingRecycleAdapter mNewOrderShoppingRecycleAdapter;
    //?????????goodsentity?????????????????????????????????
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();

    //????????????recyclerveiw????????????
    private SouSuoNewOrderRecycleAdapter mSouSuoNewOrderRecycleAdapter;
    //?????????CabinetEntityList?????????????????????????????????
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
        //??????????????????ViewModelFactory?????????ViewModel????????????????????????????????????????????????LoginViewModel(@NonNull Application application)????????????
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
                    case 1://????????????
                        Animation topAnim = AnimationUtils.loadAnimation(
                                NewOrderGoodsActivity.this, R.anim.activity_down_in);
                        //????????????
                        binding.sousuoLayout.startAnimation(topAnim);
                        break;
                    case 2://????????????
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    NewOrderGoodsActivity.this, R.anim.activity_down_out);
                            //????????????
                            binding.sousuoLayout.startAnimation(topAnimTow);
                        break;
                    case 3://???????????????
                        ShoppingEntityList.clear();
                        for (int i=0;i<CabinetEntityList.size();i++){
                            CabinetEntityList.get(i).setCom_number(1);
                            CabinetEntityList.get(i).setCom_number_state(false);
                        }
                        initRecyclerViewTow();
                        //??????UI
                        updateView();
                        break;

                    case 4://???????????????
                        if (ShoppingEntityList.size()==0){
                            RxToast.normal("???????????????!");
                        }else {

                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable("ShoppingEntityList", ShoppingEntityList);//????????????
                            mBundle.putString("goods_number", viewModel.goods_number.get());//????????????
                            mBundle.putString("goods_type", ShoppingEntityList.size()+"");//????????????
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

        viewModel.sou_suo_bool.set(false);//?????????????????????

        /**
         * ????????????
         */
        initCommunit();



        //??????editText???????????????
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
                KLog.d("SouSuoEntityList------3333???"+SouSuoEntityList.size());
                initRecyclerViewFour();

            }
        });
    }


    /**
     * ????????????
     */
    private void initCommunit() {
        for (int i=0;i<10;i++){
            CommunityEntityList.add("????????????"+i);
        }
        initRecyclerView();

        for (int j=0;j<20;j++){
            CommunityBean communityBean=new CommunityBean();
            communityBean.setCom_name("??????????????????"+j);
            communityBean.setCom_price("7."+j);
            communityBean.setGoods_number("1234567"+j);
            communityBean.setGoods_company("???"+j);
            communityBean.setGoods_peibi("1"+j);
            communityBean.setGoods_purchase_price("1"+j);//?????????
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
     * ??????
     */
    private void initRecyclerView() {
        //????????????
        //??????adapter
        mCommunityRecycleAdapter = new CommunityRecycleAdapter(this, CommunityEntityList);
        //???RecyclerView??????adapter
        binding.recyclerviewCommunity.setAdapter(mCommunityRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.recyclerviewCommunity.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.recyclerviewCommunity.getItemDecorationCount()==0) {
            binding.recyclerviewCommunity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }
    }

    /**
     * ????????????
     */
    private void initRecyclerViewTow() {
        //??????adapter
        mCabinetListRecycleAdapter = new CommunityListRecycleAdapter(this, CabinetEntityList);
        //???RecyclerView??????adapter
        binding.recyclerviewCommodity.setAdapter(mCabinetListRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.recyclerviewCommodity.setLayoutManager(new StaggeredGridLayoutManager(5, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.recyclerviewCommodity.getItemDecorationCount()==0) {
            binding.recyclerviewCommodity.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mCabinetListRecycleAdapter.setOnItemClickListener(new CommunityListRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number(1);
                /**
                 * ??????????????????
                 */
                showDialog(data,position);
            }
        });


    }
    /**
     * ????????????UI
     */
    private void updateGoodsView(CommunityBean data, int position) {

        mCabinetListRecycleAdapter.notifyItemChanged(position,mCabinetListRecycleAdapter.getItemId(R.id.shopping_num));
    }


    /**
     * ???????????????
     */
    private void initRecyclerViewThree() {

        binding.shoppingCartRecycler.setNestedScrollingEnabled(false);
        //??????adapter
        mNewOrderShoppingRecycleAdapter = new NewOrderShoppingRecycleAdapter(this, ShoppingEntityList);
        //???RecyclerView??????adapter
        binding.shoppingCartRecycler.setAdapter(mNewOrderShoppingRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.shoppingCartRecycler.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.shoppingCartRecycler.getItemDecorationCount()==0) {
            binding.shoppingCartRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        /**
         * ??????
         */
        mNewOrderShoppingRecycleAdapter.setOnDeleteListener(new NewOrderShoppingRecycleAdapter.OnDeleteListener() {
            @Override
            public void onDelete(CommunityBean data, int position) {
                DeleteShopping(data,position);
                /**
                 * ????????????UI
                 */
                updateGoodsView(data,data.getPosition());

            }
        });
    }

    /**
     * ??????????????????
     */
    private void initRecyclerViewFour() {
        //??????adapter
        mSouSuoNewOrderRecycleAdapter = new SouSuoNewOrderRecycleAdapter(this, SouSuoEntityList);
        //???RecyclerView??????adapter
        binding.sousuoRecycler.setAdapter(mSouSuoNewOrderRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.sousuoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.sousuoRecycler.getItemDecorationCount()==0) {
            binding.sousuoRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSouSuoNewOrderRecycleAdapter.setOnItemClickListener(new SouSuoNewOrderRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, CommunityBean data, int position) {
                data.setCom_number(1);
                /**
                 * ??????????????????
                 */
                showDialog(data,position);
            }
        });

    }


    /**
     * ??????????????????
     * @param data
     */
    private void addUpShopping(CommunityBean data) {
        int hasInShopCar = -1;
        for (int i=0;i<ShoppingEntityList.size();i++){
            if (data.getId()==ShoppingEntityList.get(i).getId()){
                //?????????????????????????????????
                hasInShopCar = i;
                break;
            }
        }
        if (hasInShopCar == -1) {
            ShoppingEntityList.add(data);
        }else {
            ShoppingEntityList.get(hasInShopCar).setCom_number(data.getCom_number());
        }
        //??????UI
        updateView();
    }

    /**
     * ??????UI
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
     * ????????????
     * @param data
     */
    private void DeleteShopping(CommunityBean data,int position) {
        data.setCom_number_state(false);
        data.setCom_number(1);
        /**
         * ????????????UI
         */
        updateGoodsView(data,position);
        ShoppingEntityList.remove(position);
        //??????UI
        updateView();
    }



    /**
     * ??????????????????
     */
    private void showDialog(final CommunityBean data, final int position) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //??????????????????
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //????????????
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_new_order_good_details, null);
        //??????????????????AlertDiaLog??????
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //??????????????????
        layoutParams.width = (int) (widths * 0.7);
        layoutParams.height = (int) (height*0.8);
        //??????????????????AlertDiaLog??????
        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//????????????????????????
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.show();

        //???????????????
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView goods_name = (TextView) dialogView.findViewById(R.id.goods_name);
        final TextView goods_price = (TextView) dialogView.findViewById(R.id.goods_price);


        final EditText goods_number = (EditText) dialogView.findViewById(R.id.new_goods_number);//????????????

        final TextView new_peibi_number = (TextView) dialogView.findViewById(R.id.new_peibi_number);//??????
        final TextView ratio_number = (TextView) dialogView.findViewById(R.id.new_ratio_number);//????????????

        final MyCustKeybords custom_keyboard = (MyCustKeybords) dialogView.findViewById(R.id.custom_keyboard);//??????


        goods_name.setText(data.getCom_name());
        goods_number.setText(data.getCom_number()+"");
        goods_price.setText(""+data.getCom_price());
        new_peibi_number.setText(data.getGoods_peibi());

        String number = data.getCom_number()+"";
        Double multiply = BigDecimalUtils.divide(number, data.getGoods_peibi() + "");
        ratio_number.setText(BigDecimalUtils.formatZero(multiply, 1));

        //??????
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



        //??????
        custom_keyboard.setListener(new MyCustKeybords.OnKeyBoradConfirm() {
            @Override
            public void onConfirm() {
                if ("".equals(goods_number.getText().toString())||goods_number.getText().toString()==null){
                    RxToast.normal("????????????????????????");
                }else {
                    data.setCom_number_state(true);
                    data.setCom_number(Integer.parseInt(goods_number.getText().toString()));

                    data.setDistribution_number(ratio_number.getText().toString());
                    String total_price=(Double.parseDouble(goods_number.getText().toString())*Double.parseDouble(data.getGoods_purchase_price()))+"";
                    data.setTotal_price(total_price);

                    /**
                     * ??????????????????
                     */
                    addUpShopping(data);
                    /**
                     * ????????????UI
                     */
                    updateGoodsView(data,position);
                    dialog.cancel();
                }

            }
        });

    }

    /**
     * ??????Edittext?????????????????????????????????????????????
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
