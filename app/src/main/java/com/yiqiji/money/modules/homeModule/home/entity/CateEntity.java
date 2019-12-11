package com.yiqiji.money.modules.homeModule.home.entity;

import java.util.List;

/**
 * Created by dansakai on 2017/7/6.
 */

public class CateEntity {

    /**
     * data : ["准备","拆改","水电","泥木","油漆","竣工","软装","入住"]
     * code : 0
     */
    private String cate;
    private int isChk; //1选中，0不选中

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public int getIsChk() {
        return isChk;
    }

    public void setIsChk(int isChk) {
        this.isChk = isChk;
    }
}
