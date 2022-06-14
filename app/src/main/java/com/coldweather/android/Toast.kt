package com.coldweather.android

import android.widget.Toast

object Toast {

    fun showToast(text:String,duration: Int = Toast.LENGTH_SHORT){

        Toast.makeText(ColdWeatherApplication.context,text,duration).show()

    }

}