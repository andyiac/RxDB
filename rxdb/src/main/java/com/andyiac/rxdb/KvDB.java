package com.andyiac.rxdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.andyiac.rxdb.core.Column;
import com.andyiac.rxdb.core.TableHelper;

/**
 * 全局配置数据库表
 */
public class KvDB extends SQLiteOpenHelper {

    private static final boolean d = true;
    private SQLiteDatabase db;
    private static TableHelper sqLiteTable = new TableHelper(KVDataEntry.TABLE_NAME);

    public KvDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public KvDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }


    public static KvDB getInstance() {
        return null;
    }


    /**
     * 新建表 配置表
     */
    private void createConfigTable() {
        sqLiteTable.addColumn(KVDataEntry.COLUMN_NAME_KEY, Column.DataType.TEXT);
        sqLiteTable.addColumn(KVDataEntry.COLUMN_NAME_VALUE, Column.DataType.TEXT);
        sqLiteTable.create(db);
    }


    public void addData(String key, String value) {

        String isHaveValue = getData(key);
        if (isHaveValue != null && !isHaveValue.isEmpty()) {
            deleteConfig(key);
        }

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        //values.put(SmsJsonDataContract.SmsDataEntry._ID, 1);
        values.put(KVDataEntry.COLUMN_NAME_KEY, key);
        values.put(KVDataEntry.COLUMN_NAME_VALUE, value);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(KVDataEntry.TABLE_NAME, null, values);
    }

    public String getData(String key) {

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                KVDataEntry.COLUMN_NAME_VALUE
        };

        Cursor c = db.query(
                KVDataEntry.TABLE_NAME,                   // The table to query
                projection,                                   // The columns to return
                "key = ?",                                  // The columns for the WHERE clause
                new String[]{key},                            // The values for the WHERE clause
                null,                                         // don't group the rows
                null,                                         // don't filter by row groups
                null// The sort order
        );

        if (c.getCount() == 0) return null;
        c.moveToFirst();
        String s = c.getString(c.getColumnIndexOrThrow(KVDataEntry.COLUMN_NAME_VALUE));

        return s == null ? "" : s;
    }

    public void deleteTable() {
        sqLiteTable.delete(db);
    }

    /**
     * 删除指定key 的config
     */
    private void deleteConfig(String key) {

        String[] projection = {
                KVDataEntry.COLUMN_NAME_VALUE
        };

        Cursor c = db.query(
                KVDataEntry.TABLE_NAME,  // The table to query
                projection,                                   // The columns to return
                null,                                         // The columns for the WHERE clause
                null,                                         // The values for the WHERE clause
                null,                                         // don't group the rows
                null,                                         // don't filter by row groups
                null// The sort order
        );
        if (c.getCount() == 0) return;
        c.moveToFirst();
        db.delete(KVDataEntry.TABLE_NAME, "key=?", new String[]{key});
    }


    public void deleteAllData() {
        db.execSQL("delete from " + KVDataEntry.TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /* Inner class that defines the table contents */
    public static abstract class KVDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "rxdb_kv_default_table";
        public static final String COLUMN_NAME_KEY = "key";
        public static final String COLUMN_NAME_VALUE = "value";
    }


}
