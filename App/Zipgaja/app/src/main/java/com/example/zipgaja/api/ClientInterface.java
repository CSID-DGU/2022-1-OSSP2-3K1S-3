package com.example.zipgaja.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @POST("/Api/Recommend/good")
    Call<StatusResponse> postRecommnedGood(
            @Query("id") int id,
            @Query("good1") boolean good1,
            @Query("good2") boolean good2,
            @Query("good3") boolean good3,
            @Query("good4") boolean good4,
            @Query("good") String good
    );

    @POST("/Api/Recommend/bad")
    Call<StatusResponse> postRecommnedBad(
            @Query("id") int id,
            @Query("bad1") boolean bad1,
            @Query("bad2") boolean bad2,
            @Query("bad3") boolean bad3,
            @Query("bad4") boolean bad4,
            @Query("bad") String good
    );
}
