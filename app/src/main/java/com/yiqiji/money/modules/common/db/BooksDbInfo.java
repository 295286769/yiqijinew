package com.yiqiji.money.modules.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.plication.MyApplicaction;

import java.util.ArrayList;
import java.util.List;

public class BooksDbInfo implements DailycostContract.DtBooksColumns, Parcelable {
    private static final long INVALID_ID = -1;

    private static final String[] BOOKS_COLUMNS = {_ID, DEVICEID, BOOKID, BOOKDESC, ACCOUNTBOOKCATENAME, ISSHOWTIME,
            ACCOUNTBOOKID, ACCOUNTBOOKTITLE, USERID, ACCOUNTBOOKCATE, ACCOUNTBOOKTYPE, ACCOUNTBOOKBUDGET,
            ACCOUNTBOOKSTATUS, ACCOUNTBOOKCOUNT, ISCLEAR, ACCOUNTBOOKCTIME, ACCOUNTBOOKUTIME, ACCOUNTBOOKCATEICON, ISADD,
            CREATETIMEBOOK, SORTTYPE, MYUID, ISSYNCHRONIZATION, ISDELET, PAYAMOUNT, RECEIVABLE, SPENTDIFF, MYSPENT,
            BUDGETDIFF, MYSPENTDIFF, ACCOUNTBOOKLTIME, ISNEW, FIRSTTIME, ACCOUNTBOOKBGIMG};

    public static ContentValues createContentValues(BooksDbInfo booksDbInfo) {
        ContentValues values = new ContentValues(1);
        // if (booksDbInfo.id != INVALID_ID) {
        // values.put(_ID, booksDbInfo.id);
        // }
        values.put(DEVICEID, booksDbInfo.deviceid);
        values.put(BOOKID, booksDbInfo.bookid);
        values.put(BOOKDESC, booksDbInfo.bookdesc);

        values.put(ISSHOWTIME, booksDbInfo.isShowTime);
        values.put(ISADD, booksDbInfo.isadd);
        values.put(CREATETIMEBOOK, booksDbInfo.createtime);
        values.put(ACCOUNTBOOKID, booksDbInfo.accountbookid);
        values.put(ACCOUNTBOOKTITLE, booksDbInfo.accountbooktitle);
        values.put(USERID, booksDbInfo.userid);

        values.put(ACCOUNTBOOKCATE, booksDbInfo.accountbookcate);
        values.put(ACCOUNTBOOKTYPE, booksDbInfo.accountbooktype);
        values.put(ACCOUNTBOOKBUDGET, booksDbInfo.accountbookbudget);
        values.put(ACCOUNTBOOKSTATUS, booksDbInfo.accountbookstatus);

        values.put(ACCOUNTBOOKCOUNT, booksDbInfo.accountbookcount);
        values.put(ISCLEAR, booksDbInfo.isclear);
        values.put(ACCOUNTBOOKCTIME, booksDbInfo.accountbookctime);
        values.put(ACCOUNTBOOKUTIME, booksDbInfo.accountbookutime);
        values.put(ACCOUNTBOOKCATEICON, booksDbInfo.accountbookcateicon);
        values.put(SORTTYPE, booksDbInfo.sorttype);
        values.put(MYUID, booksDbInfo.myuid);
        values.put(ACCOUNTBOOKCATENAME, booksDbInfo.accountbookcatename);
        values.put(ISSYNCHRONIZATION, booksDbInfo.issynchronization);
        values.put(ISDELET, booksDbInfo.isdelet);
        values.put(PAYAMOUNT, booksDbInfo.payamount);
        values.put(RECEIVABLE, booksDbInfo.receivable);
        values.put(SPENTDIFF, booksDbInfo.spentdiff);
        values.put(MYSPENT, booksDbInfo.myspent);
        values.put(BUDGETDIFF, booksDbInfo.budgetdiff);
        values.put(MYSPENTDIFF, booksDbInfo.myspentdiff);
        values.put(ACCOUNTBOOKLTIME, booksDbInfo.accountbookltime);
        values.put(ISNEW, booksDbInfo.isnew);
        values.put(FIRSTTIME, booksDbInfo.firsttime);
        values.put(ACCOUNTBOOKBGIMG, booksDbInfo.accountbookbgimg);
        return values;
    }

    public BooksDbInfo() {
        // TODO Auto-generated constructor stub
    }

