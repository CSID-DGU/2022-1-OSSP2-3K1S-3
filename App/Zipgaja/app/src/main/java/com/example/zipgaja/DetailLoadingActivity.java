package com.example.zipgaja;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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
        String currentLoc = "";
        String destinationLoc = "";
        String sort = "";

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            busNum = "error";
        } else {
            busNum = extras.getString("busNum");
            currentLoc = extras.getString("currentLoc");
            destinationLoc = extras.getString("destinationLoc");
            sort = extras.getString("sort");
        }

        mContext = getApplicationContext();
        DetailThread detailThread = new DetailThread(handler, mContext, busNum, currentLoc, destinationLoc, sort);
        detailThread.run();

    }
}
