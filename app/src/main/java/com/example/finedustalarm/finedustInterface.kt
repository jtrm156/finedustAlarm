package com.example.finedustalarm

import retrofit2.Call
import retrofit2.http.GET
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