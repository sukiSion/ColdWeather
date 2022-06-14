package com.coldweather.android.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.coldweather.android.databinding.PlaceItemBinding
import com.coldweather.android.logic.model.Place
import com.coldweather.android.ui.weather.WeatherActivity
import  com.coldweather.android.ui.weather.*

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

            val intent = Intent(parent.context,WeatherActivity::class.java).apply {

                putExtra("place_name",place.name)

                putExtra("location_lng",place.location.lng)

                putExtra("location_lat",place.location.lat)

            }

            fragment.viewModel.savePlace(place)

            fragment.startActivity(intent)

            fragment.activity?.finish()

        }

        return holder

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val place = places[position]

        holder.placeName.text = place.name

        holder.placeAddress.text = place.address

    }




}