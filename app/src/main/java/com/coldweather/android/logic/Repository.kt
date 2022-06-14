package com.coldweather.android.logic

import androidx.lifecycle.liveData
import com.coldweather.android.logic.Dao.PlaceDao
import com.coldweather.android.logic.model.Place
import com.coldweather.android.logic.model.Weather
import com.coldweather.android.logic.network.ColdWeatherNetWork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

object Repository {

    // 下面三行代码建议开一个线程执行而不是执行在主线程中
    // 因为这只是sharedPreferences的读写操作
    // 当使用数据库并且数据库中有大量的数据时必须开启线程
    // 正常写法：开线程执行任务并且使用LiveData观察返回值以对数据进行返回
    fun savePlace(place: Place) = PlaceDao.savePalce(place)

    fun getSavedPalce() = PlaceDao.getPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

    fun searchPlaces(query:String) = fire(Dispatchers.IO) {

            val placeResponse = ColdWeatherNetWork.searchPlaces(query)

            if(placeResponse.status == "ok"){

                val places = placeResponse.places

                Result.success(places)

            }else{

                Result.failure(RuntimeException("respone status is ${placeResponse.status}"))

            }

        }

    fun refreshWeather(lng:String,lat:String) = fire(Dispatchers.IO) {

        coroutineScope {

            val deferredRealtime = async {

                ColdWeatherNetWork.getRealtimeWeather(lng, lat)

            }

            val deferredDaily = async {

                ColdWeatherNetWork.getDailyWeather(lng, lat)
            }

            val realrimeResponse = deferredRealtime.await()

            val dailyResponse = deferredDaily.await()


            if(realrimeResponse.status == "ok" && dailyResponse.status == "ok"){

                val weather = Weather(realrimeResponse.result.realtime,dailyResponse.result.daily)

                Result.success(weather)

            }else{

                Result.failure(RuntimeException("" +
                        "realtime response status is ${realrimeResponse.status}" +
                        "daily response status is ${dailyResponse.status}"))

            }

        }

    }

    private fun <T> fire(coroutineContext: CoroutineContext,block:suspend () -> Result<T>)
            = liveData<Result<T>> {

        val result = try{

            block()

        }catch (e:Exception){

            Result.failure<T>(e)

        }

        emit(result)

    }


}







