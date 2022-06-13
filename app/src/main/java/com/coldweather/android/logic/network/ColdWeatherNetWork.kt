package com.coldweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ColdWeatherNetWork {

    //直接创建一个动代对象
    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlace(query).await()


    private suspend fun <T> Call<T>.await():T{

        return suspendCoroutine {

            continuation ->

            enqueue(object :Callback<T>{

                override fun onResponse(call: Call<T>, response: Response<T>) {

                    val body = response.body()

                    if(body!= null)

                        continuation.resume(body)

                     else
                         continuation.resumeWithException(RuntimeException("response body in null"))



                }

                override fun onFailure(call: Call<T>, t: Throwable) {

                    continuation.resumeWithException(t)

                }

            })

        }

    }


}




