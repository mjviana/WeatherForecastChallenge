package com.mobiweb.challenge.weatherforecast.ui.fragments

import android.graphics.Color
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
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
    private lateinit var lineChart: LineChart
    private var lineChartList = ArrayList<Entry>()
    private lateinit var lineChartDataSet: LineDataSet
    private lateinit var lineChartData: LineData

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
        lineChart = binding.weatherChart

        weatherForecastViewModel.currentWeatherData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideCurrentTemperatureWeatherForecastProgressBar()
                    response.data?.let { currentWeatherData ->
                        binding.currentTemperatureValue.text =
                            currentWeatherData.main.temp.roundToInt().toString() + "º"
                    }
                }
                is Resource.Error -> {
                    hideCurrentTemperatureWeatherForecastProgressBar()
                    response.message?.let { message ->
                        displayError(message)
                    }
                }
                is Resource.Loading -> {
                    showCurrentTemperatureWeatherForecastProgressBar()
                }
            }
        })

        weatherForecastViewModel.weatherForecast.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    hideWeatherForecastProgressBar()
                    response.data?.let { weatherForecast ->
                        val details = weatherForecast.details

                        // get the dates in weatherForecast response
                        val dates = mutableListOf<LocalDateTime>()
                        for (detail in details) {
                            dates.add(detail.date)
                        }
                        val days = dates.distinctBy { it.dayOfMonth } as MutableList<LocalDateTime>
                        days.removeAt(0)

                        // Set up the weather details for the current day
                        val todayDetails =
                            details.filter { it.date.dayOfYear == LocalDateTime.now().dayOfYear }
                        todayDetails.forEach {
                            lineChartList.add(
                                Entry(
                                    it.date.hour.toFloat(),
                                    it.main.temp.roundToInt().toFloat()
                                )
                            )
                        }
                        setupWeatherChart()
                        weatherForecastAdapter.setData(days, details)
                    }
                }
                is Resource.Error -> {
                    hideWeatherForecastProgressBar()
                    response.message?.let { message ->
                        displayError(message)
                    }
                }
                is Resource.Loading -> {
                    showWeatherForecastProgressBar()
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

    private fun setupWeatherChart() {
        lineChartDataSet = LineDataSet(lineChartList, "Today's Temperature")
        lineChartData = LineData(lineChartDataSet)

        lineChart.data = lineChartData
        lineChartDataSet.color = Color.BLUE
        lineChartDataSet.valueTextColor = Color.BLUE
        lineChartDataSet.valueTextSize = 12f

        val chartDescription = lineChart.description
        chartDescription.isEnabled = true
        chartDescription.text = "X -> Time. Y -> Temperature"

        val xAxis: XAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)

        val yRightAxis: YAxis = lineChart.axisRight
        yRightAxis.textSize = 12f
        yRightAxis.textColor = Color.BLACK
        yRightAxis.setDrawAxisLine(false)
        yRightAxis.setDrawGridLines(false)

        val yLeftAxis: YAxis = lineChart.axisLeft
        yLeftAxis.isEnabled = false

        lineChart.invalidate()
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

    private fun showCurrentTemperatureWeatherForecastProgressBar() {
        binding.progressBarCurrentTemperature.visibility = View.VISIBLE
        binding.weatherPredictionList.visibility = View.GONE
    }

    private fun showWeatherForecastProgressBar() {
        binding.currentTemperatureValue.visibility = View.GONE
        binding.progressBarWeatherForecast.visibility = View.VISIBLE
    }

    private fun hideWeatherForecastProgressBar() {
        binding.currentTemperatureValue.visibility = View.VISIBLE
        binding.progressBarWeatherForecast.visibility = View.GONE
    }

    private fun hideCurrentTemperatureWeatherForecastProgressBar() {
        binding.progressBarCurrentTemperature.visibility = View.GONE
        binding.weatherPredictionList.visibility = View.VISIBLE
    }
}