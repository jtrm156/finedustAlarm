package com.example.finedustalarm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface finedustInterface {

    @GET("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty")
    fun finedust(@Query("stationName") stationName: String,
                 @Query("dataTerm") dataTerm: String,
                 @Query("pageNo") pageNo: Int,
                 @Query("numOfRows") numOfRows: Int,
                 @Query("returnType") returnType: String,
                 @Query("serviceKey") serviceKey: String)
    : Call<Item>
}