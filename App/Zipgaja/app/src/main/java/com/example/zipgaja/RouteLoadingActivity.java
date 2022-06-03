package com.example.zipgaja;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

//        Intent intent = new Intent(getApplicationContext(), SearchListActivity.class);

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

        if (sortCriterion.equals("최소시간순")) {
            sortCriterion = "lessTime";
        } else if (sortCriterion.equals("최소금액순")) {
            sortCriterion = "lessMoney";
        } else if (sortCriterion.equals("추천순")) {
            sortCriterion = "recommend";
        }

        final Geocoder geocoder = new Geocoder(this);

        // 출발지 위도/경도 추출
        List<Address> currentList = null;
        double currentLat = 0;
        double currentLon = 0;
        try {
                currentList = geocoder.getFromLocationName(currentLocation, 10);
            Log.e(TAG, "출발지 위도경도" + currentList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "출발지 위도경도 error");
        }
//        int i = 0;
//        while (currentList == null) {
//            try {
//                currentList = geocoder.getFromLocationName(currentLocation, 10);
//                Log.e("DANCE", "출발지 위도경도" + currentList);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("DANCE", "출발지 위도경도 error" + i);
//            }
//            i++;
//        }
        if (currentList != null) {
            if (currentList.size() == 0) {
                Toast.makeText(getApplicationContext(), "출발지가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Address address = currentList.get(0);
                currentLat = address.getLatitude();
                currentLon = address.getLongitude();
                System.out.print("currentLat: " + currentLat);
                System.out.println("\t\tcurrentLon: " + currentLon);
            }
        }

        // 목적지 위도/경도 추출
        List<Address> destinationList = null;
        double destinationLat = 0;
        double destinationLon = 0;
        try {
            destinationList = geocoder.getFromLocationName(destinationLocation, 10);
            Log.e(TAG, "목적지 위도경도" + destinationList);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "목적지 위도경도 error");
        }
//        int j = 0;
//        while (currentList == null) {
//            try {
//                destinationList = geocoder.getFromLocationName(destinationLocation, 10);
//                Log.e("DANCE", "목적지 위도경도" + destinationList);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("DANCE", "목적지 위도경도 error" + j);
//            }
//            j++;
//        }
        if (destinationList != null) {
            if (destinationList.size() == 0) {
                Toast.makeText(getApplicationContext(), "목적지가 올바른 위치가 아닙니다.", Toast.LENGTH_LONG).show();
                return;
            }
            else {
                Address address = destinationList.get(0);
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

        Log.e(TAG, currentLocation + " " + cLat + " " + cLon + " " + destinationLocation + " " + dLat + " " + dLon);


        mContext = getApplicationContext();
        RouteThread routeThread = new RouteThread(handler, mContext, currentLocation, cLat, cLon, destinationLocation, dLat, dLon, sortCriterion);
        routeThread.run();

//        int i = 0;
//        while (true) {
//            if (resultList != null) {
//                break;
//            }
//            System.out.println(i);
//            i++;
//        }

        // Log.e(TAG, resultList);




        // 다음 Activity 에 text 및 data 전달
//        intent.putExtra("currentLocation", currentLocation);
//        intent.putExtra("destinationLocation", destinationLocation);
//        startActivity(intent);
//        finish();

    }
}
