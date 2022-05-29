package com.example.zipgaja;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    // Station 불러오기
    // @Headers({"Accept: application/json"})
    @Headers({"Connection: close"})
    @POST("/Api/Main/getStation")   // GET 으로 보내기
    Call<StationInfo> getData(
            @Body StationRequest stationRequest
    );

}
