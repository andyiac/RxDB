package com.andyiac.demo;

import android.app.Application;

import com.andyiac.rxdb.RxDB;

/**
 * @author andyiac
 * @date 1/25/16
 * @web www.andyiac.com
 */
public class ClientApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RxDB.getInstance().init(this, "first_test.db");

    }


}
