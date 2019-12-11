package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.db.DailycostEntity;

public class BillDetailInfo {
    private int code;
    private String msg;
    private DailycostEntity data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DailycostEntity getData() {
        return data;
    }

    public void setData(DailycostEntity data) {
        this.data = data;
    }

}
