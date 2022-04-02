package com.youwu.shouyin.ui.money.bean;

import java.io.Serializable;

/**
 * 充值金额表
 */
public class VipRechargeBean implements Serializable {


    private String id;
    private String recharge_prick;
    private String recharge_content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecharge_prick() {
        return recharge_prick;
    }

    public void setRecharge_prick(String recharge_prick) {
        this.recharge_prick = recharge_prick;
    }

    public String getRecharge_content() {
        return recharge_content;
    }

    public void setRecharge_content(String recharge_content) {
        this.recharge_content = recharge_content;
    }
}
