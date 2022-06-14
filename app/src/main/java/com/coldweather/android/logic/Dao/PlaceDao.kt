package com.coldweather.android.logic.Dao

import android.content.Context
import androidx.core.content.edit
import com.coldweather.android.ColdWeatherApplication
import com.coldweather.android.logic.model.Place
import com.google.gson.Gson

object PlaceDao {

    fun sharedPreferences() = ColdWeatherApplication.context.getSharedPreferences("cold_weather",
    Context.MODE_PRIVATE)

    fun savePalce(place: Place){

        sharedPreferences().edit {

            putString("place",Gson().toJson(place))

        }

    }

    fun getPlace():Place
    = Gson().fromJson(sharedPreferences().getString("place",""),Place::class.java)

    fun isPlaceSaved() = sharedPreferences().contains("place")

}