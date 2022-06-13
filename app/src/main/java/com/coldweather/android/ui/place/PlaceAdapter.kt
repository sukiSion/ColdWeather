package com.coldweather.android.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.coldweather.android.databinding.FragmentPlaceBinding
import com.coldweather.android.databinding.PlaceItemBinding
import com.coldweather.android.logic.model.Place

class PlaceAdapter(private val places: List<Place>,private val fragment: Fragment):RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {


    inner class ViewHolder(binding: PlaceItemBinding):RecyclerView.ViewHolder(binding.root){

        val placeName  = binding.placeName

        val placeAddress = binding.placeAddress

    }

    override fun getItemCount() = places.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val place = places[position]

        holder.placeName.text = place.name

        holder.placeAddress.text = place.address

    }




}