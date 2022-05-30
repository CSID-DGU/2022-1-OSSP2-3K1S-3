package com.example.zipgaja;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StationThread extends Thread {
    final static String TAG = "StationThread";
    Context mContext;
    StationInfo stationInfo = new StationInfo(999);
    Handler handler;

    float lat;
    float lon;

    public StationThread(Handler handler, Context mContext, float lat, float lon) {
        this.mContext = mContext;
        this.lat = lat;
        this.lon = lon;
        this.handler = handler;
    }

    @Override
    public void run() {
        super.run();
        String BASEURL = "http://ec2-107-23-186-215.compute-1.amazonaws.com:5000";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Station station = new Station();
        StationRequest stationRequest = new StationRequest(this.lat, this.lon);
        Call<StationInfo> call = retrofitAPI.getData(stationRequest);
        call.enqueue(new Callback<StationInfo>() {

            @Override
            public void onResponse(@NonNull Call<StationInfo> call, @NonNull Response<StationInfo> response) {
                StationInfo stationInfo = new StationInfo(response.code());
                if(response.isSuccessful()) {
                    // List<StationInfo> stationInfo= response.body();
                    stationInfo = response.body();
                    Log.d(TAG, "response.raw: " + response.raw());
                    if(stationInfo.getStatus() == 200) {
                        station.setNum(stationInfo.getStationData().get(0).getBikeStation().get(0).getNum());
                        station.setName(stationInfo.getStationData().get(0).getBikeStation().get(0).getName());
                        station.setLatitude(stationInfo.getStationData().get(0).getBikeStation().get(0).getLatitude());
                        station.setLongitude(stationInfo.getStationData().get(0).getBikeStation().get(0).getLongitude());

                        station.setStationId(stationInfo.getStationData().get(0).getBusStation().get(0).getStationId());

                        Message msg = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("Station", "Station");
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    } else {
                        Log.e(TAG, "요청 실패 / status: " + stationInfo.getStatus());
                    }
                } else {
                    Log.e(TAG, "요청 실패 / status: " + stationInfo.getStatus());
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
