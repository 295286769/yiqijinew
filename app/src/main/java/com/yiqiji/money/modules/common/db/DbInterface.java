/**
 * DbInterface.java[V 2.0.0] Classs : .db.DbInterface Dingmao.SUN create at 2016年9月8日 下午3:46:09
 */
package com.yiqiji.money.modules.common.db;

import android.content.ContentResolver;

import com.yiqiji.money.modules.common.db.DailycostContract.DtInfoColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtSummaryConlumns;
import com.yiqiji.money.modules.common.entity.MyBooksListInfo;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;

import java.util.List;

/**
 * .db.DbInterface
 *
 * @author Dingmao.SUN <br/>
 *         Create at 2016年9月8日 下午3:46:09
 */
public class DbInterface implements DtInfoColumns, DtSummaryConlumns {

    public static long addDailycostInfo(DailycostEntity dailycost) {
        return DailycostEntity.addDailycostEntity(dailycost);
    }


    /**
     * 修改数据库的一条消费记录
     *
     * @param
     * @param dailycost
     * @return
     */
    public static long updateDataBaseDailycostInfo(DailycostEntity dailycost, String whereClause, String[] whereArgs) {
        return DailycostEntity.updateDataBaseDailycostEntity(dailycost, whereClause, whereArgs);
    }

