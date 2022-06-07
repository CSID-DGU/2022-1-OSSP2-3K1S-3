package com.example.zipgaja;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RouteThread {
    final static String TAG = "RouteThread";
    Context mContext;
    SearchList searchList;
    Handler handler;

    String sAdd;    // 시작 주소
    float sLong;    // 시작 longitude
    float sLati;    // 시작 latitude
    String sName;   // 시작 장소명
    String eAdd;    // 도착 주소
    float eLong;    // 도착 longitude
    float eLati;    // 도착 latitude
    String eName;   // 도착 장소명
    String type;    // lessMoney, recommend, lessTime

    public RouteThread(Handler handler, Context mContext,
                       String sAdd, String sName, float sLati, float sLong,
                       String eAdd, String eName, float eLati, float eLong, String type) {
        this.handler = handler;
        this.mContext = mContext;
        this.sAdd = sAdd;
        this.sName = sName;
        this.sLati = sLati;
        this.sLong = sLong;
        this.eAdd = eAdd;
        this.eName = eName;
        this.eLati = eLati;
        this.eLong = eLong;
        this.type = type;
    }

    // @Override
    public void run() {
        // super.run();
        Intent intent = new Intent(mContext.getApplicationContext(), SearchListActivity.class);
        String BASEURL = " http://ec2-3-39-232-107.ap-northeast-2.compute.amazonaws.com:3000/";
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.MINUTES)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        JSONObject searchListRequest = new JSONObject();
        try {
            searchListRequest.put("sName", this.sName);
            searchListRequest.put("sLati", this.sLati);
            searchListRequest.put("sLong", this.sLong);
            searchListRequest.put("eName", this.eName);
            searchListRequest.put("eLati", this.eLati);
            searchListRequest.put("eLong", this.eLong);
            searchListRequest.put("type", this.type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "searchListRequest: " + searchListRequest);
        Call<SearchList> call = retrofitAPI.getSearchList(searchListRequest);
        call.enqueue(new Callback<SearchList>() {


            @Override
            public void onResponse(@NonNull Call<SearchList> call, @NonNull Response<SearchList> response) {
                if(response.isSuccessful()) {
                    // response.code() == 200
                    searchList = response.body();
                    Log.d(TAG, "response.raw: " + response.raw());
                    Log.d(TAG, "response.searchList: " + response.body());
                    Log.d(TAG, "response.code: " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.ResponseBody: " + response.errorBody());
                    if((response.code() == 200) && (response.errorBody() == null)) {
                        Log.e(TAG, "성공");
                        Log.d(TAG, "searchListRequest: " + searchListRequest);
                        try {
                            String SearchList_String = new Gson().toJson(searchList);
                            Log.d(TAG, "searchList: " + SearchList_String);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "searchList가 NULL");
                        }

                        // 다음 Activity 에 text 및 data 전달
                        intent.putExtra("currentLocation", sName);
                        intent.putExtra("currentAddress", sAdd);
                        intent.putExtra("destinationLocation", eName);
                        intent.putExtra("destinationAddress", eAdd);
                        intent.putExtra("sortCriterion", type);
                        intent.putExtra("result", searchList);
                        mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                        // finish();

                    } else {
                        Log.e(TAG, "요청 실패 / status: " + response.code());
                        Log.d(TAG, "response.ResponseBody: " + response.errorBody());
                    }
                } else {
                    // response.code() == 404
                    Log.e(TAG, "요청 실패 / status: " + response.code());
                    Log.e(TAG, "요청 실패 / 요청 메시지: " + call.request());
                }
            }

            @Override
            public void onFailure(@NonNull Call<SearchList> call, @NonNull Throwable t) {
                Log.e(TAG, "정보 불러오기 실패: " + t.getMessage());
                Log.e(TAG, "요청 메시지: " + call.request());

            }

        });


    }

}
