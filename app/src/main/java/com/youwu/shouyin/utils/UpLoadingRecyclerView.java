package com.youwu.shouyin.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.SOURCE;

public class UpLoadingRecyclerView extends RecyclerView {
    private boolean isScroll;
    private float downY;
    private int mHeightPixels;
    private OnUpScrollListener mOnUpScrollListener;
    private OnUpPullLoadListener mOnUpPullLoadListener;

    public UpLoadingRecyclerView(@NonNull Context context) {
        super(context);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mHeightPixels = displayMetrics.heightPixels;
    }

    public UpLoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mHeightPixels = displayMetrics.heightPixels;
    }

    public UpLoadingRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        mHeightPixels = displayMetrics.heightPixels;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重写拦截的目的是只拦截移动事件,不拦截其他触摸事件
     *
     * @param e
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        super.onInterceptTouchEvent(e);//一定要super.onInterceptTouchEvent(e);不要让RecyclerView的其他事件分发受到影响，否则会出现不滚动的问题
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isScroll = false;//按下不拦截
                downY = e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(downY - e.getY()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {//判断是否产生了最小滑动
                    isScroll = true;//移动了拦截触摸事件
                } else {
                    isScroll = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isScroll = false;//松开不拦截
                break;
        }

        return isScroll;
    }


    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {
        return super.onFilterTouchEventForSecurity(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (downY - e.getY() > mHeightPixels / 5) {//判断是不是从下往上滑动了屏幕的5分之一
                    if (mOnUpScrollListener != null) {
                        mOnUpScrollListener.onScroll();
                    }
                    if (getAdapter() != null && mOnUpPullLoadListener != null && getAdapter() instanceof Adapter) {
                        Adapter adapter = (Adapter) getAdapter();
                        if (adapter.isFooterPullUpLoading() && adapter.isFooterItemPosition()) {
                            mOnUpPullLoadListener.OnUpPullLoad();
                        }
                    }

                }
                break;

        }
        return super.onTouchEvent(e);
    }

    public void setOnUpScrollListener(OnUpScrollListener listener) {
        this.mOnUpScrollListener = listener;

    }

    public void setOnUpPullLoadListener(OnUpPullLoadListener onUpPullLoadListener) {
        this.mOnUpPullLoadListener = onUpPullLoadListener;
    }

    public interface OnUpScrollListener {
        void onScroll();

    }

    public interface OnUpPullLoadListener {
        void OnUpPullLoad();
    }

    public static abstract class Adapter<VH extends ViewHolder> extends RecyclerView.Adapter {
        private static final int FOOTER_VIEW = -101;
        private int currentFooterStatus = FooterStatus.FOOTER_NO_MORE_DATA;  //当前页角item状态
        private int scrollPosition;                                          //滚动位置


        public abstract ViewHolder onCreateItemViewHolder(@NonNull ViewGroup parent, int viewType);

        public abstract ViewHolder onCreateFooterViewHolder(@NonNull ViewGroup parent, int viewType);

        public abstract void onBindItemViewHolder(@NonNull ViewHolder holder, int position);

        public abstract void onBindFooterViewHolder(@NonNull ViewHolder holder, @FooterStatus int currentFooterStatus);

        public abstract int getItemViewCount();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType != FOOTER_VIEW) {
                return onCreateItemViewHolder(parent, viewType);
            }
            return onCreateFooterViewHolder(parent, viewType);
        }


        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            scrollPosition = position;
            if (getItemViewType(position) != FOOTER_VIEW) {
                onBindItemViewHolder(holder, position);
                return;
            }
            onBindFooterViewHolder(holder, currentFooterStatus);
        }

        /**
         * 判断是否到了页尾的位置上
         *
         * @return
         */
        public boolean isFooterItemPosition() {
            return scrollPosition >= getItemCount() - 5;
        }

        @Override
        public int getItemViewType(int position) {
            if (getItemCount() != 0 && position == getItemCount() - 1) {
                return FOOTER_VIEW;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return getItemViewCount() == 0 ? 0 : getItemViewCount() + 1;
        }

        /**
         * 修改页脚状态为加载中
         */
        public void setFooterLoading() {
            currentFooterStatus = FooterStatus.FOOTER_LOADING;
            notifyDataSetChanged();
        }

        /**
         * 修改页脚状态为网络异常
         */
        public void setFooterNetWorkError() {
            currentFooterStatus = FooterStatus.FOOTER_ERROR;
            notifyDataSetChanged();
        }

        /**
         * 修改页脚状态为没有更多数据
         */
        public void setFooterNoMoreData() {
            currentFooterStatus = FooterStatus.FOOTER_NO_MORE_DATA;
            notifyDataSetChanged();
        }

        /**
         * 修改页脚状态为上拉加载数据
         */
        public void setFooterPullUpLoading() {
            currentFooterStatus = FooterStatus.FOOTER_PULL_UP_LOADING;
            notifyDataSetChanged();
        }

        public boolean isFooterLoading() {
            return currentFooterStatus == FooterStatus.FOOTER_LOADING;
        }

        public boolean isFooterNetWorkError() {
            return currentFooterStatus == FooterStatus.FOOTER_ERROR;
        }

        public boolean isFooterNoMoreData() {
            return currentFooterStatus == FooterStatus.FOOTER_NO_MORE_DATA;
        }

        public boolean isFooterPullUpLoading() {
            return currentFooterStatus == FooterStatus.FOOTER_PULL_UP_LOADING;
        }

        /**
         * 创建使用简单页尾
         *
         * @param parent
         * @param viewType
         * @param textSize
         * @param textColor
         * @return
         */
        public SimpleFooterViewHolder createSimpleFooterView(@NonNull ViewGroup parent, int viewType, float textSize, @ColorInt int textColor) {
            if (viewType == Adapter.FOOTER_VIEW) {
                final TextView textView = new TextView(parent.getContext());
                if(textSize != 0){
                    textView.setTextSize(textSize);
                }
                if (textColor != 0){
                    textView.setTextColor(textColor);
                }
                textView.setGravity(Gravity.CENTER);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        textView.setLayoutParams(layoutParams);

                    }
                });
                return new SimpleFooterViewHolder(textView);

            }
            return null;
        }

        /**
         * 绑定简单页尾的ViewHolder
         *
         * @param holder
         * @param pullUpLoadingText  提示 上拉加载数据
         * @param loadingText        提示 数据加载中
         * @param errorText          提示 网络异常
         * @param notMoreDataText    提示 没有更多数据了
         */
        public void bindSimpleFooterViewHolder(@NonNull ViewHolder holder, @StringRes int pullUpLoadingText, @StringRes int loadingText, @StringRes int errorText, @StringRes int notMoreDataText) {
            if (holder instanceof SimpleFooterViewHolder) {
                switch (currentFooterStatus) {
                    case FooterStatus.FOOTER_PULL_UP_LOADING:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(pullUpLoadingText);
                        break;
                    case FooterStatus.FOOTER_LOADING:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(loadingText);
                        break;
                    case FooterStatus.FOOTER_ERROR:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(errorText);
                        break;
                    case FooterStatus.FOOTER_NO_MORE_DATA:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(notMoreDataText);
                        break;
                    default:
                        break;
                }
            }
        }

        /**
         * 绑定简单页尾的ViewHolder
         *
         * @param holder
         * @param pullUpLoadingText 提示 上拉加载数据
         * @param loadingText       提示 数据加载中
         * @param errorText         提示 网络异常
         * @param notMoreDataText   提示 没有更多数据了
         */
        public void bindSimpleFooterViewHolder(@NonNull ViewHolder holder, String pullUpLoadingText, String loadingText, String errorText, String notMoreDataText) {
            if (holder instanceof SimpleFooterViewHolder) {
                switch (currentFooterStatus) {
                    case FooterStatus.FOOTER_PULL_UP_LOADING:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(pullUpLoadingText);
                        break;
                    case FooterStatus.FOOTER_LOADING:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(loadingText);
                        break;
                    case FooterStatus.FOOTER_ERROR:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(errorText);
                        break;
                    case FooterStatus.FOOTER_NO_MORE_DATA:
                        ((SimpleFooterViewHolder) holder).footerTextView.setText(notMoreDataText);
                        break;
                    default:
                        break;
                }
            }
        }

        public static class SimpleFooterViewHolder extends ViewHolder {
            TextView footerTextView;

            public SimpleFooterViewHolder(@NonNull View itemView) {
                super(itemView);
                footerTextView = (TextView) itemView;
            }
        }

    }

    @Retention(SOURCE)
    @Target({PARAMETER})
    @IntDef(value = {FooterStatus.FOOTER_PULL_UP_LOADING, FooterStatus.FOOTER_LOADING, FooterStatus.FOOTER_ERROR, FooterStatus.FOOTER_NO_MORE_DATA})
    public @interface FooterStatus {
        int FOOTER_PULL_UP_LOADING = 0;             //提示上拉加载更多
        int FOOTER_LOADING = 1;                     //加载中
        int FOOTER_ERROR = 2;                       //网络异常
        int FOOTER_NO_MORE_DATA = 3;                //没有更多内容
    }
}