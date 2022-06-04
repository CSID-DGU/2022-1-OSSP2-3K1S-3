package com.example.zipgaja;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class DetailLoadingActivity extends AppCompatActivity {
    final static String TAG = "DetailLoading";
    Handler handler;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        String busNum = "";

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            busNum = "error";
        } else {
            busNum = extras.getString("busNum");
        }

        mContext = getApplicationContext();
        DetailThread detailThread = new DetailThread(handler, mContext, busNum);
        detailThread.run();

    }
}
