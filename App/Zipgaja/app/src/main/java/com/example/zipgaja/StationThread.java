package com.example.zipgaja;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationThread extends Thread {
    final static String TAG = "StationThread";
    Context mContext;
    StationInfo stationInfo;
    Handler handler;

    float lat;
    float lon;

    public StationThread(Handler handler, Context mContext, float lat, float lon) {
        this.handler = handler;
        this.mContext = mContext;
        this.lat = lat;
        this.lon = lon;
    }

    // @Override
    public void run(GoogleMap googleMap) {
        super.start();
        String BASEURL = " http://ec2-3-39-232-107.ap-northeast-2.compute.amazonaws.com:3000/";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        JSONObject stationRequest = new JSONObject();
        try {
            stationRequest.put("lati", this.lat);
            stationRequest.put("long", this.lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Call<StationInfo> call = retrofitAPI.getStationInfo(stationRequest);
        call.enqueue(new Callback<StationInfo>() {

            @Override
            public void onResponse(@NonNull Call<StationInfo> call, @NonNull Response<StationInfo> response) {
                if(response.isSuccessful()) {
                    // response.code() == 200
                    stationInfo = response.body();
                    Log.d(TAG, "response.raw: " + response.raw());
                    Log.d(TAG, "response.stationInfo: " + response.body());
                    Log.d(TAG, "response.code: " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.ResponseBody: " + response.errorBody());
                    if((response.code() == 200) && (response.errorBody() == null)) {
                        Log.e(TAG, "성공");
                        Log.d(TAG, "stationRequest: " + stationRequest);
                        try {
                            String StationInfo_String = new Gson().toJson(stationInfo);
                            Log.d(TAG, "stationInfo: " + StationInfo_String);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "stationInfo가 NULL");
                        }

                        // Bike Station 표시
                        for (Bike bike : stationInfo.getData()[0].getBike()) {
                            Log.d(TAG, "bike " + bike.toString());
                            String num = bike.getNum();
                            String name = bike.getName();
                            float latitude = bike.getLatitude();
                            float longitude = bike.getLongitude();

                            MarkerOptions bikeStations = new MarkerOptions();
                            bikeStations.position(new LatLng(latitude, longitude));
                            bikeStations.title(name);
                            bikeStations.snippet(num);

                            @SuppressLint("UseCompatLoadingForDrawables")
                            BitmapDrawable bitmapdraw = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.bike_station);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 128, 72, false);
                            bikeStations.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            googleMap.addMarker(bikeStations);
                        }
                        // Bus Station 표시
                        for (Bus bus : stationInfo.getData()[0].getBus()) {
                            Log.d(TAG, "bus " + bus.toString());
                            String name = bus.getName();
                            String station_name = bus.getStation_name();
                            float latitude = bus.getLatitude();
                            float longitude = bus.getLongitude();

                            MarkerOptions busStations = new MarkerOptions();
                            busStations.position(new LatLng(latitude, longitude));
                            busStations.title(name);
                            busStations.snippet(station_name);

                            @SuppressLint("UseCompatLoadingForDrawables")
                            BitmapDrawable bitmapdraw = (BitmapDrawable) mContext.getResources().getDrawable(R.drawable.bus_station);
                            Bitmap b = bitmapdraw.getBitmap();
                            Bitmap smallMarker = Bitmap.createScaledBitmap(b, 45, 93, false);
                            busStations.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                            googleMap.addMarker(busStations);
                        }

                    } else {
                        Log.e(TAG, "요청 실패 / status: " + response.code());
                    }
                } else {
                    // response.code() == 404
                    Log.e(TAG, "요청 실패 / status: " + response.code());
                    Log.e(TAG, "요청 실패 / 요청 메시지: " + call.request());
                }
            }

            @Override
            public void onFailure(@NonNull Call<StationInfo> call, @NonNull Throwable t) {
                Log.e(TAG, "정보 불러오기 실패: " + t.getMessage());
                Log.e(TAG, "요청 메시지: " + call.request());
            }
        });
    }

}
