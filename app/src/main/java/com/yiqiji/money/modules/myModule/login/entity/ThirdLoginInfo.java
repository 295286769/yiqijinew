package com.yiqiji.money.modules.myModule.login.entity;

/**
 * Created by ${huangweishui} on 2017/6/30.
 * address huang.weishui@71dai.com
 */
public class ThirdLoginInfo {
    private String uid;
    private String tokenid;
    private int code;

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
