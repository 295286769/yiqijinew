package com.yiqiji.money.modules.common.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yiqiji.frame.core.utils.StringUtils;
import com.yiqiji.money.modules.property.entity.FundEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dansakai on 2017/3/14.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /**
     * 删除表重新创建
     */
    public void revertSeq() {

        db.execSQL("DROP TABLE IF EXISTS sfuntable");

        db.execSQL("CREATE TABLE IF NOT EXISTS sfuntable" +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userId text, name text,code text,curPrice text, upDegr text)");
    }

    /**
     * add
     */
    public void add(List<FundEntity> entities) {
        List<FundEntity> exEntity = query();
        List<String> codes = new ArrayList<>();
        if (!StringUtils.isEmptyList(exEntity)) {
            for (FundEntity entity : exEntity) {
                codes.add(entity.getCode());
            }
        }

        db.beginTransaction();  //开始事务
        try {
            for (FundEntity entity : entities) {
                if (StringUtils.isEmptyList(codes)) {
                    db.execSQL("INSERT INTO sfuntable VALUES(NULL,?, ?, ?, ?, ?)", new Object[]{entity.userId, entity.name, entity.code, entity.curPrice, entity.upDegr});
                } else {
                    if (!codes.contains(entity.getCode())) {
                        db.execSQL("INSERT INTO sfuntable VALUES(NULL,?, ?, ?, ?, ?)", new Object[]{entity.userId, entity.name, entity.code, entity.curPrice, entity.upDegr});
                    }
                }

            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * add single
     */
    /**
     * add
     */
    public void addSingle(FundEntity entity) {
        List<FundEntity> exEntity = query();
        List<String> codes = new ArrayList<>();
        if (!StringUtils.isEmptyList(exEntity)) {
            for (FundEntity ent : exEntity) {
                codes.add(ent.getCode());
            }
        }
        if (codes.contains(entity.getCode())) {
            return;
        }
        db.beginTransaction();  //开始事务
        try {
            db.execSQL("INSERT INTO sfuntable VALUES(NULL,?, ?, ?, ?, ?)", new Object[]{entity.userId, entity.name, entity.code, entity.curPrice, entity.upDegr});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }

    /**
     * update
     */
    public void update(FundEntity entity) {
        ContentValues cv = new ContentValues();
        cv.put("userId", entity.getUserId());
        cv.put("name", entity.getName());
        cv.put("code", entity.getCode());
        cv.put("curPrice", entity.getCurPrice());
        cv.put("upDegr", entity.getUpDegr());
        db.update("sfuntable", cv, "userId = ?,name = ?,code = ?,curPrice = ?,upDegr = ?", new String[]{entity.userId, entity.name, entity.code, entity.curPrice, entity.upDegr});
    }

    /**
     * delete
     */
    public void delete(FundEntity entity) {
        db.delete("sfuntable", "code = ?", new String[]{entity.code});
    }

    /**
     * query
     */
    public List<FundEntity> query() {
        ArrayList<FundEntity> entities = new ArrayList<>();
        Cursor c = queryTheCursor();
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                FundEntity entity = new FundEntity();
                entity._id = c.getInt(c.getColumnIndex("_id"));
                entity.userId = c.getString(c.getColumnIndex("userId"));
                entity.name = c.getString(c.getColumnIndex("name"));
                entity.code = c.getString(c.getColumnIndex("code"));
                entity.curPrice = c.getString(c.getColumnIndex("curPrice"));
                entity.upDegr = c.getString(c.getColumnIndex("upDegr"));
                entities.add(entity);
            }
        }
        c.close();
        return entities;
    }

    /**
     * query all entity, return cursor
     *
     * @return Cursor
     */
    public Cursor queryTheCursor() {
        Cursor c = db.rawQuery("SELECT * FROM sfuntable", null);
        return c;
    }

    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}
