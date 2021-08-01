package com.example.finedustalarm

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    var gson = GsonBuilder().setLenient().create()
    val sRetrofit = initRetrofit()

    private const val base_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

    private fun createOkHttpClient(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    private fun initRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(base_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(createOkHttpClient())
            .build()
}