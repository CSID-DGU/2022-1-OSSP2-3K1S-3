package com.example.zipgaja;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    // Station 불러오기
    @Headers({"Accept: application/json", "Connection: close"})
    @POST("Api/Main/getStation")
    Call<StationInfo> getStationInfo(
            @Body JSONObject jsonObject
    );

    @Headers({"Accept: application/json", "Connection: close"})
    @POST("Api/route/searchList")
    Call<SearchList> getSearchList(
            @Body JSONObject jsonObject
    );

    @Headers({"Accept: application/json", "Connection: close"})
    @POST("Api/Detail")
    Call<Detail> getRecommendDetail(
            @Body JSONObject jsonObject
    );

}
