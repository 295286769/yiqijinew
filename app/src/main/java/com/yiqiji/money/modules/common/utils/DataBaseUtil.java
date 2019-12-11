package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.TextUtils;

import com.yiqiji.frame.core.config.LoginConfig;
import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.BillMemberInfo;
import com.yiqiji.money.modules.common.db.BooksDbInfo;
import com.yiqiji.money.modules.common.db.BooksDbMemberInfo;
import com.yiqiji.money.modules.common.db.DailycostContract;
import com.yiqiji.money.modules.common.db.DailycostEntity;
import com.yiqiji.money.modules.common.db.DbInterface;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.common.entity.PostBill;
import com.yiqiji.money.modules.common.myserver.MyServer;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import de.greenrobot.event.EventBus;

/**
 * Created by ${huangweishui} on 2017/4/14.
 * address huang.weishui@71dai.com
 */
public class DataBaseUtil {
    private static ExecutorService SINGLE_TASK_EXECUTOR = null;

    static {
        SINGLE_TASK_EXECUTOR = (ExecutorService) Executors.newSingleThreadExecutor();
    }

    /**
     * 根据mDeviceid跟新账本
     *
     * @param booksDbInfo
     * @param mDeviceid
     */
    public static void updateBookDeviceid(final BooksDbInfo booksDbInfo, final String mDeviceid) {

        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBooksColumns.DEVICEID + "=?";
                    String whereStrings[] = new String[]{mDeviceid};
                    DbInterface.updateBooks(booksDbInfo, where, whereStrings);

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除所有账本
     */
    public static void deletBookInfos() {

        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    DbInterface.deletBooks(null, null);

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 按条件删除账本
     *
     * @param
     */
    public static void deleBook(final String where, final String[] selectionArgs, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.deletBooks(where, selectionArgs);
                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据账本id跟新账本
     *
     * @param booksDbInfo
     */
    public static void updateBook(final BooksDbInfo booksDbInfo, final String mAccountid) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = null;
                    String[] selectionArgs = null;
                    where = DailycostContract.DtBooksColumns.ACCOUNTBOOKID + "=?";
                    selectionArgs = new String[]{mAccountid};
                    long id_book = DbInterface.updateBooks(booksDbInfo, where, selectionArgs);
                    if (id_book == 0) {
                        DbInterface.insertBooks(booksDbInfo);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据账本id插入成员 先删除后插入
     *
     * @param mAccountBookid
     * @param booksDbMemberInfos
     */
    public static void insertBooksDetailMember(final String mAccountBookid, final List<BooksDbMemberInfo> booksDbMemberInfos) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBookMemberColumns.BOOKID + "=?";
                    String selectionArgs[] = new String[]{mAccountBookid};
                    DbInterface.deletAndAddBooksMember(booksDbMemberInfos, where, selectionArgs);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 插入我的账本列表
     */
    public static void insertMyBooks(final List<BooksDbInfo> booksDbInfos) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DbInterface.deletAndInsert(booksDbInfos);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 插入账本列表和账本分类列表
     * <p/>
     * dailycostEntities
     */
    public static void insertBookList(final String mAccountbookid, final List<BooksDbInfo> booksDbInfos,
                                      final List<MyBooksListInfo> booksListInfos, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (booksDbInfos != null) {
                        long id_book = DbInterface.insertBooksMyList(booksDbInfos);
                        if (id_book > 0) {
                            LoginConfig.getInstance().setBookId(
                                    booksDbInfos.get(0).getDeviceid());
                            Message message = Message.obtain();
                            message.what = what;
                            message.obj = booksDbInfos;
                            handler.sendMessage(message);

                        }
                    }
                    if (booksListInfos != null) {
                        LoginConfig.getInstance().setBookId(mAccountbookid);
                        long id = DbInterface.insertBookList(booksListInfos);
                        if (id > 0) {
                            Message message = Message.obtain();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 按条件查询账本列表
     *
     * @param
     */
    public static void serchBookList(final String where, final String[] selectionArgs, final String groupBy,
                                     final String having, final String orderBy, final String limit, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<BooksDbInfo> booksDbInfos = DbInterface.getListBooksDbInfo(where, selectionArgs, groupBy,
                            having, orderBy, limit);
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfos;

                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据账本id查询账本和账本成员
     */
    public static void initBooksDataAndMember(final String bookid, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBooksColumns.ACCOUNTBOOKID + "=?";
                    String[] selectionArgs = new String[]{bookid};
                    BooksDbInfo booksDbInfo = DbInterface.getBooksDbInfo(where, selectionArgs, null, null, null, null);
                    if (booksDbInfo != null) {
                        where = DailycostContract.DtBookMemberColumns.BOOKID + "=?";
                        String[] selectionArgsmember = new String[]{booksDbInfo.getAccountbookid()};
                        List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                                selectionArgs, null, null, null, null);
                        booksDbInfo.setMember(booksDbMemberInfos);
                    }
                    Message message = Message.obtain();
                    message.what = what;
                    message.obj = booksDbInfo;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据账本id查询成员
     */
    public static void initMemberData(final String mAccountBookid, final Handler handler, final int what) {

        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBookMemberColumns.BOOKID + "=? ";
                    String[] selectionArgs = new String[]{mAccountBookid};
                    List<BooksDbMemberInfo> booksDbMemberInfos = DbInterface.getListBooksDbMemberInfo(where,
                            selectionArgs, null, null, null, null);
                    Message msg = Message.obtain();
                    msg.what = what;
                    msg.obj = booksDbMemberInfos;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新同步后的账单
     *
     * @param dailycostEntities
     */
    public static void updateMultipleRecords(final List<DailycostEntity> dailycostEntities) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    long id = DbInterface.updateMultipleRecordsDailycostInfo(dailycostEntities);
                    if (id > -1) {
                        EventBus.getDefault().post("getBills");
                        RequsterTag.isSynchronizationing = false;
                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跟新账单
     *
     * @param dailycost
     * @param whereClause
     * @param whereArgs
     * @param handler
     * @param what
     */
    public static void updateDataBaseDailycostInfo(final DailycostEntity dailycost, final String whereClause,
                                                   final String[] whereArgs, final Handler handler, final int what) {
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.updateDataBaseDailycostInfo(dailycost, whereClause, whereArgs);

                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 插入账单
     *
     * @param dailycostEntity
     * @param handler
     * @param what
     */
    public static void addDailycostInfo(final DailycostEntity dailycostEntity, final Handler handler, final int what) {
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    long id = DbInterface.addDailycostInfo(dailycostEntity);
                    Message message = Message.obtain();
                    message.what = what;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 账单查询和账单下成员
     * <p/>
     * record_date时间
     * mAccounBooid账本id
     * sorttype是否显示时间搓
     * billType是否需要分类
     * isExpectMoth是否是预期账单
     * pager账单页数
     * pagersize账单每页大小
     * handler
     * what
     */
    public static void searchDataReturnDailycostEntitys(final Date record_date, final String mAccounBooid,
                                                        final String sorttype, final String billType, final boolean isExpectMoth, final int pager,
                                                        final int pagersize, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (sorttype == null) {
                        return;
                    }
                    String year = DateUtil.formatTheDateToMM_dd(record_date, 5);
                    String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)) + "";
                    String selection = null;
                    String[] selectionArg = null;
                    String groupBy = null;
                    String having = null;
                    String orderBy = DailycostContract.DtInfoColumns.TRADETIME + " DESC";
                    String limit = (pager - 1) * pagersize + "," + pagersize;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                    calendar.add(Calendar.MONTH, 120);// 结束时间
                    long end_time = calendar.getTimeInMillis() / 1000;
                    List<DailycostEntity> list;
                    if (TextUtils.isEmpty(billType)) {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                            String[] selectionArgs = {mAccounBooid, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期

                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                                        + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">? and " + DailycostContract.DtInfoColumns.TRADETIME
                                        + "<?";
                                String[] selectionArgs = {mAccounBooid, "false", start_time + "", end_time + ""};
                                selectionArg = selectionArgs;
                            } else {
                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                                        + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                                String[] selectionArgs = {mAccounBooid, year, moth, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    } else {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                                    + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                            String[] selectionArgs = {mAccounBooid, billType, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期
                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                                        + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME
                                        + ">? and " + DailycostContract.DtInfoColumns.TRADETIME + "<?";
                                String[] selectionArgs = {mAccounBooid, "false", billType, start_time + "",
                                        end_time + ""};
                                selectionArg = selectionArgs;
                            } else {

                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                                        + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                                        + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                                String[] selectionArgs = {mAccounBooid, year, moth, billType, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    }
                    list = DbInterface.queryDailycostEntitys(selection, selectionArg, groupBy, having, orderBy, limit);
                    for (int i = 0; i < list.size(); i++) {
                        DailycostEntity dailycostEntity = list.get(i);
                        selection = BillMemberInfo.BILLID + "=?";
                        String[] selectionArgs = {dailycostEntity.getBillid()};
                        selectionArg = selectionArgs;
                        List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(selection, selectionArg, null, null, null, null);
                        dailycostEntity.setMemberlist(billMemberInfos);
                        list.set(i, dailycostEntity);
                    }
                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据账本id查询某一账单和账单成员
     */
    public static void searchDailycostEntitysAndMember(final String mAccounBooid, final String mBillid,
                                                       final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    DailycostEntity dailycostEntity;
                    String selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLID + "=?";

                    String[] selectionArgs = {mAccounBooid, mBillid};
                    String orderBy = null;
                    String limit = null;
                    dailycostEntity = DbInterface.getDataBaeDailycostEntity(selection, selectionArgs, null, null,
                            orderBy, null);
                    if (dailycostEntity != null) {
                        selection = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                        String[] selectionArgsStrings = {mBillid};
                        List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(selection,
                                selectionArgsStrings, null, null, null, null);
                        dailycostEntity.setMemberlist(billMemberInfos);
                    }
                    Message msg = Message.obtain();
                    msg.obj = dailycostEntity;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据账单id查询账单成员
     *
     * @param billid
     * @param i
     */
    public static void seachBillMember(final String billid, final int i, final int what, final Handler handler) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                    String[] selectionArgs = new String[]{billid};
                    List<BillMemberInfo> infos = DbInterface.getListBillDbMemberInfo(where, selectionArgs, null, null,
                            null, null);
                    Message message = Message.obtain();
                    message.what = 2;
                    message.arg1 = i;
                    message.obj = infos;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 删除账单
     * <p/>
     * _id
     * listData
     * position
     */
    public static void deleteDailycostInfo(final String billid, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @SuppressWarnings("unused")
                @Override
                public void run() {
                    long id = DbInterface.deleteDailycostEntityDataBase(billid);
                    if (id > -1) {
                        Message message = new Message();
                        message.what = what;
                        handler.sendMessage(message);
                    }
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void seachUnSys(final Context context, final BooksDbInfo booksDbInfo, final Handler handler) {
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {

                    List<DailycostEntity> dailycostEntities = new ArrayList<>();
                    List<PostBill> postBills = new ArrayList<>();
                    String selection;
                    String[] selectionArs = null;
                    if (booksDbInfo != null) {
                        selection = DailycostContract.DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and "
                                + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                        String[] selectionArgs = new String[]{"false", booksDbInfo.getAccountbookid(), "false"};
                        selectionArs = selectionArgs;
                    } else {
                        selection = DailycostContract.DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                        String[] selectionArgs = new String[]{"false", "false"};
                        selectionArs = selectionArgs;
                    }

                    List<DailycostEntity> dailycoss = DbInterface.getQueryDailycostEntitys(selection, selectionArs, null, null, null, null);

                    if (dailycoss != null && dailycoss.size() > 0) {

                        for (int i = 0; i < dailycoss.size(); i++) {
                            DailycostEntity dailycostEntity = dailycoss.get(i);
                            String whereString = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                            String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                            List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString, selectionAr,
                                    null, null, null, null);
                            dailycostEntity.setMemberlist(billMemberInfos);
                            dailycostEntities.add(dailycostEntity);
                            dailycoss.get(i).setMemberlist(billMemberInfos);
                        }
                        List<PostBill> postBillss = getPostBillData("add", dailycoss);
                        for (PostBill postBill1 : postBillss) {
                            postBills.add(postBill1);
                        }
                    }


                    if (booksDbInfo != null) {
                        String selectiondelet = DailycostContract.DtInfoColumns.ISDELETED + "=? and " + DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=?";
                        String[] selectionArgsdelet = new String[]{"true", booksDbInfo.getAccountbookid()};
                        List<DailycostEntity> dailEntities = DbInterface.getQueryDailycostEntitys(selectiondelet,
                                selectionArgsdelet, null, null, null, null);
                        if (dailEntities != null && dailEntities.size() > 0) {

                            for (int i = 0; i < dailEntities.size(); i++) {
                                DailycostEntity dailycostEntity = dailEntities.get(i);
                                String whereString = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                                String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                                List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString,
                                        selectionAr, null, null, null, null);
                                dailycostEntity.setMemberlist(billMemberInfos);
                                dailycostEntities.add(dailycostEntity);
                                dailEntities.get(i).setMemberlist(billMemberInfos);
                            }
                            List<PostBill> postBillss = getPostBillData("del", dailEntities);
                            for (PostBill postBill : postBillss) {
                                postBills.add(postBill);

                            }
                        }
                    }
                    if (booksDbInfo != null) {
                        String selectiondelet = DailycostContract.DtInfoColumns.ISSYNCHRONIZATION + "=? and " + DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=?";
                        String[] selectionArgsdelet = new String[]{"edit", booksDbInfo.getAccountbookid()};
                        List<DailycostEntity> dailEntities = DbInterface.getQueryDailycostEntitys(selectiondelet,
                                selectionArgsdelet, null, null, null, null);
                        if (dailEntities != null && dailEntities.size() > 0) {

                            for (int i = 0; i < dailEntities.size(); i++) {
                                DailycostEntity dailycostEntity = dailEntities.get(i);
                                String whereString = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                                String[] selectionAr = new String[]{dailycostEntity.getBillid()};
                                List<BillMemberInfo> billMemberInfos = DbInterface.getListBillDbMemberInfo(whereString,
                                        selectionAr, null, null, null, null);
                                dailycostEntity.setMemberlist(billMemberInfos);
                                dailycostEntities.add(dailycostEntity);
                                dailEntities.get(i).setMemberlist(billMemberInfos);
                            }
                            List<PostBill> postBillss = getPostBillData("edit", dailEntities);
                            for (PostBill postBill : postBillss) {
                                postBills.add(postBill);

                            }
                        }
                    }


                    if (dailycostEntities.size() == 0) {
                        Message msg = Message.obtain();
                        msg.what = RequsterTag.GETBILLS;
                        handler.sendMessage(msg);
                        Message msgMessage2 = Message.obtain();
                        msgMessage2.what = RequsterTag.SYNCHRONIZATION_SUS;
                        msgMessage2.obj = "0";
                        handler.sendMessage(msgMessage2);
                        RequsterTag.isSynchronizationing = false;
                    } else {
                        Intent intent = new Intent(context, MyServer.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("postBills", (ArrayList<? extends Parcelable>) postBills);
                        bundle.putParcelableArrayList("dailycostEntitys", (ArrayList<? extends Parcelable>) dailycostEntities);
                        intent.putExtras(bundle);
                        context.stopService(intent);
                        context.startService(intent);
                    }
                }


            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据账本id插入账单 先删除后插入
     * <p/>
     */
    public static void insertDailycostEntitys(final DailycostEntity dailycostEntity, final String mAccounBooid,
                                              final String billid) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<BillMemberInfo> billMemberInfos = dailycostEntity.getMemberlist();
                    if (billMemberInfos != null) {
                        String where = DailycostContract.DtBillMemberColumns.BILLID + "=?";
                        String selectionArgsString[] = new String[]{billid};
                        DbInterface.deletAndAdd(where, selectionArgsString, billMemberInfos);

                    }

                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 统计账单分类列表
     * <p/>
     * record_date时间
     * mAccounBooid账本id
     * sorttype是否显示时间搓
     * billType是否需要分类
     * isExpectMoth是否是预期账单
     * pager账单页数
     * pagersize账单每页大小
     * handler
     * what
     */
    public static void getTotalBalances(final Date record_date, final String mAccounBooid, final String sorttype,
                                        final String billType, final boolean isExpectMoth, final Handler handler, final int what) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    if (sorttype == null) {
                        return;
                    }
                    String year = DateUtil.formatTheDateToMM_dd(record_date, 5);
                    String moth = Integer.parseInt(DateUtil.formatTheDateToMM_dd(record_date, 3)) + "";
                    String selection = null;
                    String[] selectionArg = null;
                    String groupBy = DailycostContract.DtInfoColumns.BILLCATEID;
                    String having = null;
                    String orderBy = "SUM(" + DailycostContract.DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                    String limit = null;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                    calendar.add(Calendar.MONTH, 12);// 结束时间
                    long end_time = calendar.getTimeInMillis() / 1000;
                    List<TotalBalance> list;
                    if (TextUtils.isEmpty(billType)) {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                            String[] selectionArgs = {mAccounBooid, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期

                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                                        + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">? and " + DailycostContract.DtInfoColumns.TRADETIME
                                        + "<?";
                                String[] selectionArgs = {mAccounBooid, "false", start_time + "", end_time + ""};
                                selectionArg = selectionArgs;
                            } else {
                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                                        + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                                String[] selectionArgs = {mAccounBooid, year, moth, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    } else {
                        if (sorttype.equals("0")) {// 没有time tab

                            selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                                    + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                            String[] selectionArgs = {mAccounBooid, billType, "false"};
                            selectionArg = selectionArgs;
                        } else {
                            if (isExpectMoth) {// 预期
                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED
                                        + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME
                                        + ">? and " + DailycostContract.DtInfoColumns.TRADETIME + "<?";
                                String[] selectionArgs = {mAccounBooid, "false", billType, start_time + "",
                                        end_time + ""};
                                selectionArg = selectionArgs;
                            } else {

                                selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and "
                                        + DailycostContract.DtInfoColumns.MOTH + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and "
                                        + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                                String[] selectionArgs = {mAccounBooid, year, moth, billType, "false"};
                                selectionArg = selectionArgs;
                            }

                        }
                    }
                    list = DbInterface.getTotalBalances(selection, selectionArg, groupBy, having, orderBy, limit);
                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 查询最早记账时间账单
     *
     * @param mAccountId
     * @param handler
     * @param what
     */
    public static void searchTime(final String mAccountId, final Handler handler, final int what) {

        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    String where = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.ISDELETED + "=?";
                    String whereStrings[] = new String[]{mAccountId, "false"};
                    String orderBy = DailycostContract.DtInfoColumns.TRADETIME + " ASC";
                    String limit = "1";
                    List<DailycostEntity> dailycostEntitys = DbInterface.getQueryDailycostEntitys(where, whereStrings,
                            null, null, orderBy, limit);
                    DailycostEntity dailycostEntity = null;
                    if (dailycostEntitys != null && dailycostEntitys.size() != 0) {
                        dailycostEntity = dailycostEntitys.get(0);
                    }
                    long cTimeLong;
                    if (dailycostEntity == null) {
                        cTimeLong = new Date().getTime();
                    } else {
                        String cTime = dailycostEntity.getTradetime();
                        cTimeLong = Long.parseLong(cTime) * 1000;
                    }
                    Message message = new Message();
                    message.what = what;

                    message.obj = cTimeLong;
                    handler.sendMessage(message);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 统计饼图预期
     *
     * @param record_date
     */
    public static void getDailycostMothInfosTypeFirstThreeLast(final Context context, final Date record_date,
                                                               final String mAccounBooid, final String mbilltype, final String sorttype, final Handler handler) {
        // 捕获线程池拒绝执行异常
        try {
            // 线程放入线程池
            SINGLE_TASK_EXECUTOR.execute(new Runnable() {

                @Override
                public void run() {
                    List<TotalBalance> list;
                    boolean isExpectMoth = DateUtil.isExpectMoth(record_date);
                    int what = 3;
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(record_date);
                    if (isExpectMoth) {// 预期月份
                        what = RequsterTag.EXPECTMOTHBING;

                        long start_time = (DateUtil.stringToTime(calendar.getTimeInMillis())) / 1000;// 开始时间
                        calendar.add(Calendar.YEAR, 120);// 结束时间
                        long end_time = calendar.getTimeInMillis() / 1000;
                        String selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.TRADETIME + ">=? and " + DailycostContract.DtInfoColumns.TRADETIME + "<? ";
                        String[] selectionArgs = new String[]{mAccounBooid, mbilltype, start_time + "", end_time + ""};
                        String groupBy = DailycostContract.DtInfoColumns.BILLCATEID;
                        String orderBy = "SUM(" + DailycostContract.DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                        String limit = "3";
                        list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, limit);

                    } else {
                        if (sorttype.equals("1")) {//
                            String selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=? and " + DailycostContract.DtInfoColumns.YEAR + "=? and " + DailycostContract.DtInfoColumns.MOTH + "=? ";
                            String[] selectionArgs = new String[]{mAccounBooid, mbilltype, calendar.get(Calendar.YEAR) + "", (calendar.get(Calendar.MONTH) + 1) + ""};
                            String groupBy = DailycostContract.DtInfoColumns.BILLCATEID;
                            String orderBy = "SUM(" + DailycostContract.DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                            String limit = "3";
                            list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, limit);


                        } else {
                            String selection = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? and " + DailycostContract.DtInfoColumns.BILLTYPE + "=?";
                            String[] selectionArgs = new String[]{mAccounBooid, mbilltype};
                            String groupBy = DailycostContract.DtInfoColumns.BILLCATEID;
                            String orderBy = "SUM(" + DailycostContract.DtInfoColumns.BILLAMOUNT + ")" + " DESC";
                            list = DbInterface.getTotalBalances(selection, selectionArgs, groupBy, null, orderBy, null);
                        }

                    }

                    Message msg = Message.obtain();
                    msg.obj = list;
                    msg.what = what;
                    handler.sendMessage(msg);
                }
            });
        } catch (RejectedExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成一个提交表单对象
     *
     * @param action 当前是add updata delete
     * @return
     */
    public static List<PostBill> getPostBillData(String action, List<DailycostEntity> entities) {
        List<PostBill> postBills = new ArrayList<PostBill>();
        if (entities == null) {
            return postBills;
        }
        final String mPkid = "";// 作为主键的Pkid

        PostBill.MemberlistBean mMemberlistBean = null;

        for (DailycostEntity dailycostEntity : entities) {
            String remark = dailycostEntity.getBillmark();
            PostBill mPostBill = new PostBill();

            mPostBill.setTradetime(Integer.parseInt(dailycostEntity.getTradetime()));// 需要修改
            mPostBill.setPkid(dailycostEntity.getPkid());//
            mPostBill.setAccountbookid(dailycostEntity.getAccountbookid());
            mPostBill.setCateid(dailycostEntity.getBillcateid());// 一级分类ID
            mPostBill.setAction(action);// 是添加删除修改
            mPostBill.setBillctime(Integer.parseInt(dailycostEntity.getBillctime()));// 时间选择器时间
            mPostBill.setBillclear(Integer.parseInt(dailycostEntity.getBillclear()));// 0创建时是未结算
            mPostBill.setBillamount(dailycostEntity.getBillamount());
            mPostBill.setBilltype(Integer.parseInt(dailycostEntity.getBilltype()));// 账单类型，收入，支出，转账
            // 需要修改
            mPostBill.setRemark(dailycostEntity.getBillmark());
            mPostBill.setAccountnumber(dailycostEntity.getAccountnumber());
            mPostBill.setBillid(dailycostEntity.getBillid());
            mPostBill.setBillimg(dailycostEntity.getBillimg());
            mPostBill.setAddress(dailycostEntity.getAddress());

            mPostBill.setAccountbooktype(Integer.parseInt(dailycostEntity.getAccountbooktype()));
            postBills.add(mPostBill);
            List<PostBill.MemberlistBean> memberlist = new ArrayList<>();
            if (dailycostEntity.getMemberlist() != null) {
                for (BillMemberInfo billMemberInfo : dailycostEntity.getMemberlist()) {

                    String memberid = billMemberInfo.getMemberid();
                    mMemberlistBean = new PostBill.MemberlistBean();
                    mMemberlistBean.setAmount(billMemberInfo.getAmount());
                    mMemberlistBean.setCtime(dailycostEntity.getUpdatetime());
                    mMemberlistBean.setMemberid(memberid);
                    mMemberlistBean.setStatus(Integer.parseInt(billMemberInfo.getStatus()));// 需要修改
                    mMemberlistBean.setType(Integer.parseInt(billMemberInfo.getType()));// 0.收入;1.支出;2.转账
                    memberlist.add(mMemberlistBean);
                }
            }

            mPostBill.setMemberlist(memberlist);
        }

        return postBills;
    }

}
