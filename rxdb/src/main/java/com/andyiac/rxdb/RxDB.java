package com.andyiac.rxdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;

public class RxDB {

    private Context mContext;
    private RxDB mInstance;
    private KvDB mKvDB;

    private String RXDB_NAME = "rxdb";

    private RxDB() {
    }

    public RxDB getInstance(Context context) {

        if (mInstance == null) {
            this.mContext = context;
            mInstance = new RxDB();
        }

        return mInstance;
    }

    public synchronized KvDB init(String dbname) {

        if (dbname != null) RXDB_NAME = dbname;

        if (mKvDB == null) {
            mKvDB = new KvDB(openBaseDB(RXDB_NAME));
        }
        return mKvDB;
    }

    public KvDB getKvDB() {
        return mKvDB;
    }


    private SQLiteDatabase openBaseDB(String dbName) {
        return mContext.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }


    // for db update

    /**
     * 当前apk 版本号
     *
     * @return
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
