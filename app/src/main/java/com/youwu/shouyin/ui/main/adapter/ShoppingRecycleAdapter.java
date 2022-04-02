package com.youwu.shouyin.ui.main.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.utils_view.BigDecimalUtils;
import com.youwu.shouyin.utils_view.KeybordUtil;

import java.util.List;


public class ShoppingRecycleAdapter extends RecyclerView.Adapter<ShoppingRecycleAdapter.myViewHodler> {
    private Context context;
    private List<CommunityBean> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public ShoppingRecycleAdapter(Context context, List<CommunityBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.goodsEntityList = goodsEntityList;//实体类数据ArrayList
    }

    /**
     * 创建viewhodler，相当于listview中getview中的创建view和viewhodler
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建自定义布局
        View itemView = View.inflate(context, R.layout.item_shopping_layout, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView") final int position) {

        //根据点击位置绑定数据
        final CommunityBean data = goodsEntityList.get(position);
//        holder.mItemGoodsImg;
        holder.tv_name.setText(data.getCom_name());//获取实体类中的name字段并设置
        String price= BigDecimalUtils.formatRoundUp((Double.parseDouble(data.getCom_price())*data.getCom_number()),2)+"";

        holder.tv_price.setText(price);//获取实体类中的name字段并设置
        holder.tv_number.setText(data.getCom_number()+"");

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setCurrentIndex(position);
////                GoodsEntity goodsEntity=new GoodsEntity();
////                goodsEntity.id=data.id;
////                EventBus.getDefault().post(goodsEntity);
//            }

        holder.iv_edit_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.setCom_number(data.getCom_number()+1);
                /**
                 * 加操作
                 */
                if (mChangeListener != null) {
                    mChangeListener.onChange(data,position);
                }
            }
        });
        holder.iv_edit_subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getCom_number()==1){
                    /**
                     * 删除操作
                     */
                    if (mDeleteListener != null) {
                        mDeleteListener.onDelete(data,position);
                    }
                }else {
                    data.setCom_number(data.getCom_number()-1);
                    /**
                     * 减操作
                     */
                    if (mChangeListener != null) {
                        mChangeListener.onChange(data,position);
                    }
                }

            }
        });
        //添加editText的监听事件
//        holder.tv_number.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) { }
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                if (!"".equals(s.toString())){
//                    data.setCom_number(Integer.parseInt(s.toString()));
//                    /**
//                     * 加操作
//                     */
//                    if (mChangeListener != null) {
//                        mChangeListener.onChange(data,position);
//                    }
//                }
//
//            }
//        });


    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    /**
     * 得到总条数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return goodsEntityList.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView tv_name,tv_price;//商品名称，商品价格
        private TextView tv_number;//商品数量

        private ImageView iv_edit_add,iv_edit_subtract;//加  减




        public myViewHodler(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            iv_edit_add = (ImageView) itemView.findViewById(R.id.iv_edit_add);
            iv_edit_subtract = (ImageView) itemView.findViewById(R.id.iv_edit_subtract);


            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, goodsEntityList.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });


        }

    }


    //加减的监听的回调
    public interface OnChangeListener {
        void onChange(CommunityBean lists,int position);
    }

    public void setOnChangeListener(OnChangeListener listener) {
        mChangeListener = listener;
    }

    private OnChangeListener mChangeListener;

    //删除的监听的回调
    public interface OnDeleteListener {
        void onDelete(CommunityBean lists,int position);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;



    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {
        /**
         * 接口中的点击每一项的实现方法，参数自己定义
         *
         * @param view 点击的item的视图
         * @param data 点击的item的数据
         */
        public void OnItemClick(View view, CommunityBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}