package com.yiqiji.money.modules.homeModule.write.entity;

import com.yiqiji.money.modules.property.entity.PropertyTransEntity;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/6/22.
 * address huang.weishui@71dai.com
 */
public class PropertyTransListInfo {
    private int code;
    private String msg;
    private List<PropertyTransEntity> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<PropertyTransEntity> getData() {
        return data;
    }

    public void setData(List<PropertyTransEntity> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
