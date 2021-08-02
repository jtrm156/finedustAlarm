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

object RetrofitClient2 {
    var gson = GsonBuilder().setLenient().create()
    val sRetrofit3 = initRetrofit()

    private const val base_URL = "http://apis.data.go.kr/B552584/MsrstnInfoInqireSvc/"

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

object RetrofitClient3 {
    var gson = GsonBuilder().setLenient().create()
    val sRetrofit3 = initRetrofit()

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
object KakaoApiRetrofitclient{
    var gson2 = GsonBuilder().setLenient().create()
    val sRetrofit2 = initRetrofit2()

    private const val base_URL2 = "https://dapi.kakao.com/"

    private fun createOkHttpClient2(): OkHttpClient{
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(interceptor)
        return builder.build()
    }

    private fun initRetrofit2() : Retrofit =
        Retrofit.Builder()
            .baseUrl(base_URL2)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson2))
            .client(createOkHttpClient2())
            .build()
}
