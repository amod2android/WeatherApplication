package com.amod2android.weatherapplication.network


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroServiceInterface {


    @GET("data/2.5/weather")
    fun getCurrentWeather(
            @Query("lat") lat: Double,
            @Query("lon") lng: Double,
            @Query("appid") appid: String): Call<String>

    @GET("data/2.5/forecast")
    fun getWeatherForCast(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") appid: String): Call<String>
}