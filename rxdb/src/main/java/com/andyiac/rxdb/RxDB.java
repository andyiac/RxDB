package com.andyiac.rxdb;

import android.content.Context;

public class RxDB {

    private RxDB() {

    }

    private RxDB mInstance;

    private RxDB getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new RxDB();
        }

        return mInstance;
    }


}
