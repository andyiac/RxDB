package com.andyiac.rxdb.core;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author andyiac
 * @date 1/22/16
 * @web www.andyiac.com
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DEFAULT_TABLE_NAME = "kv_table";

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        initDefaultTable(db);
    }


    private void initDefaultTable(SQLiteDatabase db) {
        db.beginTransaction();
        try {
            TableHelper rxtable = new TableHelper(DEFAULT_TABLE_NAME);
            rxtable.addColumn(new Column("key", Column.Constraint.PRIMARY_KEY, Column.DataType.TEXT));
            rxtable.addColumn(new Column("value", Column.Constraint.NULL, Column.DataType.TEXT));
            rxtable.create(db);
        } finally {
            db.endTransaction();
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
