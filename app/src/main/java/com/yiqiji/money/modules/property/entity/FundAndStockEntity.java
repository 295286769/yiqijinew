package com.yiqiji.money.modules.property.entity;

import java.io.Serializable;

/**
 * Created by dansakai on 2017/3/9.
 * 股票基金实体类
 */

public class FundAndStockEntity implements Serializable{

    private String f_asset;//值为0股票，反之为基金
    private String f_code;//股票基金代码
    private String f_symbolName;//股票基金名称
    private boolean isatten;//是否关注

    public boolean isatten() {
        return isatten;
    }

    public void setIsatten(boolean isatten) {
        this.isatten = isatten;
    }

    public String getF_asset() {
        return f_asset;
    }

    public void setF_asset(String f_asset) {
        this.f_asset = f_asset;
    }

    public String getF_code() {
        return f_code;
    }

    public void setF_code(String f_code) {
        this.f_code = f_code;
    }

    public String getF_symbolName() {
        return f_symbolName;
    }

    public void setF_symbolName(String f_symbolName) {
        this.f_symbolName = f_symbolName;
    }
}
