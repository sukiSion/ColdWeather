package com.coldweather.android.ui.place


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coldweather.android.MainActivity
import com.coldweather.android.Toast
import com.coldweather.android.databinding.FragmentPlaceBinding
import com.coldweather.android.ui.weather.WeatherActivity

class placeFragment:Fragment() {

    private var _binding:FragmentPlaceBinding?= null

    val binding
    get() = _binding

    private var recyclerView:RecyclerView?= null

    private var bgImageView:ImageView? =null

    private var searchPalceEdit:EditText? = null

    private lateinit var adapter: PlaceAdapter

    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?  {

        _binding = FragmentPlaceBinding.inflate(inflater,container,false)

        val view = binding?.root

        recyclerView = binding?.recyclerView

        bgImageView = binding?.bgImageView

        searchPalceEdit = binding?.searchPlaceEdit

        return view

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        if(activity is MainActivity && viewModel.isSavedPlace()){

            val place = viewModel.getSavedPlace()

            val intent = Intent(this.context,WeatherActivity::class.java).apply {

                putExtra("location_lng",place.location.lng)

                putExtra("location_lat",place.location.lat)

                putExtra("place_name",place.name)

            }

            startActivity(intent)

            activity?.finish()

            return

        }

        val layoutManager = LinearLayoutManager(activity)

        recyclerView?.layoutManager = layoutManager

        adapter = PlaceAdapter(viewModel.placeList, this)

        recyclerView?.adapter = adapter

        searchPalceEdit?.addTextChangedListener {

                editable ->

            val content = editable.toString().trim()

            if (content.isNotEmpty()) {

                viewModel.searchPlaces(content)

            } else {

                recyclerView?.visibility = View.GONE

                bgImageView?.visibility = View.VISIBLE

                viewModel.placeList.clear()

                adapter.notifyDataSetChanged()

            }

        }

        viewModel.placeLiveData.observe(this.viewLifecycleOwner) {

                result ->

            val places = result.getOrNull()

            if (places != null) {

                recyclerView?.visibility = View.VISIBLE

                bgImageView?.visibility = View.GONE

                viewModel.placeList.clear()

                viewModel.placeList.addAll(places)

                adapter.notifyDataSetChanged()

            } else {

                Toast.showToast("未能查询到任何地点")

                result.exceptionOrNull()?.printStackTrace()

            }

        }
    }

}
