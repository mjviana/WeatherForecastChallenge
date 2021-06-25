package com.mobiweb.challenge.weatherforecast.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mobiweb.challenge.weatherforecast.databinding.FragmentWeatherForecastBinding
import com.mobiweb.challenge.weatherforecast.ui.adapters.DayWeatherForecastRecyclerViewAdapter
import com.mobiweb.challenge.weatherforecast.ui.viewModels.WeatherForecastViewModel
import com.mobiweb.challenge.weatherforecast.utilities.Resource
import java.time.LocalDateTime
import kotlin.math.roundToInt


class WeatherForecastFragment : Fragment() {

    private lateinit var binding: FragmentWeatherForecastBinding
    private lateinit var weatherForecastViewModel: WeatherForecastViewModel
    private lateinit var weatherForecastRecyclerView: RecyclerView
    private lateinit var weatherForecastAdapter: DayWeatherForecastRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherForecastViewModel = ViewModelProvider(this).get(WeatherForecastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        weatherForecastViewModel.currentWeatherData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { currentWeatherData ->
                        binding.currentTemperatureValue.text =
                            currentWeatherData.main.temp.roundToInt().toString() + "º"
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        displayError(message)
                    }
                }
            }
        })

        weatherForecastViewModel.weatherForecast.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let { weatherForecast ->
                        val details = weatherForecast.details
                        val dates = mutableListOf<LocalDateTime>()
                        for (detail in details) {
                            dates.add(detail.date)
                        }
                        val days = dates.distinctBy { it.dayOfMonth } as MutableList<LocalDateTime>
                        days.removeAt(0)
                        weatherForecastAdapter.setData(days, details)
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        displayError(message)
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() {
        weatherForecastAdapter = DayWeatherForecastRecyclerViewAdapter()
        weatherForecastRecyclerView = binding.weatherPredictionList
        weatherForecastRecyclerView.adapter = weatherForecastAdapter
        weatherForecastRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        weatherForecastRecyclerView.setHasFixedSize(true)
    }

    private fun displayError(message: String) {
        MaterialAlertDialogBuilder(context as FragmentActivity)
            .setTitle("UPS!")
            .setMessage("Something went wrong: $message")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, which ->
            }
            .show()
    }
}