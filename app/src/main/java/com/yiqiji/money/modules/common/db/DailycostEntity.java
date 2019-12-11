/**
 * DailycostEntity.java[V 2.0.0] Classs : com.ziniu.money.db.DailycostEntity Dingmao.SUN create at 2016年9月23日
 * 下午1:37:04
 */
package com.yiqiji.money.modules.common.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.db.DailycostContract.DtInfoColumns;
import com.yiqiji.money.modules.common.plication.MyApplicaction;
import com.yiqiji.money.modules.common.utils.DateUtil;
import com.yiqiji.money.modules.homeModule.home.entity.TotalBalance;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * com.ziniu.money.db.DailycostEntity
 *
 * @author Dingmao.SUN <br/>
 *         Create at 2016年9月23日 下午1:37:04
 */
public class DailycostEntity implements Parcelable, DtInfoColumns {

    private static final long serialVersionUID = 5393281726379607287L;
    public static final String DEFAULT_SORT_ORDER = TRADETIME + " DESC";
    public static final String BALANCE_SORT_ORDER = BILLAMOUNT + " DESC";

    private static final String[] QUERY_COLUMNS = {_ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
            TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2, EXTRAINFO3,
            YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID, BILLID, ACCOUNTBOOKID,
            USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME, BILLCATEICON, BILLSUBCATENAME,
            BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID, BILLIMG, ADDRESS, BILLSTATUS, BILLBRAND};

    static final String[] columns = new String[]{_ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
            TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2, EXTRAINFO3,
            YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID, BILLID, ACCOUNTBOOKID,
            USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME, BILLCATEICON, BILLSUBCATENAME,
            BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID, BILLIMG, ADDRESS, BILLSTATUS, BILLBRAND, "SUM(" + BILLAMOUNT + ")"};

    private static final long INVALID_ID = -1;

    public static ContentValues createContentValues(DailycostEntity dailycost) {
        ContentValues values = new ContentValues();
        // if (dailycost._id != INVALID_ID) {
        // values.put(_ID, dailycost._id);
        // }
        values.put(USERID, dailycost.userid);
        values.put(BILLTYPE, dailycost.billtype);
        values.put(BILLAMOUNT, dailycost.billamount);
        values.put(COSTTYPE, dailycost.costtype);
        values.put(CONTEXT, dailycost.context);
        values.put(BILLCTIME, dailycost.billctime);
        values.put(TRADETIME, dailycost.tradetime);
        values.put(UPDATETIME, dailycost.updatetime);
        values.put(WAGESID, dailycost.wagesid);
        values.put(ISDELETED, dailycost.isdeleted);
        values.put(ISCLEAR, dailycost.isclear);
        values.put(LNG, dailycost.lng);
        values.put(LAT, dailycost.lat);
        values.put(BILLMARK, dailycost.billmark);
        values.put(EXTRAINFO1, dailycost.extrainfo1);
        values.put(EXTRAINFO2, dailycost.extrainfo2);
        values.put(EXTRAINFO3, dailycost.extrainfo3);
        values.put(YEAR, dailycost.year);
        values.put(MOTH, dailycost.moth);
        values.put(DAY, dailycost.day);
        values.put(ISSYNCHRONIZATION, dailycost.issynchronization);
        values.put(WHICHBOOK, dailycost.whichbook);
        values.put(ACCOUNTBOOKTYPE, dailycost.accountbooktype);
        values.put(BILLCLEAR, dailycost.billclear);
        values.put(BILLID, dailycost.billid);
        values.put(PKID, dailycost.pkid);
        values.put(ACCOUNTBOOKID, dailycost.accountbookid);
        values.put(USERNAME, dailycost.username);
        values.put(USERICON, dailycost.usericon);
        values.put(BILLCATEID, dailycost.billcateid);
        values.put(BILLSUBCATEID, dailycost.billsubcateid);
        values.put(BILLCATENAME, dailycost.billcatename);
        values.put(BILLCATEICON, dailycost.billcateicon);
        values.put(BILLSUBCATENAME, dailycost.billsubcatename);
        values.put(BILLSUBCATEICON, dailycost.billsubcateicon);
        values.put(BILLCOUNT, dailycost.billcount);
        values.put(DEVICEID, dailycost.deviceid);
        values.put(BILLIMG, dailycost.billimg);
        values.put(ADDRESS, dailycost.address);
        values.put(BILLSTATUS, dailycost.billstatus);
        values.put(BILLBRAND, dailycost.billbrand);
        values.put(ACCOUNTNUMBER, dailycost.accountnumber);
        return values;
    }

