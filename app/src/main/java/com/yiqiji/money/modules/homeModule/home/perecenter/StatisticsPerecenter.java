package com.yiqiji.money.modules.homeModule.home.perecenter;

import android.content.Context;
import android.text.TextUtils;

import com.yiqiji.frame.core.facade.CommonFacade;
import com.yiqiji.frame.core.facade.SimpleMsg;
import com.yiqiji.frame.core.facade.ViewCallBack;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.frame.core.Constants;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.homeModule.home.entity.StaticsticsBillsInfo;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ${huangweishui} on 2017/7/5.
 * address huang.weishui@71dai.com
 */
public class StatisticsPerecenter {
    public static void iniStatisticsInfo(Context context, String id, final ViewCallBack viewCallBack) {
        HashMap<String, String> stringHashMap = StringUtils.getParamas("id", id);
        CommonFacade.getInstance().exec(Constants.STATISTICS_BOOK, stringHashMap, new ViewCallBack() {
            @Override
            public void onSuccess(Object o) throws Exception {
                super.onSuccess(o);
                if (o != null) {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    JSONObject data_info = null;
                    if (jsonObject.getInt("code") == 0) {
                        JSONObject data = jsonObject.getJSONObject("data");

                        viewCallBack.onSuccess(data);
                    }

                }
            }

            @Override
            public void onFailed(SimpleMsg simleMsg) {
                super.onFailed(simleMsg);
                viewCallBack.onFailed(simleMsg);
            }
        });
    }

    public static StaticsticsBillsInfo getStaticsticsBillsInfo(Date data_time, String bookid, String sorttype, String type, String billcateid, String billsubcateid) {
        StaticsticsBillsInfo staticsticsBillsInfo = new StaticsticsBillsInfo();
        if (sorttype == null) {
            return null;
        }
        boolean isExpectMoth = DateUtil.isExpectMoth(data_time);
        String cateidWhere = DailycostContract.DtInfoColumns.BILLCATEID;
        String cateid = billcateid;
        String year = DateUtil.formatTheDateToMM_dd(data_time, 5);
        String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(data_time, 3)) + "";
        String selection = null;
        String[] selectionArg = null;
//        String groupBy = DailycostContract.DtInfoColumns.BILLCATEID;
        String groupBy = null;
//        if (!TextUtils.isEmpty(billsubcateid) && !billsubcateid.equals("0")) {
////            groupBy = DailycostContract.DtInfoColumns.BILLSUBCATEID;
//            cateidWhere = DailycostContract.DtInfoColumns.BILLSUBCATEID;
//            cateid = billsubcateid;
//        }
        String having = null;
        String orderBy = DailycostContract.DtInfoColumns.BILLAMOUNT + " DESC";
        String limit = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_time);
        long start_time = DateUtil.getStartTime(data_time);// 开始时间
