package com.example.zipgaja.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClientInterface {

    @FormUrlEncoded
    @POST("/Api/route/detailRoute")
    Call<LessMoneyResponse> getLessMoney(
            @Field("id") String id,
            @Field("start") String start,
            @Field("end") String end
    );

    @FormUrlEncoded
    @POST("/Api/Detail")
    Call<DetailResponse> getDetail(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("/Api/Recommend/good")
    Call<StatusResponse> postRecommnedGood(
            @Field("id") String id,
            @Field("good1") boolean good1,
            @Field("good2") boolean good2,
            @Field("good3") boolean good3,
            @Field("good4") boolean good4,
            @Field("good") String good
    );

    @FormUrlEncoded
    @POST("/Api/Recommend/bad")
    Call<StatusResponse> postRecommnedBad(
            @Field("id") String id,
            @Field("bad1") boolean bad1,
            @Field("bad2") boolean bad2,
            @Field("bad3") boolean bad3,
            @Field("bad4") boolean bad4,
            @Field("bad") String good
    );
}
