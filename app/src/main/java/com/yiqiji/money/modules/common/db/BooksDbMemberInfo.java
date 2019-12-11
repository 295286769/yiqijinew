package com.yiqiji.money.modules.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.plication.MyApplicaction;

import java.util.ArrayList;
import java.util.List;

public class BooksDbMemberInfo implements DailycostContract.DtBookMemberColumns, Parcelable {
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(userid);
        dest.writeString(memberid);
        dest.writeString(bookid);
        dest.writeString(username);
        dest.writeString(bookName);
        dest.writeString(usericon);
        dest.writeInt(isclear);
        dest.writeInt(settlement);
        dest.writeInt(index);
        dest.writeInt(ispaymen);
        dest.writeDouble(balance);
        dest.writeFloat(amount);
        dest.writeInt(status);
        dest.writeString(type);
        dest.writeString(deviceid);
        dest.writeInt((isChange ? 1 : 0));
    }

    public static final Creator<BooksDbMemberInfo> CREATOR = new Creator<BooksDbMemberInfo>() {
        public BooksDbMemberInfo createFromParcel(Parcel in) {
            return new BooksDbMemberInfo(in);
        }

        public BooksDbMemberInfo[] newArray(int size) {
            return new BooksDbMemberInfo[size];
        }
    };

    private BooksDbMemberInfo(Parcel in) {
        id = in.readLong();
        userid = in.readString();
        memberid = in.readString();
        bookid = in.readString();
        username = in.readString();
        bookName = in.readString();
        usericon = in.readString();
        isclear = in.readInt();
        settlement = in.readInt();
        index = in.readInt();
        ispaymen = in.readInt();
        balance = in.readDouble();
        amount = in.readFloat();
        status = in.readInt();
        type = in.readString();
        deviceid = in.readString();
        isChange = (in.readInt() == 1) ? true : false;
    }

    public BooksDbMemberInfo() {
        // TODO Auto-generated constructor stub
    }

    private static final long INVALID_ID = -1;
    private static final String[] BOOKSMEMBER_COLUMNS = {_ID, USERID, MEMBERID, USERNAME, USERISCLEAR, BOOKNAME,
            HEADICON, INDEX, SETTLEMENT, ISPAYMEN, BALANCEMEMBER, BOOKID, DEVICEID};

    public static ContentValues createContentValues(BooksDbMemberInfo booksDbMemberInfo) {
        ContentValues values = new ContentValues(1);
        // if (booksDbMemberInfo.id != INVALID_ID) {
        // values.put(_ID, booksDbMemberInfo.id);
        // }
        values.put(USERID, booksDbMemberInfo.userid);
        values.put(MEMBERID, booksDbMemberInfo.memberid);
        values.put(BOOKID, booksDbMemberInfo.bookid);
        values.put(USERNAME, booksDbMemberInfo.username);
        values.put(USERISCLEAR, booksDbMemberInfo.isclear);
        values.put(BOOKNAME, booksDbMemberInfo.bookName);
        values.put(HEADICON, booksDbMemberInfo.usericon);
        values.put(INDEX, booksDbMemberInfo.index);
        values.put(SETTLEMENT, booksDbMemberInfo.settlement);
        values.put(ISPAYMEN, booksDbMemberInfo.ispaymen);
        values.put(BALANCEMEMBER, booksDbMemberInfo.balance);
        values.put(BALANCEMEMBER, booksDbMemberInfo.balance);
        values.put(DEVICEID, booksDbMemberInfo.deviceid);

        return values;
    }

    public BooksDbMemberInfo(Cursor cursor) {
        this.id = cursor.getLong(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns._ID));
        this.userid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.USERID));
        this.memberid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.MEMBERID));
        this.bookid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.BOOKID));
        this.username = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.USERNAME));
        this.isclear = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.USERISCLEAR));
        this.index = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.INDEX));
        this.settlement = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.SETTLEMENT));
        this.ispaymen = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.ISPAYMEN));
        this.bookName = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.BOOKNAME));
        this.usericon = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.HEADICON));
        this.balance = cursor.getDouble(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.BALANCEMEMBER));
        this.deviceid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBookMemberColumns.DEVICEID));
    }

    public static long insert(BooksDbMemberInfo booksDbInfo) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues contentValues = createContentValues(booksDbInfo);
            id = database.insert(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, null, contentValues);
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

    public static long insertList(List<BooksDbMemberInfo> booksDbMemberInfos) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues contentValues;
            for (BooksDbMemberInfo booksDbMemberInfo : booksDbMemberInfos) {
                contentValues = createContentValues(booksDbMemberInfo);
                id = database.insert(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, null, contentValues);
                if (id < 0) {
                    result = false;
                    break;
                }
            }
            if (result) {
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

    public static long Update(BooksDbMemberInfo booksDbInfo, String where, String... selectionArgs) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues contentValues = createContentValues(booksDbInfo);
            id = database.update(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, contentValues, where, selectionArgs);
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

    public static void delet(String where, String... selectionArgs) {
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, where, selectionArgs);
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

    }

    public static long deletAndAddBooksMember(List<BooksDbMemberInfo> booksDbMemberInfos, String where,
                                              String... selectionArgs) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, where, selectionArgs);
            if (id > -1) {
                for (BooksDbMemberInfo booksDbMemberInfo : booksDbMemberInfos) {
                    ContentValues values = createContentValues(booksDbMemberInfo);
                    id = database.insert(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, null, values);
                    if (id < 0) {
                        result = false;
                        break;
                    }
                }
                if (id > -1) {
                    database.setTransactionSuccessful();
                }
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

    public static BooksDbMemberInfo getBooksDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                         String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        BooksDbMemberInfo booksDbMemberInfo = null;

        Cursor cursor = database.query(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, BOOKSMEMBER_COLUMNS, where,
                selectionArgs, groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                booksDbMemberInfo = new BooksDbMemberInfo(cursor);
            }
        } finally {
            cursor.close();
            // database.close();
        }

        return booksDbMemberInfo;

    }

    public static List<BooksDbMemberInfo> getListBooksDbMemberInfo(String where, String[] selectionArgs,
                                                                   String groupBy, String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        List<BooksDbMemberInfo> dbMemberInfos = new ArrayList<BooksDbMemberInfo>();
        BooksDbMemberInfo booksDbMemberInfo = null;

        Cursor cursor = database.query(DailycostDatabaseHelper.BOOKS_MEMBER_TABLE_NAME, BOOKSMEMBER_COLUMNS, where,
                selectionArgs, groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    booksDbMemberInfo = new BooksDbMemberInfo(cursor);
                    dbMemberInfos.add(booksDbMemberInfo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            cursor.close();
            // database.close();
        }

        return dbMemberInfos;

    }

    private long id;
    private String userid; // 用户id
    private String memberid; // 成员id
    private String bookid; // 账本id
    private String username;// 成员名称
    private String bookName;// 账本名称
    private String usericon;// 头像icon
    private int isclear;// 是否需要结算
    private int settlement;// 0:没有结算1：结算
    private int index;// 头像下标
    private int ispaymen;// 是否是付款人0：是1：不是
    private double balance;// 付款金额
    // 不写入数据库字段，只作为json解析用
    private float amount;// 金额
    private int status;// 状态
    private String type;// 类型
    private String deviceid;
    private boolean isChange;

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean isChange) {
        this.isChange = isChange;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIspaymen() {
        return ispaymen;
    }

    public void setIspaymen(int ispaymen) {
        this.ispaymen = ispaymen;
    }

    public int getSettlement() {
        return settlement;
    }

    public void setSettlement(int settlement) {
        this.settlement = settlement;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIsclear() {
        return isclear;
    }

    public void setIsclear(int isclear) {
        this.isclear = isclear;
    }

}
