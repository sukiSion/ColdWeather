package com.coldweather.android.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityWeatherBinding.inflate(layoutInflater)

        setContentView(binding.root)


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

        viewModel.weatherLiveData.observe(this){

            result ->

            val weather = result.getOrNull()

            if(weather!= null){

                showWeatherInfo(weather)

            }else{

                Toast.showToast("无法成功获取天气信息")

                // 如果获取的结果是异常就显示出来
                // 否则不显示
                result.exceptionOrNull()?.printStackTrace()

            }

        }

        viewModel.resfreshWeather(viewModel.locationLng,viewModel.locationLat)
        //用来出发liveData

    }

    private fun showWeatherInfo(weather: Weather){

        placeName?.text = viewModel.placeName

        val realtime = weather.realtime

        val daily = weather.daily

        //我们需要先填充now.xml的布局

        val currentTempText = "${realtime.temperature.toInt()}"

        currentTemp?.text = currentTempText

        currentSky?.text = getSky(realtime.skycon).info

        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"

        currentAQI?.text = currentPM25Text

        nowLayout?.setBackgroundResource(getSky(realtime.skycon).bg)

        // 填充forecast.xml布局

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

        // 填充life_index.xml的数据
        val lifeIndex = daily.lifeindex

        coldRiskText?.text = lifeIndex.coldRisk[0].desc

        dressingText?.text = lifeIndex.dressing[0].desc

        ultravioletText?.text = lifeIndex.ultraviolet[0].desc

        carwashingText?.text = lifeIndex.carWashing[0].desc

        weatherLayout?.visibility = View.VISIBLE

    }

}