//        calendar.add(Calendar.MONTH, 12);// 结束时间
        long end_time = DateUtil.getEndTime(data_time);
        List<TotalBalance> list;
        if (TextUtils.isEmpty(type)) {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + cateidWhere + "=? ";
                String[] selectionArgs = {bookid, "false", cateid};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + "<? and " + cateidWhere + "=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", start_time + "", end_time + "", cateid};
                    selectionArg = selectionArgs;
                } else {
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + cateidWhere + "=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, "false", cateid};
                    selectionArg = selectionArgs;
                }

            }
        } else {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                        + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + cateidWhere + "=? ";
                ;
                String[] selectionArgs = {bookid, type, "false", cateid};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + ">? and " + DailycostContract.DtInfoColumns.TRADETIME + "<? and " + cateidWhere + "=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", type, start_time + "",
                            end_time + "", cateid};
                    selectionArg = selectionArgs;
                } else {

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                            + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + cateidWhere + "=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, type, "false", cateid};
                    selectionArg = selectionArgs;
                }

            }
        }
        staticsticsBillsInfo.setWhere(selection);
        staticsticsBillsInfo.setWhereStrings(selectionArg);
        staticsticsBillsInfo.setGroupBy(groupBy);
        staticsticsBillsInfo.setHaving(having);
        staticsticsBillsInfo.setOrderBy(orderBy);
        staticsticsBillsInfo.setLimit(limit);

        return staticsticsBillsInfo;
    }

    public static StaticsticsBillsInfo getStaticsticsBillsInfos(Date data_time, String bookid, String sorttype, String type, String billcateid, String billsubcateid) {
        StaticsticsBillsInfo staticsticsBillsInfo = new StaticsticsBillsInfo();
        if (sorttype == null) {
            return null;
        }
        boolean isExpectMoth = DateUtil.isExpectMoth(data_time);
        String year = DateUtil.formatTheDateToMM_dd(data_time, 5);
        String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(data_time, 3)) + "";
        String selection = null;
        String[] selectionArg = null;
        String groupBy = DailycostContract.DtInfoColumns.BILLSUBCATEID;
        String having = null;
        String orderBy = "SUM(" + DailycostContract.DtInfoColumns.BILLAMOUNT + ")" + " DESC";
        String limit = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_time);
        long start_time = DateUtil.getStartTime(data_time);// 开始时间
        long end_time = DateUtil.getEndTime(data_time);
        List<TotalBalance> list;
        if (TextUtils.isEmpty(type)) {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                        + DailycostContract.DtInfoColumns.BILLCATEID +/* "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + */"=? ";
                String[] selectionArgs = {bookid, "false", billcateid/*, billsubcateid*/};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + "<? and " + DailycostContract.DtInfoColumns.BILLCATEID /*+ "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID */ + "=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", start_time + "", end_time + "", billcateid/*, billsubcateid*/};
                    selectionArg = selectionArgs;
                } else {
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID +/* "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID +*/ "=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, "false", billcateid/*, billsubcateid*/};
                    selectionArg = selectionArgs;
                }

            }
        } else {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                        + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + DailycostContract.DtInfoColumns.BILLCATEID +
                        /*"=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID +*/ "=? ";
                ;
                String[] selectionArgs = {bookid, type, "false", billcateid/*, billsubcateid*/};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + ">? and " + DailycostContract.DtInfoColumns.TRADETIME + "<? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID + /*"=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + */"=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", type, start_time + "",
                            end_time + "", billcateid/*, billsubcateid*/};
                    selectionArg = selectionArgs;
                } else {

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                            + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID + /*"=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + */"=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, type, "false", billcateid/*, billsubcateid*/};
                    selectionArg = selectionArgs;
                }

            }
        }
        staticsticsBillsInfo.setWhere(selection);
        staticsticsBillsInfo.setWhereStrings(selectionArg);
        staticsticsBillsInfo.setGroupBy(groupBy);
        staticsticsBillsInfo.setHaving(having);
        staticsticsBillsInfo.setOrderBy(orderBy);
        staticsticsBillsInfo.setLimit(limit);

        return staticsticsBillsInfo;
    }

    public static StaticsticsBillsInfo getBillsCount(Date data_time, String bookid, String sorttype, String type, String billcateid, String billsubcateid) {
        StaticsticsBillsInfo staticsticsBillsInfo = new StaticsticsBillsInfo();
        if (sorttype == null) {
            return null;
        }
        boolean isExpectMoth = DateUtil.isExpectMoth(data_time);
        String year = DateUtil.formatTheDateToMM_dd(data_time, 5);
        String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(data_time, 3)) + "";
        String selection = null;
        String[] selectionArg = null;
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data_time);
        long start_time = DateUtil.getStartTime(data_time);// 开始时间
        long end_time = DateUtil.getEndTime(data_time);
        List<TotalBalance> list;
        if (TextUtils.isEmpty(type)) {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                        + DailycostContract.DtInfoColumns.BILLCATEID + "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                String[] selectionArgs = {bookid, "false", billcateid, billsubcateid};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + "<? and " + DailycostContract.DtInfoColumns.BILLCATEID + "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", start_time + "", end_time + "", billcateid, billsubcateid};
                    selectionArg = selectionArgs;
                } else {
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID + "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, "false", billcateid, billsubcateid};
                    selectionArg = selectionArgs;
                }

            }
        } else {
            if (sorttype.equals("0")) {// 没有time tab

                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                        + DailycostContract.DtInfoColumns.ISDELETED + "=? and " + DailycostContract.DtInfoColumns.BILLCATEID +
                        "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                ;
                String[] selectionArgs = {bookid, type, "false", billcateid, billsubcateid};
                selectionArg = selectionArgs;
            } else {
                if (isExpectMoth) {// 预期
                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                            + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME
                            + ">? and " + DailycostContract.DtInfoColumns.TRADETIME + "<? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID + "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                    ;
                    String[] selectionArgs = {bookid, "false", type, start_time + "",
                            end_time + "", billcateid, billsubcateid};
                    selectionArg = selectionArgs;
                } else {

                    selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                            + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                            + DailycostContract.DtInfoColumns.ISDELETED + "=? and "
                            + DailycostContract.DtInfoColumns.BILLCATEID + "=? and " + DailycostContract.DtInfoColumns.BILLSUBCATEID + "=? ";
                    ;
                    String[] selectionArgs = {bookid, year, moth, type, "false", billcateid, billsubcateid};
                    selectionArg = selectionArgs;
                }

            }
        }
        staticsticsBillsInfo.setWhere(selection);
        staticsticsBillsInfo.setWhereStrings(selectionArg);
        staticsticsBillsInfo.setGroupBy(groupBy);
        staticsticsBillsInfo.setHaving(having);
        staticsticsBillsInfo.setOrderBy(orderBy);
        staticsticsBillsInfo.setLimit(limit);

        return staticsticsBillsInfo;
    }
}
