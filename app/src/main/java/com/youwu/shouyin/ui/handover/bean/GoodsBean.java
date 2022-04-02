package com.youwu.shouyin.ui.handover.bean;

import java.io.Serializable;

public class GoodsBean implements Serializable {
    private int id;//商品id
    private String goods_name;//商品名称
    private String goods_prick;//商品价格
    private String goods_number;//商品数量
    private String goods_class;//商品分类
    private String goods_code;//商品编号

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_prick() {
        return goods_prick;
    }

    public void setGoods_prick(String goods_prick) {
        this.goods_prick = goods_prick;
    }

    public String getGoods_number() {
        return goods_number;
    }

    public void setGoods_number(String goods_number) {
        this.goods_number = goods_number;
    }

    public String getGoods_class() {
        return goods_class;
    }

    public void setGoods_class(String goods_class) {
        this.goods_class = goods_class;
    }

    public String getGoods_code() {
        return goods_code;
    }

    public void setGoods_code(String goods_code) {
        this.goods_code = goods_code;
    }
}
