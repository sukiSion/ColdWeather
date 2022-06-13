package com.coldweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class ColdWeatherApplication:Application() {

    companion object{

        //方便以后我们获取申请API的令牌
        const val TOKEN = "9R2DtYaZ9SjaL1RR"


        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

    }

    override fun onCreate() {

        super.onCreate()

        context = applicationContext

    }

}