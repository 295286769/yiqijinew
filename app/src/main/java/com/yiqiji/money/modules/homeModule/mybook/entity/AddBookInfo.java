package com.yiqiji.money.modules.homeModule.mybook.entity;

import com.yiqiji.money.modules.common.db.BooksDbInfo;

/**
 * Created by ${huangweishui} on 2017/6/15.
 * address huang.weishui@71dai.com
 */
public class AddBookInfo {
    private int code;
    private BooksDbInfo data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BooksDbInfo getData() {
        return data;
    }

    public void setData(BooksDbInfo data) {
        this.data = data;
    }
}
