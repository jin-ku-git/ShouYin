package com.youwu.shouyin.ui.order_goods.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.money.bean.SaleBillBean;

import java.util.List;

/**
 * 订货申请适配器
 */
public class ApplyGoodsAdapter extends RecyclerView.Adapter<ApplyGoodsAdapter.myViewHodler> {
    private Context context;
    private List<SaleBillBean.SaleBean> rechargeBeans;
    private int currentIndex = 0;

    //创建构造函数
    public ApplyGoodsAdapter(Context context, List<SaleBillBean.SaleBean> goodsEntityList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.rechargeBeans = goodsEntityList;//实体类数据ArrayList
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
        View itemView = View.inflate(context, R.layout.item_apply_goods_layout, null);
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
        SaleBillBean.SaleBean data = rechargeBeans.get(position);
//        holder.mItemGoodsImg;
        holder.goods_name.setText(data.getGoods_name());//商品名称
        holder.goods_number.setText(data.getGoods_number());//商品数量
        holder.goods_price.setText(data.getGoods_price());//商品单价
        holder.total_price.setText(data.getTotal_price());//小计



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
        return rechargeBeans.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView goods_name,goods_number;//商品名称，订货数量
        private TextView goods_price;//订货单价
        private TextView total_price;//小计





        public myViewHodler(View itemView) {
            super(itemView);

            goods_name = (TextView) itemView.findViewById(R.id.goods_name);
            goods_number = (TextView) itemView.findViewById(R.id.goods_number);
            goods_price = (TextView) itemView.findViewById(R.id.goods_price);
            total_price = (TextView) itemView.findViewById(R.id.total_price);




            //点击事件放在adapter中使用，也可以写个接口在activity中调用
            //方法一：在adapter中设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //可以选择直接在本位置直接写业务处理
                    //Toast.makeText(context,"点击了xxx",Toast.LENGTH_SHORT).show();
                    //此处回传点击监听事件
                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, rechargeBeans.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });


        }

    }


    //删除的监听的回调
    public interface OnDeleteListener {
        void onDelete(SaleBillBean.SaleBean lists, int position);
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
        public void OnItemClick(View view, SaleBillBean.SaleBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}