package com.coldweather.android.logic.network

import com.coldweather.android.ColdWeatherApplication
import com.coldweather.android.logic.model.DailyResponse
import com.coldweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("v2.6/${ColdWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng")lng:String,@Path("lat")lat:String):Call<RealtimeResponse>

    @GET("v2.6/${ColdWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng")lng: String,@Path("lat")lat: String):Call<DailyResponse>

}