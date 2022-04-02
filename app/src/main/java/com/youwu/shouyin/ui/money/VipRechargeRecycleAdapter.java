package com.youwu.shouyin.ui.money;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CouponBean;
import com.youwu.shouyin.ui.money.bean.VipRechargeBean;

import java.util.List;


public class VipRechargeRecycleAdapter extends RecyclerView.Adapter<VipRechargeRecycleAdapter.myViewHodler> {
    private Context context;
    private List<VipRechargeBean> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public VipRechargeRecycleAdapter(Context context, List<VipRechargeBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_vip_recharge_layout, null);
        return new myViewHodler(itemView);
    }

    /**
     * 绑定数据，数据与view绑定
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(myViewHodler holder, @SuppressLint("RecyclerView") final int position) {

        //根据点击位置绑定数据
        final VipRechargeBean data = goodsEntityList.get(position);
        holder.bindData(goodsEntityList.get(position), position, currentIndex);

        holder.recharge_content.setText(data.getRecharge_content());//获取实体类中的name字段并设置
        holder.recharge_price.setText(data.getRecharge_prick()+"");
        if ("".equals(data.getRecharge_content())){
            holder.bottom_layout.setVisibility(View.GONE);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentIndex(position);

                /**
                 * 选择的优惠券
                 */
                if (mCouPonListener!=null){
                    mCouPonListener.onCouPon(data,position);
                }

            }
        });

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

        private TextView recharge_content;//
        private TextView recharge_price;//充值金额
        private RelativeLayout bottom_layout,relative_layout;

        private TextView rmb;



        public myViewHodler(View itemView) {
            super(itemView);


            recharge_price = (TextView) itemView.findViewById(R.id.recharge_price);
            rmb = (TextView) itemView.findViewById(R.id.rmb);
            recharge_content = (TextView) itemView.findViewById(R.id.recharge_content);
            bottom_layout = (RelativeLayout) itemView.findViewById(R.id.bottom_layout);
            relative_layout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);


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
        public void bindData(VipRechargeBean data, int position, int currentIndex) {
            if (position == currentIndex) {
                rmb.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_color));
                recharge_price.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_color));
                bottom_layout.setBackgroundResource(R.drawable.radius_blue_bottom_5dp);
                relative_layout.setBackgroundResource(R.drawable.radius_white_blue_5dp);
            } else {
                rmb.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_black_85));
                recharge_price.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_black_85));
                relative_layout.setBackgroundResource(R.drawable.radius_white_5dp);
                bottom_layout.setBackgroundResource(R.drawable.radius_hui_bottom_5dp);
            }
        }
    }

    //图片的监听的回调
    public interface OnCouPonListener {
        void onCouPon(VipRechargeBean data, int position);
    }

    public void setOnCouPonListener(OnCouPonListener listener) {
        mCouPonListener = listener;
    }

    private OnCouPonListener mCouPonListener;



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
        public void OnItemClick(View view, VipRechargeBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}