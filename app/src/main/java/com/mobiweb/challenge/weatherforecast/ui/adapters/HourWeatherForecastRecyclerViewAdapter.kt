package com.mobiweb.challenge.weatherforecast.ui.adapters

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.mobiweb.challenge.weatherforecast.R
import com.mobiweb.challenge.weatherforecast.databinding.HourWeatherForecastItemBinding
import com.mobiweb.challenge.weatherforecast.models.weatherForecastModels.Details
import kotlin.math.roundToInt

class HourWeatherForecastRecyclerViewAdapter(private val detailsList: List<Details>) :
    RecyclerView.Adapter<HourWeatherForecastRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: HourWeatherForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HourWeatherForecastItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPosition = detailsList[position]
        holder.binding.hourValueLbl.text = currentPosition.date.hour.toString()
        holder.binding.temperatureValueLbl.text =
            currentPosition.main.temp.roundToInt().toString() + "º"

        Log.i("TAG", "onBindViewHolder: ${currentPosition.date}")

        setupWeatherIcon(holder, currentPosition)
    }

    override fun getItemCount() = detailsList.size

    /**
     * Sets the weather icon according to the hour
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupWeatherIcon(
        holder: HourWeatherForecastRecyclerViewAdapter.ViewHolder,
        currentPosition: Details
    ) {
        if (currentPosition.date.hour in 7..20)
            holder.binding.icWeatherState.setImageResource(R.drawable.ic_day)
        else
            holder.binding.icWeatherState.setImageResource(R.drawable.ic_night)
    }
}
