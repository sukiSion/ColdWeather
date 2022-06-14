package com.coldweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coldweather.android.logic.model.Place
import com.coldweather.android.ui.weather.WeatherActivity
import com.coldweather.android.databinding.PlaceItemBinding

// 这里的fragment改成placeFragment可以直接调用该viewModel
class PlaceAdapter(private val places: List<Place>,private val fragment: placeFragment):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


    inner class ViewHolder(binding: PlaceItemBinding):RecyclerView.ViewHolder(binding.root){

        val placeName  = binding.placeName

        val placeAddress = binding.placeAddress

    }

    override fun getItemCount() = places.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        val holder = ViewHolder(binding)

        holder.itemView.setOnClickListener {

            val position = holder.bindingAdapterPosition

            val place = places[position]

            val activity = fragment.activity

            //这是绑定在WeatherActivity上执行的代码
            if(activity is WeatherActivity){

                activity.drawLayout?.closeDrawers()

                activity.viewModel.locationLng = place.location.lng

                activity.viewModel.locationLat = place.location.lat

                activity.viewModel.placeName = place.name

                //这里选择新的城市应该出现下拉刷新
                //这样子界面比较好看
                activity.refreshWeather()

               //这里绑定在MainActivity上执行的代码
            }else{


                val intent = Intent(parent.context,WeatherActivity::class.java).apply {

                    putExtra("place_name",place.name)

                    putExtra("loaction_lng",place.location.lng)

                    putExtra("location_lat",place.location.lat)


                }

                fragment.startActivity(intent)

                activity?.finish()

            }

            //不管是哪个Activity，都应还将place保存到sharedPreferences中
            fragment.viewModel.savePlace(place)

        }

        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val place = places[position]

        holder.placeName.text = place.name

        holder.placeAddress.text = place.address

    }




}