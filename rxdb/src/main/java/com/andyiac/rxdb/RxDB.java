package com.andyiac.rxdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

public class RxDB {

    private Context mContext;
    private static RxDB mInstance;
    private KvDB mKvDB;

    private String RXDB_NAME = "rxdb";
    private int RXDB_VERSION = 1;

    private RxDB() {
    }

    public static RxDB getInstance() {

        if (mInstance == null) {
            synchronized (RxDB.class) {
                mInstance = new RxDB();
            }
        }

        return mInstance;
    }

    public synchronized void init(Context context, String dbName) {
        this.mContext = context;

        if (dbName != null) RXDB_NAME = dbName;
        if (mKvDB == null) {
            mKvDB = new KvDB(openBaseDB(RXDB_NAME));
        }
    }


    public void insert(String key, String value) {
        mKvDB.updateData(key, value);
    }

    public void update(String key, String value) {
        mKvDB.updateData(key, value);
    }

    public String query(String key) {
        return mKvDB.queryData(key);
    }

    public int delete(String key) {
        return mKvDB.deleteData(key);
    }


    private SQLiteDatabase openBaseDB(String dbName) {
        return mContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }


    //================================================
    // for db update
    //================================================

    /**
     * 当前apk 版本号
     *
     * @return apkVersionCode
     */
    private int getCurrentApkVersionCode() {
        int apkVersionCode = 0;
        try {
            apkVersionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return apkVersionCode;
    }

    /**
     * 判断apk是否更新了
     *
     * @param apkVersionCode
     * @return
     */
    private boolean isApkUpdate(int apkVersionCode) {
        boolean isUpdated = false;
        int apkVersionCodeFromSp = getLoginInfoSP().getInt("apkVersionCode", 0);
        if (apkVersionCode > apkVersionCodeFromSp) {
            isUpdated = true;
        }
        return isUpdated;
    }

    public SharedPreferences getLoginInfoSP() {
        return mContext.getSharedPreferences("rxdb_info", mContext.MODE_PRIVATE);
    }

}