    public BooksDbInfo(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns._ID));
        this.deviceid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.DEVICEID));
        this.bookid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.BOOKID));
        this.bookdesc = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.BOOKDESC));
        this.accountbookcatename = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKCATENAME));

        this.isShowTime = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISSHOWTIME));
        this.accountbookid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKID));
        this.accountbooktitle = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKTITLE));
        this.userid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.USERID));
        this.accountbookcate = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKCATE));

        this.accountbooktype = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKTYPE));
        this.accountbookbudget = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKBUDGET));
        this.accountbookstatus = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKSTATUS));
        this.accountbookcount = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKCOUNT));
        this.isclear = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISCLEAR));

        this.accountbookctime = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKCTIME));
        this.accountbookutime = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKUTIME));
        this.accountbookcateicon = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKCATEICON));
        this.myuid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.MYUID));
        this.isadd = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISADD));
        this.createtime = cursor.getLong(cursor.getColumnIndex(DailycostContract.DtBooksColumns.CREATETIMEBOOK));
        this.sorttype = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.SORTTYPE));
        this.issynchronization = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISSYNCHRONIZATION));
        this.isdelet = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISDELET));

        this.payamount = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.PAYAMOUNT));
        this.receivable = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.RECEIVABLE));
        this.spentdiff = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.SPENTDIFF));
        this.myspent = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.MYSPENT));
        this.budgetdiff = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.BUDGETDIFF));
        this.myspentdiff = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.MYSPENTDIFF));
        this.accountbookltime = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKLTIME));
        this.isnew = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ISNEW));
        this.firsttime = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.FIRSTTIME));
        this.accountbookbgimg = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBooksColumns.ACCOUNTBOOKBGIMG));
    }

    public static long insert(BooksDbInfo booksDbInfo) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues contentValues = createContentValues(booksDbInfo);
            id = database.insert(DailycostDatabaseHelper.BOOKS_TABLE_NAME, null, contentValues);
            if (id > -1 && database.isOpen()) {

                database.setTransactionSuccessful();
            }
            // database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
        return id;
    }

    /**
     * 先删除后插入
     *
     * @param
     */
    public static long deletAndInsert(List<BooksDbInfo> booksDbInfos) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.BOOKS_TABLE_NAME, null, null);
            if (id > -1) {
                for (BooksDbInfo booksDbInfo : booksDbInfos) {
                    ContentValues contentValues = createContentValues(booksDbInfo);
                    id = database.insert(DailycostDatabaseHelper.BOOKS_TABLE_NAME, null, contentValues);
                    if (id < 0) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    database.setTransactionSuccessful();
                }
            }

            // database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
        return id;
    }

    /**
     * 插入默认账本列表
     *
     * @param
     */
    public static long insertBooksDefal(List<BooksDbInfo> booksDbInfos) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            for (BooksDbInfo booksDbInfo : booksDbInfos) {
                ContentValues contentValues = createContentValues(booksDbInfo);
                id = database.insert(DailycostDatabaseHelper.BOOKS_TABLE_NAME, null, contentValues);
                if (id < 0) {
                    result = false;
                    break;
                }
            }
            if (result) {
                database.setTransactionSuccessful();
            }

            // database.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
        return id;
    }

    public static long Update(BooksDbInfo booksDbInfo, String where, String... whereArgs) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues contentValues = createContentValues(booksDbInfo);
            id = database.update(DailycostDatabaseHelper.BOOKS_TABLE_NAME, contentValues, where, whereArgs);
            if (id > -1) {
                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
        return id;
    }

    public static long delet(String where, String[] selectionArgs) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.BOOKS_TABLE_NAME, where, selectionArgs);
            if (id > -1) {
                String whereMember = DailycostContract.DtBookMemberColumns.BOOKID + "=? ";
                String whereMemberStrings[] = selectionArgs;
                database.delete(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, whereMember, whereMemberStrings);
                String whereDailycost = DailycostContract.DtInfoColumns.ACCOUNTBOOKID + "=? ";
                String whereDailycostStrings[] = selectionArgs;
                List<DailycostEntity> dailycostEntities = DailycostEntity.queryDailycostEntitys(whereDailycost, whereDailycostStrings, null, null, null, null);
                id = database.delete(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, where, selectionArgs);
                if (id > -1) {
                    for (DailycostEntity entitie : dailycostEntities) {
                        String whereBllMember = DailycostContract.DtBillMemberColumns.BILLID + "=? ";
                        String[] selectionArgsBllMember = {entitie.getBillid()};
                        database.delete(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, whereBllMember, selectionArgsBllMember);
                    }
                }


                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.endTransaction();
            }
        }
        // database.close();
        return id;
    }

    /**
     * @param where
     * @param selectionArgs 对应于selection语句中占位符的值，值在数组中的位置与占位符在语句中的位置必须一致，否则就会有异常。
     * @param groupBy       相当于select语句group by关键字后面的部分
     * @param having        相当于select语句having关键字后面的部分
     * @param orderBy       相当于select语句order by关键字后面的部分，如：personid desc, age asc;
     * @param limit         指定偏移量和获取的记录数，相当于select语句limit关键字后面的部分。
     * @return
     */
    public static BooksDbInfo getBooksDbInfo(String where, String[] selectionArgs, String groupBy, String having,
                                             String orderBy, String limit) {
        BooksDbInfo booksDbInfo = null;
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();

        Cursor cursor = database.query(DailycostDatabaseHelper.BOOKS_TABLE_NAME, BOOKS_COLUMNS, where, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                booksDbInfo = new BooksDbInfo(cursor);
            }
        } finally {
            cursor.close();
            // database.close();
        }

        return booksDbInfo;

    }

    public static List<BooksDbInfo> getListBooksDbInfo(String where, String[] selectionArgs, String groupBy,
                                                       String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        BooksDbInfo booksDbInfo = null;
        List<BooksDbInfo> booksDbInfos = new ArrayList<BooksDbInfo>();
        Cursor cursor = database.query(DailycostDatabaseHelper.BOOKS_TABLE_NAME, BOOKS_COLUMNS, where, selectionArgs,
                groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                do {
                    booksDbInfo = new BooksDbInfo(cursor);
                    booksDbInfos.add(booksDbInfo);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            cursor.close();
            // database.close();
        }

        return booksDbInfos;

    }

    private long id;
    private String deviceid = ""; // 设备ID
    private String bookid = "";// 作废
    private String bookdesc = ""; // book描述
    private String accountbookcatename = "";// 账本分类名称
    private String isShowTime = "";//
    private String accountbookid = ""; // 账本ID
    private String accountbooktitle = "";// 账本名称
    private String userid = "";
    private String accountbookcate = ""; // 账本分类
    private String accountbooktype = ""; // 类型：0.单人，1多人
    private String accountbookbudget = "0.00"; // 账本预算费
    private String accountbookstatus;
    private String accountbookcount; // 账本人数
    private String isclear; // 是否需要结算：0.否，1是
    private String accountbookctime;// 账本创建时间
    private String accountbookutime;// 更新账本时间
    private String accountbookltime;// 最新操作时间
    private String isnew = "0";// 是否是新账本
    private String accountbookcateicon; // 账本图标
    private int isadd;// 0:没有添加1：已经添加
    private long createtime;// 账本创建时间
    private String sorttype = "1";// 1:显示月份tab1：不显示月份0
    private String myuid = "";// 当前操作人id
    private String issynchronization;// 是否同步false没有同步
    private String isdelet;// 是否删除false没有删除
    // 不写入数据库字段
    private String payamount = "0.00"; // 本月支出/累计支出
    private String receivable = "0.00"; // 本月收入/累计收入
    private String spentdiff = "0.00"; // 本月结余/累计结余
    private String myspent = "0.00"; // 我的消费/我的支出
    private String budgetdiff = "0.00"; // 预算差额(当值大于0，表示预算剩余；当值小于0，表示预算超支)
    private String myspentdiff = "0.00"; // 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
    private List<BooksDbMemberInfo> member;
    private String firsttime = "";   // 第一次账单发生时间
    private String isopen = "0";   // 是否分享至社区

    private int code;
    private String msg;
    private String accountbookbgimg;//新增账本背景

    public String getAccountbookbgimg() {
        return accountbookbgimg;
    }

    public void setAccountbookbgimg(String accountbookbgimg) {
        this.accountbookbgimg = accountbookbgimg;
    }

    public List<BooksDbMemberInfo> getMember() {
        return member;
    }

    public void setMember(List<BooksDbMemberInfo> member) {
        this.member = member;
    }

    public String getIssynchronization() {
        return issynchronization;
    }

    public void setIssynchronization(String issynchronization) {
        this.issynchronization = issynchronization;
    }

    public String getIsdelet() {
        return isdelet;
    }

    public void setIsdelet(String isdelet) {
        this.isdelet = isdelet;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getReceivable() {
        return receivable;
    }

    public void setReceivable(String receivable) {
        this.receivable = receivable;
    }

    public String getSpentdiff() {
        return spentdiff;
    }

    public void setSpentdiff(String spentdiff) {
        this.spentdiff = spentdiff;
    }

    public String getMyspent() {
        return myspent;
    }

    public void setMyspent(String myspent) {
        this.myspent = myspent;
    }

    public String getBudgetdiff() {
        return budgetdiff;
    }

    public void setBudgetdiff(String budgetdiff) {
        this.budgetdiff = budgetdiff;
    }

    public String getMyspentdiff() {
        return myspentdiff;
    }

    public void setMyspentdiff(String myspentdiff) {
        this.myspentdiff = myspentdiff;
    }

    public String getAccountbookcatename() {
        return accountbookcatename;
    }

    public void setAccountbookcatename(String accountbookcatename) {
        this.accountbookcatename = accountbookcatename;
    }

    public String getMyuid() {
        return myuid;
    }

    public void setMyuid(String myuid) {
        this.myuid = myuid;
    }

    public String getSorttype() {
        return sorttype;
    }

    public void setSorttype(String sorttype) {
        this.sorttype = sorttype;
    }

    public long getCreatetime() {
        return createtime;
    }

    public void setCreatetime(long createtime) {
        this.createtime = createtime;
    }

    public int getIsadd() {
        return isadd;
    }

    public void setIsadd(int isadd) {
        this.isadd = isadd;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getBookdesc() {
        return bookdesc;
    }

    public void setBookdesc(String bookdesc) {
        this.bookdesc = bookdesc;
    }

    public String getIsShowTime() {
        return isShowTime;
    }

    public void setIsShowTime(String isShowTime) {
        this.isShowTime = isShowTime;
    }

    public String getAccountbookid() {
        return accountbookid;
    }

    public void setAccountbookid(String accountbookid) {
        this.accountbookid = accountbookid;
    }

    public String getAccountbooktitle() {
        return accountbooktitle;
    }

    public void setAccountbooktitle(String accountbooktitle) {
        this.accountbooktitle = accountbooktitle;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccountbookcate() {
        return accountbookcate;
    }

    public void setAccountbookcate(String accountbookcate) {
        this.accountbookcate = accountbookcate;
    }

    public String getAccountbooktype() {
        return accountbooktype;
    }

    public void setAccountbooktype(String accountbooktype) {
        this.accountbooktype = accountbooktype;
    }

    public String getAccountbookbudget() {
        return accountbookbudget;
    }

    public void setAccountbookbudget(String accountbookbudget) {
        this.accountbookbudget = accountbookbudget;
    }

    public String getAccountbookstatus() {
        return accountbookstatus;
    }

    public void setAccountbookstatus(String accountbookstatus) {
        this.accountbookstatus = accountbookstatus;
    }

    public String getAccountbookcount() {
        return accountbookcount;
    }

    public void setAccountbookcount(String accountbookcount) {
        this.accountbookcount = accountbookcount;
    }

    public String getIsclear() {
        return isclear;
    }

    public void setIsclear(String isclear) {
        this.isclear = isclear;
    }

    public String getAccountbookctime() {
        return accountbookctime;
    }

    public void setAccountbookctime(String accountbookctime) {
        this.accountbookctime = accountbookctime;
    }

    public String getAccountbookutime() {
        return accountbookutime;
    }

    public void setAccountbookutime(String accountbookutime) {
        this.accountbookutime = accountbookutime;
    }

    public String getAccountbookcateicon() {
        return accountbookcateicon;
    }

    public void setAccountbookcateicon(String accountbookcateicon) {
        this.accountbookcateicon = accountbookcateicon;
    }

    public String getAccountbookltime() {
        return accountbookltime;
    }

    public void setAccountbookltime(String accountbookltime) {
        this.accountbookltime = accountbookltime;
    }

    public String getIsnew() {
        return isnew;
    }

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

    public void setIsnew(String isnew) {
        this.isnew = isnew;
    }

    public static long getInvalidId() {
        return INVALID_ID;
    }

    public static String[] getBooksColumns() {
        return BOOKS_COLUMNS;
    }

    public String getFirsttime() {
        return firsttime;
    }

    public void setFirsttime(String firsttime) {
        this.firsttime = firsttime;
    }

    public String getIsopen() {
        return isopen;
    }

    public void setIsopen(String isopen) {
        this.isopen = isopen;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeLong(id);
        dest.writeString(deviceid);
        dest.writeString(bookid);
        dest.writeString(bookdesc);
        dest.writeString(accountbookcatename);

        dest.writeString(isShowTime);
        dest.writeString(accountbookid);
        dest.writeString(accountbooktitle);
        dest.writeString(userid);
        dest.writeString(accountbookcate);

        dest.writeString(accountbooktype);
        dest.writeString(accountbookbudget);
        dest.writeString(accountbookstatus);
        dest.writeString(accountbookcount);
        dest.writeString(isclear);

        dest.writeString(accountbookctime);
        dest.writeString(accountbookutime);
        dest.writeString(accountbookcateicon);
        dest.writeInt(isadd);
        dest.writeLong(createtime);

        dest.writeString(sorttype);
        dest.writeString(myuid);
        dest.writeString(payamount);
        dest.writeString(receivable);
        dest.writeString(spentdiff);

        dest.writeString(myspent);
        dest.writeString(budgetdiff);
        dest.writeString(myspentdiff);
        dest.writeString(issynchronization);
        dest.writeString(isdelet);
        dest.writeString(accountbookltime);
        dest.writeString(isnew);
        dest.writeList(member);
        dest.writeInt(code);
        dest.writeString(msg);
        dest.writeString(firsttime);
        dest.writeString(accountbookbgimg);
        dest.writeString(isopen);
    }

    public static final Creator<BooksDbInfo> CREATOR = new Creator<BooksDbInfo>() {
        @Override
        public BooksDbInfo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            return new BooksDbInfo(source);
        }

        @Override
        public BooksDbInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new BooksDbInfo[size];
        }
    };

    public BooksDbInfo(Parcel p) {

        this.id = p.readLong();
        this.deviceid = p.readString(); // 设备ID
        this.bookid = p.readString();
        this.bookdesc = p.readString(); // book描述
        this.accountbookcatename = p.readString();// 账本分类名称

        this.isShowTime = p.readString();// 1:显示月份tab1：不显示月份0
        this.accountbookid = p.readString(); // 账本ID
        this.accountbooktitle = p.readString();// 账本名称
        this.userid = p.readString();
        this.accountbookcate = p.readString(); // 账本分类

        this.accountbooktype = p.readString(); // 类型：0.单人，1多人
        this.accountbookbudget = p.readString(); // 账本预算费
        this.accountbookstatus = p.readString();
        this.accountbookcount = p.readString(); // 账本人数
        this.isclear = p.readString(); // 是否需要结算：0.否，1是

        this.accountbookctime = p.readString();// 账本创建时间
        this.accountbookutime = p.readString();// 更新账本时间
        this.accountbookcateicon = p.readString(); // 账本图标
        this.isadd = p.readInt();// 0:没有添加1：已经添加
        this.createtime = p.readLong();// 账本创建时间

        this.sorttype = p.readString();// 1:
        this.myuid = p.readString();// 当前操作人id
        // 不写入数据库字段
        this.payamount = p.readString(); // 本月支出/累计支出
        this.receivable = p.readString(); // 本月收入/累计收入
        this.spentdiff = p.readString(); // 本月结余/累计结余

        this.myspent = p.readString(); // 我的消费/我的支出
        this.budgetdiff = p.readString(); // 预算差额(当值大于0，表示预算剩余；当值小于0，表示预算超支)
        this.myspentdiff = p.readString(); // 我需付（当值小于0）/我应收（当值大于0）/已结清（当值等于0）
        this.issynchronization = p.readString();
        this.isdelet = p.readString();
        this.accountbookltime = p.readString();
        this.isnew = p.readString();
        this.member = p.readArrayList(BooksDbMemberInfo.class.getClassLoader());
        this.code = p.readInt();
        this.msg = p.readString();
        this.firsttime = p.readString();
        this.accountbookbgimg = p.readString();
        this.isopen = p.readString();
    }

}
