package com.yiqiji.money.modules.common.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dansakai on 2017/3/14.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fund.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS sfuntable" +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId text, name text,code text,curPrice text, upDegr text)");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE sfuntable ADD COLUMN other STRING");
    }
}
