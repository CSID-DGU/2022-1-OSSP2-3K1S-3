package com.example.zipgaja;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

public class RouteLoadingActivity extends AppCompatActivity {
    final static String TAG = "RouteLoading";
    Handler handler;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView loadingGIF = (ImageView)findViewById(R.id.loadingImage);
        Glide.with(this).load(R.raw.loading).into(loadingGIF);

        String currentLocation = "";
        String destinationLocation = "";
        String sortCriterion = "";

        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            currentLocation = "error";
        } else {
            currentLocation = extras.getString("currentLocation");
            destinationLocation = extras.getString("destinationLocation");
            sortCriterion = extras.getString("sortCriterion");
        }

        switch (sortCriterion) {
            case "최소시간순":
            case "lessTime":
                sortCriterion = "lessTime";
                break;
            case "최소금액순":
            case "lessMoney":
                sortCriterion = "lessMoney";
                break;
            case "추천순":
            case "recommend":
                sortCriterion = "recommend";
                break;
        }

        final Geocoder geocoder = new Geocoder(this);

        // 출발지 위도/경도 추출
        List<Address> currentList = null;
        String currentName = "";
        double currentLat = 0;
        double currentLon = 0;
        try {
                currentList = geocoder.getFromLocationName(currentLocation, 10);
            Log.e(TAG, "출발지 위도경도" + currentList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "출발지 위도경도 error");
            Toast.makeText(getApplicationContext(), "출발지(" + currentLocation + ")가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (currentList != null) {
            if (currentList.size() == 0) {
                Toast.makeText(getApplicationContext(), "출발지(" + currentLocation + ")가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            else {
                Address address = currentList.get(0);
                currentName = address.getAddressLine(0);
                currentLat = address.getLatitude();
                currentLon = address.getLongitude();
                System.out.print("currentLat: " + currentLat);
                System.out.println("\t\tcurrentLon: " + currentLon);
            }
        }

        // 목적지 위도/경도 추출
        List<Address> destinationList = null;
        String destinationName = "";
        double destinationLat = 0;
        double destinationLon = 0;
        try {
            destinationList = geocoder.getFromLocationName(destinationLocation, 10);
            Log.e(TAG, "목적지 위도경도" + destinationList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "목적지 위도경도 error");
            Toast.makeText(getApplicationContext(), "목적지(" + destinationLocation + ")가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (destinationList != null) {
            if (destinationList.size() == 0) {
                Toast.makeText(getApplicationContext(), "목적지(" + destinationLocation + ")가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainViewActivity.class);
                startActivity(intent);
                finish();
                return;
            }
            else {
                Address address = destinationList.get(0);
                destinationName = address.getAddressLine(0);
                destinationLat = address.getLatitude();
                destinationLon = address.getLongitude();
                System.out.print("destinationLat: " + destinationLat);
                System.out.println("\tdestinationLon: " + destinationLon);
            }
        }

        float cLat = (float) currentLat;
        float cLon = (float) currentLon;
        float dLat = (float) destinationLat;
        float dLon = (float) destinationLon;

        Log.e(TAG, currentName + " " + cLat + " " + cLon + " " + destinationName + " " + dLat + " " + dLon);


        mContext = getApplicationContext();
        RouteThread routeThread = new RouteThread(handler, mContext, currentName, currentLocation, cLat, cLon, destinationName, destinationLocation, dLat, dLon, sortCriterion);
//        RouteThread routeThread = new RouteThread(handler, mContext, currentName, cLat, cLon, destinationName, dLat, dLon, sortCriterion);
        routeThread.run();

    }
}
