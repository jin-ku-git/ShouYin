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
 * ??????
 * 2022/03/21
 */

public class DemoActivity extends BaseActivity<ActivityDemoBinding, DemoViewModel> implements ScanUtils.OnResultListener{

    //??????recyclerveiw????????????
    private CommunityRecycleAdapter mCommunityRecycleAdapter;
    //?????????CommunityEntityList?????????????????????????????????
    private ArrayList<String> CommunityEntityList = new ArrayList<>();

    //??????recyclerveiw????????????
    private CommunityListRecycleAdapter mCabinetListRecycleAdapter;
    //?????????CabinetEntityList?????????????????????????????????
    private ArrayList<CommunityBean> CabinetEntityList = new ArrayList<CommunityBean>();

    //?????????recyclerveiw????????????
    private ShoppingRecycleAdapter mShoppingRecycleAdapter;
    //?????????goodsentity?????????????????????????????????
    private ArrayList<CommunityBean> ShoppingEntityList = new ArrayList<CommunityBean>();

    //????????????recyclerveiw????????????
    private SouSuoListRecycleAdapter mSouSuoListRecycleAdapter;
    //?????????CabinetEntityList?????????????????????????????????
    private ArrayList<CommunityBean> SouSuoEntityList = new ArrayList<>();

    //?????????recyclerveiw????????????
    private CouponListRecycleAdapter mCouponListRecycleAdapter;
    //?????????CabinetEntityList?????????????????????????????????
    private ArrayList<CouponBean> CouponEntityList = new ArrayList<>();

    ArrayList<CommunityBean> SouSuoList = new ArrayList<CommunityBean>();

    Intent intent;

    private CouponBean couponBean;//??????????????????


