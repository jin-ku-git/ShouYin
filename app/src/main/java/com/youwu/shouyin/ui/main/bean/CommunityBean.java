package com.youwu.shouyin.ui.main.bean;

import java.io.Serializable;

public class CommunityBean implements Serializable {
    private int id;//商品id
    private String com_name;//商品名称
    private String com_img;//商品图片
    private String goods_number;//商品编号
    private String com_price;//商品价格
    private String com_discount_price;//商品优惠价格
    private String com_discount;//商品折扣
    private String sou_suo_text;//搜索的内容

    private int com_number;//商品数量
    private int position;//第几个
    private boolean com_number_state;//当加入到购物车时显示

    private String goods_peibi;//商品配比
    private String goods_peibi_number;//商品配比数量
    private String goods_company;//商品单位
    private String distribution_number;//配货数量
    private String goods_purchase_price;//商品进价
    private String total_price;//小计

    public String getGoods_peibi_number() {
        return goods_peibi_number;
    }

    public void setGoods_peibi_number(String goods_peibi_number) {
        this.goods_peibi_number = goods_peibi_number;
    }

    public String getGoods_peibi() {
        return goods_peibi;
    }

    public void setGoods_peibi(String goods_peibi) {
        this.goods_peibi = goods_peibi;
    }

    public String getGoods_company() {
        return goods_company;
    }

    public void setGoods_company(String goods_company) {
        this.goods_company = goods_company;
    }

    public String getDistribution_number() {
        return distribution_number;
    }

    public void setDistribution_number(String distribution_number) {
        this.distribution_number = distribution_number;
    }

    public String getGoods_purchase_price() {
        return goods_purchase_price;
    }

    public void setGoods_purchase_price(String goods_purchase_price) {
        this.goods_purchase_price = goods_purchase_price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getSou_suo_text() {
        return sou_suo_text;
    }

    public void setSou_suo_text(String sou_suo_text) {
        this.sou_suo_text = sou_suo_text;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isCom_number_state() {
        return com_number_state;
    }

    public void setCom_number_state(boolean com_number_state) {
        this.com_number_state = com_number_state;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public int getCom_number() {
        return com_number;
    }

    public void setCom_number(int com_number) {
        this.com_number = com_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getCom_img() {
        return com_img;
    }

    public void setCom_img(String com_img) {
        this.com_img = com_img;
    }

    public String getCom_price() {
        return com_price;
    }

    public void setCom_price(String com_price) {
        this.com_price = com_price;
    }

    public String getCom_discount_price() {
        return com_discount_price;
    }

    public void setCom_discount_price(String com_discount_price) {
        this.com_discount_price = com_discount_price;
    }

    public String getCom_discount() {
        return com_discount;
    }

    public void setCom_discount(String com_discount) {
        this.com_discount = com_discount;
    }
}
