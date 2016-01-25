package com.andyiac.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.andyiac.rxdb.RxDB;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvContent = (TextView) findViewById(R.id.tv_content);

    }


    public void onClickInsert(View view) {
        RxDB.getInstance().insert("abc", "asdfasdfasdfasdf");
    }

    public void onClickDelete(View view) {
        RxDB.getInstance().delete("abc");
    }

    public void onClickUpdate(View view) {
        RxDB.getInstance().update("abc", "====================");
    }

    public void onClickQuery(View view) {
        // 非rx方式
        // String rs = RxDB.getInstance().query("abc");

        // rx方式支持
        RxDB.getInstance().rxQuery("abc")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        tvContent.setText(s);
                    }
                });
    }

}
