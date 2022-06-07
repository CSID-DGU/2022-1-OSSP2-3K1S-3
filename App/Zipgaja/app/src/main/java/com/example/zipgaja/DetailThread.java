package com.example.zipgaja;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

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

public class DetailThread {
    final static String TAG = "DetailThread";
    Context mContext;
    Detail detail;
    Handler handler;

    String id;
    String cLoc;
    String dLoc;
    String sort;

    public DetailThread(Handler handler, Context mContext, String id, String cLoc, String dLoc, String sort) {
        this.handler = handler;
        this.mContext = mContext;
        this.id = id;
        this.cLoc = cLoc;
        this.dLoc = dLoc;
        this.sort = sort;
    }

    public void run() {
        Intent intent = new Intent(mContext.getApplicationContext(), RecommendDetailActivity.class);
        String BASEURL = "http://ec2-3-39-232-107.ap-northeast-2.compute.amazonaws.com:3000/";
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
        JSONObject detailRequest = new JSONObject();
        try {
            detailRequest.put("id", this.id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "detailRequest: " + detailRequest);
        Call<Detail> call = retrofitAPI.getRecommendDetail(detailRequest);
        call.enqueue(new Callback<Detail>() {

            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if(response.isSuccessful()) {
                    // response.code() == 200
                    detail = response.body();
                    Log.d(TAG, "response.raw: " + response.raw());
                    Log.d(TAG, "response.detail: " + response.body());
                    Log.d(TAG, "response.code: " + response.code());
                    Log.d(TAG, "response.message: " + response.message());
                    Log.d(TAG, "response.ResponseBody: " + response.errorBody());
                    if((response.code() == 200) && (response.errorBody() == null)) {
                        Log.e(TAG, "성공");
                        Log.d(TAG, "detailRequest: " + detailRequest);
                        try {
                            String Detail_String = new Gson().toJson(detail);
                            Log.d(TAG, "detail: " + Detail_String);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "deatil이 NULL");
                        }

                        Log.e(TAG, detail.getData().toString());
                        String good1 = detail.getData().getGood_check1();
                        String good2 = detail.getData().getGood_check2();
                        String good1cnt = detail.getData().getGood1();
                        String good2cnt = detail.getData().getGood2();
                        String bad1 = detail.getData().getBad_check1();
                        String bad2 = detail.getData().getBad_check2();
                        String bad1cnt = detail.getData().getBad1();
                        String bad2cnt = detail.getData().getBad2();
                        String good3 = detail.getData().getGood3();
                        String good4 = detail.getData().getGood4();
                        String bad3 = detail.getData().getBad3();
                        String bad4 = detail.getData().getBad4();

                        // 다음 Activity 에 text 전달
                        intent.putExtra("good1", good1);
                        intent.putExtra("good2", good2);
                        intent.putExtra("good1cnt", good1cnt);
                        intent.putExtra("good2cnt", good2cnt);
                        intent.putExtra("bad1", bad1);
                        intent.putExtra("bad2", bad2);
                        intent.putExtra("bad1cnt", bad1cnt);
                        intent.putExtra("bad2cnt", bad2cnt);
                        intent.putExtra("good3", good3);
                        intent.putExtra("good4", good4);
                        intent.putExtra("bad3", bad3);
                        intent.putExtra("bad4", bad4);
                        intent.putExtra("currentLoc", cLoc);
                        intent.putExtra("destinationLoc", dLoc);
                        intent.putExtra("sort", sort);
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
            public void onFailure(Call<Detail> call, Throwable t) {
                Log.e(TAG, "정보 불러오기 실패: " + t.getMessage());
                Log.e(TAG, "요청 메시지: " + call.request());
            }
        });

    }
}