    public static Intent createIntent(String action, long costId) {
        return new Intent(action).setData(getUri(costId));
    }

    public static Intent createIntent(Context context, Class<?> cls, long costId) {
        return new Intent(context, cls).setData(getUri(costId));
    }

    public static Uri getUri(long costId) {
        return ContentUris.withAppendedId(CONTENT_URI, costId);
    }

    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    /**
     * Get DailycostEntity cursor loader for all daily cost.
     *
     * @param context to query the data attention.
     * @return cursor loader with all the daily costs.
     */
    public static CursorLoader getDailycostEntityCursorLoader(Context context) {
        return new CursorLoader(context, CONTENT_URI, QUERY_COLUMNS, null, null, DEFAULT_SORT_ORDER);
    }


    /**
     * Get DailycostEntity by id.
     *
     * @param
     * @param
     * @return cloudSportSummary if found, null otherwise
     */
    public static DailycostEntity getDataBaeDailycostEntity(String where, String[] selectionArgs, String groupBy,
                                                            String having, String orderBy, String limit) {
        SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
        Cursor cursor = database.query(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, QUERY_COLUMNS, where,
                selectionArgs, null, null, null, null);
        DailycostEntity result = null;
        if (cursor == null) {
            return result;
        }

        try {
            if (cursor.moveToFirst()) {
                result = new DailycostEntity(cursor);
            }
        } finally {
            cursor.close();
        }

        return result;
    }

