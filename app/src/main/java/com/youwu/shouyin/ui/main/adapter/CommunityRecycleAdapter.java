package com.youwu.shouyin.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.youwu.shouyin.R;

import java.util.List;


public class CommunityRecycleAdapter extends RecyclerView.Adapter<CommunityRecycleAdapter.myViewHodler> {
    private Context context;
    private List<String> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public CommunityRecycleAdapter(Context context, List<String> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_layout, null);
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
//        StoreBean.DataBean data = goodsEntityList.get(position);
//        holder.mItemGoodsImg;
        holder.text_name.setText(goodsEntityList.get(position));//获取实体类中的name字段并设置
        holder.bindData(goodsEntityList.get(position), position, currentIndex);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentIndex(position);

                /**
                 * 点击操作
                 */
                if (mScanningListener != null) {
                    mScanningListener.onScanning(goodsEntityList.get(position));
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
        private View mView;
        private TextView text_name;
        private RelativeLayout mLayout_all;



        public myViewHodler(View itemView) {
            super(itemView);

            text_name = (TextView) itemView.findViewById(R.id.text_name);

            mView=(View) itemView.findViewById(R.id.view);
            mLayout_all=(RelativeLayout) itemView.findViewById(R.id.layout_all);


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


        public void bindData(String goodsEntity, int position, int currentIndex) {
            if (position == currentIndex) {
                mLayout_all.setBackgroundResource(R.drawable.radius_white_right_10dp);

                mView.setVisibility(View.VISIBLE);
                text_name.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.main_color));
            } else {
                mLayout_all.setBackgroundResource(R.color.main_touming);
                text_name.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.text_39));
                mView.setVisibility(View.GONE);
            }
//            mItemGoodsName.setText(();
        }
    }

    //回调
    public interface OnScanningListener {
        void onScanning(String communityBean);
    }

    public void setOnScanningListener(OnScanningListener listener) {
        mScanningListener = listener;
    }

    private OnScanningListener mScanningListener;



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
        public void OnItemClick(View view, String data, int position);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}