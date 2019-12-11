package com.yiqiji.money.modules.homeModule.home.entity;

import com.yiqiji.money.modules.common.db.DailycostEntity;

import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/6.
 * address huang.weishui@71dai.com
 */
public class CateBillInfo {

    /**
     * data : [{"billid":"80070","accountbookid":"42415","userid":"9","deviceid":"bbac0a55d50a403aa13cd21fc6fe6107","billamount":"800.00","billtype":"1","billclear":"0","tradetime":"1499161336","billctime":"1499161336","billmark":"","billbrand":"","accountnumber":"0","billstatus":"1","billimg":"","address":"","billcateid":"74","billsubcateid":"http://static.test.yiqijiba.com/accountbook/category/6/179@2x.png","billcatename":"厨房设备","billcateicon":"http://static.test.yiqijiba.com/accountbook/category/6/74@2x.png","billsubcatename":"厨房设备－电冰箱","memberlist":[{"memberid":"8886","type":"1","amount":"800.00","status":"1","username":"baobao","usericon":"http://static.test.yiqijiba.com/avatar/head_9@2x.png"},{"memberid":"8886","type":"0","amount":"800.00","status":"1","username":"baobao","usericon":"http://static.test.yiqijiba.com/avatar/head_9@2x.png"}],"billcount":1}]
     * code : 0
     */

    private int code;
    private String msg;
    private List<DailycostEntity> data;

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

    public List<DailycostEntity> getData() {
        return data;
    }

    public void setData(List<DailycostEntity> data) {
        this.data = data;
    }
}
