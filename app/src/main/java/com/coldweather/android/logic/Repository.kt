package com.coldweather.android.logic

import androidx.lifecycle.liveData
import com.coldweather.android.logic.model.Place
import com.coldweather.android.logic.network.ColdWeatherNetWork
import kotlinx.coroutines.Dispatchers
import java.lang.RuntimeException

object Repository {

    fun searchPlaces(query:String) = liveData(Dispatchers.IO) {

        val result = try{

            val placeResponse = ColdWeatherNetWork().searchPlaces(query)

            if(placeResponse.status == "ok"){

                val places = placeResponse.places

                Result.success(places)

            }else{

                Result.failure(RuntimeException("respone status is ${placeResponse.status}"))

            }

        }catch (e:Exception){

            Result.failure<List<Place>>(e)

        }

        emit(result)

    }

}