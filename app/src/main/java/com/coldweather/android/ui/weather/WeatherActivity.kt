package com.coldweather.android.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.coldweather.android.R
import com.coldweather.android.Toast
import com.coldweather.android.databinding.ActivityWeatherBinding
import com.coldweather.android.databinding.ForecastItemBinding
import com.coldweather.android.databinding.NowBinding
import com.coldweather.android.logic.model.Weather
import com.coldweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    private var placeName:TextView?=null

    private var currentTemp:TextView?= null

    private var currentSky:TextView?=null

    private var currentAQI:TextView?= null

    private var nowLayout:RelativeLayout?=null

    private var forecastLayout:LinearLayoutCompat?=null

    private var lifeIndexLayout:LinearLayoutCompat?=null

    private var coldRiskText:TextView?=null

    private var dressingText:TextView?=null

    private var ultravioletText:TextView?=null

    private var carwashingText:TextView?=null

    private var weatherLayout:ScrollView?=null

    private var swipeRefresh:SwipeRefreshLayout?=null

    var drawLayout:DrawerLayout?=null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)

         drawLayout = binding.drawerLayout

        swipeRefresh = binding.swipeRefresh

        placeName = binding.NowLayout.palceName

        currentTemp = binding.NowLayout.currentTemp

        currentSky = binding.NowLayout.currentSky

        currentAQI = binding.NowLayout.currentAQI

        nowLayout = binding.NowLayout.nowLayout

        forecastLayout = binding.ForecastLayout.forecastLayout

        lifeIndexLayout = binding.LifeIndexLayout.LifeIndexLayout

        coldRiskText = binding.LifeIndexLayout.coldRiskText

        dressingText = binding.LifeIndexLayout.dressingText

        ultravioletText = binding.LifeIndexLayout.ultravioletText

        carwashingText = binding.LifeIndexLayout.carWashingText

        weatherLayout = binding.weatherLayout

        if(viewModel.locationLng.isEmpty()){

            viewModel.locationLng = intent.getStringExtra("location_lng")?:""

        }

        if(viewModel.locationLat.isEmpty()){

            viewModel.locationLat = intent.getStringExtra("location_lat")?:""

        }

        if(viewModel.placeName.isEmpty()){

            viewModel.placeName = intent.getStringExtra("place_name")?:""

        }

        binding.NowLayout.navBtn.setOnClickListener {

            drawLayout?.openDrawer(GravityCompat.START)

        }

        drawLayout?.addDrawerListener(object :DrawerLayout.DrawerListener{

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerOpened(drawerView: View) {}

            //???????????????????????????????????????????????????
            override fun onDrawerClosed(drawerView: View) {

                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                //????????????????????????View????????????????????????????????????
                //???????????????InputMethodManager???????????????
                manager.hideSoftInputFromWindow(drawerView.windowToken,InputMethodManager.HIDE_NOT_ALWAYS)


            }



        })

        viewModel.weatherLiveData.observe(this){

            result ->

            val weather = result.getOrNull()

            if(weather!= null){

                showWeatherInfo(weather)

            }else{

                Toast.showToast("??????????????????????????????")

                // ?????????????????????????????????????????????
                // ???????????????
                result.exceptionOrNull()?.printStackTrace()

            }

            swipeRefresh?.isRefreshing = false

        }

        swipeRefresh?.setColorSchemeColors(R.color.colorPrimary)

        swipeRefresh?.setOnRefreshListener {

            refreshWeather()

        }

        viewModel.resfreshWeather(viewModel.locationLng,viewModel.locationLat)
        //????????????liveData

    }

    private fun showWeatherInfo(weather: Weather){

        placeName?.text = viewModel.placeName

        val realtime = weather.realtime

        val daily = weather.daily

        //?????????????????????now.xml?????????

        val currentTempText = "${realtime.temperature.toInt()}"

        currentTemp?.text = currentTempText

        currentSky?.text = getSky(realtime.skycon).info

        val currentPM25Text = "???????????? ${realtime.airQuality.aqi.chn.toInt()}"

        currentAQI?.text = currentPM25Text

        nowLayout?.setBackgroundResource(getSky(realtime.skycon).bg)

        // ??????forecast.xml??????

        forecastLayout?.removeAllViews()

        val days = daily.skycon.size

        for(i in 0 until days){

            val skycon = daily.skycon[i]

            val temperature = daily.temperature[i]

            val forecastItem  = ForecastItemBinding
                .inflate(LayoutInflater.from(this),forecastLayout,false)

            val dateInfo =  forecastItem.dataInfo

            val skyIcon = forecastItem.skyIcon

            val skyInfo = forecastItem.skyInfo

            val temperatureInfo = forecastItem.temperatureInfo

            val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            dateInfo.text = simpleDataFormat.format(skycon.date)

            val sky = getSky(skycon.value)

            skyIcon.setImageResource(sky.icon)

            skyInfo.text = sky.info

            val tempText = "${temperature.max.toInt()} ~ ${temperature.min.toInt()}"

            temperatureInfo.text = tempText

            forecastLayout?.addView(forecastItem.root)

        }

        // ??????life_index.xml?????????
        val lifeIndex = daily.lifeindex

        coldRiskText?.text = lifeIndex.coldRisk[0].desc

        dressingText?.text = lifeIndex.dressing[0].desc

        ultravioletText?.text = lifeIndex.ultraviolet[0].desc

        carwashingText?.text = lifeIndex.carWashing[0].desc

        weatherLayout?.visibility = View.VISIBLE

    }

     fun refreshWeather(){

        //???????????????LiveData
        //????????????????????????LiveData?????????
        //???????????????????????????????????????????????????
        viewModel.resfreshWeather(viewModel.locationLng,viewModel.locationLat)

        //?????????????????????
        swipeRefresh?.isRefreshing = true

    }

}