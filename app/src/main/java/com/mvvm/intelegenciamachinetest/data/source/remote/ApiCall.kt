package com.mvvm.intelegenciamachinetest.data.source.remote

import com.mvvm.intelegenciamachinetest.data.source.model.WeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiCall {

    @GET("data/2.5/weather")
    suspend fun getUserData(@Query("lat") lat: String?,
                            @Query("lon") lon: String?,
                            @Query("appid") appid: String?): Response<WeatherResponse>

    companion object {
        operator fun invoke(): ApiCall {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/")
                .client(client)
                .build()
                .create(ApiCall::class.java)
        }
    }
}