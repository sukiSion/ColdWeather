package com.coldweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.coldweather.android.logic.Repository
import com.coldweather.android.logic.model.Place
import retrofit2.http.Query

class PlaceViewModel:ViewModel() {

    val placeList = ArrayList<Place>()

    private val searchLiveData = MutableLiveData<String>()

    fun searchPlaces(query: String){

        searchLiveData.value = query

    }

    //只要searchPlaces被调用，searchLiveData值发生变化
    //就会执行里面的代码块
    val placeLiveData = Transformations.switchMap(searchLiveData){

        query ->

        Repository.searchPlaces(query)

    }

}