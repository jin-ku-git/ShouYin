package com.youwu.shouyin.ui.money.bean;

import java.io.Serializable;

public class RechargeBean implements Serializable {
    private int id;//商品id
    private String cz_name;//收银员
    private String cz_money;//充值金额
    private String vip_details;//会员信息
    private String coupon_num;//获赠优惠券/张
    private String cz_mode;//充值方式
    private String cz_time;//充值时间
    private String serial_number;//编号


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCz_name() {
        return cz_name;
    }

    public void setCz_name(String cz_name) {
        this.cz_name = cz_name;
    }

    public String getCz_money() {
        return cz_money;
    }

    public void setCz_money(String cz_money) {
        this.cz_money = cz_money;
    }

    public String getVip_details() {
        return vip_details;
    }

    public void setVip_details(String vip_details) {
        this.vip_details = vip_details;
    }

    public String getCoupon_num() {
        return coupon_num;
    }

    public void setCoupon_num(String coupon_num) {
        this.coupon_num = coupon_num;
    }

    public String getCz_mode() {
        return cz_mode;
    }

    public void setCz_mode(String cz_mode) {
        this.cz_mode = cz_mode;
    }

    public String getCz_time() {
        return cz_time;
    }

    public void setCz_time(String cz_time) {
        this.cz_time = cz_time;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }
}
