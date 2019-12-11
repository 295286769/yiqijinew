/**
 * DailycostDatabaseHelper.java[V 2.0.0] Classs : .db.DailycostDatabaseHelper Dingmao.SUN create
 * at 2016年9月8日 下午3:44:43
 */
package com.yiqiji.money.modules.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yiqiji.money.modules.common.config.RequsterTag;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBillMemberColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBookListColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBookMemberColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtBooksColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtInfoColumns;
import com.yiqiji.frame.core.utils.LogUtil;

/**
 * .db.DailycostDatabaseHelper
 *
 * @author Dingmao.SUN <br/>
 *         Create at 年9月8日 下午3:44:43
 */
public class DailycostDatabaseHelper extends SQLiteOpenHelper implements DtInfoColumns,
        DtBookMemberColumns, DtBooksColumns, DtBookListColumns {

    // Database and table names
    public static final String DATABASE_NAME = "dailycost.db";
    static final String DAILYCOST_TABLE_NAME = "dailycost";
    public static final String BOOKS_TABLE_NAME = "booksInfo";
    public static final String BOOKS_MEMBER_TABLE_NAME = "booksMemberInfo";
    public static final String BILL_MEMBER_TABLE_NAME = "billMemberInfo";
    public static final String BOOK_LIST_TABLE_NAME = "booklist";

    /**
     * @param db
     */
    private static void createDailycostTable(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DAILYCOST_TABLE_NAME + " (" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DtInfoColumns.USERID + "  TEXT NOT NULL , "
                + BILLTYPE + " TEXT NOT NULL, " + BILLAMOUNT + " TEXT, " + COSTTYPE + " INTEGER, " + CONTEXT
                + " TEXT, " + DtInfoColumns.DEVICEID + " TEXT, " + BILLSTATUS + " TEXT, " + BILLCTIME + " TEXT, "
                + TRADETIME + " TEXT, " + UPDATETIME + " INTEGER NOT NULL, " + WAGESID + " INTEGER, " + ISDELETED
                + " BOOLEAN, " + DtInfoColumns.ISCLEAR + " TEXT, " + LNG + " FLOAT, " + LAT + " FLOAT, " + BILLMARK
                + " TEXT, " + EXTRAINFO1 + " TEXT, " + EXTRAINFO2 + " TEXT, " + EXTRAINFO3 + " TEXT, " + YEAR
                + " TEXT, " + MOTH + " TEXT, " + DAY + " TEXT, " + DtInfoColumns.ISSYNCHRONIZATION + " TEXT, "
                + WHICHBOOK + " TEXT, " + DtInfoColumns.ACCOUNTBOOKTYPE + " TEXT, " + BILLSUBCATENAME + " TEXT, "
                + BILLSUBCATEICON + " TEXT, " + ACCOUNTNUMBER + " TEXT, " + BILLCOUNT + " TEXT, " + BILLCATEID
                + " TEXT, " + BILLSUBCATEID + " TEXT, " + BILLCATENAME + " TEXT, " + BILLCATEICON + " TEXT, "
                + DtInfoColumns.ACCOUNTBOOKID + " TEXT, " + DtInfoColumns.USERNAME + " TEXT, " + DtInfoColumns.USERICON
                + " TEXT, " + PKID + " TEXT, " + BILLID + " TEXT, " + BILLIMG + " TEXT, " + ADDRESS + " TEXT, " + BILLCLEAR + " TEXT, " + BILLBRAND + " TEXT, " +
                "CONSTRAINT 不重复规则 UNIQUE" + "(deviceid,billctime,tradetime,billamount)" + " ON CONFLICT REPLACE " + ");");
    }

    public static void createBookInfos(SQLiteDatabase db) {// 账本表

        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOKS_TABLE_NAME + "	(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DtBooksColumns.DEVICEID + " TEXT, "
                + DtBooksColumns.BOOKID + " TEXT, " + BOOKDESC + " TEXT, " + ACCOUNTBOOKCATENAME + " TEXT, "
                + ISSHOWTIME + " TEXT, " + DtBooksColumns.ACCOUNTBOOKID + " TEXT, " + ACCOUNTBOOKTITLE + " TEXT, "
                + DtBooksColumns.USERID + " TEX, " + ACCOUNTBOOKCATE + " TEXT, " + DtBooksColumns.ACCOUNTBOOKTYPE
                + " TEXT, " + ACCOUNTBOOKBUDGET + " TEXT, " + ACCOUNTBOOKSTATUS + " TEXT, " + ACCOUNTBOOKCOUNT
                + " TEXT, " + DtBooksColumns.ISCLEAR + " TEXT, " + ACCOUNTBOOKCTIME + " TEXT, " + ACCOUNTBOOKUTIME
                + " TEXT, " + PAYAMOUNT + " TEXT, " + RECEIVABLE + " TEXT, " + SPENTDIFF + " TEXT, " + MYSPENT
                + " TEXT, " + BUDGETDIFF + " TEXT, " + MYSPENTDIFF + " TEXT, " + MYUID + " TEXT, " + ISADD + " TEXT, " +
                ACCOUNTBOOKLTIME + " TEXT, " + ISNEW + " TEXT, "
                + CREATETIMEBOOK + " TEXT, " + SORTTYPE + " TEXT, " + ISDELET + " TEXT, " + FIRSTTIME + " TEXT, "
                + DtBooksColumns.ISSYNCHRONIZATION + " TEXT, " + ACCOUNTBOOKCATEICON + " TEXT, " + ACCOUNTBOOKBGIMG + " TEXT " + ");");
    }

    public static void createBookList(SQLiteDatabase db) {// 账本分类列表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOK_LIST_TABLE_NAME + "	(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + CATEGORYID + " TEXT, " + CATEGORYTITLE + " TEXT, "
                + CATEGORYDESC + " TEXT, " + CATEGORYTYPE + " TEXT, " + CATEGORYICON + " TEXT, " + PARENTID + " TEXT, "
                + STATUS + " TEXT, " + DtBookListColumns.ISCLEAR + " TEXT " + ");");
    }

    public static void createBookMemberInfos(SQLiteDatabase db) {// 账本成员表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BOOKS_MEMBER_TABLE_NAME + "	(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DtBookMemberColumns.USERID + " TEXT, "
                + DtBookMemberColumns.MEMBERID + " TEXT, " + DtBookMemberColumns.USERNAME + " TEXT, " + USERISCLEAR
                + " TEXT, " + HEADICON + " TEXT, " + INDEX + " TEXT, " + SETTLEMENT + " TEXT, " + ISPAYMEN + " TEXT, "
                + BALANCEMEMBER + " TEXT, " + DtBookMemberColumns.BOOKID + " TEXT, " + BOOKNAME + " TEXT ,"
                + DtBookMemberColumns.DEVICEID + " TEXT " + ");");
    }

    public static void createBillMemberInfos(SQLiteDatabase db) {// 账单成员表
        db.execSQL("CREATE TABLE IF NOT EXISTS " + BILL_MEMBER_TABLE_NAME + "	(" + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + DtBillMemberColumns.USERID + " TEXT, "
                + DtBillMemberColumns.MEMBERID + " TEXT, " + DtBillMemberColumns.TYPE + " TEXT, "
                + DtBillMemberColumns.AMOUNT + " TEXT, " + DtBillMemberColumns.STATUS + " TEXT, "
                + DtBillMemberColumns.BILLID + " TEXT, " + DtBillMemberColumns.USERNAME + " TEXT, "
                + DtBillMemberColumns.USERICON + " TEXT, " + DtBillMemberColumns.INDEX + " TEXT, "
                + DtBillMemberColumns.DEVICEID + " TEXT, " + DtBillMemberColumns.ISDELETED + " TEXT, "
                + DtBillMemberColumns.ISSYNCHRONIZATION + " TEXT " + ");");
    }

    public static void dropDailycostTable(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + DAILYCOST_TABLE_NAME + ";");
        db.execSQL("DROP VIEW IF EXISTS " + BOOKS_TABLE_NAME + ";");
        db.execSQL("DROP VIEW IF EXISTS " + BOOKS_MEMBER_TABLE_NAME + ";");
        db.execSQL("DROP VIEW IF EXISTS " + BILL_MEMBER_TABLE_NAME + ";");
        db.execSQL("DROP VIEW IF EXISTS " + BOOK_LIST_TABLE_NAME + ";");
    }

    /**
     * @param context
     */
    public DailycostDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, RequsterTag.VERSION);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createDailycostTable(db);
        createBookInfos(db);
        createBookMemberInfos(db);
        createBillMemberInfos(db);
        createBookList(db);
    }


    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        LogUtil.log_msg("newVersion+++++++++" + newVersion);
        LogUtil.log_msg("oldVersion+++++++++" + oldVersion);
        if (oldVersion != newVersion && RequsterTag.VERSION == newVersion) {
            // 使用for实现跨版本升级数据库
            for (int i = oldVersion; i < newVersion; i++) {

                switch (i) {
                    case 1:
                        upgradeToVersion1001(db);
                        break;
                    case 2:
                        break;
                    case 3:
                        dropTable(db);
                        onCreate(db);
//                        alterCloumn1003(
//                                db,
//                                DAILYCOST_TABLE_NAME,
//                                updateDailycostItem,
//                                "INSERT INTO dailycost SELECT _id, billamount " + "FROM tempTable");

                        break;
                    case 4:
                        dropTable(db);
                        onCreate(db);
//                        alterCloumn1003(
//                                db,
//                                BOOKS_TABLE_NAME,
//                                updateBookItem,
//                                "INSERT INTO booksInfo SELECT " + select + "FROM tempTable");


                        break;
                    case 5:
                        upgradeToVersion1006(db);
//                        dropTable(db);
//                        onCreate(db);
//                        upgradeToVersion1005(db);
                        break;
//                    case 6:
//                        upgradeToVersion1006(db);
//                        break;
                    case 6:
                        upgradeToVersion1007(db);
                        break;

                    default:
                        break;
                }
            }


        }
    }

    private void upgradeToVersion1001(SQLiteDatabase db) {
        // 账单表新增1个字段
        String sql1 = "ALTER TABLE " + DAILYCOST_TABLE_NAME + " ADD COLUMN billimg VARCHAR";
        String sql2 = "ALTER TABLE " + DAILYCOST_TABLE_NAME + " ADD COLUMN address VARCHAR";
        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    private void upgradeToVersion1005(SQLiteDatabase db) {
        // 账本表表新增1个字段
        String sql1 = "ALTER TABLE " + BOOKS_TABLE_NAME + " ADD COLUMN firsttime VARCHAR";
        db.execSQL(sql1);
    }

    private void upgradeToVersion1006(SQLiteDatabase db) {
        // 账单表新增1个字段
        String sql1 = "ALTER TABLE " + BOOKS_TABLE_NAME + " ADD COLUMN accountbookbgimg VARCHAR";
        db.execSQL(sql1);
    }

    private void upgradeToVersion1007(SQLiteDatabase db) {
        // 账单表新增1个字段
        String sql1 = "ALTER TABLE " + DAILYCOST_TABLE_NAME + " ADD COLUMN billbrand VARCHAR";
        db.execSQL(sql1);
    }


    // 删除字段或则修改字段的方法
    public void alterCloumn1003(SQLiteDatabase db, String alterTableName,
                                String create_Table_Sql, String copy_Sql) {

        final String DROP_TEMP_TABLE = "drop table if exists tempTable";
        // 重新命名修改的表
        db.execSQL("alter table " + alterTableName + " rename to tempTable");
        // 重新创建修改的表
        db.execSQL(create_Table_Sql);
        // 将临时表里的数据copy到新的数据库中
        db.execSQL(copy_Sql);
        // 最后删掉临时表
        db.execSQL(DROP_TEMP_TABLE);
    }

    public void dropTable(SQLiteDatabase db) {
        final String DROP_TEMP_TABLE = "drop table if exists dailycost";
        final String DROP_TEMP_TABLE_BOOK = "drop table if exists booksInfo";
        final String DROP_TEMP_TABLE_BOOK_MEMBER = "drop table if exists booksMemberInfo";
        final String DROP_TEMP_TABLE_BILL_MEMBER = "drop table if exists billMemberInfo";
        final String DROP_TEMP_TABLE_LIST = "drop table if exists booklist";
        db.execSQL(DROP_TEMP_TABLE);
        db.execSQL(DROP_TEMP_TABLE_BOOK);
        db.execSQL(DROP_TEMP_TABLE_BOOK_MEMBER);
        db.execSQL(DROP_TEMP_TABLE_BILL_MEMBER);
        db.execSQL(DROP_TEMP_TABLE_LIST);
    }
}
