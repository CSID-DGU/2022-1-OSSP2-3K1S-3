package com.example.zipgaja.module.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClientInterface {
    @GET("/Api/route/lessmoney")
    Call<LessMoneyResponse> getLessMoney(
            @Query("postID") int postID
    );

    @GET("/Api/Detail")
    Call<DetailResponse> getDetail(
            @Query("id") int id
    );
}