    /**
     * 按条件查询账单
     *
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @return
     */
    public static List<DailycostEntity> queryDailycostEntitys(String selection, String[] selectionArgs, String groupBy,
                                                              String having, String orderBy, String limit) {
        return DailycostEntity.queryDailycostEntitys(selection, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 修改数据库的多条记录
     *
     * @param
     * @param
     * @return
     */
    public static long updateMultipleRecordsDailycostInfo(List<DailycostEntity> dailycostEntities) {
        return DailycostEntity.updateMultipleRecordsDailycostInfo(dailycostEntities);
    }


    /**
     * 删除数据库的账单和账单成员
     */
    public static long deleteDailycostEntityDataBase(String billid) {
        return DailycostEntity.deleteDailycostEntityDataBase(billid);
    }

    /**
     * 查询当前条目的对象
     *
     * @param
     * @param
     * @return
     */
    public static DailycostEntity getDataBaeDailycostEntity(String where, String[] selectionArgs, String groupBy,
                                                            String having, String orderBy, String limit) {

        return DailycostEntity.getDataBaeDailycostEntity(where, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 返回所有数据
     */
    public static List<DailycostEntity> getQueryDailycostEntitys(String selection, String[] selectionArgs,
                                                                 String groupBy, String having, String orderBy, String limit) {

        return DailycostEntity.queryDailycostEntitys(selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 同步数据库先删除后添加
     *
     * @param dailycostEntities
     * @return
     */
    public static long deleAllAndAdd(String where, String[] whereStrings, List<DailycostEntity> dailycostEntities) {

        return DailycostEntity.deleAllAndAdd(where, whereStrings, dailycostEntities);

    }

    /**
     * 根据月份分组求和以消费类目为分组(SQLiteDatabase)
     *
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public static List<TotalBalance> getTotalBalances(String selection, String[] selectionArgs, String groupBy,
                                                      String having, String orderBy, String limit) {
        return DailycostEntity.getTotalBalances(selection, selectionArgs, groupBy, having, orderBy, limit);
    }
    /**
     * 获取相同分类个数
     *
     * @param
     * @param
     * @param
     * @param
     * @return
     */
    public static int getSameClassification(String selection, String[] selectionArgs, String groupBy,
                                                      String having, String orderBy, String limit) {
        return DailycostEntity.getSameClassification(selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * 求当前月份收入和支出的总和天作为为分组
     *
     * @return
     */
    public static List<TotalBalance> getTotalBalanceTypeDay(ContentResolver contentResolver, int year, int moth,
                                                            String bookid) {
        String[] selectionArgs = new String[]{String.valueOf(year), String.valueOf(moth), bookid};

        return DailycostEntity.getTataolBlance(contentResolver, "which_type_day", selectionArgs,
                DailycostEntity.DEFAULT_SORT_ORDER);

    }

    /**
     * 求当前月份收入和支出的总和 收入和支出类型作为分组
     *
     * @return
     */
    public static List<TotalBalance> getTotalBalanceGroupByType(ContentResolver contentResolver, int year, int moth,
                                                                String bookid, String isDelet) {
        String[] selectionArgs = new String[]{String.valueOf(year), String.valueOf(moth), bookid, isDelet};

        return DailycostEntity.getTataolBlance(contentResolver, "group_type", selectionArgs,
                DailycostEntity.DEFAULT_SORT_ORDER);

    }

    /**
     * 根据月份求收入或支出一个月总和
     *
     * @param contentResolver
     * @param
     * @param type
     * @param type
     * @return
     */
    public static List<TotalBalance> getDailycostMothInfosTypeTotal(ContentResolver contentResolver, long start_time,
                                                                    long end_time, String bookid, String type) {
        String[] selectionArgs = new String[]{String.valueOf(start_time), String.valueOf(end_time), bookid, type, "false"};
        return DailycostEntity.getTataolBlance(contentResolver, "moths", selectionArgs,
                DailycostEntity.DEFAULT_SORT_ORDER);
    }


    /**
     * 添加账本列表
     *
     * @param
     * @param
     */
    public static long insertBookList(List<MyBooksListInfo> booksListInfos) {

        return MyBooksListInfo.inset(booksListInfos);

    }

    /**
     * 查询账本列表
     *
     * @param
     * @param
     */
    public static List<MyBooksListInfo> getMyBooksListInfo() {

        return MyBooksListInfo.getMyBooksListInfo();

    }

    /**
     * 添加账本
     *
     * @param
     * @param booksDbInfo
     */
    public static long insertBooks(BooksDbInfo booksDbInfo) {

        return BooksDbInfo.insert(booksDbInfo);

    }

    /**
     * 添加我的账本列表默认
     *
     * @param
     */
    public static long insertBooksMyList(List<BooksDbInfo> booksDbInfos) {

        return BooksDbInfo.insertBooksDefal(booksDbInfos);

    }

    /**
     * 先删除后插入
     *
     * @param
     */
    public static long deletAndInsert(List<BooksDbInfo> booksDbInfos) {
        return BooksDbInfo.deletAndInsert(booksDbInfos);
    }

    /**
     * 跟新账本
     */
    public static long updateBooks(BooksDbInfo booksDbInfo, String where, String... selectionArgs) {

        return BooksDbInfo.Update(booksDbInfo, where, selectionArgs);

    }

    /**
     * 删除账本
     *
     * @param
     * @param where
     * @param selectionArgs
     */
    public static long deletBooks(String where, String[] selectionArgs) {
        return BooksDbInfo.delet(where, selectionArgs);
    }

    /**
     * 查询账本
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static BooksDbInfo getBooksDbInfo(String where, String[] selectionArgs, String groupBy, String having,
                                             String orderBy, String limit) {
        return BooksDbInfo.getBooksDbInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 查询账本列表
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static List<BooksDbInfo> getListBooksDbInfo(String where, String[] selectionArgs, String groupBy,
                                                       String having, String orderBy, String limit) {
        return BooksDbInfo.getListBooksDbInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 添加成员
     *
     * @param
     * @param
     */
    public static long insertBooksMember(BooksDbMemberInfo booksDbMemberInfo) {

        return BooksDbMemberInfo.insert(booksDbMemberInfo);

    }

    /**
     * 更新成员
     */
    public static long updateBooksMember(BooksDbMemberInfo booksDbMemberInfo, String where, String[] selectionArgs) {
        return BooksDbMemberInfo.Update(booksDbMemberInfo, where, selectionArgs);

    }

    /**
     * 删除成员
     *
     * @param
     * @param where
     * @param selectionArgs
     */
    public static void deletBooksMember(String where, String... selectionArgs) {
        BooksDbMemberInfo.delet(where, selectionArgs);
    }

    /**
     * 成员同步删除后在添加成员
     *
     * @param
     * @param where
     * @param selectionArgs
     */
    public static long deletAndAddBooksMember(List<BooksDbMemberInfo> booksDbMemberInfos, String where,
                                              String... selectionArgs) {

        return BooksDbMemberInfo.deletAndAddBooksMember(booksDbMemberInfos, where, selectionArgs);
    }

    /**
     * 查询成员
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static BooksDbMemberInfo getBooksDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                         String having, String orderBy, String limit) {
        return BooksDbMemberInfo.getBooksDbMemberInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 查询成员列表
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static List<BooksDbMemberInfo> getListBooksDbMemberInfo(String where, String[] selectionArgs,
                                                                   String groupBy, String having, String orderBy, String limit) {
        return BooksDbMemberInfo.getListBooksDbMemberInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 添加账单成员
     *
     * @param
     * @param
     */
    public static long insertBillMember(BillMemberInfo billMemberInfo) {

        return BillMemberInfo.insert(billMemberInfo);

    }

    /**
     * 更新账单成员
     */
    public static long updateBillMember(BillMemberInfo billMemberInfo, String where, String[] selectionArgs) {
        return BillMemberInfo.update(billMemberInfo, where, selectionArgs);

    }
    /**
     * 更新账单成员
     */
    public static long updateBillMember(List<BillMemberInfo> billMemberInfos, String where, String billid) {
        return BillMemberInfo.update(billMemberInfos, where, billid);

    }

    /**
     * 删除账单成员
     *
     * @param
     * @param where
     * @param selectionArgs
     */
    public static void deletBillMember(String where, String... selectionArgs) {
        BillMemberInfo.delet(where, selectionArgs);
    }

    /**
     * 账单成员同步先删除后添加
     *
     * @param where
     * @param whereArgs
     * @return
     */
    public static long deletAndAdd(String where, String[] whereArgs, List<BillMemberInfo> billMemberInfos) {

        return BillMemberInfo.deletAndAdd(where, whereArgs, billMemberInfos);

    }

    /**
     * 查询账单成员
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static BillMemberInfo getBillDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                     String having, String orderBy, String limit) {
        return BillMemberInfo.getBillDbMemberInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

    /**
     * 查询账单成员列表
     *
     * @param
     * @param where
     * @param selectionArgs
     * @return
     */
    public static List<BillMemberInfo> getListBillDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                               String having, String orderBy, String limit) {
        return BillMemberInfo.getListBillDbMemberInfo(where, selectionArgs, groupBy, having, orderBy, limit);

    }

}
