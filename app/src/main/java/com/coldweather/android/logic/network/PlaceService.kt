package com.coldweather.android.logic.network

import com.coldweather.android.ColdWeatherApplication
import com.coldweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceService {

    @GET("v2/place?token=${ColdWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlace(@Query("query") query: String):Call<PlaceResponse>

}