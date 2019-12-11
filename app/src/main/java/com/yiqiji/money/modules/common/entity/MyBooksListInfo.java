package com.yiqiji.money.modules.common.entity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.yiqiji.money.modules.common.db.DailycostContract.DtBookListColumns;
import com.yiqiji.money.modules.common.db.DailycostDatabaseHelper;
import com.yiqiji.money.modules.common.plication.MyApplicaction;

import java.util.ArrayList;
import java.util.List;

public class MyBooksListInfo implements DtBookListColumns, Parcelable {

    private final static String[] BOOKLIST_COLUMNS = new String[]{_ID, CATEGORYDESC, CATEGORYID, CATEGORYTITLE,
            CATEGORYTYPE, ISCLEAR, CATEGORYICON, PARENTID, STATUS};

    public MyBooksListInfo() {
        // TODO Auto-generated constructor stub
    }

    public static long inset(List<MyBooksListInfo> booksListInfos) {
        SQLiteDatabase database = null;
        long id = -1;
        boolean result = true;

        try {
            if (booksListInfos == null) {
                return -1;
            }
            database = MyApplicaction.mOpenHelper.getWritableDatabase();
            database.beginTransaction();
            id = database.delete(DailycostDatabaseHelper.BOOK_LIST_TABLE_NAME, null, null);
            if (id > -1) {
                for (MyBooksListInfo myBooksListInfo : booksListInfos) {
                    ContentValues contentValues = setContentValues(myBooksListInfo);
                    id = database.insert(DailycostDatabaseHelper.BOOK_LIST_TABLE_NAME, null, contentValues);
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

    public static List<MyBooksListInfo> getMyBooksListInfo() {
        List<MyBooksListInfo> booksListInfos = new ArrayList<MyBooksListInfo>();
        Cursor cursor = null;
        try {
            SQLiteDatabase database = MyApplicaction.mOpenHelper.getWritableDatabase();
            cursor = database.query(DailycostDatabaseHelper.BOOK_LIST_TABLE_NAME, BOOKLIST_COLUMNS, null, null, null,
                    null, null, null);
            if (cursor == null) {
                return null;
            }
            if (cursor.moveToFirst()) {
                do {
                    MyBooksListInfo booksListInfo = new MyBooksListInfo(cursor);
                    booksListInfos.add(booksListInfo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return booksListInfos;

    }

    public MyBooksListInfo(Cursor cursor) {
        categorydesc = cursor.getString(cursor.getColumnIndex(CATEGORYDESC));
        categoryid = cursor.getString(cursor.getColumnIndex(CATEGORYID));
        categorytitle = cursor.getString(cursor.getColumnIndex(CATEGORYTITLE));
        categorytype = cursor.getString(cursor.getColumnIndex(CATEGORYTYPE));
        isclear = cursor.getString(cursor.getColumnIndex(ISCLEAR));
        categoryicon = cursor.getString(cursor.getColumnIndex(CATEGORYICON));
        parentid = cursor.getString(cursor.getColumnIndex(PARENTID));
        status = cursor.getString(cursor.getColumnIndex(STATUS));
    }

    public static ContentValues setContentValues(MyBooksListInfo booksListInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORYDESC, booksListInfo.getCategorydesc());
        contentValues.put(CATEGORYID, booksListInfo.getCategoryid());
        contentValues.put(CATEGORYTITLE, booksListInfo.getCategorytitle());
        contentValues.put(CATEGORYTYPE, booksListInfo.getCategorytype());
        contentValues.put(ISCLEAR, booksListInfo.getIsclear());
        contentValues.put(CATEGORYICON, booksListInfo.getCategoryicon());
        contentValues.put(PARENTID, booksListInfo.getParentid());
        contentValues.put(STATUS, booksListInfo.getStatus());
        return contentValues;

    }

    private String categorydesc;// 账本描述
    private String categoryid;// 账本ID
    private String categorytitle; // 账本名称

    private String categorytype; // 账本类型

    private String isclear; // 是否结算

    private String categoryicon; // 账本图标
    private String accountbookbgimg; // //账本背景
    private String parentid; //
    private String status; //

    public String getAccountbookbgimg() {
        return accountbookbgimg;
    }

    public void setAccountbookbgimg(String accountbookbgimg) {
        this.accountbookbgimg = accountbookbgimg;
    }

    public String getCategorydesc() {
        return categorydesc;
    }

    public void setCategorydesc(String categorydesc) {
        this.categorydesc = categorydesc;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getCategorytitle() {
        return categorytitle;
    }

    public void setCategorytitle(String categorytitle) {
        this.categorytitle = categorytitle;
    }

    public String getCategorytype() {
        return categorytype;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public String getCategoryicon() {
        return categoryicon;
    }

    public void setCategoryicon(String categoryicon) {
        this.categoryicon = categoryicon;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsclear() {
        return isclear;
    }

    public void setIsclear(String isclear) {
        this.isclear = isclear;
    }

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

        dest.writeString(categorydesc);
        dest.writeString(categoryid);
        dest.writeString(categorytitle);
        dest.writeString(categorytype);
        dest.writeString(isclear);
        dest.writeString(categoryicon);
        dest.writeString(parentid);
        dest.writeString(status);
        dest.writeString(accountbookbgimg);

    }

    public static final Creator<MyBooksListInfo> CREATOR = new Creator<MyBooksListInfo>() {
        @Override
        public MyBooksListInfo createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            // 必须按成员变量声明的顺序读取数据，不然会出现获取数据出错
            return new MyBooksListInfo(source);
        }

        @Override
        public MyBooksListInfo[] newArray(int size) {
            // TODO Auto-generated method stub
            return new MyBooksListInfo[size];
        }
    };

    public MyBooksListInfo(Parcel p) {

        this.categorydesc = p.readString();
        this.categoryid = p.readString();
        this.categorytitle = p.readString();
        this.categorytype = p.readString();
        this.isclear = p.readString();
        this.categoryicon = p.readString();
        this.parentid = p.readString();
        this.status = p.readString();
        this.accountbookbgimg = p.readString();

    }

}