    public static List<TotalBalance> getTotalBalances(String selection, String[] selectionArgs, String groupBy,
                                                      String having, String orderBy, String limit) {
        SQLiteDatabase database = null;
        List<TotalBalance> totalBalances = new ArrayList<TotalBalance>();
        Cursor cursor = null;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            cursor = database.query(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, columns, selection, selectionArgs,
                    groupBy, having, orderBy, limit);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                do {
                    TotalBalance dailycostEntity = new TotalBalance(cursor);
                    totalBalances.add(dailycostEntity);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return totalBalances;
    }

    public static int getSameClassification(String selection, String[] selectionArgs, String groupBy,
                                            String having, String orderBy, String limit) {
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            cursor = database.query(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, QUERY_COLUMNS, selection, selectionArgs,
                    groupBy, having, orderBy, limit);
            if (cursor != null) {
                return cursor.getCount();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return 0;
    }


    /**
     * 根据月份分组求和
     */
    public static List<TotalBalance> getTataolBlance(ContentResolver contentResolver, String where,
                                                     String[] selectionArgs, String orderBy) {

        Cursor cursor = contentResolver.query(CONTENT_URI, QUERY_COLUMNS, where, selectionArgs, orderBy);
        List<TotalBalance> result = new LinkedList<TotalBalance>();
        if (cursor == null) {
            return result;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    // float d = cursor.getFloat(cursor.getColumnIndex("SUM("
                    // + BALANCE + ")"));
                    result.add(new TotalBalance(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
        } finally {
            cursor.close();
        }

        return result;
    }

    public static long addDailycostEntity(ContentResolver contentResolver, DailycostEntity dailycost) {
        ContentValues values = createContentValues(dailycost);
        Uri uri = contentResolver.insert(CONTENT_URI, values);
        dailycost._id = getId(uri);
        return dailycost._id;
    }

    public static long addDailycostEntity(DailycostEntity dailycost) {
        long rowsUpdated = -1;
        SQLiteDatabase database = null;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values = createContentValues(dailycost);
            rowsUpdated = database.insert(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, null, values);
            if (rowsUpdated > -1) {
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

        return rowsUpdated;
    }

    public static long updateDataBaseDailycostEntity(DailycostEntity dailycost, String whereClause, String[] whereArgs) {
        long rowsUpdated = -1;
        SQLiteDatabase database = null;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            ContentValues values = createContentValues(dailycost);
            rowsUpdated = database.update(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, values, whereClause, whereArgs);
            if (rowsUpdated > -1) {
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

        return rowsUpdated;
    }

    public static long updateMultipleRecordsDailycostInfo(List<DailycostEntity> dailycostEntities) {
        long rowsUpdated = -1;
        SQLiteDatabase database = null;
        boolean result = true;
        try {
            if (dailycostEntities == null) {
                return -1;
            }
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            for (DailycostEntity dailycostEntity : dailycostEntities) {
                ContentValues values = createContentValues(dailycostEntity);
                String whereClause = DtInfoColumns.PKID + "=?";
                String whereArgs[] = new String[]{dailycostEntity.getPkid()};
                DailycostEntity dailycostEntity1 = getDataBaeDailycostEntity(whereClause, whereArgs, null, null, null, null);
                if (dailycostEntity1 == null) {
                    rowsUpdated = database.insert(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, null, values);
                } else {
                    rowsUpdated = database.update(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, values, whereClause,
                            whereArgs);
                }

                if (rowsUpdated < 0) {
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

        return rowsUpdated;
    }

    public static long updateDailycostEntitys(ContentResolver contentResolver, ContentValues values, String where,
                                              String... selectionArgs) {
        return contentResolver.update(CONTENT_URI, values, where, selectionArgs);
    }

    public static List<DailycostEntity> queryDailycostEntitys(String selection, String[] selectionArgs, String groupBy,
                                                              String having, String orderBy, String limit) {
        SQLiteDatabase database = null;
        List<DailycostEntity> dailycostEntities = new ArrayList<DailycostEntity>();
        Cursor cursor = null;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            cursor = database.query(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, QUERY_COLUMNS, selection,
                    selectionArgs, groupBy, having, orderBy, limit);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                do {
                    DailycostEntity dailycostEntity = new DailycostEntity(cursor);
                    dailycostEntities.add(dailycostEntity);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }

        }
        return dailycostEntities;
    }

    /**
     * 同步数据库先删除后添加
     *
     * @param dailycostEntities
     * @return
     */
    public static long deleAllAndAdd(String where, String[] whereStrings, List<DailycostEntity> dailycostEntities) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, where, whereStrings);

            if (id > -1) {
                for (DailycostEntity dailycostEntity : dailycostEntities) {
                    ContentValues contentValues = createContentValues(dailycostEntity);
                    id = database.insert(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, null, contentValues);
                    if (id < 0) {
                        result = false;
                        break;
                    }

                }
                if (result) {
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

    /**
     * 删除账单后删除账单成员
     *
     * @param billid
     * @return
     */
    public static long deleteDailycostEntityDataBase(String billid) {
        String where = BILLID + "=? ";
        String whereString[] = {billid};
        SQLiteDatabase database = null;
        long id = -1;
        try {
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, where, whereString);
            if (id > -1) {
                where = DailycostContract.DtBillMemberColumns.BILLID + "=? ";
                String[] whereStrings = {billid};
                id = database.delete(DailycostDatabaseHelper.BILL_MEMBER_TABLE_NAME, where, whereStrings);
            }
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

    public static long deleteDailycostEntity(ContentResolver contentResolver, long costId) {
        if (costId == INVALID_ID)
            return INVALID_ID;
        int deletedRows = contentResolver.delete(getUri(costId), "", null);
        if (deletedRows == 1) {
            return costId;
        }
        return INVALID_ID;
    }

    public static long deleteDailycostEntitys(ContentResolver contentResolver, String where, String[] selectionArgs) {
        return contentResolver.delete(CONTENT_URI, where, selectionArgs);
    }

    public static final Creator<DailycostEntity> CREATOR = new Creator<DailycostEntity>() {
        public DailycostEntity createFromParcel(Parcel p) {
            return new DailycostEntity(p);
        }

        public DailycostEntity[] newArray(int size) {
            return new DailycostEntity[size];
        }
    };

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Parcelable#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeLong(_id);
        dest.writeString(userid);
        dest.writeString(billtype);
        dest.writeString(billamount);
        dest.writeInt(costtype);
        dest.writeString(context);
        dest.writeString(billctime);
        dest.writeString(date);
        dest.writeString(tradetime);
        dest.writeLong(updatetime);
        dest.writeLong(wagesid);
        dest.writeString(isdeleted);
        dest.writeString(isclear);
        dest.writeFloat(lng);
        dest.writeFloat(lat);
        dest.writeString(billmark);
        dest.writeString(extrainfo1);
        dest.writeString(extrainfo2);
        dest.writeString(extrainfo3);
        dest.writeInt(year);
        dest.writeInt(moth);
        dest.writeInt(day);
        dest.writeString(issynchronization);
        dest.writeString(whichbook);
        dest.writeString(accountbooktype);
        dest.writeString(billclear);
        dest.writeString(pkid);
        dest.writeString(billid);
        dest.writeString(accountbookid);
        dest.writeString(username);
        dest.writeString(usericon);
        dest.writeString(billcateid);
        dest.writeString(billsubcateid);
        dest.writeString(billcatename);
        dest.writeString(billcateicon);
        dest.writeString(billsubcatename);
        dest.writeString(billsubcateicon);
        dest.writeString(accountnumber);
        dest.writeString(billcount);
        dest.writeString(deviceid);
        dest.writeString(billparentcate);
        dest.writeString(billchildcate);
        dest.writeString(billimg);
        dest.writeString(address);
        dest.writeString(billstatus);
        dest.writeString(billbrand);
        dest.writeString(billnum);
        dest.writeList(imglist);
        dest.writeList(memberlist);

    }

    private long _id;
    private String userid; // 用户id
    private String billtype; // 类型（0表示收入，1表示支出,2转账，3结算，4交款）
    private String billamount; // 金额billamount
    private int costtype; // 消费类型
    private String context; // 明目（记账时编辑的文本内容）
    private String billctime; // 记录日期(时间戳,系统当前时间)billctime
    private String date; // 记录日期（日期不在数据库中记录）
    private String tradetime; // 记录创建日期(滚动时间轴选择的时间)tradetime
    private long updatetime; // 更新日期
    private long wagesid; // 工资id
    private String isdeleted; // 是否删除
    private String isclear; // 是否需要结算0:不需要1：需要
    private float lng; // 经度
    private float lat; // 纬度
    private String billmark; // 备注billmark
    private String extrainfo1; // 备用1
    private String extrainfo2; // 备用2
    private String extrainfo3; // 备用3
    private int year;
    private int moth;
    private int day;
    private String issynchronization;// 是否同步数据库
    private String whichbook;// 哪一个账本名称
    private String accountbooktype;// 是否是多人账本 0:单人
    private String billclear;// 是否已经结算 0:未结算1：已结算

    private String accountbookid;// 账本id
    private String username;// 用户名称
    private String usericon;// 用户头像
    private String billcateid;// 账单一级分类id
    private String billsubcateid = "0";// 账单二级分类id
    private String billcatename;// // 账单一级分类名称
    private String billcateicon;// 账单一级分类图片
    private String billsubcatename;//
    private String billsubcateicon;//
    private String accountnumber;// 关联资产ID，
    private String billcount; // 账单消费人数
    private String deviceid; //
    private String billstatus; //
    private String billparentcate; //
    private String billchildcate; //
    private String billimg; //账单图片地址
    private String address; //账单发生地址
    private String billbrand; //装修账本品牌字段

    // 标识数据是否同步 未同步是值为空
    private String pkid;// 主键id
    private String billid;//

    // 不写入数据库
    private List<BillMemberInfo> memberlist;
    private List<String> imglist;
    private String billnum;


    /**
     * Construction method
     */
    public DailycostEntity() {
        // TODO Auto-generated constructor stub
        this._id = INVALID_ID;
        this.userid = "";
        this.billtype = "0";
        this.billamount = "0.00";
        this.costtype = 1;
        this.context = "";
        this.billctime = "0";
        this.date = "";
        this.tradetime = "0";
        this.updatetime = 0;
        this.wagesid = 0;
        this.isdeleted = "false";
        this.isclear = "0";
        this.lng = 0;
        this.lat = 0;
        this.billmark = "";
        this.extrainfo1 = "";
        this.extrainfo2 = "";
        this.extrainfo3 = "";
        this.year = 0;
        this.moth = 0;
        this.day = 0;
        this.issynchronization = "false";
        this.whichbook = "";
        this.accountbooktype = "0";
        this.billclear = "0";
        this.pkid = "";
        this.billid = "";
        this.accountbookid = "";
        this.username = "";
        this.usericon = "";
        this.billcateid = "";
        this.billsubcateid = "";
        this.billcatename = "";
        this.billcateicon = "";
        this.billsubcatename = "";
        this.billsubcateicon = "";
        this.accountnumber = "";
        this.billcount = "";
        this.deviceid = "";
        this.billparentcate = "";
        this.billchildcate = "";
        this.billimg = "";
        this.address = "";
        this.billstatus = "1";
        this.billbrand = "";
        this.billnum = "0";

    }

    public DailycostEntity(Cursor c) {
        this._id = c.getLong(c.getColumnIndex(_ID));
        this.userid = c.getString(c.getColumnIndex(USERID));
        this.billtype = c.getString(c.getColumnIndex(BILLTYPE));
        if (c.getColumnIndex(BILLAMOUNT) != -1) {
            this.billamount = c.getString(c.getColumnIndex(BILLAMOUNT));
        }

        this.costtype = c.getInt(c.getColumnIndex(COSTTYPE));
        this.context = c.getString(c.getColumnIndex(CONTEXT));
        this.billctime = c.getString(c.getColumnIndex(BILLCTIME));
        long billctime_long = Long.parseLong(c.getString(c.getColumnIndex(BILLCTIME)));
        this.date = DateUtil.getDateToString(billctime_long);
        this.tradetime = c.getString(c.getColumnIndex(TRADETIME));
        this.updatetime = c.getLong(c.getColumnIndex(UPDATETIME));
        this.wagesid = c.getLong(c.getColumnIndex(WAGESID));
        this.isdeleted = c.getString(c.getColumnIndex(ISDELETED));
        this.isclear = c.getString(c.getColumnIndex(ISCLEAR));
        this.lng = c.getFloat(c.getColumnIndex(LNG));
        this.lat = c.getFloat(c.getColumnIndex(LAT));
        this.billmark = c.getString(c.getColumnIndex(BILLMARK));
        this.extrainfo1 = c.getString(c.getColumnIndex(EXTRAINFO1));
        this.extrainfo2 = c.getString(c.getColumnIndex(EXTRAINFO2));
        this.extrainfo3 = c.getString(c.getColumnIndex(EXTRAINFO3));
        this.year = c.getInt(c.getColumnIndex(YEAR));
        this.moth = c.getInt(c.getColumnIndex(MOTH));
        this.day = c.getInt(c.getColumnIndex(DAY));
        this.issynchronization = c.getString(c.getColumnIndex(ISSYNCHRONIZATION));
        this.whichbook = c.getString(c.getColumnIndex(WHICHBOOK));
        this.accountbooktype = c.getString(c.getColumnIndex(ACCOUNTBOOKTYPE));
        this.billclear = c.getString(c.getColumnIndex(BILLCLEAR));
        this.pkid = c.getString(c.getColumnIndex(PKID));
        this.billid = c.getString(c.getColumnIndex(BILLID));
        this.accountbookid = c.getString(c.getColumnIndex(ACCOUNTBOOKID));
        this.username = c.getString(c.getColumnIndex(USERNAME));
        this.usericon = c.getString(c.getColumnIndex(USERICON));
        this.billcateid = c.getString(c.getColumnIndex(BILLCATEID));
        this.billsubcateid = c.getString(c.getColumnIndex(BILLSUBCATEID));
        this.billcatename = c.getString(c.getColumnIndex(BILLCATENAME));
        this.billcateicon = c.getString(c.getColumnIndex(BILLCATEICON));
        this.billsubcatename = c.getString(c.getColumnIndex(BILLSUBCATENAME));
        this.billsubcateicon = c.getString(c.getColumnIndex(BILLSUBCATEICON));
        this.accountnumber = c.getString(c.getColumnIndex(ACCOUNTNUMBER));
        this.billcount = c.getString(c.getColumnIndex(BILLCOUNT));
        this.deviceid = c.getString(c.getColumnIndex(DEVICEID));
        this.billimg = c.getString(c.getColumnIndex(BILLIMG));
        this.address = c.getString(c.getColumnIndex(ADDRESS));
        this.billstatus = c.getString(c.getColumnIndex(BILLSTATUS));
        this.billbrand = c.getString(c.getColumnIndex(BILLBRAND));

    }

    public DailycostEntity(Parcel p) {
        this._id = p.readLong();
        this.userid = p.readString();
        this.billtype = p.readString();
        this.billamount = p.readString();
        this.costtype = p.readInt();
        this.context = p.readString();
        this.billctime = p.readString();
        this.date = p.readString();
        this.tradetime = p.readString();
        this.updatetime = p.readLong();
        this.wagesid = p.readLong();
        this.isdeleted = p.readString();
        this.isclear = p.readString();
        this.lng = p.readFloat();
        this.lat = p.readFloat();
        this.billmark = p.readString();
        this.extrainfo1 = p.readString();
        this.extrainfo2 = p.readString();
        this.extrainfo3 = p.readString();
        this.year = p.readInt();
        this.moth = p.readInt();
        this.day = p.readInt();
        this.issynchronization = p.readString();
        this.whichbook = p.readString();
        this.accountbooktype = p.readString();
        this.billclear = p.readString();
        this.pkid = p.readString();
        this.billid = p.readString();
        this.accountbookid = p.readString();
        this.username = p.readString();
        this.usericon = p.readString();
        this.billcateid = p.readString();
        this.billsubcateid = p.readString();
        this.billcatename = p.readString();
        this.billcateicon = p.readString();
        this.billsubcatename = p.readString();
        this.billsubcateicon = p.readString();
        this.accountnumber = p.readString();
        this.billcount = p.readString();
        this.deviceid = p.readString();
        this.billparentcate = p.readString();
        this.billchildcate = p.readString();
        this.billimg = p.readString();
        this.address = p.readString();
        this.billstatus = p.readString();
        this.billbrand = p.readString();
        this.billnum = p.readString();
        this.imglist = p.readArrayList(String.class.getClassLoader());
        this.memberlist = p.readArrayList(BillMemberInfo.class.getClassLoader());

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getCosttype() {
        return costtype;
    }

    public void setCosttype(int costtype) {
        this.costtype = costtype;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(long updatetime) {
        this.updatetime = updatetime;
    }

    public long getWagesid() {
        return wagesid;
    }

    public void setWagesid(long wagesid) {
        this.wagesid = wagesid;
    }

    public String getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(String isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getIsclear() {
        return isclear;
    }

    public void setIsclear(String isclear) {
        this.isclear = isclear;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public String getExtrainfo1() {
        return extrainfo1;
    }

    public void setExtrainfo1(String extrainfo1) {
        this.extrainfo1 = extrainfo1;
    }

    public String getExtrainfo2() {
        return extrainfo2;
    }

    public void setExtrainfo2(String extrainfo2) {
        this.extrainfo2 = extrainfo2;
    }

    public String getExtrainfo3() {
        return extrainfo3;
    }

    public void setExtrainfo3(String extrainfo3) {
        this.extrainfo3 = extrainfo3;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMoth() {
        return moth;
    }

    public void setMoth(int moth) {
        this.moth = moth;
    }

    public String getIssynchronization() {
        return issynchronization;
    }

    public void setIssynchronization(String issynchronization) {
        this.issynchronization = issynchronization;
    }

    public String getWhichbook() {
        return whichbook;
    }

    public void setWhichbook(String whichbook) {
        this.whichbook = whichbook;
    }

    public String getAccountbooktype() {
        return accountbooktype;
    }

    public void setAccountbooktype(String accountbooktype) {
        this.accountbooktype = accountbooktype;
    }

    public String getPkid() {
        return pkid;
    }

    public void setPkid(String pkid) {
        this.pkid = pkid;
    }

    public String getBillid() {
        return billid;
    }

    public void setBillid(String billid) {
        this.billid = billid;
    }

    public String getAccountbookid() {
        return accountbookid;
    }

    public void setAccountbookid(String accountbookid) {
        this.accountbookid = accountbookid;
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

    public String getBillcateid() {
        return billcateid;
    }

    public void setBillcateid(String billcateid) {
        this.billcateid = billcateid;
    }

    public String getBillsubcateid() {
        return billsubcateid;
    }

    public void setBillsubcateid(String billsubcateid) {
        this.billsubcateid = billsubcateid;
    }

    public String getBillcatename() {
        return billcatename;
    }

    public void setBillcatename(String billcatename) {
        this.billcatename = billcatename;
    }

    public String getBillcateicon() {
        return billcateicon;
    }

    public void setBillcateicon(String billcateicon) {
        this.billcateicon = billcateicon;
    }

    public String getBillsubcatename() {
        return billsubcatename;
    }

    public void setBillsubcatename(String billsubcatename) {
        this.billsubcatename = billsubcatename;
    }

    public String getBillsubcateicon() {
        return billsubcateicon;
    }

    public void setBillsubcateicon(String billsubcateicon) {
        this.billsubcateicon = billsubcateicon;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getBillcount() {
        return billcount;
    }

    public void setBillcount(String billcount) {
        this.billcount = billcount;
    }

    public List<BillMemberInfo> getMemberlist() {
        return memberlist;
    }

    public void setMemberlist(List<BillMemberInfo> memberlist) {
        this.memberlist = memberlist;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBilltype() {
        return billtype;
    }

    public void setBilltype(String billtype) {
        this.billtype = billtype;
    }

    public String getBillamount() {
        return billamount;
    }

    public void setBillamount(String billamount) {
        this.billamount = billamount;
    }

    public String getBillctime() {
        return billctime;
    }

    public void setBillctime(String billctime) {
        this.billctime = billctime;
    }

    public String getTradetime() {
        return tradetime;
    }

    public void setTradetime(String tradetime) {
        this.tradetime = tradetime;
    }

    public String getBillmark() {
        return billmark;
    }

    public void setBillmark(String billmark) {
        this.billmark = billmark;
    }

    public String getBillclear() {
        return billclear;
    }

    public void setBillclear(String billclear) {
        this.billclear = billclear;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getBillstatus() {
        return billstatus;
    }

    public void setBillstatus(String billstatus) {
        this.billstatus = billstatus;
    }

    public String getBillparentcate() {
        return billparentcate;
    }

    public void setBillparentcate(String billparentcate) {
        this.billparentcate = billparentcate;
    }

    public String getBillchildcate() {
        return billchildcate;
    }

    public void setBillchildcate(String billchildcate) {
        this.billchildcate = billchildcate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBillimg() {
        return billimg;
    }

    public void setBillimg(String billimg) {
        this.billimg = billimg;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBillbrand() {
        return billbrand;
    }

    public void setBillbrand(String billbrand) {
        this.billbrand = billbrand;
    }

    public List<String> getImglist() {
        return imglist;
    }

    public void setImglist(List<String> imglist) {
        this.imglist = imglist;
    }

    public String getBillnum() {
        return billnum;
    }

    public void setBillnum(String billnum) {
        this.billnum = billnum;
    }

    /*
                     * (non-Javadoc)
                     *
                     * @see java.lang.Object#toString()
                     */
    @Override
    public String toString() {

        // TODO Auto-generated method stub
        return "DailycostEntity::[" + _ID + "=" + _id + ", " + USERID + "=" + userid + ", " + BILLTYPE + "=" + billtype
                + ", " + BILLAMOUNT + "=" + billamount + ", " + COSTTYPE + "=" + costtype + ", " + CONTEXT + "="
                + context + ", " + BILLCTIME + "=" + billctime + ", " + "date = " + date + ", " + TRADETIME + "="
                + tradetime + ", " + UPDATETIME + "=" + updatetime + ", " + WAGESID + "=" + wagesid + ", " + ISDELETED
                + "=" + isdeleted + ", " + ISCLEAR + "=" + isclear + ", " + LNG + "=" + lng + ", " + LAT + "=" + lat
                + ", " + BILLMARK + "=" + billmark + ", " + EXTRAINFO1 + "=" + extrainfo1 + ", " + EXTRAINFO2 + "="
                + extrainfo2 + ", " + EXTRAINFO3 + "=" + extrainfo3 + ", " + YEAR + "=" + year + ", " + MOTH + "="
                + moth + ", " + DAY + "=" + day + ", " + ISSYNCHRONIZATION + "=" + issynchronization + ", " + WHICHBOOK
                + "=" + whichbook + ", " + ACCOUNTBOOKTYPE + "=" + accountbooktype + ", " + BILLID + "=" + billid
                + ", " + PKID + "=" + pkid + ", " + ACCOUNTBOOKID + "=" + accountbookid + ", " + USERNAME + "="
                + username

                + ", " + USERICON + "=" + usericon + ", " + BILLCATEID + "=" + billcateid + ", " + BILLCOUNT + "="
                + billcount + ", " + ACCOUNTNUMBER + "=" + accountnumber + ", " + BILLSUBCATEICON + "="
                + billsubcateicon + ", " + BILLSUBCATENAME + "=" + billsubcatename + ", " + BILLCATEICON + "="
                + billcateicon + ", " + BILLCATENAME + "=" + billcatename + ", " + DEVICEID + "=" + deviceid + ", "
                + BILLSTATUS + "=" + billstatus + ", " + BILLSUBCATEICON + "=" + billsubcateid + ", " + BILLIMG + "="
                + billimg + ", " + ADDRESS + "="
                + address + ", " + BILLBRAND + "=" + billbrand + ", " + BILLCLEAR + "="
                + billclear + "]";
    }

}
