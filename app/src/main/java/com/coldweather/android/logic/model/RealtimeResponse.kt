package com.coldweather.android.logic.model

import com.google.gson.annotations.SerializedName


//这里将所有的数据模型都定义在了RealtimeResponse的内部
//防止与其他数据模型有同名冲突的情况
data class RealtimeResponse(val status:String , val result:Result){

    data class Result(val realtime: Realtime)

    data class Realtime(val skycon:String,val temperature:Float,@SerializedName("air_quality")val airQuality: AirQuality)

    data class AirQuality(val aqi:AQI)

    data class AQI(val chn:Float)

}
