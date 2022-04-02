package com.youwu.shouyin.ui.money.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.ui.money.bean.RechargeBean;
import com.youwu.shouyin.ui.money.bean.SaleBillBean;

import java.util.List;

/**
 * 销售单据适配器
 */
public class SaleBillAdapter extends RecyclerView.Adapter<SaleBillAdapter.myViewHodler> {
    private Context context;
    private List<SaleBillBean> rechargeBeans;
    private int currentIndex = 0;

    //创建构造函数
    public SaleBillAdapter(Context context, List<SaleBillBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_sale_bill_layout, null);
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
        SaleBillBean data = rechargeBeans.get(position);

        holder.bindData(rechargeBeans.get(position), position, currentIndex);

        holder.order_id.setText("订单编号："+data.getOrder_sn());//获取实体类中的name字段并设置
        holder.order_time.setText("订单时间："+data.getCreate_time());//会员信息
        holder.order_prick.setText(data.getPaid_in_prick());//订单金额

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrentIndex(position);

                if(mClickListener!=null){
                    mClickListener.onClick(rechargeBeans.get(position),position);
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
        return rechargeBeans.size();
    }

    //自定义viewhodler
    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView order_id,order_time;//订单编号，订单时间
        private TextView order_prick,rmb;//订单金额
        private View mView;

        public myViewHodler(View itemView) {
            super(itemView);

            order_id = (TextView) itemView.findViewById(R.id.order_id);
            order_time = (TextView) itemView.findViewById(R.id.order_time);
            order_prick = (TextView) itemView.findViewById(R.id.order_prick);
            rmb = (TextView) itemView.findViewById(R.id.rmb);
            mView = (View) itemView.findViewById(R.id.view);



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

        public void bindData(SaleBillBean saleBillBean, int position, int currentIndex) {
            if (position == currentIndex) {

                mView.setVisibility(View.VISIBLE);
                order_prick.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_red));
                rmb.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.color_red));
            } else {

                rmb.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_black_85));
                order_prick.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_black_85));
                mView.setVisibility(View.GONE);
            }
//            mItemGoodsName.setText(();
        }

    }


    //删除的监听的回调
    public interface OnClickListener {
        void onClick(SaleBillBean lists, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    private OnClickListener mClickListener;



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
        public void OnItemClick(View view, SaleBillBean data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}