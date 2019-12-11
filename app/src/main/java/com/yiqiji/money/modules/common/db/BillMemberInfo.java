package com.yiqiji.money.modules.common.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.plication.MyApplicaction;

import java.util.ArrayList;
import java.util.List;

public class BillMemberInfo implements DailycostContract.DtBillMemberColumns, Parcelable {
    public BillMemberInfo() {
        // TODO Auto-generated constructor stub
    }

    private final static String[] BILLSMEMBER_COLUMNS = new String[]{_ID, USERID, MEMBERID, TYPE, AMOUNT, STATUS,
            USERNAME, BILLID, USERICON, INDEX, ISDELETED, ISSYNCHRONIZATION, DEVICEID};

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
        dest.writeString(type);
        dest.writeString(amount);
        dest.writeString(status);
        dest.writeString(username);
        dest.writeString(usericon);
        dest.writeString(billid);
        dest.writeInt(indexid);
        dest.writeString(isdeleted);
        dest.writeString(issynchronization);
        dest.writeString(deviceid);
        dest.writeString(accountbookid);
        dest.writeString(clearid);
        dest.writeString(clearamount);
        dest.writeString(clearstatus);
        dest.writeString(clearctime);
        dest.writeString(clearutime);
        dest.writeString(reationid);
    }

    public static final Creator<BillMemberInfo> CREATOR = new Creator<BillMemberInfo>() {
        public BillMemberInfo createFromParcel(Parcel in) {
            return new BillMemberInfo(in);
        }

        public BillMemberInfo[] newArray(int size) {
            return new BillMemberInfo[size];
        }
    };

    private BillMemberInfo(Parcel in) {
        id = in.readLong();
        userid = in.readString();
        memberid = in.readString();
        type = in.readString();
        amount = in.readString();
        status = in.readString();
        username = in.readString();
        usericon = in.readString();
        billid = in.readString();
        indexid = in.readInt();
        isdeleted = in.readString();
        issynchronization = in.readString();
        deviceid = in.readString();
        accountbookid = in.readString();
        clearid = in.readString();
        clearamount = in.readString();
        clearstatus = in.readString();
        clearctime = in.readString();
        clearutime = in.readString();
        reationid = in.readString();
    }

    // db.beginTransaction(); // 手动设置开始事务
    // for (ContentValues v : list) {
    // db.insert("bus_line_station", null, v);
    // }
    // db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
    // db.endTransaction(); // 处理完成
    // db.close()

    public static void insertList(List<BillMemberInfo> billMemberInfoList) {
        SQLiteDatabase sqLiteDatabase = null;
        boolean result = true;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();

            sqLiteDatabase.beginTransaction();

            for (BillMemberInfo billMemberInfo : billMemberInfoList) {
                ContentValues contentValues = createContentValues(billMemberInfo);
                long id = sqLiteDatabase.insert(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, null, contentValues);
                if (id < 0) {
                    result = false;
                    break;
                }
            }
            if (result) {
                sqLiteDatabase.setTransactionSuccessful();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();
            }
        }
    }

    public static long insert(BillMemberInfo billMemberInfo) {
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            ContentValues contentValues = createContentValues(billMemberInfo);
            id = sqLiteDatabase.insert(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, null, contentValues);
            if (id > -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    public static long update(BillMemberInfo billMemberInfo, String where, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            ContentValues contentValues = createContentValues(billMemberInfo);
            id = sqLiteDatabase.update(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, contentValues, where, whereArgs);
            if (id > -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    public static long update(List<BillMemberInfo> billMemberInfos, String where, String billid) {
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            for (BillMemberInfo billMemberInfo : billMemberInfos) {
                String whereStrings[] = new String[]{billid, billMemberInfo.getMemberid()};
                ContentValues contentValues = createContentValues(billMemberInfo);
                id = sqLiteDatabase.update(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, contentValues, where, whereStrings);
            }

            if (id > -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    public static long delet(String where, String[] whereArgs) {
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            id = sqLiteDatabase.delete(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, where, whereArgs);
            if (id > -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    /**
     * 账单成员同步先删除后添加
     *
     * @param where
     * @param whereArgs
     * @return
     */
    public static long deletAndAdd(String where, String[] whereArgs, List<BillMemberInfo> billMemberInfos) {
        if (billMemberInfos == null) {
            return -1;
        }
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        List<BillMemberInfo> list = new ArrayList<BillMemberInfo>();
        boolean result = true;
        try {
            sqLiteDatabase = MyApplicaction.mOpenHelper.getWritableDatabase();
            sqLiteDatabase.beginTransaction();
            id = sqLiteDatabase.delete(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, where, whereArgs);
            if (id > -1) {
                for (BillMemberInfo billMemberInfo : billMemberInfos) {
                    ContentValues values = createContentValues(billMemberInfo);
                    id = sqLiteDatabase.insert(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, null, values);
                    if (id < 0) {
                        result = false;
                        break;
                    }
                }
                if (result) {
                    sqLiteDatabase.setTransactionSuccessful();
                }

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.endTransaction();
            }
        }
        return id;
    }

    public static BillMemberInfo getBillDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                     String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        BillMemberInfo billMemberInfo = null;
        Cursor cursor = database.query(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, BILLSMEMBER_COLUMNS, where,
                selectionArgs, groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                billMemberInfo = new BillMemberInfo(cursor);
            }
        } finally {
            cursor.close();
            // database.close();
        }

        return billMemberInfo;

    }

    public static List<BillMemberInfo> getListBillDbMemberInfo(String where, String[] selectionArgs, String groupBy,
                                                               String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        List<BillMemberInfo> billMemberInfos = new ArrayList<BillMemberInfo>();
        BillMemberInfo billMemberInfo = null;

        Cursor cursor = database.query(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, BILLSMEMBER_COLUMNS, where,
                selectionArgs, groupBy, having, orderBy, limit);
        if (cursor == null) {
            // database.close();
            return null;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    billMemberInfo = new BillMemberInfo(cursor);
                    billMemberInfos.add(billMemberInfo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            cursor.close();
            // database.close();
        }

        return billMemberInfos;

    }

    public static ContentValues createContentValues(BillMemberInfo billMemberInfo) {
        ContentValues values = new ContentValues(1);

        values.put(USERID, billMemberInfo.userid);
        values.put(MEMBERID, billMemberInfo.memberid);
        values.put(TYPE, billMemberInfo.type);
        values.put(AMOUNT, billMemberInfo.amount);
        values.put(STATUS, billMemberInfo.status);
        values.put(USERNAME, billMemberInfo.username);
        values.put(BILLID, billMemberInfo.billid);
        values.put(USERICON, billMemberInfo.usericon);
        values.put(INDEX, billMemberInfo.indexid);
        values.put(ISDELETED, billMemberInfo.isdeleted);
        values.put(ISDELETED, billMemberInfo.isdeleted);
        values.put(ISSYNCHRONIZATION, billMemberInfo.issynchronization);
        values.put(DEVICEID, billMemberInfo.deviceid);
        return values;
    }

    public BillMemberInfo(Cursor cursor) {
        userid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.USERID));
        memberid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.MEMBERID));
        type = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.TYPE));
        amount = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.AMOUNT));
        status = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.STATUS));
        username = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.USERNAME));
        usericon = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.USERICON));
        billid = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.BILLID));
        indexid = cursor.getInt(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.BILLID));
        isdeleted = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.ISDELETED));
        issynchronization = cursor.getString(cursor.getColumnIndex(DailycostContract.DtBillMemberColumns.ISSYNCHRONIZATION));
        deviceid = cursor.getString(cursor.getColumnIndex(DEVICEID));

    }

    private long id;
    private String userid = ""; // 用户id
    private String memberid = "";// 账本成员ID
    private String type = "1";// 账单类型 :入账类型:0.收入,1.支出,2.转账，3.结算，4.交款
    private String amount;// 账单金额
    private String status;// 账单状态
    private String username;// 用户名称
    private String usericon;// 头像icon
    private String billid = "";// 服务器返回Id
    private int indexid;// 头像下标没有联网
    private String isdeleted = ""; // 是否删除
    private String issynchronization = "";// "false"：未同步 "true":已同步 是否已经同步
    // private String accountbookid;// 账本id
    private String deviceid = "";//

    // 不写入数据库
    private String richman;
    private String spent;// 消费金额
    private String payamount;// 付款金额
    private String recamount;// 收款金额

    private String accountbookid = "";// 账本id
    private String clearid = "0";// 账单结算id
    private String clearamount;// 账单结算金额
    private String clearstatus;// 账单状态
    private String clearctime;// 账单结算创建时间
    private String clearutime;// 账单结算跟新时间
    private String reationid;//

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getRichman() {
        return richman;
    }

    public void setRichman(String richman) {
        this.richman = richman;
    }

    public String getSpent() {
        return spent;
    }

    public void setSpent(String spent) {
        this.spent = spent;
    }

    public String getPayamount() {
        return payamount;
    }

    public void setPayamount(String payamount) {
        this.payamount = payamount;
    }

    public String getRecamount() {
        return recamount;
    }

    public void setRecamount(String recamount) {
        this.recamount = recamount;
    }

    public String getIssynchronization() {
        return issynchronization;
    }

    public void setIssynchronization(String issynchronization) {
        this.issynchronization = issynchronization;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public int getIndexid() {
        return indexid;
    }

    public void setIndexid(int indexid) {
        this.indexid = indexid;
    }

    public String getAccountbookid() {
        return accountbookid;
    }

    public void setAccountbookid(String accountbookid) {
        this.accountbookid = accountbookid;
    }

    public String getClearid() {
        return clearid;
    }

    public void setClearid(String clearid) {
        this.clearid = clearid;
    }

    public String getClearamount() {
        return clearamount;
    }

    public void setClearamount(String clearamount) {
        this.clearamount = clearamount;
    }

    public String getClearstatus() {
        return clearstatus;
    }

    public void setClearstatus(String clearstatus) {
        this.clearstatus = clearstatus;
    }

    public String getClearctime() {
        return clearctime;
    }

    public void setClearctime(String clearctime) {
        this.clearctime = clearctime;
    }

    public String getClearutime() {
        return clearutime;
    }

    public void setClearutime(String clearutime) {
        this.clearutime = clearutime;
    }

    public String getReationid() {
        return reationid;
    }

    public void setReationid(String reationid) {
        this.reationid = reationid;
    }

}