    @Override
    public void initParam() {
        super.initParam();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public DemoViewModel initViewModel() {
        //??????????????????ViewModelFactory?????????ViewModel????????????????????????????????????????????????LoginViewModel(@NonNull Application application)????????????
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
                    case 2://????????????
                        startActivity(AddVipActivity.class);
                        break;
                    case 3://?????????
                        startActivity(HandoverActivity.class);
                        break;
                    case 4://????????????
                        startActivity(SalesDocumentActivity.class);
                        break;
                    case 5://????????????
                        startActivity(OrderGoodsActivity.class);
                        break;
                    case 6://????????????
                        showFunctionDialog();
                        break;

                    case 7://?????????????????????
                        intent = new Intent(DemoActivity.this, SouSuoVipActivity.class);
                        intent.putExtra("type",1);
                        startActivityForResult(intent,200);
                        break;
                    case 8://????????????
                        Animation topAnim = AnimationUtils.loadAnimation(
                                DemoActivity.this, R.anim.activity_down_in);
                        //????????????
                        binding.sousuoLayout.startAnimation(topAnim);
                        break;
                    case 9://????????????
                            Animation topAnimTow = AnimationUtils.loadAnimation(
                                    DemoActivity.this, R.anim.activity_down_out);
                            //????????????
                            binding.sousuoLayout.startAnimation(topAnimTow);
                        break;
                    case 10://???????????????
                        ShoppingEntityList.clear();
                        for (int i=0;i<CabinetEntityList.size();i++){
                            CabinetEntityList.get(i).setCom_number(1);
                            CabinetEntityList.get(i).setCom_number_state(false);
                        }
                        initRecyclerViewTow();
                        //??????UI
                        updateView();
                        break;

                    case 11://???????????????

                        showCouponDialog();
                        break;
                    case 12://??????vip??????
                             intent = new Intent(DemoActivity.this, SouSuoVipActivity.class);
                            intent.putExtra("type",2);
                            startActivityForResult(intent,200);
                        break;
                    case 13://???????????????
                        if (ShoppingEntityList.size()==0){
                            RxToast.normal("???????????????!");
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

        // ???????????????????????????????????????????????????
        setSwipeBackEnable(false);
        viewModel.sou_suo_bool.set(false);//?????????????????????
        viewModel.vip_bool.set(false);//?????????????????????
        /**
         * ????????????
         */
        initCommunit();

        /**
         * ?????????????????????
         */
        initCouPonList();


        //??????editText???????????????
        binding.souSuoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {

                KLog.d("SouSuoList------???"+SouSuoList.size());
                SouSuoEntityList.clear();
                KLog.d("SouSuoList------2222???"+SouSuoList.size());
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
     * ?????????????????????
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 200://?????????????????????????????????
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
                data.setCom_number_state(true);
                /**
                 * ????????????UI
                 */
                updateGoodsView(data,position);
                /**
                 * ??????
                 */
                addShopping(data);

            }
        });
        mCabinetListRecycleAdapter.setOnPopupListener(new CommunityListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data,int position) {
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
        mShoppingRecycleAdapter = new ShoppingRecycleAdapter(this, ShoppingEntityList);
        //???RecyclerView??????adapter
        binding.shoppingCartRecycler.setAdapter(mShoppingRecycleAdapter);
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
        mShoppingRecycleAdapter.setOnChangeListener(new ShoppingRecycleAdapter.OnChangeListener() {
            @Override
            public void onChange(CommunityBean data,int position) {
                updateShopping(data,position);

                /**
                 * ????????????UI
                 */
                updateGoodsView(data,data.getPosition());
            }
        });
        /**
         * ??????
         */
        mShoppingRecycleAdapter.setOnDeleteListener(new ShoppingRecycleAdapter.OnDeleteListener() {
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
        mSouSuoListRecycleAdapter = new SouSuoListRecycleAdapter(this, SouSuoEntityList);
        //???RecyclerView??????adapter
        binding.sousuoRecycler.setAdapter(mSouSuoListRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        binding.sousuoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //??????item????????????
        if (binding.sousuoRecycler.getItemDecorationCount()==0) {
            binding.sousuoRecycler.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mSouSuoListRecycleAdapter.setOnAddShopping(new SouSuoListRecycleAdapter.OnAddShopping() {
            @Override
            public void onAddShopping(CommunityBean data, int position) {
                data.setCom_number_state(true);
                /**
                 * ????????????UI
                 */
                updateGoodsView(data,position);
                /**
                 * ??????
                 */
                addShopping(data);
            }
        });
        mSouSuoListRecycleAdapter.setOnPopupListener(new SouSuoListRecycleAdapter.OnPopupListener() {
            @Override
            public void onPopup(CommunityBean data,int position) {
                data.setCom_number(1);
                /**
                 * ??????????????????
                 */
                showDialog(data,position);
            }
        });

    }



    /**
     * ??????????????????  +1
     * @param data
     */
    private void addShopping(CommunityBean data) {
        int hasInShopCar = -1;
        for (int i=0;i<ShoppingEntityList.size();i++){
            if (data.getId()==ShoppingEntityList.get(i).getId()){
                //?????????????????????????????????
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
        //??????UI
        updateView();
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

            binding.shoppingNum.setText(num+"");
        }
        initRecyclerViewThree();
        //????????????
        countMoney();

    }

    /**
     * ????????????
     */
    private void countMoney() {
        Double total_price=0.00;//??????
        Double discount_price=0.00;//????????????

        if (couponBean!=null){//???????????????????????????

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long end = 0L;
            Long start = 0L;
            try {
                end = sf.parse((couponBean.getEndTime()+" 23:59:59")).getTime();// ????????????????????????
                start = sf.parse((couponBean.getStartTime()+" 00:00:00")).getTime();// ????????????????????????
            } catch (ParseException e) {
                e.printStackTrace();
            }
            KLog.d("???????????????"+end);
            KLog.d("???????????????"+start);
            KLog.d("???????????????"+System.currentTimeMillis());

            if (System.currentTimeMillis() > end) {
                RxToast.normal("????????????????????????");
                couponBean = null;
                for (int i=0;i<ShoppingEntityList.size();i++){
                    if (ShoppingEntityList.get(i).getCom_discount_price()!=null){
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();
                        discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                    }
                }

            } else if (System.currentTimeMillis() < start) {
                RxToast.normal("??????????????????????????????");
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
                        //???????????????-?????????????????????*???????????????
                        total_price+=(Double.parseDouble(ShoppingEntityList.get(i).getCom_price())-Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price()))*ShoppingEntityList.get(i).getCom_number();

                        discount_price+=Double.parseDouble(ShoppingEntityList.get(i).getCom_discount_price())*ShoppingEntityList.get(i).getCom_number();
                    }
                }
                //?????????????????????
                if (total_price-couponBean.getCou_money()<0.0){
                    //??????total_price??????
                    discount_price=discount_price+total_price;
                    total_price=0.0;
                }else {
                    total_price=total_price-couponBean.getCou_money();
                    //?????????????????????
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
     * ????????????
     * @param data
     */
    private void updateShopping(CommunityBean data,int position) {
        ShoppingEntityList.set(position,data);
        //??????UI
        updateView();
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
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_good_details, null);
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
        dialog.show();

        //???????????????
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final TextView goods_name = (TextView) dialogView.findViewById(R.id.goods_name);
        TextView goods_number = (TextView) dialogView.findViewById(R.id.goods_number);
        final TextView goods_price = (TextView) dialogView.findViewById(R.id.goods_price);
        ImageView iv_edit_subtract = (ImageView) dialogView.findViewById(R.id.iv_edit_subtract);//???
        ImageView iv_edit_add = (ImageView) dialogView.findViewById(R.id.iv_edit_add);//???
        TextView add_shopping = (TextView) dialogView.findViewById(R.id.add_shopping);//???????????????
        final EditText goods_quantity = (EditText) dialogView.findViewById(R.id.goods_quantity);//????????????
        final LinearLayout layout_view = (LinearLayout) dialogView.findViewById(R.id.layout_view);
        final MyCustKeybords custom_keyboard = (MyCustKeybords) dialogView.findViewById(R.id.custom_keyboard);//??????


        goods_name.setText(data.getCom_name());
        goods_number.setText("???????????????"+data.getGoods_number());
        goods_price.setText(""+data.getCom_price());
        goods_quantity.setText(data.getCom_number()+"");
        //??????
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        //???
        iv_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number(data.getCom_number()+1);
                goods_quantity.setText(data.getCom_number()+"");
                goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
            }
        });
        //???
        iv_edit_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCom_number()==1){
                    RxToast.normal("??????????????????");
                }else {
                    data.setCom_number(data.getCom_number()-1);
                    goods_quantity.setText(data.getCom_number()+"");
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }

            }
        });
        //????????????
        layout_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(goods_quantity);
            }
        });

        //??????editText???????????????
        goods_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())||s.toString()==null){
                    RxToast.normal("??????????????????");
                    goods_quantity.setText("1");
                    data.setCom_number(1);
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }else {
                    data.setCom_number(Integer.parseInt(s.toString()));
                    goods_price.setText(""+BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2));
                }

            }
        });
        //???????????????
        add_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number_state(true);
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
//                    RxToast.normal("??????");
//
//            }
//        });

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

    /**
     * ??????????????????
     */
    private void showFunctionDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //??????????????????
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //????????????
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_function, null);
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
        dialog.show();

        //???????????????
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);
        final Button button_1 = (Button) dialogView.findViewById(R.id.button_1);
        final Button button_2 = (Button) dialogView.findViewById(R.id.button_2);
        final Button button_3 = (Button) dialogView.findViewById(R.id.button_3);
        //??????
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //??????????????????
        button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RechargeRecordActivity.class);

            }
        });
        //??????
        button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //???????????????
                startActivity(SetUpActivity.class);
