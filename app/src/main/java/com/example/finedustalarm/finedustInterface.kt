package com.example.finedustalarm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface finedustInterface {

    @GET("getCtprvnRltmMesureDnsty?")
    @Headers("Content-Type: application/json")
    fun finedust(@Query("serviceKey") serviceKey: String,
                 @Query("returnType") returnType: String,
                 @Query("numOfRows") numOfRows: String,
                 @Query("pageNo") pageNo: String,
                 @Query("sidoName") sidoName: String,
                 @Query("ver") ver : String
                 )
    : Call<finedustdata>
}

interface KakaoApiService{
    @GET("v2/local/geo/transcoord.json")
    fun transKakaoAddress(
        @Header("authorization") authorization : String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("input_coord") input_coord: String,
        @Query("output_coord") output_coord: String,
    ): Call<kakaodata1>
}

interface KakaoApiService2{
    @GET("v2/local/geo/coord2regioncode.json")
    fun transKakaoAddress(
        @Header("authorization") authorization : String,
        @Query("x") x: Double,
        @Query("y") y: Double,
        @Query("input_coord") input_coord: String,
        @Query("output_coord") output_coord: String,
    ): Call<kakaodata2>
}

interface finedustInterface2 {
    @GET("getNearbyMsrstnList?")
    @Headers("Content-Type: application/json")
    fun NearbyMsrstnList(@Query("serviceKey") serviceKey: String,
                 @Query("returnType") returnType: String,
                 @Query("tmX") tmX: Double,
                 @Query("tmY") tmY: Double,
    ): Call<finedustdata2>
}

interface finedustInterface3 {

    @GET("getMsrstnAcctoRltmMesureDnsty?")
    @Headers("Content-Type: application/json")
    fun MsrstnAcctoRltmMesureDnsty(@Query("serviceKey") serviceKey: String,
                                   @Query("returnType") returnType: String,
                                   @Query("numOfRows") numOfRows: String,
                                   @Query("pageNo") pageNo: String,
                                   @Query("stationName") stationName: String,
                                   @Query("dataTerm") dataTerm : String,
                                   @Query("ver") ver : String
    ): Call<finedustdata3X>
}