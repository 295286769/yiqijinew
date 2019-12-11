package com.yiqiji.money.modules.book.detailinfo.model;

import com.yiqiji.money.modules.book.bookcategory.model.BookCategory;
import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.db.BillMemberInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by leichi on 2017/6/12.
 */

public class BookBillItemModel implements Serializable {

    public String billid;                                       //账单ID
    public String accountbookid;                                //账本ID
    public String userid;                                       //账单创建人id
    public String deviceid;                                     //创建账单的设备ID
    public String billamount;                                   //账单金额
    public String billtype;                                     //账单类型:0.收入,1.支出,2.转账，3.结算，4.交款 5.日志感想
    public String billclear;                                    //是否已经结算 0:未结算1：已结算
    public String tradetime;                                    //账单交易时间
    public String billctime;                                    //账单创建时间
    public String billmark;                                     //账单的备注
    public String accountnumber;                                //关联资产ID
    public String billstatus;                                   //账单状态：1.表示正常，0.表示删除
    public String billimg;                                      //账单图片
    public String address;                                      //账单的地址
    public String username;                                     //账单创建人昵称
    public String usericon;                                     //账单创建人头像
    public String billcateid;                                   //账单一级分类id
    public String billsubcateid;                                //账单二级分类id
    public String billcateicon;                                 //账单分类icon
    public String billcount;                                    //账单的成员数量
    public String billcatename;                                 //账单分类名称
    public boolean bookIsAA;                                    //是否是AA账本
    public String billsubcateicon;                              //子分类图标
    public String billsubcatename;                              //子分类名称
    public String billbrand; //装修账本品牌字段
    public List<String> imglist; //装修账本图片列表
    public String billnum; //装修账本装修数量
    public List<BillMemberInfo> memberlist;                     //账单成员列表

    public String getBillcateIconUrl() {
        return BookCategory.getHDIconUrl(billcateicon);
    }


    public String getBillsubcateIconUrl() {
        return BookCategory.getHDIconUrl(billsubcateicon);
    }

    public boolean isPartInSettlement() {
        if (memberlist == null || memberlist.size() == 0) {
            return false;
        }
        for (int i = 0; i < memberlist.size(); i++) {
            if (memberlist.get(i).getMemberid().equals(LoginConfig.getInstance().getUserid())) {
                return true;
            }
        }

        return false;
    }
}