//                RxToast.normal("??????");
            }
        });
        //????????????
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxToast.normal("????????????");
            }
        });

    }

    private RecyclerView coupon_recycler;

    /**
     * ???????????????
     */
    private void showCouponDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //??????????????????
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;

        //????????????
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_coupon, null);
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
        dialog.show();

        //???????????????
        TextView close_text = (TextView) dialogView.findViewById(R.id.close_text);//??????
        TextView use_coupon = (TextView) dialogView.findViewById(R.id.use_coupon);//?????????????????????
        coupon_recycler = (RecyclerView) dialogView.findViewById(R.id.coupon_recycler);

        initRecyclerViewFive();
        //??????
        close_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //?????????????????????
        use_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countMoney();

                dialog.dismiss();
//                RxToast.normal("???????????????");
            }
        });

    }

    /**
     * ?????????????????????
     */
    private void initCouPonList() {
        if (CouponEntityList.size()!=0){
            CouponEntityList.clear();
        }
        for (int i=0;i<10;i++){
            CouponBean couponBean=new CouponBean();
            couponBean.setName("????????????????????????"+i);
            couponBean.setCou_money(Double.parseDouble("1"+i));
            couponBean.setStartTime("2022-03-24");
            couponBean.setEndTime("2022-03-25");
            CouponEntityList.add(couponBean);
        }

    }

    /**
     * ???????????????
     */
    private void initRecyclerViewFive() {
        //??????adapter
        mCouponListRecycleAdapter = new CouponListRecycleAdapter(this, CouponEntityList);
        //???RecyclerView??????adapter
        coupon_recycler.setAdapter(mCouponListRecycleAdapter);
        //??????layoutManager,?????????????????????????????????????????????grid??????????????????????????????

        //???????????????????????????????????????????????????????????????????????????
        coupon_recycler.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        //??????item????????????
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

            //??????????????????
            ScanUtils.getInstance().setOnResultListener(DemoActivity.this);
            ScanUtils.getInstance().analysisKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }


    //????????????????????????
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

        RxToast.normal("???????????????"+resultStr);

    }

    //????????????long???????????????????????????????????????????????????????????????
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //??????????????????????????????????????????
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            //????????????????????????????????????
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //??????2000ms??????????????????????????????Toast????????????

                View toastRoot = getLayoutInflater().inflate(R.layout.my_toast, null);
                Toast toast = new Toast(this);
                toast.setView(toastRoot);
                TextView tv = (TextView) toastRoot.findViewById(R.id.TextViewInfo);
                tv.setText("????????????????????????");
                toast.setGravity(Gravity.BOTTOM, 0, 150);
                toast.show();
                //???????????????????????????????????????????????????????????????????????????
                mExitTime = System.currentTimeMillis();
            } else {
                //??????2000ms??????????????????????????????????????????-??????System.exit()??????????????????
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
