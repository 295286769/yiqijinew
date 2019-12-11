/**
 * DailycostSummary.java[V 2.0.0] Classs : .db.DailycostSummary Dingmao.SUN create at 2016年9月23日
 * 下午3:08:35
 */
package com.yiqiji.money.modules.common.db;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * .db.DailycostSummary
 *
 * @author Dingmao.SUN <br/>
 *         Create at 2016年9月23日 下午3:08:35
 */
public class DailycostSummary implements Parcelable, Serializable, DailycostContract.DtSummaryConlumns {

    private static final long serialVersionUID = 6889486772988593760L;
    private static final String[] QUERY_COLUMNS = {STYPE, WEEKDAYS, COUNT, SVALUE, DATETIME};

    public static final int STYPE_INDEX = 0;
    public static final int WEEKDAYS_INDEX = 1;
    public static final int COUNT_INDEX = 2;
    public static final int SVALUE_INDEX = 3;
    public static final int DATETIME_INDEX = 4;

    public static Intent createIntent(String action, long costId) {
        return new Intent(action).setData(getUri(costId));
    }

    public static Intent createIntent(Context context, Class<?> cls, long costId) {
        return new Intent(context, cls).setData(getUri(costId));
    }

    public static Uri getUri(long costId) {
        return ContentUris.withAppendedId(CONTENT_SUMMARY_URI, costId);
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
    public static CursorLoader getDailycostSummaryCursorLoader(Context context) {
        return new CursorLoader(context, CONTENT_SUMMARY_URI, QUERY_COLUMNS, null, null, null);
    }

    /**
     * Get all DailycostSummary given conditions.
     *
     * @param contentResolver to perform the query on.
     *                        A filter declaring which rows to return, formatted as an SQL
     *                        WHERE clause (excluding the WHERE itself). Passing null will
     *                        return all rows for the given URI.
     * @param selectionArgs   You may include ?s in selection, which will be replaced by the
     *                        values from selectionArgs, in the order that they appear in
     *                        the selection. The values will be bound as Strings.
     * @return list of cloudSportSummary matching where clause or empty list if
     * none found.
     */
    public static List<DailycostSummary> getDailycostSummarys(ContentResolver contentResolver, String where,
                                                              String[] selectionArgs, String orderBy) {
        Cursor cursor = contentResolver.query(CONTENT_SUMMARY_URI, QUERY_COLUMNS, where, selectionArgs, orderBy);
        List<DailycostSummary> result = new LinkedList<DailycostSummary>();
        if (cursor == null) {
            return result;
        }

        try {
            if (cursor.moveToFirst()) {
                do {
                    result.add(new DailycostSummary(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        return result;
    }

    public static final Creator<DailycostSummary> CREATOR = new Creator<DailycostSummary>() {
        public DailycostSummary createFromParcel(Parcel p) {
            return new DailycostSummary(p);
        }

        public DailycostSummary[] newArray(int size) {
            return new DailycostSummary[size];
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
        dest.writeInt(weekdays);
        dest.writeInt(count);
        dest.writeDouble(value);
        dest.writeString(datetime);
    }

    private int weekdays; // 周几（0表示周日，6表示周六）
    private int count; // 条数
    private double value; // sum
    private String datetime; // 日期（如： 2016-09-08）

    /**
     * Construction method
     */
    public DailycostSummary() {
        // TODO Auto-generated constructor stub
        this.weekdays = -1;
        this.count = 0;
        this.value = 0;
        this.datetime = "";
    }

    public DailycostSummary(Cursor c) {

        this.weekdays = c.getInt(WEEKDAYS_INDEX);
        this.count = c.getInt(COUNT_INDEX);
        this.value = c.getDouble(SVALUE_INDEX);
        this.datetime = c.getString(DATETIME_INDEX);
    }

    public DailycostSummary(Parcel p) {
        this.weekdays = p.readInt();
        this.count = p.readInt();
        this.value = p.readDouble();
        this.datetime = p.readString();
    }


    public int getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(int weekdays) {
        this.weekdays = weekdays;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "DailycostSummary::[" + STYPE + ", " + WEEKDAYS + "=" + weekdays + ", " + COUNT + "="
                + count + ", " + SVALUE + "=" + value + ", " + DATETIME + "=" + datetime + "]";
    }
}
