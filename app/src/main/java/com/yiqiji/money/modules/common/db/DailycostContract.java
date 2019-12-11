/**
 * DailyCostContract.java[V 2.0.0] Classs : .db.DailyCostContract Dingmao.SUN create at 2016年9月8日
 * 下午3:23:09
 */
package com.yiqiji.money.modules.common.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * .db.DailyCostContract
 *
 * @author Dingmao.SUN <br/>
 *         Create at 2016年9月8日 下午3:23:09
 */

public final class DailycostContract {

    public static final String AUTHORITY = "com.yiqiji.money.db";

    private DailycostContract() {
        // TODO Auto-generated constructor stub
    }

    public interface DtInfoColumns extends BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/dailycost");
        public static final String USERID = "userid";// 用户id
        public static final String BILLTYPE = "billtype"; // 类型（开销/收入）
        public static final String BILLAMOUNT = "billamount"; // 金额
        public static final String COSTTYPE = "costtype"; // 消费类型
        public static final String CONTEXT = "context"; // 明目
        public static final String BILLCTIME = "billctime"; // 记录日期
        public static final String TRADETIME = "tradetime"; // 记录创建日期
        public static final String UPDATETIME = "updatetime";// 更新日期
        public static final String WAGESID = "wagesid";// 工作id
        public static final String ISDELETED = "isdeleted"; // 是否删除
        public static final String ISCLEAR = "isclear"; //
        public static final String LNG = "lng"; // 经度
        public static final String LAT = "lat"; // 纬度
        public static final String EXTRAINFO1 = "extrainfo1"; // 备用1
        public static final String EXTRAINFO2 = "extrainfo2"; // 备用2
        public static final String BILLMARK = "billmark"; // 备注
        public static final String EXTRAINFO3 = "extrainfo3"; // 备用3
        public static final String YEAR = "year"; // 年
        public static final String MOTH = "moth"; // 月
        public static final String DAY = "day"; // 日
        public static final String ISSYNCHRONIZATION = "issynchronization"; // 是否同步数据库
        public static final String WHICHBOOK = "whichbook"; // 是哪一个账本名称
        public static final String ACCOUNTBOOKTYPE = "accountbooktype"; // 是否是多人账本
        public static final String BILLCLEAR = "billclear"; // 是否需要结算0：不需要 1：需要
        public static final String PKID = "pkid"; //
        public static final String BILLID = "billid"; //
        public static final String ACCOUNTBOOKID = "accountbookid"; // 账本id
        public static final String USERNAME = "username"; // 用户名称
        public static final String USERICON = "usericon"; // 用户名称
        public static final String BILLCATEID = "billcateid"; // 账单一级分类id
        public static final String BILLSUBCATEID = "billsubcateid"; // 账单二级分类id
        public static final String BILLCATENAME = "billcatename"; // 账单一级分类名称
        public static final String BILLCATEICON = "billcateicon"; // 账单一级分类图片
        public static final String BILLSUBCATENAME = "billsubcatename";
        public static final String BILLSUBCATEICON = "billsubcateicon";
        public static final String ACCOUNTNUMBER = "accountnumber"; // 关联资产ID，
        public static final String BILLCOUNT = "billcount"; // 账单消费人数
        public static final String DEVICEID = "deviceid"; // 设备号
        public static final String BILLSTATUS = "billstatus"; // 设备号
        public static final String BILLBRAND = "billbrand"; // 装修账本品牌

