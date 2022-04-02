package com.youwu.shouyin.ui.order_goods.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.youwu.shouyin.R;
import com.youwu.shouyin.ui.main.bean.CommunityBean;
import com.youwu.shouyin.utils_view.CustomRoundAngleImageView;
import com.youwu.shouyin.utils_view.FindUtil;

import java.util.List;

import me.goldze.mvvmhabit.utils.KLog;

import static com.xuexiang.xui.utils.ResUtils.getResources;


public class SouSuoNewOrderRecycleAdapter extends RecyclerView.Adapter<SouSuoNewOrderRecycleAdapter.myViewHodler> {
    private Context context;
    private List<CommunityBean> goodsEntityList;
    private int currentIndex = 0;

    //创建构造函数
    public SouSuoNewOrderRecycleAdapter(Context context, List<CommunityBean> goodsEntityList) {
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
        View itemView = View.inflate(context, R.layout.item_sousuo_new_order_layout, null);
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

        KLog.d("Sou_suo_text:"+data.getSou_suo_text());
        KLog.d("getCom_name:"+data.getCom_name());
        if ("".equals(data.getSou_suo_text())||data.getSou_suo_text()==null){
            holder.item_sousuo_name.setText(data.getCom_name());
        }else {

            holder.item_sousuo_name.setText(FindUtil.findSearch(getResources().getColor(R.color.main_color), data.getCom_name(), data.getSou_suo_text()));
        }

        holder.com_price.setText(data.getCom_price());//获取实体类中的name字段并设置

//        holder.add_shopping.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * 添加点击事件
//                 */
//                if(mAddShopping!=null){
//                    mAddShopping.onAddShopping(data,position);
//                }
//            }
//        });
//
//
//        holder.iv_commodity_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /**
//                 * 图片点击事件
//                 */
//                if(mPopupListener!=null){
//                    mPopupListener.onPopup(data,position);
//                }
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

        private TextView item_sousuo_name,com_price;//商品名称,商品价格
        private Button add_shopping;//添加商品

        private CustomRoundAngleImageView iv_commodity_img;//商品图片



        public myViewHodler(View itemView) {
            super(itemView);

            item_sousuo_name = (TextView) itemView.findViewById(R.id.item_sousuo_name);
            com_price = (TextView) itemView.findViewById(R.id.com_price);
            add_shopping = (Button) itemView.findViewById(R.id.add_shopping);

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
        void onPopup(CommunityBean lists, int position);
    }

    public void setOnPopupListener(OnPopupListener listener) {
        mPopupListener = listener;
    }

    private OnPopupListener mPopupListener;


    //图片的监听的回调
    public interface OnAddShopping {
        void onAddShopping(CommunityBean lists, int position);
    }

    public void setOnAddShopping(OnAddShopping listener) {
        mAddShopping = listener;
    }

    private OnAddShopping mAddShopping;



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