package com.coldweather.android

import android.widget.Toast

class Toast {

    fun showToast(text:String,duration: Int = Toast.LENGTH_SHORT){

        Toast.makeText(ColdWeatherApplication.context,text,duration).show()

    }

}