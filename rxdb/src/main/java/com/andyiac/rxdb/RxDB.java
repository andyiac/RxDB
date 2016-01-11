package com.andyiac.rxdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RxDB {

    private Context mContext;

    private RxDB() {
    }

    private RxDB mInstance;

    private RxDB getInstance(Context context) {

        if (mInstance == null) {
            this.mContext = context;
            mInstance = new RxDB();
        }

        return mInstance;
    }

    private SQLiteDatabase openBaseDB(String dbName) {
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + "com.smiletv.haohuo" + File.separator
                + dbName;
        File db = new File(path);

        int apkVersionCode = getCurrentApkVersionCode();

        boolean isUpdated = isApkUpdate(apkVersionCode);

        if (!db.exists() || isUpdated) {
            try {
                InputStream is = mContext.getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }

        }

        return mContext.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
    }

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
        return mContext.getSharedPreferences("login_info", mContext.MODE_PRIVATE);
    }

}
