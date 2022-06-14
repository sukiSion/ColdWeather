package com.coldweather.android.ui.weather


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.coldweather.android.logic.Repository
import com.coldweather.android.logic.model.Location
import com.coldweather.android.logic.model.Place

class WeatherViewModel:ViewModel() {

    private val localtionLiveData = MutableLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    fun resfreshWeather(lng:String,lat:String) {

        localtionLiveData.value = Location(lng,lat)

    }

    val weatherLiveData = Transformations.switchMap(localtionLiveData){

        location ->

        Repository.refreshWeather(location.lng,location.lat)

    }

}