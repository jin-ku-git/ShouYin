package com.youwu.shouyin.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.utils_view.CustomRoundAngleImageView;

import java.util.List;


public class CommunityListRecycleAdapter extends RecyclerView.Adapter<CommunityListRecycleAdapter.myViewHodler> {
    private Context context;
    private List<CommunityBean> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public CommunityListRecycleAdapter(Context context, List<CommunityBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_community_layout, null);
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
        final CommunityBean data = goodsEntityList.get(position);
//        holder.mItemGoodsImg;
        holder.item_commodity_name.setText(data.getCom_name());//获取实体类中的name字段并设置
        holder.com_price.setText(data.getCom_price());//获取实体类中的name字段并设置

        if (data.isCom_number_state()){
            holder.shopping_num.setVisibility(View.VISIBLE);
        }else {
            holder.shopping_num.setVisibility(View.GONE);
        }
        holder.shopping_num.setText(data.getCom_number()+"");



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

        private TextView item_commodity_name,com_price;//商品名称,商品价格
        private TextView shopping_num;//加入到购物车的数量
        private CustomRoundAngleImageView iv_commodity_img;//商品图片



        public myViewHodler(View itemView) {
            super(itemView);

            item_commodity_name = (TextView) itemView.findViewById(R.id.item_commodity_name);
            com_price = (TextView) itemView.findViewById(R.id.com_price);
            shopping_num = (TextView) itemView.findViewById(R.id.shopping_num);
            iv_commodity_img = (CustomRoundAngleImageView) itemView.findViewById(R.id.iv_commodity_img);





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


    //图片的监听的回调
    public interface OnPopupListener {
        void onPopup(CommunityBean lists,int position);
    }

    public void setOnPopupListener(OnPopupListener listener) {
        mPopupListener = listener;
    }

    private OnPopupListener mPopupListener;



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