        public static final String BILLIMG = "billimg"; // 图片地址
        public static final String ADDRESS = "address"; // 账单发生地址


    }

    public interface DtBookMemberColumns extends BaseColumns {
        public static final Uri BOOKMEMBER_URI = Uri.parse("content://" + AUTHORITY + "/bookMember");
        public static final String USERID = "userid";// 用户id
        public static final String MEMBERID = "memberid";// 成员id
        public static final String BOOKID = "bookid";// 账本id
        public static final String USERNAME = "userName";// 用户名称
        public static final String BOOKNAME = "bookName";// 账本名称
        public static final String HEADICON = "headicon";// 头像icon
        public static final String USERISCLEAR = "userisclear";// 是否结算
        public static final String INDEX = "indexid";// 头像下标没有联网
        public static final String SETTLEMENT = "settlement";// 0：没有结算1：已经结算
        public static final String ISPAYMEN = "ispaymen";// 0：是付款人1：参与人
        public static final String BALANCEMEMBER = "balance";// 付款金额
        public static final String DEVICEID = "deviceid";// 设备唯一ID
    }

    public interface DtBillMemberColumns extends BaseColumns {
        public static final Uri BOOKMEMBER_URI = Uri.parse("content://" + AUTHORITY + "/billMember");
        public static final String USERID = "userid";// 用户id
        public static final String MEMBERID = "memberid";// 成员id
        public static final String TYPE = "type";// 账单类型
        public static final String AMOUNT = "amount";// 账单金额
        public static final String STATUS = "status";// 账单状态
        public static final String USERNAME = "userName";// 用户名称
        public static final String BILLID = "billid";// 账单id
        public static final String USERICON = "usericon";// 头像icon
        public static final String INDEX = "indexid";// 头像下标没有联网
        public static final String ISDELETED = "isdeleted";// 是否删除
        public static final String ISSYNCHRONIZATION = "issynchronization";// fasle没有同步true已经同步
        public static final String DEVICEID = "deviceid";//
    }

    public interface DtBooksColumns extends BaseColumns {
        public static final Uri Books_URI = Uri.parse("content://" + AUTHORITY + "/books");
        public static final String DEVICEID = "deviceid";// 设备ID
        public static final String BOOKID = "bookid";// 账本ID(本地创造时生成的唯一ID)
        public static final String BOOKDESC = "bookdesc";// book描述
        public static final String ACCOUNTBOOKCATENAME = "accountbookcatename";// 账本类型名称
        public static final String ISSHOWTIME = "isShowTime";// 0:显示月份tab1：不显示月份tab
        public static final String ACCOUNTBOOKID = "accountbookid"; // 账本ID(服务器返回的ID)
        public static final String ACCOUNTBOOKTITLE = "isSaccountbooktitleing";// 账本名称如宝宝账，本装修账本
        public static final String USERID = "userid";
        public static final String ACCOUNTBOOKCATE = "accountbookcate";// 账本分类
        public static final String ACCOUNTBOOKTYPE = "accountbooktype";// 账本类型
        public static final String ACCOUNTBOOKBUDGET = "accountbookbudget";// 账本预算费
        public static final String ACCOUNTBOOKSTATUS = "accountbookstatus";
        public static final String ACCOUNTBOOKCOUNT = "accountbookcount";// 账本人数
        public static final String ISCLEAR = "isclear";// 是否需要结算：0.否，1是
        public static final String ACCOUNTBOOKCTIME = "accountbookctime";
        public static final String ACCOUNTBOOKUTIME = "accountbookutime";
        public static final String ACCOUNTBOOKCATEICON = "accountbookcateicon";// 账本图标
        public static final String ISADD = "isadd";// 0:没有添加1：添加
        public static final String CREATETIMEBOOK = "createtime";// 账本创建时间
        public static final String SORTTYPE = "sorttype";//
        public static final String MYUID = "myuid";// 单签操作人id
        public static final String ISSYNCHRONIZATION = "issynchronization";// fasle没有同步true已经同步
        public static final String ISDELET = "isdelet";// fasle没有删除true已经删除

        public static final String PAYAMOUNT = "payamount";// 本月支出/累计支出
        public static final String RECEIVABLE = "receivable";// 本月收入/累计收入
        public static final String SPENTDIFF = "spentdiff";// 本月结余/累计结余
        public static final String MYSPENT = "myspent";// 我的消费/我的支出
        public static final String BUDGETDIFF = "budgetdiff";// 预算差额(当值大于0，表示预算剩余；当值小于0，表示预算超支)
        public static final String MYSPENTDIFF = "myspentdiff"; // 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
        public static final String ACCOUNTBOOKLTIME = "accountbookltime"; // 最新操作时间
        public static final String ISNEW = "isnew";// 是否是新账本
        public static final String FIRSTTIME = "firsttime";// 账单第一次发生时间
        public static final String ACCOUNTBOOKBGIMG = "accountbookbgimg";// 账本背景图片

    }

    public interface DtBookListColumns extends BaseColumns {
        public static final Uri Books_URI = Uri.parse("content://" + AUTHORITY + "/booklist");
        public static final String CATEGORYID = "categoryid";// 账本分类ID
        public static final String CATEGORYTITLE = "categorytitle";// 账本类型名称
        public static final String CATEGORYDESC = "categorydesc";// 账本类型描述
        public static final String CATEGORYTYPE = "categorytype"; // 账本类型，0.单人，1.多人
        public static final String CATEGORYICON = "categoryicon";// 账本图标
        public static final String PARENTID = "parentid";//
        public static final String STATUS = "status";//
        public static final String ISCLEAR = "isclear";// 是否需要结算

    }

    public interface DtSummaryConlumns {
        public static final Uri CONTENT_SUMMARY_URI = Uri.parse("content://" + AUTHORITY + "/dailycost_summary");
        public static final String STYPE = "stype";
        public static final String WEEKDAYS = "weekdays";
        public static final String COUNT = "count";
        public static final String SVALUE = "svalue";
        public static final String DATETIME = "datetime";
    }
}
