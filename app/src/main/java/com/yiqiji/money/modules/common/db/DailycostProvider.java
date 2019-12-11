/**
 * DailyCostProvider.java[V 2.0.0] Classs : .db.DailyCostProvider Dingmao.SUN create at 2016年9月8日
 * 下午3:27:01
 */
package com.yiqiji.money.modules.common.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.yiqiji.money.modules.common.db.DailycostContract.DtInfoColumns;
import com.yiqiji.money.modules.common.db.DailycostContract.DtSummaryConlumns;

/**
 * .db.DailyCostProvider
 * 
 * @author Dingmao.SUN <br/>
 *         Create at 2016年9月8日 下午3:27:01
 */
public class DailycostProvider extends ContentProvider implements DtInfoColumns, DtSummaryConlumns {
	private final static String TAG = "DailycostProvider";
	private final static Object mMutex = new Object();
	private static final int DAILYCOST = 1;
	private static final int DAILYCOST_ID = 2;
	private static final int DAILYCOST_SUMMARY = 3;
	private static final int DAILYCOST_MOTH = 4;
	private DailycostDatabaseHelper mOpenHelper;

	private static final UriMatcher sURLMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURLMatcher.addURI(DailycostContract.AUTHORITY, "dailycost", DAILYCOST);
		sURLMatcher.addURI(DailycostContract.AUTHORITY, "dailycost/#", DAILYCOST_ID);
		sURLMatcher.addURI(DailycostContract.AUTHORITY, "dailycost_summary", DAILYCOST_SUMMARY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new DailycostDatabaseHelper(getContext());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sql = null;
		int match = sURLMatcher.match(uri);
		switch (match) {
		case DAILYCOST_MOTH:// 查一个月
			break;
		case DAILYCOST:
			if (selection != null && selection.equals(BILLTYPE)) {
				// 查询本月收入，本月支出 selectionArgs[2]这是一个文字，加单引号
				sql = "SELECT * FROM " + DailycostDatabaseHelper.DAILYCOST_TABLE_NAME + " WHERE " + YEAR + " = "
						+ selectionArgs[0] + " AND " + MOTH + " = " + selectionArgs[1] + " AND " + ACCOUNTBOOKID
						+ " = " + " '" + selectionArgs[2] + "' " + " AND " + BILLTYPE + " = " + selectionArgs[3]
						+ " ORDER BY " + BILLCTIME + " DESC";

			} else if (selection != null && selection.equals("typeweek")) {
				sql = "SELECT * FROM " + DailycostDatabaseHelper.DAILYCOST_TABLE_NAME + " WHERE (" + BILLCTIME
						+ " / 1000) >= CAST(strftime('%s', date('" + selectionArgs[0] + "', '-' || strftime('%w', '"
						+ selectionArgs[0] + "') || ' day')) AS INTEGER) AND (" + BILLCTIME
						+ " / 1000) < CAST(strftime('%s', date('" + selectionArgs[0] + "', abs(7 - strftime('%w', '"
						+ selectionArgs[0] + "')) || ' day')) AS INTEGER) AND " + COSTTYPE + " = " + selectionArgs[1]
						+ " AND " + BILLTYPE + " = " + selectionArgs[2] + ";";

			} else {
				if (selection != null
						&& (selection.equals(MOTH) || selection.equals("moths") || selection.equals("moths_three")
								|| selection.equals("item") || selection.equals("moths_type")
								|| selection.equals("which_type_day") || selection.equals("moths_type_order_balance") || selection
									.equals("group_type"))) {

				} else {
					qb.setTables(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME);
				}

			}
			break;
		case DAILYCOST_ID:
			qb.setTables(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME);
			qb.appendWhere(_ID + "=");
			qb.appendWhere(uri.getLastPathSegment());
			break;
		case DAILYCOST_SUMMARY:

			int type = 1;
			if (sortOrder != null && sortOrder.equals("0")) {
				type = 0;
			}

			if (selection != null && selection.equals("week") && !TextUtils.isEmpty(sortOrder)) {
				sql = "SELECT -1 AS " + STYPE + ", strftime('%w', date(" + BILLCTIME
						+ "/1000, 'unixepoch', 'localtime')) AS " + WEEKDAYS + ", count(" + _ID + ") AS " + COUNT
						+ ", SUM(" + BILLAMOUNT + ") AS " + SVALUE + ", date(" + BILLCTIME
						+ "/1000, 'unixepoch', 'localtime') AS " + DATETIME + " FROM "
						+ DailycostDatabaseHelper.DAILYCOST_TABLE_NAME + " WHERE (" + BILLCTIME
						+ " / 1000) >= CAST(strftime('%s', date('" + selectionArgs[0] + "', '-' || strftime('%w', '"
						+ selectionArgs[0] + "') || ' day')) AS INTEGER) AND (" + BILLCTIME
						+ " / 1000) < CAST(strftime('%s', date('" + selectionArgs[0] + "', abs(7 - strftime('%w', '"
						+ selectionArgs[0] + "')) || ' day')) AS INTEGER) AND " + BILLTYPE + " = " + type
						+ " GROUP BY date(" + BILLCTIME + "/1000, 'unixepoch', 'localtime') ORDER BY " + BILLCTIME
						+ ";";

			} else if (selection != null && selection.equals("week") && TextUtils.isEmpty(sortOrder)) {
				sql = "SELECT -1 AS " + STYPE + ", strftime('%w', date(" + BILLCTIME
						+ "/1000, 'unixepoch', 'localtime')) AS " + WEEKDAYS + ", count(" + _ID + ") AS " + COUNT
						+ ", SUM(" + BILLAMOUNT + ") AS " + SVALUE + ", date(" + BILLCTIME
						+ "/1000, 'unixepoch', 'localtime') AS " + DATETIME + " FROM "
						+ DailycostDatabaseHelper.DAILYCOST_TABLE_NAME + " WHERE (" + BILLCTIME
						+ " / 1000) >= CAST(strftime('%s', date('" + selectionArgs[0] + "', '-' || strftime('%w', '"
						+ selectionArgs[0] + "') || ' day')) AS INTEGER) AND (" + BILLCTIME
						+ " / 1000) < CAST(strftime('%s', date('" + selectionArgs[0] + "', abs(7 - strftime('%w', '"
						+ selectionArgs[0] + "')) || ' day')) AS INTEGER) " + " GROUP BY date(" + BILLCTIME
						+ "/1000, 'unixepoch', 'localtime') ORDER BY " + BILLCTIME + ";";

			} else if (selection != null && selection.equals("type")) {
				sql = "SELECT " + COSTTYPE + " AS " + STYPE + ", -1 AS " + WEEKDAYS + ", count(" + _ID + ") AS "
						+ COUNT + ", SUM(" + BILLAMOUNT + ") AS " + SVALUE + ", NULL AS " + DATETIME + " FROM "
						+ DailycostDatabaseHelper.DAILYCOST_TABLE_NAME + " WHERE (" + BILLCTIME
						+ " / 1000) >= CAST(strftime('%s', date('" + selectionArgs[0] + "', '-' || strftime('%w', '"
						+ selectionArgs[0] + "') || ' day')) AS INTEGER) AND (" + BILLCTIME
						+ " / 1000) < CAST(strftime('%s', date('" + selectionArgs[0] + "', abs(7 - strftime('%w', '"
						+ selectionArgs[0] + "')) || ' day')) AS INTEGER) AND " + BILLTYPE + " = " + type
						+ " GROUP BY " + COSTTYPE + " ORDER BY " + COSTTYPE + ";";

			} else {
				// sql = "";
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + uri);
		}
		Cursor ret = null;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if (sql != null) {
			ret = db.rawQuery(sql, null);
		} else {

			if (selection != null && selection.equals(MOTH)) {
				sql = "SELECT * FROM " + DailycostDatabaseHelper.DAILYCOST_TABLE_NAME
						+ " where year=? and moth=? and accountbookid=? and isdeleted=? ORDER BY " + BILLCTIME
						+ " DESC";
				ret = db.rawQuery(sql, selectionArgs);
			} else if (selection != null && selection.equals("moths")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String[] columns = new String[] { _ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
						TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2,
						EXTRAINFO3, YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID,
						BILLID, ACCOUNTBOOKID, USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME,
						BILLCATEICON, BILLSUBCATENAME, BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID,
						"SUM(" + BILLAMOUNT + ")" };
				String select = TRADETIME + ">=? and " + TRADETIME + "<=? and " + ACCOUNTBOOKID + "=? and " + BILLTYPE
						+ "=? and " + ISDELETED +"=? ";
				String[] selectionAr = selectionArgs;
				String groupBy = MOTH;
				String having = null;
				String orderBy = BILLCTIME + " DESC";
				ret = db.query(table, columns, select, selectionAr, groupBy, having, orderBy, null);

			} else if (selection != null && selection.equals("moths_three")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String[] columns = new String[] { _ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
						TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2,
						EXTRAINFO3, YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID,
						BILLID, ACCOUNTBOOKID, USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME,
						BILLCATEICON, BILLSUBCATENAME, BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID,
						"SUM(" + BILLAMOUNT + ")" };
				String select = YEAR + "=? and " + MOTH + "=? and " + ACCOUNTBOOKID + "=? and " + BILLTYPE + "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = BILLCATEID;
				String having = null;
				String orderBy = "SUM(" + BILLAMOUNT + ")" + " DESC";
				String limit = "3";
				ret = db.query(table, columns, select, selectionAr, groupBy, having, orderBy, limit);

			} else if (selection != null && selection.equals("moths_three_last")) {// 预期
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String[] columns = new String[] { _ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
						TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2,
						EXTRAINFO3, YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID,
						BILLID, ACCOUNTBOOKID, USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME,
						BILLCATEICON, BILLSUBCATENAME, BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID,
						"SUM(" + BILLAMOUNT + ")" };
				String select = TRADETIME + ">? and " + TRADETIME + "<? and " + ACCOUNTBOOKID + "=? and " + BILLTYPE
						+ "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = BILLCATEID;
				String having = null;
				String orderBy = "SUM(" + BILLAMOUNT + ")" + " DESC";
				String limit = "3";
				ret = db.query(table, columns, select, selectionAr, groupBy, having, orderBy, limit);

			} else if (selection != null && selection.equals("group_type")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String[] columns = new String[] { _ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
						TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2,
						EXTRAINFO3, YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID,
						BILLID, ACCOUNTBOOKID, USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME,
						BILLCATEICON, BILLSUBCATENAME, BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID,
						"SUM(" + BILLAMOUNT + ")" };
				String select = YEAR + "=? and " + MOTH + "=? and " + ACCOUNTBOOKID + "=? and " + ISDELETED + "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = BILLTYPE;
				String having = null;
				String orderBy = BILLCTIME + " DESC";
				String limit = null;
				ret = db.query(table, columns, select, selectionAr, groupBy, having, orderBy, limit);

			} else if (selection != null && selection.equals("which_type_day")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String[] columns = new String[] { _ID, USERID, BILLTYPE, BILLAMOUNT, COSTTYPE, CONTEXT, BILLCTIME,
						TRADETIME, UPDATETIME, WAGESID, ISDELETED, ISCLEAR, LNG, LAT, BILLMARK, EXTRAINFO1, EXTRAINFO2,
						EXTRAINFO3, YEAR, MOTH, DAY, ISSYNCHRONIZATION, WHICHBOOK, ACCOUNTBOOKTYPE, BILLCLEAR, PKID,
						BILLID, ACCOUNTBOOKID, USERNAME, USERICON, BILLCATEID, BILLSUBCATEID, BILLCATENAME,
						BILLCATEICON, BILLSUBCATENAME, BILLSUBCATEICON, ACCOUNTNUMBER, BILLCOUNT, DEVICEID,
						"SUM(" + BILLAMOUNT + ")" };
				String select = YEAR + "=? and " + MOTH + "=? and " + ACCOUNTBOOKID + "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = DAY;
				String having = null;
				String orderBy = BILLCTIME + " DESC";
				String limit = null;
				ret = db.query(table, columns, select, selectionAr, groupBy, having, orderBy, limit);

			} else if (selection != null && selection.equals("moths_type_order_balance")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;

				String select = YEAR + "=? and " + MOTH + "=? and " + ACCOUNTBOOKID + "=? and " + BILLTYPE + "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = null;
				String having = null;
				String orderBy = BILLAMOUNT + " DESC";
				String limit = null;
				ret = db.query(table, projection, select, selectionAr, groupBy, having, orderBy, limit);

			} else if (selection != null && selection.equals("moths_type")) {
				sql = "SELECT * FROM " + DailycostDatabaseHelper.DAILYCOST_TABLE_NAME
						+ " where year=? and moth=? and whichbook=? and type=?";
				ret = db.rawQuery(sql, selectionArgs);

			} else if (selection != null && selection.equals("item")) {
				String table = DailycostDatabaseHelper.DAILYCOST_TABLE_NAME;
				String select = TRADETIME + "=? and " + USERID + "=? and " + WHICHBOOK + "=?";
				String[] selectionAr = selectionArgs;
				String groupBy = null;
				String having = null;
				String orderBy = null;
				ret = db.query(table, projection, select, selectionAr, groupBy, having, orderBy, null);
			} else {
				ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			}

		}
		if (ret == null) {
			Log.e(TAG, "Wearable.query: failed");
		} else {
			ret.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#getType(android.net.Uri)
	 */
	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		int match = sURLMatcher.match(uri);
		switch (match) {
		case DAILYCOST:
		case DAILYCOST_SUMMARY:
			return "vnd.android.cursor.dir/dailycost";
		case DAILYCOST_ID:
			return "vnd.android.cursor.item/dailycost";
		default:
			throw new IllegalArgumentException("Unknown URL");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		long rowId;
		Uri uriReal;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		switch (sURLMatcher.match(uri)) {
		case DAILYCOST:
			synchronized (mMutex) {
				String createtime = values.getAsString(TRADETIME);
				String uid = values.getAsString(USERID);
				boolean byUpdating = false;
				Cursor cursor = db.query(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, new String[] { _ID }, TRADETIME
						+ " = ?1 AND " + USERID + " = ?2", new String[] { createtime, uid }, null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					byUpdating = true;
					rowId = cursor.getLong(0);
					uriReal = ContentUris.withAppendedId(uri, rowId);
					String where = _ID + "=" + rowId;
					update(uriReal, values, where, null);
				} else {
					rowId = db.insert(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, null, values);
					uriReal = CONTENT_URI;
				}

				if (cursor != null) {
					cursor.close();
				}

				if (byUpdating) {
					return uriReal;
				}
			}
			break;
		default:
			throw new IllegalArgumentException("Cannot insert from URL: " + uri);

		}
		Uri uriResult = ContentUris.withAppendedId(uriReal, rowId);
		getContext().getContentResolver().notifyChange(uriResult, null);
		return uriResult;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;
		String primaryKey;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		switch (sURLMatcher.match(uri)) {
		case DAILYCOST:
			count = db.delete(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, selection, selectionArgs);
			break;
		case DAILYCOST_ID:
			primaryKey = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				selection = _ID + "=" + primaryKey;
			} else {
				selection = _ID + "=" + primaryKey + " AND (" + selection + ")";
			}
			count = db.delete(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, selection, selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Cannot delete from URL: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int count;
		String primaryKey;
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();

		switch (sURLMatcher.match(uri)) {
		case DAILYCOST:
			count = db.update(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, values, selection, selectionArgs);
			break;
		case DAILYCOST_ID:
			primaryKey = uri.getLastPathSegment();
			count = db.update(DailycostDatabaseHelper.DAILYCOST_TABLE_NAME, values, _ID + "=" + primaryKey, null);
			break;

		default:
			throw new IllegalArgumentException("Cannot update from URL: " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
