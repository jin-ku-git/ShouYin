package com.youwu.shouyin.ui.main.bean;

import java.io.Serializable;

/**
 * 优惠券表
 */
public class CouponBean implements Serializable {


    private String image;
    private String coupon_id;
    private String name;//优惠券名称
    private String startTime;//开始时间
    private String endTime;//结束事件
    private String type;//优惠券类型
    private boolean select = false;
    private double cou_money;//优惠金额


    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(String coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCou_money() {
        return cou_money;
    }

    public void setCou_money(double cou_money) {
        this.cou_money = cou_money;
    }
}
