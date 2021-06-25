package com.mobiweb.challenge.weatherforecast.ui.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobiweb.challenge.weatherforecast.databinding.DayWeatherForecastItemBinding
import com.mobiweb.challenge.weatherforecast.models.weatherForecastModels.Details
import java.time.LocalDateTime


class DayWeatherForecastRecyclerViewAdapter() :
    RecyclerView.Adapter<DayWeatherForecastRecyclerViewAdapter.ViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    var days = mutableListOf<LocalDateTime>()
    var detailsList = listOf<Details>()

    inner class ViewHolder(val binding: DayWeatherForecastItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DayWeatherForecastItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = days[position]
        if (position == 0) {
            holder.binding.dateLbl.text =
                "Tomorrow, ${currentItem.month} ${currentItem.dayOfMonth}"
        } else
            holder.binding.dateLbl.text =
                "${currentItem.dayOfWeek}, ${currentItem.month} ${currentItem.dayOfMonth}"

        // Sets the recycler view that displays the weather prediction until the end of the day
        holder.binding.dayWeatherPredictionList.apply {
            layoutManager  = LinearLayoutManager(
                holder.binding.dayWeatherPredictionList.context,
                RecyclerView.HORIZONTAL,
                false
            )
            val dayDetails = detailsList.filter { it.date.dayOfYear == currentItem.dayOfYear }
            adapter = HourWeatherForecastRecyclerViewAdapter(dayDetails)
            setRecycledViewPool(viewPool)
        }
    }

    override fun getItemCount() = days.size

    fun setData(newData: MutableList<LocalDateTime>, detailsList: List<Details>) {
        days = newData
        this.detailsList = detailsList
        notifyDataSetChanged()
    }

}