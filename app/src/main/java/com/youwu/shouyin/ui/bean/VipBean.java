package com.youwu.shouyin.ui.bean;

import java.io.Serializable;

/**
 * vip信息表
 */
public class VipBean implements Serializable {
    private int id;
    private String name;//会员名称
    private String tel;//手机号
    private String image;//头像
    private String money;//余额
    private String add_time;//创建时间
    private String update_time;//更新时间
    private int type_state;//1 选择该vip  2取消

    public int getType_state() {
        return type_state;
    }

    public void setType_state(int type_state) {
        this.type_state = type_state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
